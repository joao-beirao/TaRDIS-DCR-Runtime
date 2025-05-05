package app1.presentation.endpoint.events;

import app1.presentation.endpoint.events.participants.UserSetExprDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Collections;
import java.util.List;

@JsonTypeName("inputEvent")
public final class InputEventDTO
        extends EventDTO {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "receivers")
    public final List<UserSetExprDTO> receivers;

    @JsonCreator
    public InputEventDTO(@JsonProperty(value = "common", required = true) Common common,
            @JsonProperty(value = "receivers") List<UserSetExprDTO> receivers) {
        super(common);
        this.receivers = (receivers == null || receivers.isEmpty())
                ? Collections.emptyList()
                : Collections.unmodifiableList(receivers);
    }
}


// @JsonTypeName("inputEvent")
// public record InputEventDTO(
//         @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "uid", required =
//         true) String uid,
//         @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "id", required =
//         true) String id,
//         @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "eventType",
//         required = true)
//         EventTypeDTO eventType,
//         @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "marking", required =
//                 true) MarkingDTO marking,
//         @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "dataExpr", required =
//                 true) TypeDTO dataExpr,
//         @JsonProperty(value = "instantiationConstraint") Optional<ComputationExprDTO>
//         instantiationConstraint,
//         @JsonProperty(value = "ifcConstraint") Optional<ComputationExprDTO> ifcConstraint,
//         @JsonProperty(value = "receivers") List<UserSetExprDTO> receivers)
//         implements EventDTO {
//
//     @JsonCreator
//     public InputEventDTO(
//             @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "uid", required =
//                     true) String uid,
//             @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "id", required =
//                     true) String id,
//             @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "eventType",
//             required = true)
//             EventTypeDTO eventType,
//             @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "marking",
//                     required = true) EventDTO.MarkingDTO marking,
//             @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "dataExpr",
//                     required = true) TypeDTO dataExpr,
//             @JsonProperty(value = "instantiationConstraint") Optional<ComputationExprDTO>
//             instantiationConstraint,
//             @JsonProperty(value = "ifcConstraint") Optional<ComputationExprDTO> ifcConstraint,
//             @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "receivers")
//             List<UserSetExprDTO> receivers) {
//         this.uid = uid;
//         this.id = id;
//         this.marking = marking;
//         this.dataExpr = dataExpr;
//         this.instantiationConstraint = instantiationConstraint;
//         this.ifcConstraint = ifcConstraint;
//         this.receivers = (receivers == null || receivers.isEmpty())
//                 ? Collections.emptyList()
//                 : Collections.unmodifiableList(receivers);
//     }
//
//
//     @Override
//     public EventTypeDTO type() {
//         return null;
//     }
// }
