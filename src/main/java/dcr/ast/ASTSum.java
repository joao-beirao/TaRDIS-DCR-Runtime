package dcr.ast;

import dcr.ast.values.NumberVal;
import dcr.ast.values.Value;

public class ASTSum extends ASTBinaryArithmeticOp {

    public ASTSum(ASTNode left, ASTNode right) {
        super(left, right);
    }

    protected Value compute(NumberVal leftVal, NumberVal rightVal) {
        return new NumberVal(leftVal.getValue() + rightVal.getValue());
    }

    @Override
    public String toString() {
        return String.format("ASTSum(%s, %s)", left, right);
    }

    @Override
    public String unparse() {
        return String.format("%s + %s", left, right);
    }

}
