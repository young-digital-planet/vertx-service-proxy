package io.vertx.serviceproxy.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonArrayDeserializer extends JsonDeserializer<JsonArray> {
    @Override
    public JsonArray deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.readValueAsTree();
        if (node == null) {
            return null;
        }
        return new JsonArray(jp.getCodec().treeToValue(node, List.class));
    }

    @Override
    public Class<JsonArray> handledType() {
        return JsonArray.class;
    }
}
