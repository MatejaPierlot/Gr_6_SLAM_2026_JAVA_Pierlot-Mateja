package gsb.service;

import gsb.modele.Medicament;
import gsb.modele.Stock;
import gsb.modele.Visiteur;
import gsb.modele.dao.StockDao;

import java.util.ArrayList;

/**
 * Contrôles et règles de gestion des stocks d'échantillons des visiteurs.
 */
public class StockService {

    /** Quantité maximale acceptée en une seule fois. */
    public static final int QUANTITE_MAX = 1000;

    /**
     * Retourne le stock d'échantillons d'un visiteur.
     * @param matricule le matricule du visiteur
     * @return ses stocks, triés par nom de médicament
     * @throws Exception si le matricule est vide ou inconnu
     */
    public static ArrayList<Stock> retournerStocksDuVisiteur(String matricule) throws Exception {
        Visiteur v = VisiteurService.rechercherVisiteur(matricule);   // contrôle déjà écrit
        return StockDao.retournerStocksDuVisiteur(v);
    }

    /**
     * Ajoute des échantillons au stock d'un visiteur.
     * @param matricule  le matricule du visiteur
     * @param depotLegal le dépôt légal du médicament
     * @param quantite   la quantité saisie (texte)
     * @throws Exception si une donnée est invalide ou si la mise à jour échoue
     */
    public static void ajouterEchantillons(String matricule, String depotLegal, String quantite) throws Exception {
        Visiteur v = VisiteurService.rechercherVisiteur(matricule);
        Medicament m = MedicamentService.rechercherMedicament(depotLegal);

        int laQuantite;
        try {
            laQuantite = Integer.parseInt(quantite == null ? "" : quantite.trim());
        } catch (NumberFormatException e) {
            throw new Exception("La quantité doit être un nombre entier.");
        }
        if (laQuantite <= 0) {
            throw new Exception("La quantité doit être supérieure à 0.");
        }
        if (laQuantite > QUANTITE_MAX) {
            throw new Exception("La quantité ne peut pas dépasser " + QUANTITE_MAX + " en une fois.");
        }

        if (StockDao.ajouterQuantite(v.getMatricule(), m.getDepotLegal(), laQuantite) != 1) {
            throw new Exception("La mise à jour du stock a échoué.");
        }
    }
}