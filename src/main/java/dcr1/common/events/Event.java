package dcr1.common.events;

import dcr1.common.data.computation.BooleanExpression;
import dcr1.common.data.types.Type;
import dcr1.common.data.values.Value;
import dcr1.common.events.userset.expressions.UserSetExpression;

import java.util.Optional;

// TODO [javadoc]
// TODO [revisit] consider adding a getInitiator()

/**
 * Common interface for any object representing a DCR event.
 *
 * @param <T>
 *         the {@link Type type} of {@link Value value} stored by this event.
 */
public interface Event<T extends Type> {

    /**
     * Common interface for any object representing a DCR marking.
     * <p>
     * DCR {@link Event events} are <i>stateful</i> entities. A Marking captures the mutable state
     * of an event, reflecting its current throughout execution.
     *
     * @param <T>
     *         the {@link Type type} of {@link Value value} stored by this marking.
     */
    interface Marking<T extends Type> {
        /**
         * Indicates whether the associated event has already been <i>executed</i> (at any given
         * point in time).
         *
         * @return <code>true</code> if the associated event has already been executed;
         *         <code>false</code> otherwise.
         */
        boolean hasExecuted();

        /**
         * Indicates whether the associated event is currently <i>pending</i>.
         *
         * @return <code>true</code> if the associated event is currently pending;
         *         <code>false</code> otherwise.
         */
        boolean isPending();

        /**
         * Indicates whether the associated event is currently <i>included</i>. An event is said to
         * be
         * <i>excluded</i> otherwise.
         *
         * @return <code>true</code> if the associated event is currently included;
         *         <code>false</code> otherwise.
         */
        boolean isIncluded();

        /**
         * Returns the current {@link Value value} of the associated event.
         *
         * @return the current {@link Value value} of the associated event.
         */
        Value<T> getValue();

        /**
         * Returns the {@link Type type} of {@link Value values} stored by the associated event.
         * <p>
         * The type of event stored by each event is fixed at design time.
         *
         * @return the type of values stored by the associated event.
         */
        default T valueType() {
            return getValue().type();
        }

        // TODO [rethink] useful, but maybe not the right place
        default String toStringPrefix() {
            if (isPending()) {
                if (!isIncluded()) {return "!%";}
                else {return "!";}
            }
            if (!isIncluded()) {return "%";}
            return "";
        }
    }

    /**
     * Returns the local id assigned to an event.
     *
     * @return the local id assigned to an event at design time.
     */
    String localId();


    /**
     * Returns the label assigned to this event.
     *
     * @return the label assigned to this event.
     */
    String label();

    /**
     * Returns a marking object reflecting the current state of this event.
     *
     * @return a marking object reflecting the current state of this event.
     */
    Marking<T> marking();

    /**
     * Returns a user-set expression describing the swarm members that participate in this event
     * as <i>receivers<i></> in an interaction.
     *
     * @return an Optional describing the receivers, if this event is an interaction; an empty
     * Optional otherwise;
     */
    Optional<? extends UserSetExpression> remoteParticipants();

    BooleanExpression constraint();

    default boolean hasExecuted() {return marking().hasExecuted();}

    default boolean isPending() {return marking().isPending();}

    default boolean isIncluded() {return marking().isIncluded();}

    default Value<T> getValue() {return marking().getValue();}

    default T valueType() {return marking().valueType();}
}