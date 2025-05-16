package deprecated.dcr.ast.values;

import deprecated.dcr.ast.typing.StringType;
import deprecated.dcr.ast.typing.Type;

/**
 * An immutable {@link Value} enclosing a String literal
 */
public final class StringVal implements Value, Comparable<StringVal> {

    /**
     * Default value for a StringVal is an empty String {@value}
     */
    public static final String DEFAULT_STRING = "";
    private static final long serialVersionUID = -617630784691869772L;

    private String value;

    @Override
    public Type getType() {
        return StringType.TYPE;
    }

    /**
     * Constructs a StringVal enclosing the {@link #DEFAULT_STRING default empty
     * string}.
     */
    public StringVal() {
        this.value = DEFAULT_STRING;
    }

    /**
     * Constructs a StringVal enclosing the provided <code>value</code>.
     *
     * @param value the String literal to enclose within this Value
     */
    public StringVal(String value) {
        this.value = value;
    }

    /**
     * Retrieves the String literal enclosed by this Value
     *
     * @return the int literal enclosed by this Value
     */
    public String getValue() {
        return this.value;
    }

    public void setValue(StringVal newVal) {
      this.value = newVal.getValue();
    }

    @Override
    public String toString() {
        return String.format("StringVal(%s)", value);
    }

    public String unparse() {
        return String.format("\"%s\"", value);
    }

    @Override
    public int compareTo(StringVal other) {
        return this.value.compareToIgnoreCase(other.value);
    }

}
