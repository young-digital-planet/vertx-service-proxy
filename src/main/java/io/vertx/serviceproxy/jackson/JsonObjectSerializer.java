package io.vertx.serviceproxy.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.vertx.core.json.JsonObject;

import java.io.IOException;

public class JsonObjectSerializer extends JsonSerializer<JsonObject> {
    @Override
    public void serialize(JsonObject value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (value == null) {
            jgen.writeNull();
        } else {
            jgen.writeObject(value.getMap());
        }
    }

    @Override
    public Class<JsonObject> handledType() {
        return JsonObject.class;
    }
}
