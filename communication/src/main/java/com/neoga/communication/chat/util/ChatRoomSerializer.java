package com.neoga.communication.chat.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.neoga.communication.chat.domain.ChatRoom;

import java.io.IOException;

public class ChatRoomSerializer extends JsonSerializer<ChatRoom> {
    @Override
    public void serialize(ChatRoom chatRoom, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        jgen.writeStartObject();
        jgen.writeNumberField("ChatRoomId", chatRoom.getId());
        jgen.writeEndObject();
    }
}
