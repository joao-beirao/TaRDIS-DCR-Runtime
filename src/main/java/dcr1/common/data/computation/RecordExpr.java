package dcr1.common.data.computation;


import dcr1.common.Environment;
import dcr1.common.Record;
import dcr1.common.data.values.PropBasedVal;
import dcr1.common.data.values.RecordVal;
import dcr1.common.data.values.Value;

import java.util.Objects;

/**
 * A Record node
 */
public final class RecordExpr
        implements PropBasedExpr {

    private final Record<ComputationExpression> exprRecord;

    private RecordExpr(Record<ComputationExpression> exprRecord) {

        this.exprRecord = exprRecord;
    }

    public static RecordExpr of(Record<ComputationExpression> recordExpr) {
        return new RecordExpr(Objects.requireNonNull(recordExpr));
    }

    @Override
    public PropBasedVal eval(Environment<Value> env) {
        Record.Builder<Value> builder = new Record.Builder<>();
        for (var fieldExpr : exprRecord.fields()) {
            builder.addFieldWithParams(fieldExpr.name(), fieldExpr.value().eval(env));
        }
        return RecordVal.of(builder.build());
    }

    @Override
    public String toString() {return exprRecord.toString();}

    @Override
    public String unparse() {
        return String.format("RecordExpr( %s )",
                exprRecord.unparseFields(ComputationExpression::unparse));
    }

    // Usage Example
    public static void main(String[] args) {

        // flat record (via .ofEntries())
        RecordExpr flatExpr = RecordExpr.of(
                Record.ofEntries(Record.Field.of("f1", IntegerLiteral.of(1)),
                        Record.Field.of("f2", StringLiteral.of("2"))));

        // nested records (via .ofEntries())
        RecordExpr nestedExpr = RecordExpr.of(
                Record.ofEntries(Record.Field.of("f1", IntegerLiteral.of(1)),
                        Record.Field.of("f2", StringLiteral.of("2")), Record.Field.of("f3",
                                RecordExpr.of(Record.ofEntries(
                                        Record.Field.of("f1", IntegerLiteral.of(1)),
                                        Record.Field.of("f2", StringLiteral.of("2"))))),
                        Record.Field.of("f4", BooleanLiteral.of(true)),
                        Record.Field.of("f5", BooleanLiteral.of(false))));

        // nested records (mixing builder() and .ofEntries()
        var recordFromBuilder = new Record.Builder<ComputationExpression>().addField(
                        Record.Field.of("f1", IntegerLiteral.of(1)))
                .addField(Record.Field.of("f2", StringLiteral.of("2")))
                .addField(Record.Field.of("f3",
                        RecordExpr.of(Record.ofEntries(
                                Record.Field.of("f1", IntegerLiteral.of(1)),
                                Record.Field.of("f2", StringLiteral.of("2"))))))
                .build();
        System.err.println(flatExpr);
        System.err.println(nestedExpr);
        System.err.println();
        System.err.println(flatExpr.unparse());
        System.err.println(nestedExpr.unparse());
        System.err.println();
        System.err.println(recordFromBuilder);
        System.err.println(RecordExpr.of(recordFromBuilder).unparse());
    }
}