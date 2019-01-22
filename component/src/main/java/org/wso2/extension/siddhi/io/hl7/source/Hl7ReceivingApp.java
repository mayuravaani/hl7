package org.wso2.extension.siddhi.io.hl7.source;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.DefaultXMLParser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.parser.XMLParser;
import ca.uhn.hl7v2.protocol.ReceivingApplication;
import ca.uhn.hl7v2.protocol.ReceivingApplicationException;
import org.apache.log4j.Logger;
import org.wso2.siddhi.core.stream.input.source.SourceEventListener;


import java.io.IOException;
import java.util.Locale;
import java.util.Map;

public class Hl7ReceivingApp implements ReceivingApplication {
  private static final org.apache.log4j.Logger log = Logger.getLogger(Hl7ReceivingApp.class);
     private SourceEventListener sourceEventListener;
     private String hl7EncodeType;
     private String hl7AckType;

     public Hl7ReceivingApp(SourceEventListener sourceEventListener, String hl7EncodeType, String hl7AckType){
         this.sourceEventListener = sourceEventListener;
         this.hl7EncodeType = hl7EncodeType;
         this.hl7AckType = hl7AckType;

     }
    @Override
    public Message processMessage(Message theMessage, Map<String, Object> theMetadata) throws ReceivingApplicationException, HL7Exception {

        PipeParser pipeParser = new PipeParser();
        XMLParser xmlParser = new DefaultXMLParser();
        // log.info(theMessage);
        if (log.isDebugEnabled()) {
            log.debug(theMessage);
        }
        if(hl7EncodeType.toUpperCase(Locale.ENGLISH).equals("ER7")){
            String ER7msg = pipeParser.encode(theMessage);
            String ER7msgFormated = ER7msg.replaceAll("\\r", "\n");
            sourceEventListener.onEvent("payload: "+ "'" + ER7msgFormated +"'",null);

        } else if(hl7EncodeType.toUpperCase(Locale.ENGLISH).equals("XML")){
            String XMLmsg = xmlParser.encode(theMessage);
            XMLmsg=  XMLmsg.replaceAll("xmlns=\"urn:hl7-org:","xmlns:dt=\"urn:hl7-org:");
            sourceEventListener.onEvent(XMLmsg,null);
        }

        try {
            Message ackMsg = theMessage.generateACK();
            if(hl7AckType.toUpperCase(Locale.ENGLISH).equals("ER7")){
                String ER7ackMsg = pipeParser.encode(ackMsg);
                ER7ackMsg = ER7ackMsg.replaceAll("\\r", "\n");
                log.info("Sent Acknowledgement: \n" + ER7ackMsg);
                if (log.isDebugEnabled()) {
                    log.debug(ER7ackMsg);
                }
            } else if(hl7AckType.toUpperCase(Locale.ENGLISH).equals("XML")){
                String XMLackMsg = xmlParser.encode(ackMsg);
                log.info("Sent Acknowledgement: \n" + XMLackMsg);
                if (log.isDebugEnabled()) {
                    log.debug(XMLackMsg);
                }
            }


            if (log.isDebugEnabled()) {
                log.debug(theMessage);
            }
            return ackMsg;
        } catch (IOException e) {
            throw new HL7Exception(e);
        }
    }

    @Override
    public boolean canProcess(Message theMessage) {
        return true;
    }
}

