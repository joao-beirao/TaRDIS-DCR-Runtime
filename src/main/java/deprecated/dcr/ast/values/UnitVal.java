package deprecated.dcr.ast.values;

import deprecated.dcr.ast.typing.Type;
import deprecated.dcr.ast.typing.UnitType;

/**
 * A {@link Value} singleton representing the absence of value (an
 * undefined value)
 */
public final class UnitVal implements Value {

  private static final long serialVersionUID = -1588707935989188517L;
  private static UnitVal SINGLETON = new UnitVal();
  static {
    SINGLETON = new UnitVal();
  }

  public Type getType() {
    return UnitType.TYPE;
  }

  private UnitVal() {
  }

  /**
   * Retrieves the singleton instance.
   *
   * @return the singleton instance.
   */
  public static UnitVal instance() {
    return SINGLETON;
  }

  /**
   * Retrieves the {@link Void} object enclosed by this Value.
   *
   * @return the {@link Void} object enclosed by this Value.
   */
  public Void getValue() {
    return (Void) null;

  }

  @Override
  public String toString() {
    return "UnitVal";
  }

  @Override
  public String unparse() {
    return "<undefined>";
  }
}
