import java.util.*;

/**
 * @author Michael Overman
 *
 * Class allows for creation of generalized tools to manipulate data
 *
 */
public class Graph {
    protected HashSet<Integer> nodes;
    protected HashMap<Integer, HashSet<Integer>> edges;
    protected HashMap<Integer, HashSet<Integer>> reverseEdges;
    protected int numNodes;
    protected int numEdges;

    public Graph() {
        nodes = new HashSet<>();
        edges = new HashMap<>();
        reverseEdges = new HashMap<>();
        numEdges = 0;
        numNodes = 0;
    }
    public int getNumVertices(){

        return numNodes;
    }

    public int getNumEdges() {
        return numEdges;
    }

    public void addVertex(int num){
        if (nodes.contains(num)) return;
        nodes.add(num);
        edges.put(num, new HashSet<>());
        reverseEdges.put(num, new HashSet<>());
        numNodes++;
    }
    public void removeVertex(int v) {
        for (int i : edges.keySet()) {
            if (i != v) {
                boolean success = edges.get(i).remove(v);
                if(success) numEdges--;
                reverseEdges.get(i).remove(v);
            }
        }
        numEdges -= edges.get(v).size();
        nodes.remove(v);
        numNodes--;
    }
    public List<Integer> getVertices(){

        return new ArrayList<Integer>(nodes);
    }
    public void contractEdge(int head, int tail) {
        redirectEdges(head, tail);
        nodes.remove(head);
    }
    public void redirectEdges(int head, int tail) {
        for (int i : edges.get(head)) {
            edges.get(tail).add(i);
            edges.get(i).remove(head);
            edges.get(i).add(tail);
        }
    }
    public void addEdge(int from, int to){
        if(!nodes.contains(from) || !nodes.contains(to)) return;

        edges.get(from).add(to);
        reverseEdges.get(to).add(from);
        numEdges++;
    }

    public Graph getEgonet(int center){
        Graph egonet = new Graph();
        if (!nodes.contains(center)) return egonet;
        egonet.addVertex(center);
        for(int i : edges.get(center)) {
            egonet.addVertex(i);
            egonet.addEdge(center, i);
        }
        for (int i : egonet.getVertices()) {
            if (i == center) continue;
            for (int j : edges.get(i)) {
                egonet.addEdge(i, j);  // addEdge method does not add the edge, if the neighbor is not already in the egonet
            }
        }

        return egonet;
    }
    public int getMinCut() {
        int minimum = Integer.MAX_VALUE;
        for (int i = 0; i < 10000; i++) {
            Graph minCutGraph = new Graph();
            // make copy of the graph here to use for mincutting
            while (minCutGraph.numNodes > 2) {
                minCutGraph.contractRandomEdge();
            }
            int min = minCutGraph.getNumEdges() / 2;
            minimum = min < minimum ? min : minimum;
        }
        return minimum;
    }
    private void contractRandomEdge() {
        Random rand = new Random();
        int node = rand.nextInt(nodes.size());
        int edge = rand.nextInt(edges.get(node).size());

    }
    public List<Graph> getSCCs() {
        Stack<Integer> finishedStack = firstDFS(getVertices());
        List<Graph> list = generateSCCs(finishedStack);
        return list;
    }

    private List<Graph> generateSCCs(Stack<Integer> vertices) {
        List<Graph> list = new ArrayList<>();
        HashSet<Integer> visited = new HashSet<>();
        Graph currentGraph = new Graph();
        while(!vertices.isEmpty()) {
            int v = vertices.pop();
            if(!visited.contains(v)) {
                sccDFSVisit(currentGraph, v, visited);
                list.add(currentGraph);
                currentGraph = new Graph();
            }
        }
        return list;
    }

    private Stack<Integer> firstDFS(List<Integer> vertices){
        Stack<Integer> finished = new Stack<>();
        HashSet<Integer> visited = new HashSet<>();
        while(!vertices.isEmpty()) {
            int v = vertices.remove(0);
            if (!visited.contains(v)) {
                dfsVisit(v, visited, finished);
            }
        }
        return finished;
    }

    private void sccDFSVisit(Graph g, int v, HashSet<Integer> visited){
        visited.add(v);
        g.addVertex(v);
        HashSet<Integer> searchNodes= reverseEdges.get(v);
        for (int n : searchNodes) {
            if (!visited.contains(n)) {
                sccDFSVisit(g, n, visited);
                g.addEdge(v, n);  // this will be REVERSE directions - swap v & n?
            }
        }
    }

    private void dfsVisit(int v, HashSet<Integer> visited, Stack<Integer> finished) {
        visited.add(v);
        HashSet<Integer> searchNodes = edges.get(v);
        for (int n : searchNodes) {
            if (!visited.contains(n)) {
                dfsVisit(n, visited, finished);
            }
        }
        finished.push(v);
    }

    public HashMap<Integer, HashSet<Integer>> exportGraph(){
        HashMap<Integer, HashSet<Integer>> hash = new HashMap<>();
        for (Integer i : nodes) {
            hash.put(i, edges.get(i));
        }

        return hash;
    }
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i : nodes) {
            sb.append(i + ": ");
            for (int j : edges.get(i)) {
                sb.append(j + ", ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    public String graphToMinCutString() {
        StringBuffer sb = new StringBuffer();
        for (int i : nodes) {
            sb.append(i);
            for (Integer j : edges.get(i)) {
                sb.append("\t").append(j);
            }
            sb.append("\n");
        }


        return sb.toString();
    }
    public static void main(String[] args) {
        Graph g = new Graph();
        Parser.parseTestCode(g);
        System.out.println("Num Vert" + g.getNumVertices());
        System.out.println("Num Edge" + g.getNumEdges());
        System.out.println(g.toString());

        List<Graph> sccs = g.getSCCs();
        System.out.println(sccs.size() + " sccs");
        for (Graph temp : sccs) {
            System.out.println("=====");
            System.out.println(temp.toString());
        }
        System.out.println("\n\nREMOVE NODE 8\n");
        g.removeVertex(8);
        System.out.println("Num Vert" + g.getNumVertices());
        System.out.println("Num Edge" + g.getNumEdges());
        System.out.println(g.toString());

        sccs = g.getSCCs();
        System.out.println(sccs.size() + " sccs");
        for (Graph temp : sccs) {
            System.out.println("=====");
            System.out.println(temp.toString());
        }

    }
}