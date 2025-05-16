package deprecated.dcr.ast;

import deprecated.dcr.ast.values.Value;

public class ASTId implements ASTNode {

  private final String identifier;

  public ASTId(String identifier) {
    this.identifier = identifier;
  }

  @Override
  public Value eval(Environment env) throws DynamicTypeCheckException, UndeclaredIdentifierException {
    return env.lookup(identifier);
  }

@Override
public String toString() {
  return String.format("ASTId(%s)", identifier);
}

  @Override
  public String unparse() {
    return identifier;
  }


}
