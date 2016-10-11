import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

/**
 * A utility class to parse data from source, and populate graphs. One method for each file, and a method for
 * populating a small test graph.
 */
public class Parser {
    private static String codes = "data/CoWCountryCodes.csv";
//    private static String codes = "data/shortCoW.txt";  // used for testing
    private static String mida = "data/MIDA_4.01.csv";
    private static String midb = "data/MIDB_4.01.csv";
    private static String testCode = "data/small_test_graph.txt"; // used for testing
//    private static String testCode = "data/med_test_graph";
    /**
     * Method to parse file of disputes.
     * Use AFTER parsing file of Country Codes, and States have been created.
     * @param ag Takes an AlliesGraph containing States, with no edges. Uses
     *           disputes from file to create the edges.
     */
    public static void parseDisputes(AlliesGraph ag) {
        HashMap<Integer, Dispute> map = new HashMap<>();

        Path filePath = Paths.get(midb);
        Scanner scanner = null;
        try {
            scanner = new Scanner(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String header = scanner.nextLine(); // capture header line, dispose of as junk
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

    /**
     * Method to parse data file of Country Codes.
     * @param ag Takes an empty AlliesGraph, and fills the nodes with State objects.
     */
    public static void parseStateCodes(AlliesGraph ag) {
        Path filePath = Paths.get(codes);
        Scanner scanner = null;
        try {
            scanner = new Scanner(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = scanner.nextLine(); // capture header line as junk to dispose of
        while (scanner.hasNext()) {
            s = scanner.nextLine();
            ag.addState(parseCodeLine(s));
        }
    }

    /**
     * Helper method to parse each line of the Country Codes data
     * @param data one line of data, as a String
     * @return Returns State, created from data in the line.
     */
    private static State parseCodeLine(String data) {
        String[] pieces = data.split("[,]| ", 3);

        return new State(pieces[0], Integer.parseInt(pieces[1]), pieces[2]);
    }

    /**
     * Utility for parsing the simple sample graph, for testing of Graph class methods. Useless now
     * except for potential future work.
     * @param g Takes an empty Graph object, and populates nodes and edges from data file
     */
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
