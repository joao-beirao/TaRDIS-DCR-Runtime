package dcr1.model.events;

import dcr1.common.data.types.Type;
import dcr1.common.events.Event;
import dcr1.model.ModelElement;

public sealed interface EventElement
    extends Event, ModelElement
    permits GenericEventElement, ComputationEventElement, InputEventElement, ReceiveEventElement {

  @Override
  MarkingElement marking();

  sealed interface MarkingElement
      extends Marking
      permits ImmutableMarkingElement {
    @Override
    default boolean hasExecuted() {
      return false;
    }
  }
}

