package dcr1.model.events;

import dcr1.common.data.types.Type;
import dcr1.common.events.Event;
import dcr1.model.ModelElement;

public sealed interface EventElement<T extends Type>
    extends Event<T>, ModelElement
    permits GenericEventElement, ComputationEventElement, InputEventElement, ReceiveEventElement {

  @Override
  MarkingElement<T> marking();

  sealed interface MarkingElement<T extends Type>
      extends Marking<T>
      permits ImmutableMarkingElement {
    @Override
    default boolean hasExecuted() {
      return false;
    }
  }
}

