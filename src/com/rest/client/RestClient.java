package com.rest.client;

import java.net.URI;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

import com.rest.service.TodoDao;
import com.rest.service.TodoVo;

public class RestClient {

	public static void main(String[] args) {
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		WebTarget service = client.target(getBaseURI());

		// Test Simple Hello World Service
		Response response = service.path("hello").request().
		// Response header can accept MediaType.APPLICATION_JSON or
		// MediaType.TEXT_XML
		// or MediaType.TEXT_HTML or MediaType.TEXT_PLAIN
				accept(MediaType.APPLICATION_JSON).get(Response.class);
		String plainAnswer = service.path("hello").request()
				.accept(MediaType.TEXT_PLAIN).get(String.class);
		String xmlAnswer = service.path("hello").request()
				.accept(MediaType.TEXT_XML).get(String.class);
		String htmlAnswer = service.path("hello").request()
				.accept(MediaType.TEXT_HTML).get(String.class);
		String jsonAnswer = service.path("hello").request()
				.accept(MediaType.APPLICATION_JSON).get(String.class);

		System.out.println(response.toString());
		System.out.println(plainAnswer);
		System.out.println(xmlAnswer);
		System.out.println(htmlAnswer);
		System.out.println(jsonAnswer);

		showAllTodos(service);

		// create one todo
		service = client.target(UriBuilder
				.fromUri("http://localhost:8080/RestServer-With-JAXRS/rest/todo").build());
		TodoVo todo = new TodoVo("3", "Blabla","Description with POST");
		response = service
				.path("addTodo")
				.request(MediaType.APPLICATION_XML)
				.post(Entity.entity(todo, MediaType.APPLICATION_XML));
		System.out.println(response.getStatus()); // Return code should be 201 == created resource
		showAllTodos(service);

		todo.setDescription("Description with PUT");
		response = service
				.path("todo/updateTodo")
				.request(MediaType.APPLICATION_XML)
				.put(Entity.entity(todo, MediaType.APPLICATION_XML),
						Response.class);
		System.out.println(response.getStatus());
		showAllTodos(service);

		todo.setDescription("Description with PATCH");
		todo.setSummary(null);
		response = service
				.path("todo/modifyTodo")
				.request(MediaType.APPLICATION_XML)
				.put(Entity.entity(todo, MediaType.APPLICATION_XML),
						Response.class);
		System.out.println(response.getStatus());
		showAllTodos(service);
		
		System.out.println("Displaying individual record");
		System.out.println(service.path("rest/todo/getTodo/3").request()
				.accept(MediaType.TEXT_XML).get(String.class));
		
		
		System.out.println("Total Number of records");
		System.out.println(service.path("rest/todo/count").request()
				.accept(MediaType.TEXT_PLAIN).get(String.class));

	}

	private static void showAllTodos(WebTarget service) {
		System.out.println(service.path("todo/getAllTodos").request()
				.accept(MediaType.APPLICATION_JSON).get(String.class));
		System.out.println(service.path("todo/getAllTodos").request()
				.accept(MediaType.APPLICATION_XML).get(String.class));
	}

	private static URI getBaseURI() {
		return UriBuilder
				.fromUri("http://localhost:8080/RestServer-With-JAXRS/rest").build();
	}

}
