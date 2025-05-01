package dto.endpoint.data.values;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @JsonSubTypes.Type(BoolValDTO.class),
        @JsonSubTypes.Type(IntValDTO.class),
        @JsonSubTypes.Type(StringValDTO.class),
        @JsonSubTypes.Type(RecordValDTO.class),
        @JsonSubTypes.Type(RefValDTO.class)
}
)
public sealed abstract class ValueDTO
        permits RecordValDTO, BoolValDTO, IntValDTO, RefValDTO, StringValDTO {
}
