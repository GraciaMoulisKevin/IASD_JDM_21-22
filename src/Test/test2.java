package Test;

import Structure.AbstractEdge;
import Structure.Graph;
import org.jgrapht.GraphPath;

public class test2 {
    public static void main(String args[]){
        String text = "Bon avant toute chose il faut savoir avoir le pouce rond.";
        String words[] = text.split("\\W+");
        Graph g = new Graph(words);

        System.out.println(g);
        System.out.println(g.getAllNode()+"\n");

        // Test done _ la donnée est gardé dans via les méthodes protected de DefaultEdge
        System.out.println(g.getEdgesFollowedBy());
        System.out.println(g.getEdgesLemme()+"\n");

        System.out.println(g.getGraphFollowedBy()+"\n");

        // System.out.println(g.getAllEdges(g.getTmp(),g.getEnd()));

        System.out.println("Nb de chemin possible : " + g.getAllPaths().size());

        /** DEBUG DE l'exec pour rendre ça plus lisible
         * ([START, END, Bon, avant, toute, chose, il, faut, savoir, avoir, le, pouce, rond,
         *
         * lemmeDe_Bon, lemmeDe_avant, lemmeDe_toute, lemmeDe_chose, lemmeDe_il, lemmeDe_faut, lemmeDe_savoir, lemmeDe_avoir, lemmeDe_le, lemmeDe_pouce, lemmeDe_rond,
         *
         * avant toute chose,  toute chose,  il faut,  avoir le pouce rond],
         *
         * [FOLLOWED_BY:toute,lemmeDe_chose=(toute,lemmeDe_chose), FOLLOWED_BY:FOLLOWED_BY:avant,toute, toute chose=(avant, toute chose), FOLLOWED_BY:FOLLOWED_BY:lemmeDe_avant,toute, toute chose=(lemmeDe_avant, toute chose),
         * FOLLOWED_BY:lemmeDe_il,lemmeDe_faut=(lemmeDe_il,lemmeDe_faut), FOLLOWED_BY:lemmeDe_avoir,lemmeDe_le=(lemmeDe_avoir,lemmeDe_le), FOLLOWED_BY:lemmeDe_faut,savoir=(lemmeDe_faut,savoir),
         * FOLLOWED_BY: il faut,FOLLOWED_BY:faut,lemmeDe_savoir=( il faut,lemmeDe_savoir), FOLLOWED_BY: avant toute chose,FOLLOWED_BY:chose,il=( avant toute chose,il),
         * FOLLOWED_BY:FOLLOWED_BY:lemmeDe_savoir,avoir, avoir le pouce rond=(lemmeDe_savoir, avoir le pouce rond), FOLLOWED_BY:le,pouce=(le,pouce), FOLLOWED_BY:il,lemmeDe_faut=(il,lemmeDe_faut),
         * FOLLOWED_BY: toute chose,FOLLOWED_BY:chose,lemmeDe_il=( toute chose,lemmeDe_il), FOLLOWED_BY:lemmeDe_savoir,avoir=(lemmeDe_savoir,avoir), FOLLOWED_BY:lemmeDe_toute,chose=(lemmeDe_toute,chose),
         * FOLLOWED_BY:pouce,lemmeDe_rond=(pouce,lemmeDe_rond), FOLLOWED_BY: avoir le pouce rond,FOLLOWED_BY:rond,END=( avoir le pouce rond,END), FOLLOWED_BY:avant,toute=(avant,toute), FOLLOWED_BY:lemmeDe_rond,END=(lemmeDe_rond,END),
         * FOLLOWED_BY:FOLLOWED_BY:lemmeDe_Bon,avant, avant toute chose=(lemmeDe_Bon, avant toute chose), FOLLOWED_BY:lemmeDe_il,faut=(lemmeDe_il,faut), FOLLOWED_BY:FOLLOWED_BY:savoir,avoir, avoir le pouce rond=(savoir, avoir le pouce rond),
         * FOLLOWED_BY:avant,lemmeDe_toute=(avant,lemmeDe_toute), FOLLOWED_BY:avoir,le=(avoir,le), FOLLOWED_BY: toute chose,FOLLOWED_BY:chose,il=( toute chose,il), FOLLOWED_BY:chose,lemmeDe_il=(chose,lemmeDe_il),
         * FOLLOWED_BY:FOLLOWED_BY:Bon,avant, avant toute chose=(Bon, avant toute chose), FOLLOWED_BY:lemmeDe_avant,lemmeDe_toute=(lemmeDe_avant,lemmeDe_toute), FOLLOWED_BY:Bon,avant=(Bon,avant), FOLLOWED_BY:toute,chose=(toute,chose),
         * FOLLOWED_BY:lemmeDe_pouce,lemmeDe_rond=(lemmeDe_pouce,lemmeDe_rond), FOLLOWED_BY: avant toute chose,FOLLOWED_BY:chose,lemmeDe_il=( avant toute chose,lemmeDe_il), FOLLOWED_BY:avoir,lemmeDe_le=(avoir,lemmeDe_le),
         * FOLLOWED_BY:lemmeDe_chose,lemmeDe_il=(lemmeDe_chose,lemmeDe_il), FOLLOWED_BY:lemmeDe_avant,toute=(lemmeDe_avant,toute), FOLLOWED_BY:lemmeDe_chose,il=(lemmeDe_chose,il), FOLLOWED_BY:lemmeDe_le,pouce=(lemmeDe_le,pouce),
         * FOLLOWED_BY:lemmeDe_faut,lemmeDe_savoir=(lemmeDe_faut,lemmeDe_savoir), FOLLOWED_BY: il faut,FOLLOWED_BY:faut,savoir=( il faut,savoir), FOLLOWED_BY:savoir,avoir=(savoir,avoir), FOLLOWED_BY:lemmeDe_toute,lemmeDe_chose=(lemmeDe_toute,lemmeDe_chose),
         * FOLLOWED_BY:lemmeDe_Bon,avant=(lemmeDe_Bon,avant), FOLLOWED_BY:Bon,lemmeDe_avant=(Bon,lemmeDe_avant), FOLLOWED_BY:le,lemmeDe_pouce=(le,lemmeDe_pouce), FOLLOWED_BY:lemmeDe_le,lemmeDe_pouce=(lemmeDe_le,lemmeDe_pouce), FOLLOWED_BY:chose,il=(chose,il),
         * FOLLOWED_BY:FOLLOWED_BY: toute chose,FOLLOWED_BY:chose,il, il faut=( toute chose, il faut), FOLLOWED_BY:savoir,lemmeDe_avoir=(savoir,lemmeDe_avoir), FOLLOWED_BY:START,lemmeDe_Bon=(START,lemmeDe_Bon),
         * FOLLOWED_BY:lemmeDe_savoir,lemmeDe_avoir=(lemmeDe_savoir,lemmeDe_avoir), FOLLOWED_BY:lemmeDe_Bon,lemmeDe_avant=(lemmeDe_Bon,lemmeDe_avant), FOLLOWED_BY:FOLLOWED_BY:lemmeDe_chose,il, il faut=(lemmeDe_chose, il faut),
         * FOLLOWED_BY:faut,savoir=(faut,savoir), FOLLOWED_BY:rond,END=(rond,END), FOLLOWED_BY:lemmeDe_avoir,le=(lemmeDe_avoir,le), FOLLOWED_BY:il,faut=(il,faut), FOLLOWED_BY:pouce,rond=(pouce,rond), FOLLOWED_BY:START,Bon=(START,Bon),
         * FOLLOWED_BY:lemmeDe_pouce,rond=(lemmeDe_pouce,rond), FOLLOWED_BY:FOLLOWED_BY: avant toute chose,FOLLOWED_BY:chose,il, il faut=( avant toute chose, il faut), FOLLOWED_BY:FOLLOWED_BY:chose,il, il faut=(chose, il faut),
         * FOLLOWED_BY:faut,lemmeDe_savoir=(faut,lemmeDe_savoir)])
         */
    }
}
