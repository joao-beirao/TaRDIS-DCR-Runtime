package deprecated.dcr.ast;

import deprecated.dcr.ast.values.StringVal;
import deprecated.dcr.ast.values.Value;

public class ASTString implements ASTNode, ASTComparable<StringVal> {

    private final StringVal value;

        /**
     * Constructs an ASTString node enclosing a default-initialized
     * {@link StringVal}.
     */
    public ASTString() {
        this.value = new StringVal();
    }

     /**
     * Constructs an ASTString node enclosing a {@link StringVal} initialized from
     * the provided <code>value</code> argument.
     *
     * @param value String literal associated to the enclosed {@link StringVal}.
     */
    public ASTString(String value) {
        this.value = new StringVal(value);
    }

     /**
     * Constructs an ASTString node enclosing the provided {@link StringVal}.
     *
     * @param stringVal {@link StringVal} enclosed by this instance
     */
    public ASTString(StringVal stringVal) {
        this.value = stringVal;
    }

    @Override
    public Value eval(Environment env) {
        return this.value;
    }

@Override
public String toString() {
    return String.format("StringVal(\"%s\")", this.value.toString());
}

    @Override
    public String unparse() {
        return this.value.unparse();
    }

    @Override
    public boolean isEqualTo(StringVal other) {
      return this.value.equals(other.getValue());
    }

}
