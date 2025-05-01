package dto.endpoint.data.values;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// @JsonTypeName(value = "boolLit")
public final class BoolValDTO extends ValueDTO implements PrimitiveValueDTO{
    public final boolean boolLit;

    @JsonCreator
    public BoolValDTO(@JsonProperty("boolLit") boolean boolLit) {
        this.boolLit = boolLit;
    }

    @Override
    public String toString() {
        return boolLit ? "true" : "false";
    }
}
