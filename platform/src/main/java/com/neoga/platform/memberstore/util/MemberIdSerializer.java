package com.neoga.platform.memberstore.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.neoga.platform.memberstore.member.domain.Members;

import java.io.IOException;

public class MemberIdSerializer extends JsonSerializer<Members> {
    @Override
    public void serialize(Members members, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        jgen.writeStartObject();
        jgen.writeNumberField("memberId", members.getId());
        jgen.writeEndObject();
    }
}
