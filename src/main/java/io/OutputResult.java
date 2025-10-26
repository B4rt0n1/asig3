package io;

import java.io.FileWriter;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mst.MSTResult;

/**
 * Writes MSTResult objects into a JSON file (pretty-printed).
 */
public class OutputResult {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(graph.Edge.class, new EdgeSerializer())
            .create();

    public static void writeResult(String path, MSTResult primRes, MSTResult kruskalRes) throws Exception {
        try (Writer w = new FileWriter(path)) {
            JsonWrapper wrapper = new JsonWrapper(primRes, kruskalRes);
            GSON.toJson(wrapper, w);
        }
    }

    // wrapper structure for output file
    private static class JsonWrapper {
        final MSTResult prim;
        final MSTResult kruskal;

        JsonWrapper(MSTResult prim, MSTResult kruskal) {
            this.prim = prim;
            this.kruskal = kruskal;
        }
    }
}
