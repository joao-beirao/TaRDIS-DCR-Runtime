package dcr1.common.data.computation;

import dcr1.common.Environment;
import dcr1.common.data.values.PropBasedVal;
import dcr1.common.data.values.Value;


public sealed interface PropBasedExpr extends ComputationExpression
        permits EventIdExpr, RecordExpr {
    @Override
    PropBasedVal eval(Environment<Value> env);
}
