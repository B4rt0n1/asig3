package mst;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import graph.Edge;
import graph.Graph;

/**
 * Kruskal's algorithm with averaged timing.
 */
public class MSTKruskal {

    private static final int REPEAT_RUNS = 10; // average over 10 runs

    private static class DSU {
        private final int[] parent;
        private final int[] rank;
        private final OperationCounter oc;

        DSU(int n, OperationCounter oc) {
            this.oc = oc;
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;
        }

        int find(int x) {
            oc.incComparisons();
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }

        boolean union(int a, int b) {
            int ra = find(a), rb = find(b);
            if (ra == rb) {
                oc.incComparisons();
                return false;
            }
            oc.incUnions();
            if (rank[ra] < rank[rb]) parent[ra] = rb;
            else if (rank[ra] > rank[rb]) parent[rb] = ra;
            else {
                parent[rb] = ra;
                rank[ra]++;
            }
            return true;
        }
    }

    public static MSTResult computeMST(Graph g) {
        MSTResult result = null;
        double totalTime = 0.0;

        for (int run = 0; run < REPEAT_RUNS; run++) {
            OperationCounter oc = new OperationCounter();
            List<Edge> edges = new ArrayList<>(g.getEdges());
            edges.sort(Comparator.naturalOrder());
            oc.addOtherOps(edges.size());

            DSU dsu = new DSU(g.getV(), oc);
            List<Edge> mst = new ArrayList<>();

            long start = System.nanoTime();
            for (Edge e : edges) {
                oc.incComparisons();
                if (dsu.union(e.getU(), e.getV())) {
                    mst.add(e);
                    if (mst.size() == g.getV() - 1) break;
                }
            }
            long end = System.nanoTime();
            double execMs = (end - start) / 1_000_000.0;
            totalTime += execMs;

            if (result == null) {
                double total = mst.stream().mapToDouble(Edge::getWeight).sum();
                result = new MSTResult(mst, total, g.getV(), g.edgeCount(),
                        oc.getComparisons(), oc.getUnions(), oc.getOtherOps(), 0.0);
            }
        }

        double avgTime = totalTime / REPEAT_RUNS;
        return new MSTResult(
                result.getMstEdges(), result.getTotalCost(),
                result.getOriginalVertices(), result.getOriginalEdges(),
                result.getComparisons(), result.getUnions(), result.getOtherOps(),
                avgTime
        );
    }
}