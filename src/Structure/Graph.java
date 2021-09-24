package Structure;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;

import java.util.Collection;

public class Graph {
    org.jgrapht.Graph<AbstractNode, AbstractEdge> g;

    public Graph(String[] words){
        g = new DefaultDirectedWeightedGraph<AbstractNode, AbstractEdge>(AbstractEdge.class);
        loadSentence(words);
    }

    /**
     * Formation des nodes de base + Start / End
     * @param words
     */
    private void loadSentence(String[] words){
        Start start = new Start();
        End end = new End();

        g.addVertex(start);
        g.addVertex(end);

        int count = 0;;

        Word preNode = new Word("");

        for (String word : words){
            Word node = new Word(word);
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
    public Collection<AbstractEdge> getAllEdges(AbstractNode n1, AbstractNode n2){ return g.getAllEdges(n1, n2); }

    public Collection<AbstractEdge> getAllNode(){
        return g.edgeSet();
    }

    public Collection<AbstractEdge> getNode(AbstractNode n1){
        return g.edgesOf(n1);
    }

    public String toString(){
        return g.toString();
    }

}
