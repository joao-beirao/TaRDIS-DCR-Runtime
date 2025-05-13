package dcr1.common.data.computation;

import dcr1.common.Environment;
import dcr1.common.data.values.BoolVal;
import dcr1.common.data.values.Value;

public class NegationExpr
        implements ComputationExpression {

    public final ComputationExpression expr;

    private NegationExpr(ComputationExpression expr) {this.expr = expr;}

    public static NegationExpr of(ComputationExpression expr) {
        return new NegationExpr(expr);
    }

    @Override
    public BoolVal eval(Environment<Value> env) {
        return BoolVal.of(
                !((BoolVal)expr.eval(env)).value());
    }

    @Override
    public String toString() {
        return String.format("!%s", expr);
    }

}
