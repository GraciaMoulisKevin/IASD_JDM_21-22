package multimots;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

import Structure.AbstractNode;

public class TreeNode {

    public static final String START = "START";

    private Map<String, TreeNode> children;
    private boolean isAnEndWord;
    private String word;

    public TreeNode(String s) {
        this(s,false);
    }

    public TreeNode(String s, boolean bool) {
        word = s;
        children = new HashMap<>();
        isAnEndWord = bool;
    }

    public static TreeNode getNew(){
        return new TreeNode(START);

    }


    public void addChild(TreeNode newChild) {
        if (!children.containsKey(newChild.getWord())) {
            children.put(newChild.getWord(), newChild);
        }
        else {
            TreeNode child = children.get(newChild.getWord());
            child.setEndWord(child.isAnEndWord() || newChild.isAnEndWord());
            for (TreeNode grandChild : newChild.getChildren()) {
                child.addChild(grandChild);
            }
        }
    }

    private Collection<TreeNode> getChildren() {
        return children.values();
    }

    public String getWord() {
        return word;
    }

    public boolean hasChild(TreeNode candidate) {
        return children.containsKey(candidate.getWord());
    }

    public boolean isAnEndWord() {
        return isAnEndWord;
    }

    public void setEndWord(boolean b) {
        isAnEndWord = b;

    }

    public int size() {
        int size = 1;
        for (TreeNode child: children.values()) {
            size += child.size();
        }
        return size;
    }

    public int getNombreMultimots() {
        int nombre = 0;
        if (isAnEndWord) nombre = 1;
        for (TreeNode child: children.values()) {
            nombre += child.getNombreMultimots();
        }
        return nombre;
    }

    public int getNombreNoeudsAvecPlusieursEnfants() {
        int nombre = 0;
        if (children.size() >1) nombre = 1;
        for (TreeNode child: children.values()) {
            nombre += child.getNombreNoeudsAvecPlusieursEnfants();
        }
        return nombre;
    }

    @Override
    public int hashCode() {
        return word.hashCode();
    }


        public static TreeNode makePath(List<String> liste) {
        liste = new ArrayList<>(liste);
        TreeNode firstNode = new TreeNode(liste.remove(0));
        TreeNode latestNode = firstNode;
        for (String word : liste) {
            TreeNode nextNode = new TreeNode(word);
            latestNode.addChild(nextNode);
            latestNode = nextNode;

        }
        latestNode.setEndWord(true);
        return firstNode;

    }

    public String toString() {
        String ret = "[" + word;
        if (isAnEndWord) ret += " * ";
        for (TreeNode child: children.values()) {
            ret += "\n" + child.toString();
        }
        ret += "]";
        return ret;
    }

    public static void main(String[] args) {
        TreeNode a = new TreeNode("a");
        TreeNode b = new TreeNode("b");
        TreeNode c = new TreeNode("c");
        TreeNode d = new TreeNode("d");
        TreeNode e = new TreeNode("e");

        d.addChild(e);
        a.addChild(b);
        a.addChild(c);
        a.addChild(d);

        System.out.println(a);


        TreeNode d2 = new TreeNode("d");
        TreeNode e2 = new TreeNode("e");
        TreeNode f2 = new TreeNode("f");


        d2.addChild(e2);
        e2.addChild(f2);
        System.out.println(d2);

        a.addChild(d2);
        System.out.println(a);
    }

    public boolean checkPath(TreeNode path) {
        if (path.getChildrenNumber() > 1) {
            throw new IllegalArgumentException("Supplied tree is not a path (multiple children somewhere) : " + path);
        }
        if (!path.getWord().equals(getWord())) return false;
        if (!path.hasChildren()) {

         	return isAnEndWord;   
        }
        TreeNode nextInLine = path.getSingleChild();
        if (children.containsKey(nextInLine.getWord())){
            TreeNode child = children.get(nextInLine.getWord());
            return child.checkPath(nextInLine);
        }
        return false;
    }

    public boolean checkPath(List<AbstractNode> nodePath){
        List<String> path = nodePath.stream()
        								.map(AbstractNode::getData).collect(Collectors.toList());
        TreeNode pathToCheck = getNew();
        pathToCheck.addChild(makePath(path));

        System.out.println("Testing " + pathToCheck );
      return checkPath(pathToCheck);
    }
    
    

    private int getChildrenNumber() {
        return children.size();
    }

    private TreeNode getSingleChild() {
        if (children.size() > 1) {
            throw new IllegalArgumentException("Node has more than one child");
        }
        TreeNode result = null;
        for (TreeNode child: children.values()) result = child;
        return result;
    }

    private boolean hasChildren() {
        return !children.isEmpty();
    }


}
