# Spring Boot - Webservice Provider / Consumer










### Web Service Provider

##### Gradle

```
configurations {
    jaxb
}
...

dependencies {
    compile("org.springframework.boot:spring-boot-starter-web-services")
    compile("wsdl4j:wsdl4j:1.6.1")
    jaxb('com.sun.xml.bind:jaxb-xjc:2.2.7', 'com.sun.xml.bind:jaxb-impl:2.2.7')
}i
...
task jaxb {
    def jaxbTargetDir = file("/src/generated-sources/java")

    doLast() {
        jaxbTargetDir.mkdirs()

        ant.taskdef(
            name: "xjc",
            classname: "com.sun.tools.xjc.XJCTask",
            classp ath: configurations.jaxb.asPath
        )
        ant.jaxbTargetDir = jaxbTargetDir

        ant.xjc(
                destdir: 'src/main/java',
                package: 'soap.webservice.provider.webservice.model',
                schema: 'src/main/resources/post-webservice.xsd'
        )
    }
}

```


##### WSDL 

```
http://localhost:8081/ws/post-webservice.wsdl
```



### Web Service Consumer


```
http://localhost:8080/post/1
```


