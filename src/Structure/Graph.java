package Structure;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.Collection;

public class Graph {
    org.jgrapht.Graph<InterfaceNode, DefaultEdge> g;

    public Graph(String text){
        g = new DefaultDirectedWeightedGraph<>(DefaultEdge.class);
        loadSentence(text);
    }

    /**
     * Formation des nodes de base + Start / End
     * @param text
     */
    private void loadSentence(String text){
        Start start = new Start();
        End end = new End();

        g.addVertex(start);
        g.addVertex(end);

        int count = 0;
        String words[] = text.split("\\W+");

        Node preNode = new Node("");

        for (String word : words){
            Node node = new Node(word);
            g.addVertex(node);
            if (count == 0) g.addEdge(start,node);
            else if (count == words.length-1) g.addEdge(node,end);
            else g.addEdge(preNode,node);
            count++;
            preNode = node;
        }
    }

    /**
     * Get all edges between node 1 et node 2
     * @param n1
     * @param n2
     * @return
     */
    public Collection<DefaultEdge> getAllEdges(InterfaceNode n1, InterfaceNode n2){
        return g.getAllEdges(n1, n2);
    }

    public Collection<DefaultEdge> getAllNode(){
        return g.edgeSet();
    }

    public Collection<DefaultEdge> getNode(InterfaceNode n1){
        return g.edgesOf(n1);
    }

    public String toString(){
        return g.toString();
    }

}
