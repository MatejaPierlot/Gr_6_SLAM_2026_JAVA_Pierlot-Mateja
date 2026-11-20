package gsb.tests;

import gsb.modele.Medicament;
import gsb.service.MedicamentService;

public class MedicamentServiceTest {

    public static void main(String[] args) {
        tester("ADIMOL9");       // existe
        tester("adimol9");       // minuscules
        tester("");              // vide
        tester("ABCDEFGHIJKLM"); // trop long
        tester("XXXX");          // inconnu
    }

    private static void tester(String code) {
        try {
            Medicament m = MedicamentService.rechercherMedicament(code);
            System.out.println("OK   [" + code + "] -> " + m.getNomCommercial());
        } catch (Exception e) {
            System.out.println("ERR  [" + code + "] -> " + e.getMessage());
        }
    }
}