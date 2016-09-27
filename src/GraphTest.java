import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Michael on 9/26/2016.
 */
public class GraphTest {
    private Graph g;
    @org.junit.Before
    public void setUp() throws Exception {
        g = new Graph();
        Parser.parseTestCode(g);
    }

    @org.junit.After
    public void tearDown() throws Exception {
        g = null;
    }

    @org.junit.Test
    public void getNumVertices() throws Exception {
        assertEquals(14, g.getNumVertices());
    }

    @org.junit.Test
    public void getNumEdges() throws Exception {
        assertEquals(34, g.getNumEdges());
    }

    @org.junit.Test
    public void addVertex() throws Exception {
        g.addVertex(15);
        assertEquals(15, g.getNumVertices());
    }

    @org.junit.Test
    public void getVertices() throws Exception {

    }

    @org.junit.Test
    public void addEdge() throws Exception {
        g.addVertex(15);
        g.addEdge(14, 15);
        g.addEdge(15, 14);
        assertEquals(36, g.getNumEdges());
    }

    @org.junit.Test
    public void getEgonet() throws Exception {
        Graph test = g.getEgonet(7);
        assertEquals("Egonet 7 Nodes", 4, test.getNumVertices());
//        assertEquals("Egonet 7 Edges", 6, test.getNumEdges());
//
        test = g.getEgonet(14);
        assertEquals("Egonet 14 Nodes", 3, test.getNumVertices());
//        assertEquals("Egonet 14 Edges", 6, test.getNumEdges());

        g.addVertex(15);
        g.addEdge(14, 15);
        g.addEdge(15, 14);
        test = g.getEgonet(14);
        assertEquals("Egonet 14 after addition Nodes", 4, test.getNumVertices());
        assertEquals("Egonet 14 after addition Edges", 8, test.getNumEdges());

    }

    @org.junit.Test
    public void exportGraph() throws Exception {

    }

    @org.junit.Test
    public void getSCCs() throws Exception {
        List<Graph> sccs = g.getSCCs();
        for (Graph g : sccs) {
            System.out.println("====" + sccs.toString());
        }
    }

}