package gsb.modele.dao;

import gsb.modele.Localite;
import gsb.modele.Visiteur;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Accès aux données de la table VISITEUR (avec sa localité).
 */
public class VisiteurDao {

    private static final String SELECT_VISITEUR =
            "select V.*, L.VILLE from VISITEUR V left join LOCALITE L on V.CODEPOSTAL = L.CODEPOSTAL";

    /**
     * Construit un Visiteur à partir de la ligne courante du résultat.
     */
    private static Visiteur lireVisiteur(ResultSet req) throws Exception {
        Localite laLocalite = null;
        if (req.getString(7) != null) {
            laLocalite = new Localite(req.getString(7), req.getString(13));   // code postal + ville
        }
        return new Visiteur(req.getString(1), req.getString(2), req.getString(3),
                req.getString(4), req.getString(5), req.getString(6), laLocalite,
                req.getString(8), req.getDate(9), req.getInt(10),
                req.getString(11), req.getString(12));
    }

    /**
     * Recherche un visiteur par son matricule.
     * @param matricule le matricule du visiteur
     * @return le visiteur trouvé, ou null s'il n'existe pas
     */
    public static Visiteur rechercher(String matricule) {
        Visiteur unVisiteur = null;
        ResultSet req = ConnexionMySql.execReqSelection(
                SELECT_VISITEUR + " where V.MATRICULE=" + ConnexionMySql.echapper(matricule));
        try {
            if (req.next()) {
                unVisiteur = lireVisiteur(req);
            }
        } catch (Exception e) {
            System.out.println("Erreur VisiteurDao.rechercher : " + e.getMessage());
        }
        ConnexionMySql.fermerConnexionBd();
        return unVisiteur;
    }

    /**
     * Retourne tous les visiteurs, triés par nom puis prénom.
     * @return la liste des visiteurs
     */
    public static ArrayList<Visiteur> retournerCollectionDesVisiteurs() {
        ArrayList<Visiteur> lesVisiteurs = new ArrayList<Visiteur>();
        ResultSet req = ConnexionMySql.execReqSelection(SELECT_VISITEUR + " order by V.NOM, V.PRENOM");
        try {
            while (req.next()) {
                lesVisiteurs.add(lireVisiteur(req));
            }
        } catch (Exception e) {
            System.out.println("Erreur VisiteurDao.retournerCollection : " + e.getMessage());
        }
        ConnexionMySql.fermerConnexionBd();
        return lesVisiteurs;
    }

    /**
     * Ajoute un visiteur dans la table VISITEUR.
     * @param unVisiteur le visiteur à ajouter
     * @return 1 si l'ajout a réussi, 0 sinon
     */
    public static int creer(Visiteur unVisiteur) {
        String codePostal = unVisiteur.getUneLocalite() == null ? null : unVisiteur.getUneLocalite().getCodePostal();
        String dateEntree = unVisiteur.getDateEntree() == null ? null
                : new SimpleDateFormat("yyyy-MM-dd").format(unVisiteur.getDateEntree());
        String requete = "insert into VISITEUR values ("
                + ConnexionMySql.echapper(unVisiteur.getMatricule()) + ", "
                + ConnexionMySql.echapper(unVisiteur.getNom()) + ", "
                + ConnexionMySql.echapper(unVisiteur.getPrenom()) + ", "
                + ConnexionMySql.echapper(unVisiteur.getLogin()) + ", "
                + ConnexionMySql.echapper(unVisiteur.getMdp()) + ", "
                + ConnexionMySql.echapper(unVisiteur.getAdresse()) + ", "
                + ConnexionMySql.echapper(codePostal) + ", "
                + ConnexionMySql.echapper(unVisiteur.getTelephone()) + ", "
                + ConnexionMySql.echapper(dateEntree) + ", "
                + unVisiteur.getPrime() + ", "
                + ConnexionMySql.echapper(unVisiteur.getCodeUnite()) + ", "
                + ConnexionMySql.echapper(unVisiteur.getNomUnite()) + ")";
        int resultat = ConnexionMySql.execReqMaj(requete);
        ConnexionMySql.fermerConnexionBd();
        return resultat;
    }
}