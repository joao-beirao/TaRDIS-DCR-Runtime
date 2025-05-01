package dto.endpoint.data.values;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// @JsonTypeName(value = "stringLit")
public final class IntValDTO extends ValueDTO implements PrimitiveValueDTO {
    public final int intLit;

    @JsonCreator
    public IntValDTO(@JsonProperty("intLit") int intLit) {
        this.intLit = intLit;
    }

    @Override
    public String toString() {
        return Integer.toString(intLit);
    }
}
