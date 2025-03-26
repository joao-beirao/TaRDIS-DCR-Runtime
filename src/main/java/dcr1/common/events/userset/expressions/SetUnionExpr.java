package dcr1.common.events.userset.expressions;


import dcr1.common.Environment;
import dcr1.common.data.values.Value;
import dcr1.common.events.userset.values.SetUnionVal;
import dcr1.common.events.userset.values.UserVal;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

// TODO [sanitize args]

public record SetUnionExpr(Collection<? extends UserSetExpression> userSetExprs)
        implements UserSetExpression {
    public SetUnionExpr(Collection<? extends UserSetExpression> userSetExprs) {
        Objects.requireNonNull(userSetExprs);
        if (userSetExprs.size() < 2) {
            throw new IllegalArgumentException(
                    "Requires at least two expressions: have " + userSetExprs.size());
        }
        if (userSetExprs.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("List argument contains null entries - not allowed");
        }
        this.userSetExprs = List.copyOf(userSetExprs);
    }

    public static SetUnionExpr of(Collection<? extends UserSetExpression> userSetExprs) {
        return new SetUnionExpr(userSetExprs);
    }

    @Override
    public SetUnionVal eval(Environment<Value<?>> valueEnv, Environment<UserVal> userEnv) {
        return SetUnionVal.of(
                userSetExprs.stream().map(expr -> expr.eval(valueEnv, userEnv)).toList());
    }
}
