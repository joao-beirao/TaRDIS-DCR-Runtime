package dto.endpoint.data.computation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.jetbrains.annotations.NotNull;

@JsonTypeName("refExpr")
public record RefExprDTO(@JsonProperty(value = "value", required = true) String value)
        implements PropBasedExprDTO {
    @NotNull
    @Override
    public String toString() {
        return value();
    }
}
