package dcr1.common.data.computation;

import dcr1.common.Environment;
import dcr1.common.data.types.Type;
import dcr1.common.data.values.BooleanVal;
import dcr1.common.data.values.Value;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record LogicalOpExpr(BooleanExpression left, BooleanExpression right, Op op)
        implements BooleanExpression {
    public enum Op {
        AND("and"), OR("or");
        private final String stringVal;

        Op(String stringVal) {
            this.stringVal = stringVal;
        }

        @Override
        public String toString() {
            return stringVal;
        }
    }

    public static LogicalOpExpr and(BooleanExpression left, BooleanExpression right) {
        return new LogicalOpExpr(left, right, Op.AND);
    }
    public static LogicalOpExpr or(BooleanExpression left, BooleanExpression right) {
        return new LogicalOpExpr(left, right, Op.OR);
    }

    public LogicalOpExpr {
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        Objects.requireNonNull(op);
    }

    @Override
    public BooleanVal eval(Environment<Value> env) {
        var leftBool = left.eval(env).value();
        var rightBool = right.eval(env).value();
        return BooleanVal.of(
                switch (op) {
                    case AND -> leftBool && rightBool;
                    case OR -> leftBool || rightBool;
                }
        );
    }

    @NotNull
    @Override
    public String toString() {
        return String.format("%s %s %s", left, op, right);
    }

    @Override
    public String unparse() {
        return String.format("%s %s %s", left, op, right);
    }
}
