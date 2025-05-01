package dcr1.runtime;

import dcr1.common.events.Event;
import dcr1.common.events.userset.values.UserSetVal;
import dcr1.common.relations.Relation;
import dcr1.runtime.elements.events.EventInstance;

import java.util.*;


public class ExecutionResult {

    private Event.Marking marking;

    private UserSetVal receiversExpr;
    private Set<String> receivers;
    // DCR Graph "Diff"
    private Set<EventInstance> diff;
    // TODO: Add relations when execute a spawn relation (WIP)
    private Set<Relation> newRelations;

    public ExecutionResult() {
        this.receivers = new HashSet<>();
        this.diff = new HashSet<>();
        this.newRelations = new HashSet<>();
    }

    public ExecutionResult(Set<String> receivers, Event.Marking value) {
        this.receivers = receivers;
        this.marking = value;
        this.diff = new HashSet<>();
        this.newRelations = new HashSet<>();
    }

    void addReceiverExpr(UserSetVal receiversExpr) {
        this.receiversExpr = receiversExpr;
    }

    Optional<UserSetVal> getReceiversExpr() {
        return Optional.ofNullable(receiversExpr);
    }

    void addReceiver(String receiver) {
        this.receivers.add(receiver);
    }

    void addAllReceivers(Set<String> receivers) {
        this.receivers.addAll(receivers);
    }

    void addModifiedEvents(EventInstance event) {
        diff.add(event);
    }

    void setMarking(Event.Marking value) {
        this.marking = value;
    }

    public Event.Marking getMarking() {
        return this.marking;
    }

    public Iterable<String> getReceivers() {
        return this.receivers;
    }


    public void addReceivers(UserSetVal receivers) {
        Map<String, List<String>> includeList;
        Map<String, List<String>> excludeList;

    }
}
