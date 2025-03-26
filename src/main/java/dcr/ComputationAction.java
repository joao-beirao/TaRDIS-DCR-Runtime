package dcr;

import dcr.ast.ASTNode;
import dcr.ast.typing.Type;

public final class ComputationAction extends DcrComputationEvent {

  ComputationAction(String eventId, String action, String initiator, ASTNode computationExpression, Type type,
      EventMarking marking) {
    super(eventId, action, initiator, computationExpression, type, marking);
  }

  ComputationAction(String eventId, String action, String initiator, ASTNode computationExpression, Type type) {
    super(eventId, action, initiator, computationExpression, type, new EventMarking());
  }

  ComputationAction(ComputationAction other) {
    super(other);
  }

  @Override
  public String getLabel() {
    return String.format("%s", this.getAction());
  }

  @Override
  public String getKind() {
    return "action";
  }
}