package com.cytoscape.CytoscapeLiteratureNetwork.internal.task;

import com.cytoscape.CytoscapeLiteratureNetwork.internal.io.HttpUtils;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.object.PubmedMetadata;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.ObservableTask;
import org.cytoscape.work.TaskMonitor;
import org.json.simple.JSONObject;


public class BuildNetworkTask extends AbstractTask implements ObservableTask{

	private List<String> documents;
	private List<String> entities;
	private JSONObject result;
	public BuildNetworkTask(List<String> documents,List<String> entities2){
		this.documents=documents;
		this.entities=entities2;
		// TODO： Test
		//this.ids = Arrays.asList("30865931","30865919","30865925");
	}
	


	@Override
	public void run(TaskMonitor monitor) throws Exception {
		// TODO Auto-generated method stub
		String query_documents = String.join("\n", documents);
				//.subList(0,300));
		String query_entities = String.join("\n", entities);
		Map<String, String> args1 = new HashMap<>();
		args1.put("documents",query_documents);
		args1.put("entities",query_entities);
		monitor.setStatusMessage("Querying LiteratureNetwork for network");
		monitor.setStatusMessage("URL: http://localhost:9000/getdata?documents=" + query_documents + "&entities=" + query_entities);
		result = (JSONObject)HttpUtils.postJSON("http://localhost:9000/getdata",
				args1).get("result");
		if (result == null) {
			monitor.showMessage(TaskMonitor.Level.ERROR,"Pubmed returned no results");
			// System.out.println("object wrong type: "+object.toString());
			return;
		}
		//System.out.println(result);
//		JSONObject edges = (JSONObject) result.get("edges");
//		ArrayList<String> nodes = (ArrayList<String>) result.get("nodes");
//		Map<String, String>  sentences  =  (Map<String, String>) result.get("sentences");
	}

	@Override
	public <R> R getResults(Class<? extends R> arg0) {

		// TODO Auto-generated method stub
		return (R) result;
	}

}
