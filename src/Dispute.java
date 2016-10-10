import java.util.ArrayList;
import java.util.List;

/**
 * A Utility class used to create the overall graph structure. Created by the Parser, the Dispute holds the
 * States from each side of the dispute in the appropriate list. Contains methods to add States, return lists of each
 * side of the dispute, etc.
 */
public class Dispute {
    private List<String> sideA;
    private List<String> sideB;
    private int id;

    public Dispute(int id) {
        this.id = id;
        sideA = new ArrayList<>();
        sideB = new ArrayList<>();
    }
    /*
    Puts the state in the correct list according to side
     */
    public void addState(String s, int side) {
        switch (side) {
            case 0:
                sideB.add(s);
                break;
            case 1:
                sideA.add(s);
                break;
            default:
        }
    }
    public int getId() {
        return id;
    }
    public List<String> sideA() {
        List<String> list = new ArrayList<>();
        list.addAll(sideA);
        return list;
    }
    public List<String> sideB() {
        List<String> list = new ArrayList<>();
        list.addAll(sideB);
        return list;
    }
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Dispute #" + id + "\n  Side A: ");
        for (String s : sideA) {
            sb.append(s + " ");
        }
        sb.append("\n  Side B: ");
        for (String s : sideB) {
            sb.append(s + " ");
        }
        return sb.toString();
    }
}
