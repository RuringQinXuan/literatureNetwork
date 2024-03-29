package com.cytoscape.CytoscapeLiteratureNetwork.internal.task;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelComponent2;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.application.swing.CytoPanelState;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyIdentifiable;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableFactory;
import org.cytoscape.model.CyTableManager;
import org.cytoscape.model.events.RowsSetListener;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import org.json.simple.JSONObject;

import com.cytoscape.CytoscapeLiteratureNetwork.internal.object.PubmedMetadata;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.view.LiteratureNetworkCytoPanel;

public class ShowNetworkTask extends AbstractTask {
	
	private CyServiceRegistrar serviceRegistrar;

	private final CyNetworkManager netMgr;
	private final CyNetworkFactory cnf;
	private final CyNetworkNaming namingUtil; 
	private JSONObject result;
	private List<PubmedMetadata> pmmds;
	private List<String> entity_ids;
	private String query_word;
	private List<String> pubmed_ids;
	public ShowNetworkTask(CyServiceRegistrar serviceRegistrar, 
			final CyNetworkManager netMgr, final CyNetworkNaming namingUtil,
			final CyNetworkFactory cnf,JSONObject result,List<PubmedMetadata> pmmds,
			List<String> pubmed_ids,List<String> entity_ids,String query_word){
		this.serviceRegistrar = serviceRegistrar;
		this.pmmds=pmmds;
		this.netMgr = netMgr;
		this.cnf = cnf;
		this.namingUtil = namingUtil;
		this.pubmed_ids=pubmed_ids;
		this.entity_ids=entity_ids;
		this.result=result;
		this.query_word=query_word;
		
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
		
	//pmmd table
		CyTableFactory ctf=serviceRegistrar.getService(CyTableFactory.class);
		CyTableManager ctm=serviceRegistrar.getService(CyTableManager.class);
		CyTable tb_pmmd=null;
		
		for (CyTable table : ctm.getAllTables(true)) {
	        if (table.getTitle().equals("PubmedMetaData")) {
	        	tb_pmmd=table;
	        }
	    }
		
		if(tb_pmmd == null) {
			tb_pmmd = ctf.createTable("PubmedMetaData", "pmid", String.class, false, true);
			ctm.addTable(tb_pmmd);
		}

		if(tb_pmmd.getColumn("title") == null) {
			tb_pmmd.createColumn("title", String.class, false);
		}
		if(tb_pmmd.getColumn("publicdate") == null) {
			tb_pmmd.createColumn("publicdate", String.class, false);
		}
		if(tb_pmmd.getColumn("journal") == null) {
			tb_pmmd.createColumn("journal", String.class, false);
		}
		if(tb_pmmd.getColumn("authors") == null) {
			tb_pmmd.createColumn("authors", String.class, false);
		}
		//get pmmd content
		for(PubmedMetadata pmmd:pmmds)
		{
			CyRow raw_pmmd=tb_pmmd.getRow(pmmd.getId());
			raw_pmmd.set("title", pmmd.getTitle());
			raw_pmmd.set("publicdate", pmmd.getPublicdate());
			raw_pmmd.set("journal", pmmd.getJournal());
			raw_pmmd.set("authors", pmmd.getAuthors());
		}
		
	// nodes table
		myNet.getDefaultNodeTable().createColumn("type_id", String.class, false);
		myNet.getDefaultNodeTable().createColumn("id", String.class, false);
		
		//merge string entity information
		CyTable tb_etmd=null;
		for (CyTable table : ctm.getAllTables(true)) {
	        if (table.getTitle().equals("EntityMetaData")) {
	        	tb_etmd=table;
	        }
	    }	
		
		for (Entry<String, JSONObject> entry :nodes.entrySet()) {

			final CyNode node1 = myNet.addNode();
			myNet.getDefaultNodeTable().getRow(node1.getSUID()).set("name", entry.getValue().get("name"));
			myNet.getDefaultNodeTable().getRow(node1.getSUID()).set("id", entry.getValue().get("id"));
			myNet.getDefaultNodeTable().getRow(node1.getSUID()).set("type_id", entry.getValue().get("type_id"));	
			networkNodes.put((String)entry.getValue().get("type_id"), node1);
			
		}
		if (tb_etmd != null){
			myNet.getDefaultNodeTable().addVirtualColumns(tb_etmd,"type_id",false);
		}
		
	//set edges
		myNet.getDefaultEdgeTable().createListColumn("sentence_text", String.class, false);
		myNet.getDefaultEdgeTable().createListColumn("sentence_id", String.class, false);
		myNet.getDefaultEdgeTable().createListColumn("sentenceInformation", String.class, false);
		myNet.getDefaultEdgeTable().createColumn("sentence_number", Integer.class, false);
		
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
		
		
		
	//network log table
				List<Long> pmids_network = (List<Long>) result.get("pmids");

				LocalTime time_network =   ZonedDateTime.now().toLocalTime().truncatedTo(ChronoUnit.SECONDS);
				
				CyTable netTable = myNet.getDefaultNetworkTable();
				System.out.println(netTable);

				if(netTable.getColumn("pubmed query") == null) {
					netTable.createColumn("pubmed query", String.class, false);
				} 
				if(netTable.getColumn("PMID selected") == null) {
					netTable.createListColumn("PMID selected", String.class, false);
				}
				if(netTable.getColumn("EntityID selected") == null) {
					netTable.createListColumn("EntityID selected", String.class, false);
				}
				if(netTable.getColumn("PMID present network") == null) {
					netTable.createListColumn("PMID present network", String.class, false);
				}
				if(netTable.getColumn("Network build time") == null) {
					netTable.createColumn("Network build time", String.class, false);
				}
				

				netTable.getRow(myNet.getSUID()).set("pubmed query", query_word);
				netTable.getRow(myNet.getSUID()).set("PMID selected", pubmed_ids);
				netTable.getRow(myNet.getSUID()).set("EntityID selected", entity_ids);
				netTable.getRow(myNet.getSUID()).set("PMID present network", pmids_network);
				netTable.getRow(myNet.getSUID()).set("Network build time", time_network);
				
		netMgr.addNetwork(myNet);
		
		// We register the Panel
		CySwingApplication swingApplication = this.serviceRegistrar.getService(CySwingApplication.class);
		CytoPanel cytoPanel = swingApplication.getCytoPanel(CytoPanelName.EAST);

		// If the panel is not already registered, create it
		if (cytoPanel.indexOfComponent(LiteratureNetworkCytoPanel.IDENTIFIER) < 0) {
			CytoPanelComponent2 panel = new LiteratureNetworkCytoPanel(serviceRegistrar);

			// Register it
			serviceRegistrar.registerService(panel, CytoPanelComponent.class, new Properties());
			serviceRegistrar.registerService(panel, RowsSetListener.class, new Properties());

			if (cytoPanel.getState() == CytoPanelState.HIDE)
				cytoPanel.setState(CytoPanelState.DOCK);
		}
	}
}
