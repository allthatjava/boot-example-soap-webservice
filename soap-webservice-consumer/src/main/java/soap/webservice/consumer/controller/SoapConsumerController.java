package soap.webservice.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import soap.webservice.consumer.service.SoapConsumerService;

@RestController
public class SoapConsumerController {
	
	@Autowired
	SoapConsumerService service;
	
	@GetMapping("/post/{postId}")
	public String getSubject(@PathVariable Integer postId) {
		
		return service.getPost();
	}
}
