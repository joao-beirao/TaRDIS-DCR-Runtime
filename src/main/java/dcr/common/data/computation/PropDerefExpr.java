package dcr.common.data.computation;

import dcr.common.Environment;
import dcr.common.data.values.PropBasedVal;
import dcr.common.data.values.Value;

public final class PropDerefExpr
        implements ComputationExpression {
    private final ComputationExpression propBasedExpr;
    private final String propName;

    public static PropDerefExpr of(ComputationExpression propBasedExpr, String propName) {
        return new PropDerefExpr(propBasedExpr, propName);
    }

    private PropDerefExpr(ComputationExpression propBasedExpr, String propName) {
        this.propBasedExpr = propBasedExpr;
        this.propName = propName;
    }

    @Override
    public Value eval(Environment<Value> env) {
        return ((PropBasedVal) (propBasedExpr.eval(env))).fetchProp(propName);
    }


    @Override
    public String toString() {
        return String.format("%s.%s", propBasedExpr, propName);
    }

}
