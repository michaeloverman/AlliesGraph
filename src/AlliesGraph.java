import java.util.*;

/**
 * Created by Michael on 9/16/2016.
 */
public class AlliesGraph extends Graph {
    private static HashMap<Integer, State> states;
    private static HashMap<Integer, Dispute> disputes;
    private static HashMap<String, Integer> abbrevs;
    private static HashMap<Integer, Graph> stateEgonets;
    private static HashMap<Integer, Graph> removedStateEgonets;
    private static HashMap<Integer, List<Graph>> removedEgoSCCs;
//    private HashMap<Integer, HashSet<Integer>> edges;
//    private HashMap<Integer, HashSet<Integer>> reverseEdges;
//    private int numEdges;

    public AlliesGraph() {
        states = new HashMap<Integer, State>();
        disputes = new HashMap<Integer, Dispute>();
        abbrevs = new HashMap<String, Integer>();
        removedStateEgonets = new HashMap<>();
        removedEgoSCCs = new HashMap<>();
//        edges = new HashMap<>();
//        reverseEdges = new HashMap<>();
//        numEdges = 0;
    }
    public void addDispute(Dispute disp) {
        disputes.put(disp.getId(), disp);
    }
    public Dispute getDispute(int id) {
        return disputes.get(id);
    }
    public boolean containsDispute(int id) {
        return disputes.containsKey(id);
    }
//    public void addVertex(int num) {
//        addState(states.get(num));
//    }
    public void addState(State s) {
        states.put(s.getCode(), s);
        abbrevs.put(s.getAbbrev(), s.getCode());
        edges.put(s.getCode(), new HashSet<>());
        reverseEdges.put(s.getCode(), new HashSet<>());
        nodes.add(s.getCode());
    }
//    public int getNumVertices() {
//        return getStateCount();
//    }
//    public List<Integer> getVertices() {
//        List<Integer> list = new ArrayList<>();
//        for (State s : getStates()) {
//            list.add(s.getCode());
//        }
//        return list;
//    }
    public int getStateCount() {
        return states.size();
    }
    public List<State> getStates() {
        ArrayList<State> list = new ArrayList<>();
        for (State s : states.values()) {
            list.add(s);
        }
        return list;
    }
//    public int getNumEdges() {
//        return numEdges;
//    }

    public void disputesToEdges() {
        for (Dispute d : disputes.values()) {
//            System.out.println(d.toString());
            if (d.sideA().size() > 1) {
                for (int i = 0; i < d.sideA().size() - 1; i++) {
//                    System.out.print("outer loop " + i + ": ");
                    for (int j = i + 1; j < d.sideA().size(); j++) {
//                        System.out.println(d.getId() + " " + i + " " + j);
                        String sa = d.sideA().get(i);
                        String sb = d.sideA().get(j);
                        int a = abbrevs.get(sa);
                        int b = abbrevs.get(sb);
                        edges.get(a).add(b);
                        reverseEdges.get(b).add(a);
                        edges.get(b).add(a);
                        reverseEdges.get(a).add(b);
                        State one = states.get(a);
                        State two = states.get(b);
                        one.addAlly(two.getCode());
                        two.addAlly(one.getCode());
                        numEdges++;
                    }
//                    System.out.println("");
                }
            }
            if (d.sideB().size() > 1) {
                for (int i = 0; i < d.sideB().size() - 1; i++) {
//                    System.out.print("outer loop " + i + ": ");
                    for (int j = i + 1; j < d.sideB().size(); j++) {
//                        System.out.println(d.getId() + " " + i + " " + j);
                        String sa = d.sideB().get(i);
                        String sb = d.sideB().get(j);
                        int a = abbrevs.get(sa);
                        int b = abbrevs.get(sb);
                        State one = states.get(a);
                        State two = states.get(b);
                        one.addAlly(two.getCode());
                        two.addAlly(one.getCode());
                        numEdges++;
                    }
//                    System.out.println("");
                }
            }

    //        System.out.println(" : " + d.sideA().toString());
        }

    }

    private void findAlliesAndEgonets() {
        stateEgonets = new HashMap<>();
        for (State s : states.values()) {
            Graph egonet = getEgonet(s.getCode());
            stateEgonets.put(s.getCode(), egonet);
            s.setEgonetSize(stateEgonets.get(s.getCode()).getNumVertices());

            Graph secondEgonet = getEgonet(s.getCode());
            secondEgonet.removeVertex(s.getCode());
            removedStateEgonets.put(s.getCode(), secondEgonet);

            List<Graph> sccs = secondEgonet.getSCCs();
            removedEgoSCCs.put(s.getCode(), sccs);
            s.setSccChange(sccs.size() - 1);
        }

//        for (int i : stateEgonets.keySet()) {
//            System.out.println("State: " + states.get(i).getName() + " Egonet Size: " +
//                    stateEgonets.get(i).getNumVertices());
//        }

    }
//    @Override
//    public void dfsVisit(int v, HashSet<Integer> visited, Stack<Integer> finished) {
//        visited.add(v);
//        HashSet<Integer> searchNodes = states.get(v).getAllies();
//        for (int n : searchNodes) {
//            if (!visited.contains(n)) {
//                dfsVisit(n, visited, finished);
//            }
//        }
//        finished.push(v);
//    }
    public void egonetsBySize() {
        egonetsBySize(1000);
    }
    public void egonetsBySize(int howMany) {
        if (stateEgonets == null) return;
        PriorityQueue<State> tempStates = new PriorityQueue<>(Math.min(howMany, states.size()));
        tempStates.addAll(states.values());
        while (!tempStates.isEmpty()) {

            System.out.println(tempStates.remove().egonetToString());
        }
    }
//    public Graph getEgonet(int center) {
//        return alliesEgonet(states.get(center));
//    }
//    public Graph alliesEgonet(State state) {
//        AlliesGraph egonet = new AlliesGraph();
//        if (!states.containsValue(state)) return egonet;
//        egonet.addState(state);
//        for(int i : state.getAllies()) {
//            egonet.addState(states.get(i));
//            egonet.addEdge(state.getCode(), i);
//        }
//        for (int i : egonet.getVertices()) {
//            for (int j : states.get(i).getAllies()) {
//                egonet.addEdge(i, j);  // addEdge method does not add the edge, if the neighbor is not already in the egonet
//            }
//        }
//
//        return egonet;
//    }
    public List<State> statesBySccChange(int howMany) {
        howMany = Math.min(howMany, states.size());
        ArrayList<State> queue = new ArrayList<>(howMany);
        queue.addAll(states.values());
        Collections.sort(queue, State.StatesBySccChangeComparator);

        return queue.subList(0, howMany);
    }
    public List<State> statesByMinCutChange() {
        ArrayList<State> queue = new ArrayList<>();
        for (State s : states.values()) {
            if(s.getHasMinCut()) queue.add(s);
        }
        Collections.sort(queue, State.StatesByMinCutComparator);

        return queue;
    }
    public List<State> statesBySccChange() {
        return statesBySccChange(Math.max(1000, states.size()));
    }
    public HashMap<Integer, HashSet<Integer>> exportGraph() {
        HashMap<Integer, HashSet<Integer>> hash = new HashMap<>();
        for (Integer i : states.keySet()) {
            hash.put(i, states.get(i).getAllies());
        }
        return hash;
    }
    public void findMinCutSizes() {
        findMinCutSizes(15);  // set minimum size of egonet/number of Allies for practical use of data
    }
    public void findMinCutSizes(int minimumAllies) {
        for (State s : states.values()) {
            if (s.getEgonetSize() < 15) continue;
            System.out.println("MinCutting " + s.getName());
            MinCutGraph mcg = new MinCutGraph();
            mcg.parseGraph(stateEgonets.get(s.getCode()).graphToMinCutString());
            s.setMinCutSize(mcg.getMinCut());
            mcg.parseGraph(removedStateEgonets.get(s.getCode()).graphToMinCutString());
            s.setMinCutSizeChange(s.getMinCutSize() - mcg.getMinCut());
            s.setHasMinCut(true);
        }
    }
    public static void main(String[] args) {
        AlliesGraph graph = new AlliesGraph();
        Parser.parseStateCodes(graph);
        System.out.println(graph.getStateCount() + " states in graph");
//        for (State s : graph.getStates()) {
//            System.out.println(s.toString());
//        }
        Parser.parseDisputes(graph);
        graph.disputesToEdges();
        System.out.println(graph.disputes.size() + " disputes");
//        for (State s : graph.getStates()) {
//            System.out.print(s.alliesToString());
//
//        }

//        List<Graph> list = graph.getSCCs();
//        System.out.println(list.size() + " SCCs");
//        for (Graph g : list) {
//            System.out.println("=====");
//            System.out.println(g.toString());
//        }

        System.out.println("Finding Allies & Egonets:");
        graph.findAlliesAndEgonets();
//        System.out.println("Finished Finding Allies");

//        graph.egonetsBySize();

//        int VERTEXTOTEST = 2;
        graph.findMinCutSizes(25);
        List<State> newestList = graph.statesByMinCutChange();
        for (State s : newestList) {
//            Graph testEgo = graph.stateEgonets.get(VERTEXTOTEST);
//            List<Graph> templist = testEgo.getSCCs();
//            int presize = templist.size();
//            System.out.println(templist.size() + " SCCs for " + graph.states.get(VERTEXTOTEST).getName());
//        for (Graph g : testList) {
//            System.out.println("=====");
//            System.out.println(g.toString());
//        }

//        System.out.println(testEgo.toString());
//            testEgo.removeVertex(VERTEXTOTEST);
//        System.out.println(testEgo.toString());

//            templist = testEgo.getSCCs();
//            int postsize = templist.size();
//            System.out.println(templist.size() + " SCCs after removing ego: " + graph.states.get(VERTEXTOTEST).getName());
//        for (Graph g : testEgo.getSCCs()) {
//            System.out.println("=====");
//            System.out.println(g.toString());
//        }
//            State s = states.get(VERTEXTOTEST);

            StringBuffer sb = new StringBuffer();
            sb.append(s.getName());
            sb.append("\n Number of Allies: ");
            sb.append(s.getEgonetSize());
            sb.append("\n SCC size Change: ");
            sb.append(s.getSccChange());
            sb.append("\n SCC size Change Per Ally: ");
            sb.append(String.format("%.04f", s.getSccChangePerAlly()));
            sb.append("\n Min Cut Size Change: ").append(s.getMinCutSizeChange());
            sb.append("\n Min Cut Change Per Ally: ").append(s.getMinCutChangePerAlly());
            sb.append("\n");
            System.out.println(sb.toString());

//            System.out.println(stateEgonets.get(s.getCode()).graphToMinCutString());
        }

    }
}
