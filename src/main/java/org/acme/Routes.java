package org.acme;

import org.apache.camel.builder.RouteBuilder;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Routes extends RouteBuilder {

    @Override
    public void configure() throws Exception {                

        // kafka producer
        from("timer:foo?period={{timer.period}}&delay={{timer.delay}}")
            .routeId("RouteKafka")
            .setBody()
            .simple("Message RedHat")
            .to("kafka:{{kafka.topic.name}}")
            .log("[TOPIC] Message correctly sent to the topic: ${body}");
        
        
        // kafka consumer
        from("kafka:{{kafka.topic.name}}")
            .routeId("SedaKafka")
            .log("[TOPIC] Received: ${body}")
            .to("seda:kafka-messages");        
    }
    
}
