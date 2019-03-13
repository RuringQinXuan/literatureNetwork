package com.cytoscape.CytoscapeLiteratureNetwork.internal.task;

import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class ShowPanelTaskFactory extends AbstractTaskFactory {

	private CyServiceRegistrar serviceRegistrar;
	
	public ShowPanelTaskFactory(CyServiceRegistrar serviceRegistrar) {
		super();
		this.serviceRegistrar = serviceRegistrar;
	}

	@Override
	public TaskIterator createTaskIterator() {
		// TODO Auto-generated method stub
		return new TaskIterator(new ShowPanelTask(this.serviceRegistrar));
	}

}
