package mst;

import java.util.ArrayList;
import java.util.List;

import graph.Edge;

/**
 * Holds MST result data for output and comparison.
 */
public class MSTResult {
    private final List<Edge> mstEdges;
    private final double totalCost;
    private final int originalVertices;
    private final int originalEdges;
    private final long comparisons;
    private final long unions;
    private final long otherOps;
    private final double execTimeMs;

    public MSTResult(List<Edge> mstEdges, double totalCost, int originalVertices, int originalEdges,
                     long comparisons, long unions, long otherOps, double execTimeMs) {
        this.mstEdges = new ArrayList<>(mstEdges);
        this.totalCost = totalCost;
        this.originalVertices = originalVertices;
        this.originalEdges = originalEdges;
        this.comparisons = comparisons;
        this.unions = unions;
        this.otherOps = otherOps;
        this.execTimeMs = execTimeMs;
    }

    // getters for JSON serialization
    public List<Edge> getMstEdges() { return mstEdges; }
    public double getTotalCost() { return totalCost; }
    public int getOriginalVertices() { return originalVertices; }
    public int getOriginalEdges() { return originalEdges; }
    public long getComparisons() { return comparisons; }
    public long getUnions() { return unions; }
    public long getOtherOps() { return otherOps; }
    public double getExecTimeMs() { return execTimeMs; }
}
