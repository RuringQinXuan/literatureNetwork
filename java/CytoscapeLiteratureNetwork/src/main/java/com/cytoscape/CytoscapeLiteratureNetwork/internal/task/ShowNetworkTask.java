package com.cytoscape.CytoscapeLiteratureNetwork.internal.task;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import org.json.simple.JSONObject;

public class ShowNetworkTask extends AbstractTask {

	private final CyNetworkManager netMgr;
	private final CyNetworkFactory cnf;
	private final CyNetworkNaming namingUtil; 
	private JSONObject result;
	
	public ShowNetworkTask(final CyNetworkManager netMgr, final CyNetworkNaming namingUtil, final CyNetworkFactory cnf,JSONObject result){
		this.netMgr = netMgr;
		this.cnf = cnf;
		this.namingUtil = namingUtil;
		this.result=result;
	}
	
	public void run(final TaskMonitor monitor) {
		// Create an empty network
		final CyNetwork myNet = cnf.createNetwork();
		myNet.getRow(myNet).set(CyNetwork.NAME,
				      namingUtil.getSuggestedNetworkTitle("My Network"));
		
		// Add two nodes to the network
		final CyNode node1 = myNet.addNode();
		final CyNode node2 = myNet.addNode();
		
		// set name for new nodes
		myNet.getDefaultNodeTable().getRow(node1.getSUID()).set("name", "Node1");
		myNet.getDefaultNodeTable().getRow(node2.getSUID()).set("name", "Node2");
		
		// Add an edge
		myNet.addEdge(node1, node2, true);
		
		// set name for new edges
				myNet.getDefaultEdgeTable().getRow(node1.getSUID()).set("name", "Node1");
				myNet.getDefaultEdgeTable().getRow(node2.getSUID()).set("name", "Node2");
				myNet.getDefaultEdgeTable().getRow(node2.getSUID()).set("name", "support_sentences_ids");
				
		netMgr.addNetwork(myNet);
		
		// Set the variable destroyNetwork to true, the following code will destroy a network
		final boolean destroyNetwork = false;
		if (destroyNetwork){
			// Destroy it
			 netMgr.destroyNetwork(myNet);			
		}
	}
}
