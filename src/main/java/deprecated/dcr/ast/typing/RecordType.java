package deprecated.dcr.ast.typing;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class RecordType implements Type {

  private final Map<String, Type> fieldTypes;
  private final List<String> displayOrder;
  private final String astString;
  private final String unparsedString;

  private RecordType(Map<String, Type> fieldTypes, List<String> displayOrder) {
    this.fieldTypes = fieldTypes;
    this.displayOrder = displayOrder;
    this.astString = toASTString(displayOrder, fieldTypes);
    this.unparsedString = toUnparseString(displayOrder, fieldTypes);
  }

  // TODO [revisit javadoc description]
  /**
   * Equality between Records is based on having the same names for fields, and
   * for each named field, the same type...
   */
  @Override
  public boolean equalsType(Type t) {
    if (t == null || t.getClass() != this.getClass()) {
      return false;
    }
    RecordType other = (RecordType) t;
    boolean res = displayOrder
        .stream()
        .map(name -> other.fieldTypes.containsKey(name)
            && this.fieldTypes.get(name).equals(other.fieldTypes.get(name)))
        .noneMatch(e -> e == false);
    return res;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public Iterable<String> fieldNames() {
    return this.displayOrder;
  }

  public Optional<Type> getFieldType(String fieldName) {
    return Optional.ofNullable(fieldTypes.get(fieldName));
  }

  @Override
  public String toString() {
    return astString;
  }

  @Override
  public String unparse() {
    return unparsedString;
  }

  private static String toASTString(List<String> displayOrder, Map<String, Type> fieldTypes) {
    String recordFieldsAsASTString = displayOrder
        .stream()
        .map(field -> String.format("%s:%s", fieldTypes.get(field).toString(), field))
        .collect(Collectors.joining(", "));
    return String.format("RecordType(%s)", recordFieldsAsASTString);
  }

  private static String toUnparseString(List<String> displayOrder, Map<String, Type> fieldTypes) {
    String recordFieldsAsString = displayOrder
        .stream()
        .map(field -> String.format("%s:%s", field, fieldTypes.get(field).unparse()))
        .collect(Collectors.joining(", "));
    return String.format("{%s}", recordFieldsAsString);
  }

  public static class Builder {

    private final Map<String, Type> fields;
    private final List<String> displayOrder;

    public Builder add(String fieldName, Type fieldType) throws Exception {
      if (displayOrder.contains(fieldName)) {
        // TODO [revisit] maybe throw custom exception
        throw new Exception("A record cannot contain duplicate fields");
      }
      fields.put(fieldName, fieldType);
      displayOrder.add(fieldName);
      return this;
    }

    public RecordType build() {
      return new RecordType(fields, displayOrder);
    }

    Builder() {
      fields = new HashMap<>();
      displayOrder = new LinkedList<>();
    }
  }

}
