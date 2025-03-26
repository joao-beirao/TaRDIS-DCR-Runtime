package dcr1.runtime;

public class UsageTestSpawns {

    // private static final class DummyCommunicationLayer implements CommunicationLayer {
    //
    //     private static final CommunicationLayer singleton = new DummyCommunicationLayer();
    //
    //     private DummyCommunicationLayer(){}
    //
    //     static CommunicationLayer instance() {return singleton;}
    //
    //     @Override
    //     public Set<User> uponSendRequest(String eventId, UserSetExprInstance receivers,
    //             Event.Marking<?> value, String uidExtension) {
    //         return DummyMembershipLayer.dummyInstance().evalUserSetExpr(receivers).stream().map(
    //                 DummyMembershipLayer.DummyNeighbour::user).collect(Collectors.toSet());
    //     }
    // }
    // public static void main(String[] args) {
    //     RecursiveGraphModel model = new GraphModelBuilder("graph0")
    //             .addComputationEvent("elem_1", "locId1", "label_1", StringLiteral.of("a_string"),
    //                     new Role("Prosumer"),
    //                     new ImmutableMarkingElement<>(false, true, StringVal.undefined()))
    //             .addLocalInputEvent("elem_6", "locId_6", "label_6",
    //                     new ImmutableMarkingElement<>(false, true, BooleanVal.undefined()))
    //             .beginSpawn("spawn_1", "graph1", "locId1")
    //             .addLocalComputationEvent("elem_2", "locId2", "label_2", BooleanLiteral.of(false),
    //                     new ImmutableMarkingElement<>(false, true, BooleanVal.undefined()))
    //             .addLocalComputationEvent("elem_5", "locId3", "label_3", BooleanLiteral.of(false),
    //                     new ImmutableMarkingElement<>(false, true, BooleanVal.undefined()))
    //             .endSpawn()
    //             .beginSpawn("spawn_2", "graph2", "locId1")
    //             .addLocalComputationEvent("elem_3", "locId2", "label_2", BooleanLiteral.of(true),
    //                     new ImmutableMarkingElement<>(false, true, BooleanVal.of(false)))
    //             .beginSpawn("spawn_3", "graph3", "locId2")
    //             .addLocalComputationEvent("elem_4", "locId3", "label_3", BooleanLiteral.of(false),
    //                     new ImmutableMarkingElement<>(false, true, BooleanVal.undefined()))
    //             .endSpawn()
    //             .endSpawn()
    //             .build();
    //
    //     System.err.println(UUID.randomUUID());
    //     System.out.println(model);
    //     System.out.println();
    //
    //     // !note -> passing dummy communication layer instance
    //     GraphRunner runner = new GraphRunner(User.of(
    //             "Prosumer", "p1"), DummyCommunicationLayer.instance());
    //
    //     runner.init(model);
    //     System.err.println(runner);
    //
    //     // Expected input - OK
    //     // var boolVal = BooleanVal.of(true);
    //     // runner.executeInputEvent("locId_6",  boolVal);
    //     // System.err.println(runner.unparse(""));
    //
    //     // BAD input - OK (it's detected)
    //     var intVal = IntegerVal.of(1);
    //     // runner.executeInputEvent("locId_6",  intVal);
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
    //
    // static void readSystemIn(GraphRunner runner) {
    //     Scanner in = new Scanner(System.in);
    //     String line = "";
    //
    //     while (!line.equals("quit")) {
    //
    //         try {
    //             System.err.print("        <select event> % ");
    //
    //             // While there is something to read
    //             line = in.nextLine();
    //             if (line == null) {
    //                 System.exit(1);
    //             }
    //
    //             StringTokenizer tokenizer = new StringTokenizer(line);
    //             if (!tokenizer.hasMoreTokens()) {
    //                 break;
    //             }
    //             String event = tokenizer.nextToken();
    //             runner.executeComputationEvent(event);
    //             System.err.println(runner.unparse(""));
    //
    //         } catch (Exception e) {
    //             // TODO: Handle event exception in here
    //             e.printStackTrace();
    //         }
    //     }
    //     System.exit(0);
    // }
}
