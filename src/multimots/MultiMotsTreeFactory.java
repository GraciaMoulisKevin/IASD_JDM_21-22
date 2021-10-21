package multimots;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MultiMotsTreeFactory {



    public static void fillPaths(TreeNode root, Collection<List<String>> paths){
        for (List<String> path : paths){
            root.addChild(TreeNode.makePath(path).getSingleChild());
        }

    }

    public static void main (String[] args) {
        System.out.println("Heap size is " +  Runtime.getRuntime().totalMemory()/1048576 + "MB\n");
        System.out.println("Début du parsing à " + LocalTime.now());
        Collection<List<String>> result = ParserMultimots.parseFile("files/ENTRIES-MULTI.txt");
        System.out.println("Nombre de multimots: " + result.size());
        System.out.println("Heap size is " +  Runtime.getRuntime().totalMemory()/1048576 + "MB\n");
        System.out.println("Début de la construction de l'arbre à " + LocalTime.now());
        TreeNode root = new TreeNode(TreeNode.START);
        for (List<String> path: result) {
            TreeNode test = TreeNode.makePath(path);
            root.addChild(test);
        }
        System.out.println("Construction terminée à " + LocalTime.now());
        System.out.println("Heap size is " +  Runtime.getRuntime().totalMemory()/1048576 + "MB\n");
        System.out.println("Nombre de noeuds: " + root.size());
        System.out.println("Nombre de multimots: " + root.getNombreMultimots());
        System.out.println("Nombre de noeuds avec plusieurs enfants: " + root.getNombreNoeudsAvecPlusieursEnfants());
        System.out.println("Looking up every single multiword to measure performance. Starting at " + LocalTime.now());
        for (List<String> multimot : result) {
            TreeNode multiwordTree = TreeNode.getNew();
            multiwordTree.addChild(TreeNode.makePath(multimot));
            if (!root.checkPath(multiwordTree)) {
                System.out.println("Issue with " + multiwordTree + " " + multimot);
                break;
            }

        }

        System.out.println("Done checking at " + LocalTime.now());
        System.out.println("Heap size is " +  Runtime.getRuntime().totalMemory()/1048576 + "MB\n");
    }

}
