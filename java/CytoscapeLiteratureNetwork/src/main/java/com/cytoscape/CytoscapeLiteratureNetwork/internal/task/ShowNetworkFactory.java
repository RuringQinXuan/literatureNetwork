package com.cytoscape.CytoscapeLiteratureNetwork.internal.task;

import java.util.List;

import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;
import org.json.simple.JSONObject;

import com.cytoscape.CytoscapeLiteratureNetwork.internal.object.PubmedMetadata;

public class ShowNetworkFactory extends AbstractTaskFactory {

	private CyServiceRegistrar serviceRegistrar;
	private final CyNetworkManager netMgr;
	private final CyNetworkFactory cnf;
	private final CyNetworkNaming namingUtil; 
	private JSONObject result;
	private List<String> entity_ids;
	private List<String> pubmed_ids;
	private List<PubmedMetadata> pmmds;
	private String query_word;
	public ShowNetworkFactory(CyServiceRegistrar serviceRegistrar,
			final CyNetworkManager netMgr, final CyNetworkNaming namingUtil, 
			final CyNetworkFactory cnf,JSONObject result,List<PubmedMetadata> pmmds,
			List<String> pubmed_ids,List<String> entity_ids,String query_word){
		this.serviceRegistrar = serviceRegistrar;
		this.netMgr = netMgr;
		this.cnf = cnf;

		this.query_word=query_word;
		this.namingUtil = namingUtil;
		this.pmmds=pmmds;
		this.pubmed_ids=pubmed_ids;
		this.entity_ids=entity_ids;
		this.result=result;
	}
	
	
	@Override
	public TaskIterator createTaskIterator() {
		// TODO Auto-generated method stub
		return new TaskIterator(new ShowNetworkTask(serviceRegistrar, 
				netMgr, namingUtil, cnf, result,pmmds,pubmed_ids,entity_ids,query_word));
	}

}
