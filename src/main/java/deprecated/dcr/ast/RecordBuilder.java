package deprecated.dcr.ast;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// TODO [discard?]
public abstract class RecordBuilder<T, V> {
  private final Map<String, V> fields;
  private final List<String> displayOrder;

  public RecordBuilder<T, V> add(String fieldName, V fieldType) throws Exception {
    if (displayOrder.contains(fieldName)) {
      // TODO [revisit] maybe throw custom exception
      throw new Exception("A record cannot contain ");
    }
    fields.put(fieldName, fieldType);
    displayOrder.add(fieldName);
    return this;
  }

  public abstract T build();

  RecordBuilder() {
    fields = new HashMap<>();
    displayOrder = new LinkedList<>();
  }


}
