# Spring Boot - Webservice Provider / Consumer
This example contains two parts SOAP Webservice Provider and SOAP Webservice Consumer. This is a minimal example for how to make a Webservice Provider and how to consumer the Webservice based on the given WSDL.

* Provider : `soap-webservice-provider` will provide the Soap Webservice 
* Consumer : `soap-webservice-consumer` will use above service as a Soap Webservice consumer. 

*NOTE: This doesn't have extensive example or sample data but, this example will give you an idea how you can apply this technology on your application.*

## Web Service Provider
Even though the Java code would be generated, minimum __xsd__ must be provided to generate the Java code.

##### XSD

```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xs:schema version="1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema"
           xmlns:tns="http://localhost:8081/post-webservice"
           targetNamespace="http://localhost:8081/post-webservice"
           elementFormDefault="qualified">

    <xs:element name="postWebserviceRequest">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="postId" type="xs:integer" />
            </xs:sequence>
        </xs:complexType>
    </xs:element>

    <xs:element name="postWebserviceResponse">
        <xs:complexType>
            <xs:sequence >
                <xs:element name="postId" type="xs:integer" />
                <xs:element name="subject" type="xs:string" />
                <xs:element name="content" type="xs:string" />
            </xs:sequence>
        </xs:complexType>
    </xs:element>
</xs:schema>
```

##### Gradle
The task `jaxb` will generate Java code based on __xsd__.

```
configurations {
    jaxb
}
...
def jaxbTargetDir = file("/src/generated-sources/java")

sourceSets{
	main.java.srcDirs = [ jaxbTargetDir, 'src/main/java']
}

dependencies {
    compile("org.springframework.boot:spring-boot-starter-web-services")
    compile("wsdl4j:wsdl4j:1.6.1")
    jaxb('com.sun.xml.bind:jaxb-xjc:2.2.7', 'com.sun.xml.bind:jaxb-impl:2.2.7')
}
...
task jaxb {
    doLast() {
        jaxbTargetDir.mkdirs()

        ant.taskdef(
            name: "xjc",
            classname: "com.sun.tools.xjc.XJCTask",
            classpath: configurations.jaxb.asPath
        )

        ant.xjc(
                destdir: jaxbTargetDir,
                package: 'soap.webservice.provider.webservice.model',
                schema: 'src/main/resources/post-webservice.xsd'
        )
    }
}

```

#### Running Gradle script
To generate the Java code from __xsd__, use `jaxb` task.

```
./gradlew clean jaxb build bootRun
```

##### WSDL 
I've set the server port to 8081 for the provider to avoid port conflict when you run both applications on the same machine.

* In application.properties

```
server.port=8081
```

* Once you application is running ( You can run with gradle command `./gradlew clean jaxb build bootRun`, you can see the live WSDL from the browser with the following URL.

```
http://localhost:8081/ws/post-webservice.wsdl
```



## Web Service Consumer
This example will show how to consume the existing webservice. 

##### Running Gradle script
To generate the Client side Java code from __WSDL__, use `jaxb` task.

##### Gradle
The following part of the gradle script will generate Java code based on given __WSDL__ file

```
configurations {
    jaxb
}
...
sourceSets{
	main.java.srcDirs = ['src/generated-sources/java', 'src/main/java']
}

dependencies {
    compile("org.springframework.boot:spring-boot-starter-web")
    compile("org.springframework.ws:spring-ws-core")
    compile group: 'javax.xml.bind', name: 'jaxb-api', version: '2.2.7'

    jaxb('com.sun.xml.bind:jaxb-xjc:2.2.7', 'com.sun.xml.bind:jaxb-impl:2.2.7')
}
...
task jaxb {
    def jaxbTargetDir = file("/src/generated-sources/java")

    doLast() {
        jaxbTargetDir.mkdirs()

        ant.taskdef(
                name: "xjc",
                classname: "com.sun.tools.xjc.XJCTask",
                classpath: configurations.jaxb.asPath
        )

        ant.xjc(
            destdir: jaxbTargetDir,
//          schema: 'http://localhost:8081/ws/post-webservice.wsdl',    // You can use remote or local wsdl
            schema: 'src/main/resources/post-webservice.wsdl',          // or use local wsdl file
            package: 'soap.webservice.consumer.model' )                 // Package name for generated Java code
        {
            arg(value: "-wsdl")
        }
    }
}
```

* To run the example, run the following command

```
./gradlew clean jaxb build bootRun
```

* Through the following code, any kind of generated Webservice call can be made 

```
public class SoapConsumer extends WebServiceGatewaySupport {

    @SuppressWarnings("unchecked")
    public <T,R> R sendAndReceive(T postReq, String serviceName) {
        
        return (R) getWebServiceTemplate()
                .marshalSendAndReceive("http://localhost:8081/ws/"+serviceName, 
                        postReq, null);
    }
}
```

* This is a configuration to setup the Webservice marshallers.

```
@Configuration
public class WebserviceConsumerConfig {
	
	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("soap.webservice.consumer.model");  // Where generated Java code would be for the webservice
		return marshaller;
	}

	@Bean
	public SoapConsumer postClient(Jaxb2Marshaller marshaller) {
		SoapConsumer client = new SoapConsumer();
		client.setDefaultUri("http://localhost:8081/ws");	// Target service
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}
}
```

##### Consumer Test 
The counter part(soap-webservice-provider) application must be up for the test.

```
http://localhost:8080/post/1
```


