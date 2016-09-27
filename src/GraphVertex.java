import java.util.HashSet;

public class GraphVertex {

    private int id;
    private HashSet<Integer> neighbors;
    private HashSet<Integer> reverseNeighbors;

    public GraphVertex(int id) {
        this.id = id;
        neighbors = new HashSet<Integer>();
        reverseNeighbors = new HashSet<Integer>();
    }

    public void addEdge(int to) {
        neighbors.add(to);
    }
    public void addReverseEdge(int from) {
        reverseNeighbors.add(from);
    }

    public boolean hasEdge(int to) {
        return neighbors.contains(to);
    }

    public int getId() {
        return id;
    }

    public HashSet<Integer> getNeighbors() {
        HashSet<Integer> list = new HashSet<Integer>();
        for (int i : neighbors) {
            list.add(i);
        }
        return list;
    }
    public HashSet<Integer> getReverseNeighbors() {
        HashSet<Integer> list = new HashSet<Integer>();
        for (int i : reverseNeighbors) {
            list.add(i);
        }
        return list;
    }
}
