package dcr1.common.data.values;

import dcr1.common.data.types.DereferableType;
import dcr1.common.data.types.Type;

import java.io.Serial;

public final class DereferableVal<T extends Type> implements Value<DereferableType<T>> {
    @Serial
    private static final long serialVersionUID = 2223282468128265501L;



    @Override
    public String unparse() {
        return "";
    }

    @Override
    public DereferableType<T> type() {
        return null;
    }
}
