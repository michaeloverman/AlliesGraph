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

    public MinCutGraph() {
        nodes = new HashMap<>();
        edges = new ArrayList<>();
        graphString = "";
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
        Random rand = new Random(); // use same seed for initial testing
        int nix = rand.nextInt(edges.size());
        return contractEdge(edges.get(nix));

    }
    public String contractEdge(Edge e) {
        String string = e.toString();
        Node t = nodes.get(e.getTail());
        Node h = nodes.get(e.getHead());
        redirectTails(h, t);
        t.consumeNode(h);
        nodes.remove(h.getId());
        generateEdges();
        return string;
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
        int minimum = Integer.MAX_VALUE;
        int numRuns = (int) (nodes.size() * nodeSize() * Math.log(nodes.size()));
        for (int i = 0; i < numRuns; i++) {
            MinCutGraph mc = new MinCutGraph();
            mc.parseGraph(graphString);
            while (mc.nodeSize() > 2) {
                mc.contractRandomEdge();
            }
            int min = mc.edgeSize() / 2;
            minimum = min < minimum ? min : minimum;
        }


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
}
