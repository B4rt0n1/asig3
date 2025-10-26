import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import graph.Edge;
import graph.Graph;
import io.EdgeSerializer;
import mst.MSTKruskal;
import mst.MSTPrim;
import mst.MSTResult;

/**
 * Processes all graphs from one JSON input file.
 * Outputs:
 *  - results/prim_<output>.json
 *  - results/kruskal_<output>.json
 *  - results/comparison_<output>.csv
 *
 * Example:
 * java -jar mst-assignment.jar small_graphs.json results_small.json
 */
public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java -jar mst-assignment.jar <input.json> <output.json>");
            System.exit(1);
        }

        String inputFile = args[0];
        String outputFile = args[1];

        // Ensure results folder
        File resultsDir = new File("results");
        if (!resultsDir.exists()) resultsDir.mkdir();

        // Define output paths
        String primOut = "results/prim_" + outputFile;
        String kruskalOut = "results/kruskal_" + outputFile;
        String csvOut = "results/comparison_" + outputFile.replace(".json", ".csv");

        try (Reader reader = new FileReader(inputFile)) {
            JsonElement root = JsonParser.parseReader(reader);
            if (!root.isJsonArray()) {
                System.err.println("Input JSON must contain an array of graphs.");
                System.exit(2);
            }

            JsonArray graphsArray = root.getAsJsonArray();
            List<JsonObject> primResults = new ArrayList<>();
            List<JsonObject> kruskalResults = new ArrayList<>();

            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(Edge.class, new EdgeSerializer())
                    .create();

            int index = 1;
            for (JsonElement graphElem : graphsArray) {
                JsonObject gObj = graphElem.getAsJsonObject();
                int vertices = gObj.get("vertices").getAsInt();

                // Build graph
                Graph g = new Graph(vertices);
                JsonArray edges = gObj.getAsJsonArray("edges");
                for (JsonElement eElem : edges) {
                    JsonObject e = eElem.getAsJsonObject();
                    int u = e.get("u").getAsInt();
                    int v = e.get("v").getAsInt();
                    double w = e.get("weight").getAsDouble();
                    g.addEdge(new Edge(u, v, w));
                }

                // Run algorithms
                MSTResult prim = MSTPrim.computeMST(g);
                MSTResult kruskal = MSTKruskal.computeMST(g);

                JsonObject primObj = gson.toJsonTree(prim).getAsJsonObject();
                primObj.addProperty("graphIndex", index);
                primObj.addProperty("vertices", vertices);
                primObj.addProperty("edges", g.edgeCount());
                primResults.add(primObj);

                JsonObject kruskalObj = gson.toJsonTree(kruskal).getAsJsonObject();
                kruskalObj.addProperty("graphIndex", index);
                kruskalObj.addProperty("vertices", vertices);
                kruskalObj.addProperty("edges", g.edgeCount());
                kruskalResults.add(kruskalObj);

                System.out.printf("Processed graph %d (V=%d, E=%d)%n", index++, vertices, g.edgeCount());
            }

            // Write JSON results
            try (Writer pw = new FileWriter(primOut);
                 Writer kw = new FileWriter(kruskalOut)) {
                gson.toJson(primResults, pw);
                gson.toJson(kruskalResults, kw);
            }

            // Write CSV comparison
            try (PrintWriter csv = new PrintWriter(Files.newBufferedWriter(Path.of(csvOut)))) {
                csv.println("GraphIndex,Vertices,Edges,Prim_TotalCost,Kruskal_TotalCost,Prim_TimeMs,Kruskal_TimeMs,Prim_Comparisons,Kruskal_Comparisons,Prim_Unions,Kruskal_Unions,Prim_OtherOps,Kruskal_OtherOps");
                for (int i = 0; i < primResults.size(); i++) {
                    JsonObject p = primResults.get(i);
                    JsonObject k = kruskalResults.get(i);

                    csv.printf(
                            Locale.US,
                            "%d,%d,%d,%.2f,%.2f,%.3f,%.3f,%d,%d,%d,%d,%d,%d%n",
                            p.get("graphIndex").getAsInt(),
                            p.get("vertices").getAsInt(),
                            p.get("edges").getAsInt(),
                            p.get("totalCost").getAsDouble(),
                            k.get("totalCost").getAsDouble(),
                            p.get("execTimeMs").getAsDouble(),
                            k.get("execTimeMs").getAsDouble(),
                            p.get("comparisons").getAsLong(),
                            k.get("comparisons").getAsLong(),
                            p.get("unions").getAsLong(),
                            k.get("unions").getAsLong(),
                            p.get("otherOps").getAsLong(),
                            k.get("otherOps").getAsLong()
                    );
                }
            }

            System.out.printf("""
                   All results written:
                   • %s
                   • %s
                   • %s
                """, primOut, kruskalOut, csvOut);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(3);
        }
    }
}
