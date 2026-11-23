package gsb.modele;

import java.util.Date;

/**
 * Une visite d'un visiteur médical chez un médecin (table VISITE),
 * avec au plus deux médicaments offerts.
 */
public class Visite {

    protected String reference;
    protected Date dateVisite;
    protected String commentaire;
    protected Visiteur unVisiteur;
    protected Medecin unMedecin;
    protected Medicament medicamentOffert1;
    protected int quantiteOfferte1;
    protected Medicament medicamentOffert2;
    protected int quantiteOfferte2;

    /**
     * Construit une visite ; les paramètres suivent l'ordre des colonnes de la table VISITE.
     */
    public Visite(String reference, Date dateVisite, String commentaire, Visiteur unVisiteur,
            Medecin unMedecin, Medicament medicamentOffert1, int quantiteOfferte1,
            Medicament medicamentOffert2, int quantiteOfferte2) {
        this.reference = reference;
        this.dateVisite = dateVisite;
        this.commentaire = commentaire;
        this.unVisiteur = unVisiteur;
        this.unMedecin = unMedecin;
        this.medicamentOffert1 = medicamentOffert1;
        this.quantiteOfferte1 = quantiteOfferte1;
        this.medicamentOffert2 = medicamentOffert2;
        this.quantiteOfferte2 = quantiteOfferte2;
    }

    /**
     * Construit une visite planifiée : sans commentaire ni médicament offert.
     */
    public Visite(String reference, Date dateVisite, Visiteur unVisiteur, Medecin unMedecin) {
        this(reference, dateVisite, null, unVisiteur, unMedecin, null, 0, null, 0);
    }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public Date getDateVisite() { return dateVisite; }
    public void setDateVisite(Date dateVisite) { this.dateVisite = dateVisite; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public Visiteur getUnVisiteur() { return unVisiteur; }
    public void setUnVisiteur(Visiteur unVisiteur) { this.unVisiteur = unVisiteur; }

    public Medecin getUnMedecin() { return unMedecin; }
    public void setUnMedecin(Medecin unMedecin) { this.unMedecin = unMedecin; }

    public Medicament getMedicamentOffert1() { return medicamentOffert1; }
    public void setMedicamentOffert1(Medicament m) { this.medicamentOffert1 = m; }

    public int getQuantiteOfferte1() { return quantiteOfferte1; }
    public void setQuantiteOfferte1(int q) { this.quantiteOfferte1 = q; }

    public Medicament getMedicamentOffert2() { return medicamentOffert2; }
    public void setMedicamentOffert2(Medicament m) { this.medicamentOffert2 = m; }

    public int getQuantiteOfferte2() { return quantiteOfferte2; }
    public void setQuantiteOfferte2(int q) { this.quantiteOfferte2 = q; }
}