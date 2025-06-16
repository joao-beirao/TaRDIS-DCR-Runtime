package rest.response;

import dcr.common.Record;
import dcr.common.data.types.*;
import dcr.common.data.values.*;
import dcr.common.events.Event;
import dcr.common.events.userset.values.UserVal;
import dcr.runtime.elements.events.ComputationEventInstance;
import dcr.runtime.elements.events.EventInstance;
import dcr.runtime.elements.events.InputEventInstance;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;

import java.util.Map;
import java.util.stream.Collectors;

public final class Mappers {

    public static EndpointDTO fromEndpoint(UserVal user, List<EventInstance> events) {
        return new EndpointDTO(fromUserVal(user), events.stream().map(Mappers::toEventDTO)
                .toList());
    }

    public static UserDTO fromUserVal(UserVal user) {
        return new UserDTO(user.role(), user.params().fields().stream()
                .collect(Collectors.toMap(Record.Field::name, f -> fromValue(f.value()))));
    }

    public static TypeDTO fromType(Type type) {
        return switch (type) {
            case VoidType ignored -> new UnitTypeDTO();
            case BooleanType ignored -> new BooleanTypeDTO();
            case IntegerType ignored -> new IntegerTypeDTO();
            case StringType ignored -> new StringTypeDTO();
            case RecordType ty -> new RecordTypeDTO(ty.fields().stream()
                    .collect(Collectors.toMap(Record.Field::name, f -> fromType(f.value()))));
            default -> throw new NotImplementedException("Unsupported type: " + type);
        };
    }

    public static Value toValue(ValueDTO dto) {
        return switch (dto) {
            case UnitDTO ignored -> VoidVal.instance();
            case BooleanDTO v -> BoolVal.of(v.value());
            case IntDTO v -> IntVal.of(v.value());
            case StringDTO v -> StringVal.of(v.value());
            case RecordDTO v -> RecordVal.of(Record.ofEntries(v.value().entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> toValue(e.getValue())))));
        };
    }

    public static ValueDTO fromValue(Value v) {
        return switch (v) {
            case BoolVal val -> new BooleanDTO(val.value());
            case IntVal val -> new IntDTO(val.value());
            case StringVal val -> new StringDTO(val.value());
            case UndefinedVal<?> ignored -> new UnitDTO("");
            case VoidVal ignored -> new UnitDTO("");
            case RecordVal val -> new RecordDTO(val.fields().stream()
                    .collect(Collectors.toMap(Record.Field::name, f -> fromValue(f.value()))));
            case EventVal ignored -> throw new NotImplementedException("EventVal not implemented");
        };
    }

    public static EventDTO toEventDTO(EventInstance e) {
        var id = e.remoteID();
        var label = e.label();
        var typeExpr = fromType(e.baseElement().valueType());
        var marking = fromMarking(e.marking());
        return switch (e) {
            case ComputationEventInstance e1 ->
                    EventDTO.newComputationEventDTO(id, label, typeExpr, e1.receivers()
                            .isPresent() ? KindDTO.COMPUTATION_SEND : KindDTO.COMPUTATION, marking);
            case InputEventInstance e2 ->
                    EventDTO.newInputEventDTO(id, label, typeExpr, e2.receivers()
                            .isPresent() ? KindDTO.INPUT_SEND : KindDTO.INPUT, marking);
            default -> throw new IllegalStateException("Unexpected value: " + e);
        };
    }

    static MarkingDTO fromMarking(Event.Marking marking) {
        return new MarkingDTO(marking.hasExecuted(), marking.isPending(), marking.isIncluded(), fromValue(marking.value()));
    }
}
