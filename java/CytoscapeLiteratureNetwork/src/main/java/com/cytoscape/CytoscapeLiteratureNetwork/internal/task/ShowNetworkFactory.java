package com.cytoscape.CytoscapeLiteratureNetwork.internal.task;

import java.util.List;

import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;
import org.json.simple.JSONObject;

public class ShowNetworkFactory extends AbstractTaskFactory {

	private final CyNetworkManager netMgr;
	private final CyNetworkFactory cnf;
	private final CyNetworkNaming namingUtil; 
	private JSONObject result;
	
	public ShowNetworkFactory(final CyNetworkManager netMgr, final CyNetworkNaming namingUtil, final CyNetworkFactory cnf,JSONObject result){
		this.netMgr = netMgr;
		this.cnf = cnf;
		this.namingUtil = namingUtil;
		this.result=result;
	}
	
	
	@Override
	public TaskIterator createTaskIterator() {
		// TODO Auto-generated method stub
		return new TaskIterator(new ShowNetworkTask(netMgr, namingUtil, cnf, result));
	}

}
