import java.util.*;

/**
 * Created by Michael on 9/16/2016.
 */
public class State implements Comparable {
    private String name;
    private int code;
    private String abbrev;

    private Map<Integer, Integer> allies; // country code, weight
    private Map<Integer, Integer> enemies;
    private int allyCount;
    private int enemyCount;

    private int egonetSize;

    public int getEgonetSize() {
        return egonetSize;
    }

    public void setEgonetSize(int egonetSize) {
        this.egonetSize = egonetSize;
    }

    public State(String abbrev, int code, String name) {
        this.name = name;
        this.code = code;
        this.abbrev = abbrev;
        allies = new HashMap<>();
        enemies = new HashMap<>();
        allyCount = 0;
        enemyCount = 0;
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
    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public String getAbbrev() {
        return abbrev;
    }

    public void addAlly(int ally) {
        if (allies.containsKey(ally)) {
            int current = allies.get(ally);
            allies.put(ally, current + 1);
        } else {
            allies.put(ally, 1);
        }
        allyCount++;
    }
    public HashSet<Integer> getAllies() {
        HashSet<Integer> list = new HashSet<>();
        for (int i : allies.keySet()) {
            list.add(i);
        }
        return list;
    }
    public void addEnemy(int enemy) {
        if (enemies.containsKey(enemy)) {
            int current = enemies.get(enemy);
            enemies.put(enemy, current + 1);
        } else {
            enemies.put(enemy, 1);
        }
        enemyCount++;
    }

    @Override
    public int compareTo(Object o) {
        State other = (State) o;
        if (egonetSize > other.egonetSize) return -1;
        else if (egonetSize < other.egonetSize) return 1;
        else return 0;
    }
    public String egonetToString() {
        return name + ": " + egonetSize;
    }
}
