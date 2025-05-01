package dto.endpoint.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dto.endpoint.data.computation.ComputationExprDTO;
import dto.endpoint.data.types.TypeDTO;

@JsonTypeName("receiveEvent")
public final class ReceiveEventDto
        extends EventDTO {

    @JsonProperty(value = "dataExpr")
    public final TypeDTO expr;

    @JsonCreator
    public ReceiveEventDto(@JsonProperty(value = "uid", required = true) String uid,
            @JsonProperty(value = "id", required = true) String id,
            @JsonProperty(value = "marking", required = true) MarkingDTO marking,
            @JsonProperty(value = "instantiationConstraint", required = true) ComputationExprDTO instantiationConstraint,
            @JsonProperty(value = "ifcConstraint", required = true) ComputationExprDTO ifcConstraint,
            @JsonProperty(value = "dataExpr", required = true) TypeDTO expr) {
        super(uid, id, marking, instantiationConstraint, ifcConstraint);
        this.expr = expr;
    }
}
