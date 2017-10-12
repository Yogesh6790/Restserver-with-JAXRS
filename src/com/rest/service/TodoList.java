package com.rest.service;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="todoList")
@XmlAccessorType (XmlAccessType.FIELD)
public class TodoList {
	
	@XmlElement(name = "todovo")
	private List<TodoVo> todoList;

	public List<TodoVo> getTodoList() {
		return todoList;
	}

	public void setTodoList(List<TodoVo> todoList) {
		this.todoList = todoList;
	}
	
 
}
