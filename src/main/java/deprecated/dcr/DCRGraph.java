package deprecated.dcr;

public interface DCRGraph {

    Iterable<? extends Event> getEvents();

    Iterable<? extends InputAction> getInputActions();

    Iterable<ComputationAction> getComputationActions();

    Iterable<ComputationSendOperation> getSendOperations();

    Iterable<ReceiveOperation> getReceiveOperations();

    Iterable<Relation> getRelations();

    // TODO: Maybe add a method to get the relations of a specific type ???

}
