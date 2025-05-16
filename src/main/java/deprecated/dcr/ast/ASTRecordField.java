package deprecated.dcr.ast;

import deprecated.dcr.ast.values.RecordVal;
import deprecated.dcr.ast.values.Value;

public class ASTRecordField implements ASTNode {

  private final ASTNode recordNode;
  private final String fieldName;

  public ASTRecordField(String fieldName, ASTNode recordNode) {
    this.recordNode = recordNode;
    this.fieldName = fieldName;
  }

  @Override
  public Value eval(Environment env) throws DynamicTypeCheckException, UndeclaredIdentifierException {
    Value recordVal = recordNode.eval(env);
    if (recordVal instanceof RecordVal) {
      // TODO revisit - optional received and discarded with .get(), but should add an
      // exception here for the time being - typecheck should eventually render this
      // useless
      return ((RecordVal) recordVal).getValue(fieldName);
    }
    // this should not happen - either typecheck or input validation must have
    // failed
    throw new DynamicTypeCheckException(
        recordNode,
        recordNode,
        recordVal.getClass(),
        RecordVal.class,
        String.format("Expected a RecordVal to access a record field: found %s", recordVal.getClass().getSimpleName()));
  }

  @Override
  public String unparse() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'unparse'");
  }

}
