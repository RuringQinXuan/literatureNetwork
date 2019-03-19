package com.cytoscape.CytoscapeLiteratureNetwork.internal.task;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyRow;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class ShowSentenceTask extends AbstractTask {
	private CyNetwork network;
	private CyEdge edge;

	public ShowSentenceTask(CyNetwork network) {
		this(network, null);
	}
	public ShowSentenceTask(CyNetwork network, CyEdge edge) {
		super();
		this.network = network;
		this.edge=edge;
	}

	@Override
	public void run(TaskMonitor arg0) throws Exception {
		if(edge == null) {
			for(CyRow edgeRow : this.network.getDefaultEdgeTable().getAllRows()) {
				if(edgeRow.get(CyNetwork.SELECTED, Boolean.class)) {
					this.edge = (CyEdge) edgeRow;
				}
			}
		}
		this.network.getDefaultEdgeTable().getAllRows().get(0).get(CyNetwork.SELECTED, Boolean.class);
	}

}
