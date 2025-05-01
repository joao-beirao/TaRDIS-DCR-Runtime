package dcr1.runtime;

import dcr1.common.data.computation.BooleanExpression;
import dcr1.common.data.computation.ComputationExpression;
import dcr1.common.data.values.Value;
import dcr1.common.events.userset.expressions.UserSetExpression;
import dcr1.model.events.ComputationEventElement;
import dcr1.model.events.EventElement;
import dcr1.model.events.InputEventElement;
import dcr1.model.events.ReceiveEventElement;
import dcr1.runtime.elements.events.ComputationEventInstance;
import dcr1.runtime.elements.events.EventInstance;
import dcr1.runtime.elements.events.InputEventInstance;
import dcr1.runtime.elements.events.ReceiveEventInstance;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;

final class Events {
    public static ComputationInstance newLocalComputationInstance(
            String globalId, ComputationEventElement baseElement) {
        return new ComputationInstance(globalId, baseElement);
    }

    public static ComputationInstance newComputationInstance(String globalId,
            ComputationEventElement baseElement) {
        return new ComputationInstance(globalId, baseElement);
    }

    public static InputInstance newLocalInputInstance(String globalId,
            InputEventElement baseElement) {
        return InputInstance.of(globalId, baseElement);
    }

    public static InputInstance newInputInstance(String globalId,
            InputEventElement baseElement) {
        return InputInstance.of(globalId, baseElement);
    }

    public static ReceiveInstance newReceiveInstance(String globalId,
            ReceiveEventElement baseElement) {
        return new ReceiveInstance(globalId, baseElement);
    }
}

/**
 * @param
 */
sealed abstract class GenericEventInstance
        implements EventInstance
        permits ComputationInstance, InputInstance, ReceiveInstance {

    private static final class MutableMarking
            implements Marking, Serializable {

        @Serial
        private static final long serialVersionUID = 1894241710394083158L;
        private boolean hasExecuted, isPending, isIncluded;
        private Value value;

        static MutableMarking of(EventElement.MarkingElement marking) {
            return new MutableMarking(marking.hasExecuted(), marking.isPending(),
                    marking.isIncluded(), marking.value());
        }

        static MutableMarking of(Marking other) {
            return new MutableMarking(other.hasExecuted(), other.isPending(),
                    other.isIncluded(), other.value());
        }

        private MutableMarking(boolean hasExecuted, boolean isPending, boolean isIncluded,
                Value value) {
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
        public Value value() {return value;}

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
    private final EventElement baseElement;
    private final MutableMarking marking;

    // TODO [revisit] not accounting for subtyping - it should - revisit Type
    // TODO [revisit] proper IllegalValueType exception
    static void trySetValue(MutableMarking marking, Value value) {
        if (!value.type().getClass().equals(marking.valueType().getClass())) {
            throw new RuntimeException("Bad Input val");
        }
        marking.value = value;
    }

    GenericEventInstance(String globalId, EventElement baseElement) {
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
    public Marking marking() {
        return marking;
    }

    @Override
    public EventElement baseElement() {
        return baseElement;
    }

    @Override
    public Optional<? extends UserSetExpression> remoteParticipants() {
        return baseElement.remoteParticipants();
    }

    void onExecuted(Value value) {
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
 * @param
 */
final class ComputationInstance
        extends GenericEventInstance
        implements ComputationEventInstance {

    ComputationInstance(String globalId, ComputationEventElement baseElement) {
        super(globalId, baseElement);
    }

    @Override
    public ComputationExpression getComputationExpression() {
        return ((ComputationEventElement) baseElement()).getComputationExpression();
    }

    @Override
    public Optional<? extends UserSetExpression> receivers() {return baseElement().remoteParticipants();}

    @Override
    public String toString() {
        return String.format("%s - %s(%s: %s) [%s] (%s) [ -> %s]", getGlobalId(),
                marking().toStringPrefix(), localId(), label(),
                getComputationExpression(), value(), receivers());
    }

    public String unparse(String indentation) {
        return String.format("%s%s - (%s: %s) [%s] %s", indentation, getGlobalId(), localId(),
                label(), getComputationExpression().unparse(), marking());
    }
}

/**
 * @param
 */
final class InputInstance
        extends GenericEventInstance
        implements InputEventInstance {

    private InputInstance(String globalId, InputEventElement baseElement) {
        super(globalId, baseElement);
    }

    static InputInstance of(String globalId, InputEventElement baseElement) {
        return new InputInstance(globalId, baseElement);
    }

    @Override
    public Optional<? extends UserSetExpression> receivers() {return baseElement().remoteParticipants();}

    @Override
    public String toString() {
        return String.format("%s - %s(%s: %s) [?:%s] (%s) [ -> %s ]", getGlobalId(),
                marking().toStringPrefix(), localId(),
                label(), valueType(), value(), receivers());
    }

    @Override
    public String unparse(String indentation) {
        return String.format("%sInput: %s  ( %s )", indentation, getGlobalId(), value());
    }
}

/**
 * @param
 */
final class ReceiveInstance
        extends GenericEventInstance
        implements ReceiveEventInstance {


    ReceiveInstance(String globalId, EventElement baseElement) {
        super(globalId, baseElement);
    }

    @Override
    public String toString() {
        return String.format("%s - %s(%s: %s) [(Rx): %s] (%s) [ %s ->]", getGlobalId(),
                marking().toStringPrefix(), localId(),
                label(), valueType(), value(), getSenderExpr());
    }

    @Override
    String unparse(String indent) {
        return String.format("%sReceive: %s  ( %s )", indent, getGlobalId(), value());
    }

    // FIXME .get()
    @Override
    public UserSetExpression getSenderExpr() {
        return remoteParticipants().get();
    }
}