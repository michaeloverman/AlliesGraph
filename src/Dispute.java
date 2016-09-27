import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 9/16/2016.
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
