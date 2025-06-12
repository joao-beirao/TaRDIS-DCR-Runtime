package rest.response;

import dcr.common.data.types.*;
import dcr.common.data.values.*;
import dcr.common.events.Event;
import dcr.runtime.elements.events.ComputationEventInstance;
import dcr.runtime.elements.events.EventInstance;
import dcr.runtime.elements.events.InputEventInstance;
import deprecated.dcr.ast.values.UnitVal;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class EventDTO {

    private final String id;
    private final String action;
    private KindDTO kind;
//    private String initiator;
//    private String receivers;
    private MarkingDTO marking;
    private TypeDTO type;

//    public EventDTO( Event e) {
//        this.id = e.remoteID();
//        this.action = "unknown";
//        this.kind = switch ()
//
//    }

    public EventDTO(ComputationEventInstance event) {
        this.action = "computation";
        this.id = event.remoteID();

    }
    public EventDTO(InputEventInstance event) {
        this.id = event.remoteID();
        this.action = "input";
        this.kind= KindDTO.INPUT;
        this.marking = new MarkingDTO(event.marking());
        this.type = new TypeDTO(event.valueType());

    }

    public KindDTO getKind() {
        return kind;
    }

    public String getAction() {
        return action;
    }

    public String getId() {
        return id;
    }

    public MarkingDTO getMarking() {
        return marking;
    }

    public TypeDTO getType() {
        return type;
    }
}

class MarkingDTO {
    private boolean executed;
    private boolean pending;
    private boolean included;
    private ValueDTO value;

    public MarkingDTO(Event.Marking m) {
        this.executed = m.hasExecuted();
        this.pending = m.isPending();
        this.included = m.isIncluded();
        this.value = ValueDTO.toValueDTO(m.value());
    }
    public ValueDTO getValue() {
        return value;
    }

    public boolean isExecuted() {
        return executed;
    }

    public boolean isPending() {
        return pending;
    }

    public boolean isIncluded() {
        return included;
    }
}

class ValueDTO {
    private final String type;

    ValueDTO(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static ValueDTO toValueDTO(Value v){
        return switch (v) {
            case BoolVal b -> new BooleanDTO(b);
            case IntVal i -> new IntDTO(i);
            case StringVal s-> new StringDTO(s.value());
            case RecordVal r -> new RecordDTO(r);
            case EventVal event -> new StringDTO(event.value().toString());
            default -> new UnitDTO();
        };
    }
}

class BooleanDTO extends ValueDTO {
    private boolean value;

    BooleanDTO(BoolVal v) {
        super("Boolean");
        this.value = v.value();
    }
    public boolean getValue() {
        return value;
    }
}

class StringDTO extends ValueDTO {
    private String value;

    StringDTO(String v) {
        super("String");
        this.value = v;
    }
    public String getValue() {
        return value;
    }
}


class IntDTO extends ValueDTO {
    private int value;

    IntDTO(IntVal v) {
        super("Integer");
        this.value = v.value();
    }
    public int getValue() {
        return value;
    }
}


class RecordDTO extends ValueDTO {
    private final Map<String,ValueDTO> value;

    RecordDTO(RecordVal v) {
        super("Record");
        HashMap<String,ValueDTO> h = new HashMap<String,ValueDTO>();
       v.fields().stream()
                .forEach((e) -> h.put(e.name(),toValueDTO(e.value()) ));
       this.value= h;
    }
    public Map<String, ValueDTO> getValue() {
        return value;
    }
}

class UnitDTO extends ValueDTO {
    UnitDTO() {
        super("Unit");
    }
}


class TypeDTO {
    private String type;
    TypeDTO(Type t) {
        this.type = switch (t) {
            case BooleanType b -> "Boolean";
            case IntegerType i -> "Integer";
            case RecordType r -> "Record";
            case EventType eventType -> "EventTy";
            case StringType stringType -> "String";
            case VoidType voidType -> "Unit";
        };
    }
    public String getType() {
        return type;
    }
}
//class UnitTypeDTO extends TypeDTO {
//
//    UnitTypeDTO() {
//        super("UnitType");
//    }
//}


enum KindDTO {
    COMPUTATION("computation"),
    INPUT("input");

    private final String value;
    KindDTO( String v) {
        value = v;
    }
    public String getValue() {
        return value;
    }
}


