import app1.App;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pingpong1.PingPongProtocol;
import pt.unl.fct.di.novasys.babel.core.Babel;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Properties;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

        //Creates a new instance of babel
        Babel babel = Babel.getInstance();
        //Reads arguments from the command line and loads them into a Properties object
        Properties props = Babel.loadConfig(args, null);

        //Creates a new instance of the pingpong.PingPongProtocol
        /* PingPongProtocol pingPong = new PingPongProtocol(); */
        PingPongProtocol pingPongTotal = new PingPongProtocol();

        //Create a new instance of App
        App app = new App();

        //Registers the protocol in babel
        /* babel.registerProtocol(pingPong); */
        babel.registerProtocol(pingPongTotal);
        babel.registerProtocol(app);

        //Starts babel
        babel.start();

        // Initializes the protocol
        /* pingPong.init(props); */
        pingPongTotal.init(props);
        app.init(props);
    }

}