package dcr1.common.data.computation;

import dcr1.common.Environment;
import dcr1.common.data.types.Type;
import dcr1.common.data.values.Value;

public record IfThenElseExpr<T extends Type>(
        BooleanExpression conditionExpr,
        ComputationExpression<? extends T> thenExpr,
        ComputationExpression<? extends T> elseExpr)
        implements ComputationExpression<T> {

    public IfThenElseExpr {
        // TODO validate args
    }

    public static <T extends Type> IfThenElseExpr<T> of(
            BooleanExpression conditionExpr,
            ComputationExpression<? extends T> thenExpr,
            ComputationExpression<? extends T> elseExpr) {
        return new IfThenElseExpr<>(conditionExpr, thenExpr, elseExpr);
    }

    public Value<? extends T> eval(Environment<Value<?>> env) {
        return conditionExpr.eval(env).value() ? thenExpr.eval(env) : elseExpr.eval(env);
    }

    @Override
    public String toString() {
        return String.format("%s ? %s : %s ", conditionExpr, thenExpr, elseExpr);
    }

    @Override
    public String unparse() {
        return toString();
    }
}
