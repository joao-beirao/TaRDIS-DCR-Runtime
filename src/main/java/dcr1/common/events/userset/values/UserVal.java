package dcr1.common.events.userset.values;

import dcr1.common.Record;
import dcr1.common.data.computation.*;
import dcr1.common.data.computation.StringLiteral;
import dcr1.common.data.values.*;
import dcr1.common.events.userset.UserParams;
import dcr1.common.events.userset.expressions.UserExpr;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

// TODO [sanitize args]
// TODO remove Serializable - have a DTO on the DRCProtocol instead

public record UserVal(String role, UserParams<PrimitiveVal> params)
        implements UserSetVal, Serializable {

    public static UserVal of(String role, UserParams<PrimitiveVal> params) {
        return new UserVal(role, params);
    }

    public static UserVal of(String role, String id) {
        return new UserVal(role, UserParams.of(StringVal.of(id)));
    }

    public UserVal {
        Objects.requireNonNull(role);
        Objects.requireNonNull(params);// ok by construction
        if (!(params.getId() instanceof StringVal)) {
            // TODO BadState.. Internal Error instead - should be compiled this way
            throw new IllegalArgumentException(
                    "'id' field of UserVal value must be a String" + " field");
        }
    }

    public StringVal getId() {
        // cast safe by construction
        return (StringVal) params.getId();
    }

    // TODO revise - quick patch
    public RecordVal getParamsAsRecordVal() {
        Record.Builder<Value> builder = Record.builder();
        for (var param : params.params().fields()) {
            builder.addFieldWithParams(param.name(), param.value());
        }
        return RecordVal.of(builder.build());
    }



    public UserExpr toUserSetExpr() {
        var recordBuilder = new Record.Builder<ComputationExpression>();
        for (var param : params.params().fields()) {
            var expr = switch (param.value()) {
                case BooleanVal bool -> BooleanLiteral.of(bool);
                case IntegerVal integer -> IntegerLiteral.of(integer);
                case StringVal str -> StringLiteral.of(str);
            };
            recordBuilder.addFieldWithParams(param.name(), expr);
        }
        return UserExpr.of(role(), UserParams.of(recordBuilder.build()));
    }


    @NotNull
    @Override
    public String toString() {
        return String.format("%s(%s))", role, params);
    }
}
