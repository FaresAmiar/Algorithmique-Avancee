public class Objet implements Comparable<Objet> {
    private String nom;
    private float prix, poids;

    public Objet() {}
    public Objet(String nom, float poids, float prix) { this.nom = nom; this.prix = prix; this.poids = poids;}

    public float getPrix() {return prix;}
    public float getPoids() {return poids;}
    public String getNom() {return nom;}

    @Override
    public int compareTo(Objet o) {
            return ( ( (prix / poids) < (o.prix) / (o.poids) ) ? 1 : ( (prix/poids) ==  (o.prix) / (o.poids) ) ? 0 : -1 );
    }
}
