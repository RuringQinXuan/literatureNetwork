package com.cytoscape.CytoscapeLiteratureNetwork.internal.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cytoscape.model.CyEdge;
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
		
		Map<String, CyNode> networkNodes = new HashMap<String, CyNode>();
		
		Map<String,JSONObject>nodes = (Map<String, JSONObject>) result.get("nodes");
		ArrayList<JSONObject> edges = (ArrayList<JSONObject>) result.get("edges");
		Map<String,String> sentences_dic = (Map<String, String>) result.get("sentences");
		
		// Add two nodes to the network
		
//		final CyNode node1 = myNet.addNode();
//		final CyNode node2 = myNet.addNode();
		
		myNet.getDefaultNodeTable().createColumn("type_id", String.class, false);
		myNet.getDefaultNodeTable().createColumn("id", String.class, false);
		myNet.getDefaultEdgeTable().createListColumn("sentence_text", String.class, false);
		myNet.getDefaultEdgeTable().createListColumn("sentence_id", String.class, false);
		myNet.getDefaultEdgeTable().createListColumn("sentenceInformation", String.class, false);
		myNet.getDefaultEdgeTable().createColumn("sentence_number", Integer.class, false);
		
		// set name for new nodes
//		myNet.getDefaeultNodeTable().getRow(node1.getSUID()).set("name", "Node1");
//		myNet.getDefaultNodeTable().getRow(node2.getSUID()).set("name", "Node2");
		for (Entry<String, JSONObject> entry :nodes.entrySet()) {
			final CyNode node1 = myNet.addNode();
			myNet.getDefaultNodeTable().getRow(node1.getSUID()).set("name", entry.getValue().get("name"));
			myNet.getDefaultNodeTable().getRow(node1.getSUID()).set("id", entry.getValue().get("id"));
			myNet.getDefaultNodeTable().getRow(node1.getSUID()).set("type_id", entry.getValue().get("type_id"));
			
			networkNodes.put((String)entry.getValue().get("type_id"), node1);
		}
		
		//set edges
		for(JSONObject edge:edges){
			final CyNode node1 = networkNodes.get(edge.get("source"));
			final CyNode node2 = networkNodes.get(edge.get("target"));
			ArrayList<JSONObject> sentenceList = (ArrayList<JSONObject>) edge.get("sentences");
			if(node1 == null || node2 == null) {
				System.out.println(edge.get("source") + " or " + edge.get("target") + " is not a node.");
				continue;
			}
			
			final CyEdge edge1 = myNet.addEdge(node1, node2, true);
			// set name for new edges
			
			
			List<String> sentence_content = new ArrayList<String>();
			List<String> sentence_ID = new ArrayList<String>();
			List<String> sentenceInformation = new ArrayList<String>();
			
			for (int i = 0; i < sentenceList.size(); i++) {
				JSONObject senten=sentenceList.get(i);
				sentence_ID.add((String) senten.get("sentenceID"));
				sentence_content.add(sentences_dic.get(senten.get("sentenceID")));		
				sentenceInformation.add((String) senten.toJSONString());
			}
			myNet.getDefaultEdgeTable().getRow(edge1.getSUID()).set("sentence_id", sentence_ID);
			myNet.getDefaultEdgeTable().getRow(edge1.getSUID()).set("sentenceInformation", sentenceInformation);
			myNet.getDefaultEdgeTable().getRow(edge1.getSUID()).set("sentence_text", sentence_content);
			myNet.getDefaultEdgeTable().getRow(edge1.getSUID()).set("sentence_number",sentence_ID.size());
		}
		
		netMgr.addNetwork(myNet);
		
		// Set the variable destroyNetwork to true, the following code will destroy a network
		final boolean destroyNetwork = false;
		if (destroyNetwork){
			// Destroy it
			 netMgr.destroyNetwork(myNet);			
		}
	}
}
