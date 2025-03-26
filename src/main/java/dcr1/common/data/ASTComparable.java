package dcr1.common.data;

// TODO revise T param (extends something?)
public interface ASTComparable<T> {
  boolean isEqualTo(T other);
}
