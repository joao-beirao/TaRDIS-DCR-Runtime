package dto.endpoint.data.values;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonTypeName("recordVal")
public record RecordValDTO(
        @JsonProperty(value = "fields", required = true) List<FieldDTO> recordVal)
        implements ValueDTO {

    public record FieldDTO(@JsonProperty(value = "name", required = true) String fieldName,
                           @JsonProperty(value = "value", required = true) ValueDTO fieldVal) {

        @NotNull
        @Override
        public String toString() {
            return String.format("%s: %s)", fieldName, fieldVal);
        }
    }

    @NotNull
    @Override
    public String toString() {
        var fields = recordVal.stream().map(FieldDTO::toString).collect(Collectors.joining(", "));
        return String.format("{%s}", fields);
    }

}
