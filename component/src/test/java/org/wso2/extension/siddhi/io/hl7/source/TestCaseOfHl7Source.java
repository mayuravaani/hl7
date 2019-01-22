package org.wso2.extension.siddhi.io.hl7.source;

import ca.uhn.hl7v2.app.HL7Service;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wso2.siddhi.core.SiddhiAppRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.stream.output.StreamCallback;
import org.wso2.siddhi.core.util.EventPrinter;

import java.util.concurrent.atomic.AtomicInteger;
import org.testng.AssertJUnit;

public class TestCaseOfHl7Source {
    private static final Logger log = Logger.getLogger(TestCaseOfHl7Source.class);
    private AtomicInteger count = new AtomicInteger();
    private org.wso2.extension.siddhi.io.hl7.source.TestCaseClient TestCaseClient;


    private AtomicInteger eventCounter = new AtomicInteger(0);
    @BeforeMethod
    private void setUP(){
        eventCounter.set(0);
    }
    @BeforeClass
    private void initBeforeMethod() {
        eventCounter.set(0);
    }

    @Test
    public void hl7ConsumerTestER7(){
        SiddhiManager siddhiManager = new SiddhiManager();
        String siddhiApp = "@source ( type = 'hl7',\n" +
                "port = '5041',\n" +
                "hl7.encoding = 'ER7',\n" +
                "@map (type = 'text'))\n" +
                "define stream hl7stream (payload string);\n";

        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp);
        siddhiAppRuntime.start();
        siddhiAppRuntime.addCallback("hl7stream", new StreamCallback() {

            @Override
            public void receive(Event[] events) {
                for (Event event : events) {
                    log.info(event);
                }
            }
        });
        this.TestCaseClient = new TestCaseClient();
        TestCaseClient.clientSend2Message();
        //  AssertJUnit.assertEquals("Number of events", 1, count.get());
        try {
            Thread.sleep(20000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void hl7ConsumerTestXML(){
        SiddhiManager siddhiManager = new SiddhiManager();
        String siddhiApp = "@source ( type = 'hl7',\n" +
                "port = '5045',\n" +
                "hl7.encoding = 'xml',\n" +
                "@map (type = 'xml', @attributes(MSH10 = \"MSH/MSH.10\", MSH3HD1 = \"MSH/MSH.3/HD.1\")))\n" +
                "define stream hl7stream (MSH10 string, MSH3HD1 string);\n";

        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp);
        siddhiAppRuntime.start();
        siddhiAppRuntime.addCallback("hl7stream", new StreamCallback() {


            @Override
            public void receive(Event[] events) {
                for (Event event : events) {
                    log.info(event);
                }
            }
        });
        this.TestCaseClient = new TestCaseClient();
        TestCaseClient.clientSendADTMessage();


        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void hl7ConsumerTestAckEncoding(){
        SiddhiManager siddhiManager = new SiddhiManager();
        String siddhiApp = "@source ( type = 'hl7',\n" +
                "port = '5013',\n" +
                "hl7.encoding = 'xml',\n" +
                "hl7.ack.encoding = 'xml',\n" +
                "@map (type = 'xml', @attributes(MSH10 = \"MSH/MSH.10\", MSH3HD1 = \"MSH/MSH.3/HD.1\")))\n" +
                "define stream hl7stream (MSH10 string, MSH3HD1 string);\n";

        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp);
        siddhiAppRuntime.start();
        siddhiAppRuntime.addCallback("hl7stream", new StreamCallback() {


            @Override
            public void receive(Event[] events) {
                for (Event event : events) {
                    log.info(event);
                }
            }
        });
        this.TestCaseClient = new TestCaseClient();
        TestCaseClient.clientSend2Message();
        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void hl7ConsumerTestCharset(){
        SiddhiManager siddhiManager = new SiddhiManager();
        String siddhiApp = "@source ( type = 'hl7',\n" +
                "port = '5045',\n" +
                "hl7.encoding = 'xml',\n" +
                "charset = 'ISO-8859-2',\n" + //If you use utf-16 not supportable if client is running on single byte encoding.
                "@map (type = 'xml', @attributes(MSH10 = \"MSH/MSH.10\", MSH3HD1 = \"MSH/MSH.3/HD.1\")))\n" +
                "define stream hl7stream (MSH10 string, MSH3HD1 string);\n";

        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp);
        siddhiAppRuntime.start();
        siddhiAppRuntime.addCallback("hl7stream", new StreamCallback() {


            @Override
            public void receive(Event[] events) {
                for (Event event : events) {
                    log.info(event);
                }
            }
        });
        this.TestCaseClient = new TestCaseClient();
        TestCaseClient.clientSend2Message();
        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void hl7ConsumerTestServerTimeout(){
        SiddhiManager siddhiManager = new SiddhiManager();
        String siddhiApp = "@source ( type = 'hl7',\n" +
                "port = '5045',\n" +
                "hl7.encoding = 'xml',\n" +
                "charset = 'ISO-8859-2',\n" + //If you use utf-16 not supportable if client is running on single byte encoding.
                "@map (type = 'xml', @attributes(MSH10 = \"MSH/MSH.10\", MSH3HD1 = \"MSH/MSH.3/HD.1\")))\n" +
                "define stream hl7stream (MSH10 string, MSH3HD1 string);\n";

        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp);
        siddhiAppRuntime.start();
        siddhiAppRuntime.addCallback("hl7stream", new StreamCallback() {


            @Override
            public void receive(Event[] events) {
                for (Event event : events) {
                    log.info(event);
                }
            }
        });
        this.TestCaseClient = new TestCaseClient();
        TestCaseClient.clientSend2Message();
        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}


