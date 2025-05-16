package dcr.model;

import dcr.common.DCRGraph;
import dcr.model.events.ComputationEventElement;
import dcr.model.events.EventElement;
import dcr.model.relations.ControlFlowRelationElement;
import dcr.model.relations.SpawnRelationElement;

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
