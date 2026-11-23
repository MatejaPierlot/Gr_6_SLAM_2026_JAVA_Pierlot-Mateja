package gsb.vue;

import gsb.modele.Medicament;
import gsb.service.MedicamentService;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Liste de tous les médicaments, avec accès à la fiche détaillée.
 */
public class JIFMedicamentListe extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private MenuPrincipal fenetreContainer;
    private JTable table;
    private JTextField JTcode;

    public JIFMedicamentListe(MenuPrincipal uneFenetreContainer) {
        fenetreContainer = uneFenetreContainer;
        setTitle("Liste des médicaments");

        // 1. Le tableau : colonnes, puis une ligne par médicament
        String[] colonnes = {"Code", "Nom", "Famille"};
        DefaultTableModel modele = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int ligne, int colonne) {
                return false;                         // tableau en lecture seule
            }
        };
        for (Medicament m : MedicamentService.retournerLesMedicaments()) {
            modele.addRow(new Object[] {m.getDepotLegal(), m.getNomCommercial(), m.getLibelleFamille()});
        }
        table = new JTable(modele);

        // 2. Le double-clic sur une ligne ouvre la fiche
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int ligne = table.getSelectedRow();
                if (ligne >= 0) {
                    JTcode.setText((String) table.getValueAt(ligne, 0));   // un clic : le code s'affiche
                    if (e.getClickCount() == 2) {
                        ouvrirFiche();                                    // deux clics : la fiche s'ouvre
                    }
                }
            }
        });

        // 3. La zone de saisie et le bouton
        JTcode = new JTextField(10);
        JButton JBfiche = new JButton("Fiche médicament détaillée");
        JBfiche.addActionListener(e -> ouvrirFiche());

        JPanel pSaisie = new JPanel();
        pSaisie.add(new JLabel("Code"));
        pSaisie.add(JTcode);
        pSaisie.add(JBfiche);

        getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
        getContentPane().add(pSaisie, BorderLayout.SOUTH);
    }

    /**
     * Ouvre la fiche du médicament dont le code est saisi, ou affiche l'erreur du Service.
     */
    private void ouvrirFiche() {
        try {
            Medicament m = MedicamentService.rechercherMedicament(JTcode.getText());
            fenetreContainer.ouvrirFenetre(new JIFMedicamentFiche(m));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            JTcode.requestFocus();                    // replace le curseur dans le champ
        }
    }
}