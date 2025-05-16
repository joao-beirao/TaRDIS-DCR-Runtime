package dcr.model.relations;

import dcr.common.relations.SpawnRelation;
import dcr.model.GraphModel;

public sealed interface SpawnRelationElement
        extends RelationElement, SpawnRelation
        permits SpawnElement {

  @Override
  GraphModel subGraph();
}
