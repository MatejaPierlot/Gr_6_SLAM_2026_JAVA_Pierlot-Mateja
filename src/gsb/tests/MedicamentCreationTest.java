package gsb.tests;

import gsb.modele.Medicament;
import gsb.service.MedicamentService;

public class MedicamentCreationTest {

    public static void main(String[] args) {
        tester(new Medicament("essai1", "ESSAI", "Paracétamol", "Contre la douleur",
                "Ne pas prendre en cas d'allergie", 0, "aa", "Antalgique"));       // doit réussir
        tester(new Medicament("ESSAI1", "ESSAI", null, null, null, 0, "AA", null)); // existe déjà
        tester(new Medicament("", "ESSAI", null, null, null, 0, "AA", null));       // dépôt vide
        tester(new Medicament("ESSAI2", "", null, null, null, 0, "AA", null));      // nom vide
        tester(new Medicament("ESSAI3", "ESSAI", null, null, null, 0, "ABCD", null)); // famille trop longue
    }

    private static void tester(Medicament m) {
        try {
            MedicamentService.creerMedicament(m);
            System.out.println("OK   " + m.getDepotLegal() + " ajouté");
        } catch (Exception e) {
            System.out.println("ERR  " + e.getMessage());
        }
    }
}