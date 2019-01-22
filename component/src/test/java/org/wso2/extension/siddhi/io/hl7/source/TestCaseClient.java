
package org.wso2.extension.siddhi.io.hl7.source;

import org.testng.AssertJUnit;
import org.wso2.siddhi.core.SiddhiAppRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.stream.output.StreamCallback;

public class TestCaseClient {
    public void clientSend2Message(){
        String siddhiApp2 = "@sink(type='hl7', " +
                "host.name = 'localhost', " +
                "port = '5041', " +
                "hl7.encoding = 'er7', " +
                "@map(type = 'text')) " +
                "define stream hl7stream(payload string);";
        SiddhiManager siddhiManager = new SiddhiManager();
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp2);
        InputHandler stream = siddhiAppRuntime.getInputHandler("hl7stream");
        siddhiAppRuntime.start();
        siddhiAppRuntime.addCallback("hl7stream", new StreamCallback() {


            @Override
            public void receive(Event[] events) {
                for (Event event : events) {
                    // log.info(event.toString().replaceAll("\\r","\n"));
                    //eventArrived = true;
                    //count++;
                }
            }
        });

        try{
            String payLoadER7 = "MSH|^~\\&|NES|NINTENDO|TESTSYSTEM|TESTFACILITY|20010101000000||ADT^A04|Q123456789T123456789X123456|P|2.3\r" +
                    "EVN|A04|20010101000000|||^KOOPA^BOWSER^^^^^^^CURRENT\r" +
                    "PID|1||123456789|0123456789^AA^^JP|BROS^MARIO^^^^||19850101000000|M|||123 FAKE STREET^MARIO \\T\\ LUIGI BROS PLACE^TOADSTOOL KINGDOM^NES^A1B2C3^JP^HOME^^1234|1234|(555)555-0123^HOME^JP:1234567|||S|MSH|12345678|||||||0|||||N\r" +
                    "NK1|1|PEACH^PRINCESS^^^^|SO|ANOTHER CASTLE^^TOADSTOOL KINGDOM^NES^^JP|(123)555-1234|(123)555-2345|NOK|||||||||||||\r" +
                    "NK2|2|TOADSTOOL^PRINCESS^^^^|SO|YET ANOTHER CASTLE^^TOADSTOOL KINGDOM^NES^^JP|(123)555-3456|(123)555-4567|EMC|||||||||||||\r" +
                    "PV1|1|O|ABCD^EFGH^|||^^|123456^DINO^YOSHI^^^^^^MSRM^CURRENT^^^NEIGHBOURHOOD DR NBR^|^DOG^DUCKHUNT^^^^^^^CURRENT||CRD|||||||123456^DINO^YOSHI^^^^^^MSRM^CURRENT^^^NEIGHBOURHOOD DR NBR^|AO|0123456789|1|||||||||||||||||||MSH||A|||20010101000000\r" +
                    "IN1|1|PAR^PARENT||||LUIGI\r";

            String payLoadER72 = "MSH|^~\\&|NES|NINTENDO|TESTSYSTEM|TESTFACILITY|20010101000000||ADT^A01|Q123456789T123456789X123456|P|2.3\r" +
                    "EVN|A01|20010101000000\r";


            stream.send(new Object[] {payLoadER7});
            stream.send(new Object[] {payLoadER72});
            Thread.sleep(10000);


        } catch (InterruptedException e){
            AssertJUnit.fail("interrupted");

        }

    }

    public void clientSendADTandORMMessage(){
        String siddhiApp2 = "@sink(type='hl7', " +
                "host.name = 'localhost', " +
                "port = '5041', " +
                "hl7.encoding = 'er7', " +
                "@map(type = 'text')) " +
                "define stream hl7stream(payload string);";
        SiddhiManager siddhiManager = new SiddhiManager();
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp2);
        InputHandler stream = siddhiAppRuntime.getInputHandler("hl7stream");
        siddhiAppRuntime.start();
        siddhiAppRuntime.addCallback("hl7stream", new StreamCallback() {


            @Override
            public void receive(Event[] events) {
                for (Event event : events) {
                    // log.info(event.toString().replaceAll("\\r","\n"));
                    //eventArrived = true;
                    //count++;
                }
            }
        });

        try{
            String payLoadER7 = "MSH|^~\\&|NES|NINTENDO|TESTSYSTEM|TESTFACILITY|20010101000000||ADT^A04|Q123456789T123456789X123456|P|2.3\r" +
                    "EVN|A04|20010101000000|||^KOOPA^BOWSER^^^^^^^CURRENT\r" +
                    "PID|1||123456789|0123456789^AA^^JP|BROS^MARIO^^^^||19850101000000|M|||123 FAKE STREET^MARIO \\T\\ LUIGI BROS PLACE^TOADSTOOL KINGDOM^NES^A1B2C3^JP^HOME^^1234|1234|(555)555-0123^HOME^JP:1234567|||S|MSH|12345678|||||||0|||||N\r" +
                    "NK1|1|PEACH^PRINCESS^^^^|SO|ANOTHER CASTLE^^TOADSTOOL KINGDOM^NES^^JP|(123)555-1234|(123)555-2345|NOK|||||||||||||\r";

            String payLoadER72 = "MSH|^~\\&|||||20190122111442.228+0530||ORM^O01|6101|T|2.3\r";


            stream.send(new Object[] {payLoadER7});
            stream.send(new Object[] {payLoadER72});
            Thread.sleep(10000);


        } catch (InterruptedException e){
            AssertJUnit.fail("interrupted");

        }

    }

    public void clientSendADTMessage(){
        String siddhiApp2 = "@sink(type='hl7', " +
                "host.name = 'localhost', " +
                "port = '5045', " +
                "hl7.encoding = 'er7', " +
                "@map(type = 'text')) " +
                "define stream hl7stream(payload string);";
        SiddhiManager siddhiManager = new SiddhiManager();
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp2);
        InputHandler stream = siddhiAppRuntime.getInputHandler("hl7stream");
        siddhiAppRuntime.start();
        siddhiAppRuntime.addCallback("hl7stream", new StreamCallback() {


            @Override
            public void receive(Event[] events) {
                for (Event event : events) {
                    // log.info(event.toString().replaceAll("\\r","\n"));
                    //eventArrived = true;
                    //count++;
                }
            }
        });

        try{

            String payLoadER7 = "MSH|^~\\&|NES|NINTENDO|TESTSYSTEM|TESTFACILITY|20010101000000||ADT^A01|Q123456789T123456789X123456|P|2.3\r" +
                    "EVN|A01|20010101000000\r";


            stream.send(new Object[] {payLoadER7});
            //stream.send(new Object[] {payLoadER72});
            Thread.sleep(10000);


        } catch (InterruptedException e){
            AssertJUnit.fail("interrupted");

        }

    }



    public void clientSendORMMessage(){
        String siddhiApp2 = "@sink(type='hl7', " +
                "host.name = 'localhost', " +
                "port = '5045', " +
                "hl7.encoding = 'er7', " +
                "@map(type = 'text')) " +
                "define stream hl7stream(payload string);";
        SiddhiManager siddhiManager = new SiddhiManager();
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp2);
        InputHandler stream = siddhiAppRuntime.getInputHandler("hl7stream");
        siddhiAppRuntime.start();
        siddhiAppRuntime.addCallback("hl7stream", new StreamCallback() {


            @Override
            public void receive(Event[] events) {
                for (Event event : events) {
                    // log.info(event.toString().replaceAll("\\r","\n"));
                    //eventArrived = true;
                    //count++;
                }
            }
        });

        try{

            String payLoadER7 = "MSH|^~\\&|||||20190122111442.228+0530||ORM^O01|6101|T|2.3\r";


            stream.send(new Object[] {payLoadER7});
            //stream.send(new Object[] {payLoadER72});
            Thread.sleep(10000);


        } catch (InterruptedException e){
            AssertJUnit.fail("interrupted");

        }

    }
}


