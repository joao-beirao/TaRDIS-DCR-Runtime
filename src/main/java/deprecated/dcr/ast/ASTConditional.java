package deprecated.dcr.ast;

import deprecated.dcr.ast.values.BooleanVal;
import deprecated.dcr.ast.values.Value;

public class ASTConditional implements ASTNode {
  private final ASTNode conditionNode;
  private final ASTNode thenNode;
  private final ASTNode elseNode;

  public ASTConditional(ASTNode conditionNode, ASTNode thenNode, ASTNode elseNode) {
    this.conditionNode = conditionNode;
    this.thenNode = thenNode;
    this.elseNode = elseNode;
  }

  public Value eval(Environment env)
      throws DynamicTypeCheckException, UndeclaredIdentifierException {
    Value conditionValue = conditionNode.eval(env);
    if (conditionValue instanceof BooleanVal) {
      // TODO [review request] we're doing lazy evaluation here since it's not
      // possible (..right?) to check whether then and else evaluate to
      // some 'unifiable' value type (not possible from here, ..right?)
      if (((BooleanVal) conditionValue).getValue()) {
        return thenNode.eval(env);
      } else {
        return elseNode.eval(env);
      }
    }
    throw new DynamicTypeCheckException(this, conditionNode, conditionValue.getClass(), BooleanVal.class,
        "Conditional requires a boolean-valued condition: found " + conditionValue.getClass().getSimpleName());
  }

  @Override
  public String toString() {
    return String.format("ASTConditional(%s, %s, %s)", conditionNode, thenNode, elseNode);
  }

  @Override
  public String unparse() {
    return String.format("if (%s) then { %s } else { %s }", conditionNode.unparse(), thenNode.unparse(),
        elseNode.unparse());
  }

}
