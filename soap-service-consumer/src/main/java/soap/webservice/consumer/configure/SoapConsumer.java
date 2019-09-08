package soap.webservice.consumer.configure;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import soap.webservice.consumer.model.PostWebserviceRequest;
import soap.webservice.consumer.model.PostWebserviceResponse;

public class SoapConsumer extends WebServiceGatewaySupport {
	
	public PostWebserviceResponse getPost(PostWebserviceRequest postReq) {
		
		return (PostWebserviceResponse) getWebServiceTemplate()
												.marshalSendAndReceive(
														"http://localhost:8081/ws/PostWebServiceRequest",
														postReq,
														new SoapActionCallback("http://spring.io/guides/gs-producing-web-service/GetCountryRequest")
									);
	}
	
}
