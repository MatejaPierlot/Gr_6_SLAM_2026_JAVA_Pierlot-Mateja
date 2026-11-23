package gsb.tests;

import gsb.modele.Visiteur;
import gsb.modele.dao.VisiteurDao;

public class VisiteurDaoTest {
    public static void main(String[] args) {
        Visiteur v = VisiteurDao.rechercher("f21");
        System.out.println(v.getMatricule() + " " + v.getPrenom() + " " + v.getNom()
                + " - " + v.getUneLocalite().getVille()
                + " - entré le " + v.getDateEntree() + " - prime " + v.getPrime());

        System.out.println(VisiteurDao.rechercher("zzzz"));

        for (Visiteur vis : VisiteurDao.retournerCollectionDesVisiteurs()) {
            System.out.println(vis.getMatricule() + "  " + vis.getNom() + " " + vis.getPrenom());
        }
    }
}