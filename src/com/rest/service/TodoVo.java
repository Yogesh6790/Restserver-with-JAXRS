package com.rest.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="todovo")
@XmlAccessorType (XmlAccessType.FIELD)
//JAX-RS supports an automatic mapping from JAXB annotated class to XML and JSON
public class TodoVo {
	private String id;
    private String summary;
    private String description;

    
    public TodoVo(){

    }
    
	public TodoVo(String id, String summary,String description) {
		this.id = id;
		this.summary = summary;
		this.description = description;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
