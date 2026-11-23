package gsb.vue;

import gsb.modele.Medicament;
import gsb.service.MedicamentService;

import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * Écran de saisie d'un nouveau médicament.
 */
public class JIFMedicamentAjout extends JIFMedicament {

    private static final long serialVersionUID = 1L;

    public JIFMedicamentAjout() {
        super();                                   // le formulaire, déjà fait
        setTitle("Ajout d'un médicament");

        JButton JBajouter = new JButton("Ajouter");
        JBajouter.addActionListener(e -> ajouter());
        pBoutons.add(JBajouter);
    }

    /**
     * Construit le médicament à partir des champs, l'envoie au Service,
     * puis affiche le résultat.
     */
    private void ajouter() {
        Medicament m = new Medicament(
                JTdepotLegal.getText(), JTnomCommercial.getText(), JTcomposition.getText(),
                JTeffets.getText(), JTcontreIndication.getText(), 0,
                JTcodeFamille.getText(), JTlibelleFamille.getText());
        try {
            MedicamentService.creerMedicament(m);
            JOptionPane.showMessageDialog(this, "Le médicament " + m.getDepotLegal() + " a été ajouté.");
            viderText();                           // formulaire prêt pour un nouvel ajout
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            placerCurseur(e.getMessage());
        }
    }

    /**
     * Replace le curseur dans le champ concerné par l'erreur.
     */
    private void placerCurseur(String message) {
        if (message.startsWith("Le dépôt légal"))               JTdepotLegal.requestFocus();
        else if (message.startsWith("Le nom commercial"))       JTnomCommercial.requestFocus();
        else if (message.startsWith("La composition"))          JTcomposition.requestFocus();
        else if (message.startsWith("Les effets"))              JTeffets.requestFocus();
        else if (message.startsWith("Les contre-indications"))  JTcontreIndication.requestFocus();
        else if (message.startsWith("Le code famille"))         JTcodeFamille.requestFocus();
        else if (message.startsWith("Le libellé famille"))      JTlibelleFamille.requestFocus();
    }

    private void viderText() {
        JTdepotLegal.setText("");
        JTnomCommercial.setText("");
        JTcomposition.setText("");
        JTeffets.setText("");
        JTcontreIndication.setText("");
        JTcodeFamille.setText("");
        JTlibelleFamille.setText("");
        JTdepotLegal.requestFocus();
    }
}