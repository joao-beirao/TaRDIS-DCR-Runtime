package deprecated.dcr;

import deprecated.dcr.ast.ASTNode;
import deprecated.dcr.ast.typing.Type;

abstract class DcrComputationEvent extends DcrEvent {

  private final ASTNode computationExpression;

  DcrComputationEvent(String eventId, String action, String initiator, ASTNode computationExpression, Type type,
      EventMarking marking) {
    super(eventId, action, initiator, type, marking);
    this.computationExpression = computationExpression;
  }

  DcrComputationEvent(String eventId, String action, String initiator, ASTNode computationExpression, Type type) {
    super(eventId, action, initiator, type);
    this.computationExpression = computationExpression;
  }

  DcrComputationEvent(DcrComputationEvent other) {
    this(other.getId(), other.getAction(), other.getInitiator(), other.getComputationExpression(),
        other.getTypeExpression(),
        other.getMarking());
  }

  ASTNode getComputationExpression() {
    return this.computationExpression;
  }

  @Override
  public String getKind() {
    return "computation";
  }
}
