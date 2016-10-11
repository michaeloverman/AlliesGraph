import java.util.*;

/**
 * The State class is essentially the data structure which holds all relevante information for each
 * node in the larger graph. Some of the data is data from the original datasource, but other pieces
 * are details calculated in this AlliesGraph, such as: egonetSize, sccSizeChange, minCutSize, etc.
 * Methods are descriptively named, and consist almost entirely of getters and setters. Several Comparators
 * at the end allow for ordering by various measures.
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
    private double minCutSizePerAlly;
    private boolean hasMinCut; // a simple 'tally' of whether the time-consuming min-cut operation has been completed
    private double grandMeasureOfConnectedness;

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
        minCutSizePerAlly = (double) minCutSize / egonetSize;
        grandMeasureOfConnectedness = minCutSizePerAlly - sccChangePerAlly;
    }

    public double getGrandMeasureOfConnectedness() {
        return grandMeasureOfConnectedness;
    }

    public void setHasMinCut(boolean hmc) {
        this.hasMinCut = hmc;
    }

    public boolean getHasMinCut() {
        return hasMinCut;
    }

    public double getMinCutSizePerAlly() {
        return minCutSizePerAlly;
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

    public void addAlly(int ally) {
        if (allies.containsKey(ally)) {
            int current = allies.get(ally);
            allies.put(ally, current + 1);
        } else {
            allies.put(ally, 1);
        }
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
     * Comparator to sort states by minCutSize
     */
    public static Comparator<State> StatesByMinCutComparator = new Comparator<State>() {
        @Override
        public int compare(State o1, State o2) {
            if (o1.minCutSizePerAlly > o2.minCutSizePerAlly) return -1;
            if (o2.minCutSizePerAlly > o1.minCutSizePerAlly) return 1;
            return 0;
        }
    };

    /**
     * Comparator to sort states by grandMeasureOfConnectedness
     */
    public static Comparator<State> StatesByGrandMeasureComparator = new Comparator<State>() {
        @Override
        public int compare(State o1, State o2) {
            if (o1.grandMeasureOfConnectedness > o2.grandMeasureOfConnectedness) return -1;
            if (o2.grandMeasureOfConnectedness > o1.grandMeasureOfConnectedness) return 1;
            return 0;
        }
    };
}
