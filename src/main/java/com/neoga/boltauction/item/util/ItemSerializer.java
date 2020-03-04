package com.neoga.boltauction.item.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.neoga.boltauction.item.domain.Item;

import java.io.IOException;

public class ItemSerializer extends JsonSerializer<Item> {
    @Override
    public void serialize(Item value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeNumberField("itemId", value.getId());
        jgen.writeStringField("itemName", value.getName());
        jgen.writeStringField("itemImagePath", value.getImagePath());
        jgen.writeEndObject();
    }
}
