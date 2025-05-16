package deprecated.dcr;

import deprecated.dcr.ast.typing.Type;
import deprecated.dcr.ast.values.Value;

/**
 * An end-point projected event
 */
abstract class DcrEvent implements Event {
  // TODO [revisit - ASAP] id vs reda_id - assuming for now this is a
  // globally unique identifier - may need two identifiers
  private final String id;
  private final String action;
  private final String initiator;
  private final Type typeExpression;
  private final EventMarking marking;

  // full constructor
  DcrEvent(String id, String action, String initiator, Type type, EventMarking marking) {
    this.id = id;
    this.action = action;
    this.initiator = initiator;
    this.typeExpression = type;
    this.marking = marking;
  }

  // conveninence constructor for default marking
  DcrEvent(String id, String action, String initiator, Type type) {
    this(id, action, initiator, type, new EventMarking());
  }

  // convenience copy constructor
  DcrEvent(DcrEvent e) {
    this(e.id, e.action, e.initiator, e.typeExpression, e.marking);
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getAction() {
    return action;
  }

  @Override
  public abstract String getLabel();

  @Override
  public abstract String getKind();

  @Override
  public String getInitiator() {
    return initiator;
  }

  @Override
  public Type getTypeExpression() {
    return typeExpression;
  }

  @Override
  public int hashCode() {
    return action.hashCode();
  }

  // event equality by id
  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (obj instanceof DcrEvent) {
      return id.equals(((DcrEvent) obj).id);
    }
    return false;
  }

  @Override
  public EventMarking getMarking() {
    return new EventMarking(this.marking);
  }

  // TODO maybe this should not receive an argument - once executed, it does not
  // change back
  /**
   * Update the marking of this event to reflect the provided
   * <code>isExecuted</code> status.
   *
   * @param isExecuted whether this event should become executed.
   * @return the updated event
   */
  DcrEvent setExecuted(boolean isExecuted) {
    this.marking.setExecuted(isExecuted);
    return this;
  }

  /**
   * Update the marking of this event to reflect the provided
   * <code>isPending</code> status.
   *
   * @param isPending whether this event should become pending.
   * @return the updated event
   */
  DcrEvent setPending(boolean isPending) {
    this.marking.setPending(isPending);
    return this;
  }

  /**
   * Update the marking of this event to reflect the provided
   * <code>isIncluded</code> status.
   *
   * @param isIncluded whether this event should become included.
   * @return the updated event
   */
  DcrEvent setIncluded(boolean isIncluded) {
    this.marking.setIncluded(isIncluded);
    return this;
  }

  /**
   * Update the marking of this event to reflect the provided
   * <code>value</code>.
   *
   * @param value the value to store in this event's marking
   * @return the updated event
   */
  DcrEvent setValue(Value value) {
    // TODO
    // if (this.marking.getValue().getType().equals(value.getType())) {
    this.marking.setValue(value);
    // } else
    // System.err.println("Bad things happened here ");
    return this;
  }

  /**
   * Update the marking of this event to reflect the provided
   * <code>marking</code>.
   * </p>
   * This method does not directly store the argument, and therefore,
   * further modifying the provided <code>marking</code> after this method returns
   * will have no effect over the this event.
   *
   * @param marking the event state to reflect
   * @return the updated event
   */
  DcrEvent setMarking(EventMarking marking) {
    this.marking.setExecuted(marking.isExecuted());
    this.marking.setPending(marking.isPending());
    this.marking.setIncluded(marking.isIncluded());
    this.marking.setValue(marking.getValue());
    return this;
  }

  @Override
  public String toString() {
    return String.format("id:%s  %s  %s ", this.getId(), this.getLabel(), this.getMarking());
  }

  public String unparse() {
    // return String.format(" %s: %s %s : %s", this.getId(), this.getAction(),
    // this.getMarking().unparse(), this.getTypeExpression().unparse());
    return String.format(
        "(%s) %s(%s : %s) [ %s ]",
        this.getKind(),
        this.getMarking().unparseAbbreviated(),
        this.getId(),
        this.getAction(),
        this.getTypeExpression().unparse(),
        this.getMarking().getValue().unparse());
  }
}
