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
package app;

import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import dcr.*;
import dcr.Relation.Type;
import dcr.ast.typing.*;
import dcr.ast.*;
import dcr.ast.ASTRecord.RecordField;

public class App extends DCRProtocol {

  public static final short PROTO_ID = 2; // unique protocol id

  public App() {
    super("DcrApp", PROTO_ID);
  }

  @Override
  public void init(Properties properties) throws HandlerRegistrationException, IOException {
    try {
      super.init(properties);

      String name = properties.getProperty("target-name");

      // INSERT HERE
      switch (name) {
        case "Prosumer(0)":
          dcrGraph = DCRGraphClass.newBuilder(name)
              .addComputationAction("pInfo", "ProsumerInfo", "Prosumer(0)",
                  new ASTRecord(newHashSet(new RecordField("pid", new ASTNumber(0)))), UnitType.TYPE,
                  new EventMarking(false, false, true))
              .addInputSendOperation("consume", "!consume@{Prosumer(1), Prosumer(2)}", "Prosumer(0)",
                  newHashSet("Prosumer(1)", "Prosumer(2)"),
                  RecordType.newBuilder().add("t", StringType.TYPE).add("kw", IntType.TYPE).build(),
                  new EventMarking(false, false, true))
              .addReceiveOperation("reply_1", "?replyConsume@Prosumer(1)", "Prosumer(1)", "Prosumer(0)",
                  RecordType.newBuilder().add("price", IntType.TYPE).add("t", StringType.TYPE)
                      .add("kw", StringType.TYPE).build(),
                  new EventMarking(false, false, true))
              .addReceiveOperation("reply_2", "?replyConsume@Prosumer(2)", "Prosumer(2)", "Prosumer(0)",
                  RecordType.newBuilder().add("price", IntType.TYPE).add("t", StringType.TYPE)
                      .add("kw", StringType.TYPE).build(),
                  new EventMarking(false, false, true))
              .addInputSendOperation("accept_1", "!accept@Prosumer(1)", "Prosumer(0)", newHashSet("Prosumer(1)"),
                  RecordType.newBuilder().add("r", StringType.TYPE).build(), new EventMarking(false, false, true))
              .addInputSendOperation("reject_1", "!reject@Prosumer(1)", "Prosumer(0)", newHashSet("Prosumer(1)"),
                  RecordType.newBuilder().add("r", StringType.TYPE).build(), new EventMarking(false, false, true))
              .addInputSendOperation("accept_2", "!accept@Prosumer(2)", "Prosumer(0)", newHashSet("Prosumer(2)"),
                  RecordType.newBuilder().add("r", StringType.TYPE).build(), new EventMarking(false, false, true))
              .addInputSendOperation("reject_2", "!reject@Prosumer(2)", "Prosumer(0)", newHashSet("Prosumer(2)"),
                  RecordType.newBuilder().add("r", StringType.TYPE).build(), new EventMarking(false, false, true))
              .addRelation(Type.EXCLUDE, "reject_1", "reply_1")
              .addRelation(Type.EXCLUDE, "accept_1", "reply_1")
              .addRelation(Type.CONDITION, "reply_1", "reject_1")
              .addRelation(Type.RESPONSE, "reply_1", "reject_1")
              .addRelation(Type.EXCLUDE, "reject_1", "reject_1")
              .addRelation(Type.EXCLUDE, "accept_1", "reject_1")
              .addRelation(Type.EXCLUDE, "reject_2", "reply_2")
              .addRelation(Type.EXCLUDE, "accept_2", "reply_2")
              .addRelation(Type.CONDITION, "reply_2", "reject_2")
              .addRelation(Type.RESPONSE, "reply_2", "reject_2")
              .addRelation(Type.CONDITION, "reply_1", "accept_1")
              .addRelation(Type.RESPONSE, "reply_1", "accept_1")
              .addRelation(Type.EXCLUDE, "reject_1", "accept_1")
              .addRelation(Type.EXCLUDE, "accept_1", "accept_1")
              .addRelation(Type.EXCLUDE, "reject_2", "reply_2")
              .addRelation(Type.EXCLUDE, "accept_2", "reply_2")
              .addRelation(Type.CONDITION, "reply_2", "accept_2")
              .addRelation(Type.RESPONSE, "reply_2", "accept_2")
              .build();
          break;
        case "Prosumer(1)":
          dcrGraph = DCRGraphClass.newBuilder(name)
              .addReceiveOperation("consume", "?consume@Prosumer(0)", "Prosumer(0)", "Prosumer(1)",
                  RecordType.newBuilder().add("t", StringType.TYPE).add("kw", IntType.TYPE).build(),
                  new EventMarking(false, false, true))
              .addInputSendOperation("reply_1", "!replyConsume@Prosumer(0)", "Prosumer(1)", newHashSet("Prosumer(0)"),
                  RecordType.newBuilder().add("price", IntType.TYPE).add("t", StringType.TYPE)
                      .add("kw", StringType.TYPE).build(),
                  new EventMarking(false, false, true))
              .addReceiveOperation("accept_1", "?accept@Prosumer(0)", "Prosumer(0)", "Prosumer(1)",
                  RecordType.newBuilder().add("r", StringType.TYPE).build(), new EventMarking(false, false, true))
              .addReceiveOperation("reject_1", "?reject@Prosumer(0)", "Prosumer(0)", "Prosumer(1)",
                  RecordType.newBuilder().add("r", StringType.TYPE).build(), new EventMarking(false, false, true))
              .addRelation(Type.CONDITION, "consume", "reply_1")
              .addRelation(Type.EXCLUDE, "accept_1", "reply_1")
              .addRelation(Type.EXCLUDE, "reject_1", "reply_1")
              .build();
          break;
        case "Prosumer(2)":
          dcrGraph = DCRGraphClass.newBuilder(name)
              .addReceiveOperation("consume", "?consume@Prosumer(0)", "Prosumer(0)", "Prosumer(2)",
                  RecordType.newBuilder().add("t", StringType.TYPE).add("kw", IntType.TYPE).build(),
                  new EventMarking(false, false, true))
              .addInputSendOperation("reply_2", "!replyConsume@Prosumer(0)", "Prosumer(2)", newHashSet("Prosumer(0)"),
                  RecordType.newBuilder().add("price", IntType.TYPE).add("t", StringType.TYPE)
                      .add("kw", StringType.TYPE).build(),
                  new EventMarking(false, false, true))
              .addReceiveOperation("accept_2", "?accept@Prosumer(0)", "Prosumer(0)", "Prosumer(2)",
                  RecordType.newBuilder().add("r", StringType.TYPE).build(), new EventMarking(false, false, true))
              .addReceiveOperation("reject_2", "?reject@Prosumer(0)", "Prosumer(0)", "Prosumer(2)",
                  RecordType.newBuilder().add("r", StringType.TYPE).build(), new EventMarking(false, false, true))
              .addRelation(Type.CONDITION, "consume", "reply_2")
              .addRelation(Type.EXCLUDE, "accept_2", "reply_2")
              .addRelation(Type.EXCLUDE, "reject_2", "reply_2")
              .build();
          break;

        default:
          throw new IllegalArgumentException("Unknown role: " + name);
      }

      readSystemIn();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static final ASTNode eventValueFieldAsASTNode(String eventId) {
    return new ASTDeref(
        new ASTRecordField("value", new ASTId(eventId)));
  }

  private static final <T> Set<T> newHashSet(T... objs) {
    Set<T> set = new HashSet<T>();
    Collections.addAll(set, objs);
    return set;
  }
}
