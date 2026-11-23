package gsb.modele;

/**
 * Stock d'échantillons d'un médicament détenu par un visiteur (table STOCK).
 */
public class Stock {

    protected Visiteur unVisiteur;
    protected Medicament unMedicament;
    protected int quantite;

    public Stock(Visiteur unVisiteur, Medicament unMedicament, int quantite) {
        this.unVisiteur = unVisiteur;
        this.unMedicament = unMedicament;
        this.quantite = quantite;
    }

    public Visiteur getUnVisiteur() { return unVisiteur; }
    public void setUnVisiteur(Visiteur unVisiteur) { this.unVisiteur = unVisiteur; }

    public Medicament getUnMedicament() { return unMedicament; }
    public void setUnMedicament(Medicament unMedicament) { this.unMedicament = unMedicament; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
}