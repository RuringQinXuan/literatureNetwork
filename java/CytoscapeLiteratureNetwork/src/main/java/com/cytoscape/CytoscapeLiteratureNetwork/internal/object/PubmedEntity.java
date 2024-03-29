package com.cytoscape.CytoscapeLiteratureNetwork.internal.object;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class PubmedEntity {



	private Object entityID;
	private Object name;
	private String type;
	private int foreground,background;
	private double score;
	
	
	public PubmedEntity(Object entityID,Object name, Object str_foreground, Object str_background, Object str_score,String type)
	{
		this.entityID=entityID;
		this.name=name;
		this.type=type;

		try {
			this.foreground = Integer.valueOf(str_foreground.toString());
		} catch (NumberFormatException e) {
			this.foreground=0;
		}
		try {
			this.background = Integer.valueOf(str_background.toString());
		} catch (NumberFormatException e) {
			this.background=0;
		}
		try {
			this.score = Double.valueOf(str_score.toString());
		} catch (NumberFormatException e) {
			this.score=0;
		}
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
	public int getForeground() {
		return foreground;
	}

	public int getBackground() {
		return background;
	}

	public double getScore() {
		return score;
	}

	
	
}
