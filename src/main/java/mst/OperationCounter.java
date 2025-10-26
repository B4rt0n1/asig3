package mst;

/**
 * Simple operation counter to collect algorithmic metrics.
 */
public class OperationCounter {
    private long comparisons = 0;
    private long unions = 0;
    private long otherOps = 0;

    public void incComparisons() { comparisons++; }
    public void addComparisons(long n) { comparisons += n; }

    public void incUnions() { unions++; }
    public void addUnions(long n) { unions += n; }

    public void incOtherOps() { otherOps++; }
    public void addOtherOps(long n) { otherOps += n; }

    public long getComparisons() { return comparisons; }
    public long getUnions() { return unions; }
    public long getOtherOps() { return otherOps; }

    public void reset() {
        comparisons = unions = otherOps = 0;
    }
}
