package gsb.tests;

import gsb.modele.Stock;
import gsb.service.StockService;

public class StockServiceTest {

    public static void main(String[] args) {
        tester("f21", "adimol9", "5");     // OK : ADIMOL9 passe de 15 à 20
        tester("zzzz", "ADIMOL9", "5");    // visiteur inconnu
        tester("f21", "XXXX", "5");        // médicament inconnu
        tester("f21", "ADIMOL9", "abc");   // pas un nombre
        tester("f21", "ADIMOL9", "0");     // zéro
        tester("f21", "ADIMOL9", "-3");    // négatif
        tester("f21", "ADIMOL9", "5000");  // trop grand

        try {
            for (Stock s : StockService.retournerStocksDuVisiteur("f21")) {
                System.out.println(s.getUnMedicament().getNomCommercial() + "  " + s.getQuantite());
            }
        } catch (Exception e) {
            System.out.println("ERR  " + e.getMessage());
        }
    }

    private static void tester(String matricule, String depot, String qte) {
        try {
            StockService.ajouterEchantillons(matricule, depot, qte);
            System.out.println("OK   " + matricule + " " + depot + " +" + qte);
        } catch (Exception e) {
            System.out.println("ERR  " + e.getMessage());
        }
    }
}