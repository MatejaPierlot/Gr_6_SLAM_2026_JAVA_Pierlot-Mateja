package gsb.service;

import gsb.modele.Medicament;
import gsb.modele.dao.MedicamentDao;

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
}