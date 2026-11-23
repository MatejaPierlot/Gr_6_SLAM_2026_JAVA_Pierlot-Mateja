package gsb.vue;

import gsb.modele.Visiteur;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Formulaire commun aux écrans visiteur (fiche, ajout).
 */
public class JIFVisiteur extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    protected JPanel pTexte;
    protected JPanel pBoutons;

    protected JTextField JTmatricule = new JTextField();
    protected JTextField JTnom = new JTextField();
    protected JTextField JTprenom = new JTextField();
    protected JTextField JTadresse = new JTextField();
    protected JTextField JTcodePostal = new JTextField();
    protected JTextField JTville = new JTextField();
    protected JTextField JTtelephone = new JTextField();
    protected JTextField JTdateEntree = new JTextField();
    protected JTextField JTprime = new JTextField();
    protected JTextField JTcodeUnite = new JTextField();
    protected JTextField JTnomUnite = new JTextField();

    public JIFVisiteur() {
        pTexte = new JPanel(new GridLayout(0, 2, 8, 6));   // 2 colonnes, autant de lignes que nécessaire
        pBoutons = new JPanel();

        ajouterLigne("Matricule", JTmatricule);
        ajouterLigne("Nom", JTnom);
        ajouterLigne("Prénom", JTprenom);
        ajouterLigne("Adresse", JTadresse);
        ajouterLigne("Code postal", JTcodePostal);
        ajouterLigne("Ville", JTville);
        ajouterLigne("Téléphone", JTtelephone);
        ajouterLigne("Date d'entrée (jj/mm/aaaa)", JTdateEntree);
        ajouterLigne("Prime", JTprime);
        ajouterLigne("Code unité", JTcodeUnite);
        ajouterLigne("Nom unité", JTnomUnite);

        getContentPane().add(pTexte, BorderLayout.CENTER);
        getContentPane().add(pBoutons, BorderLayout.SOUTH);
    }

    /** Ajoute une étiquette et son champ sur une nouvelle ligne du formulaire. */
    protected void ajouterLigne(String libelle, java.awt.Component champ) {
        pTexte.add(new JLabel(libelle));
        pTexte.add(champ);
    }

    /**
     * Remplit les champs avec les données d'un visiteur.
     * @param v le visiteur à afficher
     */
    public void remplirText(Visiteur v) {
        JTmatricule.setText(v.getMatricule());
        JTnom.setText(v.getNom());
        JTprenom.setText(v.getPrenom());
        JTadresse.setText(v.getAdresse());
        if (v.getUneLocalite() != null) {
            JTcodePostal.setText(v.getUneLocalite().getCodePostal());
            JTville.setText(v.getUneLocalite().getVille());
        }
        JTtelephone.setText(v.getTelephone());
        if (v.getDateEntree() != null) {
            JTdateEntree.setText(new SimpleDateFormat("dd/MM/yyyy").format(v.getDateEntree()));
        }
        JTprime.setText(String.valueOf(v.getPrime()));
        JTcodeUnite.setText(v.getCodeUnite());
        JTnomUnite.setText(v.getNomUnite());
    }
}