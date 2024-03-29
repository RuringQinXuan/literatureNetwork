package com.cytoscape.CytoscapeLiteratureNetwork.internal.task;

import java.util.List;

import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;
import org.json.simple.JSONObject;

public class BuildNetworkFactory extends AbstractTaskFactory {
	private List<String> documents;
	private List<String> entities;
	public BuildNetworkFactory(List<String> documents,List<String> entityList){
		this.documents=documents;
		this.entities=entityList;
		// TODO： Test
		//this.ids = Arrays.asList("30865931","30865919","30865925");
	}
	@Override
	public TaskIterator createTaskIterator() {
		// TODO Auto-generated method stub
		return new TaskIterator(new BuildNetworkTask(documents,entities));
	}

}
