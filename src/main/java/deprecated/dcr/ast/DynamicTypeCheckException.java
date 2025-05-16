package deprecated.dcr.ast;

import deprecated.dcr.ast.values.Value;

public class DynamicTypeCheckException extends Exception {
    private final ASTNode expression;
    private final Class<? extends Value> found;
    private final Class<? extends Value> expected;
    private final String info;

    public DynamicTypeCheckException(
            ASTNode expression,
            ASTNode illegalExpression,
            Class<? extends Value> found,
            Class<? extends Value> expected,
            String info) {
        super();
        this.expression = expression;
        this.found = found;
        this.expected = expected;
        this.info = info;
    }

    public String getInfo() {
      return info;
    }

    // public DynamicTypeCheckException(ASTNode expression, String problemDetails) {
    //     this(expression, null, problemDetails);
    // }
}
