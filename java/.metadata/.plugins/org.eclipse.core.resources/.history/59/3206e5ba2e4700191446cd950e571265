package com.cytoscape.CytoscapeLiteratureNetwork.internal.task;

import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import com.cytoscape.CytoscapeLiteratureNetwork.internal.view.Interface;


public class ShowPanelTask extends AbstractTask {
	private CyServiceRegistrar serviceRegistrar;

	public ShowPanelTask(CyServiceRegistrar serviceRegistrar) {
		super();
		this.serviceRegistrar = serviceRegistrar;
	}

	@Override
	public void run(TaskMonitor arg0) throws Exception {
		// TODO Auto-generated method stub
		Interface Interface = new Interface(this.serviceRegistrar);
		Interface.setVisible(true);
	}

}
