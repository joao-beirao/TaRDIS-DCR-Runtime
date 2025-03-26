package dcr;

import dcr.ast.typing.Type;

public interface Event {

    String getId();

    String getLabel();

    String getAction();

    String getKind();

    String getInitiator();

    Type getTypeExpression();

    /**
     * Returns a copy of this event's {@link EventMarking marking}.
     * </p>
     * Changing the state of the returned object <em>will not</em> affect the
     * associated event's marking, and vice-versa.
     * 
     * @return a copy of this Event's {@link EventMarking marking}
     */
    EventMarking getMarking();

    // boolean isEnabled();

    @Override
    int hashCode();

    @Override
    boolean equals(Object obj);

    @Override
    String toString();

    String unparse();

}
