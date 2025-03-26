// package app;

// import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;

// import java.io.IOException;
// import java.util.Collections;
// import java.util.HashSet;
// import java.util.Properties;
// import java.util.Set;

// import dcr.DCRGraphClass;
// import dcr.EventMarking;
// import dcr.Relation.RelationType;
// import dcr.ast.ASTNumber;
// import dcr.ast.ASTRecordField;
// import dcr.ast.ASTString;
// import dcr.ast.typing.BooleanType;
// import dcr.ast.typing.IntType;
// import dcr.ast.typing.StringType;
// import dcr.ast.ASTBoolean;
// import dcr.ast.ASTConditional;
// import dcr.ast.ASTDeref;
// import dcr.ast.ASTEquals;
// import dcr.ast.ASTId;
// import dcr.ast.ASTLessThan;
// import dcr.ast.ASTNode;

// public class App extends DCRProtocol {

//   public static final short PROTO_ID = 2; // unique protocol id

//   public App() {
//     super("DcrApp", PROTO_ID);
//   }

//   @Override
//   public void init(Properties properties) throws HandlerRegistrationException, IOException {
//     super.init(properties);

//     String name = properties.getProperty("target-name");

//     // setupExample0(name);
//     // setupExample1(name);
//     setupBuyerSellerUC(name);

//     readSystemIn();
//   }

//   // private final void setupExample0(String name) {

//   // // Environment env = new Environment();
//   // switch (name) {
//   // case "target-A":
//   // logger.info("Running on node A");
//   // dcrGraph = DCRGraphClass.newBuilder(name)
//   // .addSendOperation(
//   // "1",
//   // "E1",
//   // "target-A",
//   // Collections.singleton("target-B"),
//   // new ASTBoolean(false))
//   // .addSendOperation(
//   // "2",
//   // "E2",
//   // "target-A",
//   // Collections.singleton("target-C"),
//   // new ASTNumber(1))
//   // .addReceiveOperation(
//   // "3",
//   // "E3",
//   // "target-B",
//   // "target-A")
//   // .addRelation(RelationType.CONDITION, "1", "2")
//   // .addRelation(RelationType.RESPONSE, "1", "3")
//   // .addRelation(RelationType.EXCLUDE, "2", "1")
//   // .addRelation(RelationType.MILESTONE, "3", "2")
//   // .build();

//   // break;
//   // case "target-B":
//   // logger.info("Running on node B");
//   // // Projection of B
//   // dcrGraph = DCRGraphClass.newBuilder(name)
//   // .addReceiveOperation(
//   // "1",
//   // "E1",
//   // "target-A",
//   // "target-B")
//   // .addSendOperation(
//   // "3",
//   // "E3",
//   // "target-B",
//   // Collections.singleton("target-A"),
//   // new ASTNumber(2))
//   // .addRelation(RelationType.RESPONSE, "1", "3")
//   // .build();

//   // break;
//   // case "target-C":
//   // logger.info("Running on node C");
//   // dcrGraph = DCRGraphClass.newBuilder(name)
//   // .addReceiveOperation(
//   // "2",
//   // "E2",
//   // "target-A",
//   // "target-C")
//   // .build();

//   // break;
//   // default:
//   // logger.warn("Participant not recognized: " + name);
//   // break;
//   // }
//   // }

//   // private void setupExample1(String name) {

//   // Environment env = new Environment();

//   // switch (name) {
//   // case "target-A":
//   // this.dcrGraph = DCRGraphClass.newBuilder(name, env)
//   // .addComputationAction(
//   // "1",
//   // "E1",
//   // "target-A",
//   // new ASTRecord(),
//   // new RecordVal.Builder().addField("value", new NumberVal(0)).build())
//   // .addSendOperation(
//   // "2",
//   // "E2",
//   // "target-A",
//   // Collections.singleton("target-B"),
//   // new EventMarking().setIncluded(false))
//   // .addReceiveOperation(
//   // "3",
//   // "E4",
//   // "target-B",
//   // "target-A")
//   // .addReceiveOperation(
//   // "4",
//   // "E5",
//   // "target-C",
//   // "target-A")
//   // .addRelation(
//   // RelationType.CONDITION,
//   // "E1",
//   // "E2")
//   // .addRelation(
//   // RelationType.MILESTONE,
//   // "E4",
//   // "E2")
//   // .addRelation(
//   // RelationType.EXCLUDE,
//   // "E5",
//   // "E4")
//   // .addRelation(
//   // RelationType.INCLUDE,
//   // "E5",
//   // "E2")
//   // .build();
//   // break;

//   // case "target-B":
//   // this.dcrGraph = DCRGraphClass.newBuilder(name, env)
//   // .addReceiveOperation(
//   // "1",
//   // "E2",
//   // "target-A",
//   // "target-B",
//   // new EventMarking().setIncluded(false))
//   // .addInputAction(
//   // "2",
//   // "E3",
//   // "target-B",
//   // new Record.Builder().build())
//   // .addSendOperation(
//   // "3",
//   // "E4",
//   // "target-B",
//   // Collections.singleton("target-A"),
//   // )
//   // .addReceiveOperation(
//   // "4",
//   // "E5",
//   // "target-C",
//   // "target-B")
//   // .addRelation(RelationType.MILESTONE, "E4", "E2")
//   // .addRelation(RelationType.EXCLUDE, "E5", "E4")
//   // .addRelation(RelationType.INCLUDE, "E5", "E2")
//   // .addRelation(RelationType.EXCLUDE, "E5", "E5")
//   // .addRelation(RelationType.RESPONSE, "E3", "E4")
//   // .build();

//   // case "target-C":
//   // this.dcrGraph = DCRGraphClass.newBuilder(name, env)
//   // .addSendOperation(
//   // "1",
//   // "E5",
//   // "target-C",
//   // newHashSet("target-A", "target-B"))
//   // .build();

//   // default:
//   // break;
//   // }
//   // }

//   private final void setupBuyerSellerUC(String name) {
//     // docker run --network babel-tutorial-net --rm -h Buyer --name Buyer -it
//     // babel-tutorial/f-dcr interface=eth0 target-name=Buyer
//     // docker run --network babel-tutorial-net --rm -h Seller --name Seller -it
//     // babel-tutorial/f-dcr interface=eth0 target-name=Seller
//     switch (name) {
//       case "Buyer":
//         logger.info("Buyer's endpoint running");
//         dcrGraph = DCRGraphClass.newBuilder(name)
//             .addInputAction(
//                 "1",
//                 "Car",
//                 "Buyer",
//                 StringType.TYPE)
//             .addInputAction(
//                 "2",
//                 "Maximum",
//                 "Buyer",
//                 IntType.TYPE)
//             .addComputationSendOperation(
//                 "3",
//                 "Request",
//                 "Buyer",
//                 Collections.singleton("Seller"),
//                 eventValueFieldAsASTNode("1"),
//                 StringType.TYPE)
//             .addComputationSendOperation(
//                 "4",
//                 "Decision",
//                 "Buyer",
//                 Collections.singleton("Seller"),
//                 new ASTConditional(
//                     new ASTLessThan(
//                         eventValueFieldAsASTNode("6"),
//                         eventValueFieldAsASTNode("2")),
//                     new ASTBoolean(true),
//                     new ASTBoolean(false)),
//                 BooleanType.TYPE)
//             .addComputationSendOperation(
//                 "5",
//                 "Timeout",
//                 "Buyer",
//                 Collections.singleton("Seller"),
//                 new ASTNumber(),
//                 IntType.TYPE)// TODO revisit, expressions already available
//             .addReceiveOperation(
//                 "6",
//                 "Quote",
//                 "Seller",
//                 "Buyer",
//                 IntType.TYPE,
//                 EventMarking.newInitiallyExcluded())
//             .addRelation(RelationType.CONDITION, "1", "3")
//             .addRelation(RelationType.CONDITION, "2", "4")
//             .addRelation(RelationType.CONDITION, "3", "4")
//             .addRelation(RelationType.RESPONSE, "3", "4")
//             .addRelation(RelationType.INCLUDE, "3", "4")
//             .addRelation(RelationType.CONDITION, "3", "5")
//             .addRelation(RelationType.RESPONSE, "3", "5")
//             .addRelation(RelationType.INCLUDE, "3", "6")
//             .addRelation(RelationType.RESPONSE, "3", "6")
//             .addRelation(RelationType.EXCLUDE, "4", "6")
//             .addRelation(RelationType.EXCLUDE, "5", "4")
//             .addRelation(RelationType.MILESTONE, "6", "4")
//             .build();
//         break;
//       case "Seller":
//         logger.info("Seller's endpoint running");
//         dcrGraph = DCRGraphClass.newBuilder(name)
//             .addReceiveOperation(
//                 "3",
//                 "Request",
//                 "Buyer",
//                 "Seller",
//                 StringType.TYPE)
//             .addReceiveOperation(
//                 "4",
//                 "Decision",
//                 "Buyer",
//                 "Seller",
//                 BooleanType.TYPE)
//             .addReceiveOperation(
//                 "5",
//                 "Timeout",
//                 "Buyer",
//                 "Seller",
//                 IntType.TYPE)
//             .addComputationSendOperation(
//                 "6",
//                 "Quote",
//                 "Seller",
//                 Collections.singleton("Buyer"),
//                 new ASTConditional(
//                     new ASTEquals(
//                         eventValueFieldAsASTNode("3"),
//                         new ASTString("Toyota")),
//                     new ASTNumber(50000),
//                     new ASTNumber(100000)),
//                 IntType.TYPE,
//                 EventMarking.newInitiallyExcluded())
//             .addRelation(RelationType.INCLUDE, "3", "6")
//             .addRelation(RelationType.RESPONSE, "3", "6")
//             .addRelation(RelationType.EXCLUDE, "4", "6")
//             .addRelation(RelationType.EXCLUDE, "5", "4")
//             .addRelation(RelationType.EXCLUDE, "5", "6")
//             .build();
//         break;
//       default:
//         logger.warn("Participant not recognized: " + name);
//         break;
//     }
//   }

//   private static final ASTNode eventValueFieldAsASTNode(String eventId) {
//     return new ASTDeref(
//         new ASTRecordField("value", new ASTId(eventId)));
//   }

//   private static final <T> Set<T> newHashSet(T... objs) {
//     Set<T> set = new HashSet<T>();
//     Collections.addAll(set, objs);
//     return set;
//   }
// }
package app1;

import app1.membership.DummyMembershipLayer;
import dcr1.common.Record;
import dcr1.common.data.computation.*;
import dcr1.common.data.computation.StringLiteral;
import dcr1.common.data.values.*;
import dcr1.common.data.types.EventType;
import dcr1.common.data.types.GenericStringType;
import dcr1.common.data.types.IntegerType;
import dcr1.common.data.types.RecordType;
import dcr1.common.events.userset.RoleParams;
import dcr1.common.events.userset.expressions.RoleExpr;
import dcr1.common.events.userset.expressions.SetDiffExpr;
import dcr1.common.events.userset.expressions.UserExpr;
import dcr1.common.events.userset.values.UserVal;
import dcr1.common.relations.ControlFlowRelation;
import dcr1.model.GraphModel;
import dcr1.model.GraphModelBuilder;
import dcr1.model.events.ImmutableMarkingElement;
import dcr1.runtime.GraphRunner;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.stream.IntStream;

// docker run example
// docker run --network tardis-babel-backend-net --rm -h Prosumer_p1 --name Prosumer_p1 -it
// dcr-babel interface=eth0 target-name=Prosumer_p1 role=Prosumer id=p1


public final class App
        extends DCRProtocol {

    public static final short PROTO_ID = 2; // unique protocol id

    public App() {
        super("DcrApp", PROTO_ID);
    }

    @Override
    public void init(Properties properties) throws HandlerRegistrationException, IOException {
        try {
            // CHOOSE USE CASE HERE
            energyRequest(properties);

            // boilerplate
            runner = new GraphRunner(self, this);
            runner.init(model);
            super.init(properties);

            // {
            //     var role = properties.getProperty("role");
            //     var id = properties.getProperty("id");
            //     owner = UserVal.of(role, id);
            // }
            // runner = new GraphRunner(owner, this);
            // {
            //     // -> CHOOSE USE CASE HERE
            //     var model = energyRequest(properties);
            //     runner.init(model);
            // }
            //
            // super.init(properties);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // (e1:E1) ['a_string'] P(1) -> P(2)
    private static GraphModel simpleSendReceiveUC(Properties properties) {
        // locId_1 ['a_string'] P(1) -> P(2)
        String name = properties.getProperty("target-name");
        switch (name) {
            case "Prosumer_p1": {
                // instantiate dummy membership layer accordingly
                DummyMembershipLayer.instance()
                        .onNeighborUp(new DummyMembershipLayer.DummyNeighbour(
                                UserVal.of("Prosumer", "p2"), "Prosumer_p2"));
                // build & return model
                return new GraphModelBuilder("graph0").addComputationEvent("elem_1", "e1", "E1",
                        StringLiteral.of("a_string"),
                        SetDiffExpr.of(RoleExpr.of("Prosumer"), UserExpr.of("Prosumer", "p1")),
                        new ImmutableMarkingElement<>(false, true, StringVal.undefined()),
                        BooleanLiteral.of(true)).build();

            }
            case "Prosumer_p2": {
                // instantiate dummy membership layer accordingly
                DummyMembershipLayer.instance()
                        .onNeighborUp(new DummyMembershipLayer.DummyNeighbour(
                                UserVal.of("Prosumer", "p1"), "Prosumer_p1"));
                // build & return model
                return new GraphModelBuilder("graph0").addReceiveEvent("elem_1", "e1", "E1",
                        SetDiffExpr.of(RoleExpr.of("Prosumer"), UserExpr.of("Prosumer", "p2")),
                        new ImmutableMarkingElement<>(false, true, StringVal.undefined()),
                        BooleanLiteral.of(true)).build();
            }
            default:
                throw new IllegalArgumentException("Unknown target: " + name);
        }
    }


    // cp [?:{id:String; pInfo:String}] : CO(cid=0) -> P(id=*; cid=0)
    // ;
    // cp -->> {
    //  pInfo
    //  consume [?:{kw:Integer}] : P(id=@trigger.value.id; cid=0) -> P(id=*; cid=0)
    // }

    // UNDER CONSTRUCTION
    // e1 [?:Integer] [P(1) -> P(2)]
    // ;
    // e1 -->% e1
    // e1 -->> {
    //   (e2:E2) ['ACK'] [P(2) -> P(1)]
    //   ;
    //   e2 -->+ e1
    // }
    // private static GraphModel sndRcvWithInputsSpawnAndDataDeps(Properties properties) {
    //     String name = properties.getProperty("target-name");
    //     switch (name) {
    //         case "Prosumer_p1": {
    //             // instantiate dummy membership layer accordingly
    //             DummyMembershipLayer.instance()
    //                     .onNeighborUp(new DummyMembershipLayer.DummyNeighbour(
    //                             dcr1.runtime.participants.User.of("Prosumer", "p2"),
    //                             "Prosumer_p2"));
    //             // build & return model
    //             return new GraphModelBuilder("graph0")
    //                     // e1 [?:Integer] [ @self -> P(2)]
    //                     .addInputEvent("event_1", "e1", "E1", User.of("Prosumer", "p2"),
    //                             new ImmutableMarkingElement<>(false, true, IntegerVal
    //                             .undefined()))
    //                     // ;
    //                     // e1 -->% e1
    //                     .addControlFlowRelation("excl_1", "e1", "e1",
    //                             ControlFlowRelation.Type.EXCLUDE)
    //                     // e1 -->> {
    //                     .beginSpawn("spawn_1", "graph_1", "e1")
    //                     // >> (e2:E2) [e1.value] [P(2) -> @self]
    //                     .addReceiveEvent("event_2", "e2", "E2", User.of("Prosumer", "p2"),
    //                             new ImmutableMarkingElement<>(false, true, StringVal.undefined
    //                             ()))
    //                     // >> ;
    //                     // >> e2 -->+ e1
    //                     .addControlFlowRelation("rel_elem_1", "e2", "e1",
    //                             ControlFlowRelation.Type.INCLUDE)
    //                     // }
    //                     .endSpawn().build();
    //
    //         }
    //         case "Prosumer_p2": {
    //             // instantiate dummy membership layer accordingly
    //             DummyMembershipLayer.instance()
    //                     .onNeighborUp(new DummyMembershipLayer.DummyNeighbour(
    //                             dcr1.runtime.participants.User.of("Prosumer", "p1"),
    //                             "Prosumer_p1"));
    //             // build & return model
    //             return new GraphModelBuilder("graph0")
    //                     // e1 [?:Integer] [ P1 -> @self ]
    //                     .addReceiveEvent("elem_1", "e1", "E1",
    //                             User.of("Prosumer", "p1"),
    //                             new ImmutableMarkingElement<>(false, true, IntegerVal
    //                             .undefined()))
    //                     // e1 -->> {
    //                     .beginSpawn("rel_elem_1", "graph_elem_1", "e1")
    //                     // >> (e2:E2) [e1.value] [P(2) -> @self]
    //                     .addComputationEvent("event_elem_2", "e2", "E2",
    //                             IfThenElseExpr.of(
    //                                     EventValueDeref.of(
    //                                             EventIdExpr.of(
    //                                                     EventIdVal.of("e1", EventType.of("E1",
    //                                                             GenericStringType.singleton()))
    //                                             )
    //                                     ),
    //                                     StringLiteral
    //                             ),
    //                             User.of("Prosumer", "p1"),
    //                             new ImmutableMarkingElement<>(false, true, StringVal.undefined
    //                             ()))
    //                     .endSpawn()
    //                     // }
    //                     .build();
    //         }
    //         default:
    //             throw new IllegalArgumentException("Unknown target: " + name);
    //     }
    // }


    // e1 [?:Integer] [P(1) -> P(2)]
    // ;
    // e1 -->% e1
    // e1 -->> {
    //   (e2:E2) [e1.value > 3 ? true : false] [P(2) -> P(1)]
    //   ;
    //   e2 -->+ e1
    // }
    private static GraphModel sndRcvWithInputsSpawnDataDepsAndComputation(Properties properties) {
        String name = properties.getProperty("target-name");
        switch (name) {
            case "Prosumer_p1": {
                // instantiate dummy membership layer accordingly
                DummyMembershipLayer.instance()
                        .onNeighborUp(new DummyMembershipLayer.DummyNeighbour(
                                UserVal.of("Prosumer", "p2"), "Prosumer_p2"));
                // build & return model
                return new GraphModelBuilder("graph0")
                        // e1 [?:Integer] [ @self -> P(2)]
                        .addInputEvent("event_1", "e1", "E1", UserExpr.of("Prosumer", "p2"),
                                new ImmutableMarkingElement<>(false, true, IntegerVal.undefined()),
                                BooleanLiteral.of(true))
                        // ;
                        // e1 -->% e1
                        .addControlFlowRelation("excl_1", "e1", "e1",
                                ControlFlowRelation.Type.EXCLUDE)
                        // e1 -->> {
                        .beginSpawn("spawn_1", "graph_1", "e1")
                        // >> (e2:E2) [e1.value] [P(2) -> @self]
                        .addReceiveEvent("event_2", "e2", "E2", UserExpr.of("Prosumer", "p2"),
                                new ImmutableMarkingElement<>(false, true, BooleanVal.undefined()),
                                BooleanLiteral.of(true))
                        // >> ;
                        // >> e2 -->+ e1
                        .addControlFlowRelation("rel_elem_1", "e2", "e1",
                                ControlFlowRelation.Type.INCLUDE)
                        // }
                        .endSpawn().build();
            }
            case "Prosumer_p2": {
                // instantiate dummy membership layer accordingly
                DummyMembershipLayer.instance()
                        .onNeighborUp(new DummyMembershipLayer.DummyNeighbour(
                                UserVal.of("Prosumer", "p1"), "Prosumer_p1"));
                // build & return model
                return new GraphModelBuilder("graph0")
                        // e1 [?:Integer] [ P1 -> @self ]
                        .addReceiveEvent("event_1", "e1", "E1", UserExpr.of("Prosumer", "p1"),
                                new ImmutableMarkingElement<>(false, true, IntegerVal.undefined()),
                                BooleanLiteral.of(true))
                        // e1 -->> {
                        .beginSpawn("rel_1", "graph_1", "e1")
                        // >> (e2:E2) [e1.value] [P(2) -> @self]
                        .addComputationEvent("event_2", "e2", "E2", IfThenElseExpr.of(
                                        IntegerCompareExpr.ofGt(EventValueDeref.of(EventIdExpr.of(
                                                        EventIdVal.of("e1",
                                                                EventType.of("E1",
                                                                        IntegerType.singleton())))),
                                                IntegerLiteral.of(IntegerVal.of(0))),
                                        BooleanLiteral.of(true), BooleanLiteral.of(false)),
                                UserExpr.of("Prosumer", "p1"),
                                new ImmutableMarkingElement<>(false, true, BooleanVal.undefined()),
                                BooleanLiteral.of(true))
                        .endSpawn()
                        // }
                        .build();
            }
            default:
                throw new IllegalArgumentException("Unknown target: " + name);
        }
    }


    // e1 ['a_string'] [P(1) -> P(2)]
    //
    // e1 -->% e1
    // e1 --> {
    //   (e2:E2) [3] [P(2) -> P(1)]
    //   ;
    //   e2 -->+ e1
    // }
    private static GraphModel simpleSendReceiveWithSpawn(Properties properties) {
        String name = properties.getProperty("target-name");
        switch (name) {
            case "Prosumer_p1": {
                // instantiate dummy membership layer accordingly
                DummyMembershipLayer.instance()
                        .onNeighborUp(new DummyMembershipLayer.DummyNeighbour(
                                UserVal.of("Prosumer", "p2"), "Prosumer_p2"));
                // build & return model
                return new GraphModelBuilder("graph0").addComputationEvent("event_elem_1", "e1",
                                "E1", IntegerLiteral.of(2), UserExpr.of("Prosumer", "p2"),
                                new ImmutableMarkingElement<>(false, true, IntegerVal.undefined()),
                                BooleanLiteral.of(true))
                        .addControlFlowRelation("rel_elem_1", "e1", "e1",
                                ControlFlowRelation.Type.EXCLUDE)
                        .beginSpawn("rel_elem_1", "graph_elem_1", "e1")
                        .addReceiveEvent("event_elem_2", "e2", "E2", UserExpr.of("Prosumer", "p2"),
                                new ImmutableMarkingElement<>(false, true, BooleanVal.of(false)),
                                BooleanLiteral.of(true))
                        .addControlFlowRelation("rel_elem_1", "e2", "e1",
                                ControlFlowRelation.Type.INCLUDE)
                        .endSpawn()
                        .build();

            }
            case "Prosumer_p2": {
                // instantiate dummy membership layer accordingly
                DummyMembershipLayer.instance()
                        .onNeighborUp(new DummyMembershipLayer.DummyNeighbour(
                                UserVal.of("Prosumer", "p1"), "Prosumer_p1"));
                // build & return model
                return new GraphModelBuilder("graph0").addReceiveEvent("elem_1", "e1", "E1",
                                UserExpr.of("Prosumer", "p1"),
                                new ImmutableMarkingElement<>(false, true, IntegerVal.undefined()),
                                BooleanLiteral.of(true))
                        .beginSpawn("rel_elem_1", "graph_elem_1", "e1")
                        .addComputationEvent("event_elem_2", "e2", "E2", BooleanLiteral.of(true),
                                UserExpr.of("Prosumer", "p1"),
                                new ImmutableMarkingElement<>(false, true, BooleanVal.of(false)),
                                BooleanLiteral.of(true))
                        .endSpawn()
                        .build();
            }
            default:
                throw new IllegalArgumentException("Unknown target: " + name);
        }
    }

    // (e0:readId) [?:{pId:String}] [C0(0)]
    // %(e1:createProsumer) [?] [P(e0.value.pId)]
    // e0 *--> e1
    // e0 -->+ e1
    // e1 -->% e1
    // e1 -->> {
    //   (e2:consume) [?:{kw:Integer, t:String}] [Receiver(e1) -> P(*)]
    // }
    private void energyRequest(Properties properties) {
        String role = properties.getProperty("role");
        switch (role) {
            case "CO": {
                //  get all fields for this user (param-<field_name>)
                String id = properties.getProperty("param-id");
                // instantiate @self
                this.self = UserVal.of(role, id);

                // (hardcoded) CO knows about Prosumers 1, 2, and 3
                DummyMembershipLayer.instance()
                        .onNeighborUp(
                                new DummyMembershipLayer.DummyNeighbour(UserVal.of("Prosumer", "1"),
                                        "P_1"));
                DummyMembershipLayer.instance()
                        .onNeighborUp(
                                new DummyMembershipLayer.DummyNeighbour(UserVal.of("Prosumer", "2"),
                                        "P_2"));
                DummyMembershipLayer.instance()
                        .onNeighborUp(
                                new DummyMembershipLayer.DummyNeighbour(UserVal.of("Prosumer", "3"),
                                        "P_3"));


                this.model = new GraphModelBuilder("0")
                        // (e0:readId) [?:{pId:String}}] [@self]
                        .addLocalInputEvent("001", "e0", "readId",
                                new ImmutableMarkingElement<>(false, true, RecordVal.undefined(
                                        RecordType.of(Record.ofEntries(Record.Field.of("pId",
                                                GenericStringType.singleton()))))),
                                BooleanLiteral.of(true))
                        // [Tx] (e1:createProsumer) [?] [ @self -> P(e0.value.pId) ]
                        .addInputEvent("002", "e1", "createProsumer", UserExpr.of("Prosumer",
                                        RecordFieldDeref.of(EventValueDeref.of(EventIdExpr.of(
                                                        EventIdVal.of("e0", EventType.of("readId"
                                                                , RecordType.of(
                                                                        Record.ofEntries(Record.Field.of(
                                                                                "pId",
                                                                                GenericStringType.singleton()))))))),
                                                "pId")),
                                new ImmutableMarkingElement<>(false, true, Undefined.ofVoid()),
                                BooleanLiteral.of(true))
                        // e0 *--> e1
                        .addControlFlowRelation("003", "e0", "e1",
                                ControlFlowRelation.Type.RESPONSE)
                        // e0 -->+ e1
                        .addControlFlowRelation("004", "e0", "e1",
                                // e1 -->% e1
                                ControlFlowRelation.Type.INCLUDE)
                        .addControlFlowRelation("005", "e1", "e1", ControlFlowRelation.Type.EXCLUDE)
                        // e1 -->> {
                        .beginSpawn("006", "1", "e1")

                        // }
                        .endSpawn()
                        .build();
                break;
            }


            case "Prosumer": {
                //  get all fields for this user
                String id = properties.getProperty("param-id");
                // instantiate @self
                this.self = UserVal.of(role, id);


                // (hardcoded) P knows about CO(1) and P(1)..P(4)
                DummyMembershipLayer.instance()
                        .onNeighborUp(new DummyMembershipLayer.DummyNeighbour(UserVal.of("CO", "1"),
                                "CO(1)"));
                // add Prosumers(i) with i ≠ @self.id
                IntStream.range(1, 4).mapToObj(String::valueOf).forEach(pId -> {
                    if (!pId.equals(id)) {
                        DummyMembershipLayer.instance()
                                .onNeighborUp(new DummyMembershipLayer.DummyNeighbour(
                                        UserVal.of("Prosumer", pId), "P_" + pId));
                    }
                });
                // build model
                this.model = new GraphModelBuilder("0")
                        // [Rx @ CO(1)] (e1:createProsumer) [?]
                        .addReceiveEvent("002", "e1", "createProsumer",
                                UserExpr.of("CO", StringLiteral.of("1")),
                                new ImmutableMarkingElement<>(false, true, Undefined.ofVoid()),
                                BooleanLiteral.of(true))
                        .beginSpawn("006", "1", "e1")
                        // -- [Tx @ P(*)\@self] (e2:consume) [?:{kw:Integer, t:String}]
                        .addInputEvent("101", "e2", "consume",
                                RoleExpr.of("Prosumer", RoleParams.empty()),
                                new ImmutableMarkingElement<>(false, true, RecordVal.undefined(
                                        RecordType.of(Record.ofEntries(
                                                Record.Field.of("kw", IntegerType.singleton()),
                                                Record.Field.of("t",
                                                        GenericStringType.singleton()))))),
                                BooleanLiteral.of(true))
                        // -- [Rx @ P(*)\@self ] (e2:consume) [{kw:Integer, t:String}]
                        .addReceiveEvent("102", "e2", "consume", RoleExpr.of("Prosumer"),
                                new ImmutableMarkingElement<>(false, true, RecordVal.undefined(
                                        RecordType.of(Record.ofEntries(
                                                Record.Field.of("kw", IntegerType.singleton()),
                                                Record.Field.of("t",
                                                        GenericStringType.singleton()))))),
                                BooleanLiteral.of(true))
                        .endSpawn()
                        .build();
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }


    private static final <T> Set<T> newHashSet(T... objs) {
        Set<T> set = new HashSet<T>();
        Collections.addAll(set, objs);
        return set;
    }
}
