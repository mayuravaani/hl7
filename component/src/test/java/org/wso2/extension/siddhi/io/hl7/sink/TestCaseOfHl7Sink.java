package org.wso2.extension.siddhi.io.hl7.sink;

import org.apache.log4j.Logger;
import org.testng.AssertJUnit;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wso2.siddhi.core.SiddhiAppRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.stream.output.StreamCallback;
import org.wso2.siddhi.core.util.EventPrinter;

import java.util.concurrent.atomic.AtomicInteger;

public class TestCaseOfHl7Sink {
    private volatile int count;
    private volatile boolean eventArrived;
    private static final Logger log = (Logger) Logger.getLogger(TestCaseOfHl7Sink.class);
    private AtomicInteger eventCounter = new AtomicInteger(0);
    /*private void setUP(){
      eventCounter.set(0);
  }*/
  @BeforeMethod
  public void initBeforeMethod() {
     count = 0;
      eventArrived = false;
  }
  /*
   @BeforeClass
   private void initBeforeMethod() {
       eventCounter.set(0);
   }*/

   @Test
    public void hl7PublishTestER7() throws InterruptedException {
       SiddhiManager siddhiManager = new SiddhiManager();
       String siddhiApp = "@sink(type='hl7', " +
               "host.name = 'localhost', " +
               "port = '5010', " +
               "hl7.encoding = 'er7', " +
               "@map(type = 'text')) " +
               "define stream hl7stream(payload string);";
       SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp);
       //ResultContainer resultContainer = new ResultContainer(2);
       InputHandler stream = siddhiAppRuntime.getInputHandler("hl7stream");
       siddhiAppRuntime.addCallback("hl7stream", new StreamCallback() {


           @Override
           public void receive(Event[] events) {
               for (Event event : events) {
                  // log.info(event.toString().replaceAll("\\r","\n"));
                   eventArrived = true;
                   count++;
               }
           }
       });
       siddhiAppRuntime.start();
       try{
            String payLoadER7 ="MSH|^~\\&|NES|NINTENDO|TESTSYSTEM|TESTFACILITY|20010101000000||ADT^A01|M123456789T123456789X123456|P|2.3\r" +
                   "EVN|A01|20010101000000|||^KOOPA^BOWSER^^^^^^^CURRENT\r" +
                   "PID|1||123456789|0123456789^AA^^JP|BROS^MARIO^^^^||19850101000000|M|||123 FAKE STREET^MARIO \\T\\ LUIGI BROS PLACE^TOADSTOOL KINGDOM^NES^A1B2C3^JP^HOME^^1234|1234|(555)555-0123^HOME^JP:1234567|||S|MSH|12345678|||||||0|||||N\r" +
                   "NK1|1|PEACH^PRINCESS^^^^|SO|ANOTHER CASTLE^^TOADSTOOL KINGDOM^NES^^JP|(123)555-1234|(123)555-2345|NOK|||||||||||||\r" +
                   "NK2|2|TOADSTOOL^PRINCESS^^^^|SO|YET ANOTHER CASTLE^^TOADSTOOL KINGDOM^NES^^JP|(123)555-3456|(123)555-4567|EMC|||||||||||||\r" +
                   "PV1|1|O|ABCD^EFGH^|||^^|123456^DINO^YOSHI^^^^^^MSRM^CURRENT^^^NEIGHBOURHOOD DR NBR^|^DOG^DUCKHUNT^^^^^^^CURRENT||CRD|||||||123456^DINO^YOSHI^^^^^^MSRM^CURRENT^^^NEIGHBOURHOOD DR NBR^|AO|0123456789|1|||||||||||||||||||MSH||A|||20010101000000\r" +
                   "IN1|1|PAR^PARENT||||LUIGI\r" +
                   "IN2|2|FRI^FRIEND||||PRINCESS\r";
           String payLoadER72 ="MSH|^~\\&|NES|NINTENDO|TESTSYSTEM|TESTFACILITY|20010101000000||ADT^A04|Q123456789T123456789X123456|P|2.3\r" +
                   "EVN|A04|20010101000000|||^KOOPA^BOWSER^^^^^^^CURRENT\r" +
                   "PID|1||123456789|0123456789^AA^^JP|BROS^MARIO^^^^||19850101000000|M|||123 FAKE STREET^MARIO \\T\\ LUIGI BROS PLACE^TOADSTOOL KINGDOM^NES^A1B2C3^JP^HOME^^1234|1234|(555)555-0123^HOME^JP:1234567|||S|MSH|12345678|||||||0|||||N\r" +
                   "NK1|1|PEACH^PRINCESS^^^^|SO|ANOTHER CASTLE^^TOADSTOOL KINGDOM^NES^^JP|(123)555-1234|(123)555-2345|NOK|||||||||||||\r" +
                   "NK2|2|TOADSTOOL^PRINCESS^^^^|SO|YET ANOTHER CASTLE^^TOADSTOOL KINGDOM^NES^^JP|(123)555-3456|(123)555-4567|EMC|||||||||||||\r" +
                   "PV1|1|O|ABCD^EFGH^|||^^|123456^DINO^YOSHI^^^^^^MSRM^CURRENT^^^NEIGHBOURHOOD DR NBR^|^DOG^DUCKHUNT^^^^^^^CURRENT||CRD|||||||123456^DINO^YOSHI^^^^^^MSRM^CURRENT^^^NEIGHBOURHOOD DR NBR^|AO|0123456789|1|||||||||||||||||||MSH||A|||20010101000000\r" +
                   "IN1|1|PAR^PARENT||||LUIGI\r" +
                   "IN2|2|FRI^FRIEND||||PRINCESS\r";

           stream.send(new Object[] {payLoadER7});
           stream.send(new Object[] {payLoadER72});
           /*stream.send(new Object[] {payLoadER73});
           Thread.sleep(10000);*/


       } catch (InterruptedException e){
           AssertJUnit.fail("interrupted");

       }
     AssertJUnit.assertEquals(2, count);
       AssertJUnit.assertTrue(eventArrived);


       siddhiAppRuntime.shutdown();


   }


    @Test
    public void hl7PublishTestXML() throws InterruptedException {
        SiddhiManager siddhiManager = new SiddhiManager();
        String siddhiApp = "@sink(type='hl7', " +
                "host.name = 'localhost', " +
                "port = '5028', " +
                "hl7.encoding = 'xml', " +
                "hl7.ack.encoding = 'xml', " +
                "@map(type = 'xml', enclosing.element=\"<ADT_A01  xmlns='urn:hl7-org:v2xml'>\", " +
                "@payload('<MSH><MSH.1>{{MSH1}}</MSH.1><MSH.2>{{MSH2}}</MSH.2><MSH.3><HD.1>{{MSH3HD1}}</HD.1></MSH.3><MSH.4><HD.1>{{MSH4HD1}}</HD.1></MSH.4><MSH.5><HD.1>{{MSH5HD1}}</HD.1></MSH.5><MSH.6><HD.1>{{MSH6HD1}}</HD.1></MSH.6><MSH.7>{{MSH7}}</MSH.7><MSH.8>{{MSH8}}</MSH.8><MSH.9><CM_MSG.1>{{CM_MSG1}}</CM_MSG.1><CM_MSG.2>{{CM_MSG2}}</CM_MSG.2></MSH.9><MSH.10>{{MSH10}}</MSH.10><MSH.11>{{MSH11}}</MSH.11><MSH.12>{{MSH12}}</MSH.12></MSH>'))) " +
                "define stream hl7stream(MSH1 string,MSH2 string,MSH3HD1 string,MSH4HD1 string,MSH5HD1 string,MSH6HD1 string,MSH7 string,MSH8 string,CM_MSG1 string,CM_MSG2 string,MSH10 string,MSH11 string,MSH12 string);  ";

        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp);
        InputHandler stream = siddhiAppRuntime.getInputHandler("hl7stream");
        siddhiAppRuntime.start();
        try{
            stream.send(new Object[] {"|", "^~\\&amp;", "sendingSystem", "senderFacility", "receivingSystem", "receivingFacility", "20080925161613", " ", "ADT", "A01", "123456789", "P", "2.3"});
            siddhiAppRuntime.addCallback("hl7stream", new StreamCallback() {


                @Override
                public void receive(Event[] events) {
                    for (Event event : events) {
                        log.info(event);
                    }
                }
            });
        } catch (InterruptedException e){
            AssertJUnit.fail("interrupted");

        }
    }

    @Test
    public void hl7PublishTestAckEncoding() throws InterruptedException {
        SiddhiManager siddhiManager = new SiddhiManager();
        String siddhiApp = "@sink(type='hl7', " +
                "host.name = 'localhost', " +
                "port = '5028', " +
                "hl7.encoding = 'xml', " +
                "hl7.ack.encoding = 'xml', " +
                "@map(type = 'xml', enclosing.element=\"<ADT_A01  xmlns='urn:hl7-org:v2xml'>\", " +
                "@payload('<MSH><MSH.1>{{MSH1}}</MSH.1><MSH.2>{{MSH2}}</MSH.2><MSH.3><HD.1>{{MSH3HD1}}</HD.1></MSH.3><MSH.4><HD.1>{{MSH4HD1}}</HD.1></MSH.4><MSH.5><HD.1>{{MSH5HD1}}</HD.1></MSH.5><MSH.6><HD.1>{{MSH6HD1}}</HD.1></MSH.6><MSH.7>{{MSH7}}</MSH.7><MSH.8>{{MSH8}}</MSH.8><MSH.9><CM_MSG.1>{{CM_MSG1}}</CM_MSG.1><CM_MSG.2>{{CM_MSG2}}</CM_MSG.2></MSH.9><MSH.10>{{MSH10}}</MSH.10><MSH.11>{{MSH11}}</MSH.11><MSH.12>{{MSH12}}</MSH.12></MSH>'))) " +
                "define stream hl7stream(MSH1 string,MSH2 string,MSH3HD1 string,MSH4HD1 string,MSH5HD1 string,MSH6HD1 string,MSH7 string,MSH8 string,CM_MSG1 string,CM_MSG2 string,MSH10 string,MSH11 string,MSH12 string);  ";

        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp);
        InputHandler stream = siddhiAppRuntime.getInputHandler("hl7stream");
        siddhiAppRuntime.start();
        try{
            stream.send(new Object[] {"|", "^~\\&amp;", "sendingSystem", "senderFacility", "receivingSystem", "receivingFacility", "20080925161613", " ", "ADT", "A01", "123456789", "P", "2.3"});
            siddhiAppRuntime.addCallback("hl7stream", new StreamCallback() {


                @Override
                public void receive(Event[] events) {
                    for (Event event : events) {
                        log.info(event);
                    }
                }
            });
            // stream.send(new Object[] {payLoadXML});
        } catch (InterruptedException e){
            AssertJUnit.fail("interrupted");

        }


    }


}

