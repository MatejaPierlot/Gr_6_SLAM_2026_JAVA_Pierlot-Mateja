package gsb.tests;

import gsb.modele.Visite;
import gsb.modele.dao.MedecinDao;
import gsb.modele.dao.VisiteDao;
import gsb.modele.dao.VisiteurDao;

import java.text.SimpleDateFormat;

public class VisiteDaoTest {
    public static void main(String[] args) throws Exception {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");

        // 1. Rechercher une visite existante
        Visite v = VisiteDao.rechercher("v0011");
        System.out.println(v.getReference() + " du " + f.format(v.getDateVisite())
                + " - visiteur " + v.getUnVisiteur().getNom()
                + " - médecin " + v.getUnMedecin().getNom());

        // 2. Référence inconnue
        System.out.println(VisiteDao.rechercher("zzzzz"));

        // 3. Créer une visite planifiée
        Visite nouvelle = new Visite("v9999", f.parse("02/12/2026"),
                VisiteurDao.rechercher("f21"), MedecinDao.rechercher("m001"));
        System.out.println("Création v9999 -> " + VisiteDao.creer(nouvelle));

        // 4. Les visites de f21 le 02/12/2026
        for (Visite vis : VisiteDao.retournerVisitesDuVisiteurALaDate("f21", f.parse("02/12/2026"))) {
            System.out.println(vis.getReference() + "  " + vis.getUnMedecin().getNom()
                    + "  " + vis.getUnMedecin().getLaLocalite().getVille());
        }
    }
}