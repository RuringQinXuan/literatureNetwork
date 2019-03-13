package com.cytoscape.CytoscapeLiteratureNetwork.internal.task;

import com.cytoscape.CytoscapeLiteratureNetwork.internal.io.HttpUtils;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.object.PubmedMetadata;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.ObservableTask;
import org.cytoscape.work.TaskMonitor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SearchPubmedMetadataTask extends AbstractTask implements ObservableTask {
	
	private List<PubmedMetadata> pmmd;
	private List<String> ids;

	public SearchPubmedMetadataTask(List<String> ids){
		this.ids=ids;
	}
	
	public void run(TaskMonitor monitor) {
	String query_metadata = String.join(",", ids);
	Map<String, String> args1 = new HashMap<>();
	args1.put("db", "pubmed");
	args1.put("retmode","json");
	args1.put("id",query_metadata);
	monitor.setStatusMessage("Querying PubMed IDs");
	JSONObject object1 = HttpUtils.getJSON("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi",
			args1);
	JSONObject result1 = (JSONObject) object1.get("result");
	if (result1 == null) {
		monitor.showMessage(TaskMonitor.Level.ERROR,"Pubmed returned no results");
		// System.out.println("object wrong type: "+object.toString());
		return;
	}
	
	JSONArray pmid_content = (JSONArray)result1.get("result");
	for(Object metadata : pmid_content) {
		if(metadata instanceof JSONArray) {
			continue;
		}
		pmmd.add(new PubmedMetadata((JSONObject) metadata));
	}
	
	
	}

	@Override
	public <R> R getResults(Class<? extends R> arg0) {
		// TODO Auto-generated method stub
		return (R) pmmd;
	}
}

