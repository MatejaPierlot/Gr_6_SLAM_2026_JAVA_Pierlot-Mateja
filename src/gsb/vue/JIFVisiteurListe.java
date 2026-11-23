package gsb.vue;

import gsb.modele.Visiteur;
import gsb.service.VisiteurService;

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
 * Liste de tous les visiteurs, avec accès à la fiche détaillée.
 */
public class JIFVisiteurListe extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private MenuPrincipal fenetreContainer;
    private JTable table;
    private JTextField JTmatricule;

    public JIFVisiteurListe(MenuPrincipal uneFenetreContainer) {
        fenetreContainer = uneFenetreContainer;
        setTitle("Liste des visiteurs");

        DefaultTableModel modele = new DefaultTableModel(new String[] {"Matricule", "Nom", "Prénom"}, 0) {
            @Override
            public boolean isCellEditable(int ligne, int colonne) {
                return false;
            }
        };
        for (Visiteur v : VisiteurService.retournerLesVisiteurs()) {
            modele.addRow(new Object[] {v.getMatricule(), v.getNom(), v.getPrenom()});
        }
        table = new JTable(modele);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int ligne = table.getSelectedRow();
                if (ligne >= 0) {
                    JTmatricule.setText((String) table.getValueAt(ligne, 0));
                    if (e.getClickCount() == 2) {
                        ouvrirFiche();
                    }
                }
            }
        });

        JTmatricule = new JTextField(6);
        JButton JBfiche = new JButton("Fiche visiteur détaillée");
        JBfiche.addActionListener(e -> ouvrirFiche());

        JPanel pSaisie = new JPanel();
        pSaisie.add(new JLabel("Matricule"));
        pSaisie.add(JTmatricule);
        pSaisie.add(JBfiche);

        getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
        getContentPane().add(pSaisie, BorderLayout.SOUTH);
    }

    private void ouvrirFiche() {
        try {
            Visiteur v = VisiteurService.rechercherVisiteur(JTmatricule.getText());
            fenetreContainer.ouvrirFenetre(new JIFVisiteurFiche(v));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            JTmatricule.requestFocus();
        }
    }
}