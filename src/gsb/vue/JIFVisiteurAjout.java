package gsb.vue;

import gsb.service.VisiteurService;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Écran de saisie d'un nouveau visiteur.
 */
public class JIFVisiteurAjout extends JIFVisiteur {

    private static final long serialVersionUID = 1L;

    private JTextField JTlogin = new JTextField();
    private JPasswordField JPmdp = new JPasswordField();

    public JIFVisiteurAjout() {
        super();                                     // les 11 lignes du formulaire
        setTitle("Ajout d'un visiteur");

        ajouterLigne("Login", JTlogin);              // + 2 lignes
        ajouterLigne("Mot de passe (8 caractères min.)", JPmdp);

        // La ville se déduit du code postal : lecture seule, remplie en quittant le champ
        JTville.setEditable(false);
        JTcodePostal.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                JTville.setText(VisiteurService.villeDuCodePostal(JTcodePostal.getText()));
            }
        });

        JButton JBajouter = new JButton("Ajouter");
        JBajouter.addActionListener(e -> ajouter());
        pBoutons.add(JBajouter);
    }

    private void ajouter() {
        try {
            VisiteurService.creerVisiteur(JTmatricule.getText(), JTnom.getText(), JTprenom.getText(),
                    JTlogin.getText(), new String(JPmdp.getPassword()), JTadresse.getText(),
                    JTcodePostal.getText(), JTtelephone.getText(), JTdateEntree.getText(),
                    JTprime.getText(), JTcodeUnite.getText(), JTnomUnite.getText());
            JOptionPane.showMessageDialog(this, "Le visiteur " + JTmatricule.getText().trim() + " a été ajouté.");
            viderText();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            placerCurseur(e.getMessage());
        }
    }

    /** Replace le curseur dans le champ concerné par l'erreur. */
    private void placerCurseur(String message) {
        if (message.startsWith("Le matricule"))            JTmatricule.requestFocus();
        else if (message.startsWith("Le nom de l'unité"))  JTnomUnite.requestFocus();
        else if (message.startsWith("Le nom"))             JTnom.requestFocus();
        else if (message.startsWith("Le prénom"))          JTprenom.requestFocus();
        else if (message.startsWith("Le login"))           JTlogin.requestFocus();
        else if (message.startsWith("Le mot de passe"))    JPmdp.requestFocus();
        else if (message.startsWith("L'adresse"))          JTadresse.requestFocus();
        else if (message.startsWith("Le code postal"))     JTcodePostal.requestFocus();
        else if (message.startsWith("Le téléphone"))       JTtelephone.requestFocus();
        else if (message.startsWith("La date"))            JTdateEntree.requestFocus();
        else if (message.startsWith("La prime"))           JTprime.requestFocus();
        else if (message.startsWith("Le code unité"))      JTcodeUnite.requestFocus();
    }

    private void viderText() {
        for (java.awt.Component c : pTexte.getComponents()) {
            if (c instanceof JTextField) {                // JPasswordField est aussi un JTextField
                ((JTextField) c).setText("");
            }
        }
        JTmatricule.requestFocus();
    }
}