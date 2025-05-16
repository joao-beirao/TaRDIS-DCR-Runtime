package dcr.runtime;

import java.util.Scanner;
import java.util.StringTokenizer;

public class UsageExtendedExample {

    // public static final class DummyCommunicationLayer implements CommunicationLayer {
    //
    //     private static final CommunicationLayer singleton = new DummyCommunicationLayer();
    //
    //     private DummyCommunicationLayer(){}
    //
    //     static CommunicationLayer instance() {return singleton;}
    //
    //     @Override
    //     public Set<dcr1.runtime.userset.User> uponSendRequest(String eventId, UserSetExprInstance receivers,
    //             Event.Marking<?> value, String uidExtension) {
    //         return DummyMembershipLayer.dummyInstance().evalUserSetExpr(receivers).stream().map(
    //                 DummyMembershipLayer.DummyNeighbour::user).collect(Collectors.toSet());
    //     }
    // }

    // public static void main(String[] args) {
    //     RecursiveGraphModel model =
    //             new GraphModelBuilder("graph_0").addLocalInputEvent("elem_0", "locId_0", "label_1",
    //                             new ImmutableMarkingElement<>(false, true, StringVal.undefined()))
    //                     .addComputationEvent("elem_1", "locId_1", "label_1",
    //                             StringLiteral.of("empty_string"), new User("Prosumer", "P1"),
    //                             new ImmutableMarkingElement<>(false, true, StringVal.undefined()))
    //                     .addLocalComputationEvent("elem_1", "locId_1", "label_1",
    //                             StringLiteral.of("empty_string"),
    //                             new ImmutableMarkingElement<>(false, true, StringVal.empty()))
    //                     .addLocalComputationEvent("elem_2", "locId_2", "label_2",
    //                             IntegerLiteral.of(3),
    //                             new ImmutableMarkingElement<>(false, true, IntegerVal.undefined()))
    //                     .addLocalComputationEvent("elem_6", "locId_6", "label_6",
    //                             EventValueDeref.of(EventIdExpr.of(EventIdVal.of("locId_2",
    //                                     EventType.of("label_2", IntegerType.singleton())))),
    //                             new ImmutableMarkingElement<>(false, true, IntegerVal.of(0)))
    //                     // cover data dependency from locId_6 to locId_2 (not including response
    //                     // yet)
    //                     .addControlFlowRelation("rel_elem_0", "locId_2", "locId_6",
    //                             ControlFlowRelation.Type.CONDITION)
    //                     .beginSpawn("spawn_1", "graph_1", "locId_1")
    //                     .addLocalComputationEvent("elem_3", "locId_3", "label_3",
    //                             BooleanLiteral.of(false),
    //                             new ImmutableMarkingElement<>(false, true, BooleanVal.undefined()))
    //                     .beginSpawn("spawn_2", "graph_2", "locId_2")
    //                     .addLocalComputationEvent("elem_4", "locId_4", "label_4",
    //                             BooleanLiteral.of(false),
    //                             new ImmutableMarkingElement<>(false, true, BooleanVal.undefined()))
    //                     .endSpawn()
    //                     .beginSpawn("spawn_3", "graph_3", "locId_3")
    //                     .addLocalComputationEvent("elem_5", "locId_5", "label_5",
    //                             BooleanLiteral.of(false),
    //                             new ImmutableMarkingElement<>(false, true, BooleanVal.of(true)))
    //                     .addLocalComputationEvent("elem_6", "locId_6", "label_6", RecordExpr.of(
    //                                     Record.ofEntries(Record.Field.of("f1",
    //                                                     IntegerLiteral.of(1)),
    //                                             Record.Field.of("f2", StringLiteral.of("2")),
    //                                             Record.Field.of("f3",
    //                                                     RecordExpr.of(Record.ofEntries(
    //                                                     Record.Field.of("f1", IntegerLiteral.of(1)),
    //                                                     Record.Field.of("f2", StringLiteral.of("2"))))))),
    //                             new ImmutableMarkingElement<>(false, true, RecordVal.undefined()))
    //                     .endSpawn()
    //                     .endSpawn()
    //                     .build();
    //     System.out.println(model);
    //
    //     // Record<ComputationExpression<?>> recordExpr
    //
    //     RecordExpr expr = RecordExpr.of(
    //             Record.ofEntries(Record.Field.of("f1", IntegerLiteral.of(1)),
    //                     Record.Field.of("f2", StringLiteral.of("2"))));
    //
    //     //
    //     RecordExpr expr1 = RecordExpr.of(
    //             Record.ofEntries(Record.Field.of("f1", IntegerLiteral.of(1)),
    //                     Record.Field.of("f2", StringLiteral.of("2")), Record.Field.of("f3",
    //                             RecordExpr.of(Record.ofEntries(
    //                                     Record.Field.of("f1", IntegerLiteral.of(1)),
    //                                     Record.Field.of("f2", StringLiteral.of("2")))))));
    //
    //     new ImmutableMarkingElement<>(false, true, RecordVal.undefined());
    //
    //     System.out.println();
    //     GraphRunner runner = new GraphRunner(dcr1.runtime.userset.User.of(
    //             "Prosumer", "p1"), DummyCommunicationLayer.instance());
    //     runner.init(model);
    //     // runner.executeInputEvent("locId_0", new NumberVal(1));
    //     // runner.executeInputEvent("locId_0", new StringVal("new"));
    //     System.err.println(runner.unparse(""));
    //
    //     readSystemIn(runner);
    //     // runner.executeComputationEvent("locId_2");
    //     // System.err.println(runner.unparse(""));
    //     // runner.executeComputationEvent("locId_6");
    //     // System.err.println(runner.unparse(""));
    //     // runner.executeComputationEvent("locId_1");
    //     // System.err.println(runner.unparse(""));
    //     // runner.executeComputationEvent("locId_2");
    //     // System.err.println(runner.unparse(""));
    // }

    static void readSystemIn(GraphRunner runner) {
        Scanner in = new Scanner(System.in);
        String line = "";

        while (!line.equals("quit")) {

            try {
                System.err.print("        <select event> % ");

                // While there is something to read
                line = in.nextLine();
                if (line == null) {
                    System.exit(1);
                }

                StringTokenizer tokenizer = new StringTokenizer(line);
                if (!tokenizer.hasMoreTokens()) {
                    break;
                }
                String event = tokenizer.nextToken();
                runner.executeComputationEvent(event);
                System.err.println(runner.unparse(""));

            } catch (Exception e) {
                // TODO: Handle event exception in here
                e.printStackTrace();
            }
        }
        System.exit(0);
    }
}
