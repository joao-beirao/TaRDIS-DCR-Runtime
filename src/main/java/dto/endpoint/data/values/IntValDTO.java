package dto.endpoint.data.values;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.jetbrains.annotations.NotNull;

@JsonTypeName(value = "intLit")
public record IntValDTO(@JsonProperty(value="value", required = true) int intLit)
        implements ValueDTO {

    @NotNull
    @Override
    public String toString() {
        return Integer.toString(intLit);
    }
}
