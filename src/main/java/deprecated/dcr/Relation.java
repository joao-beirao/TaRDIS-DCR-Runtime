package deprecated.dcr;

public final class Relation {

    public enum Type {
        INCLUDE, EXCLUDE, RESPONSE, CONDITION, MILESTONE
    }

    public final Type relationType;
    public final Event source;
    public final Event target;

    public Relation(Type relationType, Event source, Event target) {
        this.relationType = relationType;
        this.source = source;
        this.target = target;
    }
}