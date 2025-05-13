package dcr1.model.relations;

import dcr1.common.relations.SpawnRelation;
import dcr1.model.GraphModel;

public sealed interface SpawnRelationElement
        extends RelationElement, SpawnRelation
        permits SpawnElement {

  @Override
  GraphModel subGraph();
}
