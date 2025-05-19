package dcr.model.events;

import dcr.common.events.Event;
import dcr.model.ModelElement;

public sealed interface EventElement
    extends Event, ModelElement
        permits ComputationEventElement,
                GenericEventElement,
                InputEventElement,
                ReceiveEventElement {

  @Override
  MarkingElement marking();

  String choreoElementUID();

  sealed interface MarkingElement
      extends Marking
      permits ImmutableMarkingElement {
    @Override
    default boolean hasExecuted() {
      return false;
    }
  }
}

