package gsb.modele.dao;

import gsb.modele.Visite;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Accès aux données de la table VISITE.
 */
public class VisiteDao {

    private static final SimpleDateFormat FORMAT_SQL = new SimpleDateFormat("yyyy-MM-dd");

    /** Une ligne de VISITE lue en base, avant d'être transformée en objet. */
    private static class LigneVisite {
        String reference, commentaire, matricule, codeMed, med1, med2;
        Date date;
        int qte1, qte2;
    }

    /** Lit la ligne courante du résultat (sans appeler d'autre Dao). */
    private static LigneVisite lireLigne(ResultSet req) throws Exception {
        LigneVisite l = new LigneVisite();
        l.reference = req.getString("REFERENCE");
        l.date = req.getDate("DATEVISITE");
        l.commentaire = req.getString("COMMENTAIRE");
        l.matricule = req.getString("MATRICULE");
        l.codeMed = req.getString("CODEMED");
        l.med1 = req.getString("MED_OFFERT_1");
        l.qte1 = req.getInt("QUANTITE_MED_1");
        l.med2 = req.getString("MED_OFFERT_2");
        l.qte2 = req.getInt("QUANTITE_MED_2");
        return l;
    }

    /** Transforme une ligne en Visite, en allant chercher les objets liés (connexion déjà fermée). */
    private static Visite construire(LigneVisite l) {
        return new Visite(l.reference, l.date, l.commentaire,
                VisiteurDao.rechercher(l.matricule),
                MedecinDao.rechercher(l.codeMed),
                l.med1 == null ? null : MedicamentDao.rechercher(l.med1), l.qte1,
                l.med2 == null ? null : MedicamentDao.rechercher(l.med2), l.qte2);
    }

    /**
     * Recherche une visite par sa référence.
     * @param reference la référence de la visite
     * @return la visite trouvée, ou null si elle n'existe pas
     */
    public static Visite rechercher(String reference) {
        LigneVisite ligne = null;
        ResultSet req = ConnexionMySql.execReqSelection(
                "select * from VISITE where REFERENCE=" + ConnexionMySql.echapper(reference));
        try {
            if (req.next()) {
                ligne = lireLigne(req);
            }
        } catch (Exception e) {
            System.out.println("Erreur VisiteDao.rechercher : " + e.getMessage());
        }
        ConnexionMySql.fermerConnexionBd();                  // 1. on ferme…
        return ligne == null ? null : construire(ligne);     // 2. …puis on complète
    }

    /**
     * Retourne les visites d'un visiteur à une date, triées par référence.
     * @param matricule le matricule du visiteur
     * @param date la date des visites
     * @return la liste des visites (vide s'il n'y en a aucune)
     */
    public static ArrayList<Visite> retournerVisitesDuVisiteurALaDate(String matricule, Date date) {
        ArrayList<LigneVisite> lignes = new ArrayList<LigneVisite>();
        ResultSet req = ConnexionMySql.execReqSelection(
                "select * from VISITE where MATRICULE=" + ConnexionMySql.echapper(matricule)
                + " and DATEVISITE='" + FORMAT_SQL.format(date) + "' order by REFERENCE");
        try {
            while (req.next()) {
                lignes.add(lireLigne(req));
            }
        } catch (Exception e) {
            System.out.println("Erreur VisiteDao.retournerVisitesDuVisiteurALaDate : " + e.getMessage());
        }
        ConnexionMySql.fermerConnexionBd();
        ArrayList<Visite> lesVisites = new ArrayList<Visite>();
        for (LigneVisite l : lignes) {
            lesVisites.add(construire(l));
        }
        return lesVisites;
    }

    /**
     * Ajoute une visite planifiée (sans commentaire ni médicament offert).
     * @param uneVisite la visite à ajouter
     * @return 1 si l'ajout a réussi, 0 sinon
     */
    public static int creer(Visite uneVisite) {
        String requete = "insert into VISITE (REFERENCE, DATEVISITE, COMMENTAIRE, MATRICULE, CODEMED) values ("
                + ConnexionMySql.echapper(uneVisite.getReference()) + ", "
                + "'" + FORMAT_SQL.format(uneVisite.getDateVisite()) + "', "
                + ConnexionMySql.echapper(uneVisite.getCommentaire()) + ", "
                + ConnexionMySql.echapper(uneVisite.getUnVisiteur().getMatricule()) + ", "
                + ConnexionMySql.echapper(uneVisite.getUnMedecin().getCodeMed()) + ")";
        int resultat = ConnexionMySql.execReqMaj(requete);
        ConnexionMySql.fermerConnexionBd();
        return resultat;
    }
}