package com.cytoscape.CytoscapeLiteratureNetwork.internal.object;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class PubmedMetadata {

	private String title;
	private String authors;
	private String publicdate;
	private String journal;
	private String id;
	
	public PubmedMetadata(JSONObject o)
	{
		id=(String) o.get("uid");
		title = (String) o.get("title");
		publicdate = (String) o.get("pubdate");
		journal = (String) o.get("source");
		authors = "";
		JSONArray listauthors = (JSONArray) o.get("authors");
		for(Object author : listauthors){
			authors += ", " + (String)((JSONObject)author).get("name");
		}
		if(authors.length()>2) {
			authors = authors.substring(2);
		}
	}

        
	public String getId() {
		return id;
	}


	public String getTitle() {
		return title;
	}

	public String getAuthors() {
		return authors;
	}

	public String getPublicdate() {
		return publicdate;
	}

	public String getJournal() {
		return journal;
	}
	
	
}
