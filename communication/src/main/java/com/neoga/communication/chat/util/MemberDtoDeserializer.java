package com.neoga.communication.chat.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.neoga.communication.chat.dto.MemberDto;

import java.io.IOException;

public class MemberDtoDeserializer
        extends JsonDeserializer<MemberDto> {

  @Override
  public MemberDto deserialize(JsonParser jsonParser,
                               DeserializationContext deserializationContext)
          throws IOException, JsonProcessingException {
    ObjectCodec oc = jsonParser.getCodec();
    JsonNode node = oc.readTree(jsonParser);
    return MemberDto.builder()
            .memberId(node.get("id").asLong())
            .memberName(node.get("name").asText())
            .imagePath(node.get("imagePath").asText()).build();
  }
}
