package com.neoga.boltauction.memberstore.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.neoga.boltauction.chat.domain.ChatRoom;
import com.neoga.boltauction.memberstore.member.domain.Members;

import java.io.IOException;
import java.lang.reflect.Member;

public class MemberSerializer extends JsonSerializer<Members> {
    @Override
    public void serialize(Members members, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        jgen.writeStartObject();
        jgen.writeNumberField("MemberId", members.getId());
        jgen.writeStringField("MemberName", members.getName());
        jgen.writeNumberField("StoreId", members.getStore().getId());
        jgen.writeEndObject();
    }
}
