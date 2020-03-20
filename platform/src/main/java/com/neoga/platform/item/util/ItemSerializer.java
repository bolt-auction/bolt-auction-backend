package com.neoga.platform.item.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.neoga.platform.item.domain.Item;

import java.io.IOException;

public class ItemSerializer extends JsonSerializer<Item> {
    @Override
    public void serialize(Item value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {
        jgen.writeStartObject();
        jgen.writeNumberField("itemId", value.getId());
        jgen.writeStringField("itemName", value.getName());
        jgen.writeStringField("itemImagePath", value.getImagePath());
        jgen.writeEndObject();
    }
}
