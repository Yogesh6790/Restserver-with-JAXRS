package com.rest.service;

import java.util.HashMap;
import java.util.Map;

public enum TodoDao {
	instance;
	
	private Map<String, TodoVo> contentProvider = new HashMap<>();
	 
	private TodoDao() {
		TodoVo todo = new TodoVo("1", "Learn REST","Read 1");
		this.contentProvider.put("1", todo);
		todo = new TodoVo("2", "Do something","Read 2");
		this.contentProvider.put("2", todo);
	}
	
    public Map<String, TodoVo> getModel(){
        return contentProvider;
    }

	
}
