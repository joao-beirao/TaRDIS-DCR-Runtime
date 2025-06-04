import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.server.Server;
import protocols.application.DCRApp;
import protocols.application.utils.NetworkingUtilities;
import protocols.dcr.DistributedDCRProtocol;
import pt.unl.di.novasys.babel.webservices.rest.GenericREST;
import pt.unl.di.novasys.babel.webservices.utils.ServerConfig;
import pt.unl.di.novasys.babel.webservices.websocket.GenericWebSocket;
import pt.unl.fct.di.novalincs.babel.protocols.epidemicglobalview.EpidemicGlobalView;
import pt.unl.fct.di.novasys.babel.core.Babel;
import pt.unl.fct.di.novasys.babel.protocols.eagerpush.EagerPushGossipBroadcast;
import pt.unl.fct.di.novasys.network.data.Host;

import java.net.InetAddress;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;


public class Main {

    private static Logger logger;
    private static final String DEFAULT_CONF = "config.properties";

    public static void main(String[] args) throws Exception {
        Main.configLogger();
        Properties props = Babel.loadConfig(args, DEFAULT_CONF);

        // Babel Setup
        InetAddress address = InetAddress.getByName(props.getProperty("babel.address"));
        // logger.info(address.getHostAddress());
        // Peer myself = new Peer(address, Integer.parseInt(props.getProperty("babel.port")));
        // Peer myself = generatePeer(address, props);

        // Babel setup
        // InetAddress address = InetAddress.getByName(NetworkingUtilities.getAddress("eth0"));
        // Host self = new Host(address, Short.parseShort(props.getProperty("babel.port")));
        // Host myself = new Host(address, 9001);
        Babel babel = Babel.getInstance();

        // protocols
        // EagerPushGossipBroadcast bcast =
        //         new EagerPushGossipBroadcast("channel.gossip", props, myself);
        // EpidemicGlobalView epiGlobalView = new EpidemicGlobalView("epidemic", props, myself);
        DistributedDCRProtocol dcrProtocol = new DistributedDCRProtocol();
        DCRApp app = DCRApp.getInstance();

        // register protocols with babel
        // babel.registerProtocol(bcast);
        // babel.registerProtocol(epiGlobalView);
        babel.registerProtocol(dcrProtocol);
        babel.registerProtocol(app);

        // initialize protocols
        // bcast.init(props);
        // epiGlobalView.init(props);
        dcrProtocol.init(props);
        app.init(props);

        // setup REST server
         Set<Class<? extends GenericREST>> restServices = ServerConfig.generateRestServices
                 (props);
         Set<Class<? extends GenericWebSocket>> wsServices =
                 ServerConfig.generateWebsocketServices(props);
         ServletContextHandler serverContext =
                 ServerConfig.createServerContextWithStaticFiles(wsServices, restServices,
                         app,
                         props);
         Server server = ServerConfig.createServer(serverContext, props);

        // deploy Babel and Server processes
        babel.start();
        // logger.info("Babel node started on {}", myself.toString());
         server.start();
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
        return UUID.randomUUID() + ".log";
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

    // private static Peer generatePeer(InetAddress address, Properties props)
    //         throws UnknownHostException, SocketException {
    //     int port = Integer.parseInt(props.getProperty(Babel.PAR_DEFAULT_PORT, "8080"));
    //     return new Peer(address, port);
    // }

}