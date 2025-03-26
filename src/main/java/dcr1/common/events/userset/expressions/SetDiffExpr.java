package dcr1.common.events.userset.expressions;

import dcr1.common.Environment;
import dcr1.common.data.values.Value;
import dcr1.common.events.userset.values.SetDiffVal;
import dcr1.common.events.userset.values.UserSetVal;
import dcr1.common.events.userset.values.UserVal;

import java.util.Objects;

// TODO [sanitize args]

public record SetDiffExpr(UserSetExpression positiveSet, UserSetExpression negativeSet)
        implements UserSetExpression {

    public SetDiffExpr {
        Objects.requireNonNull(positiveSet);
        Objects.requireNonNull(negativeSet);
    }

    public static SetDiffExpr of(UserSetExpression positiveSet, UserSetExpression negativeSet) {
        return new SetDiffExpr(positiveSet, negativeSet);
    }

    @Override
    public UserSetVal eval(Environment<Value<?>> valueEnv, Environment<UserVal> userEnv) {
        return SetDiffVal.of(positiveSet.eval(valueEnv, userEnv),
                negativeSet.eval(valueEnv, userEnv));
    }
}
