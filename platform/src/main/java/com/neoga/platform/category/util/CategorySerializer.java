package com.neoga.platform.category.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.neoga.platform.category.domain.Category;

import java.io.IOException;

public class CategorySerializer extends JsonSerializer<Category> {
    @Override
    public void serialize(Category category, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", category.getId());
        jsonGenerator.writeStringField("name", category.getName());
        if(category.getSupCategory() != null) {
            jsonGenerator.writeNumberField("supCategoryId", category.getSupCategory().getId());
            jsonGenerator.writeStringField("supCategoryName", category.getSupCategory().getName());
        }
        jsonGenerator.writeEndObject();
    }
}