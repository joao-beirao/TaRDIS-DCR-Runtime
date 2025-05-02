package dto.endpoint.data.values.tmp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dto.endpoint.data.values.ValueDTO;

// @JsonTypeName(value = "refval")
public final class RefValDTO
        implements PropBasedValueDTO {
    public final String refVal;

    @JsonCreator
    public RefValDTO(@JsonProperty("refVal") String refVal) {this.refVal = refVal;}

    @Override
    public String toString() {
        return refVal;
    }
}
