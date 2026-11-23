package gsb.tests;

import gsb.modele.Visite;
import gsb.service.VisiteService;

public class VisiteServiceTest {

    public static void main(String[] args) {
        tester("v9998", "02/12/2026", "f21", "m001");    // OK
        tester("v9998", "02/12/2026", "f21", "m001");    // référence existe déjà
        tester("",      "02/12/2026", "f21", "m001");    // référence vide
        tester("visite", "02/12/2026", "f21", "m001");   // mauvais format
        tester("v9997", "2026-12-02", "f21", "m001");    // date mal écrite
        tester("v9997", "01/01/2020", "f21", "m001");    // date passée
        tester("v9997", "02/12/2026", "zzzz", "m001");   // visiteur inexistant
        tester("v9997", "02/12/2026", "f21", "m999");    // médecin inexistant

        try {
            for (Visite v : VisiteService.retournerVisitesDuVisiteurALaDate("f21", "02/12/2026")) {
                System.out.println("Liste : " + v.getReference() + " chez " + v.getUnMedecin().getNom());
            }
        } catch (Exception e) {
            System.out.println("ERR  " + e.getMessage());
        }
    }

    private static void tester(String ref, String date, String matricule, String med) {
        try {
            VisiteService.creerVisite(ref, date, matricule, med);
            System.out.println("OK   " + ref + " ajoutée");
        } catch (Exception e) {
            System.out.println("ERR  " + e.getMessage());
        }
    }
}