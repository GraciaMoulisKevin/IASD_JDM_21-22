package Structure;

import multimots.MultiMotsTreeFactory;
import multimots.ParserMultimots;
import multimots.TreeNode;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

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

        // Gen de la liste gérant les points de départ
        List<AbstractNode> activeNodeList = new ArrayList<>();
        activeNodeList.add(getStart());
        AbstractNode lastStartNode = getStart();
        AsSubgraph<AbstractNode, AbstractEdge> graphFollowedBy = getGraphFollowedBy();
        int cptProfondeur = 1;

        // Exécutions tant que la liste de départ n'est à l'arrivé
        List<AbstractNode> aRemove = new ArrayList<>();
        while (!activeNodeList.contains(getEnd())) {
            // Loop sur chaque element de la liste de départ
            if (cptProfondeur == 1) {
                lastStartNode = activeNodeList.get(0); // Si crash => erreur dans le code car getEnd()
                for (AbstractNode activeNode : activeNodeList) {
                    //if (activeNode != getStart()) {
                        List<AbstractNode> tmp = new ArrayList<>(); tmp.add(activeNode);
                        if (!root.checkBeginning(tmp)) {
                            aRemove.add(activeNode);
                        }
                    //}
                }
            }
            for (AbstractNode node : aRemove) activeNodeList.remove(node);
            // Check si pas de début potentiel de mot composés
            // si oui gen nouveau début potentiel dans activeNodeList
            if (activeNodeList.isEmpty()){
                cptProfondeur = 1;
                for (AbstractEdge activeEdge : graphFollowedBy.outgoingEdgesOf(lastStartNode)){
                    activeNodeList.add(g.getEdgeTarget(activeEdge));
                }
            }else{
                // Cas ou il y a des éléments de départ potentiel
                List<AbstractNode> potentialEndNode = new ArrayList<>();
                AbstractNode ptrNode = lastStartNode;
                // Charge potentialEndEdge selon la profondeur
                for (int i=0; i<cptProfondeur; i++){
                    for (AbstractEdge activeEdge : graphFollowedBy.outgoingEdgesOf(ptrNode)){
                        ptrNode = g.getEdgeTarget(activeEdge);
                        if (i == cptProfondeur - 1){
                            potentialEndNode.add(ptrNode);
                        }
                    }
                    for (AbstractEdge activeEdge : graphFollowedBy.outgoingEdgesOf(ptrNode)){
                        ptrNode = g.getEdgeTarget(activeEdge);
                    }
                }
                // Check allPath between activeNodeList & potentialEndNode
                aRemove = new ArrayList<>();
                for (AbstractNode startNode : activeNodeList){
                    int cptPotentialStart = 0;
                    for (AbstractNode endNode : potentialEndNode){
                        // Check si Path est sous mot composé ou un mot composé
                        for ( List<AbstractNode> nodeList : getAllPathsBetween(graphFollowedBy,startNode,endNode)){
                            // si mot composé
                            if (root.checkPath(nodeList)){
                                String motComp = "";
                                for (AbstractNode node : nodeList){
                                    motComp += " "+node;
                                }
                                setMultiMots(startNode,endNode,motComp);
                            }
                            // si sous mot composé
                            if (root.checkBeginning(nodeList)){
                                cptPotentialStart++;
                            }
                        }
                    }
                    // SI =0 alors aucun chemin utilise cette node comme point de départ
                    if(cptPotentialStart == 0){
                        aRemove.add(startNode);
                    }
                }
                for (AbstractNode node : aRemove) activeNodeList.remove(node);
                cptProfondeur++;
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

    public List<List<AbstractNode>> getAllPathsBetween(AsSubgraph<AbstractNode, AbstractEdge> subGraph, AbstractNode _start, AbstractNode _end){
        AllDirectedPaths<AbstractNode, AbstractEdge> graph = new AllDirectedPaths<AbstractNode, AbstractEdge>(subGraph);
        List<GraphPath<AbstractNode, AbstractEdge>> allPaths =
                graph.getAllPaths(_start, _end, true, null);

        List<List<AbstractNode>> list = new ArrayList<List<AbstractNode>>();
        for (GraphPath<AbstractNode, AbstractEdge> graphPath : allPaths){
            list.add(getArrayNodes(graphPath));
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

        return graphList;
    }

    /**********
     * OTHERS *
     *********/
    public String toString(){
        return g.toString();
    }

}