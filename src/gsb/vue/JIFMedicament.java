package gsb.vue;

import gsb.modele.Medicament;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Formulaire commun aux écrans médicament (fiche, ajout).
 */
public class JIFMedicament extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    protected JPanel pTexte;
    protected JPanel pBoutons;

    protected JTextField JTdepotLegal;
    protected JTextField JTnomCommercial;
    protected JTextField JTcomposition;
    protected JTextField JTeffets;
    protected JTextField JTcontreIndication;
    protected JTextField JTcodeFamille;
    protected JTextField JTlibelleFamille;

    public JIFMedicament() {
        pTexte = new JPanel(new GridLayout(7, 2, 8, 6));
        pBoutons = new JPanel();

        JTdepotLegal = new JTextField();
        JTnomCommercial = new JTextField();
        JTcomposition = new JTextField();
        JTeffets = new JTextField();
        JTcontreIndication = new JTextField();
        JTcodeFamille = new JTextField();
        JTlibelleFamille = new JTextField();

        pTexte.add(new JLabel("Dépôt légal"));        pTexte.add(JTdepotLegal);
        pTexte.add(new JLabel("Nom commercial"));     pTexte.add(JTnomCommercial);
        pTexte.add(new JLabel("Composition"));        pTexte.add(JTcomposition);
        pTexte.add(new JLabel("Effets"));             pTexte.add(JTeffets);
        pTexte.add(new JLabel("Contre-indications")); pTexte.add(JTcontreIndication);
        pTexte.add(new JLabel("Code famille"));       pTexte.add(JTcodeFamille);
        pTexte.add(new JLabel("Libellé famille"));    pTexte.add(JTlibelleFamille);

        getContentPane().add(pTexte, BorderLayout.CENTER);
        getContentPane().add(pBoutons, BorderLayout.SOUTH);
    }

    /**
     * Remplit les zones de texte avec les données d'un médicament.
     * @param unMedicament le médicament à afficher
     */
    public void remplirText(Medicament unMedicament) {
        JTdepotLegal.setText(unMedicament.getDepotLegal());
        JTnomCommercial.setText(unMedicament.getNomCommercial());
        JTcomposition.setText(unMedicament.getComposition());
        JTeffets.setText(unMedicament.getEffets());
        JTcontreIndication.setText(unMedicament.getContreIndication());
        JTcodeFamille.setText(unMedicament.getCodeFamille());
        JTlibelleFamille.setText(unMedicament.getLibelleFamille());
    }
}
