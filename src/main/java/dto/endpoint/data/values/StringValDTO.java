package dto.endpoint.data.values;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.jetbrains.annotations.NotNull;

@JsonTypeName(value = "stringLit")
public record StringValDTO(@JsonProperty(value = "value", required = true) String stringLit)
        implements ValueDTO {

    @NotNull
    @Override
    public String toString() {
        return String.format("\"%s\"", stringLit);
    }
}
