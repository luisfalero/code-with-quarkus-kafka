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
            //.to("kafka:my-topic?brokers=my-cluster-kafka-tls-bootstrap-amq-streams.apps.cluster-xl8x9.xl8x9.sandbox2914.opentlc.com:443" +      
            //    "&sslTruststoreLocation=/home/lfalero/Documents/KAFKA/truststore.jks" +
            //    "&sslTruststorePassword=redhat01" +
            //    "&securityProtocol=SSL")
            .log("[TOPIC] Message correctly sent to the topic: ${body}");
        
        
        // kafka consumer
        from("kafka:{{kafka.topic.name}}")
        //from("kafka:my-topic?brokers=my-cluster-kafka-tls-bootstrap-amq-streams.apps.cluster-xl8x9.xl8x9.sandbox2914.opentlc.com:443" +      
        //        "&sslTruststoreLocation=/home/lfalero/Documents/KAFKA/truststore.jks" +
        //        "&sslTruststorePassword=redhat01" +
        //        "&securityProtocol=SSL")
            .routeId("SedaKafka")
            .log("[TOPIC] Received: ${body}")
            .to("seda:kafka-messages");
    }    
}
