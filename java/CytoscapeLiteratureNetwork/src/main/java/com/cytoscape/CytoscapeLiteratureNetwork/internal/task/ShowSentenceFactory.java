package com.cytoscape.CytoscapeLiteratureNetwork.internal.task;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.task.AbstractNetworkTaskFactory;
import org.cytoscape.task.EdgeViewTaskFactory;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.work.TaskIterator;

public class ShowSentenceFactory extends AbstractNetworkTaskFactory implements EdgeViewTaskFactory {

	@Override
	public TaskIterator createTaskIterator(CyNetwork arg0) {
		return new TaskIterator(new ShowSentenceTask(arg0));
	}

	@Override
	public TaskIterator createTaskIterator(View<CyEdge> arg0, CyNetworkView arg1) {
		return new TaskIterator(new ShowSentenceTask(arg1.getModel(), arg0.getModel()));
	}

	@Override
	public boolean isReady(View<CyEdge> arg0, CyNetworkView arg1) {
		return true;
	}

}