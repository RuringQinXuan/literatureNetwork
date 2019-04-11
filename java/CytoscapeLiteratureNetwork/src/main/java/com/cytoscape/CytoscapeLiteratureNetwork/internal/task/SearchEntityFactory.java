package com.cytoscape.CytoscapeLiteratureNetwork.internal.task;

import java.util.List;

import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class SearchEntityFactory extends AbstractTaskFactory {

	private List<String> ids;
	private String type2;
	private int limit;
	private CyServiceRegistrar serviceRegistrar;
	public SearchEntityFactory(CyServiceRegistrar serviceRegistrar,String type2,int limit,List<String> ids){
		this.serviceRegistrar = serviceRegistrar;
		this.ids=ids;
		this.type2=type2;
		this.limit=limit;
	}
	@Override
	public TaskIterator createTaskIterator() {
		// TODO Auto-generated method stub
		return new TaskIterator(new SearchEntityTask(serviceRegistrar,type2,limit,ids));
	}

}
