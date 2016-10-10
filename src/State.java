import java.util.*;

/**
 * The State class is essentially the data structure which holds all relevante information for each
 * node in the larger graph. Some of the data is data from the original datasource, but other pieces
 * are details calculated in this AlliesGraph, such as: egonetSize, sccSizeChange, minCutSize, etc.
 */
public class State implements Comparable {
    private String name;
    private int code;
    private String abbrev;

    private Map<Integer, Integer> allies; // country code, weight
    private ArrayList<State> consumedStates; // for minimum cut methods

    private int egonetSize;
    private int sccSizeChange;
    private double sccChangePerAlly;
    private int minCutSize;
    private int minCutSizeChange;
    private double minCutChangePerAlly;
    private boolean hasMinCut; // a simple 'tally' of whether the time-consuming min-cut operation has been completed

    public State(String abbrev, int code, String name) {
        this.name = name;
        this.code = code;
        this.abbrev = abbrev;
        allies = new HashMap<>();
        consumedStates = new ArrayList<>();
        sccSizeChange = 0;
        hasMinCut = false;
    }

    /**\
     * Getters and Setters for various details about the State
     */
    public int getMinCutSize() {
        return minCutSize;
    }
    public void setMinCutSize(int minCutSize) {
        this.minCutSize = minCutSize;
    }
    public void setHasMinCut(boolean hmc) {
        this.hasMinCut = hmc;
    }
    public boolean getHasMinCut() {
        return hasMinCut;
    }
    public int getMinCutSizeChange() {
        return minCutSizeChange;
    }
    public void setMinCutSizeChange(int minCutSizeChange) {
        this.minCutSizeChange = minCutSizeChange;
        minCutChangePerAlly = (double) minCutSizeChange / egonetSize;
    }
    public double getMinCutChangePerAlly() {
        return minCutChangePerAlly;
    }

    public int getEgonetSize() {
        return egonetSize;
    }

    public void setEgonetSize(int egonetSize) {
        this.egonetSize = egonetSize;
    }

    public void setSccChange(int i) {
        sccSizeChange = i;
        sccChangePerAlly = (double) sccSizeChange / egonetSize;
    }
    public int getSccChange() {
        return sccSizeChange;
    }

    public double getSccChangePerAlly() {
        return sccChangePerAlly;

    }
    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public String getAbbrev() {
        return abbrev;
    }

    public String toString() {
        return "Name: " + name + ", Abbr: " + abbrev + ", Code: " + code;
    }
    public String alliesToString() {
        StringBuffer sb = new StringBuffer();
        for (int i : allies.keySet()) {
            sb.append(code + ";" + i + ";" + allies.get(i) + "\n");
        }
        return sb.toString();
    }
    public void addAlly(int ally) {
        if (allies.containsKey(ally)) {
            int current = allies.get(ally);
            allies.put(ally, current + 1);
        } else {
            allies.put(ally, 1);
        }
    }
    public HashSet<Integer> getAllies() {
        HashSet<Integer> list = new HashSet<>();
        for (int i : allies.keySet()) {
            list.add(i);
        }
        return list;
    }

    /**
     * compareTo override, sorts States by egonet size
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o) {
        State other = (State) o;
        if (egonetSize > other.egonetSize) return -1;
        else if (egonetSize < other.egonetSize) return 1;
        else return 0;
    }

    /**
     * Comparator to sort States by scc Size Change per ally
     */
    public static Comparator<State> StatesBySccChangeComparator = new Comparator<State>() {
        @Override
        public int compare(State o1, State o2) {
            if (o1.sccChangePerAlly > o2.sccChangePerAlly) return -1;
            if (o2.sccChangePerAlly > o1.sccChangePerAlly) return 1;
            return 0;
        }
    };
    /**
     * Comparator to sort states by minCut
     */
    public static Comparator<State> StatesByMinCutComparator = new Comparator<State>() {
        @Override
        public int compare(State o1, State o2) {
            if (o1.minCutChangePerAlly > o2.minCutChangePerAlly) return -1;
            if (o2.minCutChangePerAlly > o1.minCutChangePerAlly) return 1;
            return 0;
        }
    };
    public String egonetToString() {
        return name + ": " + egonetSize;
    }

}
