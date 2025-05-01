package dcr1.model;

import dcr1.common.data.types.Type;
import dcr1.common.DCRGraph;
import dcr1.model.events.ComputationEventElement;
import dcr1.model.events.EventElement;
import dcr1.model.relations.IControlFlowRelationElement;
import dcr1.model.relations.ISpawnRelationElement;

public sealed interface GraphModel
    extends ModelElement, DCRGraph
    permits RecursiveGraphModel {
  // TODO query methods - show/navigate graph model (in the future, maybe modify the graph in-place)

  @Override
  Iterable<? extends EventElement> events();

  @Override
  Iterable<? extends ComputationEventElement> computationEvents();

  @Override
  Iterable<? extends IControlFlowRelationElement> controlFlowRelations();

  @Override
  Iterable<? extends ISpawnRelationElement> spawnRelations();

  String unparse();

  // String toString(String indent);
}
