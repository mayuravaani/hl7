package org.wso2.extension.siddhi.io.hl7.sink;


import org.apache.commons.logging.Log;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Class to retain results received by Mqtt client.
 */
public class ResultContainer {
    private static Log log = LogFactory.getLog(ResultContainer.class);
    private int eventCount;
    private List<String> results;
    private CountDownLatch latch;
    private int timeout = 90;

    public ResultContainer(int expectedEventCount) {
        eventCount = 0;
        results = new ArrayList<>(expectedEventCount);
        latch = new CountDownLatch(expectedEventCount);
    }

    public ResultContainer(int expectedEventCount, int timeoutInSeconds) {
        eventCount = 0;
        results = new ArrayList<>(expectedEventCount);
        latch = new CountDownLatch(expectedEventCount);
        timeout = timeoutInSeconds;
    }

    public void eventReceived(Message hl7Msg) throws HL7Exception {
        eventCount++;
        // String message = new String(hl7Msg.getPayload(), "UTF-8");
        PipeParser p = new PipeParser();
        String message = p.encode(hl7Msg);
        results.add(message);
        latch.countDown();
    }

    public Boolean assertMessageContent(String content) {
        try {
            if (latch.await(timeout, TimeUnit.SECONDS)) {
                for (String message : results) {
                    if (message.contains(content)) {
                        return true;
                    }
                }
                return false;
            } else {
                log.error("Expected number of results not received. Only received " + eventCount + " events.");
                return false;
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return false;
    }
}