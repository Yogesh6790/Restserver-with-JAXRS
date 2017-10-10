package com.rest.client;

import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

import com.rest.service.TodoDao;

public class RestClient {

	public static void main(String[] args) {
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        //Test Sample Hello World service
        WebTarget target = client.target(UriBuilder.fromUri("http://localhost:8080/RestServer-With-JAXRS").build());
        String response = target.path("rest").
                path("hello").
                request().
                // Response header can accept MediaType.APPLICATION_JSON or MediaType.TEXT_XML 
                // or MediaType.TEXT_HTML or MediaType.TEXT_PLAIN
                accept(MediaType.APPLICATION_JSON). 
                get(Response.class)
                .toString();
        String plainAnswer =
                target.path("rest").path("hello").request().accept(MediaType.TEXT_PLAIN).get(String.class);
        String xmlAnswer =
                target.path("rest").path("hello").request().accept(MediaType.TEXT_XML).get(String.class);
        String htmlAnswer=
                target.path("rest").path("hello").request().accept(MediaType.TEXT_HTML).get(String.class);
        String jsonAnswer =
                target.path("rest").path("hello").request().accept(MediaType.APPLICATION_JSON).get(String.class);
        
        System.out.println(response);
        System.out.println(plainAnswer);
        System.out.println(xmlAnswer);
        System.out.println(htmlAnswer);
        System.out.println(jsonAnswer);
        
        
        target = client.target(UriBuilder.fromUri("http://localhost:8080/RestServer-With-JAXRS").build());
        response = target.path("rest").
                path("todo").
                request().
                accept(MediaType.APPLICATION_JSON). 
                get(Response.class)
                .toString();
        xmlAnswer =
                target.path("rest").path("todo").request().accept(MediaType.TEXT_XML).get(String.class);
        jsonAnswer =
                target.path("rest").path("todo").request().accept(MediaType.APPLICATION_JSON).get(String.class);
        String xmlAnswer2 =
                target.path("rest").path("todo").request().accept(MediaType.APPLICATION_XML).get(String.class);

        
        System.out.println(response);
        System.out.println(xmlAnswer);
        System.out.println(xmlAnswer2);
        System.out.println(jsonAnswer);
        
	}

}
