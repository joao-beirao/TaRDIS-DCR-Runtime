package dto.endpoint.data.values;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// @JsonTypeName(value = "stringLit")
public final class StringValDTO
        extends ValueDTO implements PrimitiveValueDTO {

    public final String stringLit;

    @JsonCreator
    public StringValDTO(@JsonProperty("stringLit") String stringLit) {
        this.stringLit = stringLit;
    }

    @Override
    public String toString() {
        return String.format("\"%s\"", stringLit);
    }
}
