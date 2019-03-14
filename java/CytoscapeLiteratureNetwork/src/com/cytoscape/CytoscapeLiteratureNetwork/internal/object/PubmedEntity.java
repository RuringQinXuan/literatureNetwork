package com.cytoscape.CytoscapeLiteratureNetwork.internal.object;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class PubmedEntity {



	private Object entityID;
	private Object name;
	private String type;
	
	
	public PubmedEntity(Object entityID,Object name,String type)
	{
		this.entityID=entityID;
		this.name=name;
		this.type=type;
	}

	public String getEntityID() {
		return (String) entityID;
	}

	public String getName() {
		return (String) name;
	}

	public String getType() {
		return type;
	}
	
	
}