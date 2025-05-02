package dto.endpoint.data.types;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record RecordTypeDTO(@JsonProperty(value = "recordTy",required = true) List<FieldDTO> recordType)
        implements TypeDTO {

    public record FieldDTO(@JsonProperty(value = "name", required = true) String fieldName,
                           @JsonProperty(value = "fieldType", required = true) TypeDTO fieldType) {

        @NotNull
        @Override
        public String toString() {
            return String.format("%s: %s", fieldName, fieldType);
        }
    }

    @NotNull
    @Override
    public String toString() {
        return recordType.stream()
                .map(FieldDTO::toString)
                .collect(Collectors.joining(", ", "{", "}"));
    }

}
