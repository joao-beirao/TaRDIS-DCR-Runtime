package dto.endpoint.data.computation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.jetbrains.annotations.NotNull;

@JsonTypeName("stringLit")
public record StringLiteralDTO(@JsonProperty(value = "value", required = true) String value)
        implements ComputationExprDTO {

    @NotNull
    @Override
    public String toString() {return value;}
}
