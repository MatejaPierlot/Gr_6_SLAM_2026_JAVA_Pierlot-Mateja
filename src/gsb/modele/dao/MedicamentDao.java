package gsb.modele.dao;

import gsb.modele.Medicament;

import java.sql.ResultSet;
import java.util.ArrayList;

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
}