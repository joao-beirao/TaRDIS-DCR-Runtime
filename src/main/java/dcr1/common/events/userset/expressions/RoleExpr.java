package dcr1.common.events.userset.expressions;

import dcr1.common.Environment;
import dcr1.common.Record;
import dcr1.common.data.computation.ComputationExpression;
import dcr1.common.data.types.PrimitiveType;
import dcr1.common.data.values.Value;
import dcr1.common.events.userset.RoleParams;
import dcr1.common.events.userset.values.RoleVal;
import dcr1.common.events.userset.values.UserSetVal;
import dcr1.common.events.userset.values.UserVal;

import java.util.Objects;

// TODO [sanitize args]

public final class RoleExpr
        implements UserSetExpression {

    private final String role;
    private final RoleParams<ComputationExpression<? extends PrimitiveType>> params;

    public RoleExpr(String role, RoleParams<ComputationExpression<? extends PrimitiveType>> params) {
        this.role = Objects.requireNonNull(role);
        this.params = Objects.requireNonNull(params);
    }

    public static RoleExpr of(String role) {
        return new RoleExpr(role, RoleParams.empty());
    }

    public static RoleExpr of(String role, RoleParams<ComputationExpression<? extends PrimitiveType>> params) {
        return new RoleExpr(role, params);
    }

    // TODO [sanitize args]


    @Override
    public UserSetVal eval(Environment<Value<?>> valueEnv, Environment<UserVal> userEnv) {
        var evalParams = Record.<Value<? extends PrimitiveType>>builder();
        params.params().fields()
                .stream()
                .map(field -> Record.Field.of(field.name(), field.value().eval(valueEnv)))
                .forEach(evalParams::addField);
        return RoleVal.of(role, RoleParams.of(evalParams.build()));
    }
}
