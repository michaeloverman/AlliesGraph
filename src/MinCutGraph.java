import java.util.*;

/**
 * Utility class, creates a "parallel" graph for the minimum cut algorithm to manipulate. Methods for finding
 * the min-cut include:
 * getMinCut() - coordinator of the process - runs many times to ensure accuracy
 * contractEdge() - combines two nodes into one
 * redirectTails() - moves edges to and from the consumed node to the remaining node
 */
public class MinCutGraph {
    Map<Integer, Node> nodes;
    List<Edge> edges;
    private String graphString;
    static Random rand = new Random();

    public MinCutGraph() {
        nodes = new HashMap<>();
        edges = new ArrayList<>();
        graphString = "";
//        rand =
    }
    public MinCutGraph(String gs) {
        this();
        this.graphString = gs;

    }

    public int nodeSize() {
        return nodes.size();
    }
    public int edgeSize() {
        return edges.size();
    }
    public String contractRandomEdge() {
        Random rand = new Random(); // use same seed for initial testing NO NOT HERE ANYWAY!
        int nix = rand.nextInt(edges.size());
        return contractEdge(edges.get(nix));
        // ABOVE HERE ORIGINAL
        // BELOW HERE - ATTEMPT TO REWORK TO FIND WHATEVER THE BUG IS
                // didn't work... other problems... or the same problem another way...
//        Node tailToNix = getRandomNode(nodes);
//        int whichHead = rand.nextInt(tailToNix.getNeighbors().size());
//        Node headToNix = nodes.get(tailToNix.getNeighbors().get(whichHead));
//        if (tailToNix == null || headToNix == null)
//            nullNode(headToNix, tailToNix);
//        redirectTails(headToNix, tailToNix);
//        tailToNix.consumeNode(headToNix);
//        nodes.remove(headToNix);
//        generateEdges();
//        return tailToNix.getId() + "-->" + headToNix.getId();
    }
//    private Node getRandomNode(Map<Integer, Node> set) {
//        int size = set.size();
//        int item = rand.nextInt(size);
//        int i = 0;
//        for (Node n : set.values()) {
//            if (i == item)
//                return n;
//            i++;
//        }
//        return null;
//    }
    public String contractEdge(Edge e) {
        String string = e.toString();
        Node t = nodes.get(e.getTail());
        Node h = nodes.get(e.getHead());
        if (t == null || h == null) {
            nullNode(e);
        }
        redirectTails(h, t);
        t.consumeNode(h);
        nodes.remove(h.getId());
        generateEdges();
        return string;
    }
    private void nullNode(Node a, Node b) {
        System.out.print("Null Node Found: ");
        if (a == null)
            System.out.println("head # ??");
        if (b == null)
            System.out.println("tail # ??");
    }
    private void nullNode(Edge e) {
        System.out.println("Null Node Found: " + e.toString());
        if (nodes.get(e.getHead()) == null) {
            System.out.println("Head: " + e.getHead() + " is null");
        } else if (nodes.get(e.getTail()) == null) {
            System.out.println("Tail: " + e.getTail() + " is null");
        }
        System.out.println("=======================" + nodeSize() + " NODES=======================");
        for(Node n : nodes.values()) {
            System.out.println(n.toString());
        }
    }
    /**
     * after contracting an edge and redirecting tails, it is easier to recreate the List of edges,
     * rather than dealing with the List of Edges directly.
     */
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
//        System.out.println("Head: " + h.getId() + " Tail: " + t.getId());
//        if (h == null) {
//            System.out.println("  Error: Head Node Null");
//        }
//        if (t == null) {
//            System.out.println("  Error: Tail Node Null");
//        }
        for(int n : h.getNeighbors()) {
            try {
                nodes.get(n).resetEdge(h, t);
            } catch (NullPointerException npe) {
                System.out.println("Got that ol' NullPointerException again...");
                System.out.println("    Getting Node: " + n + " Resetting Head: " + h.getId() + " Tail: " + t.getId());
//                npe.printStackTrace();
            }
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
//    public List<Edge> getEdges() {
//        ArrayList<Edge> list = new ArrayList<>();
//        list.addAll(edges);
//        return list;
//    }
//    public List<Node> getNodes() {
//        ArrayList<Node> list = new ArrayList<>();
//        list.addAll(nodes.values());
//
//        return list;
//    }
//    private void parseFile(String fileName) {
//        Path filePath = Paths.get(fileName);
//        Scanner scanner = null;
//        try {
//            scanner = new Scanner(filePath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        parseWithScanner(scanner);
//
//    }

    /**
     * Methods for parsing original graph into the mincut graph's data structure. Takes a string
     * representation of the original graph as a list of each node followed by its neighbors.
     * @param graph - string representation of graph
     */
    public void parseGraph(String graph) {
        Scanner scanner = new Scanner(graph).useDelimiter("\\n");
        parseWithScanner(scanner);
    }
    public void parseGraph() {
        if(graphString == null) {
            System.out.println("Null graphString....returning");
            return;
        }
        parseGraph(graphString);
    }
    private void parseWithScanner(Scanner scanner) {
        while (scanner.hasNext()) {
            String data = scanner.nextLine();
            String[] pieces = data.split("\\s+");

            Node newNode = new Node(Integer.parseInt(pieces[0]));
            for (int i = 1; i < pieces.length; i++) {
                if(pieces[0] != pieces[i]) {
                    newNode.addNeighbor(Integer.parseInt(pieces[i]));
                    addEdge(newNode.getId(), Integer.parseInt(pieces[i]));
                }
            }
//            System.out.println("Adding newNode to graph...");
            nodes.put(Integer.parseInt(pieces[0]), newNode);
        }
    }
//    public String edgesToString() {
//        StringBuffer sb = new StringBuffer();
//        for (Edge e : edges) {
//            sb.append(e.toString() + "\n");
//        }
//        return sb.toString();
//    }
//    public String listNodes() {
//        StringBuffer sb = new StringBuffer();
//        for (Node n : nodes.values()) {
//            sb.append(n.toString() + "\n");
//        }
//        return sb.toString();
//    }
//    public String printNode(int n) {
//        Node node = nodes.get(n);
//        return node.toString();
//    }
    public int getMinCut() {
        System.out.print("Running getMinCut()...");
        int minimum = Integer.MAX_VALUE;
//        System.out.println("Nodes: " + nodes.size());
//        System.out.println("log of N: " + Math.log(nodes.size()));
        int numRuns = (int) (nodes.size() * nodeSize() * Math.log(nodes.size()));
        for (int i = 0; i < numRuns; i++) {
            if(i % 253 == 0) System.out.print(".");
            MinCutGraph mc = new MinCutGraph(graphString);
            mc.parseGraph();
            while (mc.nodeSize() > 2) {
                mc.contractRandomEdge();
//                System.out.println(this.toString());
            }
            int min = mc.edgeSize() / 2;
            minimum = min < minimum ? min : minimum;
        }
        System.out.println("\n--MinCutGraph: getMinCut() run " + numRuns + " times; MinCut: " + minimum);
        return minimum;
    }

    /**
     * Private utility class for storing data about each node in the graph to be mincut.
     * ArrayList holds neighbors, and another holds 'consumed' nodes.
     */
    private class Node {
        private int id;
        private ArrayList<Integer> neighbors;
        private ArrayList<Integer> consumedNodes;

        public int getId() {
            return id;
        }
        public Node(int id){
            this.id = id;
            neighbors = new ArrayList<>();
            consumedNodes = new ArrayList<>();
        }
        public void addNeighbor(int n) {
            neighbors.add(n);
        }
        public void removeNeighbor(int n) {
            neighbors.remove((Integer) n);
        }
        public void resetEdge(Node h, Node t) {
            if(!neighbors.contains(h.getId())) {
                System.out.println("Neighbors doesn't contain node: " + h.getId());
                return;
            }
            int head = h.getId();
            if (head != t.getId()) {
                neighbors.set(neighbors.indexOf(head), t.getId());
            }
        }
        public List<Integer> getNeighbors() {
            List<Integer> list = new ArrayList<>();
            list.addAll(neighbors);
            return list;
        }
        public void consumeNode(Node other) {
            for(int i : other.getNeighbors()) {
                if (i != this.getId()) this.addNeighbor(i);
            }

            consumedNodes.add(other.getId());

        }
        public String toString() {
            StringBuffer sb = new StringBuffer(id + ": ");
            for(int i : neighbors) {
                sb.append(i + " ");
            }
            return sb.toString();
        }
    }

    /**
     * private utility class edge, holds head and tail nodes.
     */
    private class Edge {
        private int tail;
        private int head;

        public Edge(int tail, int head) {
            this.tail = tail;
            this.head = head;
        }

        public int getTail() {
            return tail;
        }

        public int getHead() {
            return head;
        }
        public String toString() {
            return tail + "-->" + head;
        }

    }
    public String toString() {
        return graphString;
    }
    public static void main(String[] args) {
        Graph g = new Graph();
        Parser.parseTestCode(g);

        MinCutGraph mcg = new MinCutGraph(g.graphToMinCutString());
        mcg.parseGraph();
//        System.out.println(mcg.toString());
        System.out.println("MinCutofSampleGraph: " + mcg.getMinCut());
    }
}
