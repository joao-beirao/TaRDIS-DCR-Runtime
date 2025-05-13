package app1;

import app1.presentation.endpoint.EndpointDTO;
import app1.presentation.mappers.EndpointMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import dcr1.common.Record;
import dcr1.common.data.types.BooleanType;
import dcr1.common.data.types.IntegerType;
import dcr1.common.data.types.StringType;
import dcr1.common.data.types.Type;
import dcr1.common.data.values.*;
import dcr1.common.events.userset.values.UserVal;
import dcr1.model.GraphModel;
import dcr1.runtime.GraphRunner;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.stream.Collectors;

// docker run example
// babel % docker run --network tardis-babel-backend-net --rm -h P_1_2 --name P_1_2 -it dcr-babel
// interface=eth0 role=P cid=1 pid=2


// TODO revisit bootstrap config process - currently loading a json resource based a role
public final class DCRApp
        extends DCRProtocol1
        implements GraphObserver {

    private static final String CLI_ROLE_ARG = "role";


    private static UserVal instantiateSelf(Properties props, Endpoint.Role roleDecl) {
        return UserVal.of(roleDecl.roleName(), Record.ofEntries(roleDecl.params()
                .stream()
                .map(param -> fetchSelfParamField(props, param.name(), param.value()))
                .collect(Collectors.toMap(Record.Field::name, Record.Field::value))));
    }
    // private static RecordVal instantiateSelf(Properties props, Endpoint.Role roleDecl) {
    //     return RecordVal.of(Record.ofEntries(roleDecl.params()
    //             .stream()
    //             .map(param -> fetchSelfParamField(props, param.name(), param.value()))
    //             .collect(Collectors.toMap(Record.Field::name, Record.Field::value))));
    // }

    private static Record.Field<Value> fetchSelfParamField(Properties props,
            String key, Type type) {
        var prop = props.getProperty(key);
        return Record.Field.of(key, switch (type) {
            case BooleanType ignored -> BoolVal.of(Boolean.parseBoolean(prop));
            case IntegerType ignored -> IntVal.of(Integer.parseInt(prop));
            case StringType ignored -> StringVal.of(prop);
            default -> throw new IllegalStateException("Unexpected value for role param: " + type);
        });
    }


    // private static RecordExpr instantiateSelf(Properties props, Endpoint.Role roleDecl) {
    //     return RecordExpr.of(Record.ofEntries(roleDecl.params()
    //             .stream()
    //             .map(param -> fetchSelfParamField(props, param.name(), param.value()))
    //             .collect(Collectors.toMap(Record.Field::name, Record.Field::value))));
    // }

    // private static RecordExpr instantiateSelf(Properties props, Endpoint.Role roleDecl) {
    //     return RecordExpr.of(Record.ofEntries(roleDecl.params()
    //             .stream()
    //             .map(param -> fetchSelfParamField(props, param.name(), param.value()))
    //             .collect(Collectors.toMap(Record.Field::name, Record.Field::value))));
    // }
    //
    // private static Value fetchSelfParam(Properties props, String key, Type type) {
    //     var prop = props.getProperty(key);
    //     return switch (type) {
    //         case BooleanType ignored -> BoolVal.of(Boolean.parseBoolean(prop));
    //         case IntegerType ignored -> IntVal.of(Integer.parseInt(prop));
    //         case StringType ignored -> StringVal.of(prop);
    //         default -> throw new IllegalStateException("Unexpected value for role param: " + type);
    //     };
    // }
    //
    // private static Record.Field<ComputationExpression> fetchSelfParamField(Properties props,
    //         String key, Type type) {
    //     var prop = props.getProperty(key);
    //     return Record.Field.of(key, switch (type) {
    //         case BooleanType ignored -> BoolLiteral.of(Boolean.parseBoolean(prop));
    //         case IntegerType ignored -> IntLiteral.of(Integer.parseInt(prop));
    //         case StringType ignored -> StringLiteral.of(prop);
    //         default -> throw new IllegalStateException("Unexpected value for role param: " + type);
    //     });
    // }

    public DCRApp(Properties props) {
        super(props);
    }

    private static Endpoint decodeEndpoint(String jsonEncodedEndpoint)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        var deserializedEndpoint = objectMapper.readValue(jsonEncodedEndpoint, EndpointDTO.class);
        return EndpointMapper.mapEndpoint(deserializedEndpoint);
    }



    @Override
    public void init(Properties properties) throws HandlerRegistrationException, IOException {
        super.init(properties);
        // observation: Bootstrap is currently supported by CLI params:
        // - a 'role' param is required, and determines the role this endpoint should enact - based
        // on this param, the json-encoded endpoint resource is loaded and used to instantiate,
        // both the DCR Model and the Role for the active participant;
        // - similarly, CLI params are used to inject the runtime parameter values for the
        // role (when applicable), and are expected to follow the parameter names declared by the
        // selected endpoint.
        logger.info("role property: {}", properties.getProperty(CLI_ROLE_ARG));

        try (InputStream in = DCRApp.class.getResourceAsStream(
                String.format("/%s", properties.getProperty(CLI_ROLE_ARG)))) {
            assert in != null;

            // the user behind this endpoint's
            UserVal self;
            // the projection this endpoint will run
            GraphModel graphModel;
            {
                // load the information required to deploy this endpoint
                var jsonEncodedEndpoint = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                var endpoint = decodeEndpoint(jsonEncodedEndpoint);
                // inject runtime parameters into self
                var roleDecl = endpoint.role();
                self = instantiateSelf(properties, endpoint.role());
                graphModel = endpoint.graphModel();
            }

            // TODO remove debug
            System.err.println(graphModel);

            // aggregates CLI-based functionality and callbacks (replaceable with GUI)
            CLI cmdLineRunner =  new CLI(this);

            // boilerplate
            runner = new GraphRunner(self, this);
            runner.init(graphModel);
            runner.registerGraphObserver(this);


            // TODO remove debug
            System.err.println(runner);

            // kickstart interaction
            cmdLineRunner.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
