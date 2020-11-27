import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.security.DigestInputStream;
import java.util.*;


public class SacADos {

    private float poids_max;
    private String chemin;
    private List<Objet> objets;
    private List<Objet> objetsResolus = new LinkedList<>();

    public SacADos(){}
    public SacADos(String chemin, float poids_max) {
        this.poids_max = poids_max;
        this.chemin = chemin;
    }


    /**
     *  Recupere la liste d'objets et la stocke dans l'attribut objets
     */
    public void recupererListe() {
        Scanner sc = null;
        objets = new LinkedList<>();
        try {
            sc = new Scanner(new FileReader(chemin));
            while(sc.hasNextLine()) {
                String ligne = sc.nextLine();
                String[] tabLigne = ligne.split(";");
                objets.add(new Objet(tabLigne[0],Float.parseFloat(tabLigne[1]),Float.parseFloat(tabLigne[2])));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(sc!=null) {sc.close();}
        }
    }

    /**
     * Methode de resolution par algorithme glouton
     */
    public void resoudreGlouton(){
        float poidsActuel = 0;
        Collections.sort(objets);

        //Pour toute la liste ajoute l'objet et verifie si le poids de la liste des resolus + celui de l'objet ne depasse pas le poids max
        // si oui on l'ajoute a la liste resolue
        for(int i = 0;  i < objets.size() ; ++i) {
            if(poidsActuel + objets.get(i).getPoids() < poids_max) {
                objetsResolus.add(objets.get(i));
                poidsActuel += objets.get(i).getPoids();
            }
        }
    }


    /**
     * Resolution par prog. Dynamique
     */
    public void resoudreDynamique(){
        objetsResolus.clear();
        int[][] tab = new int[objets.size()][(int) (poids_max * 100) + 1];

        //Ajout de la valeur du premier objet dans le tableau sur la premiere ligne
        for (int j = 0; j < tab[0].length; ++j) {
            if ((int) (objets.get(0).getPoids() * 100) > j)
                tab[0][j] = 0;
            else
                tab[0][j] = (int) (objets.get(0).getPrix() * 100);
        }

        //Ajoute les autres valeurs des objets
        for (int i = 1; i < tab.length; ++i) {
            for (int j = 0; j < tab[0].length; ++j) {
                if ((int) (objets.get(i).getPoids() * 100) > j)
                    tab[i][j] = tab[i - 1][j];
                //On regard la plus grande valeur entre la valeur de l'objet courant et la valeur de l'objet courant et celle de l'objet precedent (indice j - poids de l'objet i)
                else
                    tab[i][j] = Math.max(tab[i - 1][j], tab[i - 1][j - ((int) (objets.get(i).getPoids() * 100))] + ((int) (objets.get(i).getPrix() * 100)));
            }
        }

        int j = tab[0].length - 1;
        int i = tab.length - 1;
        //Trouve la colonne correspondant a l'objet
        while(tab[i][j] == tab[i][j-1])
            j--;


        //Verifie si l'objet est ajouté (meme valeur qu'en haut) sinon on remonte dans le tableau (ligne)
        while(j>0) {
            while (i > 0 && (tab[i][j] == tab[i - 1][j]))
                i--;
            //On se décale en colonne par rapport au poids de l'objet i
            j -= (int) (objets.get(i).getPoids() * 100);
            if (j >= 0) {
                objetsResolus.add(objets.get(i));
                i--;
            }
        }
    }

    /**
     * Resoud le probleme par PSE
     */
    public void resoudrePSE(){
        resoudreGlouton();
        ABR arbre = new ABR(objetsResolus, poids_max);
        arbre.ajout();
        objetsResolus = ABR.getSolution();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(Objet o : objetsResolus) {
            sb.append(o.getNom() + "; ");
            sb.append(o.getPoids() + "; ");
            sb.append(o.getPrix() + "\n");
        }
        return sb.toString();
    }
}

