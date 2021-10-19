package Test;

import Structure.AbstractEdge;
import Structure.Graph;
import org.jgrapht.GraphPath;

public class test2 {
    public static void main(String args[]){
        String text = "avant toute chose";
        String words[] = text.split("\\W+");
        Graph g = new Graph(words);

        System.out.println(g);
        System.out.println(g.getAllNode()+"\n");

        // Test done _ la donnée est gardé dans via les méthodes protected de DefaultEdge
        System.out.println(g.getEdgesFollowedBy());
        System.out.println(g.getEdgesLemme()+"\n");

        System.out.println(g.getGraphFollowedBy()+"\n");

        // System.out.println(g.getAllEdges(g.getTmp(),g.getEnd()));

        System.out.println("Nb de chemin possible : " + g.getAllPaths().size());

        // test mise en forme pour multi mot

        // TEST
        // multiMots
    }
}
