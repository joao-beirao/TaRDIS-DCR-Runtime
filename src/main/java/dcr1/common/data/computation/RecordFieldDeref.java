package dcr1.common.data.computation;

import dcr1.common.Environment;
import dcr1.common.Record;
import dcr1.common.data.values.IntVal;
import dcr1.common.data.values.RecordVal;
import dcr1.common.data.values.Value;

// TODO [sanitize args]

public class RecordFieldDeref
        implements ComputationExpression {

    private final ComputationExpression recordExpr;
    private final String fieldName;

    public static RecordFieldDeref of(ComputationExpression recordExpr,
            String fieldName) {
        return new RecordFieldDeref(recordExpr, fieldName);
    }

    public RecordFieldDeref(ComputationExpression recordExpr,
            String fieldName) {
        this.recordExpr = recordExpr;
        this.fieldName = fieldName;
    }

    // TODO [revisit exceptions] move core part of error messages to static
    @Override
    public Value eval(Environment<Value> env) {
        if (recordExpr.eval(env) instanceof RecordVal recordValue) {
            recordValue.value()
                    .get(fieldName)
                    .orElseThrow(() -> new IllegalStateException(
                            "Internal Error: attempt to access unknown field in record " +
                                    "value: "));
            // return fieldValue;
        }
        // FIXME 'recordExpr.eval(env)' shouldn't be evaluating twice - pull up var
        // there are currently no other implementors of Value<RecordType> aside from RecordVal
        throw new IllegalStateException("Internal Error: attempt to dereference " +
                "field of a value that is not of Record type: " + recordExpr.eval(env));
    }

    @Override
    public String toString() {
        return recordExpr.toString() + "." + fieldName;
    }

    @Override
    public String unparse() {
        return String.format("RecordFieldDeref(%s, %s)", recordExpr.unparse(), fieldName);
    }

    // TODO [discard tests - or move to Unit Testing]
    public static void main(String[] args) {
        // {f3: {f1: 1 ; f2: '2'}}
        var nestedRecordExpr = RecordExpr.of(
                Record.ofEntries(Record.Field.of("f3",
                        RecordExpr.of(Record.ofEntries(
                                Record.Field.of("f1", IntLiteral.of(1)),
                                Record.Field.of("f2", StringLiteral.of("2")))))
                ));
        // {f3: {f1: 1 ; f2: '2'}}.f3
        var f3FieldDeref = new RecordFieldDeref(nestedRecordExpr, "f3");
        // {f1: 1 ; f2: '2'}
        RecordVal rec = (RecordVal) f3FieldDeref.eval(Environment.empty());

        // {f3: {f1: 1 ; f2: '2'}}.f3.f1
        var doubleDerefExpr = new RecordFieldDeref(
                new RecordFieldDeref(nestedRecordExpr, "f3"),
                "f1");

        IntVal intVal = (IntVal) doubleDerefExpr.eval(Environment.empty());
        System.err.println(doubleDerefExpr + "   -->   " + intVal);
    }
}
