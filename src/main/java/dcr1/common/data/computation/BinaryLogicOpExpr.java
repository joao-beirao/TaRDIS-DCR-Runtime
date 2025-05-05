package dcr1.common.data.computation;

import dcr1.common.Environment;
import dcr1.common.data.values.BoolVal;
import dcr1.common.data.values.Value;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record BinaryLogicOpExpr(BooleanExpression left, BooleanExpression right, Op op)
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

    public static BinaryLogicOpExpr and(BooleanExpression left, BooleanExpression right) {
        return new BinaryLogicOpExpr(left, right, Op.AND);
    }
    public static BinaryLogicOpExpr or(BooleanExpression left, BooleanExpression right) {
        return new BinaryLogicOpExpr(left, right, Op.OR);
    }

    public BinaryLogicOpExpr {
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        Objects.requireNonNull(op);
    }

    @Override
    public BoolVal eval(Environment<Value> env) {
        var leftBool = left.eval(env).value();
        var rightBool = right.eval(env).value();
        return BoolVal.of(
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
