package dto.endpoint.data.values;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// @JsonTypeName(value = "refval")
public final class RefValDTO extends ValueDTO implements PropBasedValueDTO {
    public final String refVal;

    @JsonCreator
    public RefValDTO(@JsonProperty("refVal") String refVal) {this.refVal = refVal;}

    @Override
    public String toString() {
        return refVal;
    }
}
