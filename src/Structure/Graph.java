package Structure;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultEdge;

public class Graph {
    org.jgrapht.Graph<AbstractEdge, DefaultEdge> g;

    public Graph(String text){
        g = new DefaultDirectedWeightedGraph<>(DefaultEdge.class);
        genStartEnd();
        loadSentence(text);
    }

    private void genStartEnd(){
        Start start = new Start();
        End end = new End();
        g.addVertex(start);
        g.addVertex(end);
    }

    private void loadSentence(String text){
        
    }

}
