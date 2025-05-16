package deprecated.dcr.ast;

import java.util.HashMap;
import java.util.Map;

import deprecated.dcr.ast.values.RecordVal;
import deprecated.dcr.ast.values.Value;

/**
 * A Record node
 */
public final class ASTRecord implements ASTNode {

  private final Iterable<RecordField> fields;

  public ASTRecord(Iterable<RecordField> fields) {
    this.fields = fields;
  }

// TODO [revisit] constructors for recordval

  @Override
  public Value eval(Environment env) throws DynamicTypeCheckException, UndeclaredIdentifierException {
    Map<String, Value> fieldsByName = new HashMap<>();
    for (RecordField field : fields) {
      // TODO [revisit] maybe we want to discard some type of values from being on a
      // recordfield.. assuming typechecker took care of it
      fieldsByName.put(field.name, field.value.eval(env));
    }
    return RecordVal.builder().addFields(fieldsByName).build();
  }

  @Override
  public String toString() {
    // TODO Auto-generated method stub
    return super.toString();
  }

  @Override
  public String unparse() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'unparse'");
  }

  public static final class RecordField {
    final String name;
    final ASTNode value;

    public RecordField(String name, ASTNode value) {
      this.name = name;
      this.value = value;
    }
  }

}
