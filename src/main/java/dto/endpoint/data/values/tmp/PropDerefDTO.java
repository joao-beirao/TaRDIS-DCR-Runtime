package dto.endpoint.data.values.tmp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class PropDerefDTO {
    public final PropBasedValueDTO propBasedVal;
    public final String propName;

    @JsonCreator
    public PropDerefDTO(@JsonProperty("propBasedVal") PropBasedValueDTO propBasedVal,
            @JsonProperty("propName") String propName) {
        this.propBasedVal = propBasedVal;
        this.propName = propName;
    }

    @Override
    public String toString() {
        return String.format("%s.%s", propBasedVal.toString(), propName);
    }
}
