package dto.endpoint.data.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @JsonSubTypes.Type(PrimitiveTypeDTO.class),
        @JsonSubTypes.Type(RecordTypeDTO.class),
        @JsonSubTypes.Type(RefTypeDTO.class),
}
)
public sealed interface TypeDTO
        permits PrimitiveTypeDTO, RecordTypeDTO, RefTypeDTO {
}
