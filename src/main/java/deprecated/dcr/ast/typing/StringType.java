package deprecated.dcr.ast.typing;

public final class StringType implements Type {

  public static final StringType TYPE = new StringType();
  private static final String LABEL = "string";

  private StringType() {
  }

  // @Override
  // public Type instance() {
  //   return TYPE;
  // }

  @Override
  public boolean equalsType(Type t) {
    return t == TYPE;
  }

  @Override
  public String toString() {
    return "StringType";
  }

  @Override
  public String unparse() {
    return LABEL;
  }
}
