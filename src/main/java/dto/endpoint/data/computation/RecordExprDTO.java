package dto.endpoint.data.computation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

@JsonTypeName("record")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record RecordExprDTO(
        @JsonProperty(value = "fields", required = true) List<FieldDTO> recordExpr)
        implements PropBasedExprDTO {

    public record FieldDTO(@JsonProperty(value = "name", required = true) String name,
                           @JsonProperty(value = "value", required = true) ComputationExprDTO expr) {

        @NotNull
        @Override
        public String toString() {
            return String.format("%s = %s", name, expr.toString());
        }
    }

    @NotNull
    @Override
    public String toString() {
        return recordExpr.stream()
                .map(FieldDTO::toString)
                .collect(Collectors.joining("; ", "{", "}"));
    }

}
