package dcr1.model;

import dcr1.common.DCRGraph;
import dcr1.model.events.ComputationEventElement;
import dcr1.model.events.EventElement;
import dcr1.model.relations.ControlFlowRelationElement;
import dcr1.model.relations.SpawnRelationElement;

public sealed interface GraphModel
    extends ModelElement, DCRGraph
    permits RecursiveGraphModel {
  // TODO query methods - show/navigate graph model (in the future, maybe modify the graph in-place)

  @Override
  Iterable<? extends EventElement> events();

  @Override
  Iterable<? extends ComputationEventElement> computationEvents();

  @Override
  Iterable<? extends ControlFlowRelationElement> controlFlowRelations();

  @Override
  Iterable<? extends SpawnRelationElement> spawnRelations();

  String unparse();

  // String toString(String indent);
}
