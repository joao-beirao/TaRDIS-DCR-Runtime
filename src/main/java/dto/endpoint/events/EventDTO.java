package dto.endpoint.events;

import com.fasterxml.jackson.annotation.*;
import dto.endpoint.data.computation.ComputationExprDTO;
import dto.endpoint.data.values.ValueDTO;
import dto.endpoint.participants.UserSetExprDTO;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
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
    @JsonProperty(value = "remoteParticipants")
    public final UserSetExprDTO remoteParticipants;
    @JsonProperty(value = "instantiationConstraint")
    public final ComputationExprDTO instantiationConstraint;
    @JsonProperty(value = "ifcConstraint")
    public final ComputationExprDTO ifcConstraint;

    @JsonCreator
    public EventDTO(@JsonProperty(value = "uid", required = true) String uid,
            @JsonProperty(value = "id", required = true) String id,
            @JsonProperty(value = "marking", required = true) MarkingDTO marking,
            @JsonProperty(value = "remoteParticipants") UserSetExprDTO remoteParticipants,
            @JsonProperty(value = "instantiationConstraint", required = true) ComputationExprDTO instantiationConstraint,
            @JsonProperty(value = "ifcConstraint", required = true) ComputationExprDTO ifcConstraint) {
        this.uid = uid;
        this.id = id;
        this.marking = marking;
        this.remoteParticipants = remoteParticipants;
        this.instantiationConstraint = instantiationConstraint;
        this.ifcConstraint = ifcConstraint;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public record MarkingDTO(
            @JsonProperty(value = "isIncluded", required = true) boolean isIncluded,
            @JsonProperty(value = "isPending", required = true) boolean isPending,
            @JsonProperty("value") Optional<ValueDTO> value) {

        @NotNull
        @Override
        public String toString() {
            return String.format("(%b, %b, %s)", isIncluded, isPending, value.isPresent() ?
                    value.get().toString() : "<undefined>");
        }
    }

    public Optional<UserSetExprDTO> remoteParticipants() {
        return Optional.ofNullable(remoteParticipants);
    }
}
