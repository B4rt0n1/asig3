package graph;

import java.util.*;

/**
 * Undirected weighted graph.
 */
public class Graph {
    private final int V;
    private final List<Edge> edges;
    private final List<List<Edge>> adj;

    public Graph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be non-negative");
        this.V = V;
        this.edges = new ArrayList<>();
        this.adj = new ArrayList<>(V);
        for (int i = 0; i < V; i++) adj.add(new ArrayList<>());
    }

    public int getV() { return V; }

    public List<Edge> getEdges() { return Collections.unmodifiableList(edges); }

    public void addEdge(Edge e) {
        edges.add(e);
        adj.get(e.getU()).add(e);
        adj.get(e.getV()).add(e);
    }

    public Iterable<Edge> adj(int v) {
        return Collections.unmodifiableList(adj.get(v));
    }

    public int edgeCount() {
        return edges.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph: V=").append(V).append(", E=").append(edgeCount()).append("\n");
        for (Edge e : edges) sb.append(e.toString()).append("\n");
        return sb.toString();
    }
}
