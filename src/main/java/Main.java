import app1.DCRApp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import app1.protocols.pingpong.PingPongProtocol;
import pt.unl.fct.di.novasys.babel.core.Babel;
import java.util.Properties;
import java.util.UUID;


public class Main {

    private static Logger logger;
    private static final String DEFAULT_CONF = "config.properties";

    // private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        Main.configLogger();


        Babel babel = Babel.getInstance();

        // (arguments from the command line loaded into the Properties object)
        Properties props = Babel.loadConfig(args, null);

        // Babel Setup
        // InetAddress address = InetAddress.getByName(props.getProperty("babel.address"));
        // logger.info(address.getHostAddress());
        //
        // Peer myself = new Peer(address, Integer.parseInt(props.getProperty("babel.port")));

        // Peer myself = generatePeer(address, props);


        // logger.info("Babel node started on {}", myself.toString());
        // logger.info("Babel node started on {}", "myself.toString()");


        // App appREST = App.getInstance();


        // EpidemicGlobalView epiView = new EpidemicGlobalView("channelName", props, null);
        //
        //

        // Nimbus nimbus = new Nimbus(myself, props);


        // DCRProtocol1 pingPongTotal = new DCRProtocol1();
        PingPongProtocol pingPongTotal = new PingPongProtocol();
        DCRApp app = new DCRApp(props);

        babel.registerProtocol(pingPongTotal);
        babel.registerProtocol(app);
        // babel.registerProtocol(appREST);



        pingPongTotal.init(props);
        app.init(props);
        // appREST.init(props);

        babel.start();

        logger.info("Babel node started on {}", "myself.toString()");

        // Set<Class<? extends GenericREST>> restServices = ServerConfig.generateRestServices(props);
        // Set<Class<? extends GenericWebSocket>> wsServices =
        //         ServerConfig.generateWebsocketServices(props);
        // ServletContextHandler serverContext =
        //         ServerConfig.createServerContextWithStaticFiles(wsServices, restServices, appREST,
        //                 props);





        // Server server = ServerConfig.createServer(serverContext, props);
        // server.start();
        // logger.info("Server started on {}", server.getURI());


        Runtime.getRuntime().addShutdownHook(new Thread(() -> logger.info("Server stopped!")));

    }



    private static void configLogger() {
        System.setProperty("log4j.configurationFile", "log4j2.xml");
        if (System.getProperty("logFileName") == null) {
            System.setProperty("logFileName", generateLogFileName());
        }

        logger = LogManager.getLogger(Main.class);
    }

    private static String generateLogFileName() {
        return UUID.randomUUID().toString() + ".log";
    }


    // private static InetAddress getAddress(Properties props) throws UnknownHostException,
    // SocketException {
    //     String babelAddress=null;
    //
    //     if (props.containsKey(Babel.PAR_DEFAULT_ADDRESS))
    //         babelAddress = props.getProperty(Babel.PAR_DEFAULT_ADDRESS);
    //     else if (props.containsKey(Babel.PAR_DEFAULT_INTERFACE))
    //         babelAddress = NetworkingUtilities.getAddress(props.getProperty(Babel
    //         .PAR_DEFAULT_INTERFACE));
    //
    //     return InetAddress.getByName(babelAddress);
    // }
    // //
    // private static Peer generatePeer(InetAddress address, Properties props)
    //         throws UnknownHostException, SocketException {
    //     int port = Integer.parseInt(props.getProperty(Babel.PAR_DEFAULT_PORT, "8080"));
    //     return new Peer(address, port);
    // }


}