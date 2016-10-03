/**
 * Created by Michael on 9/9/2016.
 */
public class Edge {
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

    public boolean equals(Edge other) {
        if(this.head == other.head && this.tail == other.tail)
            return true;
        return false;
    }
}
