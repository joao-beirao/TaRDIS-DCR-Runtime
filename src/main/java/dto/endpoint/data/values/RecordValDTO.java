package dto.endpoint.data.values;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public final class RecordValDTO
        extends ValueDTO
        implements PropBasedValueDTO {

    @JsonUnwrapped
    @JsonProperty(value = "recordVal", required = true)
    public final List<FieldDTO> recordVal;

    public static class FieldDTO {
        @JsonProperty(value = "name", required = true)
        public final String fieldName;
        @JsonProperty(value = "fieldValue", required = true)
        public final ValueDTO fieldVal;

        @JsonCreator
        public FieldDTO(@JsonProperty(value = "name", required = true) String fieldName,
                @JsonProperty(value = "fieldValue", required = true) ValueDTO fieldValue) {
            this.fieldName = fieldName;
            this.fieldVal = fieldValue;
        }

        @Override
        public String toString() {
            return String.format("%s: %s)", fieldName, fieldVal);
        }
    }

    @JsonCreator
    public RecordValDTO(@JsonProperty(value = "recordVal", required = true) List<FieldDTO> recordVal) {
        this.recordVal = recordVal;
    }


    @Override
    public String toString() {
        var fields = recordVal.stream().map(FieldDTO::toString).collect(Collectors.joining(", "));
        return String.format("{%s}", fields);
    }

}
