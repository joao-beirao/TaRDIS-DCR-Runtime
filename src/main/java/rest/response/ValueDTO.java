package rest.response;

import dcr.common.data.values.*;

import java.util.HashMap;
import java.util.Map;

public class ValueDTO {
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
