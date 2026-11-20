package gsb.tests;
import gsb.modele.Localite;
import gsb.modele.dao.LocaliteDao;

public class LocaliteDaoTest {

	public static void main(String[] args) {
		Localite l = LocaliteDao.rechercher("23200");
		System.out.println(l.getCodePostal() + " " + l.getVille());

		Localite inconnue = LocaliteDao.rechercher("99999");
		System.out.println(inconnue);

	}

}
