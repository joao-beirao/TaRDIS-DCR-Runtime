package dcr1.common.data.values;

import java.util.Optional;

public sealed interface PropBasedVal
        extends Value
        permits EventVal, RecordVal {

    Value fetchProp(String propName);
}
