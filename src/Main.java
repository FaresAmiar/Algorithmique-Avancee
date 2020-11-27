public class Main {

    public static void main(String[] args) {
        SacADos s = new SacADos(args[0],Float.parseFloat(args[1]));
        s.recupererListe();
        switch (args[2]) {
            case "gloutonne" : s.resoudreGlouton();
            break;

            case "Dynamique" : s.resoudreDynamique();
            break;

            case "PSE" : s.resoudrePSE();
            break;
        }
        System.out.println(s.toString());

        /*
        float poids = 4.3f;
        SacADos s = new SacADos("liste.txt",poids);
        s.recupererListe();

        s.resoudreGlouton();
        System.out.println(s.toString() + "\n");

        s.resoudreDynamique();
        System.out.println(s.toString() + "\n");

        s.resoudrePSE();
        System.out.println(s.toString());
        */

    }
}
