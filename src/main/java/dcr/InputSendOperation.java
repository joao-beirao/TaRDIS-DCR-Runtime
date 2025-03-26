package dcr;

import java.util.HashSet;
import java.util.Set;

import dcr.ast.typing.Type;

/**
 * A POJO holding the runtime state of a DCR end-point projected event,
 * currently reflecting the tuple <code>(Ex, Pe, In)</code>
 */

// TODO extend to set of receivers: Set<String> receivers (validade that
// initiator not in 'receivers')

// TODO review - some redundancy after extensions - could just extend
// InputAction (which could not be final)
public final class InputSendOperation extends DcrInputEvent {

  private final Set<String> receivers;

  InputSendOperation(String eventId, String action, String initiator, Set<String> receivers, Type typeExpression,
      EventMarking marking) {
    super(eventId, action, initiator, typeExpression, marking);
    this.receivers = receivers;
  }

  InputSendOperation(String eventId, String action, String initiator, Set<String> receivers, Type typeExpression,
      Type type) {
    super(eventId, action, initiator, typeExpression);
    this.receivers = receivers;
  }

  InputSendOperation(String eventId, InputSendOperation other) {
    super(other);
    this.receivers = new HashSet<>(other.getReceivers());
  }

  public Set<String> getReceivers() {
    return receivers;
  }

  @Override
  public String getLabel() {
    // TODO remove
    return this.getAction();
    // return String.format("( ! %s @ %s, %s )", this.getAction(), String.join(",",
    // this.getReceivers()),
    // this.getInitiator());
  }

  // @Override
  // public String getLabel() {
  // // TODO fix set receivers
  // return String.format("( ! %s @ %s, %s )", this.getAction(), String.join(",",
  // this.getReceivers()),
  // this.getInitiator());
  // }

  @Override
  public String getKind() {
    return "send";
  }
}