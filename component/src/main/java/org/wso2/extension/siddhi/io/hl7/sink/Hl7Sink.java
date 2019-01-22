package org.wso2.extension.siddhi.io.hl7.sink;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.llp.MinLowerLayerProtocol;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.DefaultXMLParser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.parser.XMLParser;
import org.apache.log4j.Logger;
import org.wso2.extension.siddhi.io.hl7.util.Hl7Constants;
import org.wso2.siddhi.annotation.Example;
import org.wso2.siddhi.annotation.Extension;
import org.wso2.siddhi.annotation.Parameter;
import org.wso2.siddhi.annotation.util.DataType;
import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.exception.ConnectionUnavailableException;
import org.wso2.siddhi.core.stream.output.sink.Sink;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.core.util.transport.DynamicOptions;
import org.wso2.siddhi.core.util.transport.OptionHolder;
import org.wso2.siddhi.query.api.definition.StreamDefinition;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This is a sample class-level comment, explaining what the extension class does.
 */

/**
 * Annotation of Siddhi Extension.
 * <pre><code>
 * eg:-
 * {@literal @}Extension(
 * name = "The name of the extension",
 * namespace = "The namespace of the extension",
 * description = "The description of the extension (optional).",
 * //Sink configurations
 * parameters = {
 * {@literal @}Parameter(name = "The name of the first parameter", type = "Supprted parameter types.
 *                              eg:{DataType.STRING,DataType.INT, DataType.LONG etc},dynamic=false ,optinal=true/false ,
 *                              if optional =true then assign default value according the type")
 *   System parameter is used to define common extension wide
 *              },
 * examples = {
 * {@literal @}Example({"Example of the first CustomExtension contain syntax and description.Here,
 *                      Syntax describe default mapping for SourceMapper and description describes
 *                      the output of according this syntax},
 *                      }
 * </code></pre>
 */

@Extension(
        name = "hl7",
        namespace = "sink",
        description = "The hl7 pushes the hl7 message into HAPI client using MLLP protocol ",
        parameters = {
                @Parameter(name = "host.name",
                        description = "The target host where the HL7 message will be pushed" ,
                        type = {DataType.STRING }),

                @Parameter(name = "port",
                        description = "This is the unique logical address used to establish the connection " +
                                "for the process.",
                        type = {DataType.INT}),

                @Parameter(name = "hl7.encoding",
                        description = "Encoding method of hl7",
                        type = {DataType.STRING}),

                @Parameter(name = "hl7.ack.encoding",
                        description = "Encoding method of hl7 to log the acknowledgment message",
                        optional = true, defaultValue = "ER7",
                        type = {DataType.STRING}),

                @Parameter(name = "charset",
                        description = "Character encoding method",
                        optional = true, defaultValue = "UTF-8",
                        type = {DataType.STRING}),

                @Parameter(name = "tls.enabled",
                        description = "Transport layer security",
                        optional = true, defaultValue = "false",
                        type = {DataType.BOOL}),

                @Parameter(name = "hl7.timeout",
                        description = "Sets the time that the initiator will wait for a response for a given message" +
                                " before timing out and throwing an exception",
                        optional = true, defaultValue = "10000",
                        type = {DataType.INT})
        },
        examples = {
                @Example(
                        syntax = "@App:name('TestExecutionPlanSink') \n" +
                        "@sink(type = 'hl7',\n" +
                                "host.name = 'localhost',\n" +
                                "port = 1080,\n" +
                                "hl7.encoding = 'ER7',\n" +
                                "@map(type = 'text'))\n" +
                                "define stream hl7stream(payload string);\n"
                        ,
                        description = "This publishes the HL7 messages using the MLLP protocol " +
                                "and receive and log the acknowledgement message.\n "
                ),
                @Example(
                        syntax = "@App:name('TestExecutionPlanSink') \n" +
                                "@sink(type = 'hl7',\n" +
                                "host.name = 'localhost',\n" +
                                "port = 1080,\n" +
                                "hl7.encoding = 'xml',\n" +
                                "@map(type = 'text'))\n" +
                                "define stream hl7stream(payload string);\n"
                        ,
                        description = "This publishes the HL7 messages using the MLLP protocol " +
                                "and receive and log the acknowledgement message.\n "
                )
        }
)

// for more information refer https://wso2.github.io/siddhi/documentation/siddhi-4.0/#sinks

public class Hl7Sink extends Sink {
    private static final Logger log = Logger.getLogger(Hl7Sink.class);

    private String hostName;
    private int port;
    private boolean tlsEnabled;
    private String charset;
    private String hl7Encoding;
    private String hl7AckEncoding;
    private int hl7Timeout;
    private Message message;
    private StreamDefinition streamDefinition;
    private Connection connection;
    private String responseString = null;




    /**
     * Returns the list of classes which this sink can consume.
     * Based on the type of the sink, it may be limited to being able to publish specific type of classes.
     * For example, a sink of type file can only write objects of type String .
     * @return array of supported classes , if extension can support of any types of classes
     * then return empty array .
     */
    @Override
    public Class[] getSupportedInputEventClasses() {
            return new Class[]{String.class};
    }

    /**
     *  Returns a list of supported dynamic options (that means for each event value of the option can change) by
     *  the transport
     *
     *  @return the list of supported dynamic option keys
     */
    @Override
    public String[] getSupportedDynamicOptions() {
            return new String[0];
    }

    /**
     * The initialization method for {@link Sink}, will be called before other methods. It used to validate
     * all configurations and to get initial values.
     * @param streamDefinition  containing stream definition bind to the {@link Sink}
     * @param optionHolder            Option holder containing static and dynamic configuration related
     *                                to the {@link Sink}
     * @param configReader            to read the sink related system configuration.
     * @param siddhiAppContext        the context of the {@link SiddhiApp} used to
     *                                get siddhi related utility functions.
     */
    @Override
    protected void init(StreamDefinition streamDefinition, OptionHolder optionHolder, ConfigReader configReader,
            SiddhiAppContext siddhiAppContext) {
            this.streamDefinition = streamDefinition;
            this.hostName = optionHolder.validateAndGetStaticValue(Hl7Constants.HL7_SERVER_HOST_NAME);
            this.port = Integer.parseInt(optionHolder.validateAndGetStaticValue(Hl7Constants.HL7_PORT_NO));
            this.hl7Encoding = optionHolder.validateAndGetStaticValue(Hl7Constants.HL7_ENCODING, Hl7Constants.DEFAULT_HL7_ENCODING);
            this.charset = optionHolder.validateAndGetStaticValue(Hl7Constants.CHARSET_NAME, Hl7Constants.DEFAULT_HL7_CHARSET);
            this.hl7AckEncoding = optionHolder.validateAndGetStaticValue(Hl7Constants.ACK_HL7_ENCODING, Hl7Constants.DEFAULT_ACK_HL7_ENCODING);
            this.tlsEnabled = Boolean.parseBoolean(optionHolder.validateAndGetStaticValue(Hl7Constants.TLS_ENABLE, Hl7Constants.DEFAULT_TLS_ENABLE));
            this.hl7Timeout = Integer.parseInt(optionHolder.validateAndGetStaticValue(Hl7Constants.HL7_TIMEOUT, Hl7Constants.DEFAULT_HL7_TIMEOUT));

    }

    /**
     * This method will be called when events need to be published via this sink
     * @param payload        payload of the event based on the supported event class exported by the extensions
     * @param dynamicOptions holds the dynamic options of this sink and Use this object to obtain dynamic options.
     * @throws ConnectionUnavailableException if end point is unavailable the ConnectionUnavailableException thrown
     *                                        such that the  system will take care retrying for connection
     */
    @Override
    public void publish(Object payload, DynamicOptions dynamicOptions) throws ConnectionUnavailableException {
        Initiator initiator = connection.getInitiator();
        String payload1 = (String) payload;


       /*  */
        PipeParser pipeParser = new PipeParser();
        XMLParser xmlParser = new DefaultXMLParser();
        try {
            if (hl7Encoding.toUpperCase(Locale.ENGLISH).equals("ER7")) {

                //int x = payload1.indexOf("\"");
                //payload1 = payload1.substring(x+1);
                //payload1 = payload1.substring(0, payload1.length() - 1);
                // Todo This parse the message into pipe format
                message = pipeParser.parse(payload1);
                //payload1 = payload1.replaceAll("\\r", "\n");
               // System.out.println(payload1);
            } else if (hl7Encoding.toUpperCase(Locale.ENGLISH).equals("XML")) {
                message = xmlParser.parse(payload1);
            } else {
                log.error("Error occurred when parsing message, check the hl7.encoding =  " +
                    hl7Encoding + "defined in " + streamDefinition);
            }

            try {
                initiator.setTimeout(hl7Timeout, TimeUnit.MILLISECONDS);
                // Todo This sends the message to hl7 server and receive the response..Issue is this sends as payload:"MSH|^~\&|NES|NINT...But it has to be strat at MSH|^~\...
                Message response = initiator.sendAndReceive(message);
                if (hl7AckEncoding.toUpperCase(Locale.ENGLISH).equals("ER7")) {
                    responseString = pipeParser.encode(response);
                    responseString = responseString.replaceAll("\\r", "\n");
                } else if (hl7AckEncoding.toUpperCase(Locale.ENGLISH).equals("XML")) {
                    responseString = xmlParser.encode(response);
                } else {
                    log.error ("Error occurred when parsing message, check the hl7.encoding =  "
                            + hl7Encoding + "defined " +
                            "in " + streamDefinition);
                }
                log.info("Received Response:\n"+responseString);
                if (log.isDebugEnabled()) {
                    log.debug(responseString);
                }

            } catch (LLPException | IOException e) {

                if (log.isDebugEnabled()) {
                    log.error ("Error: " + e);
                }
            }


        } catch (HL7Exception e) {
            log.error ("Error occurred when parsing message, check the hl7.encoding =  "
                    + hl7Encoding + "defined in " + streamDefinition, e);

            //if (log.isDebugEnabled()) {
            //    log.error("Error: "+ e);
           // }
        }
    }

    /**
     * This method will be called before the processing method.
     * Intention to establish connection to publish event.
     * @throws ConnectionUnavailableException if end point is unavailable the ConnectionUnavailableException thrown
     *                                        such that the  system will take care retrying for connection
     */
    @Override
    public void connect() throws ConnectionUnavailableException {
        HapiContext hapiContext = new DefaultHapiContext();
        try {
            MinLowerLayerProtocol mllp = new MinLowerLayerProtocol();
            mllp.setCharset(charset);
            hapiContext.setLowerLayerProtocol(mllp);
            connection = hapiContext.newClient(hostName, port, tlsEnabled);
            System.out.println(charset);
            log.info (" Executing HL7Sender : HOST:" + hostName + " PORT :" + port);

        } catch (HL7Exception e) {
            //log.error("Error creating the hapi client"+ e);
            throw new ConnectionUnavailableException ("Error while connecting with the HL7 server, check " +
                    "the host.name = " + hostName + ": port= " + port + " defined in " + streamDefinition , e);
        }
    }

    /**
     * Called after all publishing is done, or when {@link ConnectionUnavailableException} is thrown
     * Implementation of this method should contain the steps needed to disconnect from the sink.
     */
    @Override
    public void disconnect() {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * The method can be called when removing an event receiver.
     * The cleanups that have to be done after removing the receiver could be done here.
     */
    @Override
    public void destroy() {

    }

    /**
     * Used to collect the serializable state of the processing element, that need to be
     * persisted for reconstructing the element to the same state on a different point of time
     * This is also used to identify the internal states and debugging
     * @return all internal states should be return as an map with meaning full keys
     */
    @Override
    public Map<String, Object> currentState() {
            return null;
    }

    /**
     * Used to restore serialized state of the processing element, for reconstructing
     * the element to the same state as if was on a previous point of time.
     *
     * @param map the stateful objects of the processing element as a map.
     *              This map will have the  same keys that is created upon calling currentState() method.
     */
    @Override
    public void restoreState(Map<String, Object> map) {

    }
}

