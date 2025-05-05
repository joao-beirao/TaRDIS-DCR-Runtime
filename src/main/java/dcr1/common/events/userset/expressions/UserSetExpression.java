package dcr1.common.events.userset.expressions;

import dcr1.common.Environment;
import dcr1.common.events.Event;
import dcr1.common.data.values.Value;
import dcr1.common.events.userset.values.UserSetVal;
import dcr1.common.events.userset.values.UserVal;

/**
     * UserSetExpression provides a common marker interface for expressions describing a set of
     * swarm members.
     * <p>
     * {@link Event events} rely on user-set expressions to describe both <i>active</i> participants
     * (<i>initiators</i>initiators / <i>senders</i>) as well as <i>passive</i> participants
     * (<i>receivers</i>)
     */
public sealed interface UserSetExpression
        permits ReceiverExpr, RoleExpr, InitiatorExpr, SetDiffExpr, SetUnionExpr {
    UserSetVal eval(Environment<Value> valueEnv, Environment<UserVal> userEnv);
}
