import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 9/9/2016.
 */
public class Node {
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
//            System.out.println("    adding " + i);
            if (i != this.getId()) this.addNeighbor(i);
        }

        consumedNodes.add(other.getId());
//        System.out.println(toString());
//        System.out.println(other.toString());
//        cleanupSelfLoops();
    }
    public String toString() {
        StringBuffer sb = new StringBuffer(id + ": ");
        for(int i : neighbors) {
            sb.append(i + " ");
        }
        return sb.toString();
    }
}
