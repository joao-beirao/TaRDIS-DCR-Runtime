package dcr.common.events.userset.expressions;

import dcr.common.Environment;
import dcr.common.Record;
import dcr.common.data.computation.ComputationExpression;
import dcr.common.data.computation.StringLiteral;
import dcr.common.data.values.PrimitiveVal;
import dcr.common.data.values.Value;
import dcr.common.events.userset.RoleParams;
import dcr.common.events.userset.values.RoleVal;
import dcr.common.events.userset.values.UserSetVal;
import dcr.common.events.userset.values.UserVal;

import java.util.Objects;
import java.util.stream.Collectors;

// TODO [sanitize args]

public final class RoleExpr
        implements UserSetExpression {

    private final String role;
    private final Record<ComputationExpression> params;

    public RoleExpr(String role, Record<ComputationExpression> params) {
        this.role = Objects.requireNonNull(role);
        this.params = Objects.requireNonNull(params);
    }

    public static RoleExpr of(String role) {
        return new RoleExpr(role, Record.empty());
    }

    // TODO [deprecate]
    public static RoleExpr of(String role, String id) {
        return new RoleExpr(role, Record.ofEntries(Record.Field.of("id", StringLiteral.of(id))));
    }

    public static RoleExpr of(String role, Record<ComputationExpression> params) {
        return new RoleExpr(role, params);
    }

    // TODO [sanitize args]


    @Override
    public UserSetVal eval(Environment<Value> valueEnv, Environment<UserVal> userEnv) {
        var evalParams = Record.<PrimitiveVal>builder();
        params.fields()
                .stream()
                .map(field -> Record.Field.of(field.name(),
                        (PrimitiveVal) field.value().eval(valueEnv)))
                .forEach(evalParams::addField);
        return RoleVal.of(role, RoleParams.of(evalParams.build()));
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", role,
                params.stream().map(p -> String.format("%s=%s", p.name(), p.value())).collect(
                        Collectors.joining(", ")));
    }
}
