package dto.endpoint.data.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.jetbrains.annotations.NotNull;

@JsonTypeName(value = "eventType")
public record EventRefTypeDTO(@JsonProperty(value = "label", required = true) String refType)
        implements RefTypeDTO {

    @NotNull
    @Override
    public String toString() {
        return refType;
    }
}
