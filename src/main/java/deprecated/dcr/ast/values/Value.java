package deprecated.dcr.ast.values;

import java.io.Serializable;

import deprecated.dcr.ast.typing.Type;

public interface Value extends Serializable {
    String unparse();
    Type getType();
}
