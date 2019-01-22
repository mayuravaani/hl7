package org.wso2.extension.siddhi.io.hl7.source;

import ca.uhn.hl7v2.DefaultHapiContext;

import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.HL7Service;
import ca.uhn.hl7v2.llp.MinLowerLayerProtocol;

import org.apache.log4j.Logger;
import org.wso2.extension.siddhi.io.hl7.util.Hl7Constants;
import org.wso2.siddhi.annotation.Example;
import org.wso2.siddhi.annotation.Extension;
import org.wso2.siddhi.annotation.Parameter;
import org.wso2.siddhi.annotation.util.DataType;
import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.exception.ConnectionUnavailableException;
import org.wso2.siddhi.core.exception.SiddhiAppCreationException;
import org.wso2.siddhi.core.stream.input.source.Source;
import org.wso2.siddhi.core.stream.input.source.SourceEventListener;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.core.util.transport.OptionHolder;


import java.util.Locale;
import java.util.Map;

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
 * //Source configurations
 * parameters = {
 * {@literal @}Parameter(name = "The name of the first parameter",
 *                               description= "The description of the first parameter",
 *                               type =  "Supported parameter types.
 *                                        eg:{DataType.STRING, DataType.INT, DataType.LONG etc}",
 *                               dynamic= "false
 *                                         (if parameter doesn't depend on each event then dynamic parameter is false.
 *                                         In Source, only use static parameter)",
 *                               optional= "true/false, defaultValue= if it is optional then assign a default value
 *                                          according to the type."),
 * {@literal @}Parameter(name = "The name of the second parameter",
 *                               description= "The description of the second parameter",
 *                               type =   "Supported parameter types.
 *                                         eg:{DataType.STRING, DataType.INT, DataType.LONG etc}",
 *                               dynamic= "false
 *                                         (if parameter doesn't depend on each event then dynamic parameter is false.
 *                                         In Source, only use static parameter)",
 *                               optional= "true/false, defaultValue= if it is optional then assign a default value
 *                                         according to the type."),
 * },
 * //If Source system configurations will need then
 * systemParameters = {
 * {@literal @}SystemParameter(name = "The name of the first  system parameter",
 *                                      description="The description of the first system parameter." ,
 *                                      defaultValue = "the default value of the system parameter.",
 *                                      possibleParameter="the possible value of the system parameter.",
 *                               ),
 * },
 * examples = {
 * {@literal @}Example(syntax = "sample query with Source annotation that explain how extension use in Siddhi."
 *                              description =" The description of the given example's query."
 *                      ),
 * }
 * )
 * </code></pre>
 */

@Extension(
        name = "hl7",
        namespace = "source",
        description = "The hl7 consumes the hl7 message using MLLP protocol ",
        parameters = {
                @Parameter(name = "port" ,
                        description = "This is the unique logical address used to " +
                                "establish the connection for the process." ,
                        type = { DataType.INT }) ,
                @Parameter(name = "tls.enabled" ,
                        description = "Transport layer security" ,
                        optional = true, defaultValue = "false" ,
                        type = { DataType.BOOL }) ,
                @Parameter(name = "charset" ,
                        description = "Character encoding method" ,
                        optional = true, defaultValue = "UTF-8" ,
                        type = { DataType.STRING }) ,
                @Parameter(name = "hl7.encoding",
                        description = "Encoding method of received hl7" ,
                        type = { DataType.STRING }) ,
                @Parameter(name = "hl7.ack.encoding",
                        description = "Encoding method of received hl7" ,
                        optional = true, defaultValue = "ER7",
                        type = { DataType.STRING }) ,
                @Parameter(name = "hl7.server.timeout" ,
                        description = "how long the hl7 should wait for the thread to terminate." ,
                        optional = true, defaultValue = "6000" ,
                        type = { DataType.INT })


        },
        examples = {
                @Example(
                        syntax = "@App:name ( 'TestExecutionPlanSource' ) \n" +
                                "@source ( type = 'hl7',\n" +
                                "port = 1080,\n" +
                                "hl7.encoding = 'ER7',\n" +
                                "@map (type = 'text'))\n" +
                                "define stream hl7stream (payload string);\n"
                        ,
                        description = "This receives the HL7 messages using the MLLP protocol" +
                                " and send the acknowledgement message to the client.\n "
                )
        }
)

// for more information refer https://wso2.github.io/siddhi/documentation/siddhi-4.0/#sources
public class Hl7Source extends Source {
    private static final Logger log = Logger.getLogger(Hl7Source.class);
    private SourceEventListener sourceEventListener;
    private int port;
    private boolean tlsEnabled;
    private String hl7Encoding;
    private String hl7AckEncoding;
    private String charset;
    private int timeout;
    private HL7Service hl7Service;

    /**
     * The initialization method for {@link Source}, will be called before other methods. It used to validate
     * all configurations and to get initial values.
     * @param sourceEventListener After receiving events, the source should trigger onEvent() of this listener.
     *                            Listener will then pass on the events to the appropriate mappers for processing .
     * @param optionHolder        Option holder containing static configuration related to the {@link Source}
     * @param configReader        ConfigReader is used to read the {@link Source} related system configuration.
     * @param siddhiAppContext    the context of the {@link org.wso2.siddhi.query.api.SiddhiApp} used to get Siddhi
     *                            related utility functions.
     */
    @Override
    public void init(SourceEventListener sourceEventListener, OptionHolder optionHolder,
                     String[] requestedTransportPropertyNames, ConfigReader configReader,
                     SiddhiAppContext siddhiAppContext) {
        this.sourceEventListener = sourceEventListener;
        this.port = Integer.parseInt(optionHolder.validateAndGetStaticValue(Hl7Constants.HL7_PORT_NO));
        this.hl7Encoding = optionHolder.validateAndGetStaticValue(Hl7Constants.HL7_ENCODING);
        this.hl7AckEncoding = optionHolder.validateAndGetStaticValue(Hl7Constants.ACK_HL7_ENCODING, Hl7Constants.DEFAULT_ACK_HL7_ENCODING);
        this.charset = optionHolder.validateAndGetStaticValue(Hl7Constants.CHARSET_NAME, Hl7Constants.DEFAULT_HL7_CHARSET);
        this.tlsEnabled = Boolean.parseBoolean(optionHolder.validateAndGetStaticValue(Hl7Constants.TLS_ENABLE, Hl7Constants.DEFAULT_TLS_ENABLE));
        this.timeout = Integer.parseInt(optionHolder.validateAndGetStaticValue(Hl7Constants.HL7_SERVER_SHUTDOWN_TIMEOUT, Hl7Constants.DEFAULT_HL7_SERVER_SHUTDOWN_TIMEOUT));
    }

    /**
     * Returns the list of classes which this source can output.
     *
     * @return Array of classes that will be output by the source.
     * Null or empty array if it can produce any type of class.
     */
    @Override
    public Class[] getOutputEventClasses() {
        return new Class[]{String.class};
    }

    /**
     * Returns the HL7 encoding format
     */

  /*  public String getHl7Encoding() throws Exception {
        if(hl7Encoding.toUpperCase(Locale.ENGLISH).equals("ER7")){
            return "ER7";
        }
        else if(hl7Encoding.toUpperCase(Locale.ENGLISH).equals("XML")){
            return "XML";
        }
        else{
            throw new Exception("Invalid HL7 Encoding format");
        }
    } */

    /**
     * Initially Called to connect to the end point for start retrieving the messages asynchronously .
     *
     * @param connectionCallback Callback to pass the ConnectionUnavailableException in case of connection failure after
     *                           initial successful connection. (can be used when events are receiving asynchronously)
     * @throws ConnectionUnavailableException if it cannot connect to the source backend immediately.
     */


    @Override
    public void connect(ConnectionCallback connectionCallback) throws ConnectionUnavailableException {
        HapiContext hapiContext = new DefaultHapiContext();
        MinLowerLayerProtocol mllp = new MinLowerLayerProtocol();
        mllp.setCharset(charset);
        hapiContext.setLowerLayerProtocol(mllp);

        hl7Service = hapiContext.newServer(port, tlsEnabled);
        if((hl7Encoding.toUpperCase(Locale.ENGLISH).equals("ER7") || hl7Encoding.toUpperCase(Locale.ENGLISH).equals("XML"))
        && (hl7AckEncoding.toUpperCase(Locale.ENGLISH).equals("ER7") || hl7AckEncoding.toUpperCase(Locale.ENGLISH).equals("XML"))){
            try {
                hl7Service.setShutdownTimeout(timeout);
                hl7Service.startAndWait();
                System.out.println("server working");
                hl7Service.registerApplication(new RegistrationEventRouting(), new Hl7ReceivingApp(sourceEventListener, hl7Encoding, hl7AckEncoding));
            } catch (InterruptedException e) {
                log.error("Error occurred while creating the server: " + e);
            }
        } else {
            throw new SiddhiAppCreationException("Check the hl7.encoding or hl7.ack.encoding type in " + this.sourceEventListener + ". " +
                    "There is no hl7 encoding type named as " + "" + " in hl7");

        }





    }



    /**
     * This method can be called when it is needed to disconnect from the end point.
     */
    @Override
    public void disconnect() {
        hl7Service.stop();
    }

    /**
     * Called at the end to clean all the resources consumed by the {@link Source}
     */
    @Override
    public void destroy() {

    }

    /**
     * Called to pause event consumption
     */
    @Override
    public void pause() {

    }

    /**
     * Called to resume event consumption
     */
    @Override
    public void resume() {

    }

    /**
     * Used to collect the serializable state of the processing element, that need to be
     * persisted for the reconstructing the element to the same state on a different point of time
     *
     * @return stateful objects of the processing element as a map
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
     * This map will have the  same keys that is created upon calling currentState() method.
     */
     @Override
     public void restoreState(Map<String, Object> map) {

     }




}

