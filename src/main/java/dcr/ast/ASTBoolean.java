package dcr.ast;

import dcr.ast.values.BooleanVal;
import dcr.ast.values.Value;

/** */
public final class ASTBoolean implements ASTNode {

    private final BooleanVal value;

    /**
     * Constructs an ASTBoolean node enclosing a default-initialized
     * {@link BooleanVal}
     */
    public ASTBoolean() {
        this.value = new BooleanVal();
    }

    /**
     * Constructs an ASTBoolean node enclosing a {@link BooleanVal} initialized from
     * the provided <code>value</code> argument.
     * 
     * @param value boolean value associated to the enclosed {@link BooleanVal}.
     */
    public ASTBoolean(boolean value) {
        this.value = new BooleanVal(value);
    }

    /**
     * Constructs an ASTBoolean node enclosing the provided {@link BooleanVal}.
     * 
     * @param booleanVal {@link BooleanVal} enclosed by this instance
     */
    public ASTBoolean(BooleanVal booleanVal) {
        this.value = booleanVal;
    }

    @Override
    public Value eval(Environment env) {
        return value;
    }

    @Override
    public String toString() {
        return String.format("ASTBoolean(%s)", String.valueOf(value.getValue()));
    }

    @Override
    public String unparse() {
        return String.valueOf(value.getValue());
    }

}