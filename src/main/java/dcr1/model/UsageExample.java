package dcr1.model;

import dcr1.common.data.computation.BooleanExpression;
import dcr1.common.data.computation.IntegerLiteral;
import dcr1.common.data.computation.BooleanLiteral;
import dcr1.common.data.computation.StringLiteral;
import dcr1.common.data.values.BooleanVal;
import dcr1.common.data.values.IntegerVal;
import dcr1.common.data.values.StringVal;
import dcr1.model.events.ImmutableMarkingElement;

class UsageExample {
    public static void main(String[] args) {
        RecursiveGraphModel model = new GraphModelBuilder("graph_0")
                .addLocalComputationEvent("elem_1", "locId_1", "label_1", StringLiteral.empty(),
                        new ImmutableMarkingElement(false, true, null),
                        BooleanLiteral.of(true),BooleanLiteral.of(true))
                .addLocalComputationEvent("elem_2", "locId_2", "label_2", IntegerLiteral.of(3),
                        new ImmutableMarkingElement(false, true, null),
                        BooleanLiteral.of(true), BooleanLiteral.of(true))
                .beginSpawn("spawn_1", "graph_1", "locId_1", BooleanLiteral.of(true))
                .addLocalComputationEvent("elem_3", "locId_3", "label_3", BooleanLiteral.of(true),
                        new ImmutableMarkingElement(false, true, null),
                        BooleanLiteral.of(true), BooleanLiteral.of(true))
                .beginSpawn("spawn_2", "graph_2", "locId_2", BooleanLiteral.of(true))
                .addLocalComputationEvent("elem_4", "locId_4", "label_4", BooleanLiteral.of(false),
                        new ImmutableMarkingElement(false, true, null),
                        BooleanLiteral.of(true), BooleanLiteral.of(true))
                .endSpawn()
                .beginSpawn("spawn_3", "graph_3", "locId_3", BooleanLiteral.of(true))
                .addLocalComputationEvent("elem_5", "locId_5", "label_5", BooleanLiteral.of(false),
                        new ImmutableMarkingElement(false, true, null),
                        BooleanLiteral.of(true), BooleanLiteral.of(true))
                .endSpawn()
                .endSpawn()
                .build();
        System.out.println(model);
    }
}
