package dcr1.common.events.userset.expressions;

import dcr1.common.Environment;
import dcr1.common.Record;
import dcr1.common.data.computation.ComputationExpression;
import dcr1.common.data.computation.StringLiteral;
import dcr1.common.data.types.PrimitiveType;
import dcr1.common.data.values.PrimitiveVal;
import dcr1.common.data.values.StringVal;
import dcr1.common.data.values.Value;
import dcr1.common.events.userset.UserParams;
import dcr1.common.events.userset.values.UserSetVal;
import dcr1.common.events.userset.values.UserVal;

import java.util.Objects;

// TODO [validate args]
public final class UserExpr
        implements UserSetExpression {

    private static final String ID_FIELD_LABEL = "id";

    private final String role;
    private final UserParams<ComputationExpression> params;

    public static UserExpr of(String role, String id) {
        return new UserExpr(role, UserParams.of(StringLiteral.of(StringVal.of(id))));
    }

    public static UserExpr of(String role, ComputationExpression id) {
        return new UserExpr(role,
                UserParams.of(Record.ofEntries(Record.Field.of(ID_FIELD_LABEL, id))));
    }

    public static UserExpr of(String role,
            UserParams<ComputationExpression> params) {
        return new UserExpr(role, params);

    }

    public UserExpr(String role,
            UserParams<ComputationExpression> params) {
        // TODO [validate args] role, params not null (all else ensured by RoleParamsExpr by
        //  construction)
        this.role = Objects.requireNonNull(role);
        this.params = Objects.requireNonNull(params);
        // TODO enforce id expr as required
    }

    public ComputationExpression getId() {
        // ok by construction
        return (ComputationExpression) params.getId();
    }

    @Override
    public UserSetVal eval(Environment<Value> valueEnv, Environment<UserVal> userEnv) {
        var evalParams = Record.<PrimitiveVal>builder();
        // var evalParams = Record.<Value<? extends PrimitiveType>>builder();
        params.params()
                .stream()
                .map(field -> Record.Field.of(field.name(),
                        (PrimitiveVal)field.value().eval(valueEnv)))
                .forEach(evalParams::addField);
        return UserVal.of(role, UserParams.of(evalParams.build()));
    }
}
