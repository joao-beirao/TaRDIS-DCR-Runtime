package dto.endpoint.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dto.endpoint.data.computation.*;

// @JsonIgnoreProperties(ignoreUnknown = true)
// @JsonInclude(JsonInclude.Include.NON_NULL)
// @JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
// @JsonSubTypes({@JsonSubTypes.Type(BoolLiteralDTO.class), @JsonSubTypes.Type(IntLiteralDTO.class),
//         @JsonSubTypes.Type(StringLiteralDTO.class),
//         @JsonSubTypes.Type(RecordExprDTO.class)})
public interface DataExprDTO {

}
