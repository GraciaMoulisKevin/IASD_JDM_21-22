package Structure;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

import multimots.ParserMultimots;
import multimots.TreeNode;
import multimots.MultiMotsTreeFactory;
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
                FollowedBy edgeStart = new FollowedBy("START,"+node);
                g.addEdge(start, node, edgeStart);
            } else {
                FollowedBy edgeFollowedBy = new FollowedBy(preNode+","+node);
                g.addEdge(preNode,node,edgeFollowedBy);
            }
            count++;
            preNode = node;
        }
        FollowedBy edgeEnd = new FollowedBy(preNode+",END");
        g.addEdge(preNode,end, edgeEnd);
    }

    /**
     * Set Lemme of each Word
     */
    protected void loadLemmes(){
        AbstractNode active_node = getStart();
        AbstractNode last_node = getStart();

        Lemme last_lemme = null;

        for (AbstractEdge edge : g.outgoingEdgesOf(active_node)){
            active_node = g.getEdgeTarget(edge);
        }

        while (active_node != getEnd()) {
            Lemme active_lemme = new Lemme(active_node.getData());
            g.addVertex(active_lemme);

            AbstractNode tmp_node = null;
            for (AbstractEdge edge : g.outgoingEdgesOf(active_node)){
                tmp_node = g.getEdgeTarget(edge);
            }

            Edge edgeLemme = new Edge(active_node+","+active_lemme,"LEMME");
            g.addEdge(active_node,active_lemme,edgeLemme);

            FollowedBy edgeFollowed_LastNode_ActLemme = new FollowedBy(last_node+","+active_lemme);
            g.addEdge(last_node,active_lemme,edgeFollowed_LastNode_ActLemme);

            if (last_lemme != null) {
                FollowedBy edgeFollowed_LastLemme_ActLemme = new FollowedBy(last_lemme+","+active_lemme);
                FollowedBy edgeFollowed_LastLemme_ActNode = new FollowedBy(last_lemme+","+active_node);
                g.addEdge(last_lemme,active_lemme,edgeFollowed_LastLemme_ActLemme);
                g.addEdge(last_lemme,active_node,edgeFollowed_LastLemme_ActNode);
            }

            last_lemme = active_lemme;
            last_node = active_node;

            active_node = tmp_node;
        }
        FollowedBy edgeFollowed_LastLemme_ActNode = new FollowedBy(last_lemme+","+active_node);
        g.addEdge(last_lemme,active_node,edgeFollowed_LastLemme_ActNode);
        /*
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
        */
    }

    /**
     * Set MultiWords
     */
    protected void loadMultiWords(){
        Collection<List<String>> paths = ParserMultimots.parseFile("files//09172021-LEXICALNET-JEUXDEMOTS-ENTRIES-MWE.txt");
        TreeNode root = TreeNode.getNew();
        MultiMotsTreeFactory.fillPaths(root, paths);

        ParserMultimots.write(paths,"files/output.txt");
        //System.out.println(root);

        List<List<AbstractNode>> nodeSequences = genAllGetArrayNodes();
        System.out.println("Testing multiwords");
        for (List<AbstractNode> nodeSequence : nodeSequences){
           // nodeSequence.remove(0); nodeSequence.remove(nodeSequence.size()-1);
			System.out.println(nodeSequence + " = " + root.checkPath(nodeSequence));

        }

    }
    protected boolean verif_quentin(List<AbstractNode> nodeList){ return true; }

    /**
     * Set MultiWords
     */
    protected void tmp_loadMultiWords(){
        Collection<List<String>> paths = ParserMultimots.parseFile("files//09172021-LEXICALNET-JEUXDEMOTS-ENTRIES-MWE.txt");
        TreeNode root = TreeNode.getNew();
        MultiMotsTreeFactory.fillPaths(root, paths);

        AbstractNode active_node = getStart();
        AsSubgraph<AbstractNode, AbstractEdge> graphFollowedBy = getGraphFollowedBy();

        // spam getAllPaths sur les sous graph
        while (active_node != getEnd()) {
            for (AbstractEdge activeEdge : graphFollowedBy.outgoingEdgesOf(active_node)){
                AbstractNode start_path = graphFollowedBy.getEdgeTarget(activeEdge);
                List<AbstractNode> nodeSequence = new ArrayList<>();
                nodeSequence.add(start_path);

                if (verif_quentin(nodeSequence)){
                    // break et passe à l'active node suivante
                }

                // avance 1 ------> n pas par pas
                // en vérifiant si le sous multi existe à chaque fois
                // de plus s'il existe totalement l'ajoute

                System.out.println(nodeSequence + " = " + root.checkPath(nodeSequence));
            }
        }
    }

    /***********************************
     * FONCTIONS MANIPULATION DU GRAPH *
     **********************************/

    /**
     * Generate edge followedBy between MultiWord and pre n1 nodes and post n2 nodes
     * @param n1 start of multiword
     * @param n2 end of multiword
     * @param multiMots
     */
    protected void setMultiMots(AbstractNode n1,AbstractNode n2,String multiMots){// Possibilité de mettre en place une class MultiWord à la place de Word
        AbstractNode nodeMultiWords = new Word(multiMots);
        g.addVertex(nodeMultiWords);

        for (AbstractEdge edge : getGraphFollowedBy().incomingEdgesOf(n1)){
            FollowedBy edgeFollowed_Pre_Multi = new FollowedBy(edge+","+multiMots);
            g.addEdge(g.getEdgeSource(edge),nodeMultiWords,edgeFollowed_Pre_Multi);
        }

        for (AbstractEdge edge : getGraphFollowedBy().outgoingEdgesOf(n2)){
            FollowedBy edgeFollowed_Post_Multi = new FollowedBy(multiMots+","+edge);
            g.addEdge(nodeMultiWords,g.getEdgeTarget(edge),edgeFollowed_Post_Multi);
        }
    }

    /*****************************
     * FONCTIONS ACCESS DU GRAPH *
     ****************************/

    public End getEnd(){ return end; }
    public Start getStart(){ return start; }

    /**
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
        AllDirectedPaths<AbstractNode, AbstractEdge> graph = new AllDirectedPaths<AbstractNode, AbstractEdge>(getGraphFollowedBy());

        List<GraphPath<AbstractNode, AbstractEdge>> allPaths =
                graph.getAllPaths(getStart(), getEnd(), true, null);

        return allPaths;
    }
    public List<GraphPath<AbstractNode, AbstractEdge>> getAllPaths(AsSubgraph<AbstractNode, AbstractEdge> subGraph){
        AllDirectedPaths<AbstractNode, AbstractEdge> graph = new AllDirectedPaths<AbstractNode, AbstractEdge>(subGraph);

        List<GraphPath<AbstractNode, AbstractEdge>> allPaths =
                graph.getAllPaths(getStart(), getEnd(), true, null);

        return allPaths;
    }

    ///////// FAUT SUREMENT SUICIDE CES DEUX METHODES
    /**
     * Call getArrayNodes on each path
     */
    protected List<List<AbstractNode>> genAllGetArrayNodes(){
        List<List<AbstractNode>> list = new ArrayList<List<AbstractNode>>();
        for (GraphPath<AbstractNode, AbstractEdge> graph : getAllPaths()){
            list.add(getArrayNodes(graph));
        }
        return list;
    }

    /**
     * Return ArrayList of Nodes for multiWords
     */
    protected List<AbstractNode> getArrayNodes(GraphPath<AbstractNode, AbstractEdge> graph){
        List<AbstractNode> graphList = graph.getVertexList();
        graphList.remove(getStart());
        graphList.remove(getEnd());

        for (AbstractNode node : graphList) {

        }

        return graphList;
        /**
         * mettre les compositions de phrase de 0 à n
         * puis 1 à n
         * etc etc etc
         */
    }

    /**********
     * OTHERS *
     *********/
    public String toString(){
        return g.toString();
    }

}