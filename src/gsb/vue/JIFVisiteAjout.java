package gsb.vue;

import gsb.modele.Medecin;
import gsb.service.MedecinService;
import gsb.service.VisiteService;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Écran de création d'une visite planifiée (Annexe 9.1).
 */
public class JIFVisiteAjout extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private JTextField JTreference = new JTextField();
    private JTextField JTdate = new JTextField();
    private JTextField JTcommentaire = new JTextField();
    private JComboBox<String> JCvisiteurs;
    private JComboBox<String> JCmedecins;

    public JIFVisiteAjout() {
        setTitle("Ajout d'une visite");

        JTdate.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));   // date du jour

        JTcommentaire.setEnabled(false);                                          // rempli à la mise à jour
        JTcommentaire.setToolTipText("Le commentaire sera saisi après la visite.");

        JCvisiteurs = JIFStockVisiteur.listeDesVisiteurs();                       // outil partagé
        JCmedecins = new JComboBox<String>();
        for (Medecin m : MedecinService.retournerLesMedecins()) {
            JCmedecins.addItem(m.getCodeMed() + " - " + m.getNom() + " " + m.getPrenom());
        }

        JPanel pTexte = new JPanel(new GridLayout(0, 2, 8, 6));
        pTexte.add(new JLabel("Référence (ex. v0024)"));  pTexte.add(JTreference);
        pTexte.add(new JLabel("Date visite (jj/mm/aaaa)")); pTexte.add(JTdate);
        pTexte.add(new JLabel("Commentaire"));             pTexte.add(JTcommentaire);
        pTexte.add(new JLabel("Matricule visiteur"));      pTexte.add(JCvisiteurs);
        pTexte.add(new JLabel("Code médecin"));            pTexte.add(JCmedecins);

        JButton JBajouter = new JButton("Ajouter");
        JBajouter.addActionListener(e -> ajouter());
        JPanel pBoutons = new JPanel();
        pBoutons.add(JBajouter);

        getContentPane().add(pTexte, BorderLayout.CENTER);
        getContentPane().add(pBoutons, BorderLayout.SOUTH);
    }

    private void ajouter() {
        try {
            VisiteService.creerVisite(JTreference.getText(), JTdate.getText(),
                    JIFStockVisiteur.codeChoisi(JCvisiteurs), JIFStockVisiteur.codeChoisi(JCmedecins));
            JOptionPane.showMessageDialog(this, "La visite " + JTreference.getText().trim().toLowerCase()
                    + " du " + JTdate.getText().trim() + " a été planifiée.");
            JTreference.setText("");
            JTreference.requestFocus();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            if (e.getMessage().startsWith("La référence")) {
                JTreference.requestFocus();
                JTreference.selectAll();
            } else if (e.getMessage().startsWith("La date")) {
                JTdate.requestFocus();
                JTdate.selectAll();
            }
        }
    }
}