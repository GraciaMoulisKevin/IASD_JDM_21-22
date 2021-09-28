package Test;

import Structure.Graph;

public class test1 {
    public static void main(String args[]){
        String text = "Salut je suis une phrase de test c'est super!";
        String words[] = text.split("\\W+");
        Graph g = new Graph(words);

        System.out.println(g.toString());
        System.out.println(g.getAllNode().toString());
    }
}