package dcr;

import dcr.ast.typing.Type;

public class ReceiveOperation extends DcrEvent {

    private final String receiver;

    ReceiveOperation(String eventId, String action, String initiator, String receiver, Type type,
            EventMarking marking) {
        super(eventId, action, initiator, type, marking);
        this.receiver = receiver;
    }

    ReceiveOperation(String eventId, String action, String initiator, Type type, String receiver) {
        super(eventId, action, initiator, type);
        this.receiver = receiver;
    }

    ReceiveOperation(ReceiveOperation other) {
        this(other.getId(), other.getAction(), other.getInitiator(), other.getReceiver(), other.getTypeExpression(),
                other.getMarking());
    }

    public String getReceiver() {
        return receiver;
    }

    @Override
    public String getLabel() {
        return String.format("( ? %s @ %s, %s )", this.getAction(), this.getInitiator(), this.getReceiver());
    }

    @Override
    public String getKind() {
        return "receive";
    }

    // @Override
    // public String toString() {
    // return String.format("(?%s@%s, %s)", this.getAction(), receiver,
    // this.getInitiator());
    // }
}