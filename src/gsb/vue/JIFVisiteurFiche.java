package gsb.vue;

import gsb.modele.Visiteur;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTextField;

/**
 * Fiche détaillée d'un visiteur, en consultation seule.
 */
public class JIFVisiteurFiche extends JIFVisiteur {

    private static final long serialVersionUID = 1L;

    public JIFVisiteurFiche(Visiteur unVisiteur) {
        super();
        setTitle("Fiche visiteur");
        remplirText(unVisiteur);

        for (Component c : pTexte.getComponents()) {     // tous les champs du formulaire
            if (c instanceof JTextField) {
                ((JTextField) c).setEditable(false);
            }
        }

        JButton fermer = new JButton("Fermer");
        fermer.addActionListener(e -> dispose());
        pBoutons.add(fermer);
    }
}