package soap.webservice.consumer.configure;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public class SoapConsumer extends WebServiceGatewaySupport {

	@SuppressWarnings("unchecked")
	public <T,R> R sendAndReceive(T postReq, String serviceName) {
		
		return (R) getWebServiceTemplate()
				.marshalSendAndReceive("http://localhost:8081/ws/"+serviceName, 
						postReq, null);
	}
}
