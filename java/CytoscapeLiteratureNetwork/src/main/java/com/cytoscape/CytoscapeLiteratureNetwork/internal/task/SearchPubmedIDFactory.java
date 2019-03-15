package com.cytoscape.CytoscapeLiteratureNetwork.internal.task;

import java.util.List;

import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class SearchPubmedIDFactory extends AbstractTaskFactory {

	private String query;

	public SearchPubmedIDFactory(String query){
		this.query=query;
		System.out.println(query);
	}
	
	@Override
	public TaskIterator createTaskIterator() {
		// TODO Auto-generated method stub
		return new TaskIterator(new SearchPubmedIDTask(query));
	}

}
