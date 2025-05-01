package dto.endpoint.data.computation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

@JsonTypeName("recordExpr")
public record RecordExprDTO( @JsonUnwrapped  @JsonProperty(value = "fields") List<FieldDTO> recordExpr)
        implements ComputationExprDTO {



    public record FieldDTO(@JsonProperty(value = "fieldName", required = true) String fieldName,
                           @JsonProperty(value = "fieldExpr", required = true) ComputationExprDTO fieldExpr) {

        @NotNull
        @Override
        public String toString() {
            return String.format("%s: %s", fieldName, fieldExpr.toString());
        }
    }

    @NotNull
    @Override
    public String toString() {
        return recordExpr.stream()
                .map(FieldDTO::toString)
                .collect(Collectors.joining(", ", "{", "}"));
    }

}
