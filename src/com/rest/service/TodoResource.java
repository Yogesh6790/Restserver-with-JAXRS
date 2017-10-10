package com.rest.service;

import java.io.IOException;
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
public class TodoResource {

	@Context
	UriInfo uriInfo;

	// GET, POST, PUT, PATCH, DELETE --> Http Methods

	// Get All Todos
	@GET
	@Path("getAllTodos")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<TodoVo> getTodoList() {
		return (List<TodoVo>) TodoDao.instance.getModel().values();
	}

	// Get particular Todo
	@GET
	@Path("getTodo/{id}")
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
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCount() {
		int count = TodoDao.instance.getModel().size();
		return String.valueOf(count);
	}

	// Insert new Todo -- it is not idempotent
	@POST
	@Path("addTodo")
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newTodo(@FormParam("summary") String summary,
			@FormParam("description") String description,
			@Context HttpServletResponse servletResponse) throws IOException {
		String id = String.valueOf(TodoDao.instance.getModel().size() + 1);
		TodoVo todo = new TodoVo(id, summary);
		if (description != null) {
			todo.setDescription(description);
		}
		TodoDao.instance.getModel().put(id, todo);
		servletResponse.sendRedirect("../create_todo.html");
	}

	// Update Todo / Insert new Todo -- it is idempotent
	@PUT
	@Path("updateTodo")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response addTodo(JAXBElement<TodoVo> todoVo) {
		TodoVo vo = todoVo.getValue();
		return putAndGetResponse(vo);
	}

	private Response putAndGetResponse(TodoVo vo) {
		Response res;
		if (TodoDao.instance.getModel().containsKey(vo.getId())) {
			res = Response.noContent().build();
		} else {
			res = Response.created(uriInfo.getAbsolutePath()).build();
		}
		TodoDao.instance.getModel().put(vo.getId(), vo);
		return res;
	}

	// Patch -- Modify only particular attributes -- it is idempotent
	@PATCH
	@Path("modifyTodo")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response patchResponse(JAXBElement<TodoVo> todoVo) {
		TodoVo vo = todoVo.getValue();
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
		return res;
	}

	// Remove Todo with params
	@DELETE
	@Path("deleteTodo/{id}")
	public void deleteTodo(@PathParam("id") String id) {
		TodoVo c = TodoDao.instance.getModel().remove(id);
		if (c == null)
			throw new RuntimeException("Delete: Todo with " + id + " not found");
	}

	// Remove Todo with form
	@DELETE
	@Path("deleteTodo")
	public void deleteTodo(JAXBElement<TodoVo> todoVo) {
		TodoVo c = TodoDao.instance.getModel()
				.remove(todoVo.getValue().getId());
		if (c == null)
			throw new RuntimeException("Delete: Todo with "
					+ todoVo.getValue().getId() + " not found");
	}

	// Testing Simple JSON and XML return types
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,
			MediaType.TEXT_XML })
	public TodoVo getApplicationXml() {
		TodoVo todo = new TodoVo("10", "Application XML Todo Summary");
		todo.setDescription("Application XML Todo Description");
		return todo;
	}

}
