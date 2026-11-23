package gsb.tests;

import gsb.service.VisiteurService;

public class VisiteurServiceTest {

    public static void main(String[] args) {
        // matricule, nom, prénom, login, mdp, adresse, cp, téléphone, date, prime, code unité, nom unité
        tester("z99", "Test", "Anna", "atest", "motdepasse1", "1 rue de la Gare", "23200",
                "06 11 22 33 44", "15/03/2024", "500", "SW", "SWISS");          // doit réussir
        tester("z99", "Test", "Anna", "atest2", "motdepasse1", "", "23200", "", "15/03/2024", "500", "SW", "");   // matricule existe
        tester("z98", "Test", "Anna", "cduncombe", "motdepasse1", "", "23200", "", "15/03/2024", "500", "SW", ""); // login pris
        tester("z98", "Test", "Anna", "atest2", "court", "", "23200", "", "15/03/2024", "500", "SW", "");         // mdp trop court
        tester("z98", "Test", "Anna", "atest2", "motdepasse1", "", "99999", "", "15/03/2024", "500", "SW", "");   // cp inconnu
        tester("z98", "Test", "Anna", "atest2", "motdepasse1", "", "23200", "12345", "15/03/2024", "500", "SW", ""); // tél invalide
        tester("z98", "Test", "Anna", "atest2", "motdepasse1", "", "23200", "", "31/02/2024", "500", "SW", "");   // date impossible
        tester("z98", "Test", "Anna", "atest2", "motdepasse1", "", "23200", "", "15/03/2024", "abc", "SW", "");   // prime pas un nombre
        tester("z98", "", "Anna", "atest2", "motdepasse1", "", "23200", "", "15/03/2024", "500", "SW", "");       // nom vide
    }

    private static void tester(String... champs) {
        try {
            VisiteurService.creerVisiteur(champs[0], champs[1], champs[2], champs[3], champs[4], champs[5],
                    champs[6], champs[7], champs[8], champs[9], champs[10], champs[11]);
            System.out.println("OK   " + champs[0] + " ajouté");
        } catch (Exception e) {
            System.out.println("ERR  " + e.getMessage());
        }
    }
}