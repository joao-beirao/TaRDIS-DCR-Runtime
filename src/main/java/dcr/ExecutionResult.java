package dcr;

import java.util.Set;
import java.util.HashSet;


public class ExecutionResult {

    private EventMarking marking;
    // private Value value;
    private Set<String> receivers;
    // DCR Graph "Diff"
    private Set<Event> diff;
    // TODO: Add relations when execute a spawn relation (WIP)
    private Set<Relation> newRelations;

    public ExecutionResult() {
        this.receivers = new HashSet<>();
        this.diff = new HashSet<>();
        this.newRelations = new HashSet<>();
    }

    public ExecutionResult(Set<String> receivers, EventMarking value) {
        this.receivers = receivers;
        this.marking = value;
        this.diff = new HashSet<>();
        this.newRelations = new HashSet<>();
    }

    void addReceiver(String receiver) {
        this.receivers.add(receiver);
    }

    void addAllReceivers(Set<String> receivers) {
        this.receivers.addAll(receivers);
    }

    void addModifiedEvents(Event event) {
        diff.add(event);
    }

    void setMarking(EventMarking value) {
        this.marking = value;
    }

    public EventMarking getMarking() {
        return this.marking;
    }

    public Iterable<String> getReceivers() {
        return this.receivers;
    }


}
