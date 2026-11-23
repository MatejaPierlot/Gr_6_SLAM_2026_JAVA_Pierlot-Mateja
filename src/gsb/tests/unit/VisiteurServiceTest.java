package gsb.tests.unit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import gsb.modele.Visiteur;
import gsb.service.VisiteurService;

/**
 * Tests unitaires de VisiteurService.
 * Les tests d'ajout ne testent que des refus : la base n'est jamais modifiée.
 */
class VisiteurServiceTest {

    /** Appelle creerVisiteur avec des données valides, sauf celle qu'on remplace. */
    private void creer(String matricule, String login, String mdp, String cp,
                       String tel, String date, String prime) throws Exception {
        VisiteurService.creerVisiteur(matricule, "Test", "Anna", login, mdp, "", cp, tel,
                date, prime, "SW", "SWISS");
    }

    private String messageErreur(org.junit.jupiter.api.function.Executable action) {
        return assertThrows(Exception.class, action).getMessage();
    }

    @Test
    void rechercherUnVisiteurExistant() throws Exception {
        Visiteur v = VisiteurService.rechercherVisiteur("f21");
        assertEquals("Finck", v.getNom());
        assertEquals("Allauh", v.getUneLocalite().getVille());
    }

    @Test
    void matriculeVideEstRefuse() {
        assertEquals("Le matricule est obligatoire.",
                messageErreur(() -> VisiteurService.rechercherVisiteur("  ")));
    }

    @Test
    void matriculeInconnuEstRefuse() {
        assertThrows(Exception.class, () -> VisiteurService.rechercherVisiteur("zzzz"));
    }

    @Test
    void matriculeExistantEstRefuseALAjout() {
        assertEquals("Le matricule f21 existe déjà.",
                messageErreur(() -> creer("f21", "nouveau", "motdepasse1", "23200", "", "15/03/2024", "0")));
    }

    @Test
    void loginDejaUtiliseEstRefuse() {
        assertEquals("Le login cduncombe est déjà utilisé.",
                messageErreur(() -> creer("z98", "cduncombe", "motdepasse1", "23200", "", "15/03/2024", "0")));
    }

    @Test
    void motDePasseTropCourtEstRefuse() {
        assertEquals("Le mot de passe doit contenir au moins 8 caractères.",
                messageErreur(() -> creer("z98", "nouveau", "court", "23200", "", "15/03/2024", "0")));
    }

    @Test
    void codePostalInconnuEstRefuse() {
        assertEquals("Le code postal 99999 n'existe pas dans les localités.",
                messageErreur(() -> creer("z98", "nouveau", "motdepasse1", "99999", "", "15/03/2024", "0")));
    }

    @Test
    void telephoneInvalideEstRefuse() {
        assertEquals("Le téléphone doit être au format 0X XX XX XX XX.",
                messageErreur(() -> creer("z98", "nouveau", "motdepasse1", "23200", "12345", "15/03/2024", "0")));
    }

    @Test
    void dateImpossibleEstRefusee() {
        assertEquals("La date d'entrée doit être au format jj/mm/aaaa.",
                messageErreur(() -> creer("z98", "nouveau", "motdepasse1", "23200", "", "31/02/2024", "0")));
    }

    @Test
    void dateDansLeFuturEstRefusee() {
        assertEquals("La date d'entrée ne peut pas être dans le futur.",
                messageErreur(() -> creer("z98", "nouveau", "motdepasse1", "23200", "", "01/01/2099", "0")));
    }

    @Test
    void primeNonNumeriqueEstRefusee() {
        assertEquals("La prime doit être un nombre entier.",
                messageErreur(() -> creer("z98", "nouveau", "motdepasse1", "23200", "", "15/03/2024", "abc")));
    }

    @Test
    void primeNegativeEstRefusee() {
        assertEquals("La prime ne peut pas être négative.",
                messageErreur(() -> creer("z98", "nouveau", "motdepasse1", "23200", "", "15/03/2024", "-50")));
    }
}