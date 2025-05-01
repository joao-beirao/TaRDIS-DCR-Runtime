package dto.endpoint.data.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

public record RefTypeDTO(@JsonProperty(value = "refType", required = true) String refType)
        implements TypeDTO {

    @NotNull
    @Override
    public String toString() {
        return refType;
    }
}
