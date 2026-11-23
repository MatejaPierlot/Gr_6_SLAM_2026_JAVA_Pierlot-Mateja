package gsb.tests;

import gsb.modele.dao.StockDao;
import gsb.service.VisiteService;

public class VisiteMiseAJourTest {

    public static void main(String[] args) {
        afficherStock("avant");
        // ref, commentaire, med1, qte1, med2, qte2
        tester("v0024", "",              "ADIMOL9", "2", "", "");          // commentaire vide
        tester("v0024", "Visite faite",  "ADIMOL9", "abc", "", "");        // quantité pas un nombre
        tester("v0024", "Visite faite",  "ADIMOL9", "999", "", "");        // stock insuffisant
        tester("v0024", "Visite faite",  "ADIMOL9", "2", "ADIMOL9", "1");  // même médicament deux fois
        tester("v0024", "Visite faite",  "BACTIG10", "1", "", "");         // f21 n'a pas ce médicament
        tester("v0024", "Médecin intéressé, à revoir dans 3 mois", "ADIMOL9", "2", "AMOPIL7", "1");  // OK
        tester("v0024", "Encore",        "", "", "", "");                  // déjà effectuée
        afficherStock("après");
    }

    private static void tester(String ref, String com, String d1, String q1, String d2, String q2) {
        try {
            VisiteService.effectuerVisite(ref, com, d1, q1, d2, q2);
            System.out.println("OK   visite " + ref + " enregistrée");
        } catch (Exception e) {
            System.out.println("ERR  " + e.getMessage());
        }
    }

    private static void afficherStock(String moment) {
        System.out.println("Stock f21 " + moment + " : ADIMOL9 = " + StockDao.quantiteEnStock("f21", "ADIMOL9")
                + ", AMOPIL7 = " + StockDao.quantiteEnStock("f21", "AMOPIL7"));
    }
}