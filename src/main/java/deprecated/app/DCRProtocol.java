package deprecated.app;

// import static pt.unl.fct.di.novasys.babel.core.GenericProtocol.logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import deprecated.dcr.DCRGraphClass;
import deprecated.dcr.EventMarking;
import deprecated.dcr.ExecutionResult;
import deprecated.dcr.ast.DynamicTypeCheckException;
import deprecated.dcr.ast.UndeclaredIdentifierException;
import deprecated.dcr.ast.values.NumberVal;
import deprecated.dcr.ast.values.RecordFieldVal;
import deprecated.dcr.ast.values.RecordVal;
import deprecated.dcr.ast.values.StringVal;
import deprecated.dcr.ast.values.Value;
import deprecated.pingpong.PingPongProtocol;
import deprecated.pingpong.requests.DcrReply;
import deprecated.pingpong.requests.DcrRequest;
import pt.unl.fct.di.novasys.babel.core.GenericProtocol;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;
import pt.unl.fct.di.novasys.network.data.Host;

public abstract class DCRProtocol extends GenericProtocol {

  // TODO: Get all logic from App class of the dcr graph and put it here

  // private static final short PROTO_ID = 403; // unique protocol id

  protected DCRGraphClass dcrGraph; // the dcr graph

  protected static final Logger logger = LogManager.getLogger(App.class); // logger for the protocol
  protected DcrRequest request = null;
  protected static final int DEFAULT_PORT = 9000; // default port to listen on
  protected int received = 0;

  public DCRProtocol(String protoName, short id) {
    super(protoName, id);
  }

  public void init(Properties properties) throws HandlerRegistrationException, IOException {

    // register protocol handlers
    // register reply handler
    registerReplyHandler(DcrReply.REPLY_ID, this::onPongReply);

    // register request handlers
    registerRequestHandler(DcrRequest.REQUEST_ID, this::uponReceiveDcrRequest);
  }

  // TODO safe remove
  /**
   * Handle Pong Reply
   *
   * @param pongReply   pong reply message
   * @param sourceProto source protocol
   */
  private void onPongReply(DcrReply pongReply, short sourceProto) {
    // System.out.println("Received reply from " + pongReply.getDestination() + " in
    // " + pongReply.getRTT() + "ms");
    logger.info("Received reply from " + pongReply.getDestination() + " in " + pongReply.getRTT() + "ms");
    received++;
    // FIXME: rearrange this part
    if (received == 1) {
      readSystemIn();
    }
  }

  private void uponReceiveDcrRequest(DcrRequest dcrRequest, short sourceProtocol) {
    logger.info("@App: received dcr request to execute event {} with value {}", dcrRequest.getEventId(),
        dcrRequest.getMarking());
    try {
      // FIXME:
      String event = dcrRequest.getEventId();
      // EventMarking params = JSON.decode(dcrRequest.getMarking(),
      // EventMarking.class);
      logger.info("received params (event marking) {}", dcrRequest.getMarking());
      execute_recv(dcrGraph, event, dcrRequest.getMarking());
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Error reading command: " + e.getMessage());
    }
  }

  /**
   * Reads commands from stdin
   */
  void readSystemIn() {
    Scanner in = new Scanner(System.in);
    String line = "";

    while (!line.equals("quit")) {
      try {
        System.out.println("\n('help' for available commands)");
        System.out.print("        <select command> % ");

        // While there is something to read
        line = in.nextLine();
        if (line == null) {
          System.exit(1);
        }

        readCommand(line);

      } catch (Exception e) {
        // TODO: Handle event exception in here
        e.printStackTrace();
        logger.error("Error reading command: " + e.getMessage());
      }
    }

    System.exit(0);

  }

  /**
   * Reads a command from stdin and executes it
   *
   * @param line command to execute
   */
  void readCommand(String line) throws UnknownHostException, InterruptedException {
    StringTokenizer tokenizer = new StringTokenizer(line);

    if (!tokenizer.hasMoreTokens()) {
      return;
    }

    String cmd = tokenizer.nextToken();
    String event, input;
    switch (cmd) {
      case "help":
        help();
        break;
      case "list":
        list(this.dcrGraph);
        break;
      case "debug":
        debugGraph(this.dcrGraph);
        break;
      case "input":
        if (tokenizer.countTokens() != 2)
          return;
        event = tokenizer.nextToken();
        input = tokenizer.nextToken();
        execute_input(dcrGraph, event, parseInputVal(input));
        break;
      case "compute":
        // computation action
        event = tokenizer.nextToken();
        execute_comp(dcrGraph, event);
        break;
      case "input-send":
        // input-send interaction
        if (tokenizer.countTokens() != 2)
          return;
        event = tokenizer.nextToken();
        input = tokenizer.nextToken();
        execute_input_send(dcrGraph, event, parseInputVal(input));
        break;
      case "compute-send":
        // computation-send interaction
        event = tokenizer.nextToken();
        execute_computation_send(dcrGraph, event);
        break;
      case "quit":
        System.exit(0);
        break;
      default:
        System.out.println("Unknown command: " + cmd);
        break;
    }

  }

  private static final void help() {
    System.out.println("\nHelp");
    System.out.println("---");
    System.out.println("list :\n\tlist all enabled events");
    System.out.println("debug :\n\tlist all events in this end-point");
    System.out.println("input <event_label> <input_value> :\n\t(local action) execute an input event");
    System.out.println("compute <event_label> :\n\t(local action) execute a local computation event");
    System.out
        .println("input-send <event_label> <input_value> :\n\t(interaction) execute an input send operation event");
    System.out.println("compute-send <event_label> :\n\t(interaction) execute an input send operation event");
    System.out.println("quit :\n\texit the application");
    System.out.println("---");
  }

  private static final void list(DCRGraphClass dcrGraph) {
    System.out.println("\nEnabled events");
    System.out.println("---");
    dcrGraph.getEnabledEvents().forEach(e -> System.out.println(" >  " + e.unparse()));
    System.out.println("---");
  }

  private static final void debugGraph(DCRGraphClass dcrGraph) {
    System.out.println("\nAll events");
    System.out.println("---");
    dcrGraph.getEvents().forEach(e -> System.out.println(" >  " + e.unparse()));
    System.out.println("---");
  }

  private static final void execute_input(DCRGraphClass dcrGraph, String eventId, Value inputValue) {
    logger.info("Executing input action '{}' with input value {}", eventId, inputValue);
    try {
      dcrGraph.executeInputAction(eventId, inputValue);
    } catch (UndeclaredIdentifierException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }

  private static final void execute_comp(DCRGraphClass dcrGraph, String eventId) {
    logger.info("Executing ComputationAction '{}'...", eventId);
    try {
      ExecutionResult result = dcrGraph.executeComputationAction(eventId);
      logger.info("ComputationAction executed. Event marking updated to {}", result.getMarking());
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }

  private final void execute_computation_send(DCRGraphClass dcrGraph, String eventId) {
    logger.info("Executing SendOperation '{}'", eventId);
    try {
      ExecutionResult result = dcrGraph.executeComputationSendOperation(eventId);
      Iterable<String> receivers = result.getReceivers();
      // TODO come back to this - getting without testing
      sendMessage(translate_participants(receivers), eventId, result.getMarking());
      logger.info("SendOperation executed. Event marking updated to {}", result.getMarking());
      // update reply to graphical client with diffs (events and relations - modified
      // or created)
    } catch (DynamicTypeCheckException e) {
      System.err.println(e.getInfo());
      e.printStackTrace();
    } catch (UndeclaredIdentifierException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }

  private final void execute_input_send(DCRGraphClass dcrGraph, String eventId, Value inputValue) {
    logger.info("Executing SendOperation '{}'", eventId);
    try {
      ExecutionResult result = dcrGraph.executeInputSendOperation(eventId, inputValue);
      System.err.println("Executing input-send: " + eventId);
      Iterable<String> receivers = result.getReceivers();
      // TODO come back to this - getting without testing
      // translate_participants(receivers);
      sendMessage(translate_participants(receivers), eventId, result.getMarking());
      logger.info("SendOperation executed. Event marking updated to {}", result.getMarking());
      // update reply to graphical client with diffs (events and relations - modified
      // or created)
    } catch (DynamicTypeCheckException e) {
      System.err.println(e.getInfo());
      e.printStackTrace();
    } catch (UndeclaredIdentifierException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }

  private static final void execute_recv(DCRGraphClass dcrGraph, String eventId, EventMarking marking) {
    logger.info("Executing receive operation '{}': received {}", eventId, marking);
    try {
      dcrGraph.executeReceiveOperation(eventId, marking);
    } catch (UndeclaredIdentifierException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.err.println(e.getMessage());
    }
  }

  private final void sendMessage(Iterable<String> receivers, String eventId, EventMarking marking) {
    try {
      for (String receiver : receivers) {
        InetAddress targetAddr = InetAddress.getByName(receiver);
        Host destination = new Host(targetAddr, DEFAULT_PORT);
        // request = new DcrRequest(eventId, JSON.encode(marking), destination);
        request = new DcrRequest(eventId, marking, destination);
        logger.info("Sending message to receiver {}...", receiver);
        sendRequest(request, PingPongProtocol.PROTO_ID);
        logger.info("  Message Sent.");
      }
    } catch (Exception e1) {
      logger.warn("Error executing event: " + e1.getMessage());
    }
  }

  private static final Iterable<String> translate_participants(Iterable<String> participants) {
    List<String> refactored = new LinkedList<>();
    for (String participant : participants) {
      refactored.add(participant.replace("(", "_").replace(")", ""));
    }
    return refactored;
  }

  // ========== (very primitive && ugly) input value parsing
  // =======================
  // (for the sake of demonstration only - will be gone in the end)

  private static Value parseInputVal(String input) {
    input = input.trim();
    if (input.isEmpty())
      throw new IllegalArgumentException("Not implemented: empty input value not allowed at this point");
    if (input.startsWith("{") && input.endsWith("}")) {
      return parseRecordVal(input.substring(1, input.length() - 1).trim());
    }
    if (input.startsWith("'") && input.endsWith("'")) {
      String string_val = input.substring(1, input.length() - 1);
      return new StringVal(input.substring(1, input.length() - 1));
    }
    // Boolean
    try {
      return new NumberVal(Integer.parseInt(input));
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Unable to parse input value");
    }
  }

  // a:"22"
  // ab:3;c:{a:55;b:"string"}
  // input 1 {a:2;b:'a';c:{d:1;ab:'d'}}
  private static RecordVal parseRecordVal(String content) {
    if (content.isEmpty())
      throw new IllegalArgumentException("Expecting record fields: empty record not supported");
    RecordVal.Builder builder = RecordVal.builder();
    String rest = content;
    while (!rest.isEmpty()) {
      int field_end_pos = -1;
      int next_field_pos = -1;

      int field_assign_pos = rest.indexOf(":");

      if (rest.charAt(field_assign_pos + 1) == '{') {
        // record field value follows - use '}' do detect end of record
        field_end_pos = rest.indexOf("}");
        if (field_end_pos < rest.length() - 1 && rest.charAt(field_end_pos + 1) == ';') {
          // one more field after this one
          next_field_pos = field_end_pos;

        } else {
          // this is the last field
          next_field_pos = rest.length();
        }
        builder.addField(parseRecordFieldVal(rest.substring(0, field_end_pos + 1)));
      } else {
        // primitive field value (not record)
        next_field_pos = rest.indexOf(";");
        if (next_field_pos == -1) {
          // this is the last field
          field_end_pos = rest.length();
          next_field_pos = rest.length();
        } else {
          // one more field after this one
          field_end_pos = next_field_pos;
          ++next_field_pos;
        }
        builder.addField(parseRecordFieldVal(rest.substring(0, field_end_pos)));
      }
      rest = rest.substring(next_field_pos);
    }
    return builder.build();
  }

  private static final RecordFieldVal parseRecordFieldVal(String field) {
    int split_pos = field.indexOf(":");
    String name = field.substring(0, split_pos);
    String val_field = field.substring(split_pos + 1, field.length());
    Value val = parseInputVal(val_field);
    return new RecordFieldVal(name, val);
  }

  // @Override
  // public Set<User> send(UserSetExprInstance receivers) {
  //   throw new RuntimeException("Not yet implemented");
  // }
}