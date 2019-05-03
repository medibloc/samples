package org.medibloc.insurance_java_ko.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.protobuf.util.JsonFormat;
import org.medibloc.phr.ClaimDataV1.Claim;

import java.io.IOException;

public class ClaimSerializer extends JsonSerializer<Claim> {
    @Override
    public void serialize(Claim claim, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeRawValue(JsonFormat.printer().omittingInsignificantWhitespace().print(claim));
    }
}
