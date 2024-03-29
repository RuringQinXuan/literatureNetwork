package com.cytoscape.CytoscapeLiteratureNetwork.internal.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CytoPanelComponent2;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableManager;
import org.cytoscape.model.events.RowSetRecord;
import org.cytoscape.model.events.RowsSetEvent;
import org.cytoscape.model.events.RowsSetListener;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.util.swing.OpenBrowser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LiteratureNetworkCytoPanel extends JPanel implements CytoPanelComponent2, RowsSetListener, HyperlinkListener {
	private static final long serialVersionUID = -1721743083367408559L;
	
	public static final String IDENTIFIER = "LiteratureNetwork";
	
	private CyServiceRegistrar serviceRegistrar;
	
	private OpenBrowser openBrowser;
	private JEditorPane editorPane;
	
	public LiteratureNetworkCytoPanel(CyServiceRegistrar serviceRegistrar) {
		this.serviceRegistrar = serviceRegistrar;
		
		this.openBrowser = this.serviceRegistrar.getService(OpenBrowser.class);
		
		this.editorPane = new JEditorPane("text/html", "");
		this.editorPane.addHyperlinkListener(this);
		this.editorPane.setBackground(getBackground());
		this.editorPane.setEditable(false);
		
		this.setLayout(new BorderLayout());
		this.add(this.editorPane, BorderLayout.CENTER);
	}
	
	public void setHTML(String html) {
		if(html == null || html.isEmpty()) {
			this.editorPane.setText("");
			return;
		}
		
		if(!html.startsWith("<html>")) {
			html = "<html>" + html;
		}
		if(!html.endsWith("</html>")) {
			html += "</html>";
		}
		
		this.editorPane.setText(html);
	}

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public CytoPanelName getCytoPanelName() {
		return CytoPanelName.EAST;
	}

	@Override
	public String getTitle() {
		return "Literature Network";
	}

	@Override
	public Icon getIcon() {
		return null;
	}

	@Override
	public String getIdentifier() {
		return LiteratureNetworkCytoPanel.IDENTIFIER;
	}

	@Override
	public void handleEvent(RowsSetEvent e) {
		if(e.containsColumn(CyNetwork.SELECTED)) {
			this.setHTML(null);
			
			CyNetwork currentNetwork = this.serviceRegistrar.getService(CyApplicationManager.class).getCurrentNetwork();
			if(currentNetwork == null) {
				return;
			}
			
			for(RowSetRecord rse : e.getPayloadCollection()) {
				if(!rse.getColumn().equals(CyNetwork.SELECTED)) {
					continue;
				}
				
				if(!((Boolean)rse.getValue())) {
					continue;
				}
				
				CyEdge edge = currentNetwork.getEdge(rse.getRow().get(CyNetwork.SUID, Long.class));
				if(edge == null) {
					continue;
				}
				
				CyRow edgeRow = currentNetwork.getRow(edge);
				
				CyTableManager tableManager = this.serviceRegistrar.getService(CyTableManager.class);
				CyTable pubMedMetadata=null;
				for(CyTable t : tableManager.getAllTables(true)) {
					if(t.getTitle().equals("PubmedMetaData")){
						pubMedMetadata=t;
					}
				}
				
				List<String> sentence_id =  edgeRow.getList("sentence_id", String.class);
				List<String> sentence_text =  edgeRow.getList("sentence_text",String.class);
				List<String> sentenceInformation =  edgeRow.getList("sentenceInformation",String.class);

				String content="";
				for (int i = 0; i < sentence_id.size(); i++) {
					String contentID=sentence_id.get(i).split("\\.")[0];
					String contentSentence=sentence_text.get(i); 
					JSONObject sentenceInformationObejct;
					try {
						sentenceInformationObejct = (JSONObject) new JSONParser().parse(sentenceInformation.get(i));
						JSONArray entityLocation1List = (JSONArray) sentenceInformationObejct.get("entityLocation1");
						JSONArray entityLocation2List = (JSONArray) sentenceInformationObejct.get("entityLocation2");
						String sentenceHtml=contentSentence;
						for (int j = 0; j < entityLocation1List.size(); j++) {

							String entity1=contentSentence.substring(Integer.parseInt(((JSONArray)entityLocation1List.get(j)).get(0).toString()), Integer.parseInt(((JSONArray)entityLocation1List.get(j)).get(1).toString()));
							
							sentenceHtml=sentenceHtml.replace(entity1,"<span style='background-color:yellow;'>"+entity1+"</span>");
						}
						for (int j = 0; j < entityLocation2List.size(); j++) {
							String entity2=contentSentence.substring(Integer.parseInt(((JSONArray)entityLocation2List.get(j)).get(0).toString()), Integer.parseInt(((JSONArray)entityLocation2List.get(j)).get(1).toString()));
							sentenceHtml=sentenceHtml.replace(entity2,"<span style='background-color:red;'>"+entity2+"</span>");
							
						}

						int dis=i+1;
						CyRow pmmd = pubMedMetadata.getRow(contentID);
						if(pmmd != null) {
							String content_pmid="<br>PMID:"+contentID+"<br>";
							if (content.contains(content_pmid)){
								content+="<li>"+sentenceHtml+"</li>";
							}else{
								if(content!=""){
									content += "</ul>";
								}
							content+="["+dis+"]"+"<a target='_blank' href='https://www.ncbi.nlm.nih.gov/pubmed/"+contentID+"'>"+pmmd.get("title",String.class)+"</a>"
									+"<br>"+pmmd.get("authors", String.class)
									+ "<br>"+pmmd.get("journal",String.class)+" "+pmmd.get("publicdate",String.class)
									+content_pmid+ "<ul><li>"+sentenceHtml+"</li>";
								
							}
						}
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						continue;
					} 

					//<font color="red">This is some text!</font> 
					
				}
				
				content += "</ul>";
				
				String html = "<html>";
				html += content;
				html += "</html>";
				
				this.setHTML(html);
			}
		}
		
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			this.openBrowser.openURL(e.getURL().toString());
		}
	}

}
