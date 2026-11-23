package gsb.service;

import gsb.modele.Localite;
import gsb.modele.Visiteur;
import gsb.modele.dao.LocaliteDao;
import gsb.modele.dao.VisiteurDao;
import gsb.utils.ValidationUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Contrôles de saisie et règles de gestion pour les visiteurs.
 */
public class VisiteurService {

    /**
     * Recherche un visiteur après avoir contrôlé le matricule saisi.
     * @param matricule le matricule saisi
     * @return le visiteur trouvé
     * @throws Exception si le matricule est vide ou inconnu
     */
    public static Visiteur rechercherVisiteur(String matricule) throws Exception {
        if (matricule == null || matricule.trim().isEmpty()) {
            throw new Exception("Le matricule est obligatoire.");
        }
        Visiteur unVisiteur = VisiteurDao.rechercher(matricule.trim());
        if (unVisiteur == null) {
            throw new Exception("Aucun visiteur ne correspond au matricule " + matricule.trim() + ".");
        }
        return unVisiteur;
    }

    /** @return tous les visiteurs, triés par nom puis prénom */
    public static ArrayList<Visiteur> retournerLesVisiteurs() {
        return VisiteurDao.retournerCollectionDesVisiteurs();
    }

    /**
     * Contrôle les champs saisis (tous en texte), les convertit, puis ajoute le visiteur.
     * @throws Exception si une donnée est invalide ou si l'ajout échoue
     */
    public static void creerVisiteur(String matricule, String nom, String prenom, String login,
            String mdp, String adresse, String codePostal, String telephone, String dateEntree,
            String prime, String codeUnite, String nomUnite) throws Exception {

        matricule = nettoyer(matricule);
        nom = nettoyer(nom);
        prenom = nettoyer(prenom);
        login = nettoyer(login);
        mdp = mdp == null ? "" : mdp;          // pas de trim : les espaces font partie du mot de passe
        adresse = nettoyer(adresse);
        codePostal = nettoyer(codePostal);
        telephone = nettoyer(telephone);
        dateEntree = nettoyer(dateEntree);
        prime = nettoyer(prime);
        codeUnite = nettoyer(codeUnite).toUpperCase();
        nomUnite = nettoyer(nomUnite);

        // Matricule
        obligatoire(matricule, "Le matricule");
        longueurMax(matricule, 4, "Le matricule");
        if (VisiteurDao.rechercher(matricule) != null) {
            throw new Exception("Le matricule " + matricule + " existe déjà.");
        }
        // Nom, prénom
        obligatoire(nom, "Le nom");
        longueurMax(nom, 50, "Le nom");
        obligatoire(prenom, "Le prénom");
        longueurMax(prenom, 50, "Le prénom");
        // Login
        obligatoire(login, "Le login");
        longueurMax(login, 50, "Le login");
        if (VisiteurDao.loginExiste(login)) {
            throw new Exception("Le login " + login + " est déjà utilisé.");
        }
        // Mot de passe
        if (mdp.length() < 8) {
            throw new Exception("Le mot de passe doit contenir au moins 8 caractères.");
        }
        longueurMax(mdp, 20, "Le mot de passe");
        // Adresse
        longueurMax(adresse, 50, "L'adresse");
        // Code postal
        if (!ValidationUtils.isCodePostalValide(codePostal)) {
            throw new Exception("Le code postal doit contenir 5 chiffres.");
        }
        Localite laLocalite = LocaliteDao.rechercher(codePostal);
        if (laLocalite == null) {
            throw new Exception("Le code postal " + codePostal + " n'existe pas dans les localités.");
        }
        // Téléphone (facultatif)
        if (!telephone.isEmpty() && !ValidationUtils.isTelephoneValide(telephone)) {
            throw new Exception("Le téléphone doit être au format 0X XX XX XX XX.");
        }
        // Date d'entrée
        if (!ValidationUtils.isDateValide(dateEntree)) {
            throw new Exception("La date d'entrée doit être au format jj/mm/aaaa.");
        }
        Date laDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateEntree);
        if (laDate.after(new Date())) {
            throw new Exception("La date d'entrée ne peut pas être dans le futur.");
        }
        // Prime
        int laPrime;
        try {
            laPrime = prime.isEmpty() ? 0 : Integer.parseInt(prime);
        } catch (NumberFormatException e) {
            throw new Exception("La prime doit être un nombre entier.");
        }
        if (laPrime < 0) {
            throw new Exception("La prime ne peut pas être négative.");
        }
        // Unité
        obligatoire(codeUnite, "Le code unité");
        longueurMax(codeUnite, 4, "Le code unité");
        longueurMax(nomUnite, 50, "Le nom de l'unité");

        Visiteur unVisiteur = new Visiteur(matricule, nom, prenom, login, mdp, adresse,
                laLocalite, telephone, laDate, laPrime, codeUnite, nomUnite);
        if (VisiteurDao.creer(unVisiteur) != 1) {
            throw new Exception("L'ajout du visiteur a échoué.");
        }
    }

    // --- petites méthodes de contrôle, réutilisées pour chaque champ ---

    private static String nettoyer(String texte) {
        return texte == null ? "" : texte.trim();
    }

    private static void obligatoire(String texte, String nomChamp) throws Exception {
        if (texte.isEmpty()) {
            throw new Exception(nomChamp + " est obligatoire.");
        }
    }

    private static void longueurMax(String texte, int max, String nomChamp) throws Exception {
        if (texte.length() > max) {
            throw new Exception(nomChamp + " fait " + max + " caractères au maximum.");
        }
    }
    
    /**
     * @param codePostal un code postal
     * @return la ville correspondante, ou "" si le code est inconnu
     */
    public static String villeDuCodePostal(String codePostal) {
        Localite l = (codePostal == null) ? null : LocaliteDao.rechercher(codePostal.trim());
        return (l == null) ? "" : l.getVille();
    }
}