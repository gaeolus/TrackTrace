package org.oliot.tracktrace.service.query;

import org.apache.cxf.jaxrs.client.WebClient;



public class RESTClient {
	public static void main(String[] args) {
		WebClient client = WebClient.create("http://localhost:8080/");
		RESTLikeQueryService RESTLikeQueryService=client.path("Track_Trace/Service/GetStandardVersion").accept(
				"application/xml").get(RESTLikeQueryService.class);
		//System.out.println(RESTLikeQueryService.getStandardVersion());
	}

}
