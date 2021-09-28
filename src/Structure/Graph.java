package Structure;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;

import java.util.Collection;

public class Graph {
    org.jgrapht.Graph<AbstractNode, AbstractEdge> g;

    public Graph(String[] words){
        g = new DefaultDirectedWeightedGraph<AbstractNode, AbstractEdge>(AbstractEdge.class);
        loadSentence(words);
        loadLemmes();
        loadMultiWords();
    }

    /**
     * Formation des nodes de base + Start / End
     * @param words
     *
     * --- question : comment set le type d'Edge qu'on utilise afin de mettre Suivant ici
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
            if (count == 0) {
                NextTo edgeStart = new NextTo("");
                g.addEdge(start, node, edgeStart);
            } else if (count == words.length-1) {
                NextTo edgeEnd = new NextTo("");
                g.addEdge(node,end, edgeEnd);
            } else {
                NextTo edgeNextTo = new NextTo("");
                g.addEdge(preNode,node,edgeNextTo);
            }
            count++;
            preNode = node;
        }
    }

    /**
     * Set Lemme of each Word
     */
    private void loadLemmes(){

    }

    /**
     * Set MultiWords
     */
    private void loadMultiWords(){

    }

    /***********************************
     * FONCTIONS MANIPULATION DU GRAPH *
     **********************************/
    /**
     * Get all edges between node 1 et node 2
     * @param n1
     * @param n2
     * @return
     */
    public Collection<AbstractEdge> getAllEdges(AbstractNode n1, AbstractNode n2){ return g.getAllEdges(n1, n2); }

    /***** EN COURS *****/
    public Collection<AbstractEdge> getAllNode(){
        return g.edgeSet();
    }

    public Collection<AbstractEdge> getNode(AbstractNode n1){
        return g.edgesOf(n1);
    }

    /** OTHER **/
    public String toString(){
        return g.toString();
    }

}
