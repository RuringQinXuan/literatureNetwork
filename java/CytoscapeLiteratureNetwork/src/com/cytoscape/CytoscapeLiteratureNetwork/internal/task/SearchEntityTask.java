package com.cytoscape.CytoscapeLiteratureNetwork.internal.task;

import com.cytoscape.CytoscapeLiteratureNetwork.internal.io.HttpUtils;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.object.PubmedEntity;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.object.PubmedMetadata;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.ObservableTask;
import org.cytoscape.work.TaskMonitor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SearchEntityTask extends AbstractTask implements ObservableTask {
	private List<String> ids;
	private List<PubmedEntity> pmet;
	final int limit=100;
	private String type2;
	public SearchEntityTask(String type2,List<String> ids){
		this.ids=ids;
		this.type2=type2;
		//this.limit = limit;
		// TODO： Test
		this.pmet=new ArrayList<>();
	}
	@Override
	public void run(TaskMonitor monitor) throws Exception {
		// TODO Auto-generated method stub
		
		monitor.showMessage(TaskMonitor.Level.INFO,"Pubmed returned "+ids.size()+" results, of which we downloaded "+ids.size());
		
		Map<String, String> args = new HashMap<>();
		StringBuilder sb = new StringBuilder();
		for (Object id: ids) {
			sb.append(id.toString()+" ");
		}
		args.put("documents", sb.toString());
		args.put("format", "json");
		args.put("limit", Integer.toString(limit));
		args.put("type2", type2);
		monitor.setTitle("Querying STRING");
		monitor.setStatusMessage("Querying PubMed Entities");
		JSONObject object = HttpUtils.postJSON("https://api11.jensenlab.org/Textmining",
				args);
		
		JSONArray result = (JSONArray) object.get("result");
		if (result == null) {
			monitor.showMessage(TaskMonitor.Level.ERROR,"String returned no results");
			// System.out.println("object wrong type: "+object.toString());
			return;
		}
		Map<String, Map> maps = (Map<String, Map>) result.get(0);
		 
		for(Object id : maps.keySet()) {
			  
			pmet.add(new PubmedEntity(id,maps.get(id).get("name"),type2));
		}
	}
	@Override
	public <R> R getResults(Class<? extends R> arg0) {
		// TODO Auto-generated method stub
		return (R) pmet;
	}
}