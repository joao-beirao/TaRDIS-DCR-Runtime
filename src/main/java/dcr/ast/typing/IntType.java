package dcr.ast.typing;

public final class IntType implements Type {

  public static final IntType TYPE = new IntType();
  private static final String LABEL = "int";

  private IntType() {
  }

  @Override
  public boolean equalsType(Type t) {
    return t == TYPE;
  }

  @Override
  public String toString() {
    return "IntType";
  }

  @Override
  public String unparse() {
    return LABEL;
  }

}
