package io;

import graph.Edge;
import graph.Graph;
import com.google.gson.*;

import java.io.FileReader;
import java.io.Reader;
import java.util.List;

/**
 * Expected JSON structure:
 * {
 *   "vertices": N,
 *   "edges": [
 *     {"u": 0, "v": 1, "weight": 2.5},
 *     ...
 *   ]
 * }
 *
 * This class loads a single graph from a JSON file.
 */
public class InputGraph {
    public static Graph fromJsonFile(String path) throws Exception {
        try (Reader reader = new FileReader(path)) {
            JsonElement root = JsonParser.parseReader(reader);
            JsonObject obj = root.getAsJsonObject();

            int vertices = obj.get("vertices").getAsInt();
            Graph g = new Graph(vertices);

            JsonArray edges = obj.getAsJsonArray("edges");
            for (JsonElement el : edges) {
                JsonObject eo = el.getAsJsonObject();
                int u = eo.get("u").getAsInt();
                int v = eo.get("v").getAsInt();
                double w = eo.get("weight").getAsDouble();
                g.addEdge(new Edge(u, v, w));
            }
            return g;
        }
    }
}
