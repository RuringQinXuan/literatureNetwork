package com.cytoscape.CytoscapeLiteratureNetwork.internal.task;

import com.cytoscape.CytoscapeLiteratureNetwork.internal.io.HttpUtils;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.object.PubmedMetadata;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.ObservableTask;
import org.cytoscape.work.TaskMonitor;
import org.json.simple.JSONObject;

public class SearchPubmedMetadataTask extends AbstractTask implements ObservableTask {

	private List<PubmedMetadata> pmmd;
	private List<String> ids;
	private int gap=100;
	private int subend;
	public SearchPubmedMetadataTask(List<String> ids){
		this.ids=ids;
		this.pmmd=new ArrayList<>();
		// TODO： Test
		//this.ids = Arrays.asList("30865931","30865919","30865925");
	}

	public void run(TaskMonitor monitor) {
		subend=gap;
		if(subend>=ids.size()){
			subend=ids.size()-1;
		}
		String query_metadata = String.join(",", ids.subList(0,subend));
		Map<String, String> args1 = new HashMap<>();
		args1.put("db", "pubmed");
		args1.put("retmode","json");
		args1.put("id",query_metadata);
		
		args1.put("tool","mytool");
		monitor.setStatusMessage("Querying PubMed Metadata");
		JSONObject object1 = HttpUtils.getJSON("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi",
				args1);

		JSONObject result1 = (JSONObject) object1.get("result");
		if (result1 == null) {
			monitor.showMessage(TaskMonitor.Level.ERROR,"Pubmed returned no results");
			// System.out.println("object wrong type: "+object.toString());
			return;
		}

		JSONObject pmid_content = (JSONObject)result1.get("result");
		for(String id : ids.subList(0,subend)) {
			pmmd.add(new PubmedMetadata((JSONObject) pmid_content.get(id)));
		}
	}


	@Override
	public <R> R getResults(Class<? extends R> arg0) {
		// TODO Auto-generated method stub
		return (R) pmmd;
	}
}


