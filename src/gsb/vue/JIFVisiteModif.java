package gsb.vue;

import gsb.modele.Stock;
import gsb.modele.Visite;
import gsb.service.StockService;
import gsb.service.VisiteService;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Mise à jour d'une visite effectuée : commentaire et médicaments offerts.
 */
public class JIFVisiteModif extends JInternalFrame {

    private static final long serialVersionUID = 1L;
    private static final String AUCUN = "(aucun)";

    private JTextField JTreference = new JTextField(8);
    private JLabel JLdate = new JLabel("-");
    private JLabel JLvisiteur = new JLabel("-");
    private JLabel JLmedecin = new JLabel("-");
    private JTextField JTcommentaire = new JTextField();
    private JComboBox<String> JCmed1 = new JComboBox<String>();
    private JComboBox<String> JCmed2 = new JComboBox<String>();
    private JTextField JTqte1 = new JTextField(4);
    private JTextField JTqte2 = new JTextField(4);
    private JButton JBmodifier = new JButton("Modifier");

    public JIFVisiteModif() {
        setTitle("Mise à jour d'une visite");

        // Ligne du haut : la référence et le bouton Afficher (Entrée fonctionne aussi)
        JButton JBafficher = new JButton("Afficher");
        JBafficher.addActionListener(e -> afficherVisite());
        JTreference.addActionListener(e -> afficherVisite());       // touche Entrée dans le champ
        JPanel pHaut = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pHaut.add(new JLabel("Référence"));
        pHaut.add(JTreference);
        pHaut.add(JBafficher);

        // Le formulaire
        JPanel pTexte = new JPanel(new GridLayout(0, 2, 8, 6));
        pTexte.add(new JLabel("Date visite"));  pTexte.add(JLdate);
        pTexte.add(new JLabel("Visiteur"));     pTexte.add(JLvisiteur);
        pTexte.add(new JLabel("Médecin"));      pTexte.add(JLmedecin);
        pTexte.add(new JLabel("Commentaire"));  pTexte.add(JTcommentaire);
        pTexte.add(new JLabel("Médicament 1")); pTexte.add(ligneMedicament(JCmed1, JTqte1));
        pTexte.add(new JLabel("Médicament 2")); pTexte.add(ligneMedicament(JCmed2, JTqte2));

        JBmodifier.addActionListener(e -> modifier());
        JPanel pBoutons = new JPanel();
        pBoutons.add(JBmodifier);

        getContentPane().add(pHaut, BorderLayout.NORTH);
        getContentPane().add(pTexte, BorderLayout.CENTER);
        getContentPane().add(pBoutons, BorderLayout.SOUTH);

        activerSaisie(false);                                        // rien d'affiché : tout est grisé
    }

    /** Un petit panneau « liste déroulante + Qté » pour un médicament. */
    private JPanel ligneMedicament(JComboBox<String> liste, JTextField quantite) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        p.add(liste);
        p.add(new JLabel("Qté"));
        p.add(quantite);
        return p;
    }

    /** Active ou désactive tout ce qui sert à la mise à jour. */
    private void activerSaisie(boolean actif) {
        JTcommentaire.setEnabled(actif);
        JCmed1.setEnabled(actif);
        JCmed2.setEnabled(actif);
        JTqte1.setEnabled(actif);
        JTqte2.setEnabled(actif);
        JBmodifier.setEnabled(actif);
    }

    private void afficherVisite() {
        try {
            Visite v = VisiteService.rechercherVisite(JTreference.getText());
            JLdate.setText(new SimpleDateFormat("dd/MM/yyyy").format(v.getDateVisite()));
            JLvisiteur.setText(v.getUnVisiteur().getMatricule() + " - " + v.getUnVisiteur().getNom()
                    + " " + v.getUnVisiteur().getPrenom());
            JLmedecin.setText(v.getUnMedecin().getCodeMed() + " - " + v.getUnMedecin().getNom()
                    + " " + v.getUnMedecin().getPrenom());
            JTqte1.setText("");
            JTqte2.setText("");

            boolean dejaEffectuee = v.getCommentaire() != null && !v.getCommentaire().trim().isEmpty();
            if (dejaEffectuee) {
                // on montre ce qui a été saisi, sans permettre de le modifier
                JTcommentaire.setText(v.getCommentaire());
                remplirAvecLaVisite(JCmed1, JTqte1, v.getMedicamentOffert1() == null ? null
                        : v.getMedicamentOffert1().getDepotLegal() + " - " + v.getMedicamentOffert1().getNomCommercial(),
                        v.getQuantiteOfferte1());
                remplirAvecLaVisite(JCmed2, JTqte2, v.getMedicamentOffert2() == null ? null
                        : v.getMedicamentOffert2().getDepotLegal() + " - " + v.getMedicamentOffert2().getNomCommercial(),
                        v.getQuantiteOfferte2());
                activerSaisie(false);
                JOptionPane.showMessageDialog(this, "Cette visite a déjà été effectuée : elle est affichée en consultation.");
            } else {
                JTcommentaire.setText("");
                remplirAvecLeStock(v.getUnVisiteur().getMatricule());
                activerSaisie(true);
                JTcommentaire.requestFocus();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            activerSaisie(false);
            JTreference.requestFocus();
            JTreference.selectAll();
        }
    }

    /** Remplit les deux listes avec le stock du visiteur, précédé de « (aucun) ». */
    private void remplirAvecLeStock(String matricule) throws Exception {
        JCmed1.removeAllItems();
        JCmed2.removeAllItems();
        JCmed1.addItem(AUCUN);
        JCmed2.addItem(AUCUN);
        for (Stock s : StockService.retournerStocksDuVisiteur(matricule)) {
            String libelle = s.getUnMedicament().getDepotLegal() + " - "
                    + s.getUnMedicament().getNomCommercial() + " (stock " + s.getQuantite() + ")";
            JCmed1.addItem(libelle);
            JCmed2.addItem(libelle);
        }
    }

    /** Affiche dans une liste le médicament déjà offert (consultation). */
    private void remplirAvecLaVisite(JComboBox<String> liste, JTextField qte, String libelle, int quantite) {
        liste.removeAllItems();
        liste.addItem(libelle == null ? AUCUN : libelle);
        qte.setText(libelle == null ? "" : String.valueOf(quantite));
    }

    /** Le dépôt légal choisi, ou "" pour « (aucun) ». */
    private String depotChoisi(JComboBox<String> liste) {
        String code = JIFStockVisiteur.codeChoisi(liste);
        return AUCUN.equals(code) ? "" : code;
    }

    private void modifier() {
        try {
            VisiteService.effectuerVisite(JTreference.getText(), JTcommentaire.getText(),
                    depotChoisi(JCmed1), JTqte1.getText(), depotChoisi(JCmed2), JTqte2.getText());
            JOptionPane.showMessageDialog(this, "La visite " + JTreference.getText().trim().toLowerCase()
                    + " a été enregistrée et le stock du visiteur mis à jour.");
            afficherVisite();                     // réaffiche : la visite passe en consultation
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            String m = e.getMessage();
            if (m.startsWith("Le commentaire")) {
                JTcommentaire.requestFocus();
            } else if (m.contains("médicament 2")) {
                JTqte2.requestFocus();
                JTqte2.selectAll();
            } else if (m.contains("médicament 1") || m.startsWith("Stock insuffisant")) {
                JTqte1.requestFocus();
                JTqte1.selectAll();
            }
        }
    }
}