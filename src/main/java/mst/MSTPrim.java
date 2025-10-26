package mst;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

import graph.Edge;
import graph.Graph;

/**
 * Prim's algorithm with averaged timing over multiple runs.
 */
public class MSTPrim {

    private static final int REPEAT_RUNS = 10; // repeat for timing accuracy

    public static MSTResult computeMST(Graph g) {
        MSTResult result = null;
        double totalTime = 0.0;

        for (int run = 0; run < REPEAT_RUNS; run++) {
            OperationCounter oc = new OperationCounter();
            boolean[] visited = new boolean[g.getV()];
            double[] minWeight = new double[g.getV()];
            Edge[] edgeTo = new Edge[g.getV()];

            Arrays.fill(minWeight, Double.POSITIVE_INFINITY);
            minWeight[0] = 0.0;

            PriorityQueue<VertexEntry> pq = new PriorityQueue<>();
            pq.add(new VertexEntry(0, 0.0));
            oc.incOtherOps();

            List<Edge> mstEdges = new ArrayList<>();
            long start = System.nanoTime();

            while (!pq.isEmpty()) {
                VertexEntry ve = pq.poll();
                oc.incOtherOps();
                int v = ve.vertex;
                if (visited[v]) {
                    oc.incComparisons();
                    continue;
                }
                visited[v] = true;
                Edge e = edgeTo[v];
                if (e != null) mstEdges.add(e);

                for (Edge edge : g.adj(v)) {
                    int w = edge.other(v);
                    if (visited[w]) {
                        oc.incComparisons();
                        continue;
                    }
                    oc.incComparisons();
                    if (edge.getWeight() < minWeight[w]) {
                        minWeight[w] = edge.getWeight();
                        edgeTo[w] = edge;
                        pq.add(new VertexEntry(w, minWeight[w]));
                        oc.incOtherOps();
                    }
                }
            }

            long end = System.nanoTime();
            double execMs = (end - start) / 1_000_000.0;
            totalTime += execMs;

            // only need to keep one representative result
            if (result == null) {
                double totalCost = mstEdges.stream().mapToDouble(Edge::getWeight).sum();
                result = new MSTResult(
                        mstEdges, totalCost, g.getV(), g.edgeCount(),
                        oc.getComparisons(), oc.getUnions(), oc.getOtherOps(), 0.0
                );
            }
        }

        // Replace time with average
        double avgTime = totalTime / REPEAT_RUNS;
        return new MSTResult(
                result.getMstEdges(), result.getTotalCost(),
                result.getOriginalVertices(), result.getOriginalEdges(),
                result.getComparisons(), result.getUnions(),
                result.getOtherOps(), avgTime
        );
    }

    private static class VertexEntry implements Comparable<VertexEntry> {
        final int vertex;
        final double weight;

        VertexEntry(int v, double w) {
            this.vertex = v;
            this.weight = w;
        }

        @Override
        public int compareTo(VertexEntry o) {
            return Double.compare(this.weight, o.weight);
        }
    }
}
