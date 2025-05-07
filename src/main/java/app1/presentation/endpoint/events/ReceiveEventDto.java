package app1.presentation.endpoint.events;

import app1.presentation.endpoint.events.participants.UserSetExprDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Collections;
import java.util.List;

@JsonTypeName("receiveEvent")
public final class ReceiveEventDto
        extends EventDTO {
    // @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "initiators", required = true)
    public final List<UserSetExprDTO> initiators;


    @JsonCreator
    public ReceiveEventDto(@JsonProperty(value = "common", required = true) Common common,
             @JsonProperty(value = "initiators",
                    required = true) List<UserSetExprDTO> initiators) {
        super(common);
        this.initiators = initiators;
        // this.initiators = (initiators == null || initiators.isEmpty())
        //         ? Collections.emptyList()
        //         : Collections.unmodifiableList(initiators);
    }
    // @JsonCreator
    // ReceiveEventDto(@JsonProperty(value = "uid", required = true) String uid,
    //         @JsonProperty(value = "id", required = true) String id,
    //         @JsonProperty(value = "eventType", required = true) EventTypeDTO eventType,
    //         @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "dataType",
    //                 required = true) TypeDTO dataType,
    //         @JsonProperty(value = "marking", required = true) EventDTO.MarkingDTO marking,
    //         @JsonProperty(value = "instantiationConstraint") Optional<ComputationExprDTO>
    //         instantiationConstraint,
    //         @JsonProperty(value = "ifcConstraint") Optional<ComputationExprDTO> ifcConstraint,
    //         @JsonProperty(value = "initiators", required = true) UserSetExprDTO initiators) {
    //     super(uid, id, eventType, dataType, marking, instantiationConstraint, ifcConstraint);
    //     this.initiators = initiators;
    // }
}