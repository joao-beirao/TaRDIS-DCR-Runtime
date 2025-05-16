package deprecated.dcr;

import deprecated.dcr.ast.typing.Type;

// TODO review along with InputAction - some redundancy after extensions
abstract class DcrInputEvent extends DcrEvent {

  DcrInputEvent(String eventId, String action, String initiator, Type typeExpression,
      EventMarking marking) {
    super(eventId, action, initiator, typeExpression, marking);
  }

  DcrInputEvent(String eventId, String action, String initiator, Type typeExpression) {
    super(eventId, action, initiator, typeExpression);
  }

  DcrInputEvent(DcrInputEvent other) {
    this(other.getId(), other.getAction(), other.getInitiator(), other.getTypeExpression(),
        other.getMarking());
  }
}
