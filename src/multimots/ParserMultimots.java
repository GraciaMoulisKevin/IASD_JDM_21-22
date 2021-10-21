package multimots;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ParserMultimots {

    public static Collection<List<String>> parseFile(String fileName){
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(fileName);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream, StandardCharsets.UTF_8));

        Collection<List<String>> result = new ArrayList<>();
        String strLine;
        //Read File Line By Line
        try {
            while ((strLine = br.readLine()) != null)   {
                if (strLine.isBlank() || strLine.startsWith("//")) {
                    continue;
                }

                //on enlève seulement l'id et le point virgule final
                String multimot = strLine.split(";", 2)[1];
                multimot = multimot.substring(0, multimot.length()-1);
                multimot = multimot.startsWith("\"") ? multimot.substring(1) : multimot;
                multimot = multimot.endsWith("\"") ? multimot.substring(0, multimot.length() -1) : multimot;

                //on veut split sur l'apostrophe MAIS la garder. Donc en amont on rajoute un espace
                multimot = multimot.replaceAll("'", "' ");
                List<String> multimotListe = new ArrayList<String>(Arrays.asList(multimot.split("\\s")));
                result.add(multimotListe);

                // using String interning triples the time while providing only a minimal improvement
//				String[] multimotArray = multimot.split("\\s");
//				for (String s: multimotArray) {
//					multimotListe.add(s.intern());
//				}
//				result.add(multimotListe);
//				}

            }



            //Close the input stream
            fstream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;




    }

    public static void write(Collection<List<String>> result, String fileName)  {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File(fileName)));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (List<String> liste : result) {
            try {
                writer.write(liste.toString()+"\n");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    

    public static void main(String[] args) {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Collection<List<String>> result = parseFile("files/09172021-LEXICALNET-JEUXDEMOTS-ENTRIES-MWE.txt");
        write(result, "files/output.txt");
        TreeNode root = TreeNode.getNew();
        for (List<String> path: result) {
            TreeNode test = TreeNode.makePath(path);
            root.addChild(test);
            }
        TreeNode avant = TreeNode.getNew();
        avant.addChild(new TreeNode("avant"));
        List<String> aa= new ArrayList<String>(Arrays.asList("avant", "toute"));
        TreeNode avantFull = TreeNode.makePath(aa);
        System.out.println(avant);
        System.out.println(root.checkBeginning(avant));
        
        System.out.println(avantFull);
        System.out.println("checkPath = " + root.checkPath(avantFull));
        System.out.println("checkBeginning = " + root.checkBeginning(avantFull));
        
    }

}
