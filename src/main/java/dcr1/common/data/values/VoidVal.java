package dcr1.common.data.values;

import dcr1.common.data.types.Type;
import dcr1.common.data.types.VoidType;

import java.io.Serial;

public final class VoidVal
        implements Value {
    @Serial
    private static final long serialVersionUID = -5587609113242528555L;

    @Override
    public String unparse() {
        return "<Void>";
    }

    @Override
    public Type type() {
        return VoidType.singleton();
    }
}
