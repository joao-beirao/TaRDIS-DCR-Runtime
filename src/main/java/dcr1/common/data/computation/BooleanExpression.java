package dcr1.common.data.computation;

import dcr1.common.data.values.BoolVal;
import dcr1.common.data.values.Value;
import dcr1.common.Environment;

public interface BooleanExpression extends ComputationExpression {
    @Override
    BoolVal eval(Environment<Value> env);
}
