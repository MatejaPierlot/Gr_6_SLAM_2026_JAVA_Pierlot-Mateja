package gsb.modele.dao;

import gsb.modele.Medicament;
import gsb.modele.Stock;
import gsb.modele.Visiteur;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Accès aux données de la table STOCK.
 */
public class StockDao {

    /**
     * Retourne le stock d'échantillons d'un visiteur, trié par nom de médicament.
     * @param unVisiteur le visiteur
     * @return la liste de ses stocks (vide s'il n'en a aucun)
     */
    public static ArrayList<Stock> retournerStocksDuVisiteur(Visiteur unVisiteur) {
        ArrayList<Stock> lesStocks = new ArrayList<Stock>();
        ResultSet req = ConnexionMySql.execReqSelection(
                "select M.*, S.STOCK from STOCK S join MEDICAMENT M on S.MED_DEPOTLEGAL = M.MED_DEPOTLEGAL"
                + " where S.MATRICULE=" + ConnexionMySql.echapper(unVisiteur.getMatricule())
                + " order by M.MED_NOMCOMMERCIAL");
        try {
            while (req.next()) {
                Medicament m = new Medicament(req.getString(1), req.getString(2), req.getString(3),
                        req.getString(4), req.getString(5), req.getFloat(6), req.getString(7), req.getString(8));
                lesStocks.add(new Stock(unVisiteur, m, req.getInt(9)));
            }
        } catch (Exception e) {
            System.out.println("Erreur StockDao.retournerStocksDuVisiteur : " + e.getMessage());
        }
        ConnexionMySql.fermerConnexionBd();
        return lesStocks;
    }

    /**
     * Ajoute une quantité au stock d'un visiteur pour un médicament :
     * met à jour la ligne si elle existe, sinon la crée.
     * @return 1 si la mise à jour a réussi, 0 sinon
     */
    public static int ajouterQuantite(String matricule, String depotLegal, int quantite) {
        String mat = ConnexionMySql.echapper(matricule);
        String dep = ConnexionMySql.echapper(depotLegal);
        int resultat = ConnexionMySql.execReqMaj(
                "update STOCK set STOCK = STOCK + " + quantite
                + " where MATRICULE=" + mat + " and MED_DEPOTLEGAL=" + dep);
        ConnexionMySql.fermerConnexionBd();
        if (resultat == 0) {                                  // pas encore de ligne : on la crée
            resultat = ConnexionMySql.execReqMaj(
                    "insert into STOCK (MED_DEPOTLEGAL, MATRICULE, STOCK) values ("
                    + dep + ", " + mat + ", " + quantite + ")");
            ConnexionMySql.fermerConnexionBd();
        }
        return resultat;
    }
    
    /**
     * @return la quantité en stock d'un médicament pour un visiteur (0 s'il n'en a pas)
     */
    public static int quantiteEnStock(String matricule, String depotLegal) {
        int quantite = 0;
        ResultSet req = ConnexionMySql.execReqSelection(
                "select STOCK from STOCK where MATRICULE=" + ConnexionMySql.echapper(matricule)
                + " and MED_DEPOTLEGAL=" + ConnexionMySql.echapper(depotLegal));
        try {
            if (req.next()) {
                quantite = req.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Erreur StockDao.quantiteEnStock : " + e.getMessage());
        }
        ConnexionMySql.fermerConnexionBd();
        return quantite;
    }

    /**
     * Retire une quantité du stock, seulement si le stock est suffisant.
     * @return 1 si le retrait a été fait, 0 sinon
     */
    public static int retirerQuantite(String matricule, String depotLegal, int quantite) {
        int resultat = ConnexionMySql.execReqMaj(
                "update STOCK set STOCK = STOCK - " + quantite
                + " where MATRICULE=" + ConnexionMySql.echapper(matricule)
                + " and MED_DEPOTLEGAL=" + ConnexionMySql.echapper(depotLegal)
                + " and STOCK >= " + quantite);
        ConnexionMySql.fermerConnexionBd();
        return resultat;
    }
}