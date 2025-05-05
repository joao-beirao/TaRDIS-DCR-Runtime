package app1.presentation.endpoint.events;

import app1.presentation.endpoint.data.computation.ComputationExprDTO;
import app1.presentation.endpoint.data.types.EventTypeDTO;
import app1.presentation.endpoint.data.types.TypeDTO;
import app1.presentation.endpoint.data.values.StringValDTO;
import app1.presentation.endpoint.data.values.ValueDTO;
import com.fasterxml.jackson.annotation.*;

import java.util.Optional;

//
// @JsonIgnoreProperties(ignoreUnknown = true)
// @JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
// @JsonSubTypes({@JsonSubTypes.Type(ComputationEventDTO.class),
//         @JsonSubTypes.Type(InputEventDTO.class), @JsonSubTypes.Type(ReceiveEventDto.class)})
// public sealed interface EventDTO
//         permits ComputationEventDTO, InputEventDTO, ReceiveEventDto {
//
//     @JsonIgnoreProperties(ignoreUnknown = true)
//     @JsonInclude(JsonInclude.Include.NON_EMPTY)
//     public record MarkingDTO(
//             @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "isIncluded",
//                     required = true) boolean isIncluded,
//             @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "isPending",
//                     required = true) boolean isPending,
//             @JsonProperty("defaultValue") Optional<ValueDTO> value) {
//
//     }
//
//     public String uid();
//
//     public String id();
//
//     public EventTypeDTO type();
// }


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({@JsonSubTypes.Type(ComputationEventDTO.class),
        @JsonSubTypes.Type(InputEventDTO.class), @JsonSubTypes.Type(ReceiveEventDto.class)})
public sealed abstract class EventDTO
        permits ComputationEventDTO, InputEventDTO, ReceiveEventDto {


    // @JsonInclude(JsonInclude.Include.NON_EMPTY)
    // @JsonProperty(value = "uid", required = true)
    // public final String uid;
    // @JsonInclude(JsonInclude.Include.NON_EMPTY)
    // @JsonProperty(value = "id", required = true)
    // public final String id;
    // @JsonInclude(JsonInclude.Include.NON_EMPTY)
    // @JsonProperty(value = "eventType", required = true)
    // public final EventTypeDTO eventType;
    // @JsonInclude(JsonInclude.Include.NON_EMPTY)
    // @JsonProperty(value = "dataType", required = true)
    // public final TypeDTO dataType;
    // @JsonInclude(JsonInclude.Include.NON_EMPTY)
    // @JsonProperty(value = "marking", required = true)
    // public final EventDTO.MarkingDTO marking;
    // @JsonProperty(value = "instantiationConstraint")
    // public final Optional<ComputationExprDTO> instantiationConstraint;
    // @JsonProperty(value = "ifcConstraint")
    // public final Optional<ComputationExprDTO> ifcConstraint;
    //
    // @JsonCreator
    // public EventDTO(
    //         @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "uid", required =
    //                 true) String uid,
    //         @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "id", required =
    //                 true) String id,
    //         @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "eventType",
    //                 required = true) EventTypeDTO eventType,
    //         @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "dataType",
    //                 required = true) TypeDTO dataType,
    //         @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "marking",
    //                 required = true) MarkingDTO marking,
    //         @JsonProperty(value = "instantiationConstraint") Optional<ComputationExprDTO>
    //         instantiationConstraint,
    //         @JsonProperty(value = "ifcConstraint") Optional<ComputationExprDTO> ifcConstraint) {
    //     this.uid = uid;
    //     this.id = id;
    //     this.eventType = eventType;
    //     this.dataType = dataType;
    //     this.marking = marking;
    //     this.instantiationConstraint = instantiationConstraint;
    //     this.ifcConstraint = ifcConstraint;
    // }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)

    @JsonProperty(value = "common", required = true)
    public final Common common;


    @JsonCreator
    public EventDTO(
            @JsonInclude(JsonInclude.Include.NON_EMPTY)  @JsonProperty(value =
                    "common", required = true) Common common) {
        this.common = common;
    }

    // @JsonTypeName(value = "common")
    public record Common(
            @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "uid", required =
                    true) String uid,
            @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "id", required =
                    true) String id,
            @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "label",
                    required = true) String label,
            @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "dataType",
                    required = true) TypeDTO dataType,
            @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "marking",
                    required = true) MarkingDTO marking,
            @JsonInclude(JsonInclude.Include.NON_EMPTY)@JsonProperty(value = "instantiationConstraint") Optional<ComputationExprDTO> instantiationConstraint,
            @JsonInclude(JsonInclude.Include.NON_EMPTY)@JsonProperty(value = "ifcConstraint") Optional<ComputationExprDTO> ifcConstraint) {}


    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public record MarkingDTO(
            @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "isIncluded",
                    required = true) boolean isIncluded,
            @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "isPending",
                    required = true) boolean isPending,
            @JsonProperty("defaultValue") Optional<ValueDTO> value) {}
}