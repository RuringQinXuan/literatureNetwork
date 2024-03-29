package com.cytoscape.CytoscapeLiteratureNetwork.internal.task;

import java.util.List;
import java.util.stream.Collectors;

import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class SearchPubmedMetadataFactory extends AbstractTaskFactory {

	private List<Long> ids;

	public SearchPubmedMetadataFactory(List<Long> ids){
		this.ids=ids;
	}

	public SearchPubmedMetadataFactory(List<String> ids, boolean string){
		this.ids=ids.stream().map(Long::valueOf).collect(Collectors.toList());
	}
	
	@Override
	public TaskIterator createTaskIterator() {
		// TODO Auto-generated method stub
		return new TaskIterator(new SearchPubmedMetadataTask(ids));
	}

}
