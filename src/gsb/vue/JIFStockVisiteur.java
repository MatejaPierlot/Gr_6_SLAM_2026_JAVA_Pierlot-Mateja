package gsb.vue;

import gsb.modele.Stock;
import gsb.modele.Visiteur;
import gsb.service.StockService;
import gsb.service.VisiteurService;

import java.awt.BorderLayout;

import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Stock d'échantillons d'un visiteur choisi dans une liste déroulante.
 */
public class JIFStockVisiteur extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private JComboBox<String> JCvisiteurs;
    private DefaultTableModel modele;

    public JIFStockVisiteur() {
        setTitle("Stock d'échantillons d'un visiteur");

        JCvisiteurs = listeDesVisiteurs();
        JCvisiteurs.addActionListener(e -> afficherStock());     // choix = mise à jour, sans bouton

        JPanel pHaut = new JPanel();
        pHaut.add(new JLabel("Visiteur"));
        pHaut.add(JCvisiteurs);

        modele = new DefaultTableModel(new String[] {"Dépôt légal", "Nom commercial", "Stock"}, 0) {
            @Override
            public boolean isCellEditable(int ligne, int colonne) {
                return false;
            }
        };

        getContentPane().add(pHaut, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(new JTable(modele)), BorderLayout.CENTER);

        afficherStock();
    }

    /** Liste déroulante « matricule - nom prénom » de tous les visiteurs. Réutilisée par d'autres écrans. */
    public static JComboBox<String> listeDesVisiteurs() {
        JComboBox<String> liste = new JComboBox<String>();
        for (Visiteur v : VisiteurService.retournerLesVisiteurs()) {
            liste.addItem(v.getMatricule() + " - " + v.getNom() + " " + v.getPrenom());
        }
        return liste;
    }

    /** Garde le code au début d'un choix « code - libellé ». Réutilisée par d'autres écrans. */
    public static String codeChoisi(JComboBox<String> liste) {
        Object choix = liste.getSelectedItem();
        return choix == null ? "" : choix.toString().split(" - ")[0];
    }

    private void afficherStock() {
        modele.setRowCount(0);
        try {
            for (Stock s : StockService.retournerStocksDuVisiteur(codeChoisi(JCvisiteurs))) {
                modele.addRow(new Object[] {s.getUnMedicament().getDepotLegal(),
                        s.getUnMedicament().getNomCommercial(), s.getQuantite()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}