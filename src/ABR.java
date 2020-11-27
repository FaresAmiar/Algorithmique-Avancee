import java.util.LinkedList;
import java.util.List;

public class ABR {

    private LinkedList<Objet> objetsDisponibles;
    private LinkedList<Objet> objetsCourants;
    private LinkedList<Objet> objetsSupprimes;
    private ABR filsGauche;
    private ABR filsDroit;
    private static float poids_max;
    private float borneMin;
    private float borneMax;

    public static List<Objet> getSolution() {
        return solution;
    }

    private static List<Objet> solution = new LinkedList<>();
    private static float valeurTotaleMax = 0;

    /**
     * Constructeur racine
     * @param objetsDisponibles : liste d'objet de depart
     * @param poids_max : poids maximum a ne pas depasser
     */
    public ABR(List<Objet> objetsDisponibles, float poids_max) {
        this.objetsCourants = new LinkedList<>();
        this.objetsDisponibles = new LinkedList<>(objetsDisponibles);
        this.objetsSupprimes = new LinkedList<>();
        this.filsDroit = null;
        this.filsGauche = null;
        this.poids_max = poids_max;
        this.borneMax = 0;
        this.borneMin = 0;
    }

    /**
     * Constructeur pour les fils
     * @param pere : pere du noeud qui va est créé
     */
    public ABR(ABR pere) {
        this.objetsCourants = new LinkedList<>(pere.objetsCourants);
        this.objetsDisponibles = new LinkedList<>(pere.objetsDisponibles);
        this.objetsSupprimes = new LinkedList<>(pere.objetsSupprimes);
        this.filsDroit = null;
        this.filsGauche = null;
        this.poids_max = poids_max;
        this.borneMax = 0;
        this.borneMin = pere.borneMin;
    }

    /**
     * Ajoute recursivement les fils dans l'arbre
     */
    public void ajout(){
        filsDroit = new ABR(this);
        filsGauche = new ABR(this);
        ajoutDroit();
        ajoutGauche();
        //Recursivité
        if(filsDroit != null)
            filsDroit.ajout();
        if(filsGauche != null)
            filsGauche.ajout();
    }

    /**
     * S'occupe de faire les changements d'objet dans les listes
     */
    public void ajoutGauche() {
        if(filsGauche.peutAjouter()) {
            filsGauche.objetsSupprimes.add(filsGauche.objetsDisponibles.removeFirst());
        }
        //Sinon on met a null pour la condition d'arret de recursivité
        else
            filsGauche = null;
    }

    /**
     * S'occupe de faire les changements d'objet dans les listes
     */
    public void ajoutDroit() {
        if(filsDroit.peutAjouter()) {
            filsDroit.objetsCourants.add(filsDroit.objetsDisponibles.removeFirst());
        }
        //Sinon on met a null pour la condition d'arret de recursivité
        else
            filsDroit = null;
    }

    /**
     *
     * @return si on peut ajouter un noeud dans l'arbre
     */
    public boolean peutAjouter() {
        //On met a jour les bornes (si la borne max est > a min), si le poids cumulé du sac + celui de l'objet suivant est < au poids max
        if(objetsDisponibles.size() != 0) {
            float poidsSac = 0, valeurSac = 0, poidsObjSuiv = objetsDisponibles.getFirst().getPoids();
            for (Objet o : objetsCourants)
                poidsSac += o.getPoids();
            for (Objet o : objetsCourants)
                valeurSac += o.getPrix();
            return majBornes() && ((poidsSac + poidsObjSuiv) < poids_max);
        }
        //Si on est en fin de branche et qu'il n'y a plus d'objet a ajouter on verifie si il est solution
        else {
            if(getValeurObjetsCourants() > valeurTotaleMax) {
                valeurTotaleMax = getValeurObjetsCourants();
                solution = objetsCourants;
            }
            return false;
        }
    }

    /**
     * Met a jour les bornes max et min pour l'arbre (noeud) correspondant
     * @return la borne max est supérieure a la borne min
     */
    public boolean majBornes(){
        if(objetsCourants.size() == 0) {
            for (Objet o : objetsDisponibles)
                borneMax += o.getPrix();
            return true;
        }

        borneMax = getValeurObjetsCourants();

        //Verifie si le noeud a la plus grande valeur et donc est solution
        if(borneMin == borneMax && borneMax > valeurTotaleMax) {
            valeurTotaleMax = borneMax;
            solution = objetsCourants;
            return false;
        }
        //Sinon on met a jour les bornes et on retourne que la borne max est > a min et on peut continuer
        else if(borneMax > borneMin) {
            borneMin = borneMax;
            for (Objet o : objetsDisponibles)
                borneMax += o.getPrix();
            return true;
        }
        return false;
    }

    /**
     *
     * @return Valeur du prix des objets courants de l'arbre (noeud)
     */
    public float getValeurObjetsCourants() {
        float val = 0;
        for(Objet o : objetsCourants) {
            val += o.getPrix();
        }
        return val;
    }

}
