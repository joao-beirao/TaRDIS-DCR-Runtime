package dcr1.model.events;

import dcr1.common.data.types.Type;
import dcr1.common.data.values.Value;

public record ImmutableMarkingElement<T extends Type>(boolean isPending, boolean isIncluded,
                                      Value<T> value) implements EventElement.MarkingElement<T> {

    // (for uniformity-sake) handle "record vs class"-naming mismatch for getters in Java
    @Override
    public Value<T> getValue() {
        return this.value();
    }

}