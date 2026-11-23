package gsb.tests;

import gsb.modele.Stock;
import gsb.modele.Visiteur;
import gsb.modele.dao.StockDao;
import gsb.modele.dao.VisiteurDao;

public class StockDaoTest {
    public static void main(String[] args) {
        System.out.println("Ajout 10 ADIMOL9 -> " + StockDao.ajouterQuantite("f21", "ADIMOL9", 10));  // INSERT
        System.out.println("Ajout 5 ADIMOL9  -> " + StockDao.ajouterQuantite("f21", "ADIMOL9", 5));   // UPDATE
        System.out.println("Ajout 3 AMOPIL7  -> " + StockDao.ajouterQuantite("f21", "AMOPIL7", 3));   // INSERT

        Visiteur v = VisiteurDao.rechercher("f21");
        for (Stock s : StockDao.retournerStocksDuVisiteur(v)) {
            System.out.println(s.getUnMedicament().getDepotLegal() + "  "
                    + s.getUnMedicament().getNomCommercial() + "  " + s.getQuantite());
        }
    }
}