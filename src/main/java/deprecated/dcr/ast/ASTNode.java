package deprecated.dcr.ast;

import deprecated.dcr.ast.values.Value;

public interface ASTNode {

    /**
     * Evaluates and returns the {@link Value value} enclosed by this node under the provided {@link Environment env}
     *
     * @param env the environment under which to evaluate this node.
     * @return the {@link Value value} to which this node evaluates under the provided environment.
     * @throws DynamicTypeCheckException
     */
    Value eval(Environment env) throws DynamicTypeCheckException, UndeclaredIdentifierException;

    @Override
    String toString();

    public String unparse();
}



