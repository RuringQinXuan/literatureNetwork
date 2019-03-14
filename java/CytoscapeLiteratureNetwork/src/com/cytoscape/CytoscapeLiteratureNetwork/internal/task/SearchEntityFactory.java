package com.cytoscape.CytoscapeLiteratureNetwork.internal.task;

import java.util.List;

import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class SearchEntityFactory extends AbstractTaskFactory {

	private List<String> ids;
	private String type2;

	public SearchEntityFactory(String type2,List<String> ids){
		this.ids=ids;
		this.type2=type2;
	}
	@Override
	public TaskIterator createTaskIterator() {
		// TODO Auto-generated method stub
		return new TaskIterator(new SearchEntityTask(type2,ids));
	}

}
