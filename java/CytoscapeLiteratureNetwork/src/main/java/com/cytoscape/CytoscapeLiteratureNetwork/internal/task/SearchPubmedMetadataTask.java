package com.cytoscape.CytoscapeLiteratureNetwork.internal.task;

import com.cytoscape.CytoscapeLiteratureNetwork.internal.io.HttpUtils;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.object.PubmedMetadata;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.ObservableTask;
import org.cytoscape.work.TaskMonitor;
import org.json.simple.JSONObject;

public class SearchPubmedMetadataTask extends AbstractTask implements ObservableTask {

	private List<PubmedMetadata> pmmd;
	private List<Long> ids;
	
	public SearchPubmedMetadataTask(List<Long> ids){
		this.ids=ids;
		this.pmmd=new ArrayList<>();
		// TODO： Test
		//this.ids = Arrays.asList("30865931","30865919","30865925");
	}

	public void run(TaskMonitor monitor) {
		int gap=100;
		int loop=ids.size()/gap;
		int id_start,id_end;
		for(int i=0; i<=loop ;i++){
			id_start=i*gap;
			id_end=(i+1)*gap;
			if(id_end>ids.size()){
				id_end=ids.size();
			}
			List<Long> pubmed_ids = ids.subList(id_start,id_end);
			String query_metadata = pubmed_ids.stream().map(Object::toString).collect(Collectors.joining(","));
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
			for(Long id : pubmed_ids) {
				pmmd.add(new PubmedMetadata((JSONObject) pmid_content.get(id.toString())));
			}
		}
	}


	@Override
	public <R> R getResults(Class<? extends R> arg0) {
		// TODO Auto-generated method stub
		return (R) pmmd;
	}
}


