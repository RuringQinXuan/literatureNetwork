package com.cytoscape.CytoscapeLiteratureNetwork.internal.view;

import java.awt.BorderLayout;
import java.awt.Component;

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
import org.cytoscape.model.events.RowSetRecord;
import org.cytoscape.model.events.RowsSetEvent;
import org.cytoscape.model.events.RowsSetListener;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.util.swing.OpenBrowser;

public class LiteratureNetworkCytoPanel extends JPanel implements CytoPanelComponent2, RowsSetListener, HyperlinkListener {
	private static final long serialVersionUID = -1721743083367408559L;
	
	public static final String IDENTIFIER = "LiteratureNetwork";
	
	private CyServiceRegistrar serviceRegistrar;
	
	private OpenBrowser openBrowser;
	private JEditorPane editorPane;
	
	public LiteratureNetworkCytoPanel(CyServiceRegistrar serviceRegistrar) {
		this.serviceRegistrar = serviceRegistrar;
		
		this.openBrowser = this.serviceRegistrar.getService(OpenBrowser.class);
		
		this.editorPane = new JEditorPane();
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
				
				CyEdge edge = currentNetwork.getEdge(rse.getRow().get(CyNetwork.SUID, Long.class));
				if(edge == null) {
					continue;
				}
				
				String html = "<html>";
				html += edge.toString();
				html += "<br>";
				html += "<a href='http://jensenlab.org'>test</a>";
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
