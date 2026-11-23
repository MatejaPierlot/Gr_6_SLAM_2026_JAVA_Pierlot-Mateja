package gsb.vue;

import gsb.modele.Medicament;
import gsb.service.MedicamentService;
import gsb.service.StockService;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Ajout d'échantillons de médicaments au stock d'un visiteur.
 */
public class JIFStockAjout extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private JComboBox<String> JCvisiteurs;
    private JComboBox<String> JCmedicaments;
    private JTextField JTquantite;

    public JIFStockAjout() {
        setTitle("Ajout d'échantillons pour un visiteur");

        JCvisiteurs = JIFStockVisiteur.listeDesVisiteurs();       // outil partagé
        JCmedicaments = new JComboBox<String>();
        for (Medicament m : MedicamentService.retournerLesMedicaments()) {
            JCmedicaments.addItem(m.getDepotLegal() + " - " + m.getNomCommercial());
        }
        JTquantite = new JTextField(6);

        JPanel pTexte = new JPanel(new GridLayout(0, 2, 8, 6));
        pTexte.add(new JLabel("Visiteur"));     pTexte.add(JCvisiteurs);
        pTexte.add(new JLabel("Médicament"));   pTexte.add(JCmedicaments);
        pTexte.add(new JLabel("Quantité"));     pTexte.add(JTquantite);

        JButton JBajouter = new JButton("Ajouter quantité");
        JBajouter.addActionListener(e -> ajouter());
        JPanel pBoutons = new JPanel();
        pBoutons.add(JBajouter);

        getContentPane().add(pTexte, BorderLayout.CENTER);
        getContentPane().add(pBoutons, BorderLayout.SOUTH);
    }

    private void ajouter() {
        try {
            StockService.ajouterEchantillons(JIFStockVisiteur.codeChoisi(JCvisiteurs),
                    JIFStockVisiteur.codeChoisi(JCmedicaments), JTquantite.getText());
            JOptionPane.showMessageDialog(this, JTquantite.getText().trim() + " échantillon(s) de "
                    + JCmedicaments.getSelectedItem() + " ajouté(s) au stock de " + JCvisiteurs.getSelectedItem() + ".");
            JTquantite.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            JTquantite.requestFocus();
            JTquantite.selectAll();                               // sélectionne la valeur fausse
        }
    }
}