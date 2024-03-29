package com.cytoscape.CytoscapeLiteratureNetwork.internal.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;

import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.work.FinishStatus;
import org.cytoscape.work.ObservableTask;
import org.cytoscape.work.TaskManager;
import org.cytoscape.work.TaskObserver;
import org.json.simple.JSONObject;

import com.cytoscape.CytoscapeLiteratureNetwork.internal.object.PubmedEntity;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.object.PubmedMetadata;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.object.Species;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.task.BuildNetworkFactory;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.task.SearchEntityFactory;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.task.SearchPubmedIDFactory;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.task.SearchPubmedMetadataFactory;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.task.ShowNetworkFactory;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.ui.ProteinNumberPanel;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.ui.SpeciesPanel;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.view.Interface;

public class InterfaceEntity extends JFrame implements ActionListener, TaskObserver,ItemListener{

	JButton jb_entity_initial,jb_entity_run,jb_entity_back,jb_entity_import=null;
	JLabel jl_file_dir;
	JPanel jp_search_button,jp_result_button,jp_species_panel;
	JComboBox<?> jcb,speciesJcb,speciesJcb2,jcb1;
	JTextArea jta_entity_list,jtaDisplayEntity;
	JScrollPane jscrollp_entity_list;
	ProteinNumberPanel jp_Entity_Number;
	private JSONObject result;
	private List<String> pubmed_ids;
	private List<Long> pmids_network;
	private String type2;
	private int limit;	
	private CyServiceRegistrar serviceRegistrar;
	private String query_word;
	private List<PubmedMetadata> pmmds;
	List<String> entity_ids;
	public InterfaceEntity(CyServiceRegistrar serviceRegistrar, List<String> ids,String query_word)
	{
		this.serviceRegistrar=serviceRegistrar;
		this.pubmed_ids=ids;
		this.pmmds=pmmds;
		this.query_word=query_word;
		//entity label
		JLabel jl_entity = new JLabel("select entity source");
		JPanel jp_entity_main_label=new JPanel();
		jp_entity_main_label.setLayout(new BorderLayout(5,5));
		jp_entity_main_label.add(jl_entity,BorderLayout.CENTER);
		
		
//query part
	//query label
		JLabel jl_entity_query = new JLabel("A:Query protein from StringApp");
		//panel
		JPanel jp_entity_query_label = new JPanel();
		jp_entity_query_label.setLayout(new BorderLayout(5,5));
		jp_entity_query_label.add(jl_entity_query,BorderLayout.NORTH);
	//query option
		//species panel
		jp_species_panel=new SpeciesPanel();
		//protein number panel
		jp_Entity_Number = new ProteinNumberPanel();	
		JPanel jp_entity_query_options = new JPanel();
		jp_entity_query_options.setLayout(new GridLayout(2,1));
		//jp_entity_query_options.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		jp_entity_query_options.add(jp_species_panel);
		jp_entity_query_options.add(jp_Entity_Number);
	//query button panel
		//button
		jb_entity_initial= new JButton("initial");
		jb_entity_run= new JButton("run");
		jb_entity_initial.addActionListener(this);
		jb_entity_initial.setActionCommand("initial");
		jb_entity_run.addActionListener(this);
		jb_entity_run.setActionCommand("run");
		//panel
		JPanel jp_entity_query_button = new JPanel();
		jp_entity_query_button.add(jb_entity_initial);
		jp_entity_query_button.add(jb_entity_run);			
	//query panel
		JPanel jp_entity_query = new JPanel();
		jp_entity_query.setLayout(new BorderLayout(5,5));
		jp_entity_query.add(jp_entity_query_label,BorderLayout.NORTH);
		jp_entity_query.add(jp_entity_query_options,BorderLayout.CENTER);
		jp_entity_query.add(jp_entity_query_button,BorderLayout.SOUTH);		
		
//entity list
	//entity list label
		JLabel jl_entity_list = new JLabel("B:Entity List");
		JLabel jl_entity_exp = new JLabel("exmaple:9606.ENSP00000216807");	
		JLabel jl_or = new JLabel("Or"); 
		JPanel jp_entity_label = new JPanel();
		jp_entity_label.setLayout(new BorderLayout(5,5));
		jp_entity_label.add(jl_entity_list,BorderLayout.NORTH);
		jp_entity_label.add(jl_entity_exp,BorderLayout.CENTER);
		jp_entity_label.add(jl_or,BorderLayout.SOUTH);
	//entity list jtextarea
		jta_entity_list=new JTextArea(50,60);
		jta_entity_list.setLineWrap(true);
		jta_entity_list.setWrapStyleWord(true);
		jscrollp_entity_list=new JScrollPane(jta_entity_list);
	//entity list button panel
		JButton jb_entity_clear = new JButton("clear");
		jb_entity_clear.addActionListener(this);
		jb_entity_clear.setActionCommand("clear");
		JPanel jp_entity_list_button = new JPanel();
		jp_entity_list_button.setLayout(new BorderLayout(5,5));
		jp_entity_list_button.add(jb_entity_clear,BorderLayout.NORTH);
	//entity list panel
		JPanel jp_entity_list = new JPanel();
		jp_entity_list.setLayout(new BorderLayout(5,5));
		jp_entity_list.add(jp_entity_label,BorderLayout.WEST);
		jp_entity_list.add(jscrollp_entity_list,BorderLayout.CENTER);
		jp_entity_list.add(jp_entity_list_button,BorderLayout.EAST);
		
//file 
	//file label
		JLabel jl_entity_file = new JLabel("C:Choose From a File");		
	//file button
		JButton jb_entity_browse = new JButton("Browse");
		jb_entity_browse.addActionListener(this);
		jb_entity_browse.setActionCommand("browse");		
	//file dir label
		jl_file_dir = new JLabel("");		
	//file panel
		JPanel jp_entity_file = new JPanel();
		jp_entity_file.setLayout(new BorderLayout(5,5));
		jp_entity_file.add(jl_entity_file,BorderLayout.WEST);
		jp_entity_file.add(jb_entity_browse,BorderLayout.CENTER);
		jp_entity_file.add(jl_file_dir,BorderLayout.EAST);
		
// entity result button panel
		jb_entity_back=new JButton("back");
		jb_entity_import=new JButton("import");
		jb_entity_back.addActionListener(this);
		jb_entity_back.setActionCommand("back");
		jb_entity_import.addActionListener(this);
		jb_entity_import.setActionCommand("import");
		JPanel jp_entity_result_button = new JPanel();		
		jp_entity_result_button.setLayout(new BorderLayout(5,5));
		jp_entity_result_button.add(jb_entity_back,BorderLayout.WEST);
		jp_entity_result_button.add(jb_entity_import,BorderLayout.EAST);
		
	//entity panel	
		JPanel jp_entity = new JPanel();
		jp_entity.setLayout(new BorderLayout(5,5));
		jp_entity.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		jp_entity.add(jp_entity_query,BorderLayout.NORTH);
		jp_entity.add(jp_entity_list,BorderLayout.CENTER);
		jp_entity.add(jp_entity_file,BorderLayout.SOUTH);

	//main panel
		JPanel jp_entity_main= new JPanel();
		jp_entity_main.setLayout(new BorderLayout(5,5));
		jp_entity_main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		jp_entity_main.add(jp_entity_main_label,BorderLayout.NORTH);
		jp_entity_main.add(jp_entity,BorderLayout.CENTER);
		jp_entity_main.add(jp_entity_result_button,BorderLayout.SOUTH);

		this.add(jp_entity_main);
		this.setSize(800,600);
		this.setTitle("Literature Netwrok");
		//this.setIconImage((new ImageIcon("images/images.jpeg")).getImage());
		this.setLocation(100, 100);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}



	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("initial"))
		{
			jp_Entity_Number.Initial_entity_number();
		}
		else if(e.getActionCommand().equals("run"))
		{
			
			type2=SpeciesPanel.getSpeciesID();
			//			type2=jp_species_panel.GetSpeices();
			limit=Integer.parseInt(jp_Entity_Number.Get_entity_number());
			SearchEntityFactory factory=new  SearchEntityFactory(this.serviceRegistrar,type2,limit,pubmed_ids);
			TaskManager<?,?> taskManager = this.serviceRegistrar.getService(TaskManager.class);
			taskManager.execute(factory.createTaskIterator(), this);

		}
		else if(e.getActionCommand().equals("import"))
		{
			if(jta_entity_list.getText()!=null){
				System.out.println("Start network");
				entity_ids=  Arrays.asList(jta_entity_list.getText().split("\\s+"));
				BuildNetworkFactory factory=new BuildNetworkFactory(pubmed_ids,entity_ids);
				TaskManager<?,?> taskManager = this.serviceRegistrar.getService(TaskManager.class);
				taskManager.execute(factory.createTaskIterator(), this);
				setVisible(false); //you can't see me!
				dispose();
			}
			else{
				System.out.println("Select entity first");
			}
		}
		else if(e.getActionCommand().equals("back"))
		{
			Interface Interface =  new Interface(serviceRegistrar);
			Interface.setVisible(true);
			setVisible(false); //you can't see me!
			dispose();
		}
		else if(e.getActionCommand().equals("browse"))
		{
			JFileChooser fileDlg = new JFileChooser();
			fileDlg.showOpenDialog(this);
			String filename = fileDlg.getSelectedFile().getAbsolutePath();
			jl_file_dir.setText(filename);

			FileInputStream fis;
			try {
				fis = new FileInputStream(filename);
				byte buffer[] = new byte[fis.available()];
				fis.read(buffer);
				String message = new String(buffer);
				jta_entity_list.setText(message);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}  
		}
		else if(e.getActionCommand().equals("clear"))
		{
			jta_entity_list.setText("");
		}
	}


	@Override
	public void allFinished(FinishStatus arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void taskFinished(ObservableTask arg0) {
		//todo
		if(arg0.getClass().getSimpleName().equals("SearchEntityTask")) {
			System.out.println("Start");
			String text = "";
			for(PubmedEntity et : (List<PubmedEntity>) arg0.getResults(List.class)) {
				text += et.getType()+"."+et.getEntityID() + "\n";	
			}
			jta_entity_list.setText(text);
		} else if(arg0.getClass().getSimpleName().equals("BuildNetworkTask")) {
			
			 result = (JSONObject) arg0.getResults(List.class);
			 pmids_network = (List<Long>) result.get("pmids");
			SearchPubmedMetadataFactory factory_pmmd=new SearchPubmedMetadataFactory(pmids_network);
			TaskManager<?,?> taskManager = this.serviceRegistrar.getService(TaskManager.class);
			taskManager.execute(factory_pmmd.createTaskIterator(), this);
			
		}else if(arg0.getClass().getSimpleName().equals("SearchPubmedMetadataTask")) {
			
			pmmds = (List<PubmedMetadata>) arg0.getResults(List.class);

			ShowNetworkFactory factory=new ShowNetworkFactory(this.serviceRegistrar, this.serviceRegistrar.getService(CyNetworkManager.class),
					this.serviceRegistrar.getService(CyNetworkNaming.class),
					this.serviceRegistrar.getService(CyNetworkFactory.class),
					result,
					 pmmds,pubmed_ids,entity_ids,query_word);
			
			TaskManager<?,?> taskManager = this.serviceRegistrar.getService(TaskManager.class);
			taskManager.execute(factory.createTaskIterator(), this);
		} 
	}



	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
	}
}





