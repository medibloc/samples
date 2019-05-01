package org.medibloc.insurance_java_ko.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.protobuf.util.JsonFormat;
import org.medibloc.phr.ClaimDataV1.Claim;

public class ClaimDeserializer extends JsonDeserializer {
    @Override
    public Claim deserialize(JsonParser jsonParser, DeserializationContext ctxt) {
        try {
            Claim.Builder builder = Claim.newBuilder();
            JsonFormat.parser().merge(jsonParser.readValueAsTree().toString(), builder);
            return builder.build();
        } catch (Exception ex) {
            throw new RuntimeException("Fail to parse json", ex);
        }
    }
}
