package dto.endpoint.data.values.tmp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dto.endpoint.data.values.RecordValDTO;

// @JsonIgnoreProperties(ignoreUnknown = true)
// @JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
// @JsonSubTypes({
//         @JsonSubTypes.Type(RefValDTO.class),
//         @JsonSubTypes.Type(RecordValDTO.class)
// }
// )
public interface PropBasedValueDTO {
}
