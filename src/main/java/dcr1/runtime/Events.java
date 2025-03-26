package dcr1.runtime;

import dcr1.common.data.computation.BooleanExpression;
import dcr1.common.data.computation.ComputationExpression;
import dcr1.common.data.types.Type;
import dcr1.common.data.values.Value;
import dcr1.common.events.userset.expressions.UserSetExpression;
import dcr1.model.events.ComputationEventElement;
import dcr1.model.events.EventElement;
import dcr1.model.events.InputEventElement;
import dcr1.model.events.ReceiveEventElement;
import dcr1.runtime.events.ComputationEventInstance;
import dcr1.runtime.events.EventInstance;
import dcr1.runtime.events.InputEventInstance;
import dcr1.runtime.events.ReceiveEventInstance;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;

final class Events {
    public static <T extends Type> ComputationInstance<T> newLocalComputationInstance(
            String globalId, ComputationEventElement<T> baseElement) {
        return new ComputationInstance<>(globalId, baseElement);
    }

    public static <T extends Type> ComputationInstance<T> newComputationInstance(String globalId,
            ComputationEventElement<T> baseElement) {
        return new ComputationInstance<>(globalId, baseElement);
    }

    public static <T extends Type> InputInstance<T> newLocalInputInstance(String globalId,
            InputEventElement<T> baseElement) {
        return InputInstance.of(globalId, baseElement);
    }

    public static <T extends Type> InputInstance<T> newInputInstance(String globalId,
            InputEventElement<T> baseElement) {
        return InputInstance.of(globalId, baseElement);
    }

    public static ReceiveInstance<?> newReceiveInstance(String globalId,
            ReceiveEventElement<?> baseElement) {
        return new ReceiveInstance<>(globalId, baseElement);
    }
}

/**
 * @param <T>
 */
sealed abstract class GenericEventInstance<T extends Type>
        implements EventInstance<T>
        permits ComputationInstance, InputInstance, ReceiveInstance {

    private static final class MutableMarking<T extends Type>
            implements Marking<T>, Serializable {

        @Serial
        private static final long serialVersionUID = 1894241710394083158L;
        private boolean hasExecuted, isPending, isIncluded;
        private Value<T> value;

        static <T extends Type> MutableMarking<T> of(EventElement.MarkingElement<T> marking) {
            return new MutableMarking<>(marking.hasExecuted(), marking.isPending(),
                    marking.isIncluded(), marking.getValue());
        }

        static <T extends Type> MutableMarking<T> of(Marking<T> other) {
            return new MutableMarking<>(other.hasExecuted(), other.isPending(),
                    other.isIncluded(), other.getValue());
        }

        private MutableMarking(boolean hasExecuted, boolean isPending, boolean isIncluded,
                Value<T> value) {
            this.hasExecuted = hasExecuted;
            this.isPending = isPending;
            this.isIncluded = isIncluded;
            this.value = value;
        }

        @Override
        public boolean hasExecuted() {return hasExecuted;}

        @Override
        public boolean isPending() {return isPending;}

        @Override
        public boolean isIncluded() {return isIncluded;}

        @Override
        public Value<T> getValue() {return value;}

        @Override
        public String toString() {
            return String.format("(%b, %b, %b, %s)", hasExecuted, isPending, isIncluded, value);
        }

        public String unparse() {
            return "Marking[" + "hasExecuted=" + hasExecuted + ", isPending=" + isPending +
                    ", isIncluded=" + isIncluded + ", value=" + value.unparse() + ']';
        }
    }

    private final String globalId;
    private final EventElement<T> baseElement;
    private final MutableMarking<T> marking;

    // TODO [revisit] not accounting for subtyping - it should - revisit Type
    // TODO [revisit] proper IllegalValueType exception
    static <T extends Type> void trySetValue(MutableMarking<T> marking, Value<?> value) {
        if (!value.type().getClass().equals(marking.valueType().getClass())) {
            throw new RuntimeException("Bad Input val");
        }
        @SuppressWarnings("unchecked") var typedValue = (Value<T>) value;
        marking.value = typedValue;
    }

    GenericEventInstance(String globalId, EventElement<T> baseElement) {
        this.globalId = globalId;
        this.baseElement = baseElement;
        this.marking = MutableMarking.of(baseElement.marking());
    }

    @Override
    public String getGlobalId() {
        return globalId;
    }

    @Override
    public String localId() {
        return baseElement.localId();
    }

    @Override
    public String label() {
        return baseElement.label();
    }

    @Override
    public Marking<T> marking() {
        return marking;
    }

    @Override
    public Optional<? extends UserSetExpression> remoteParticipants() {
        return baseElement.remoteParticipants();
    }

    @Override
    public BooleanExpression constraint() {
        return baseElement.constraint();
    }

    protected final EventElement<T> getBaseElement() {
        return baseElement;
    }

    void onExecuted(Value<?> value) {
        trySetValue(marking, value);
        marking.hasExecuted = true;
        marking.isPending = false;
    }

    void onResponse() {marking.isPending = true;}

    void onInclude() {marking.isIncluded = true;}

    void onExclude() {marking.isIncluded = false;}

    abstract String unparse(String indentation);
}

/**
 * @param <T>
 */
final class ComputationInstance<T extends Type>
        extends GenericEventInstance<T>
        implements ComputationEventInstance<T> {

    ComputationInstance(String globalId, ComputationEventElement<T> baseElement) {
        super(globalId, baseElement);
    }

    // ComputationInstance(String globalId, ComputationEventElement<T> baseElement) {
    //     this(globalId, baseElement, null);
    // }

    @Override
    public ComputationExpression<T> getComputationExpression() {
        return ((ComputationEventElement<T>) getBaseElement()).getComputationExpression();
    }

    @Override
    public Optional<? extends UserSetExpression> receivers() {return getBaseElement().remoteParticipants();}

    @Override
    public String toString() {
        return String.format("%s - %s(%s: %s) [%s] (%s) [ -> %s]", getGlobalId(),
                marking().toStringPrefix(), localId(), label(),
                getComputationExpression(), getValue(), receivers());
    }

    public String unparse(String indentation) {
        return String.format("%s%s - (%s: %s) [%s] %s", indentation, getGlobalId(), localId(),
                label(), getComputationExpression().unparse(), marking());
    }
}

/**
 * @param <T>
 */
final class InputInstance<T extends Type>
        extends GenericEventInstance<T>
        implements InputEventInstance<T> {

    private InputInstance(String globalId, InputEventElement<T> baseElement) {
        super(globalId, baseElement);
    }

    static <T extends Type> InputInstance<T> of(String globalId, InputEventElement<T> baseElement) {
        return new InputInstance<>(globalId, baseElement);
    }
    //
    // static <T extends Type> InputInstance<T> of(String globalId, InputEventElement<T> baseElement) {
    //     return new InputInstance<>(globalId, baseElement, null);
    // }

    @Override
    public Optional<? extends UserSetExpression> receivers() {return getBaseElement().remoteParticipants();}

    @Override
    public String toString() {
        return String.format("%s - %s(%s: %s) [?:%s] (%s) [ -> %s ]", getGlobalId(),
                marking().toStringPrefix(), localId(),
                label(), valueType(), getValue(), receivers());
    }

    @Override
    public String unparse(String indentation) {
        return String.format("%sInput: %s  ( %s )", indentation, getGlobalId(), getValue());
    }
}

/**
 * @param <T>
 */
final class ReceiveInstance<T extends Type>
        extends GenericEventInstance<T>
        implements ReceiveEventInstance<T> {


    ReceiveInstance(String globalId, EventElement<T> baseElement) {
        super(globalId, baseElement);
    }

    @Override
    public String toString() {
        return String.format("%s - %s(%s: %s) [(Rx): %s] (%s) [ %s ->]", getGlobalId(),
                marking().toStringPrefix(), localId(),
                label(), valueType(), getValue(), getSenderExpr());
    }

    @Override
    String unparse(String indent) {
        return String.format("%sReceive: %s  ( %s )", indent, getGlobalId(), getValue());
    }

    // FIXME .get()
    @Override
    public UserSetExpression getSenderExpr() {
        return remoteParticipants().get();
    }
}