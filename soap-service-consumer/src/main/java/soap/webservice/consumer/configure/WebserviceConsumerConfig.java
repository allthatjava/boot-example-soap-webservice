package soap.webservice.consumer.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

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
