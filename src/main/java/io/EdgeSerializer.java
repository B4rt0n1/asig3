package io;

import graph.Edge;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class EdgeSerializer implements JsonSerializer<Edge> {
    @Override
    public JsonElement serialize(Edge edge, Type type, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("u", edge.getU());
        obj.addProperty("v", edge.getV());
        obj.addProperty("weight", edge.getWeight());
        return obj;
    }
}
