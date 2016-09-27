import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Michael on 9/16/2016.
 */
public class Parser {
    private static String codes = "data/CoWCountryCodes.csv";
//    private static String codes = "data/shortCoW.txt";  // used for testing
    private static String mida = "data/MIDA_4.01.csv";
    private static String midb = "data/MIDB_4.01.csv";
    private static String testCode = "data/small_test_graph";

    public static void parseDisputes(AlliesGraph ag) {
        HashMap<Integer, Dispute> map = new HashMap<>();

        Path filePath = Paths.get(midb);
        Scanner scanner = null;
        try {
            scanner = new Scanner(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String header = scanner.nextLine();
        while (scanner.hasNext()) {
            String[] data = scanner.nextLine().split(",");
            int id = Integer.parseInt(data[0]);
            if(!ag.containsDispute(id)) {
                ag.addDispute(new Dispute(id));
            }
            Dispute d = ag.getDispute(id);
            d.addState(data[2], Integer.parseInt(data[10]));
        }


    }

    public static void parseStateCodes(AlliesGraph ag) {
        Path filePath = Paths.get(codes);
        Scanner scanner = null;
        try {
            scanner = new Scanner(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = scanner.nextLine();
        while (scanner.hasNext()) {
            s = scanner.nextLine();
            ag.addState(parseCodeLine(s));

        }


    }
    private static State parseCodeLine(String data) {
        String[] pieces = data.split("[,]| ", 3);
//        System.out.println(pieces.length);
//        for (String p : pieces) {
//            System.out.println(p);
//        }
//        System.out.println("");
//        if (pieces.length > 3) {
//            String s = "";
//            for (int i = 2; i < pieces.length; i++) {
//                s += pieces[i];
//            }
//            pieces[2] = s;
//        }
        return new State(pieces[0], Integer.parseInt(pieces[1]), pieces[2]);
    }
    public static void parseTestCode(Graph g) {
        Path filePath = Paths.get(testCode);
        Scanner scanner = null;
        try {
            scanner = new Scanner(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (scanner.hasNext()) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            g.addVertex(a);
            g.addVertex(b);
            g.addEdge(a, b);

        }

    }
}
