package dcr;

import dcr.ast.typing.Type;

/**
 * An end-point projected input event that can be executed locally.
 */
class InputAction extends DcrInputEvent {

    InputAction(String eventId, String action, String initiator, Type type, EventMarking marking) {
        super(eventId, action, initiator, type, marking);
    }

    InputAction(String eventId, String action, Type type, String initiator) {
        super(eventId, action, initiator, type);
    }

    // convenience copy constructor
    InputAction(InputAction other) {
        this(other.getId(), other.getAction(), other.getInitiator(), other.getTypeExpression(), other.getMarking());
    }

    @Override
    public String getLabel() {
        return String.format("( ? %s, %s )", this.getAction(), this.getInitiator());
    }

    @Override
    public String getKind() {
        return "action";
    }

}