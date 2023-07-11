package com.raduciochina.camelmicroservicea.routes.a;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class MyFirstTimerRouter extends RouteBuilder {
    @Autowired
    private GetCurrentTimeBean getCurrentTimeBean;
    @Autowired
    private SimpleLoggingProcessingComponent loggingComponent;
    @Override
    public void configure() throws Exception {
        // These steps form a route
        // 1. listen to a queue (first endpoint) -> we'll use a timer to simulate a queue
        // 2. transformation
        // 3. save to database (second endpoint) -> we'll use a log to simulate a database

        from("timer:first-timer") // timer endpoint
                .log("${body}") // log endpoint
                .transform().constant("My Constant Message") // transformation
                .log("${body}") // log endpoint
//                .transform().constant("Time now is " + java.time.LocalDateTime.now()) // transformation
                .bean(getCurrentTimeBean, "getCurrentTime") // transformation
                .log("${body}") // log endpoint
                .bean(loggingComponent) // transformation
                .process(new SimpleLoggingProcessor()) // transformation
                .to("log:first-timer"); // log endpoint
    }
}

@Component
class GetCurrentTimeBean {
    public String getCurrentTime() {
        return "Time now is " + java.time.LocalDateTime.now();
    }
}

@Component
class SimpleLoggingProcessingComponent{
    private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessingComponent.class);
    public void process(String message) {
        logger.info("SimpleLoggingProcessingComponent {}", message);
    }
}


class SimpleLoggingProcessor implements Processor {
    private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("SimpleLoggingProcessor {}", exchange.getMessage().getBody());
    }
}