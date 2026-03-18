package com.init_spring_bean_mvn.demo.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.stream.Stream;

import static java.net.HttpURLConnection.HTTP_OK;

public class HttpClientGet {
    public static void main(String[] args) {
        try {
            // URL url = new URL("http://example.com");
            // URL url = new URL("http://example.com/extra"); // returns title, id, userId, completed
            
            URL url = new URL("http://localhost:8080"); // url still needed
            HttpClient client = HttpClient.newHttpClient(); // 1 of 2 ways, includes default default setttings - http 2 protocol
            // Pros - Request being sent is more transparent than HttpURLConnection
            // has simple builder pattern to create request in simple step like jpa.Criteria
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(url.toURI())
                    .header("User-Agent", "Chrome")
                    .headers("Accept", "application/json", "Accept", "text/html")
                    .timeout(Duration.ofSeconds(30))
                    .build(); // returns request instance

            // response to Stream of String
            HttpResponse<Stream<String>> response = client.send(request, HttpResponse.BodyHandlers.ofLines());

            // instance can only use one request but may persist on many request objects and persists even if call close connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); // content type application/ json, URF-8
            // doesn't establish actual network connection - call urlConnection.connect()
            // Allows to configure connection without and before connecting to it - read/ write depending on protocol
            // Java needs to create socket and handshaking, etc... under api

            response.body()
                    .filter(s -> s.contains("<h1>"))
                    .map(s -> s.replaceAll("<[^>]*", "").strip())
                    .forEach(System.out::println);
//            HttpResponse<String> response = client.send(request,
//                    HttpResponse.BodyHandlers.ofString());

            // NEXT - CompletableFuture class with HttpClient
            if(response.statusCode() != HTTP_OK) { // no inputStream but can configure to
                System.out.println("Error reading " + url);
                return;
            }
            // BodyHandlers class on HttpResponse - methods prefixed with of - ofByteArray, ofFile, ofInputStream,  - help processing response data
            // Setting header fields contain information on request or responses
            // urlConnection.setRequestMethod("GET");
            // urlConnection.setRequestMethod("POST");
            // urlConnection.setRequestProperty("User-Agent", "Chrome"); //Key - value pair - Browser usually sets so simulating
            // example.com should accept
            // property could take comma del string of values
            // urlConnection.setRequestProperty("Accept", "application/json, text/html"); // benefit is app wont hang, returns 406 not accepted error
            // urlConnection.setReadTimeout(30000);
            // Note! didn't call connect - methods will...
            // Implicitly perform connection step - GET method for example - getInputStream also connects
            // Explicitely calling can make code more readable

                // Error
                //Error: Not Found
                //text/html
                //null=[HTTP/1.1 404 Not Found]
                //Transfer-Encoding=[chunked]
                //CF-Cache-Status=[HIT]
                //CF-RAY=[9baebcf0cf320cc4-EWR]
                //Server=[cloudflare]
                //Connection=[keep-alive]
                //Date=[Thu, 08 Jan 2026 21:20:18 GMT]
                //Age=[168]
                //Content-Type=[text/html]
                //null


            // Class URLConnection
                // Subclass HttpUrlConnection
                // Http basics: Get, Post, Put, Delete, Patch, Head, Options, Connect, Trace
                // Browser requests webpage - 
            // System.out.println(urlConnection.getContentType());
            // printContents(url.openStream()); // shortcut to use to call openConnection.getInputStream
            // urlConnection.getHeaderFields().entrySet().forEach(System.out::println);

            // urlConnection.connect();
            /// /Users/mjh5153/Library/Java/JavaVirtualMachines/openjdk-17/Contents/Home/bin/java -javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=64782 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath /Users/mjh5153/Downloads/demo/target/classes:/Users/mjh5153/.m2/repository/org/springframework/boot/spring-boot-starter/3.5.6/spring-boot-starter-3.5.6.jar:/Users/mjh5153/.m2/repository/org/springframework/boot/spring-boot/3.5.6/spring-boot-3.5.6.jar:/Users/mjh5153/.m2/repository/org/springframework/spring-context/6.2.11/spring-context-6.2.11.jar:/Users/mjh5153/.m2/repository/org/springframework/boot/spring-boot-autoconfigure/3.5.6/spring-boot-autoconfigure-3.5.6.jar:/Users/mjh5153/.m2/repository/org/springframework/boot/spring-boot-starter-logging/3.5.6/spring-boot-starter-logging-3.5.6.jar:/Users/mjh5153/.m2/repository/ch/qos/logback/logback-classic/1.5.18/logback-classic-1.5.18.jar:/Users/mjh5153/.m2/repository/ch/qos/logback/logback-core/1.5.18/logback-core-1.5.18.jar:/Users/mjh5153/.m2/repository/org/apache/logging/log4j/log4j-to-slf4j/2.24.3/log4j-to-slf4j-2.24.3.jar:/Users/mjh5153/.m2/repository/org/apache/logging/log4j/log4j-api/2.24.3/log4j-api-2.24.3.jar:/Users/mjh5153/.m2/repository/org/slf4j/jul-to-slf4j/2.0.17/jul-to-slf4j-2.0.17.jar:/Users/mjh5153/.m2/repository/jakarta/annotation/jakarta.annotation-api/2.1.1/jakarta.annotation-api-2.1.1.jar:/Users/mjh5153/.m2/repository/org/springframework/spring-core/6.2.11/spring-core-6.2.11.jar:/Users/mjh5153/.m2/repository/org/springframework/spring-jcl/6.2.11/spring-jcl-6.2.11.jar:/Users/mjh5153/.m2/repository/org/yaml/snakeyaml/2.4/snakeyaml-2.4.jar:/Users/mjh5153/.m2/repository/org/springframework/boot/spring-boot-starter-web/3.5.6/spring-boot-starter-web-3.5.6.jar:/Users/mjh5153/.m2/repository/org/springframework/boot/spring-boot-starter-json/3.5.6/spring-boot-starter-json-3.5.6.jar:/Users/mjh5153/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.19.2/jackson-databind-2.19.2.jar:/Users/mjh5153/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.19.2/jackson-annotations-2.19.2.jar:/Users/mjh5153/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.19.2/jackson-core-2.19.2.jar:/Users/mjh5153/.m2/repository/com/fasterxml/jackson/datatype/jackson-datatype-jdk8/2.19.2/jackson-datatype-jdk8-2.19.2.jar:/Users/mjh5153/.m2/repository/com/fasterxml/jackson/datatype/jackson-datatype-jsr310/2.19.2/jackson-datatype-jsr310-2.19.2.jar:/Users/mjh5153/.m2/repository/com/fasterxml/jackson/module/jackson-module-parameter-names/2.19.2/jackson-module-parameter-names-2.19.2.jar:/Users/mjh5153/.m2/repository/org/springframework/boot/spring-boot-starter-tomcat/3.5.6/spring-boot-starter-tomcat-3.5.6.jar:/Users/mjh5153/.m2/repository/org/apache/tomcat/embed/tomcat-embed-core/10.1.46/tomcat-embed-core-10.1.46.jar:/Users/mjh5153/.m2/repository/org/apache/tomcat/embed/tomcat-embed-websocket/10.1.46/tomcat-embed-websocket-10.1.46.jar:/Users/mjh5153/.m2/repository/org/springframework/spring-web/6.2.11/spring-web-6.2.11.jar:/Users/mjh5153/.m2/repository/org/springframework/spring-beans/6.2.11/spring-beans-6.2.11.jar:/Users/mjh5153/.m2/repository/io/micrometer/micrometer-observation/1.15.4/micrometer-observation-1.15.4.jar:/Users/mjh5153/.m2/repository/io/micrometer/micrometer-commons/1.15.4/micrometer-commons-1.15.4.jar:/Users/mjh5153/.m2/repository/org/springframework/spring-webmvc/6.2.11/spring-webmvc-6.2.11.jar:/Users/mjh5153/.m2/repository/org/springframework/spring-aop/6.2.11/spring-aop-6.2.11.jar:/Users/mjh5153/.m2/repository/org/springframework/spring-expression/6.2.11/spring-expression-6.2.11.jar:/Users/mjh5153/.m2/repository/org/springframework/boot/spring-boot-starter-data-jpa/3.5.6/spring-boot-starter-data-jpa-3.5.6.jar:/Users/mjh5153/.m2/repository/org/springframework/boot/spring-boot-starter-jdbc/3.5.6/spring-boot-starter-jdbc-3.5.6.jar:/Users/mjh5153/.m2/repository/com/zaxxer/HikariCP/6.3.3/HikariCP-6.3.3.jar:/Users/mjh5153/.m2/repository/org/springframework/spring-jdbc/6.2.11/spring-jdbc-6.2.11.jar:/Users/mjh5153/.m2/repository/org/hibernate/orm/hibernate-core/6.6.29.Final/hibernate-core-6.6.29.Final.jar:/Users/mjh5153/.m2/repository/jakarta/persistence/jakarta.persistence-api/3.1.0/jakarta.persistence-api-3.1.0.jar:/Users/mjh5153/.m2/repository/jakarta/transaction/jakarta.transaction-api/2.0.1/jakarta.transaction-api-2.0.1.jar:/Users/mjh5153/.m2/repository/org/jboss/logging/jboss-logging/3.6.1.Final/jboss-logging-3.6.1.Final.jar:/Users/mjh5153/.m2/repository/org/hibernate/common/hibernate-commons-annotations/7.0.3.Final/hibernate-commons-annotations-7.0.3.Final.jar:/Users/mjh5153/.m2/repository/io/smallrye/jandex/3.2.0/jandex-3.2.0.jar:/Users/mjh5153/.m2/repository/com/fasterxml/classmate/1.7.0/classmate-1.7.0.jar:/Users/mjh5153/.m2/repository/net/bytebuddy/byte-buddy/1.17.7/byte-buddy-1.17.7.jar:/Users/mjh5153/.m2/repository/org/glassfish/jaxb/jaxb-runtime/4.0.5/jaxb-runtime-4.0.5.jar:/Users/mjh5153/.m2/repository/org/glassfish/jaxb/jaxb-core/4.0.5/jaxb-core-4.0.5.jar:/Users/mjh5153/.m2/repository/org/eclipse/angus/angus-activation/2.0.2/angus-activation-2.0.2.jar:/Users/mjh5153/.m2/repository/org/glassfish/jaxb/txw2/4.0.5/txw2-4.0.5.jar:/Users/mjh5153/.m2/repository/com/sun/istack/istack-commons-runtime/4.1.2/istack-commons-runtime-4.1.2.jar:/Users/mjh5153/.m2/repository/jakarta/inject/jakarta.inject-api/2.0.1/jakarta.inject-api-2.0.1.jar:/Users/mjh5153/.m2/repository/org/antlr/antlr4-runtime/4.13.0/antlr4-runtime-4.13.0.jar:/Users/mjh5153/.m2/repository/org/springframework/data/spring-data-jpa/3.5.4/spring-data-jpa-3.5.4.jar:/Users/mjh5153/.m2/repository/org/springframework/data/spring-data-commons/3.5.4/spring-data-commons-3.5.4.jar:/Users/mjh5153/.m2/repository/org/springframework/spring-orm/6.2.11/spring-orm-6.2.11.jar:/Users/mjh5153/.m2/repository/org/springframework/spring-tx/6.2.11/spring-tx-6.2.11.jar:/Users/mjh5153/.m2/repository/org/slf4j/slf4j-api/2.0.17/slf4j-api-2.0.17.jar:/Users/mjh5153/.m2/repository/org/springframework/spring-aspects/6.2.11/spring-aspects-6.2.11.jar:/Users/mjh5153/.m2/repository/org/aspectj/aspectjweaver/1.9.24/aspectjweaver-1.9.24.jar:/Users/mjh5153/.m2/repository/com/h2database/h2/2.3.232/h2-2.3.232.jar:/Users/mjh5153/.m2/repository/com/mysql/mysql-connector-j/9.4.0/mysql-connector-j-9.4.0.jar:/Users/mjh5153/.m2/repository/jakarta/xml/bind/jakarta.xml.bind-api/4.0.2/jakarta.xml.bind-api-4.0.2.jar:/Users/mjh5153/.m2/repository/jakarta/activation/jakarta.activation-api/2.1.4/jakarta.activation-api-2.1.4.jar:/Users/mjh5153/.m2/repository/org/springframework/boot/spring-boot-starter-thymeleaf/3.5.6/spring-boot-starter-thymeleaf-3.5.6.jar:/Users/mjh5153/.m2/repository/org/thymeleaf/thymeleaf-spring6/3.1.3.RELEASE/thymeleaf-spring6-3.1.3.RELEASE.jar:/Users/mjh5153/.m2/repository/org/thymeleaf/thymeleaf/3.1.3.RELEASE/thymeleaf-3.1.3.RELEASE.jar:/Users/mjh5153/.m2/repository/org/attoparser/attoparser/2.0.7.RELEASE/attoparser-2.0.7.RELEASE.jar:/Users/mjh5153/.m2/repository/org/unbescape/unbescape/1.1.6.RELEASE/unbescape-1.1.6.RELEASE.jar:/Users/mjh5153/.m2/repository/org/springframework/boot/spring-boot-starter-validation/3.5.6/spring-boot-starter-validation-3.5.6.jar:/Users/mjh5153/.m2/repository/org/apache/tomcat/embed/tomcat-embed-el/10.1.46/tomcat-embed-el-10.1.46.jar:/Users/mjh5153/.m2/repository/org/hibernate/validator/hibernate-validator/8.0.3.Final/hibernate-validator-8.0.3.Final.jar:/Users/mjh5153/.m2/repository/jakarta/validation/jakarta.validation-api/3.0.2/jakarta.validation-api-3.0.2.jar:/Users/mjh5153/.m2/repository/org/projectlombok/lombok/1.18.42/lombok-1.18.42.jar:/Users/mjh5153/Downloads/mysql-connector-j-8.3.0/mysql-connector-j-8.3.0.jar com.init_spring_bean_mvn.demo.networking.WebContent
            /// application/json; charset=utf-8
            /// null=[HTTP/1.1 200OK]
            /// expires=[-1]
            /// Server=[cloudflare]
            /// vary=[Origin, Accept-Encoding]
            /// x-ratelimit-remaining=[999]
            /// via=[2.0heroku-router]
            /// reporting-endpoints=[heroku-nel="https://nel.heroku.com/reports?s=JsC8%2FBLzX74xV041mJD1ow%2B3jigsIHMGyhIsREy9lp8%3D&sid=e11707d5-02a7-43ef-b45e-2cf4d2036f7d&ts=1767900495"]
            /// nel=[{"report_to":"heroku-nel","response_headers":["Via"],"max_age":3600,"success_fraction":0.01,"failure_fraction":0.1}]
            /// x-powered-by=[Express]
            /// report-to=[{"group":"heroku-nel","endpoints":[{"url":"https://nel.heroku.com/reports?s=JsC8%2FBLzX74xV041mJD1ow%2B3jigsIHMGyhIsREy9lp8%3D\u0026sid=e11707d5-02a7-43ef-b45e-2cf4d2036f7d\u0026ts=1767900495"}],"max_age":3600}]
            /// Content-Length=[144]
            /// Age=[295]
            /// Content-Type=[application/json;charset=utf-8]
            /// CF-RAY=[9bae200a28397cff-EWR]
            /// x-ratelimit-limit=[1000]
            /// Connection=[keep-alive]
            /// cf-cache-status=[HIT]
            /// x-ratelimit-reset=[1767900498]
            /// Date=[Thu, 08 Jan 2026 19:33:11 GMT]
            /// pragma=[no-cache]
            /// Accept-Ranges=[bytes]
            /// access-control-allow-credentials=[true]
            /// Cache-Control=[max-age=43200]
            /// x-content-type-options=[nosniff]
            /// etag=[W/"90-3jL8C12KgG4G+HQmftxlFfSQEKw"]
            /// alt-svc=[h3=":443";ma=86400]
            /// max-age=43200
            // get single header field by key
            // System.out.println(urlConnection.getHeaderField("Cache-Control"));
            System.out.println(response.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
//application/json; charset=utf-8
//null=[HTTP/1.1 200 OK]
//expires=[-1]
//Server=[cloudflare]
//vary=[Origin, Accept-Encoding]
//x-ratelimit-remaining=[999]
//via=[2.0 heroku-router]
//reporting-endpoints=[heroku-nel="https://nel.heroku.com/reports?s=JsC8%2FBLzX74xV041mJD1ow%2B3jigsIHMGyhIsREy9lp8%3D&sid=e11707d5-02a7-43ef-b45e-2cf4d2036f7d&ts=1767900495"]
//nel=[{"report_to":"heroku-nel","response_headers":["Via"],"max_age":3600,"success_fraction":0.01,"failure_fraction":0.1}]
//x-powered-by=[Express]
//report-to=[{"group":"heroku-nel","endpoints":[{"url":"https://nel.heroku.com/reports?s=JsC8%2FBLzX74xV041mJD1ow%2B3jigsIHMGyhIsREy9lp8%3D\u0026sid=e11707d5-02a7-43ef-b45e-2cf4d2036f7d\u0026ts=1767900495"}],"max_age":3600}]
//Content-Length=[144]
//Age=[166]
//Content-Type=[application/json; charset=utf-8]
//CF-RAY=[9bae1ce3598dc4fb-EWR]
//x-ratelimit-limit=[1000]
//Connection=[keep-alive]
//cf-cache-status=[HIT]
//x-ratelimit-reset=[1767900498]
//Date=[Thu, 08 Jan 2026 19:31:02 GMT]
//pragma=[no-cache]
//Accept-Ranges=[bytes]
//access-control-allow-credentials=[true]
//Cache-Control=[max-age=43200]
//x-content-type-options=[nosniff]
//etag=[W/"90-3jL8C12KgG4G+HQmftxlFfSQEKw"]
//alt-svc=[h3=":443"; ma=86400]
    private static void printContents(InputStream is ) {
        try(BufferedReader inputStream = new BufferedReader(
                new InputStreamReader(is))
        ) {
            String line;
            while((line = inputStream.readLine()) != null) {
                System.out.println(line);
            }
        } catch(IOException io) {
            ;io.printStackTrace();
        }
    }
}
