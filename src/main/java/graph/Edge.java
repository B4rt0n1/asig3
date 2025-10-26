package graph;

/**
 * Edge for an undirected weighted graph.
 */
public class Edge implements Comparable<Edge> {
    private final int u; // one endpoint
    private final int v; // other endpoint
    private final double weight;

    public Edge(int u, int v, double weight) {
        if (u < 0 || v < 0) throw new IllegalArgumentException("Vertex indices must be non-negative");
        this.u = u;
        this.v = v;
        this.weight = weight;
    }

    public int either() {
        return u;
    }

    public int other(int vertex) {
        if (vertex == u) return v;
        if (vertex == v) return u;
        throw new IllegalArgumentException("Vertex not incident to edge");
    }

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public int compareTo(Edge o) {
        return Double.compare(this.weight, o.weight);
    }

    @Override
    public String toString() {
        return String.format("(%d - %d : %.2f)", u, v, weight);
    }
}
