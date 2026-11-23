package gsb.vue;

import gsb.modele.Medicament;

import javax.swing.JButton;
import javax.swing.JTextField;

/**
 * Fiche détaillée d'un médicament, en consultation seule.
 */
public class JIFMedicamentFiche extends JIFMedicament {

    private static final long serialVersionUID = 1L;

    public JIFMedicamentFiche(Medicament unMedicament) {
        super();
        setTitle("Fiche médicament");
        remplirText(unMedicament);

        JTextField[] champs = {JTdepotLegal, JTnomCommercial, JTcomposition, JTeffets,
                               JTcontreIndication, JTcodeFamille, JTlibelleFamille};
        for (JTextField champ : champs) {
            champ.setEditable(false);
        }

        JButton fermer = new JButton("Fermer");
        fermer.addActionListener(e -> dispose());
        pBoutons.add(fermer);
    }
}