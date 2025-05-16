package dcr.common.data.computation;

import dcr.common.data.values.Value;
import dcr.common.Environment;

public interface ComputationExpression {

    @Override
    String toString();

    Value eval(Environment<Value> env);
}



