package dcr1.common.data.computation;

import dcr1.common.data.types.Type;
import dcr1.common.data.values.Value;
import dcr1.common.Environment;

public interface ComputationExpression {

    @Override
    String toString();

    Value eval(Environment<Value> env);
}



