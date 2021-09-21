package Test;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultEdge;

public class Main {
    public static void main(String args[]){
        Graph<String, DefaultEdge> g = new DefaultDirectedWeightedGraph<String, DefaultEdge>(DefaultEdge.class);

        g.addVertex("a");
        g.addVertex("b");
        g.addVertex("c");

        g.addEdge("a", "b");
        g.addEdge("b", "c");
        g.addEdge("a", "c");

        System.out.println(g.toString());
    }
    /**
     Classe pour le graph
     => forme ens noeud
     graphe mot de base + lemme + multi mot etcetc

     ensemble copiant les ensembles afin de rechercher plus rapidement les éléments
     méthode exploration
     classe abstract : noeud (id+string) puis spécification avec start / end pour début fin graphe (exemple) etc etc
     node word (ex)
     */
}
