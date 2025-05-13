package dcr1.common.data.computation;

import dcr1.common.Environment;
import dcr1.common.data.values.PropBasedVal;
import dcr1.common.data.values.Value;
import org.apache.commons.lang3.NotImplementedException;

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
