
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
