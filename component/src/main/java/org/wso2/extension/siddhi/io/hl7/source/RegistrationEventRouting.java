package org.wso2.extension.siddhi.io.hl7.source;


import ca.uhn.hl7v2.protocol.ApplicationRouter;

public class RegistrationEventRouting implements ApplicationRouter.AppRoutingData{

    @Override
    public String getMessageType() {
        return "*";
    }

    @Override
    public String getTriggerEvent() {
        return "*";
    }

    @Override
    public String getProcessingId() {
        return "*";
    }

    @Override
    public String getVersion() {
        return "*";
    }
}
