package pingpong.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import dcr.EventMarking;
import io.netty.buffer.ByteBuf;
import pt.unl.fct.di.novasys.babel.generic.ProtoMessage;
import pt.unl.fct.di.novasys.network.ISerializer;

public class PingMessage extends ProtoMessage {

  public static final short MSG_ID = 101;

  private final int pingId;
  private final String message;
  private final EventMarking marking;
  // private final String marking;

  // public PingMessage(int pingId, String message, String marking) {
  public PingMessage(int pingId, String message, EventMarking marking) {
    super(MSG_ID);
    this.pingId = pingId;
    this.message = message;
    this.marking = marking;
  }

  public int getPingId() {
    return pingId;
  }

  public String getMessage() {
    return message;
  }

  // public String getMarking() {
  // return marking;
  // }

  public EventMarking getMarking() {
    return marking;
  }

  public static ISerializer<? extends ProtoMessage> serializer = new ISerializer<PingMessage>() {
    public void serialize(PingMessage msg, ByteBuf out) {

      out.writeInt(msg.pingId);
      Utils.encodeUTF8(msg.message, out);
      try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
          ObjectOutputStream outStream = new ObjectOutputStream(bos)) {
        outStream.writeObject(msg.getMarking());
        outStream.flush();
        out.writeInt(bos.size());
        out.writeBytes(bos.toByteArray());
      } catch (Exception ex) {
        throw new RuntimeException(ex);
        // Utils.encodeUTF8(msg.marking, out);
      }
    }

    public PingMessage deserialize(ByteBuf in) {
      int pingId = in.readInt();
      String message = Utils.decodeUTF8(in);
      byte[] markingBytes = new byte[in.readInt()];
      in.readBytes(markingBytes);
      EventMarking val = null;

      try (ByteArrayInputStream bis = new ByteArrayInputStream(markingBytes);
          ObjectInputStream inStream = new ObjectInputStream(bis)) {
        val = (EventMarking) inStream.readObject();
        System.out.println("my marking" + val);

      } catch (ClassNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return new PingMessage(pingId, message, val);
      // String marking = Utils.decodeUTF8(in);
      // return new PingMessage(pingId, message, marking);
    }
  };

}
