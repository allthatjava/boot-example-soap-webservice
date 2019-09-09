package soap.webservice.consumer.configure;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public class SoapConsumer extends WebServiceGatewaySupport {

	@SuppressWarnings("unchecked")
	public <T,R> R getPost(T postReq, R postRes, String serviceName) {
		
		postRes = (R) getWebServiceTemplate()
				.marshalSendAndReceive("http://localhost:8081/ws/"+serviceName, 
						postReq, null);
		
		return postRes;
	}
}
