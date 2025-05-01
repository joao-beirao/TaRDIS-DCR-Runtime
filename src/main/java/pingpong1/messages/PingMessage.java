package pingpong1.messages;

import dcr1.common.events.Event;
import dcr1.common.events.userset.values.UserVal;
import io.netty.buffer.ByteBuf;
import pt.unl.fct.di.novasys.babel.generic.ProtoMessage;
import pt.unl.fct.di.novasys.network.ISerializer;

import java.io.*;

public class PingMessage
        extends ProtoMessage {

    public static final short MSG_ID = 101;

    private final int pingId;
    private final String message;
    private final Event.Marking marking;

    private final UserVal sender;
    //
    private final String idExtensionToken;

    // private final String marking;

    // public PingMessage(int pingId, String message, String marking) {
    public PingMessage(int pingId, String message, Event.Marking marking, UserVal sender,
            String idExtensionToken) {
        super(MSG_ID);
        this.pingId = pingId;
        this.message = message;
        this.marking = marking;
        this.sender = sender;
        this.idExtensionToken = idExtensionToken;
    }

    public int getPingId() {
        return pingId;
    }

    public String getMessage() {
        return message;
    }

    public Event.Marking getMarking() {
        return marking;
    }

    public String getIdExtensionToken() {return idExtensionToken; }

    public UserVal getSender() {return sender;}

    public static ISerializer<? extends ProtoMessage> serializer = new ISerializer<PingMessage>() {
        public void serialize(PingMessage msg, ByteBuf out) {

            out.writeInt(msg.pingId);
            Utils.encodeUTF8(msg.message, out);
            Utils.encodeUTF8(msg.getIdExtensionToken(), out);
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 ObjectOutputStream outStream = new ObjectOutputStream(bos)) {
                outStream.writeObject(msg.getMarking());
                outStream.flush();
                out.writeInt(bos.size());
                out.writeBytes(bos.toByteArray());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 ObjectOutputStream outStream = new ObjectOutputStream(bos)) {
                outStream.writeObject(msg.getSender());
                outStream.flush();
                out.writeInt(bos.size());
                out.writeBytes(bos.toByteArray());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        }

        public PingMessage deserialize(ByteBuf in) {
            int pingId = in.readInt();
            String message = Utils.decodeUTF8(in);
            String idExtensionToken = Utils.decodeUTF8(in);
            byte[] markingBytes = new byte[in.readInt()];
            in.readBytes(markingBytes);
            byte[] senderBytes = new byte[in.readInt()];
            in.readBytes(senderBytes);
            Event.Marking val = null;
            UserVal sender = null;
            try (ByteArrayInputStream bis = new ByteArrayInputStream(markingBytes);
                 ObjectInputStream inStream = new ObjectInputStream(bis)) {
                val = (Event.Marking) inStream.readObject();
                System.out.println("my marking" + val);
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try (ByteArrayInputStream bis = new ByteArrayInputStream(senderBytes);
                 ObjectInputStream inStream = new ObjectInputStream(bis)) {
                sender = (UserVal) inStream.readObject();
                System.out.println("my marking" + val);
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return new PingMessage(pingId, message, val, sender, idExtensionToken);
            // String marking = Utils.decodeUTF8(in);
            // return new PingMessage(pingId, message, marking);
        }
    };

}
