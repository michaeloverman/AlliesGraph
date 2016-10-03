import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Michael on 9/9/2016.
 */
public class MinCutGraph {
    Map<Integer, Node> nodes;
    List<Edge> edges;
    private String graphString;

    public MinCutGraph() {
        nodes = new HashMap<>();
        edges = new ArrayList<>();
        graphString = "";
    }
    public MinCutGraph(String filename) {
        this();
//        parseGraph(filename);
        parseFile(filename);
//        System.out.println("Nodes: " + nodeSize());
//        System.out.println("Edges: " + edgeSize());
    }

    public int nodeSize() {
        return nodes.size();
    }
    public int edgeSize() {
        return edges.size();
    }
    public String contractRandomEdge() {
        Random rand = new Random(); // use same seed for initial testing
        int nix = rand.nextInt(edges.size());
        return contractEdge(edges.get(nix));

    }
    public String contractEdge(Edge e) {
//        System.out.println("Contracting Edge: " + e.toString());
        String string = e.toString();
        Node t = nodes.get(e.getTail());
        Node h = nodes.get(e.getHead());
        redirectTails(h, t);
        t.consumeNode(h);
        nodes.remove(h.getId());
        generateEdges();
//        System.out.println(nodeSize() + " nodes remaining");
//        System.out.println(listNodes());
//        System.out.println(edgeSize() + " edges remaining");
        return string;
    }
    public void generateEdges() {
        edges = new ArrayList<>();
        for (Node n : nodes.values()) {
            for (int i : n.getNeighbors()) {
                if (i == n.getId()) {
                    n.removeNeighbor(i);
                } else {
                    edges.add(new Edge(n.getId(), i));
                }
            }
        }
    }
    private void redirectTails(Node h, Node t) {
        for(int n : h.getNeighbors()) {
            nodes.get(n).resetEdge(h, t);
        }
    }
    public void addEdge(Edge newEdge) {
        if (!edges.contains(newEdge)) edges.add(newEdge);
    }
    public void addEdge(int tail, int head) {
        if(!nodes.containsKey(tail)) nodes.put(tail, new Node(tail));
        if(!nodes.containsKey(head)) nodes.put(head, new Node(head));
        Edge edge = new Edge(tail, head);
        addEdge(edge);
    }
    public List<Edge> getEdges() {
        ArrayList<Edge> list = new ArrayList<>();
        list.addAll(edges);
        return list;
    }
    public List<Node> getNodes() {
        ArrayList<Node> list = new ArrayList<>();
        list.addAll(nodes.values());

        return list;
    }
    private void parseFile(String fileName) {
        Path filePath = Paths.get(fileName);
        Scanner scanner = null;
        try {
            scanner = new Scanner(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        parseWithScanner(scanner);

    }
    private void parseWithScanner(Scanner scanner) {
        while (scanner.hasNext()) {
            String data = scanner.nextLine();
            String[] pieces = data.split("\\s+");

            Node newNode = new Node(Integer.parseInt(pieces[0]));
            for (int i = 1; i < pieces.length; i++) {
                newNode.addNeighbor(Integer.parseInt(pieces[i]));
                addEdge(newNode.getId(), Integer.parseInt(pieces[i]));
            }
            nodes.put(Integer.parseInt(pieces[0]), newNode);
//            System.out.println(newNode.toString());
        }
    }
    public void parseGraph(String graph) {
        graphString = graph;
        Scanner scanner = new Scanner(graph).useDelimiter("\\n");
        parseWithScanner(scanner);
    }
    public String edgesToString() {
        StringBuffer sb = new StringBuffer();
        for (Edge e : edges) {
            sb.append(e.toString() + "\n");
        }
        return sb.toString();
    }
    public String listNodes() {
        StringBuffer sb = new StringBuffer();
        for (Node n : nodes.values()) {
            sb.append(n.toString() + "\n");
        }
        return sb.toString();
    }
    public String printNode(int n) {
        Node node = nodes.get(n);
        return node.toString();
    }
    public int getMinCut() {
        int minimum = Integer.MAX_VALUE;
        for (int i = 0; i < 10000; i++) {
            MinCutGraph mc = new MinCutGraph();
            mc.parseGraph(graphString);
//            StringBuffer sb = new StringBuffer();

//            while (mc.nodeSize() > 2) {
////                sb.append(mc.contractRandomEdge());
//
//            }
            int min = mc.edgeSize() / 2;
            minimum = min < minimum ? min : minimum;
        }


        return minimum;
    }
}
