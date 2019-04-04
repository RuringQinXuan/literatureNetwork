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
import com.cytoscape.CytoscapeLiteratureNetwork.internal.ui.DefineEntityNumberPanel;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.ui.DefineSpeciesIDPanel;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.view.Interface;

public class InterfaceEntity extends JFrame implements ActionListener, TaskObserver,ItemListener{

	JButton jb_initial,jb2,jb_run,jb_back,jb_import=null;
	JLabel jl_protein_number,jl_entity_result=null,jtfFileDir;
	JPanel jpResult,jpQuery,jp_Entity_Number,jp_search_button,jp_result_button=null,jpi,jpi1,jpi2,mainP,jp_species_panel,jpis1;
	JComboBox<?> jcb,speciesJcb,speciesJcb2,jcb1;
	JTextField jtf2,jtf_entity_number;
	JTextArea jta_entity_result,jtaDisplayEntity;
	JScrollPane jscrollp_entity_result;

	private List<String> pubmed_ids;
	private String type2;
	private int limit;
	private List<String> entity_ids;
	private CyServiceRegistrar serviceRegistrar;

	public InterfaceEntity(CyServiceRegistrar serviceRegistrar, List<String> ids)
	{
		this.serviceRegistrar=serviceRegistrar;
		this.pubmed_ids=ids;

		jb_initial= new JButton("initial");
		jb_run= new JButton("run");
		jb_back=new JButton("back");
		jb_import=new JButton("import");
		
		jl_entity_result=new JLabel("Entity Query Result:  ");
		jta_entity_result=new JTextArea(50,60);
		jta_entity_result.setLineWrap(true);
		jta_entity_result.setWrapStyleWord(true);
		jscrollp_entity_result=new JScrollPane(jta_entity_result);

		jp_species_panel=new DefineSpeciesIDPanel();
		jp_Entity_Number=new DefineEntityNumberPanel();
		jp_search_button=new JPanel();		

		jpQuery=new JPanel();
		jpQuery.setBorder(BorderFactory.createEmptyBorder(1, 10, 0, 10)); 
		jpQuery.setLayout(new GridLayout(3,1,5,5));

		jp_result_button=new JPanel();
		jp_result_button.setLayout(new BorderLayout(5,5));
		jpResult =new JPanel();
		jpResult.setLayout(new BorderLayout(5,5));
		jpResult.setBorder(BorderFactory.createEmptyBorder(1, 10, 10, 10)); 

		jp_search_button.add(jb_initial);

		jp_search_button.add(jb_run);


		jpQuery.add(jp_species_panel);
		//jpQuery.add(jps2);
		jpQuery.add(jp_Entity_Number);
		jpQuery.add(jp_search_button);

		jp_result_button.add(jb_back,BorderLayout.WEST);
		jp_result_button.add(jb_import,BorderLayout.EAST);

		jpResult.add(jl_entity_result,BorderLayout.NORTH);
		jpResult.add(jscrollp_entity_result,BorderLayout.CENTER);
		jpResult.add(jp_result_button,BorderLayout.SOUTH);

		jb_initial.addActionListener(this);
		jb_initial.setActionCommand("initial");
		jb_run.addActionListener(this);
		jb_run.setActionCommand("run");
		jb_back.addActionListener(this);
		jb_back.setActionCommand("back");
		jb_import.addActionListener(this);
		jb_import.setActionCommand("import");


		//input panel
		jpis1=new DefineSpeciesIDPanel();

		JLabel jli1 = new JLabel("Enter Entity List:");
		JLabel jlPast = new JLabel("A: Paste a Entity ID list");
		JLabel jlOr = new JLabel("Or"); 
		JLabel jlFile = new JLabel("B:Choose From a File");
		JLabel jlexp = new JLabel("exmaple:ENSP00000216807");
		jtfFileDir = new JLabel("");

		jtaDisplayEntity=new JTextArea(30,50);
		JScrollPane jscrollp3 = new JScrollPane(jtaDisplayEntity);

		JButton jb_clear = new JButton("clear");
		JButton jb_browse = new JButton("Browse");
		JButton jb_back2 = new JButton("back");
		JButton jb_import2 = new JButton("import");

		JPanel ip = new JPanel();
		;
		JPanel jpPastSpecies = new JPanel();
		JPanel jpPast = new JPanel();
		JPanel jpPastLabel = new JPanel();
		JPanel jpPastButton = new JPanel();
		JPanel jpFile = new JPanel();
		JPanel ip2 = new JPanel();
		JPanel jpInputBoutton = new JPanel();

		ip.setLayout(new BorderLayout(5,5));
		ip.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
		
		jpPastSpecies.setLayout(new BorderLayout(5,5));
		jpPast.setLayout(new BorderLayout(5,5));
		jpPastLabel.setLayout(new BorderLayout(5,5));
		jpFile.setLayout(new BorderLayout(5,5));
		ip2.setLayout(new BorderLayout(5,5));
		ip2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
		jpInputBoutton.setLayout(new BorderLayout(5,5));
		jpInputBoutton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 

		//ip1.add(jli1,BorderLayout.WEST);

		jpPastSpecies.add(jli1,BorderLayout.WEST);
		jpPastSpecies.add(jpis1,BorderLayout.NORTH);

		jpPastLabel.add(jlPast,BorderLayout.NORTH);
		jpPastLabel.add(jlexp,BorderLayout.CENTER);
		jpPastLabel.add(jlOr,BorderLayout.SOUTH);

		jpPastButton.add(jb_clear);

		jpPast.add(jpPastLabel,BorderLayout.WEST);
		jpPast.add(jscrollp3,BorderLayout.CENTER);
		jpPast.add(jpPastButton,BorderLayout.EAST);

		jpFile.add(jlFile,BorderLayout.WEST);
		jpFile.add(jb_browse,BorderLayout.CENTER);
		jpFile.add(jtfFileDir,BorderLayout.EAST);

		//ip2.add(jpPastSpecies,BorderLayout.NORTH);
		ip2.add(jpPast,BorderLayout.CENTER);
		ip2.add(jpFile,BorderLayout.SOUTH);

		jpInputBoutton.add(jb_back2,BorderLayout.WEST);
		jpInputBoutton.add(jb_import2,BorderLayout.EAST);

		jb_clear.addActionListener(this);
		jb_clear.setActionCommand("clear");
		jb_browse.addActionListener(this);
		jb_browse.setActionCommand("browse");
		jb_back2.addActionListener(this);
		jb_back2.setActionCommand("back");
		jb_import2.addActionListener(this);
		jb_import2.setActionCommand("ip_import");


		//layout
		String SearchPANEL = "Search Entity information from String";
		String InputPANEL = "Input Entity information";
		mainP = new JPanel(new CardLayout());

		//search panel
		JPanel jpEntityMain =new JPanel(new GridLayout(4,1));
		jpEntityMain.setLayout(new GridLayout(2,1));
		jpEntityMain.add(jpQuery);
		jpEntityMain.add(jpResult);
		//input panel
		ip.add(ip2,BorderLayout.CENTER);
		ip.add(jpInputBoutton,BorderLayout.SOUTH);
		//main panel
		mainP.add(jpEntityMain,SearchPANEL);
		mainP.add(ip,InputPANEL);


		JPanel comboBoxPane = new JPanel(); //use FlowLayout
		String comboBoxItems[] = { SearchPANEL, InputPANEL };
		JComboBox cb = new JComboBox(comboBoxItems);
		cb.setEditable(false);
		cb.addItemListener((ItemListener) this);
		comboBoxPane.add(cb);
		JLabel jl00=new JLabel("literature Source:");
		JPanel jlsp=new JPanel();
		jlsp.add(jl00);
		jlsp.add(comboBoxPane);

		this.add(jlsp, BorderLayout.PAGE_START);
		this.add(mainP, BorderLayout.CENTER);


		Insets insets1 = this.getInsets();
		this.setSize(800 + insets1.left + insets1.right,
				600 + insets1.top + insets1.bottom);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}



	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("initial"))
		{
			//jp_species_panel.changeSpeices("9606");
			jtf_entity_number.setText("100");
		}
		else if(e.getActionCommand().equals("run"))
		{
			
			type2=DefineSpeciesIDPanel.getSpeciesID();
			//			type2=jp_species_panel.GetSpeices();
			limit=Integer.parseInt(((DefineEntityNumberPanel) jp_Entity_Number).getEntityNumber());
			System.out.println(limit);
			SearchEntityFactory factory=new  SearchEntityFactory(type2,limit,pubmed_ids);
			TaskManager<?,?> taskManager = this.serviceRegistrar.getService(TaskManager.class);
			taskManager.execute(factory.createTaskIterator(), this);

		}
		else if(e.getActionCommand().equals("import"))
		{
			if(jta_entity_result.getText()!=null){
				System.out.println("Start network");
				BuildNetworkFactory factory=new BuildNetworkFactory(pubmed_ids,entity_ids);
				TaskManager<?,?> taskManager = this.serviceRegistrar.getService(TaskManager.class);
				taskManager.execute(factory.createTaskIterator(), this);
				setVisible(false); //you can't see me!
				dispose();
			}
			else{
				System.out.println("print run first");
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
			jtfFileDir.setText(filename);

			FileInputStream fis;
			try {
				fis = new FileInputStream(filename);
				byte buffer[] = new byte[fis.available()];
				fis.read(buffer);
				String message = new String(buffer);
				jtaDisplayEntity.setText(message);
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
			jtaDisplayEntity.setText("");
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
			entity_ids= new ArrayList();
			for(PubmedEntity et : (List<PubmedEntity>) arg0.getResults(List.class)) {

				text += et.getType()+"."+et.getEntityID() +"\t"+et.getName()+ "\n";
				entity_ids.add(et.getType()+"."+et.getEntityID());	
			}
			jta_entity_result.setText(text);
		} else if(arg0.getClass().getSimpleName().equals("BuildNetworkTask")) {
			ShowNetworkFactory factory=new ShowNetworkFactory(this.serviceRegistrar.getService(CyNetworkManager.class),
					this.serviceRegistrar.getService(CyNetworkNaming.class),
					this.serviceRegistrar.getService(CyNetworkFactory.class),
					arg0.getResults(JSONObject.class));
			TaskManager<?,?> taskManager = this.serviceRegistrar.getService(TaskManager.class);
			taskManager.execute(factory.createTaskIterator(), this);

		} 
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		CardLayout cl = (CardLayout)(mainP.getLayout());
		cl.show(mainP, (String)e.getItem());
	}
}





