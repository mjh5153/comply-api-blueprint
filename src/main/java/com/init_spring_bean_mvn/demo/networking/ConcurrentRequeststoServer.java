package com.init_spring_bean_mvn.demo.networking;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ConcurrentRequeststoServer {
    public static void main(String[] args) {
        Map<String, Integer> orderMap = Map.of("apples", 500, "oranges", 1000, "bananas", 75,
                "carrots", 2000,
                "cantaloupes", 100);

        String urlparams = "product=%s&amount=%d";
        String urlBase = "http://localhost:8080";

        List<URI> sites = new ArrayList<>();
        orderMap.forEach((k,v) -> sites.add(URI.create(
                urlBase + "?" + urlparams.formatted(k,v)
        )));

        HttpClient client = HttpClient.newHttpClient();
        sendGets(client, sites);
    }
    // Expected Concurrent Response - /Users/mjh5153/Library/Java/JavaVirtualMachines/openjdk-17/Contents/Home/bin/java -javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=60131 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath /Users/mjh5153/Downloads/demo/target/classes:/Users/mjh5153/.m2/repository/org/springframework/boot/spring-boot-starter/3.5.6/spring-boot-starter-3.5.6.jar:/Users/mjh5153/.m2/repository/org/springframework/boot/spring-boot/3.5.6/spring-boot-3.5.6.jar:/Users/mjh5153/.m2/repository/org/springframework/spring-context/6.2.11/spring-context-6.2.11.jar:/Users/mjh5153/.m2/repository/org/springframework/boot/spring-boot-autoconfigure/3.5.6/spring-boot-autoconfigure-3.5.6.jar:/Users/mjh5153/.m2/repository/org/springframework/boot/spring-boot-starter-logging/3.5.6/spring-boot-starter-logging-3.5.6.jar:/Users/mjh5153/.m2/repository/ch/qos/logback/logback-classic/1.5.18/logback-classic-1.5.18.jar:/Users/mjh5153/.m2/repository/ch/qos/logback/logback-core/1.5.18/logback-core-1.5.18.jar:/Users/mjh5153/.m2/repository/org/apache/logging/log4j/log4j-to-slf4j/2.24.3/log4j-to-slf4j-2.24.3.jar:/Users/mjh5153/.m2/repository/org/apache/logging/log4j/log4j-api/2.24.3/log4j-api-2.24.3.jar:/Users/mjh5153/.m2/repository/org/slf4j/jul-to-slf4j/2.0.17/jul-to-slf4j-2.0.17.jar:/Users/mjh5153/.m2/repository/jakarta/annotation/jakarta.annotation-api/2.1.1/jakarta.annotation-api-2.1.1.jar:/Users/mjh5153/.m2/repository/org/springframework/spring-core/6.2.11/spring-core-6.2.11.jar:/Users/mjh5153/.m2/repository/org/springframework/spring-jcl/6.2.11/spring-jcl-6.2.11.jar:/Users/mjh5153/.m2/repository/org/yaml/snakeyaml/2.4/snakeyaml-2.4.jar:/Users/mjh5153/.m2/repository/org/springframework/boot/spring-boot-starter-web/3.5.6/spring-boot-starter-web-3.5.6.jar:/Users/mjh5153/.m2/repository/org/springframework/boot/spring-boot-starter-json/3.5.6/spring-boot-starter-json-3.5.6.jar:/Users/mjh5153/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.19.2/jackson-databind-2.19.2.jar:/Users/mjh5153/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.19.2/jackson-annotations-2.19.2.jar:/Users/mjh5153/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.19.2/jackson-core-2.19.2.jar:/Users/mjh5153/.m2/repository/com/fasterxml/jackson/datatype/jackson-datatype-jdk8/2.19.2/jackson-datatype-jdk8-2.19.2.jar:/Users/mjh5153/.m2/repository/com/fasterxml/jackson/datatype/jackson-datatype-jsr310/2.19.2/jackson-datatype-jsr310-2.19.2.jar:/Users/mjh5153/.m2/repository/com/fasterxml/jackson/module/jackson-module-parameter-names/2.19.2/jackson-module-parameter-names-2.19.2.jar:/Users/mjh5153/.m2/repository/org/springframework/boot/spring-boot-starter-tomcat/3.5.6/spring-boot-starter-tomcat-3.5.6.jar:/Users/mjh5153/.m2/repository/org/apache/tomcat/embed/tomcat-embed-core/10.1.46/tomcat-embed-core-10.1.46.jar:/Users/mjh5153/.m2/repository/org/apache/tomcat/embed/tomcat-embed-websocket/10.1.46/tomcat-embed-websocket-10.1.46.jar:/Users/mjh5153/.m2/repository/org/springframework/spring-web/6.2.11/spring-web-6.2.11.jar:/Users/mjh5153/.m2/repository/org/springframework/spring-beans/6.2.11/spring-beans-6.2.11.jar:/Users/mjh5153/.m2/repository/io/micrometer/micrometer-observation/1.15.4/micrometer-observation-1.15.4.jar:/Users/mjh5153/.m2/repository/io/micrometer/micrometer-commons/1.15.4/micrometer-commons-1.15.4.jar:/Users/mjh5153/.m2/repository/org/springframework/spring-webmvc/6.2.11/spring-webmvc-6.2.11.jar:/Users/mjh5153/.m2/repository/org/springframework/spring-aop/6.2.11/spring-aop-6.2.11.jar:/Users/mjh5153/.m2/repository/org/springframework/spring-expression/6.2.11/spring-expression-6.2.11.jar:/Users/mjh5153/.m2/repository/org/springframework/boot/spring-boot-starter-data-jpa/3.5.6/spring-boot-starter-data-jpa-3.5.6.jar:/Users/mjh5153/.m2/repository/org/springframework/boot/spring-boot-starter-jdbc/3.5.6/spring-boot-starter-jdbc-3.5.6.jar:/Users/mjh5153/.m2/repository/com/zaxxer/HikariCP/6.3.3/HikariCP-6.3.3.jar:/Users/mjh5153/.m2/repository/org/springframework/spring-jdbc/6.2.11/spring-jdbc-6.2.11.jar:/Users/mjh5153/.m2/repository/org/hibernate/orm/hibernate-core/6.6.29.Final/hibernate-core-6.6.29.Final.jar:/Users/mjh5153/.m2/repository/jakarta/persistence/jakarta.persistence-api/3.1.0/jakarta.persistence-api-3.1.0.jar:/Users/mjh5153/.m2/repository/jakarta/transaction/jakarta.transaction-api/2.0.1/jakarta.transaction-api-2.0.1.jar:/Users/mjh5153/.m2/repository/org/jboss/logging/jboss-logging/3.6.1.Final/jboss-logging-3.6.1.Final.jar:/Users/mjh5153/.m2/repository/org/hibernate/common/hibernate-commons-annotations/7.0.3.Final/hibernate-commons-annotations-7.0.3.Final.jar:/Users/mjh5153/.m2/repository/io/smallrye/jandex/3.2.0/jandex-3.2.0.jar:/Users/mjh5153/.m2/repository/com/fasterxml/classmate/1.7.0/classmate-1.7.0.jar:/Users/mjh5153/.m2/repository/net/bytebuddy/byte-buddy/1.17.7/byte-buddy-1.17.7.jar:/Users/mjh5153/.m2/repository/org/glassfish/jaxb/jaxb-runtime/4.0.5/jaxb-runtime-4.0.5.jar:/Users/mjh5153/.m2/repository/org/glassfish/jaxb/jaxb-core/4.0.5/jaxb-core-4.0.5.jar:/Users/mjh5153/.m2/repository/org/eclipse/angus/angus-activation/2.0.2/angus-activation-2.0.2.jar:/Users/mjh5153/.m2/repository/org/glassfish/jaxb/txw2/4.0.5/txw2-4.0.5.jar:/Users/mjh5153/.m2/repository/com/sun/istack/istack-commons-runtime/4.1.2/istack-commons-runtime-4.1.2.jar:/Users/mjh5153/.m2/repository/jakarta/inject/jakarta.inject-api/2.0.1/jakarta.inject-api-2.0.1.jar:/Users/mjh5153/.m2/repository/org/antlr/antlr4-runtime/4.13.0/antlr4-runtime-4.13.0.jar:/Users/mjh5153/.m2/repository/org/springframework/data/spring-data-jpa/3.5.4/spring-data-jpa-3.5.4.jar:/Users/mjh5153/.m2/repository/org/springframework/data/spring-data-commons/3.5.4/spring-data-commons-3.5.4.jar:/Users/mjh5153/.m2/repository/org/springframework/spring-orm/6.2.11/spring-orm-6.2.11.jar:/Users/mjh5153/.m2/repository/org/springframework/spring-tx/6.2.11/spring-tx-6.2.11.jar:/Users/mjh5153/.m2/repository/org/slf4j/slf4j-api/2.0.17/slf4j-api-2.0.17.jar:/Users/mjh5153/.m2/repository/org/springframework/spring-aspects/6.2.11/spring-aspects-6.2.11.jar:/Users/mjh5153/.m2/repository/org/aspectj/aspectjweaver/1.9.24/aspectjweaver-1.9.24.jar:/Users/mjh5153/.m2/repository/com/h2database/h2/2.3.232/h2-2.3.232.jar:/Users/mjh5153/.m2/repository/com/mysql/mysql-connector-j/9.4.0/mysql-connector-j-9.4.0.jar:/Users/mjh5153/.m2/repository/jakarta/xml/bind/jakarta.xml.bind-api/4.0.2/jakarta.xml.bind-api-4.0.2.jar:/Users/mjh5153/.m2/repository/jakarta/activation/jakarta.activation-api/2.1.4/jakarta.activation-api-2.1.4.jar:/Users/mjh5153/.m2/repository/org/springframework/boot/spring-boot-starter-thymeleaf/3.5.6/spring-boot-starter-thymeleaf-3.5.6.jar:/Users/mjh5153/.m2/repository/org/thymeleaf/thymeleaf-spring6/3.1.3.RELEASE/thymeleaf-spring6-3.1.3.RELEASE.jar:/Users/mjh5153/.m2/repository/org/thymeleaf/thymeleaf/3.1.3.RELEASE/thymeleaf-3.1.3.RELEASE.jar:/Users/mjh5153/.m2/repository/org/attoparser/attoparser/2.0.7.RELEASE/attoparser-2.0.7.RELEASE.jar:/Users/mjh5153/.m2/repository/org/unbescape/unbescape/1.1.6.RELEASE/unbescape-1.1.6.RELEASE.jar:/Users/mjh5153/.m2/repository/org/springframework/boot/spring-boot-starter-validation/3.5.6/spring-boot-starter-validation-3.5.6.jar:/Users/mjh5153/.m2/repository/org/apache/tomcat/embed/tomcat-embed-el/10.1.46/tomcat-embed-el-10.1.46.jar:/Users/mjh5153/.m2/repository/org/hibernate/validator/hibernate-validator/8.0.3.Final/hibernate-validator-8.0.3.Final.jar:/Users/mjh5153/.m2/repository/jakarta/validation/jakarta.validation-api/3.0.2/jakarta.validation-api-3.0.2.jar:/Users/mjh5153/.m2/repository/org/projectlombok/lombok/1.18.42/lombok-1.18.42.jar:/Users/mjh5153/Downloads/mysql-connector-j-8.3.0/mysql-connector-j-8.3.0.jar com.init_spring_bean_mvn.demo.networking.ConcurrentRequeststoServer
    //{
    //    "order":
    //    {
    //        "orderId": "0000000001",
    //        "product": "cantaloupes",
    //        "amount":100,
    //        "orderReceivedData": "2026-01-09T11:36:28.886669",
    //        "orderDeliveryData": "2026-01-12"
    //    }
    //}
    //
    //{
    //    "order":
    //    {
    //        "orderId": "0000000004",
    //        "product": "carrots",
    //        "amount":2000,
    //        "orderReceivedData": "2026-01-09T11:36:29.282436",
    //        "orderDeliveryData": "2026-01-12"
    //    }
    //}
    //
    //{
    //    "order":
    //    {
    //        "orderId": "0000000005",
    //        "product": "apples",
    //        "amount":500,
    //        "orderReceivedData": "2026-01-09T11:36:29.308259",
    //        "orderDeliveryData": "2026-01-12"
    //    }
    //}
    //
    //{
    //    "order":
    //    {
    //        "orderId": "0000000002",
    //        "product": "bananas",
    //        "amount":75,
    //        "orderReceivedData": "2026-01-09T11:36:29.234701",
    //        "orderDeliveryData": "2026-01-12"
    //    }
    //}
    //
    //{
    //    "order":
    //    {
    //        "orderId": "0000000003",
    //        "product": "oranges",
    //        "amount":1000,
    //        "orderReceivedData": "2026-01-09T11:36:29.247027",
    //        "orderDeliveryData": "2026-01-12"
    //    }
    //}
    private static void sendGets(HttpClient client, List<URI> uris) {
        var futures = uris.stream()
                .map(uri-> HttpRequest.newBuilder(uri))
                .map(HttpRequest.Builder::build)
                .map(request -> client.sendAsync(
                        request, HttpResponse.BodyHandlers.ofString()
                )).toList(); // collect completable futures into list
        var allFutureRequests = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture<?>[0])
        );

        var allComplete = allFutureRequests.join(); // aggregates futures in some way - join - aggregated completable future ? no response
        // need to get from each
        futures.forEach(f -> {
            System.out.println(f.join().body());
        });
    }
}
