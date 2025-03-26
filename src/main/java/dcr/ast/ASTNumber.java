package dcr.ast;

import dcr.ast.values.NumberVal;
import dcr.ast.values.Value;

public final class ASTNumber implements ASTNode, ASTComparable<NumberVal> {

    // TODO same comment placed in NumberVal -> this just should be renamed to
    // ASTInteger (but ReGraDa would have to go first)
    private final NumberVal value;

    /**
     * Constructs an ASTNumber node enclosing a default-initialized
     * {@link NumberVal}
     */
    public ASTNumber() {
        this.value = new NumberVal();
    }

    /**
     * Constructs an ASTNumber node enclosing a {@link NumberVal} initialized from
     * the provided <code>value</code> argument.
     * 
     * @param value int value associated to the enclosed {@link NumberVal}.
     */
    public ASTNumber(int value) {
        this.value = new NumberVal(value);
    }

 /**
     * Constructs an ASTNumber node enclosing the provided {@link NumberVal}.
     * 
     * @param booleanVal {@link NumberVal} enclosed by this instance
     */
    public ASTNumber(NumberVal numberVal) {
        this.value = numberVal;
    }

    @Override
    public Value eval(Environment env) throws DynamicTypeCheckException {
        return this.value;
    }

    @Override
    public String toString() {
        return String.format("ASTNumber(%s)", value);
    }

    @Override
    public String unparse() {
        return String.valueOf(value);
    }

    @Override
    public boolean isEqualTo(NumberVal other) {
      return this.value.getValue() == other.getValue();
    }

}