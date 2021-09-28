package Structure;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.util.ArrayUnenforcedSet;

import java.sql.Array;
import java.util.*;

public class Graph {
    org.jgrapht.Graph<AbstractNode, AbstractEdge> g;
    End end; Start start;

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
        Start _start = new Start(); start = _start;
        End _end = new End(); end = _end;

        g.addVertex(start);
        g.addVertex(end);

        int count = 0;;

        Word preNode = new Word("");

        for (String word : words){
            Word node = new Word(word);
            g.addVertex(node);
            if (count == 0) {
                FollowedBy edgeStart = new FollowedBy("");
                g.addEdge(start, node, edgeStart);
            } else {
                FollowedBy edgeFollowedBy = new FollowedBy(preNode+","+node);
                g.addEdge(preNode,node,edgeFollowedBy);
            }
            count++;
            preNode = node;
        }
        FollowedBy edgeEnd = new FollowedBy(preNode+",");
        g.addEdge(preNode,end, edgeEnd);
    }

    /**
     * Set Lemme of each Word
     *
     * NE FONCTIONNE PAS CAR getAllNode ne conserve pas l'ordre les noeuds il faut avancer pas à pas en partant de START
     */
    protected void loadLemmes(){
        int cpt = 0;
        String disp = "START";

        Set<AbstractNode> nodes = getAllNode();
        Set<AbstractNode> set = new HashSet<>();
        for (AbstractNode node : nodes){
            set.add(node);
        }

        int limit = nodes.size();
        Lemme pre_lemme = new Lemme("");
        for (AbstractNode word : set){
            System.out.println("DEBUG ============ "+word.getData());
            if (word.getType().equals("MOT")){
                Lemme lemme = new Lemme(word.getData());
                Edge edge = new Edge(disp+","+lemme,"LEMME");
                g.addVertex(lemme);
                if (cpt == 0){
                    g.addEdge(getStart(),lemme,edge);
                }else if (cpt == limit){
                    g.addEdge(lemme,getEnd(),edge);
                }else{
                    g.addEdge(pre_lemme,lemme,edge);
                }
                g.addEdge(word,lemme,edge);
                pre_lemme = lemme; disp = lemme.getData();
                cpt++;
                if (cpt == limit) disp = "END";
            }
        }
    }

    /**
     * Set MultiWords
     */
    protected void loadMultiWords(){
        // truc de Quentin ici
    }

    /***********************************
     * FONCTIONS MANIPULATION DU GRAPH *
     **********************************/

    protected void setMultiMots(AbstractNode n1,AbstractNode n2,String multiMots){

    }

    /*****************************
     * FONCTIONS ACCESS DU GRAPH *
     ****************************/

    public End getEnd(){ return end; }
    public Start getStart(){ return start; }

    /**
     * @param n1
     * @param n2
     * @return all edges between node 1 et node 2
     */
    public Set<AbstractEdge> getAllEdges(AbstractNode n1, AbstractNode n2){ return g.getAllEdges(n1, n2); }
    /**
     * @return all edges
     */
    public Set<AbstractEdge> getAllEdges(){ return g.edgeSet(); }
    /**
     * @return all Nodes
     */
    public Set<AbstractNode> getAllNode(){ return g.vertexSet(); }

    /**
     * @return set des Edges de type FOLLOWED_BY
     */
    public Set<AbstractEdge> getEdgesFollowedBy(){
        Set<AbstractEdge> set = new HashSet<>();
        for (AbstractEdge edge : getAllEdges()){
            if( edge.getType().equals("FOLLOWED_BY") ) set.add(edge);
        }
        return set;
    }

    /**
     * @return set des Edges de type LEMME
     */
    public Set<AbstractEdge> getEdgesLemme(){
        Set<AbstractEdge> set = new HashSet<>();
        for (AbstractEdge edge : getAllEdges()){
            if( edge.getType().equals("LEMME") ) set.add(edge);
        }
        return set;
    }

    /**
     * @return subGraph comprenant uniquement les arcs de type Followed By
     */
    public AsSubgraph<AbstractNode, AbstractEdge> getGraphFollowedBy(){
        AsSubgraph<AbstractNode, AbstractEdge> graph =
                new AsSubgraph<AbstractNode, AbstractEdge>(g, getAllNode(), getEdgesFollowedBy());
        return graph;
    }

    /**
     * @return List de GraphPath comprenant tous les chemins possible entre START & END
     */
    public List<GraphPath<AbstractNode, AbstractEdge>> getAllPaths(){
        AllDirectedPaths<AbstractNode, AbstractEdge> graph = new AllDirectedPaths<AbstractNode, AbstractEdge>(g);

        List<GraphPath<AbstractNode, AbstractEdge>> allPaths =
                graph.getAllPaths(getEnd(), getStart(), true, null);

        return allPaths;
    }

    /**********
     * OTHERS *
     *********/
    public String toString(){
        return g.toString();
    }

}