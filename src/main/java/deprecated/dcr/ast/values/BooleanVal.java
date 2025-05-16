package deprecated.dcr.ast.values;

import deprecated.dcr.ast.typing.BooleanType;
import deprecated.dcr.ast.typing.Type;

/**
 * An immutable {@link Value} enclosing a boolean literal
 */
public final class BooleanVal implements Value {

    /**
     * Default value for a BooleanVal is the boolean literal {@value}
     */
    private static final boolean DEFAULT_VALUE = false;
    private static final long serialVersionUID = 3976540914858038158L;
    private boolean value;


    public Type getType() {
      return BooleanType.TYPE;
    }


    /**
     * Constructs a BooleanVal enclosing the {@link #DEFAULT_VALUE default boolean
     * literal}.
     */
    public BooleanVal() {
        this.value = DEFAULT_VALUE;
    }

    // TODO [pending review request] - this returns false if an unexpected value
    // arrives, null included - this can silently enable bugs; was it intended?
    // public Boolean(String value) {
    // this.value = java.lang.Boolean.parseBoolean(value);
    // }

    /**
     * Constructs a BooleanVal enclosing the provided <code>value</code>.
     *
     * @param value the boolean literal to enclose within this Value
     */
    public BooleanVal(boolean value) {
        this.value = value;
    }

    /**
     * Retrieves the boolean literal enclosed by this Value
     *
     * @return the boolean literal enclosed by this Value
     */
    public boolean getValue() {
        return this.value;
    }

    public void setValue(BooleanVal value) {
        this.value = value.getValue();
    }

    @Override
    public String toString() {
        return String.format("Bool(%s)", String.valueOf(value));
    }

    @Override
    public String unparse() {
        return String.valueOf(value);
    }
}
