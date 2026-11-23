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
}