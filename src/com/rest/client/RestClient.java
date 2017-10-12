package com.rest.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.rest.service.TodoVo;


public class RestClient {

	public static void main(String[] args) {
		try {
			// HTTP GET
			System.out.println("**** Getting All Todos ****");
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(getContext() + "/getAllTodos");
			HttpResponse response = null;
			StringBuilder sb = null;
			getAllValues(client,get,response,sb);
			
			// HTTP POST
			System.out.println("**** Posting data ****");
			client = new DefaultHttpClient();
	        HttpPost post = new HttpPost(getContext() +"/addTodo");
	        String json = "{\"id\":\"3\",\"summary\":\"summ\",\"description\":\"desc\"}";
        	post.setEntity(new StringEntity(json));
        	post.setHeader("Accept", "application/json");
        	post.setHeader("Content-type", "application/json");
		    response = client.execute(post);
		    System.out.println(response.getStatusLine().getStatusCode());
		    client.getConnectionManager().shutdown();
		    System.out.println();
		    
		    
		    // HTTP GET
		    System.out.println("**** Getting All Todos ****");
		    client = new DefaultHttpClient();
		    getAllValues(client,get,response,sb);
		    
		    System.out.println("**** Getting Unique Todo ****");
		    client = new DefaultHttpClient();
		    get = new HttpGet(getContext() + "/getAllTodos/2");
			get.setHeader("Accept", "application/json");
			response = client.execute(get);
			sb = new StringBuilder("");
			populateBuilder(response, sb);
			unmarshalJson(sb.toString());
			client.getConnectionManager().shutdown();
			System.out.println();
			
			
			//HTTP PUT
			System.out.println("**** Putting New Todo ****");
			client = new DefaultHttpClient();
			HttpPut put = new HttpPut(getContext() + "/updateTodo");
	        json = "{\"id\":\"7\",\"summary\":\"ffgg\",\"description\":\"desfdsfdsfsc\"}";
        	put.setEntity(new StringEntity(json));
			put.setHeader("Accept", "application/json");
        	put.setHeader("Content-type", "application/json");
		    response = client.execute(put);
		    System.out.println(response.getStatusLine().getStatusCode());
		    client.getConnectionManager().shutdown();
		    System.out.println();
		    
		    //HTTP GET
		    System.out.println("**** Getting All Todos ****");
		    client = new DefaultHttpClient();
		    get = new HttpGet(getContext() + "/getAllTodos");
		    getAllValues(client,get,response,sb);
		    
		    //HTTP PUT
			System.out.println("**** Putting existing Todo ****");
			client = new DefaultHttpClient();
			put = new HttpPut(getContext() + "/updateTodo");
	        json = "{\"id\":\"2\",\"summary\":\"gibran\",\"description\":\"music\"}";
        	put.setEntity(new StringEntity(json));
			put.setHeader("Accept", "application/json");
        	put.setHeader("Content-type", "application/json");
		    response = client.execute(put);
		    System.out.println(response.getStatusLine().getStatusCode());
		    client.getConnectionManager().shutdown();
		    System.out.println();
		    
		    //HTTP GET
		    System.out.println("**** Getting All Todos ****");
		    client = new DefaultHttpClient();
		    getAllValues(client,get,response,sb);
		    
		    //HTTP DELETE
		    System.out.println("**** Deleting Todo ****");
			client = new DefaultHttpClient();
			HttpDelete delete = new HttpDelete(getContext() + "/deleteTodo/1");		    
			response = client.execute(delete);
		    System.out.println(response.getStatusLine().getStatusCode());
		    client.getConnectionManager().shutdown();
		    System.out.println();
		    
		    //HTTP GET
		    System.out.println("**** Getting All Todos ****");
		    client = new DefaultHttpClient();
		    getAllValues(client,get,response,sb);
		    
		    //HTTP PATCH
		    System.out.println("**** Patching Todo ****");
		    client = new DefaultHttpClient();
		    HttpPatch patch = new HttpPatch(getContext() + "/modifyTodo");
	        json = "{\"id\":\"3\",\"summary\":\"ar rahman\"}";
	        patch.setEntity(new StringEntity(json));
	        patch.setHeader("Accept", "application/json");
	        patch.setHeader("Content-type", "application/json");
		    response = client.execute(patch);
		    System.out.println(response.getStatusLine().getStatusCode());
		    client.getConnectionManager().shutdown();
		    System.out.println();
		    
		    //HTTP GET
		    System.out.println("**** Getting All Todos ****");
		    client = new DefaultHttpClient();
		    getAllValues(client,get,response,sb);
		    
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
       		
	}

	private static void getAllValues(HttpClient client, HttpGet get, HttpResponse response, StringBuilder sb) throws ClientProtocolException, IOException {
		sb = new StringBuilder("");
		get.setHeader("Accept", "application/json");
		response = client.execute(get);
		populateBuilder(response, sb);
		unmarshalJsonList(sb.toString());
		client.getConnectionManager().shutdown();
		System.out.println();
		
	}

	private static void populateBuilder(HttpResponse response,StringBuilder sb) throws UnsupportedEncodingException, IllegalStateException, IOException {
		BufferedReader rd = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent()));
		
		String line = "";
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
	}

	private static void unmarshalJsonList(String jsonString) throws JsonParseException, JsonMappingException, IOException {
//		System.out.println("***** JSON without key  for list*****");
		ObjectMapper mapper = new ObjectMapper();
		List<TodoVo> list = mapper.readValue(jsonString,
				TypeFactory.defaultInstance().constructCollectionType(List.class,  
						TodoVo.class));
		list.forEach(t->{System.out.println(t.getId()+" "+t.getSummary()+" "+t.getDescription());});
//		System.out.println("***** JSON with key  for list*****");
//		TodoList employeList = mapper.readValue(jsonString,TodoList.class);
//		employeList.getTodoList().forEach(t->{System.out.println(t.getId()+" "+t.getSummary()+" "+t.getDescription());});
	}
	
	private static void unmarshalJson(String jsonString)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		TodoVo vo = mapper.readValue(jsonString, TodoVo.class);
		System.out.println(vo.getId() + " " + vo.getSummary() + " "
				+ vo.getDescription());
	}

	private static String getContext() {
		return "http://localhost:8080/RestServer-With-JAXRS/rest/todo";
	}

}
