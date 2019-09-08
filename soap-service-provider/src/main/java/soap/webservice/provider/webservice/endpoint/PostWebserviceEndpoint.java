package soap.webservice.provider.webservice.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import soap.webservice.provider.webservice.model.PostWebserviceRequest;
import soap.webservice.provider.webservice.model.PostWebserviceResponse;

@Endpoint
public class PostWebserviceEndpoint extends WsConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(PostWebserviceEndpoint.class.getSimpleName());

    @PayloadRoot(namespace="http://localhost:8080/post-webservice", localPart="postWebserviceRequest")
    @ResponsePayload
    public PostWebserviceResponse getPost(@RequestPayload PostWebserviceRequest request){

        logger.info("getPost:"+request);
        PostWebserviceResponse res = new PostWebserviceResponse();
        res.setPostId(request.getPostId());
        res.setSubject("Test Subject");
        res.setContent("Test Content");

        return res;
    }
}
