package dcr1.model.events;

import dcr1.common.data.types.Type;
import dcr1.common.data.values.Value;

public record ImmutableMarkingElement(boolean isPending, boolean isIncluded,
                                      Value value) implements EventElement.MarkingElement {

    // (for uniformity-sake) handle "record vs class"-naming mismatch for getters in Java
    // @Override
    // public Value value() {
    //     return this.value();
    // }

}