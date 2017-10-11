package com.rest.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

@Path("/todo")
public class TodoResource<T> {

	@Context
	UriInfo uriInfo;
	
	// GET, POST, PUT, PATCH, DELETE --> Http Methods

	// Get All Todos
	@GET
	@Path("getAllTodos")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<TodoVo> getTodoList() {
		List<TodoVo> todos = new ArrayList<TodoVo>();
		todos.addAll(TodoDao.instance.getModel().values());
		return todos;
	}

	// Get particular Todo
	@GET
	@Path("getAllTodos/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public TodoVo getTodoListXML(@PathParam("id") String id) {
		TodoVo vo = TodoDao.instance.getModel().get(id);
		if (vo == null) {
			throw new RuntimeException("Get: Todo with " + id + " not found");
		}
		return vo;
	}

	// Get count of Todos
	@GET
	@Path("getAllTodos/count")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCount() {
		int count = TodoDao.instance.getModel().size();
		return String.valueOf(count);
	}

	// Insert new Todo -- it is not idempotent
	@POST
	@Path("addTodo")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Status newTodo(TodoVo vo) throws IOException {
		Status status = new Status();
		try{
			vo.setId(String.valueOf(TodoDao.instance.getModel().size() + 1));
			TodoDao.instance.getModel().put(vo.getId(), vo);
			status.setSuccess(true);
		}catch(Exception e){
			status.setSuccess(false);
			status.setMessage("Error in creating record");
		}
		return status;
	}
	
	
	@POST
	@Path("addTodoFromForm")
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newTodoForm(@FormParam("summary") String summary,
			@FormParam("description") String description,
			@Context HttpServletResponse servletResponse) throws IOException {
		String id = String.valueOf(TodoDao.instance.getModel().size() + 1);
		TodoVo todo = new TodoVo(id, summary, description);
		if (description != null) {
			todo.setDescription(description);
		}
		TodoDao.instance.getModel().put(id, todo);
		servletResponse.sendRedirect("../../create_todo.html");
	}

	// Update Todo / Insert new Todo -- it is idempotent
	@PUT
	@Path("updateTodo")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Status updateTodo(TodoVo vo) {
		Status status = new Status();
		try{
			putAndGetResponse(vo);
			status.setSuccess(true);
		}catch(Exception e){
			status.setSuccess(false);
			status.setMessage("Error in creating or updating record");
		}
		return status;
	}

	private Response putAndGetResponse(TodoVo vo) {
		Response res;
		if (TodoDao.instance.getModel().containsKey(vo.getId())) {
			res = Response.noContent().build();
		} else {
			res = Response.created(uriInfo.getAbsolutePath()).build();
			if(vo.getId() == null){
				vo.setId(String.valueOf(TodoDao.instance.getModel().size() + 1));
			}
		}
		TodoDao.instance.getModel().put(vo.getId(), vo);
		return res;
	}

	// Patch -- Modify only particular attributes -- it is idempotent
	@PATCH
	@Path("modifyTodo")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Status patchResponse(TodoVo vo) {
		Status status = new Status();
		try{
			TodoVo actualVo = TodoDao.instance.getModel().get(vo.getId());
			Response res;
			if (actualVo == null) {
				res = Response.noContent().build();
			} else {
				res = Response.ok().build();
				if (vo.getSummary() != null && !vo.getSummary().equals("")) {
					actualVo.setSummary(vo.getSummary());
				}
				if (vo.getDescription() != null && !vo.getDescription().equals("")) {
					actualVo.setDescription(vo.getDescription());
				}
				TodoDao.instance.getModel().put(vo.getId(), actualVo);
			}
			status.setSuccess(true);
		}catch(Exception e){
			status.setSuccess(false);
			status.setMessage("Error in patching record");
		}
		return status;
	}

	// Remove Todo with params
	@DELETE
	@Path("deleteTodo/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Status deleteTodo(@PathParam("id") String id) {
		Status status = new Status();
		TodoVo c = TodoDao.instance.getModel().remove(id);
		status.setSuccess(true);
		if (c == null){
			status.setSuccess(false);
			status.setMessage("Delete: Todo with " + id + " not found");
		}
		return status;	
	}

	// Remove Todo with form
	@DELETE
	@Path("deleteTodo")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Status deleteTodo(TodoVo vo) {
		Status status = new Status();
		TodoVo c = TodoDao.instance.getModel()
				.remove(vo.getId());
		status.setSuccess(true);
		if (c == null){
			status.setSuccess(false);
			status.setMessage("Delete: Todo with " + vo.getId() + " not found");
		}
		return status;
	}

}
