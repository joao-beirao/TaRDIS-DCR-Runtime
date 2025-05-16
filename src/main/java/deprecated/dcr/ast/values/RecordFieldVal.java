package deprecated.dcr.ast.values;

import deprecated.dcr.ast.typing.Type;

public final class RecordFieldVal implements Value {
  private final String name;
  private final Value value;

  public RecordFieldVal(String name, Value value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public Value getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.format("RecordFieldVal(\"%s\": %s)", name, value);
  }

  @Override
  public String unparse() {
    return String.format("%s : %s", name, value.unparse());
  }

  @Override
  public Type getType() {
    // TODO [revise]
    return value.getType();
    // throw new UnsupportedOperationException("Unimplemented method 'getType'");
  }

}
