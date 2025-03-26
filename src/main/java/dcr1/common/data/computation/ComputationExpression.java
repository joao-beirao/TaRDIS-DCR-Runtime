package dcr1.common.data.computation;

import dcr1.common.data.types.Type;
import dcr1.common.data.values.Value;
import dcr1.common.Environment;

public interface ComputationExpression<T extends Type> {

    @Override
    String toString();

    Value<? extends T> eval(Environment<Value<? extends Type>> env);

    public String unparse();
}



