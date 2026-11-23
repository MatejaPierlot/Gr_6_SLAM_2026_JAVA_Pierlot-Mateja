package gsb.vue;

import gsb.modele.Medicament;
import gsb.service.MedicamentService;

import java.awt.BorderLayout;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Médicaments d'une famille choisie dans une liste déroulante.
 */
public class JIFMedicamentFamille extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private JComboBox<String> JCfamilles;
    private DefaultTableModel modele;

    public JIFMedicamentFamille() {
        setTitle("Médicaments par famille");

        // 1. La liste déroulante, remplie à partir du dictionnaire trié
        TreeMap<String, String> lesFamilles = MedicamentService.retournerLesFamilles();
        JCfamilles = new JComboBox<String>();
        for (Map.Entry<String, String> famille : lesFamilles.entrySet()) {
            JCfamilles.addItem(famille.getKey() + " - " + famille.getValue());
        }
        JCfamilles.addActionListener(e -> afficherMedicaments());   // choix = mise à jour, sans bouton

        JPanel pHaut = new JPanel();
        pHaut.add(new JLabel("Famille"));
        pHaut.add(JCfamilles);

        // 2. Le tableau (lecture seule)
        String[] colonnes = {"Dépôt légal", "Nom commercial", "Composition", "Effets", "Contre-indications"};
        modele = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int ligne, int colonne) {
                return false;
            }
        };
        JTable table = new JTable(modele);

        // 3. Le bouton Fermer
        JButton JBfermer = new JButton("Fermer");
        JBfermer.addActionListener(e -> dispose());
        JPanel pBas = new JPanel();
        pBas.add(JBfermer);

        getContentPane().add(pHaut, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
        getContentPane().add(pBas, BorderLayout.SOUTH);

        afficherMedicaments();   // affiche directement la première famille
    }

    /**
     * Vide le tableau puis le remplit avec les médicaments de la famille choisie.
     */
    private void afficherMedicaments() {
        modele.setRowCount(0);                                   // vide le tableau
        String choix = (String) JCfamilles.getSelectedItem();    // ex. "AAH - Antihistaminique"
        if (choix == null) return;
        String codeFamille = choix.split(" - ")[0];              // garde "AAH"
        for (Medicament m : MedicamentService.retournerMedicamentsDeLaFamille(codeFamille)) {
            modele.addRow(new Object[] {m.getDepotLegal(), m.getNomCommercial(),
                    m.getComposition(), m.getEffets(), m.getContreIndication()});
        }
    }
}