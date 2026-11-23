package gsb.service;

import gsb.modele.Medicament;
import gsb.modele.dao.MedicamentDao;
import java.util.TreeMap;
import java.util.ArrayList;

/**
 * Contrôles de saisie et règles de gestion pour les médicaments.
 */
public class MedicamentService {

    /**
     * Recherche un médicament après avoir contrôlé le code saisi.
     * @param depotLegal le code saisi par l'utilisateur
     * @return le médicament trouvé
     * @throws Exception si le code est vide, trop long ou inconnu
     */
    public static Medicament rechercherMedicament(String depotLegal) throws Exception {
        if (depotLegal == null || depotLegal.trim().isEmpty()) {
            throw new Exception("Le dépôt légal est obligatoire.");
        }
        depotLegal = depotLegal.trim().toUpperCase();
        if (depotLegal.length() > 10) {
            throw new Exception("Le dépôt légal fait 10 caractères au maximum.");
        }
        Medicament unMedicament = MedicamentDao.rechercher(depotLegal);
        if (unMedicament == null) {
            throw new Exception("Aucun médicament ne correspond au dépôt légal " + depotLegal + ".");
        }
        return unMedicament;
    }

    /**
     * @return tous les médicaments, triés par nom commercial
     */
    public static ArrayList<Medicament> retournerLesMedicaments() {
        return MedicamentDao.retournerCollectionDesMedicaments();
    }
    
    /**
     * Contrôle les données saisies puis ajoute le médicament.
     * @param unMedicament le médicament saisi
     * @throws Exception si une donnée est invalide ou si l'ajout échoue
     */
    public static void creerMedicament(Medicament unMedicament) throws Exception {
        String depot = unMedicament.getDepotLegal() == null ? "" : unMedicament.getDepotLegal().trim().toUpperCase();
        String nom = unMedicament.getNomCommercial() == null ? "" : unMedicament.getNomCommercial().trim();
        String codeFamille = unMedicament.getCodeFamille() == null ? "" : unMedicament.getCodeFamille().trim().toUpperCase();

        // champs obligatoires
        if (depot.isEmpty())       throw new Exception("Le dépôt légal est obligatoire.");
        if (nom.isEmpty())         throw new Exception("Le nom commercial est obligatoire.");
        if (codeFamille.isEmpty()) throw new Exception("Le code famille est obligatoire.");

        // longueurs maximales = tailles des colonnes de la table
        if (depot.length() > 10)       throw new Exception("Le dépôt légal fait 10 caractères au maximum.");
        if (nom.length() > 25)         throw new Exception("Le nom commercial fait 25 caractères au maximum.");
        if (codeFamille.length() > 3)  throw new Exception("Le code famille fait 3 caractères au maximum.");
        verifierLongueur(unMedicament.getComposition(), 255, "La composition");
        verifierLongueur(unMedicament.getEffets(), 255, "Les effets");
        verifierLongueur(unMedicament.getContreIndication(), 255, "Les contre-indications");
        verifierLongueur(unMedicament.getLibelleFamille(), 80, "Le libellé famille");

        // pas de doublon sur la clé primaire
        if (MedicamentDao.rechercher(depot) != null) {
            throw new Exception("Le dépôt légal " + depot + " existe déjà.");
        }

        unMedicament.setDepotLegal(depot);
        unMedicament.setNomCommercial(nom);
        unMedicament.setCodeFamille(codeFamille);
        if (MedicamentDao.creer(unMedicament) != 1) {
            throw new Exception("L'ajout du médicament a échoué.");
        }
    }

    private static void verifierLongueur(String texte, int max, String nomChamp) throws Exception {
        if (texte != null && texte.trim().length() > max) {
            throw new Exception(nomChamp + " fait " + max + " caractères au maximum.");
        }
    }
    
    /** @return les familles, triées par code (code -> libellé) */
    public static TreeMap<String, String> retournerLesFamilles() {
        return MedicamentDao.retournerLesFamilles();
    }

    /** @return les médicaments de la famille choisie */
    public static ArrayList<Medicament> retournerMedicamentsDeLaFamille(String codeFamille) {
        return MedicamentDao.retournerMedicamentsDeLaFamille(codeFamille);
    }
}