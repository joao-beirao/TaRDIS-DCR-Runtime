package dcr.ast.values;

import java.io.Serializable;

import dcr.ast.typing.Type;

public interface Value extends Serializable {
    String unparse();
    Type getType();
}
