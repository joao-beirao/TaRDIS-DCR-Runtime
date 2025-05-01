package dto.endpoint.events;

import com.fasterxml.jackson.annotation.*;
import dto.endpoint.data.computation.ComputationExprDTO;
import dto.endpoint.data.values.ValueDTO;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({@JsonSubTypes.Type(ComputationEventDTO.class),
        @JsonSubTypes.Type(InputEventDTO.class), @JsonSubTypes.Type(ReceiveEventDto.class)})
public sealed abstract class EventDTO
        permits ComputationEventDTO, InputEventDTO, ReceiveEventDto {
    @JsonProperty(value = "uid")
    public final String uid;
    @JsonProperty(value = "id")
    public final String id;
    @JsonProperty(value = "marking")
    public final MarkingDTO marking;
    @JsonProperty(value = "instantiationConstraint")
    public final ComputationExprDTO instantiationConstraint;
    @JsonProperty(value = "ifcConstraint")
    public final ComputationExprDTO ifcConstraint;

    @JsonCreator
    public EventDTO(@JsonProperty(value = "uid", required = true) String uid,
            @JsonProperty(value = "id", required = true) String id,
            @JsonProperty(value = "marking", required = true) MarkingDTO marking,
            @JsonProperty(value = "instantiationConstraint", required = true) ComputationExprDTO instantiationConstraint,
            @JsonProperty(value = "ifcConstraint", required = true) ComputationExprDTO ifcConstraint) {
        this.uid = uid;
        this.id = id;
        this.marking = marking;
        this.instantiationConstraint = instantiationConstraint;
        this.ifcConstraint = ifcConstraint;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class MarkingDTO {
        public final boolean isIncluded;
        public final boolean isPending;
        public final ValueDTO value;

        public MarkingDTO(@JsonProperty("isIncluded") boolean isIncluded,
                @JsonProperty("isPending") boolean isPending,
                @JsonProperty("value") ValueDTO value) {
            this.isIncluded = isIncluded;
            this.isPending = isPending;
            this.value = value;
        }

        @JsonCreator
        public MarkingDTO(@JsonProperty("isIncluded") boolean isIncluded,
                @JsonProperty("isPending") boolean isPending) {
            this.isIncluded = isIncluded;
            this.isPending = isPending;
            this.value = null;
        }

        @Override
        public String toString() {
            return String.format("(%b, %b, %s)", isIncluded, isPending, value);
        }
    }
}
