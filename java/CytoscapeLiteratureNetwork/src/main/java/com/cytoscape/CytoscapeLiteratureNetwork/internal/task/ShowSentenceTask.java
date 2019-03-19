package com.cytoscape.CytoscapeLiteratureNetwork.internal.task;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyRow;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class ShowSentenceTask extends AbstractTask {
	private CyNetwork network;
	private CyRow edgeRow;

	public ShowSentenceTask(CyNetwork network) {
		this(network, null);
	}
	public ShowSentenceTask(CyNetwork network, CyEdge edge) {
		super();
		this.network = network;
		this.edgeRow=this.network.getDefaultEdgeTable().getRow(edge.getSUID());
	}

	@Override
	public void run(TaskMonitor arg0) throws Exception {
		
		if(this.edgeRow == null) {
			//TODO: Show error message
			return;
		}
		
		
		JFrame jf = new JFrame();
		JTextArea jta = new JTextArea(400,300);
		jta.setText(edgeRow.get("sentence_text", String.class));
		JScrollPane jscrollp=new JScrollPane(jta);
		jf.setLayout(new BorderLayout());
		jf.add(jscrollp,BorderLayout.CENTER);
		jf.setSize(400,300);
		jf.setVisible(true);
	}

}
