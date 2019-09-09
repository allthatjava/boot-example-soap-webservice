# Spring Boot - Webservice Provider / Consumer
This example contains two parts SOAP Webservice Provider and SOAP Webservice Consumer. This is a minimal example to make how to make a Webservice Provider and how to consumer a Webservice based on the given WSDL.


## Web Service Provider
Even though the Java code would be generated, minimum xsd must be provided to generate the Java code.

##### Gradle

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
I've set the server port to 8081 for the provider to avoid port conflict when you run both application on the same machine.

* In application.properties

```
server.port=8081
```

* Once you application is run ( You can run with gradle command `./gradlew clean jaxb build bootRun`, you can see the live WSDL from the browser with the following URL.

```
http://localhost:8081/ws/post-webservice.wsdl
```



## Web Service Consumer


#### Running Gradle script
To generate the Client side Java code from __wsdl__, use `jaxb` task.


##### Gradle
The following gradle script will generate Java code based on given WSDL file

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
//					schema: 'http://localhost:8081/ws/post-webservice.wsdl',	// You can use remote or local wsdl
					schema: 'src/main/resources/post-webservice.wsdl',			// or use local wsdl file
					package: 'soap.webservice.consumer.model' )						// Package name for generated Java code
        {
            arg(value: "-wsdl")
        }
    }
}
```

* To run the example, run the following command on the command line

```
./gradlew clean jaxb build bootRun
```

* Through the following code, any kind of generated Webservice call can be made 

```
public class SoapConsumer extends WebServiceGatewaySupport {

	@SuppressWarnings("unchecked")
	public <T,R> R getPost(T postReq, R postRes, String serviceName) {
		
		postRes = (R) getWebServiceTemplate()
				.marshalSendAndReceive("http://localhost:8081/ws/"+serviceName, 
						postReq, null);
		
		return postRes;
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
		marshaller.setContextPath("soap.webservice.consumer.model");
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

#### Test 
The counter part(soap-webservice-provider) application must be up for the test.

```
http://localhost:8080/post/1
```


