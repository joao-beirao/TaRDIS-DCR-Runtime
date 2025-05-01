package dcr1.common.data.computation;

import dcr1.common.Environment;
import dcr1.common.data.values.BooleanVal;
import dcr1.common.data.values.Value;

public class NegationExpr
        implements BooleanExpression {

    public final BooleanExpression expr;

    private NegationExpr(BooleanExpression expr) {this.expr = expr;}

    public static NegationExpr of(BooleanExpression expr) {
        return new NegationExpr(expr);
    }

    @Override
    public BooleanVal eval(Environment<Value> env) {
        return BooleanVal.of(!expr.eval(env).value());
    }

    @Override
    public String toString() {
        return String.format("!%s", expr);
    }

    @Override
    public String unparse() {
        return toString();
    }
}
