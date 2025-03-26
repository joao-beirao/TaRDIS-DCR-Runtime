package dcr.ast.typing;

public class UnitType implements Type {
  public static final UnitType TYPE = new UnitType();
  private static final String LABEL = "unit";

  private UnitType() {
  }

  // @Override
  // public Type instance() {
  //   return null;
  // }

  @Override
  public boolean equalsType(Type t) {
    return t == TYPE;
  }

  @Override
  public String toString() {
    return "UnitType";
  }

  @Override
  public String unparse() {
    return LABEL;
  }
}
