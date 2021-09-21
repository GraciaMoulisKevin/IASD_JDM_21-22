package Test;

import Structure.Graph;

public class test1 {
    public static void main(String args[]){
        String texte = "Salut je suis une phrase de test c'est super!";
        Graph g = new Graph(texte);

        System.out.println(g.toString());
        System.out.println(g.getAllNode().toString());
    }
}
