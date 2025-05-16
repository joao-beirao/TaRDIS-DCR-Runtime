package deprecated.dcr;

import java.util.HashSet;
import java.util.Set;

import deprecated.dcr.ast.ASTNode;
import deprecated.dcr.ast.typing.Type;

/**
 * A POJO holding the runtime state of a DCR end-point projected event,
 * currently reflecting the tuple <code>(Ex, Pe, In)</code>
 */

// TODO extend to set of receivers: Set<String> receivers (validade that
// initiator not in 'receivers')
/*
 * public DCRGraph addInteractionInputEventOf(String action, String initiator,
 * String receiver,
 * EventMarking marking) {
 * Event res = new Event(action, initiator, receiver, true, true, marking);
 * this.events.put(action, res);
 * return this;
 * }
 */

// TODO projected events are either actions or interactions - we will
// eventually need to adjust to

public class ComputationSendOperation extends DcrComputationEvent {

  private final Set<String> receivers;

  ComputationSendOperation(String eventId, String action, String initiator, Set<String> receivers,
      ASTNode computationExpression, Type type, EventMarking marking) {
    super(eventId, action, initiator, computationExpression, type, marking);
    this.receivers = receivers;
  }

  ComputationSendOperation(String eventId, String action, String initiator, Set<String> receivers,
      ASTNode computationExpression, Type type) {
    super(eventId, action, initiator, computationExpression, type);
    this.receivers = receivers;
  }

  ComputationSendOperation(String eventId, ComputationSendOperation other) {
    super(other);
    this.receivers = new HashSet<>(other.getReceivers());
  }

  public Set<String> getReceivers() {
    return receivers;
  }

  @Override
  public String getLabel() {
    // TODO fix set receivers
    return String.format("( ! %s @ %s, %s )", this.getAction(), String.join(",", this.getReceivers()),
        this.getInitiator());
  }

}