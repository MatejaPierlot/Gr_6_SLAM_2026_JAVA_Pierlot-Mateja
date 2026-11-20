package gsb.tests;

import gsb.modele.Medicament;
import gsb.modele.dao.MedicamentDao;

public class MedicamentDaoTest {
    public static void main(String[] args) {
        // Test 1 : un médicament qui existe
        Medicament m = MedicamentDao.rechercher("ADIMOL9");
        System.out.println(m.getDepotLegal() + " - " + m.getNomCommercial() + " - " + m.getLibelleFamille());

        // Test 2 : un code qui n'existe pas
        System.out.println(MedicamentDao.rechercher("XXXX"));

        // Test 3 : tous les médicaments
        for (Medicament med : MedicamentDao.retournerCollectionDesMedicaments()) {
            System.out.println(med.getDepotLegal() + "  " + med.getNomCommercial());
        }
    }
}