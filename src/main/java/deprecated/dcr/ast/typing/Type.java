package deprecated.dcr.ast.typing;

public interface Type {

  // Type instance();

  boolean equalsType(Type t);

  String unparse();
}

