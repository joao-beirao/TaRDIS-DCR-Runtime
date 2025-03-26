package dcr1.common.relations;

import dcr1.common.data.computation.BooleanExpression;

public interface Relation {
    String getSourceId();
    BooleanExpression getGuard();
}
