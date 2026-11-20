package gsb.tests.unit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import gsb.modele.Medicament;
import gsb.service.MedicamentService;

/**
 * Tests unitaires de MedicamentService (contrôles de saisie et lecture en base).
 */
class MedicamentServiceTest {

    @Test
    void rechercherUnMedicamentExistant() throws Exception {
        Medicament m = MedicamentService.rechercherMedicament("ADIMOL9");
        assertEquals("ADIMOL", m.getNomCommercial());
    }

    @Test
    void rechercherEnMinusculesTrouveLeMedicament() throws Exception {
        Medicament m = MedicamentService.rechercherMedicament("adimol9");
        assertEquals("ADIMOL9", m.getDepotLegal());
    }

    @Test
    void codeVideEstRefuse() {
        Exception e = assertThrows(Exception.class,
                () -> MedicamentService.rechercherMedicament(""));
        assertEquals("Le dépôt légal est obligatoire.", e.getMessage());
    }

    @Test
    void codeTropLongEstRefuse() {
        Exception e = assertThrows(Exception.class,
                () -> MedicamentService.rechercherMedicament("ABCDEFGHIJKLM"));
        assertEquals("Le dépôt légal fait 10 caractères au maximum.", e.getMessage());
    }

    @Test
    void codeInconnuEstRefuse() {
        assertThrows(Exception.class,
                () -> MedicamentService.rechercherMedicament("XXXX"));
    }

    @Test
    void laListeDesMedicamentsNestPasVide() {
        assertFalse(MedicamentService.retournerLesMedicaments().isEmpty());
    }
}