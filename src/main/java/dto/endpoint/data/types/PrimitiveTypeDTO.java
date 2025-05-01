package dto.endpoint.data.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

public record PrimitiveTypeDTO(
        @JsonUnwrapped @JsonProperty(value = "primitiveTy", required = true) PrimitiveType primitiveType)
        implements TypeDTO {

    public enum PrimitiveType {

        BOOL("bool"), INT("int"), STRING("string");

        @JsonProperty(value = "primitiveTy")
        public final String value;

        @JsonCreator
        PrimitiveType(@JsonProperty(value = "primitiveTy", required = true) String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {return value;}

        @NotNull
        @Override
        public String toString() {return getValue();}
    }

    @NotNull
    @Override
    public String toString() {return primitiveType.toString();}
}
