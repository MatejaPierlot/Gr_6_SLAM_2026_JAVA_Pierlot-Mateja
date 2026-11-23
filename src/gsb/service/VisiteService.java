package gsb.service;

import gsb.modele.Medecin;
import gsb.modele.Visite;
import gsb.modele.Visiteur;
import gsb.modele.dao.MedecinDao;
import gsb.modele.dao.VisiteDao;
import gsb.utils.ValidationUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import gsb.modele.Medicament;
import gsb.modele.dao.StockDao;

/**
 * Contrôles et règles de gestion des visites.
 */
public class VisiteService {

    /**
     * Recherche une visite après avoir contrôlé la référence saisie.
     * @throws Exception si la référence est vide ou inconnue
     */
    public static Visite rechercherVisite(String reference) throws Exception {
        if (reference == null || reference.trim().isEmpty()) {
            throw new Exception("La référence est obligatoire.");
        }
        Visite uneVisite = VisiteDao.rechercher(reference.trim().toLowerCase());
        if (uneVisite == null) {
            throw new Exception("Aucune visite ne correspond à la référence " + reference.trim() + ".");
        }
        return uneVisite;
    }

    /**
     * Retourne les visites d'un visiteur à une date saisie (jj/mm/aaaa).
     * @throws Exception si le visiteur est inconnu ou si la date est invalide
     */
    public static ArrayList<Visite> retournerVisitesDuVisiteurALaDate(String matricule, String date) throws Exception {
        Visiteur v = VisiteurService.rechercherVisiteur(matricule);
        return VisiteDao.retournerVisitesDuVisiteurALaDate(v.getMatricule(), convertirDate(date));
    }

    /**
     * Contrôle les données saisies puis crée une visite planifiée
     * (sans commentaire ni médicament offert).
     * @throws Exception si une donnée est invalide ou si l'ajout échoue
     */
    public static void creerVisite(String reference, String date, String matricule, String codeMedecin) throws Exception {
        // Référence : obligatoire, au format v0000, pas déjà utilisée  (test de l'Annexe 9.1)
        reference = reference == null ? "" : reference.trim().toLowerCase();
        if (reference.isEmpty()) {
            throw new Exception("La référence est obligatoire.");
        }
        if (!reference.matches("^v[0-9]{4}$")) {
            throw new Exception("La référence doit être au format v suivi de 4 chiffres (ex. v0024).");
        }
        if (VisiteDao.rechercher(reference) != null) {
            throw new Exception("La référence " + reference + " existe déjà.");
        }

        // Date : valide et pas dans le passé (planning)
        Date laDate = convertirDate(date);
        if (laDate.before(aujourdhui())) {
            throw new Exception("La date de visite ne peut pas être dans le passé.");
        }

        // Visiteur : doit exister  (test de l'Annexe 9.1)
        Visiteur leVisiteur = VisiteurService.rechercherVisiteur(matricule);

        // Médecin : doit exister  (test de l'Annexe 9.1)
        if (codeMedecin == null || codeMedecin.trim().isEmpty()) {
            throw new Exception("Le code médecin est obligatoire.");
        }
        Medecin leMedecin = MedecinDao.rechercher(codeMedecin.trim().toLowerCase());
        if (leMedecin == null) {
            throw new Exception("Aucun médecin ne correspond au code " + codeMedecin.trim() + ".");
        }

        Visite uneVisite = new Visite(reference, laDate, leVisiteur, leMedecin);   // visite planifiée
        if (VisiteDao.creer(uneVisite) != 1) {
            throw new Exception("L'ajout de la visite a échoué.");
        }
    }

    /** Contrôle une date saisie (jj/mm/aaaa) et la convertit. */
    private static Date convertirDate(String date) throws Exception {
        if (date == null || !ValidationUtils.isDateValide(date.trim())) {
            throw new Exception("La date de visite doit être au format jj/mm/aaaa.");
        }
        return new SimpleDateFormat("dd/MM/yyyy").parse(date.trim());
    }

    /** La date du jour, à minuit (pour comparer des jours, sans les heures). */
    private static Date aujourdhui() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
    
    /** Nombre maximum de caractères du commentaire (colonne varchar(100)). */
    public static final int COMMENTAIRE_MAX = 100;

    /**
     * Enregistre une visite effectuée : commentaire et jusqu'à 2 médicaments offerts,
     * puis retire les échantillons du stock du visiteur.
     * Un dépôt légal vide veut dire « pas de médicament ».
     * @throws Exception si une règle n'est pas respectée ou si une mise à jour échoue
     */
    public static void effectuerVisite(String reference, String commentaire,
            String depot1, String quantite1, String depot2, String quantite2) throws Exception {

        // 1. La visite doit exister et ne pas être déjà effectuée
        Visite laVisite = rechercherVisite(reference);
        if (laVisite.getCommentaire() != null && !laVisite.getCommentaire().trim().isEmpty()) {
            throw new Exception("La visite " + laVisite.getReference() + " a déjà été effectuée.");
        }

        // 2. Le commentaire est obligatoire
        commentaire = commentaire == null ? "" : commentaire.trim();
        if (commentaire.isEmpty()) {
            throw new Exception("Le commentaire est obligatoire.");
        }
        if (commentaire.length() > COMMENTAIRE_MAX) {
            throw new Exception("Le commentaire fait " + COMMENTAIRE_MAX + " caractères au maximum.");
        }

        // 3. Les médicaments offerts (facultatifs)
        String matricule = laVisite.getUnVisiteur().getMatricule();
        Medicament m1 = null, m2 = null;
        int q1 = 0, q2 = 0;
        if (depot1 != null && !depot1.trim().isEmpty()) {
            m1 = MedicamentService.rechercherMedicament(depot1);
            q1 = controlerQuantite(quantite1, "1", matricule, m1);
        }
        if (depot2 != null && !depot2.trim().isEmpty()) {
            m2 = MedicamentService.rechercherMedicament(depot2);
            if (m1 != null && m1.getDepotLegal().equals(m2.getDepotLegal())) {
                throw new Exception("Le même médicament ne peut pas être offert deux fois : regroupez la quantité.");
            }
            q2 = controlerQuantite(quantite2, "2", matricule, m2);
        }

        // 4. Tout est contrôlé : on écrit
        laVisite.setCommentaire(commentaire);
        laVisite.setMedicamentOffert1(m1);
        laVisite.setQuantiteOfferte1(q1);
        laVisite.setMedicamentOffert2(m2);
        laVisite.setQuantiteOfferte2(q2);
        if (VisiteDao.modifier(laVisite) != 1) {
            throw new Exception("La mise à jour de la visite a échoué.");
        }
        if (m1 != null && StockDao.retirerQuantite(matricule, m1.getDepotLegal(), q1) != 1) {
            throw new Exception("Le stock de " + m1.getNomCommercial() + " n'a pas pu être mis à jour.");
        }
        if (m2 != null && StockDao.retirerQuantite(matricule, m2.getDepotLegal(), q2) != 1) {
            throw new Exception("Le stock de " + m2.getNomCommercial() + " n'a pas pu être mis à jour.");
        }
    }

    /** Contrôle une quantité offerte : entier > 0 et pas plus que le stock du visiteur. */
    private static int controlerQuantite(String quantite, String numero, String matricule, Medicament m) throws Exception {
        int q;
        try {
            q = Integer.parseInt(quantite == null ? "" : quantite.trim());
        } catch (NumberFormatException e) {
            throw new Exception("La quantité du médicament " + numero + " doit être un nombre entier.");
        }
        if (q <= 0) {
            throw new Exception("La quantité du médicament " + numero + " doit être supérieure à 0.");
        }
        int enStock = StockDao.quantiteEnStock(matricule, m.getDepotLegal());
        if (q > enStock) {
            throw new Exception("Stock insuffisant pour " + m.getNomCommercial()
                    + " : " + enStock + " en stock, " + q + " demandé(s).");
        }
        return q;
    }
}