package dcr1.common.data.computation;

import dcr1.common.data.types.BooleanType;
import dcr1.common.data.types.Type;
import dcr1.common.data.values.BooleanVal;
import dcr1.common.data.values.Value;
import dcr1.common.Environment;

public interface BooleanExpression extends ComputationExpression {
    @Override
    BooleanVal eval(Environment<Value> env);
}
