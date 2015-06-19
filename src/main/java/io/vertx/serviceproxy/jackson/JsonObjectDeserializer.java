package io.vertx.serviceproxy.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.vertx.core.json.JsonObject;

import java.io.IOException;
import java.util.Map;

public class JsonObjectDeserializer extends JsonDeserializer<JsonObject> {
    @Override
    public JsonObject deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.readValueAsTree();
        if (node == null) {
            return null;
        }
        return new JsonObject(jp.getCodec().treeToValue(node, Map.class));
    }

    @Override
    public Class<JsonObject> handledType() {
        return JsonObject.class;
    }
}
