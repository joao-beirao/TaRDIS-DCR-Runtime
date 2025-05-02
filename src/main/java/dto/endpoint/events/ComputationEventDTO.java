package dto.endpoint.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dto.endpoint.data.computation.ComputationExprDTO;
import dto.endpoint.participants.UserSetExprDTO;
import org.apache.commons.lang3.NotImplementedException;

@JsonTypeName("computationEvent")
public final class ComputationEventDTO
        extends EventDTO {

    @JsonProperty(value = "dataExpr")
    public final ComputationExprDTO expr;

    @JsonCreator
    public ComputationEventDTO(@JsonProperty(value = "uid", required = true) String uid,
            @JsonProperty(value = "id", required = true) String id,
            @JsonProperty(value = "marking", required = true) MarkingDTO marking,
            @JsonProperty(value = "remoteParticipants") UserSetExprDTO remoteParticipants,
            @JsonProperty(value = "instantiationConstraint", required = true) ComputationExprDTO instantiationConstraint,
            @JsonProperty(value = "ifcConstraint", required = true) ComputationExprDTO ifcConstraint,
            @JsonProperty(value = "dataExpr", required = true) ComputationExprDTO expr) {
        super(uid, id, marking, remoteParticipants, instantiationConstraint, ifcConstraint);
        this.expr = expr;
    }

    // TODO (complete)
    @Override
    public String toString() {
        return String.format("%s (%s) [%s]", uid, id, expr);
    }
}
