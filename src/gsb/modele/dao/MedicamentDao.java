package gsb.modele.dao;

import gsb.modele.Medicament;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Accès aux données de la table MEDICAMENT.
 */
public class MedicamentDao {

    /**
     * Recherche un médicament par son dépôt légal.
     * @param depotLegal le code du médicament
     * @return le médicament trouvé, ou null s'il n'existe pas
     */
    public static Medicament rechercher(String depotLegal) {
        Medicament unMedicament = null;
        ResultSet req = ConnexionMySql.execReqSelection(
                "select * from MEDICAMENT where MED_DEPOTLEGAL='" + depotLegal + "'");
        try {
            if (req.next()) {
                unMedicament = new Medicament(req.getString(1), req.getString(2),
                        req.getString(3), req.getString(4), req.getString(5),
                        req.getFloat(6), req.getString(7), req.getString(8));
            }
        } catch (Exception e) {
            System.out.println("Erreur MedicamentDao.rechercher : " + e.getMessage());
        }
        ConnexionMySql.fermerConnexionBd();
        return unMedicament;
    }

    /**
     * Retourne tous les médicaments, triés par nom commercial.
     * @return la liste des médicaments (vide si aucun)
     */
    public static ArrayList<Medicament> retournerCollectionDesMedicaments() {
        ArrayList<Medicament> lesMedicaments = new ArrayList<Medicament>();
        ResultSet req = ConnexionMySql.execReqSelection(
                "select * from MEDICAMENT order by MED_NOMCOMMERCIAL");
        try {
            while (req.next()) {
                lesMedicaments.add(new Medicament(req.getString(1), req.getString(2),
                        req.getString(3), req.getString(4), req.getString(5),
                        req.getFloat(6), req.getString(7), req.getString(8)));
            }
        } catch (Exception e) {
            System.out.println("Erreur MedicamentDao.retournerCollection : " + e.getMessage());
        }
        ConnexionMySql.fermerConnexionBd();
        return lesMedicaments;
    }
    
    /**
     * Ajoute un médicament dans la table MEDICAMENT.
     * @param unMedicament le médicament à ajouter
     * @return 1 si l'ajout a réussi, 0 sinon
     */
    public static int creer(Medicament unMedicament) {
        String requete = "insert into MEDICAMENT values ("
                + echapper(unMedicament.getDepotLegal()) + ", "
                + echapper(unMedicament.getNomCommercial()) + ", "
                + echapper(unMedicament.getComposition()) + ", "
                + echapper(unMedicament.getEffets()) + ", "
                + echapper(unMedicament.getContreIndication()) + ", "
                + unMedicament.getPrixEchantillon() + ", "
                + echapper(unMedicament.getCodeFamille()) + ", "
                + echapper(unMedicament.getLibelleFamille()) + ")";
        int resultat = ConnexionMySql.execReqMaj(requete);
        ConnexionMySql.fermerConnexionBd();
        return resultat;
    }

    /**
     * Prépare un texte pour une requête SQL : NULL s'il est vide,
     * sinon entre apostrophes, avec les apostrophes et les \ doublés.
     */
    private static String echapper(String texte) {
        return ConnexionMySql.echapper(texte);
    }
    
    /**
     * Retourne les familles de médicaments, triées par code.
     * @return dictionnaire trié code famille -> libellé famille
     */
    public static TreeMap<String, String> retournerLesFamilles() {
        TreeMap<String, String> lesFamilles = new TreeMap<String, String>();
        ResultSet req = ConnexionMySql.execReqSelection(
                "select distinct FAM_CODE, FAM_LIBELLE from MEDICAMENT where FAM_CODE is not null");
        try {
            while (req.next()) {
                lesFamilles.put(req.getString(1), req.getString(2));
            }
        } catch (Exception e) {
            System.out.println("Erreur MedicamentDao.retournerLesFamilles : " + e.getMessage());
        }
        ConnexionMySql.fermerConnexionBd();
        return lesFamilles;
    }

    /**
     * Retourne les médicaments d'une famille, triés par nom commercial.
     * @param codeFamille le code de la famille
     * @return la liste des médicaments de cette famille
     */
    public static ArrayList<Medicament> retournerMedicamentsDeLaFamille(String codeFamille) {
        ArrayList<Medicament> lesMedicaments = new ArrayList<Medicament>();
        ResultSet req = ConnexionMySql.execReqSelection(
                "select * from MEDICAMENT where FAM_CODE=" + echapper(codeFamille)
                + " order by MED_NOMCOMMERCIAL");
        try {
            while (req.next()) {
                lesMedicaments.add(new Medicament(req.getString(1), req.getString(2),
                        req.getString(3), req.getString(4), req.getString(5),
                        req.getFloat(6), req.getString(7), req.getString(8)));
            }
        } catch (Exception e) {
            System.out.println("Erreur MedicamentDao.retournerMedicamentsDeLaFamille : " + e.getMessage());
        }
        ConnexionMySql.fermerConnexionBd();
        return lesMedicaments;
    }
}