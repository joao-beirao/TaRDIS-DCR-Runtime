package dcr.ast.typing;

public class RefType implements Type {
  public static final RefType TYPE = new RefType();
  private static final String LABEL = "ref";

  private RefType() {
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
