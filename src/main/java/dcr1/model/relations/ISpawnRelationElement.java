package dcr1.model.relations;

import dcr1.common.relations.SpawnRelation;
import dcr1.model.GraphModel;

public sealed interface ISpawnRelationElement extends RelationElement, SpawnRelation
    permits SpawnRelationElement {

  @Override
  GraphModel getSubgraph();
}
