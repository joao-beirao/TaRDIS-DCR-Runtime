package dcr1.common.relations;

import dcr1.common.DCRGraph;

public interface SpawnRelation extends Relation{
  String triggerId();
  DCRGraph subGraph();
}