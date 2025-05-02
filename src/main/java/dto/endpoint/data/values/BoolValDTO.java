package dto.endpoint.data.values;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.jetbrains.annotations.NotNull;

@JsonTypeName(value = "boolLit")
public record BoolValDTO(@JsonProperty(value="value",required = true) boolean boolLit)
        implements ValueDTO {

    @NotNull
    @Override
    public String toString() {
        return boolLit ? "true" : "false";
    }
}
