package dcr1.common.events.userset.values;

import dcr1.common.Record;
import dcr1.common.data.computation.BoolLiteral;
import dcr1.common.data.computation.ComputationExpression;
import dcr1.common.data.computation.IntLiteral;
import dcr1.common.data.computation.StringLiteral;
import dcr1.common.data.values.*;
import dcr1.common.events.userset.RoleParams;
import dcr1.common.events.userset.expressions.RoleExpr;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

// TODO [sanitize args]
// TODO remove Serializable - have a DTO on the DRCProtocol instead

public record UserVal(String role, Record<Value> params)
        implements UserSetVal, Serializable {

    public static UserVal of(String role, Record<Value> params) {
        return new UserVal(role, params);
    }

    // deprecating...
    public static UserVal of(String role, String id) {
        return new UserVal(role, Record.ofEntries(Record.Field.of("id",
                StringVal.of(Objects.requireNonNull(id)))));
    }

    public UserVal {
        Objects.requireNonNull(role);
        Objects.requireNonNull(params);// ok by construction
        // deprecating...
        // if (!(params.getId() instanceof StringVal)) {
        //     // TODO BadState.. Internal Error instead - should be compiled this way
        //     throw new IllegalArgumentException(
        //             "'id' field of UserVal value must be a String" + " field");
        // }
    }

    // deprecating
    // public StringVal getId() {
    //     // cast safe by construction
    //     return (StringVal) params.getId();
    // }

    // TODO revise - quick patch
    public RecordVal getParamsAsRecordVal() {
        Record.Builder<Value> builder = Record.builder();
        for (var param : params.fields()) {
            builder.addFieldWithParams(param.name(), param.value());
        }
        return RecordVal.of(builder.build());
    }


    public RoleExpr toRoleExpr() {
        var recordBuilder = new Record.Builder<ComputationExpression>();
        for (var param : params.fields()) {
            var expr = switch (param.value()) {
                case BoolVal bool -> BoolLiteral.of(bool);
                case IntVal integer -> IntLiteral.of(integer);
                case StringVal str -> StringLiteral.of(str);
                default ->
                        throw new IllegalStateException("[TODO] Unexpected value" + param.value());
            };
            recordBuilder.addFieldWithParams(param.name(), expr);
        }
        return RoleExpr.of(role(), recordBuilder.build());
    }


    @NotNull
    @Override
    public String toString() {
        return String.format("%s(%s))", role, params);
    }
}
