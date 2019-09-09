package soap.webservice.consumer.service;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import soap.webservice.consumer.configure.SoapConsumer;
import soap.webservice.consumer.model.PostWebserviceRequest;
import soap.webservice.consumer.model.PostWebserviceResponse;

@Service
public class SoapConsumerService {
	
	@Autowired
	SoapConsumer con;

	public String getPost() {

		PostWebserviceRequest postReq = new PostWebserviceRequest();
		postReq.setPostId( BigInteger.valueOf(1) );
		
		PostWebserviceResponse postRes = con.sendAndReceive(postReq, "PostWebServiceRequest");
		
		return postRes.getSubject();
	}
}
