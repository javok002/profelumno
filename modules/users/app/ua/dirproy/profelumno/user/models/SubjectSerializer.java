package ua.dirproy.profelumno.user.models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Created by tombatto on 26/09/15.
 */
public class SubjectSerializer extends JsonSerializer<Subject> {

    @Override
    public void serialize(Subject subject, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id",subject.getId());
        jsonGenerator.writeStringField("text", subject.getName());
        jsonGenerator.writeEndObject();
    }
}
