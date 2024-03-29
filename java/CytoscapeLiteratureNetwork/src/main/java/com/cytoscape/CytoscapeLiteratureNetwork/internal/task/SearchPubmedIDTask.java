package com.cytoscape.CytoscapeLiteratureNetwork.internal.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.ObservableTask;
import org.cytoscape.work.TaskMonitor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.cytoscape.CytoscapeLiteratureNetwork.internal.io.HttpUtils;

public class SearchPubmedIDTask extends AbstractTask implements ObservableTask{

	private String query;
	private List<String> ids;

	public SearchPubmedIDTask(String query){
		this.query=query;
	}

	@Override
	public void run(TaskMonitor monitor) {
		monitor.setTitle("Querying PubMed");
		Map<String, String> args = new HashMap<>();
		args.put("db", "pubmed");
		args.put("retmode","json");
		args.put("retmax","40000");
		args.put("term",query);
		monitor.setStatusMessage("Querying PubMed IDs");
		monitor.setStatusMessage("query="+query);
		JSONObject object = HttpUtils.getJSON("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi",
				args);
		JSONObject result = (JSONObject) object.get("result");
		if (result == null) {
			monitor.showMessage(TaskMonitor.Level.ERROR,"Pubmed returned no results");
			// System.out.println("object wrong type: "+object.toString());
			return;
		}
		JSONObject json = (JSONObject)result.get("esearchresult");
		if (json == null) {
			monitor.showMessage(TaskMonitor.Level.ERROR,"Pubmed returned no results");
			// System.out.println("object doesn't contain esearchresult: "+object.toString());
			return;
		}

		// Get the total number of results
		int count = Integer.parseInt((String)json.get("count"));
		if (count == 0) {
			monitor.showMessage(TaskMonitor.Level.ERROR,"Pubmed returned no results");
			// System.out.println("object doesn't contain count: "+json.toString());
			return;
		}

		JSONArray json_ids = (JSONArray)json.get("idlist");
		ids = new ArrayList<>();
		for(Object id : json_ids) {
			ids.add(id.toString());
		}
		
	}

	@Override
	public <R> R getResults(Class<? extends R> arg0) {
		// TODO Auto-generated method stub
		return (R) ids;
	}
}
