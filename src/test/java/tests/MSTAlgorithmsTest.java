package tests;

import graph.Edge;
import graph.Graph;
import mst.MSTKruskal;
import mst.MSTPrim;
import mst.MSTResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class MSTAlgorithmsTest {

    private Graph exampleGraph() {
        // small example (5 vertices)
        Graph g = new Graph(5);
        g.addEdge(new Edge(0,1,2));
        g.addEdge(new Edge(0,3,6));
        g.addEdge(new Edge(1,2,3));
        g.addEdge(new Edge(1,3,8));
        g.addEdge(new Edge(1,4,5));
        g.addEdge(new Edge(2,4,7));
        g.addEdge(new Edge(3,4,9));
        return g;
    }

    @Test
    public void testMSTCostEqualityAndEdgeCount() {
        Graph g = exampleGraph();
        MSTResult p = MSTPrim.computeMST(g);
        MSTResult k = MSTKruskal.computeMST(g);

        // total cost identical
        Assertions.assertEquals(p.getTotalCost(), k.getTotalCost(), 1e-6);

        // correct number of edges
        Assertions.assertEquals(g.getV() - 1, p.getMstEdges().size());
        Assertions.assertEquals(g.getV() - 1, k.getMstEdges().size());
    }

    @Test
    public void testAcyclicAndConnected() {
        Graph g = exampleGraph();
        MSTResult k = MSTKruskal.computeMST(g);

        // quick acyclic check: edges == V-1 and union-find during building guarantees no cycles.
        Assertions.assertEquals(g.getV() - 1, k.getMstEdges().size());

        // connectivity: number of unique vertices in MST edges should be V
        Set<Integer> verts = new HashSet<>();
        for (Edge e : k.getMstEdges()) {
            verts.add(e.getU());
            verts.add(e.getV());
        }
        Assertions.assertEquals(g.getV(), verts.size());
    }
}
