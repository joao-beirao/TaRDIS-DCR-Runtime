package deprecated.dcr.ast.values;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import deprecated.dcr.ast.typing.RecordType;
import deprecated.dcr.ast.typing.Type;

/**
 * A record-type of value storing a collection of name-value mappings, called
 * fields.
 * </p>
 * A RecordVal instance has a fixed structure, and it is not possible to add,
 * remove, or rename fields. It is also not possible to assign a new value (in
 * the sense of replacement) to a field. A field value can still change if
 * it is inherently mutable (e.g., if the field holds a RefVal instance, one
 * cannot change the instance, but can still mutate it's internal state).
 * </p>
 * The type of a RecordVal is indicated
 */
public final class RecordVal implements Value {

  private static final long serialVersionUID = -1287514074906057952L;
  private final Map<String, RecordFieldVal> fields;

  // private constructor - use builder instead
  private RecordVal(Map<String, RecordFieldVal> fields) {
    this.fields = fields;
  }

  // TODO [revisit] typecheck should eventually cover this, but just in case,
  // leave defensive option in place for now
  public Value getValue(String name) {
    return fields.get(name).getValue();
  }

  /**
   * Retrieves an unmodifiable map view of the record literal enclosed by this
   * Value
   *
   * @return an unmodifiable map view of the record literal enclosed by this
   *         Value
   */
  public Map<String, Value> getValues() {
    return Collections.unmodifiableMap(this.fields);
  }

  @Override
  public String toString() {
    String fieldsStringified = fields
        .entrySet()
        .stream()
        .map(e -> String.format("\"%s\":%s", e.getKey(), e.getValue().getValue().toString()))
        .collect(Collectors.joining(", "));
    return String.format("RecordVal(%s)", fieldsStringified);
  }

  @Override
  public String unparse() {
    String fieldsUnparse = fields
        .entrySet()
        .stream()
        .map(e -> String.format("\"%s\":%s", e.getKey(), e.getValue().getValue().unparse()))
        .collect(Collectors.joining(", "));
    return String.format("{%s}", fieldsUnparse);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private final Map<String, RecordFieldVal> recordFields;

    private Builder() {
      this.recordFields = new HashMap<>();
    }

    public RecordVal build() {// TODO throw appropriate exception
      if (recordFields.isEmpty())
        throw new IllegalArgumentException("A record must defined at least one field");
      return new RecordVal(recordFields);
    }

    public Builder addField(String name, Value value) {
      this.recordFields.put(name, new RecordFieldVal(name, value));
      return this;
    }

    public Builder addField(RecordFieldVal field) {
      this.recordFields.put(field.getName(), new RecordFieldVal(field.getName(), field.getValue()));
      return this;
    }

    public Builder addFields(Map<String, Value> fields) {
      for (Entry<String, Value> entry : fields.entrySet()) {
        this.addField(entry.getKey(), entry.getValue());
      }
      return this;
    }
  }

  @Override
  public Type getType() {
    RecordType.Builder typeBuilder = RecordType.newBuilder();
    fields.entrySet().forEach((entry) -> {
      String name = entry.getKey();
      Type ty = entry.getValue().getType();
      try {
        typeBuilder.add(name, ty);
      } catch(Exception e) {
        // TODO [revise]
        // this should never happen here, must revise structuring
        // ignore this field for now
        System.err.println(">> ! Attempted to add duplicate field to record - discarding");
      }
    });
    return typeBuilder.build();
    // throw new UnsupportedOperationException("Not fully supporting records yet");
  }

}
