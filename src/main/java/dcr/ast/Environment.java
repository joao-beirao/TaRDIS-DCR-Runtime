package dcr.ast;

import java.util.HashMap;
import java.util.Map;

import dcr.ast.values.Value;

public final class Environment {

  private final Environment ancestor;
  private final Map<String, Value> bindings;
  private int level;

  /**
   * Create an new environment frame enclosed within the ancestor environment
   * frame
   * 
   * @param ancestor
   * @param level
   */
  private Environment(Environment ancestor, int level) {
    this.level = level;
    this.ancestor = ancestor;
    this.bindings = new HashMap<>();
  }

  /**
   * Create the global environment
   */
  public Environment() {
    this(null, 0);
  }

  public Environment beginScope() {
    return new Environment(this, level + 1);
  }

  public Environment endScope() {
    return ancestor;
  }

  public int getLevel() {
    return level;
  }

  public void bind(String name, Value value) {
    bindings.put(name, value);
  }

  /**
   * Code fails, how to check ancestor and kids?;
   */
  public Value lookup(String identifier) throws UndeclaredIdentifierException {
    Value value = bindings.get(identifier);
    if (value != null) {
      return value;
    } else if (ancestor != null) {
      return ancestor.lookup(identifier);
    } else {
      throw new UndeclaredIdentifierException("Variable nor found: " + identifier);
    }

  }

  @Override
  public String toString() {
    return new StringBuilder()
        .append("Environment{")
        .append("level=")
        .append(level)
        .append(", binding=")
        .append(bindings)
        .append(", ancestor=")
        .append(ancestor)
        .append("}")
        .toString();
  }

}
