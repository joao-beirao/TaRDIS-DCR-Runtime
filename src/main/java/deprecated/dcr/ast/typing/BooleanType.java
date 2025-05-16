package deprecated.dcr.ast.typing;

public class BooleanType implements Type {

  public static final BooleanType TYPE = new BooleanType();
  private static final String LABEL = "boolean";

  private BooleanType() {
  }

  @Override
  public boolean equalsType(Type t) {
    return t == TYPE;
  }

  @Override
  public String toString() {
    return "BooleanType";
  }

  @Override
  public String unparse() {
    return LABEL;
  }

}
