package deprecated.dcr;

import java.io.Serializable;
import java.util.Objects;

import deprecated.dcr.ast.values.UnitVal;
import deprecated.dcr.ast.values.Value;

/**
 * A mutable object representing the runtime state of an {@link Event event}'s.
 * </p>
 *
 * The marking currently consists of the following information:
 * <ul>
 * <li><code>isExecuted</code> status, indicating whether the event has already
 * executed</li>
 * <li><code>isPending</code> status, indicating whether the event is currently
 * pending</li>
 * <li><code>isIncluded</code> status, indicating whether the event is currently
 * included</li>
 * <li><code>value</code>, storing the current value of the event, which may be
 * undefined if the event has not yet executed, and defined otherwise</li>
 * </ul>
 */
public final class EventMarking implements Serializable {

  private static final long serialVersionUID = 7487226112623422517L;

  // tuple (in order)
  private boolean isExecuted;
  private boolean isPending;
  private boolean isIncluded;
  private Value value;

  // ========================================================================
  // === Constructors

  /**
   * {@link EventMarking} constructor for a non-default inital marking.
   *
   * @param isExecuted whether the event has previously been executed.
   * @param isPending  whether the event is currently pending.
   * @param isIncluded whether the event is currently included.
   * @param value      the initial value
   */
  public EventMarking(boolean isExecuted, boolean isPending, boolean isIncluded, Value value) {
    this.isExecuted = isExecuted;
    this.isPending = isPending;
    this.isIncluded = isIncluded;
    this.value = Objects.requireNonNull(value);
  }

  // TODO [javadoc]
  public EventMarking(boolean isExecuted, boolean isPending, boolean isIncluded) {
    this.isExecuted = isExecuted;
    this.isPending = isPending;
    this.isIncluded = isIncluded;
    this.value = UnitVal.instance();
  }

  // /**
  // * {@link EventMarking} constructor for a non-default inital marking.
  // *
  // * @param isExecuted whether the event has previously been executed.
  // * @param isPending whether the event is currently pending.
  // * @param isIncluded whether the event is currently included.
  // */
  // EventMarking(boolean isExecuted, boolean isPending, boolean isIncluded, Value
  // value) {
  // this(isExecuted, isPending, isIncluded, Optional.ofNullable(value));
  // }

  /**
   * Default {@link EventMarking} constructor reflecting the default marking
   * <code>{ Ex:false, Pe:false, In:true, val:undefined }</code>.
   */
  public EventMarking() {
    this(false, false, true, UnitVal.instance());
  }

  /**
   * Constructor for a default {@link EventMarking} where the <code>value</code>
   * field is the provided value instead.
   *
   * @param value the value of reflected by this marking
   */
  public EventMarking(Value value) {
    this(false, false, true, value);
  }

  /**
   * Copy constructor
   *
   * @param other marking from which to initialize this one.
   */
  EventMarking(EventMarking other) {
    this(other.isExecuted, other.isPending, other.isIncluded, other.value);
  }

  public static final EventMarking newInitiallyExcluded() {
    return new EventMarking(false, false, false);
  }

  // setters (restricted visibility - marking should not be modified from outside
  // this class)
  EventMarking setExecuted(boolean isExecuted) {
    this.isExecuted = isExecuted;
    return this;
  }

  EventMarking setPending(boolean isPending) {
    this.isPending = isPending;
    return this;
  }

  EventMarking setIncluded(boolean isIncluded) {
    this.isIncluded = isIncluded;
    return this;
  }

  EventMarking setValue(Value value) {
    this.value = Objects.requireNonNull(value);
    return this;
  }

  // getters (public)
  public boolean isExecuted() {
    return this.isExecuted;
  }

  public boolean isPending() {
    return this.isPending;
  }

  public boolean isIncluded() {
    return this.isIncluded;
  }

  public Value getValue() {
    return this.value;
  }

  @Override
  public String toString() {
    return String.format("{%s, %s, %s, %s}", this.isExecuted ? "Ex" : "_", this.isPending ? "Pe" : "_",
        this.isIncluded ? "In" : "_", this.value);
  }

  public String unparse() {
    return String.format("{%s, %s, %s, %s}", this.isExecuted ? "Ex" : "_", this.isPending ? "Pe" : "_",
        this.isIncluded ? "In" : "_", this.value.unparse());
  }

  public String unparseAbbreviated() {
    return String.format("%s%s", this.isPending ? "!" : "",
        !this.isIncluded ? "%" : "");
  }
}