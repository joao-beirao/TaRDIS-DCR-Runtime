package dto.endpoint.data.values;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.List;
import java.util.stream.Collectors;

public final class RecordValDTO
        extends ValueDTO
        implements PropBasedValueDTO {

    @JsonUnwrapped
    @JsonProperty("recordVal")
    public final List<FieldDTO> recordVal;

    public static class FieldDTO {
        @JsonProperty("fieldName")
        public final String fieldName;
        @JsonProperty("fieldValue")
        public final ValueDTO fieldVal;

        @JsonCreator
        public FieldDTO(@JsonProperty("fieldName") String fieldName,
                @JsonProperty("fieldValue") ValueDTO fieldValue) {
            this.fieldName = fieldName;
            this.fieldVal = fieldValue;
        }

        @Override
        public String toString() {
            return String.format("%s: %s)", fieldName, fieldVal);
        }
    }

    @JsonCreator
    public RecordValDTO(@JsonProperty("recordVal") List<FieldDTO> recordVal) {
        this.recordVal = recordVal;
    }


    @Override
    public String toString() {
        var fields = recordVal.stream().map(FieldDTO::toString).collect(Collectors.joining(", "));
        return String.format("{%s}", fields);
    }

}
