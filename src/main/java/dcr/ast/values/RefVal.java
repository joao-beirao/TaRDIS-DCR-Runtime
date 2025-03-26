package dcr.ast.values;

import dcr.ast.typing.RefType;
import dcr.ast.typing.Type;

public class RefVal implements Value {

  public static final Value DEFAULT_VALUE = UnitVal.instance();
  private Value value;

  public RefVal(Value value) {
    this.value = value;
  }

  public RefVal() {
    this(DEFAULT_VALUE);
  }

  public Value getValue() {
    return value;
  }

  public void setValue(Value value){
    this.value = value;
  }

@Override
public String toString() {
  return String.format("RefVal(%s)", value);
}

  @Override
  public String unparse() {
    return String.format("[ -> %s ]", value.unparse());
  }

  @Override
  public Type getType() {
    return RefType.TYPE;
  }


}
