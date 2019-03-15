package com.cytoscape.CytoscapeLiteratureNetwork.internal.task;

import java.util.List;

import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class SearchPubmedMetadataFactory extends AbstractTaskFactory {

	private List<String> ids;

	public SearchPubmedMetadataFactory(List<String> ids){
		this.ids=ids;
	}
	
	@Override
	public TaskIterator createTaskIterator() {
		// TODO Auto-generated method stub
		return new TaskIterator(new SearchPubmedMetadataTask(ids));
	}

}
