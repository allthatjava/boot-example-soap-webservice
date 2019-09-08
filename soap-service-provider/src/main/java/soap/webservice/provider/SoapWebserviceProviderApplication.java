package soap.webservice.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@SpringBootApplication
public class SoapWebserviceProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoapWebserviceProviderApplication.class, args);
	}

	//########### Web Service Configuration ####################################
	@Bean
	public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(servlet, "/ws/*");
	}

	@Bean(name = "post-webservice")
	public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema eaaWebServiceSchema) {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("post-webservice");
		wsdl11Definition.setLocationUri("/ws");
		wsdl11Definition.setTargetNamespace("http://localhost:8080/post-webservice");
		wsdl11Definition.setSchema(eaaWebServiceSchema);
		return wsdl11Definition;
	}

	@Bean
	public XsdSchema eaaWebServiceSchema() {
		return new SimpleXsdSchema(new ClassPathResource("post-webservice.xsd"));
	}
}
