package com.rest.service;

import java.util.HashMap;
import java.util.Map;

public enum TodoDao {
	instance;
	
	private Map<String, TodoVo> contentProvider = new HashMap<>();
	 
	private TodoDao() {
		TodoVo todo = new TodoVo("1", "Learn REST","Read http://www.vogella.com/tutorials/REST/article.html");
		this.contentProvider.put("1", todo);
		todo = new TodoVo("2", "Do something","Read complete http://www.vogella.com");
		this.contentProvider.put("2", todo);
	}
	
    public Map<String, TodoVo> getModel(){
        return contentProvider;
    }

	
}
