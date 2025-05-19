package dcr.runtime;

import dcr.common.events.Event;
import dcr.common.events.userset.values.UserSetVal;
import dcr.common.relations.Relation;
import dcr.runtime.elements.events.EventInstance;

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

    void addModifiedEvents(EventInstance event) {
        diff.add(event);
    }

    void setMarking(Event.Marking value) {
        this.marking = value;
    }

    public Event.Marking getMarking() {
        return this.marking;
    }

}
