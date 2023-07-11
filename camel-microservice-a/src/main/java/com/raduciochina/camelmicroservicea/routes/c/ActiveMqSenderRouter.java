package com.raduciochina.camelmicroservicea.routes.c;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ActiveMqSenderRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        //timer  - 1st endpoint
        from("timer:active-mq-timer?period=10000")
                .transform().constant("My message for Active MQ")
                .log("${body}")
                .to("activemq:my-activemq-queue");
        //queue  - 2nd endpoint
    }
}
