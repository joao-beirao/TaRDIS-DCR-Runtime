package app1;

import app1.membership.DummyMembershipLayer;
import dcr1.common.Record;
import dcr1.common.data.computation.*;
import dcr1.common.data.types.EventType;
import dcr1.common.data.types.StringType;
import dcr1.common.data.values.BoolVal;
import dcr1.common.data.values.EventIdVal;
import dcr1.common.data.values.IntVal;import dcr1.common.events.userset.expressions.RoleExpr;
import dcr1.common.events.userset.expressions.SetDiffExpr;
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
            simpleSendReceiveUC(properties);

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
                return new GraphModelBuilder().addComputationEvent("elem_1", "e1",
                        "E1",
                        StringLiteral.of("a_string"),
                        SetDiffExpr.of(RoleExpr.of("Prosumer"), RoleExpr.of("Prosumer", "p1")),
                        new ImmutableMarkingElement(false, true, null), BoolLiteral.of(true),
                        BoolLiteral.of(true)).build();

            }
            case "Prosumer_p2": {
                // instantiate dummy membership layer accordingly
                DummyMembershipLayer.instance()
                        .onNeighborUp(new DummyMembershipLayer.DummyNeighbour(
                                UserVal.of("Prosumer", "p1"), "Prosumer_p1"));
                // build & return model
                return new GraphModelBuilder().addReceiveEvent("elem_1", "e1",
                                "E1",
                        SetDiffExpr.of(RoleExpr.of("Prosumer"), RoleExpr.of("Prosumer", "p2")),
                        new ImmutableMarkingElement(false, true, null), BoolLiteral.of(true),
                        BoolLiteral.of(true)).build();
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
    //                             new ImmutableMarkingElement(false, true, IntegerVal
    //                             .undefined()))
    //                     // ;
    //                     // e1 -->% e1
    //                     .addControlFlowRelation("excl_1", "e1", "e1",
    //                             ControlFlowRelation.Type.EXCLUDE)
    //                     // e1 -->> {
    //                     .beginSpawn("spawn_1", "graph_1", "e1")
    //                     // >> (e2:E2) [e1.value] [P(2) -> @self]
    //                     .addReceiveEvent("event_2", "e2", "E2", User.of("Prosumer", "p2"),
    //                             new ImmutableMarkingElement(false, true, StringVal.undefined
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
    //                             new ImmutableMarkingElement(false, true, IntegerVal
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
    //                             new ImmutableMarkingElement(false, true, StringVal.undefined
    //                             ()))
    //                     .endSpawn()
    //                     // }
    //                     .build();
    //         }
    //         default:
    //             throw new IllegalArgumentException("Unknown target: " + name);
    //     }
    // }


    // // e1 [?:Integer] [P(1) -> P(2)]
    // // ;
    // // e1 -->% e1
    // // e1 -->> {
    // //   (e2:E2) [e1.value > 3 ? true : false] [P(2) -> P(1)]
    // //   ;
    // //   e2 -->+ e1
    // // }
    // private static GraphModel sndRcvWithInputsSpawnDataDepsAndComputation(Properties properties) {
    //     String name = properties.getProperty("target-name");
    //     switch (name) {
    //         case "Prosumer_p1": {
    //             // instantiate dummy membership layer accordingly
    //             DummyMembershipLayer.instance()
    //                     .onNeighborUp(new DummyMembershipLayer.DummyNeighbour(
    //                             UserVal.of("Prosumer", "p2"), "Prosumer_p2"));
    //             // build & return model
    //             return new GraphModelBuilder()
    //                     // e1 [?:Integer] [ @self -> P(2)]
    //                     .addInputEvent("event_1", "e1", "E1", RoleExpr.of("Prosumer", "p2"),
    //                             new ImmutableMarkingElement(false, true, null),
    //                             BoolLiteral.of(true), BoolLiteral.of(true))
    //                     // ;
    //                     // e1 -->% e1
    //                     .addControlFlowRelation("excl_1", "e1", "e1",
    //                             ControlFlowRelation.Type.EXCLUDE, BoolLiteral.of(true))
    //                     // e1 -->> {
    //                     .beginSpawn("spawn_1", "graph_1", "e1", BoolLiteral.of(true))
    //                     // >> (e2:E2) [e1.value] [P(2) -> @self]
    //                     .addReceiveEvent("event_2", "e2", "E2", RoleExpr.of("Prosumer", "p2"),
    //                             new ImmutableMarkingElement(false, true, null),
    //                             BoolLiteral.of(true), BoolLiteral.of(true))
    //                     // >> ;
    //                     // >> e2 -->+ e1
    //                     .addControlFlowRelation("rel_elem_1", "e2", "e1",
    //                             ControlFlowRelation.Type.INCLUDE, BoolLiteral.of(true))
    //                     // }
    //                     .endSpawn().build();
    //         }
    //         case "Prosumer_p2": {
    //             // instantiate dummy membership layer accordingly
    //             DummyMembershipLayer.instance()
    //                     .onNeighborUp(new DummyMembershipLayer.DummyNeighbour(
    //                             UserVal.of("Prosumer", "p1"), "Prosumer_p1"));
    //             // build & return model
    //             return new GraphModelBuilder()
    //                     // e1 [?:Integer] [ P1 -> @self ]
    //                     .addReceiveEvent("event_1", "e1", "E1", RoleExpr.of("Prosumer", "p1"),
    //                             new ImmutableMarkingElement(false, true, null),
    //                             BoolLiteral.of(true), BoolLiteral.of(true))
    //                     // e1 -->> {
    //                     .beginSpawn("rel_1", "graph_1", "e1", BoolLiteral.of(true))
    //                     // >> (e2:E2) [e1.value] [P(2) -> @self]
    //                     .addComputationEvent("event_2", "e2", "E2", IfThenElseExpr.of(
    //                                     IntegerCompareExpr.ofGt(
    //                                             EventValueDeref.of(RefExpr.of(EventIdVal.of(
    //                                                     "e1"))),
    //                                             IntLiteral.of(IntVal.of(0))),
    //                                     BoolLiteral.of(true), BoolLiteral.of(false)),
    //                             RoleExpr.of("Prosumer", "p1"),
    //                             new ImmutableMarkingElement(false, true, null),
    //                             BoolLiteral.of(true), BoolLiteral.of(true)).endSpawn()
    //                     // }
    //                     .build();
    //         }
    //         default:
    //             throw new IllegalArgumentException("Unknown target: " + name);
    //     }
    // }
    //
    //
    // // e1 ['a_string'] [P(1) -> P(2)]
    // //
    // // e1 -->% e1
    // // e1 --> {
    // //   (e2:E2) [3] [P(2) -> P(1)]
    // //   ;
    // //   e2 -->+ e1
    // // }
    // private static GraphModel simpleSendReceiveWithSpawn(Properties properties) {
    //     String name = properties.getProperty("target-name");
    //     switch (name) {
    //         case "Prosumer_p1": {
    //             // instantiate dummy membership layer accordingly
    //             DummyMembershipLayer.instance()
    //                     .onNeighborUp(new DummyMembershipLayer.DummyNeighbour(
    //                             UserVal.of("Prosumer", "p2"), "Prosumer_p2"));
    //             // build & return model
    //             return new GraphModelBuilder().addComputationEvent("event_elem_1", "e1",
    //                             "E1", IntLiteral.of(2), RoleExpr.of("Prosumer", "p2"),
    //                             new ImmutableMarkingElement(false, true, null),
    //                             BoolLiteral.of(true),
    //                             BoolLiteral.of(true))
    //                     .addControlFlowRelation("rel_elem_1", "e1", "e1",
    //                             ControlFlowRelation.Type.EXCLUDE, BoolLiteral.of(true))
    //                     .beginSpawn("rel_elem_1", "graph_elem_1", "e1", BoolLiteral.of(true))
    //                     .addReceiveEvent("event_elem_2", "e2", "E2", RoleExpr.of("Prosumer", "p2"),
    //                             new ImmutableMarkingElement(false, true, BoolVal.of(false)),
    //                             BoolLiteral.of(true), BoolLiteral.of(true))
    //                     .addControlFlowRelation("rel_elem_1", "e2", "e1",
    //                             ControlFlowRelation.Type.INCLUDE, BoolLiteral.of(true))
    //                     .endSpawn()
    //                     .build();
    //
    //         }
    //         case "Prosumer_p2": {
    //             // instantiate dummy membership layer accordingly
    //             DummyMembershipLayer.instance()
    //                     .onNeighborUp(new DummyMembershipLayer.DummyNeighbour(
    //                             UserVal.of("Prosumer", "p1"), "Prosumer_p1"));
    //             // build & return model
    //             return new GraphModelBuilder().addReceiveEvent("elem_1", "e1", "E1",
    //                             RoleExpr.of("Prosumer", "p1"),
    //                             new ImmutableMarkingElement(false, true, null),
    //                             BoolLiteral.of(true),
    //                             BoolLiteral.of(true))
    //                     .beginSpawn("rel_elem_1", "graph_elem_1", "e1", BoolLiteral.of(true))
    //                     .addComputationEvent("event_elem_2", "e2", "E2", BoolLiteral.of(true),
    //                             RoleExpr.of("Prosumer", "p1"),
    //                             new ImmutableMarkingElement(false, true, BoolVal.of(false)),
    //                             BoolLiteral.of(true), BoolLiteral.of(true))
    //                     .endSpawn()
    //                     .build();
    //         }
    //         default:
    //             throw new IllegalArgumentException("Unknown target: " + name);
    //     }
    // }
    //
    // // (e0:readId) [?:{pId:String}] [C0(0)]
    // // %(e1:createProsumer) [?] [P(e0.value.pId)]
    // // e0 *--> e1
    // // e0 -->+ e1
    // // e1 -->% e1
    // // e1 -->> {
    // //   (e2:consume) [?:{kw:Integer, t:String}] [Receiver(e1) -> P(*)]
    // // }
    // private void energyRequest(Properties properties) {
    //     String role = properties.getProperty("role");
    //     switch (role) {
    //         case "CO": {
    //             //  get all fields for this user (param-<field_name>)
    //             String id = properties.getProperty("param-id");
    //             // instantiate @self
    //             this.self = UserVal.of(role, id);
    //
    //             // (hardcoded) CO knows about Prosumers 1, 2, and 3
    //             DummyMembershipLayer.instance()
    //                     .onNeighborUp(
    //                             new DummyMembershipLayer.DummyNeighbour(UserVal.of("Prosumer", "1"),
    //                                     "P_1"));
    //             DummyMembershipLayer.instance()
    //                     .onNeighborUp(
    //                             new DummyMembershipLayer.DummyNeighbour(UserVal.of("Prosumer", "2"),
    //                                     "P_2"));
    //             DummyMembershipLayer.instance()
    //                     .onNeighborUp(
    //                             new DummyMembershipLayer.DummyNeighbour(UserVal.of("Prosumer", "3"),
    //                                     "P_3"));
    //
    //
    //             this.model = new GraphModelBuilder()
    //                     // (e0:readId) [?:{pId:String}}] [@self]
    //                     .addLocalInputEvent("001", "e0", "readId",
    //                             new ImmutableMarkingElement(false, true, null),
    //                             BoolLiteral.of(true), BoolLiteral.of(true))
    //                     // [Tx] (e1:createProsumer) [?] [ @self -> P(e0.value.pId) ]
    //                     .addInputEvent("002", "e1", "createProsumer", RoleExpr.of("Prosumer",
    //                                     Record.ofEntries(Record.Field.of("pId", RecordFieldDeref.of(
    //                                                             EventValueDeref.of(RefExpr.of(EventIdVal.of(
    //                                                                     "e0"))), "pId")))), new ImmutableMarkingElement(false, true,
    //                                     null),
    //                             BoolLiteral.of(true), BoolLiteral.of(true))
    //                     // e0 *--> e1
    //                     .addControlFlowRelation("003", "e0", "e1",
    //                             ControlFlowRelation.Type.RESPONSE, BoolLiteral.of(true))
    //                     // e0 -->+ e1
    //                     .addControlFlowRelation("004", "e0", "e1",
    //                             // e1 -->% e1
    //                             ControlFlowRelation.Type.INCLUDE, BoolLiteral.of(true))
    //                     .addControlFlowRelation("005", "e1", "e1", ControlFlowRelation.Type.EXCLUDE, BoolLiteral.of(true))
    //                     // e1 -->> {
    //                     .beginSpawn("006", "1", "e1", BoolLiteral.of(true))
    //
    //                     // }
    //                     .endSpawn()
    //                     .build();
    //             break;
    //         }
    //
    //
    //         case "Prosumer": {
    //             //  get all fields for this user
    //             String id = properties.getProperty("param-id");
    //             // instantiate @self
    //             this.self = UserVal.of(role, id);
    //
    //
    //             // (hardcoded) P knows about CO(1) and P(1)..P(4)
    //             DummyMembershipLayer.instance()
    //                     .onNeighborUp(new DummyMembershipLayer.DummyNeighbour(UserVal.of("CO", "1"),
    //                             "CO(1)"));
    //             // add Prosumers(i) with i ≠ @self.id
    //             IntStream.range(1, 4).mapToObj(String::valueOf).forEach(pId -> {
    //                 if (!pId.equals(id)) {
    //                     DummyMembershipLayer.instance()
    //                             .onNeighborUp(new DummyMembershipLayer.DummyNeighbour(
    //                                     UserVal.of("Prosumer", pId), "P_" + pId));
    //                 }
    //             });
    //             // build model
    //             this.model = new GraphModelBuilder()
    //                     // [Rx @ CO(1)] (e1:createProsumer) [?]
    //                     .addReceiveEvent("002", "e1", "createProsumer",
    //                             RoleExpr.of("CO", Record.ofEntries(Record.Field.of("id", StringLiteral.of("1")))),
    //                             new ImmutableMarkingElement(false, true, null),
    //                             BoolLiteral.of(true), BoolLiteral.of(true))
    //                     .beginSpawn("006", "1", "e1", BoolLiteral.of(true))
    //                     // -- [Tx @ P(*)\@self] (e2:consume) [?:{kw:Integer, t:String}]
    //                     .addInputEvent("101", "e2", "consume",
    //                             RoleExpr.of("Prosumer", Record.empty()),
    //                             new ImmutableMarkingElement(false, true, null),
    //                             BoolLiteral.of(true), BoolLiteral.of(true))
    //                     // -- [Rx @ P(*)\@self ] (e2:consume) [{kw:Integer, t:String}]
    //                     .addReceiveEvent("102", "e2", "consume", RoleExpr.of("Prosumer"),
    //                             new ImmutableMarkingElement(false, true, null),
    //                             BoolLiteral.of(true), BoolLiteral.of(true))
    //                     .endSpawn()
    //                     .build();
    //             break;
    //         }
    //         default:
    //             throw new IllegalArgumentException("Unknown role: " + role);
    //     }
    // }


    private static final <T> Set<T> newHashSet(T... objs) {
        Set<T> set = new HashSet<T>();
        Collections.addAll(set, objs);
        return set;
    }
}
