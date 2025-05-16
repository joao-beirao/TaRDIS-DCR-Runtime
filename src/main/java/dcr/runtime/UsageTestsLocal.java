package dcr.runtime;

import protocols.application.DummyMembershipLayer;
import dcr.common.Record;
import dcr.common.data.values.*;
import dcr.common.events.Event;
import dcr.common.events.userset.values.UserSetVal;
import dcr.common.events.userset.values.UserVal;
import dcr.runtime.communication.CommunicationLayer;

import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class UsageTestsLocal {

    private static final class DummyCommunicationLayer
            implements CommunicationLayer {

        private static final CommunicationLayer singleton = new DummyCommunicationLayer();

        private DummyCommunicationLayer() {}

        static CommunicationLayer instance() {return singleton;}

        @Override
        public Set<UserVal> uponSendRequest(UserVal requester, String eventId, UserSetVal receivers,
                Event.Marking marking, String uidExtension) {
            return DummyMembershipLayer.instance()
                    .resolveParticipants(receivers)
                    .stream()
                    .map(DummyMembershipLayer.Neighbour::user)
                    .collect(Collectors.toSet());
        }
    }

    // public static void main(String[] args) {
    //     simpleSendReceiveWithSpawn();
    // }

    // FIXME
    // look into this - EventVal.undefined("E1",GenericStringType.singleton())
    // should be - EventVal.undefined("E1", EventType.of("E1", GenericStringType.singleton()))
    // program not detecting the difference - fails at runtime
    // public static void testSimpleTriggerDataDependency() {
    //     RecursiveGraphModel model =
    //             new GraphModelBuilder().addLocalComputationEvent("elem_1", "e1",
    //                             "E1",
    //                             StringLiteral.of("a_string"),
    //                             new ImmutableMarkingElement(false, true, StringVal.undefined
    //                             ()))
    //                     .beginSpawn("spawn_1", "graph1", "e1")
    //                     .addLocalComputationEvent("elem_2", "e2", "E2",
    //                             EventIdExpr.of(EventIdVal.of("@trigger", EventType.of("E1",
    //                                     GenericStringType.singleton()))),
    //                             new ImmutableMarkingElement(false, true,
    //                                     EventVal.undefined("E1",
    //                                             GenericStringType.singleton())))
    //                     .endSpawn()
    //                     .build();
    //
    //     GraphRunner runner = new GraphRunner(dcr1.runtime.participants.User.of("Prosumer", "p1"),
    //             DummyCommunicationLayer.instance());
    //     runner.init(model);
    //     System.err.println(runner);
    //     readSystemIn(runner);
    // }


    // public static void testComputationWithRecord() {
    //     RecursiveGraphModel model = new GraphModelBuilder()
    //             // e1 [?:Int]
    //             .addLocalComputationEvent("event_1", "e1", EventType.of("E1"), RecordExpr.of(
    //                             Record.ofEntries(Record.Field.of("kw", IntLiteral.of(2)))),
    //                     new ImmutableMarkingElement(false, true, RecordVal.of(
    //                             Record.ofEntries(Record.Field.of("kw", null)))),
    //                     BoolLiteral.of(true), BoolLiteral.of(true)).build();
    //     GraphRunner runner =
    //             new GraphRunner(UserVal.of("Prosumer", "p1"), DummyCommunicationLayer.instance());
    //     runner.init(model);
    //     //
    //     System.err.println("== Initial State\n==");
    //     System.err.println(runner);
    //     readSystemIn(runner);
    // }
    //
    //
    // // e1 [?:Int]
    // // e2 [e1.value > 0 ? true : false]
    // public static void testConditionalExprWithDataDep() {
    //     RecursiveGraphModel model = new GraphModelBuilder()
    //             // e1 [?:Int]
    //             .addLocalInputEvent("event_1", "e1", EventType.of("E1"),
    //                     new ImmutableMarkingElement(false, true, null),
    //                     BoolLiteral.of(true), BoolLiteral.of(true))
    //             // e2 [e1.value > 0 ? true : false]
    //             .addLocalComputationEvent("event", "e2", EventType.of("E2"), IfThenElseExpr.of(
    //                             IntegerCompareExpr.ofGt(EventValueDeref.of(RefExpr.of(
    //                                             EventIdVal.of("e1"))),
    //                                     IntLiteral.of(IntVal.of(0))),
    //                             BoolLiteral.of(true),
    //                             BoolLiteral.of(false)),
    //                     new ImmutableMarkingElement(false, true, null),
    //                     BoolLiteral.of(true), BoolLiteral.of(true)).build();
    //     GraphRunner runner =
    //             new GraphRunner(UserVal.of("Prosumer", "p1"), DummyCommunicationLayer.instance());
    //     runner.init(model);
    //     //
    //     System.err.println("== Initial State\n==");
    //     System.err.println(runner);
    //     readSystemIn(runner);
    // }
    //
    //
    // // e1 [{kw:1, t:3}]
    // // e2 [e1.value.kw]
    // public static void testRecordExprWithDataDep() {
    //     RecursiveGraphModel model = new GraphModelBuilder()
    //             // e1 [{kw:1, t:3}]
    //             .addLocalComputationEvent("event_1", "e1", EventType.of("E1"), RecordExpr.of(
    //                             Record.ofEntries(Record.Field.of("kw", IntLiteral.of(1)),
    //                                     Record.Field.of("t", IntLiteral.of(3)))),
    //                     new ImmutableMarkingElement(false, true, null),
    //                     BoolLiteral.of(true), BoolLiteral.of(true))
    //             // e2 [e1.value.kw]
    //             .addLocalComputationEvent("event", "e2", EventType.of("E2"), RecordFieldDeref.of(
    //                             EventValueDeref.of(RefExpr.of(EventIdVal.of("e1"))), "kw"),
    //                     new ImmutableMarkingElement(false, true, null),
    //                     BoolLiteral.of(true), BoolLiteral.of(true)).build();
    //     GraphRunner runner =
    //             new GraphRunner(UserVal.of("Prosumer", "p1"), DummyCommunicationLayer.instance());
    //     runner.init(model);
    //     //
    //     System.err.println("== Initial State\n==");
    //     System.err.println(runner);
    //     readSystemIn(runner);
    // }
    //
    //
    // // e1 ['a_string']
    // // ;
    // // e1 -->> {
    // //   e2 [@trigger]
    // //   e3 [e2.value]
    // //   e4 [e2.value.value]
    // // }
    // public static void testSimpleTriggerDataDependency() {
    //     RecursiveGraphModel model = new GraphModelBuilder()
    //
    //
    //             .addLocalInputEvent("0_RxO", "cp", EventType.of("createProsumer"),
    //                     new ImmutableMarkingElement(false, true, null),
    //                     BinaryLogicOpExpr.and(BoolLiteral.of(true),
    //                             BinaryLogicOpExpr.or(BoolLiteral.of(true), EqualsExpr.of(
    //                                     RecordFieldDeref.of(EventValueDeref.of(RefExpr.of(
    //                                                     EventIdVal.of("@self"))),
    //                                             "P#cid"), IntLiteral.of(0)))), BoolLiteral.of(true))
    //
    //             // e1 ['a_string']
    //             .addLocalComputationEvent("elem_1", "e1", EventType.of("E1"), StringLiteral.of("a_string"),
    //                     new ImmutableMarkingElement(false, true, null),
    //                     BoolLiteral.of(true), BoolLiteral.of(true)).beginSpawn("spawn_1", "graph1", "e1", BoolLiteral.of(true))
    //             //   e2 [@trigger]
    //             .addLocalComputationEvent("elem_2", "e2", EventType.of("E2"), RefExpr.of(
    //                             EventIdVal.of("@trigger")),
    //                     new ImmutableMarkingElement(false, true, null),
    //                     BoolLiteral.of(true), BoolLiteral.of(true))
    //             //   e3 [e2.value]
    //             .addLocalComputationEvent("elem_3", "e3", EventType.of("E3"), EventValueDeref.of(RefExpr.of(
    //                             EventIdVal.of("e2"))),
    //                     new ImmutableMarkingElement(false, true, null),
    //                     BoolLiteral.of(true), BoolLiteral.of(true))
    //             //   e4 [e2.value.value]
    //             .addLocalComputationEvent("elem_4", "e4", EventType.of("E4"), EventValueDeref.of(
    //                             EventValueDeref.of(RefExpr.of(EventIdVal.of("e2")))),
    //                     new ImmutableMarkingElement(false, true, null),
    //                     BoolLiteral.of(true), BoolLiteral.of(true)).endSpawn().build();
    //
    //     GraphRunner runner =
    //             new GraphRunner(UserVal.of("Prosumer", "p1"), DummyCommunicationLayer.instance());
    //     runner.init(model);
    //     System.err.println(runner);
    //     readSystemIn(runner);
    // }
    //
    //
    // // e1 ['a_string']
    // // e2 [e1]
    // // e3 [e2.value.value]
    // public static void testSimpleNestedDataDependency() {
    //     RecursiveGraphModel model = new GraphModelBuilder()
    //
    //
    //             // e1 ['a_string']
    //             .addLocalComputationEvent("elem_1", "e1", EventType.of("E1"), BoolLiteral.of(true),
    //                     new ImmutableMarkingElement(false, true, BoolVal.of(false)),
    //                     BoolLiteral.of(true), BoolLiteral.of(true))
    //             // e2 [e1]
    //             .addLocalComputationEvent("elem_2", "e2", EventType.of("E2"), RefExpr.of(
    //                             EventIdVal.of("e1")),
    //                     new ImmutableMarkingElement(false, true,
    //                             null), BoolLiteral.of(true), BoolLiteral.of(true))
    //             // e3 [e2.value.value]
    //             .addLocalComputationEvent("elem_3", "e3", EventType.of("E3"), EventValueDeref.of(
    //                             EventValueDeref.of(RefExpr.of(EventIdVal.of("e2")))),
    //                     new ImmutableMarkingElement(false, true, null),
    //                     BoolLiteral.of(true), BoolLiteral.of(true)).build();
    //     GraphRunner runner =
    //             new GraphRunner(UserVal.of("Prosumer", "p1"), DummyCommunicationLayer.instance());
    //     runner.init(model);
    //     //
    //     System.err.println(runner);
    //     readSystemIn(runner);
    // }
    //
    //
    // // e1 ['a_string']
    // // e2 [e1.value]
    // public static void testSimpleDataDependency() {
    //     RecursiveGraphModel model =
    //             new GraphModelBuilder().addLocalComputationEvent("elem_1", "e1", EventType.of("E1"),
    //                             StringLiteral.of("a_string"),
    //                             new ImmutableMarkingElement(false, true, null),
    //                             BoolLiteral.of(true), BoolLiteral.of(true))
    //                     .addLocalComputationEvent("elem_2", "e2", EventType.of("E2"), EventValueDeref.of(
    //                                     RefExpr.of(EventIdVal.of("e1"))),
    //                             new ImmutableMarkingElement(false, true, null),
    //                             BoolLiteral.of(true), BoolLiteral.of(true))
    //                     .build();
    //     GraphRunner runner =
    //             new GraphRunner(UserVal.of("Prosumer", "p1"), DummyCommunicationLayer.instance());
    //     runner.init(model);
    //     //
    //     System.err.println(runner);
    //     readSystemIn(runner);
    // }
    //
    // //
    //
    // public static void testSimpleSpawnDummyReceivers() {
    //     RecursiveGraphModel model =
    //             new GraphModelBuilder()
    //
    //
    //                     .addComputationEvent("elem_1", "locId_1", EventType.of("label_1"),
    //                             StringLiteral.of("a_string"), new SetUnionExpr(
    //                                     List.of(RoleExpr.of("Prosumer", "p2"),
    //                                             RoleExpr.of("Prosumer", "p3"))),
    //                             new ImmutableMarkingElement(false, true, null),
    //                             BoolLiteral.of(true), BoolLiteral.of(true))
    //
    //
    //                     .addComputationEvent("elem_1", "locId_1", EventType.of("label_1"),
    //                             StringLiteral.of("a_string"), new SetUnionExpr(
    //                                     List.of(RoleExpr.of("Prosumer",
    //                                                     Record.ofEntries(Record.Field.of(
    //                                                             "id", IntLiteral.of(1)))),
    //                                             RoleExpr.of("Prosumer", "p3"))),
    //                             new ImmutableMarkingElement(false, true, null),
    //                             BoolLiteral.of(true), BoolLiteral.of(true))
    //
    //                     .beginSpawn("spawn_1", "graph1", "locId_1", BoolLiteral.of(true))
    //                     .addLocalComputationEvent("elem_2", "locId_2", EventType.of("label_2"),
    //                             BoolLiteral.of(false),
    //                             new ImmutableMarkingElement(false, true, null),
    //                             BoolLiteral.of(true), BoolLiteral.of(true))
    //                     .endSpawn()
    //                     .build();
    //
    //     GraphRunner runner =
    //             new GraphRunner(UserVal.of("Prosumer", "p1"), DummyCommunicationLayer.instance());
    //     runner.init(model);
    //     System.err.println(runner);
    //     readSystemIn(runner);
    // }
    //
    //
    // // e1 [?:String] (<undefined>)
    // // e2 [e1.value] ('in')
    // public static void testInputSimpleDataDependency() {
    //     RecursiveGraphModel model =
    //             new GraphModelBuilder().addLocalInputEvent("elem_1", "e1", EventType.of("E1"),
    //                             new ImmutableMarkingElement(false, true, null),
    //                             BoolLiteral.of(true), BoolLiteral.of(true))
    //                     .addLocalComputationEvent("elem_2", "e2", EventType.of("E2"), EventValueDeref.of(
    //                                     RefExpr.of(EventIdVal.of("e1"))),
    //                             new ImmutableMarkingElement(false, true, StringVal.of("in")),
    //                             BoolLiteral.of(true), BoolLiteral.of(true))
    //                     .addControlFlowRelation("flow_1", "e1", "e2",
    //                             ControlFlowRelation.Type.RESPONSE, BoolLiteral.of(true))
    //                     .addControlFlowRelation("flow_1", "e1", "e2",
    //                             ControlFlowRelation.Type.CONDITION, BoolLiteral.of(true))
    //                     .build();
    //     GraphRunner runner =
    //             new GraphRunner(UserVal.of("Prosumer", "p1"), DummyCommunicationLayer.instance());
    //     runner.init(model);
    //     //
    //     System.err.println(runner);
    //     readSystemIn(runner);
    // }
    //
    // // e1 [?] (<void>)
    // // e2 [true] (false)
    // // ;
    // // e1 -->* e2
    // // e1 *--> e2
    // public static void testEmptyInput() {
    //     RecursiveGraphModel model =
    //             new GraphModelBuilder().addLocalInputEvent("1", "e1", EventType.of("E1"),
    //                             new ImmutableMarkingElement(false, true, null),
    //                             BoolLiteral.of(true), BoolLiteral.of(true))
    //                     .addLocalComputationEvent("2", "e2", EventType.of("E2"),
    //                             BoolLiteral.of(BoolVal.of(true)),
    //                             new ImmutableMarkingElement(false, true, BoolVal.of(false)),
    //                             BoolLiteral.of(true), BoolLiteral.of(true))
    //                     .addControlFlowRelation("3", "e1", "e2", ControlFlowRelation.Type.RESPONSE, BoolLiteral.of(true))
    //                     .addControlFlowRelation("4", "e1", "e2", ControlFlowRelation.Type.CONDITION, BoolLiteral.of(true))
    //                     .build();
    //     GraphRunner runner =
    //             new GraphRunner(UserVal.of("Prosumer", "p1"), DummyCommunicationLayer.instance());
    //     runner.init(model);
    //     //
    //     System.err.println(runner);
    //     readSystemIn(runner);
    // }
    //
    //
    // // e1 [?:String] (<undefined>)
    // public static void testSimpleInputEvent() {
    //     RecursiveGraphModel model =
    //             new GraphModelBuilder().addLocalInputEvent("elem_1", "e1", EventType.of("E1"),
    //                     new ImmutableMarkingElement(false, true, null),
    //                     BoolLiteral.of(true), BoolLiteral.of(true)).build();
    //     GraphRunner runner =
    //             new GraphRunner(UserVal.of("Prosumer", "p1"), DummyCommunicationLayer.instance());
    //     runner.init(model);
    //     //
    //     System.err.println(runner);
    //     readSystemIn(runner);
    //
    // }
    //
    // // e1 ['a_string'] (<undefined>)
    // public static void testSimpleSendReceiveP1toP2() {
    //     RecursiveGraphModel model =
    //             new GraphModelBuilder().addComputationEvent("elem_1", "e1", EventType.of("E1"),
    //                     StringLiteral.of("a_string"),
    //                     SetDiffExpr.of(RoleExpr.of("Prosumer"), RoleExpr.of("Prosumer", "p1")),
    //                     new ImmutableMarkingElement(false, true, null),
    //                     BoolLiteral.of(true), BoolLiteral.of(true)).build();
    //     GraphRunner runner =
    //             new GraphRunner(UserVal.of("Prosumer", "p1"), DummyCommunicationLayer.instance());
    //     runner.init(model);
    //     //
    //     System.err.println(runner);
    //     readSystemIn(runner);
    // }
    //
    // // e1 ['a_string'] [P(1) -> P(2)]
    // //
    // // e1 -->% e1
    // // e1 --> {
    // //   (e2:E2) [3] [P(2) -> P(1)]
    // //   ;
    // //   e2 -->+ e1
    // // }
    // private static void simpleSendReceiveWithSpawn() {
    //     // instantiate dummy membership layer accordingly
    //     DummyMembershipLayer.instance()
    //             .onNeighborUp(new DummyMembershipLayer.DummyNeighbour(UserVal.of("Prosumer", "p2"),
    //                     "Prosumer_p2"));
    //     // build & return model
    //     var model = new GraphModelBuilder().addComputationEvent("1", "e1", EventType.of("E1"),
    //                     IntLiteral.of(2), RoleExpr.of("Prosumer", "p2"),
    //                     new ImmutableMarkingElement(false, true, null),
    //                     BoolLiteral.of(true), BoolLiteral.of(true))
    //             .addControlFlowRelation("2", "e1", "e1", ControlFlowRelation.Type.EXCLUDE, BoolLiteral.of(true))
    //             .beginSpawn("3", "4", "e1", BoolLiteral.of(true))
    //             .addReceiveEvent("5", "e2", EventType.of("E2"), RoleExpr.of("Prosumer", "p2"),
    //                     new ImmutableMarkingElement(false, true, BoolVal.of(false)),
    //                     BoolLiteral.of(true), BoolLiteral.of(true))
    //             .addControlFlowRelation("6", "e2", "e1", ControlFlowRelation.Type.INCLUDE, BoolLiteral.of(true))
    //             .endSpawn()
    //             .build();
    //
    //     GraphRunner runner =
    //             new GraphRunner(UserVal.of("Prosumer", "p1"), DummyCommunicationLayer.instance());
    //     runner.init(model);
    //     //
    //     System.err.println(runner);
    //     readSystemIn(runner);
    //
    // }
    //
    // // (e0:readId) [?:{pId:String}] [C0(0)]
    // // %(e1:createProsumer) [?] [P(e0.value.pId)]
    // // e0 *--> e1
    // // e0 -->+ e1
    // // e1 -->% e1
    // // e1 -->> {
    // //
    // // }
    // private static void energyRequest() {
    //     DummyMembershipLayer.instance()
    //             .onNeighborUp(new DummyMembershipLayer.DummyNeighbour(UserVal.of("Prosumer", "1"),
    //                     "P(1)"));
    //     DummyMembershipLayer.instance()
    //             .onNeighborUp(new DummyMembershipLayer.DummyNeighbour(UserVal.of("Prosumer", "2"),
    //                     "P(2)"));
    //     DummyMembershipLayer.instance()
    //             .onNeighborUp(new DummyMembershipLayer.DummyNeighbour(UserVal.of("Prosumer", "3"),
    //                     "P(3)"));
    //
    //     var model = new GraphModelBuilder()
    //             //(e0:readId) [?:{pId:String}}] [C0(1)]
    //             .addLocalInputEvent("01", "e0", EventType.of("readId"), new ImmutableMarkingElement(false, true,
    //                             null),
    //                     BoolLiteral.of(true), BoolLiteral.of(true))
    //             // (e1:createProsumer) [?] [P(e0.value.pId)]
    //             .addInputEvent("02", "e1", EventType.of("createProsumer"), RoleExpr.of("Prosumer",
    //                             Record.ofEntries(Record.Field.of("id", RecordFieldDeref.of(EventValueDeref.of(RefExpr.of(EventIdVal.of("e0"))),
    //                                     "pId")))),
    //                     new ImmutableMarkingElement(false, true, null),
    //                     BoolLiteral.of(true), BoolLiteral.of(true))
    //             .addLocalComputationEvent("1", "e1", EventType.of("E1"), RefExpr.of(EventIdVal.of("e0")),
    //                     new ImmutableMarkingElement(false, true, null),
    //                     BoolLiteral.of(true), BoolLiteral.of(true))
    //
    //
    //             // .addLocalComputationEvent("1", "e1", "E1",
    //             //         EqualsExpr.of(EventValueDeref.of(EventIdExpr.of(EventIdVal.of("@V0",
    //             //                 EventType.of("E0",
    //             //                         BooleanType.singleton())))), BooleanLiteral.of(false)),
    //             //         new ImmutableMarkingElement(false, true,
    //             //                 BooleanVal.undefined()),
    //             //         LogicalOpExpr.and(LogicalOpExpr.and(BooleanLiteral.of(true),
    //             //         LogicalOpExpr.or(BooleanLiteral.of(true), EqualsExpr.of
    //             //         (EventValueDeref.of(EventIdExpr.of(EventIdVal.of("P#cid",EventType.of
    //             //         ("P#cid", IntegerType.singleton())))), IntegerLiteral.of(0)))),
    //             //         LogicalOpExpr.or(BooleanLiteral.of(true), EqualsExpr.of
    //             //         (EventValueDeref.of(EventIdExpr.of(EventIdVal.of("P#id",EventType.of
    //             //         ("P#id", GenericStringType.singleton())))), StringLiteral.of("p1"))))
    //             //         )
    //
    //             // e0 *--> e1
    //             .addControlFlowRelation("03", "e0", "e1",
    //                     // e0 -->+ e1
    //                     ControlFlowRelation.Type.RESPONSE, BoolLiteral.of(true))
    //             .addControlFlowRelation("04", "e0", "e1",
    //                     // e1 -->% e1
    //                     ControlFlowRelation.Type.INCLUDE, BoolLiteral.of(true))
    //             .addControlFlowRelation("05", "e1", "e1", ControlFlowRelation.Type.EXCLUDE, BoolLiteral.of(true))
    //             .beginSpawn("06", "1", "e1", BoolLiteral.of(true))
    //             // .addReceiveEvent("event_elem_2", "e2", "E2", User.of
    //             // ("Prosumer", "p2"),
    //             //         new ImmutableMarkingElement(false, true, BooleanVal
    //             //         .of(false)))
    //             // .addControlFlowRelation("rel_elem_1", "e2", "e1",
    //             //         ControlFlowRelation.Type.INCLUDE)
    //             .endSpawn()
    //             .build();
    //
    //     GraphRunner runner =
    //             new GraphRunner(UserVal.of("CO", "1"), DummyCommunicationLayer.instance());
    //     runner.init(model);
    //     //
    //     System.err.println(runner);
    //     readSystemIn(runner);
    // }


    static void readSystemIn(GraphRunner runner) {
        Scanner in = new Scanner(System.in);
        String line = "";

        while (!line.equals("quit")) {

            try {
                System.err.print("\n  <select event> :");

                // While there is something to read
                line = in.nextLine();
                if (line == null) {
                    System.exit(1);
                }

                StringTokenizer tokenizer = new StringTokenizer(line);
                if (!tokenizer.hasMoreTokens()) {
                    break;
                }
                var eventId = tokenizer.nextToken();
                if (eventId.equals("?")) {
                    eventId = tokenizer.nextToken();

                    if (tokenizer.hasMoreTokens()) {
                        var inputString = tokenizer.nextToken();
                        var inputValue = parseInputVal(inputString);
                        runner.executeInputEvent(eventId, inputValue);
                    }
                    else {
                        runner.executeInputEvent(eventId);
                    }
                    System.err.println("\n\n " + runner);
                }
                else {
                    runner.executeComputationEvent(eventId);
                    System.err.println("\n\n " + runner);
                }


            } catch (Exception e) {
                // TODO: Handle event exception in here
                e.printStackTrace();
            }
        }
        System.exit(0);
    }

    private static Value parseInputVal(String input) {
        input = input.trim();
        // TODO handle empty input values
        if (input.isEmpty()) {
            throw new IllegalArgumentException(
                    "Not implemented: empty input value not allowed at this point");
        }
        // FIXME - still ignoring records after code restructuring
        if (input.startsWith("{") && input.endsWith("}")) {
            return parseRecordVal(input.substring(1, input.length() - 1).trim());
        }
        if (input.startsWith("'") && input.endsWith("'")) {
            // String string_val = input.substring(1, input.length() - 1);
            return StringVal.of(input.substring(1, input.length() - 1));
        }
        // Boolean
        try {
            return IntVal.of(Integer.parseInt(input));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Unable to parse input value");
        }
    }

    private static RecordVal parseRecordVal(String content) {
        if (content.isEmpty()) {
            throw new IllegalArgumentException(
                    "Expecting record fields: empty record not        supported");
        }

        var builder = new Record.Builder<Value>();
        String rest = content;
        while (!rest.isEmpty()) {
            var field_end_pos = -1;
            var next_field_pos = -1;

            var field_assign_pos = rest.indexOf(":");

            if (rest.charAt(field_assign_pos + 1) == '{') {
                // record field value follows - use '}' do detect end of record
                field_end_pos = rest.indexOf("}");
                if (field_end_pos < rest.length() - 1 && rest.charAt(field_end_pos + 1) == ';') {
                    // one more field after this one
                    next_field_pos = field_end_pos;

                }
                else {
                    // this is the last field
                    next_field_pos = rest.length();
                }
                builder.addField(parseRecordFieldVal(rest.substring(0, field_end_pos + 1)));
            }
            else {
                // primitive field value (not record)
                next_field_pos = rest.indexOf(";");
                if (next_field_pos == -1) {
                    // this is the last field
                    field_end_pos = rest.length();
                    next_field_pos = rest.length();
                }
                else {
                    // one more field after this one
                    field_end_pos = next_field_pos;
                    ++next_field_pos;
                }
                builder.addField(parseRecordFieldVal(rest.substring(0, field_end_pos)));
            }
            rest = rest.substring(next_field_pos);
        }
        return RecordVal.of(builder.build());
    }

    private static Record.Field<Value> parseRecordFieldVal(String field) {
        var split_pos = field.indexOf(":");
        var name = field.substring(0, split_pos);
        var valueAsString = field.substring(split_pos + 1);
        Value val = parseInputVal(valueAsString);
        return new Record.Field<>(name, val);
    }
}
