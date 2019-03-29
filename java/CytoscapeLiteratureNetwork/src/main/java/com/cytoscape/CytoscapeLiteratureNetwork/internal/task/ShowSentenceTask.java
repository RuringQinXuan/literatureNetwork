package com.cytoscape.CytoscapeLiteratureNetwork.internal.task;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.cytoscape.command.AvailableCommands;
import org.cytoscape.command.CommandExecutorTaskFactory;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyRow;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.FinishStatus;
import org.cytoscape.work.ObservableTask;
import org.cytoscape.work.TaskManager;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.TaskObserver;

import com.cytoscape.CytoscapeLiteratureNetwork.internal.object.PubmedMetadata;

public class ShowSentenceTask extends AbstractTask implements TaskObserver{
	private CyServiceRegistrar serviceRegistrar;
	private CyNetwork network;
	private CyRow edgeRow;
	List<String>sentence_id,sentence_text;
	public ShowSentenceTask(CyServiceRegistrar serviceRegistrar, CyNetwork network) {
		this(serviceRegistrar, network, null);
	}
	public ShowSentenceTask(CyServiceRegistrar serviceRegistrar, CyNetwork network, CyEdge edge) {
		super();
		this.serviceRegistrar = serviceRegistrar;
		this.network = network;
		this.edgeRow=this.network.getDefaultEdgeTable().getRow(edge.getSUID());
	}

	@Override
	public void run(TaskMonitor arg0) throws Exception {

		if(this.edgeRow == null) {
			//TODO: Show error message
			return;
		}


		AvailableCommands availableCommands = this.serviceRegistrar.getService(AvailableCommands.class);
		if(availableCommands.getNamespaces().contains("cybrowser")) {
			sentence_id =  edgeRow.getList("sentence_id", String.class);
			sentence_text =  edgeRow.getList("sentence_text",String.class);
			String contentID="";
			List<String>pubmed_ids=new ArrayList<String>();
			for (int i = 0; i < sentence_id.size(); i++) {
				contentID=sentence_id.get(i).split("\\.")[0];
				pubmed_ids.add(contentID);
				System.out.println(contentID);
			}
			SearchPubmedMetadataFactory factory=new SearchPubmedMetadataFactory(pubmed_ids);
			TaskManager<?,?> taskManager = this.serviceRegistrar.getService(TaskManager.class);
			taskManager.execute(factory.createTaskIterator(), this);
		} 
	}
	@Override
	public void allFinished(FinishStatus arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void taskFinished(ObservableTask arg0) {
		// TODO Auto-generated method stub
		if(arg0.getClass().getSimpleName().equals("SearchPubmedMetadataTask")) {
			Map<String, Object> args = new HashMap<>();
			Map<String, PubmedMetadata> pmmds = new HashMap<>();	
			args.put("id", "CytoscapeLiteratureNetwork");
			args.put("title", "View co-occurences");
			String contentID,contentSentence,content="";
			List<PubmedMetadata> results = (List<PubmedMetadata>) arg0.getResults(List.class);	
			for (int i = 0; i < results.size(); i++) {
				pmmds.put(results.get(i).getId(), results.get(i));
				
			}

			for (int i = 0; i < sentence_id.size(); i++) {
				contentID=sentence_id.get(i).split("\\.")[0];
				contentSentence=sentence_text.get(i);
				int dis=i+1;
				content+="["+dis+"]"+"<a target='_blank' href='https://www.ncbi.nlm.nih.gov/pubmed/"+contentID+"'>"+pmmds.get(contentID).getTitle()+"</a>"
				+"<br>"+pmmds.get(contentID).getAuthors()
						+ "<br>"+pmmds.get(contentID).getJournal()+pmmds.get(contentID).getPublicdate()
								+ "<br>"+contentSentence+"<br>";
			}
			System.out.println(content);
			
			
			AvailableCommands availableCommands = this.serviceRegistrar.getService(AvailableCommands.class);
			if(availableCommands.getNamespaces().contains("cybrowser")) {
				args.put("text", "<html>"+content+" </html>");
				CommandExecutorTaskFactory commandExecutor = this.serviceRegistrar.getService(CommandExecutorTaskFactory.class);
				TaskManager<?,?> taskManager = this.serviceRegistrar.getService(TaskManager.class);
				taskManager.execute(commandExecutor.createTaskIterator("cybrowser", "show", args, null));
			}
			else {
				JFrame jf = new JFrame();
				JTextArea jta = new JTextArea(400,300);
				jta.setText(edgeRow.get("sentence_text", String.class));
				JScrollPane jscrollp=new JScrollPane(jta);
				jf.setLayout(new BorderLayout());
				jf.add(jscrollp,BorderLayout.CENTER);
				jf.setSize(400,300);
				jf.setVisible(true);
			}
			System.out.println("finished");
	}	
		
	}

}
