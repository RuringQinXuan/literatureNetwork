package com.cytoscape.CytoscapeLiteratureNetwork.internal.task;

import com.cytoscape.CytoscapeLiteratureNetwork.internal.io.HttpUtils;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.object.PubmedEntity;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.object.PubmedMetadata;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.cytoscape.model.CyIdentifiable;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableFactory;
import org.cytoscape.model.CyTableManager;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.ObservableTask;
import org.cytoscape.work.TaskMonitor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SearchEntityTask extends AbstractTask implements ObservableTask {
	private List<String> ids;
	private List<PubmedEntity> etmds;
	private int limit;
	private String type2;
	private CyServiceRegistrar serviceRegistrar;
	public SearchEntityTask(CyServiceRegistrar serviceRegistrar,String type2,int limit,List<String> ids){
		this.serviceRegistrar = serviceRegistrar;
		this.ids=ids;
		this.type2=type2;
		this.limit=limit;
		//this.limit = limit;
		// TODO： Test
		this.etmds=new ArrayList<>();
		System.out.println(this.limit);
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
		args.put("limit",  Integer.toString(limit));
		args.put("type2", type2);
		monitor.setTitle("Querying STRING");
		monitor.setStatusMessage("Querying String Entities");
		JSONObject object = HttpUtils.postJSON("https://api.jensenlab.org/Textmining",
				args);
		JSONArray result = (JSONArray) object.get("result");
		//System.out.println(result);
		if (result == null) {
			monitor.showMessage(TaskMonitor.Level.ERROR,"String returned no results");
			// System.out.println("object wrong type: "+object.toString());
			return;
		}
		Map<String, Map> maps = (Map<String, Map>) result.get(0);
		for(Object id : maps.keySet()) {
			etmds.add(new PubmedEntity(id,maps.get(id).get("name"),maps.get(id).get("foreground"),maps.get(id).get("background"),maps.get(id).get("score"),type2));
		}
		
		CyTableFactory ctf=serviceRegistrar.getService(CyTableFactory.class);
		CyTableManager ctm=serviceRegistrar.getService(CyTableManager.class);

		CyTable tb_etmd=null;
		for (CyTable table : ctm.getAllTables(true)) {
	        if (table.getTitle().equals("EntityMetaData")) {
	        	tb_etmd=table;
	        }
	    }
		
		if(tb_etmd == null) {
			tb_etmd = ctf.createTable("EntityMetaData", "entityID", String.class, false, true); //TODO : change public to false
			ctm.addTable(tb_etmd);
		}
		
		if(tb_etmd.getColumn("LiteratureNetwork", "foreground") == null) {
			tb_etmd.createColumn("LiteratureNetwork", "foreground", Integer.class, false);
		}
		if(tb_etmd.getColumn("LiteratureNetwork", "background") == null) {
			tb_etmd.createColumn("LiteratureNetwork", "background", Integer.class, false);
		}
		if(tb_etmd.getColumn("LiteratureNetwork", "score") == null) {
			tb_etmd.createColumn("LiteratureNetwork", "score", Double.class, false);
		}
		//get etmd content
		for(PubmedEntity etmd:etmds)
		{
			CyRow raw_etmd=tb_etmd.getRow(etmd.getType()+"."+etmd.getEntityID());
			raw_etmd.set("LiteratureNetwork", "foreground", etmd.getForeground());
			raw_etmd.set("LiteratureNetwork", "background", etmd.getBackground());
			raw_etmd.set("LiteratureNetwork", "score", etmd.getScore());
		}
	}
	@Override
	public <R> R getResults(Class<? extends R> arg0) {
		// TODO Auto-generated method stub
		return (R) etmds;
	}
}