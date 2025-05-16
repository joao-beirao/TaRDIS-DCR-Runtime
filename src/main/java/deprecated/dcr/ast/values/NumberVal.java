package deprecated.dcr.ast.values;

import deprecated.dcr.ast.typing.IntType;
import deprecated.dcr.ast.typing.Type;

// TODO maybe rename both here and ReGraDa? Why Number and not Integer?
/**
 * An immutable {@link Value} enclosing an integer literal
 */
public final class NumberVal implements Value, Comparable<NumberVal> {

  /**
   * Default value for a NumberVal is the boolean literal {@value}
   */
  public static final int DEFAULT_VALUE = 0;
  private static final long serialVersionUID = -6289916328707077719L;
    // public static final Type TYPE = IntType.TYPE;
  private int value;


   @Override
    public Type getType() {
        return IntType.TYPE;
    }

  /**
   * Constructs a NumberVal enclosing the {@link #DEFAULT_VALUE default number
   * literal}.
   */
  public NumberVal() {
    this.value = DEFAULT_VALUE;
  }

  /**
   * Constructs a NumberVal enclosing the provided <code>value</code>.
   *
   * @param value the int literal to enclose within this Value
   */
  public NumberVal(int value) {
    this.value = value;
  }

  /**
   * Retrieves the int literal enclosed by this Value
   *
   * @return the int literal enclosed by this Value
   */
  public int getValue() {
    return this.value;
  }

  public void setValue(NumberVal newValue) {
    this.value = newValue.getValue();
  }

  @Override
  public String toString() {
    return String.format("Number(%s)", String.valueOf(value));
  }

  public String unparse() {
    return String.valueOf(value);
  }

  @Override
  public int compareTo(NumberVal other) {
    return Integer.valueOf(this.value).compareTo(Integer.valueOf(other.value));
  }
}
