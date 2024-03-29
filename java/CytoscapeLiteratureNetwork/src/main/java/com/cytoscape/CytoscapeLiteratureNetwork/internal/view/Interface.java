package com.cytoscape.CytoscapeLiteratureNetwork.internal.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.FinishStatus;
import org.cytoscape.work.ObservableTask;
import org.cytoscape.work.TaskManager;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.TaskObserver;

import com.cytoscape.CytoscapeLiteratureNetwork.internal.object.PubmedMetadata;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.task.SearchPubmedIDFactory;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.task.SearchPubmedMetadataFactory;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.view.InterfaceEntity;

import java.net.*;
import java.io.*;

public class Interface extends JFrame implements ActionListener, TaskObserver,ItemListener{
	/**
	 * 
	 */
	private String query_word;
	//private ArrayList<String> chatter;
	private static final long serialVersionUID = 1L;
	JPanel jp_pubmed_result,jp_pubmed_query_button,jp_pubmed_button,main_P;
	JButton jb_pubmed_delete,jb_pubmed_run,jb_pubmed_cancel,jb_pubmed_next,jb_pubmed_paste_clear,jb_pubmed_ip_cancel,jb_pubmed_browse,jb_pubmed_ip_next;
	JLabel jl1,jlPubmedQuery,jl_pubmed_result,jl_file_dir;
	JTextArea jta_pubmed_query,jta_pubmed_paste;
	JEditorPane jep_pubmed_result;
	private List<PubmedMetadata> pmmds;
	private List<String> pubmed_ids;

	private int subend=100;
	
	private CyServiceRegistrar serviceRegistrar;

	
	public Interface(CyServiceRegistrar serviceRegistrar){
		this.serviceRegistrar=serviceRegistrar;
		
//pubmed label
		JLabel jl_pubmed = new JLabel("literature Source:");
		
//query part
		
	//query enter
		//query  label
		JLabel jl_pubmed_query = new JLabel("A:PubMed Query:");
		JPanel jp_pubmed_query_jl=new JPanel();
		jp_pubmed_query_jl.setLayout(new BorderLayout(5,5));
		jp_pubmed_query_jl.add(jl_pubmed_query,BorderLayout.WEST);
		//query enter
		jta_pubmed_query= new JTextArea (30,30);
		String text = "breast cancer";
		jta_pubmed_query.setText(text);
		jta_pubmed_query.setLineWrap(true);
		jta_pubmed_query.setWrapStyleWord(true);
		JScrollPane jscrollp_pubmed_query = new JScrollPane(jta_pubmed_query);
		jscrollp_pubmed_query.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jscrollp_pubmed_query.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		//query button panel
		jb_pubmed_delete= new JButton("delete");
		jb_pubmed_run= new JButton("run");
		jb_pubmed_delete.addActionListener(this);
		jb_pubmed_delete.setActionCommand("delete");
		jb_pubmed_run.addActionListener(this);
		jb_pubmed_run.setActionCommand("run");
		JPanel jp_pubmed_query_button = new JPanel();
		jp_pubmed_query_button.add(jb_pubmed_delete);
		jp_pubmed_query_button.add(jb_pubmed_run);
		//query panel
		JPanel jp_pubmed_query_input=new JPanel();
		jp_pubmed_query_input.setLayout(new BorderLayout(5,5));
		jp_pubmed_query_input.add(jp_pubmed_query_jl,BorderLayout.NORTH);
		jp_pubmed_query_input.add(jscrollp_pubmed_query,BorderLayout.CENTER);
		jp_pubmed_query_input.add(jp_pubmed_query_button,BorderLayout.SOUTH);
		
	//pubmed query result part
		//pubmed result label
		JLabel jl_pubmed_query_result = new JLabel("PubMed Query Result:");		
		// display result	
		//new JEditorPane("text/html", content) dispaly html
		jep_pubmed_result=new JEditorPane();
		jep_pubmed_result.setSize(30, 30);		
		//scroll format
		JScrollPane jscrollp_pubmed_result = new JScrollPane(jep_pubmed_result);
		jscrollp_pubmed_result.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
		jscrollp_pubmed_result.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 		
		//control scroll display when needs
		JSplitPane JSplitPane_pubmed_result = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JSplitPane_pubmed_result.setOneTouchExpandable(true);
		JSplitPane_pubmed_result.setLayout(new BorderLayout(5,5));
		JSplitPane_pubmed_result.add(jl_pubmed_query_result,BorderLayout.NORTH);		
		JSplitPane_pubmed_result.add(jscrollp_pubmed_result,BorderLayout.CENTER);
		
	//pubmed query display panel	
		JPanel jp_pubmed_query_display= new JPanel();
		jp_pubmed_query_display.setLayout(new GridLayout(2,1));
		jp_pubmed_query_display.add(jp_pubmed_query_input);
		jp_pubmed_query_display.add(JSplitPane_pubmed_result);
		
	//pubmed query result button
		//button define
		jb_pubmed_next= new JButton("next");
		jb_pubmed_cancel= new JButton("cancel");
		//button funtion
		jb_pubmed_cancel.addActionListener(this);
		jb_pubmed_cancel.setActionCommand("cancel");
		jb_pubmed_next.addActionListener(this);
		jb_pubmed_next.setActionCommand("next");
		//button panel
		jp_pubmed_query_button=new JPanel();
		jp_pubmed_query_button.setLayout(new BorderLayout(5,5));
		jp_pubmed_query_button.add(jb_pubmed_next,BorderLayout.EAST);
		jp_pubmed_query_button.add(jb_pubmed_cancel,BorderLayout.WEST);
		

//input part
		
	//paste part
		//Paste label
		JLabel jl_pubmed_paste= new JLabel("A: Paste a PMID list");
		JLabel jl_or1 = new JLabel("Or"); 
		JLabel jl_pubmed_exp = new JLabel("exmaple:27137076");		
		JPanel jp_pubmed_paste_jl=new JPanel();
		jp_pubmed_paste_jl.setLayout(new BorderLayout(5,5));
		jp_pubmed_paste_jl.add(jl_pubmed_paste,BorderLayout.NORTH);
		jp_pubmed_paste_jl.add(jl_pubmed_exp,BorderLayout.CENTER);
		jp_pubmed_paste_jl.add(jl_or1,BorderLayout.SOUTH);	
		//paste enter
		jta_pubmed_paste = new JTextArea (30,30);
		jta_pubmed_paste.setLineWrap(true);
		jta_pubmed_paste.setWrapStyleWord(true);		
		JScrollPane jscrollp_pubmed_paste = new JScrollPane(jta_pubmed_paste);
		jscrollp_pubmed_paste.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jscrollp_pubmed_paste.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 	
		//paste clear button
		jb_pubmed_paste_clear = new JButton("clear");
		jb_pubmed_paste_clear.addActionListener(this);
		jb_pubmed_paste_clear.setActionCommand("clear");
		JPanel jp_pubmed_paste_button = new JPanel();
		jp_pubmed_paste_button.setLayout(new BorderLayout(5,5));
		jp_pubmed_paste_button.add(jb_pubmed_paste_clear,BorderLayout.NORTH);	
		//paste panel
		JPanel jp_pubmed_paste = new JPanel();
		jp_pubmed_paste.setLayout(new BorderLayout(5,5));
		jp_pubmed_paste.add(jp_pubmed_paste_jl,BorderLayout.WEST);
		jp_pubmed_paste.add(jscrollp_pubmed_paste,BorderLayout.CENTER);
		jp_pubmed_paste.add(jp_pubmed_paste_button,BorderLayout.EAST);	

		
	//file part
		//file label
		JLabel jl_pubmed_file = new JLabel("B:Choose From a File");		
		//file button
		JButton jb_pubmed_browse = new JButton("Browse");
		jb_pubmed_browse.addActionListener(this);
		jb_pubmed_browse.setActionCommand("browse");		
		//file dir label
		jl_file_dir = new JLabel("");		
		//file panel
		JPanel jp_pubmed_file = new JPanel();
		jp_pubmed_file.setLayout(new BorderLayout(5,5));
		jp_pubmed_file.add(jl_pubmed_file,BorderLayout.WEST);
		jp_pubmed_file.add(jb_pubmed_browse,BorderLayout.CENTER);
		jp_pubmed_file.add(jl_file_dir,BorderLayout.EAST);
		
	//input enter panel	
		JPanel jp_pubmed_enter_input = new JPanel();
		jp_pubmed_enter_input.setLayout(new BorderLayout(5,5));
		jp_pubmed_enter_input.add(jp_pubmed_paste,BorderLayout.CENTER);
		jp_pubmed_enter_input.add(jp_pubmed_file,BorderLayout.SOUTH);
		
	// pubmed input button panel		
		//button define
		jb_pubmed_ip_next = new JButton("next");
		jb_pubmed_ip_cancel= new JButton("cancel");
		//button funtion
		jb_pubmed_ip_next.addActionListener(this);
		jb_pubmed_ip_next.setActionCommand("ip_next");
		jb_pubmed_ip_cancel.addActionListener(this);
		jb_pubmed_ip_cancel.setActionCommand("cancel");
		//button panel
		JPanel jp_pubmed_ip_button = new JPanel();
		jp_pubmed_ip_button.setLayout(new BorderLayout(5,5));
		jp_pubmed_ip_button.add(jb_pubmed_ip_next,BorderLayout.EAST);
		jp_pubmed_ip_button.add(jb_pubmed_ip_cancel,BorderLayout.WEST);

		
//pubmed main panel		
		String QueryPANEL = "Query literature information from PubMed";
		String InputPANEL = "Input literature information";
		main_P = new JPanel(new CardLayout());
		main_P.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
		
		//query panel
		JPanel jp_pubmed_query = new JPanel();
		jp_pubmed_query.setLayout(new BorderLayout(5,5));
		jp_pubmed_query.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
		jp_pubmed_query.add(jp_pubmed_query_display,BorderLayout.CENTER);
		jp_pubmed_query.add(jp_pubmed_query_button,BorderLayout.SOUTH);

		//input panel
		JPanel jp_pubmed_input = new JPanel();
		jp_pubmed_input.setLayout(new BorderLayout(5,5));
		jp_pubmed_input.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		jp_pubmed_input.add(jp_pubmed_enter_input,BorderLayout.CENTER);
		jp_pubmed_input.add(jp_pubmed_ip_button,BorderLayout.SOUTH);
		
		//main panel
		main_P.add(jp_pubmed_query,QueryPANEL);
		main_P.add(jp_pubmed_input,InputPANEL);
		jlPubmedQuery= new JLabel("PubMed Query:");
		
		JPanel comboBoxPane = new JPanel(); //use FlowLayout
		String comboBoxItems[] = { QueryPANEL, InputPANEL };
		JComboBox jcb_pubmed = new JComboBox(comboBoxItems);
		jcb_pubmed.setEditable(false);
		jcb_pubmed.addItemListener((ItemListener) this);
		comboBoxPane.add(jcb_pubmed);
		
		JPanel jlsp=new JPanel();
		jlsp.add(jl_pubmed);
		jlsp.add(comboBoxPane);
		
		this.add(jlsp, BorderLayout.PAGE_START);
		this.add(main_P, BorderLayout.CENTER);
			
		
		this.setSize(800,600);
		this.setTitle("Literature Netwrok");
		//this.setIconImage((new ImageIcon("images/images.jpeg")).getImage());
		this.setLocation(100, 100);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("delete"))
		{
			jta_pubmed_query.setText("");
		}
		else if(e.getActionCommand().equals("run"))
			
		{
			query_word=jta_pubmed_query.getText();
			SearchPubmedIDFactory factory=new SearchPubmedIDFactory(query_word);
			TaskManager<?,?> taskManager = this.serviceRegistrar.getService(TaskManager.class);
			taskManager.execute(factory.createTaskIterator(), this);
		}
		else if(e.getActionCommand().equals("next"))
		{
			 InterfaceEntity ei =new InterfaceEntity(serviceRegistrar, pubmed_ids,query_word);
			 ei.setVisible(true);
			 setVisible(false); //you can't see me!
			 dispose();
		}
		else if(e.getActionCommand().equals("ip_next"))
		{
			 pubmed_ids=Arrays.asList(jta_pubmed_paste.getText().split("\\s+"));
			 InterfaceEntity ei =new InterfaceEntity(serviceRegistrar, pubmed_ids,query_word);
			 ei.setVisible(true);
			 setVisible(false); //you can't see me!
			 dispose();
		}
		else if(e.getActionCommand().equals("cancel"))
		{
			 setVisible(false); //you can't see me!
			 dispose();
		}
		else if(e.getActionCommand().equals("clear"))
		{
			jta_pubmed_paste.setText("");
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
			    jta_pubmed_paste.setText(message);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    
		    
		}

	}

	@Override
	public void allFinished(FinishStatus arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void taskFinished(ObservableTask arg0) {
		if(arg0.getClass().getSimpleName().equals("SearchPubmedIDTask")) {
			pubmed_ids = (List<String>) arg0.getResults(List.class);
			if(subend>pubmed_ids.size()){
				subend=pubmed_ids.size();
			}
			SearchPubmedMetadataFactory factory=new SearchPubmedMetadataFactory(pubmed_ids.subList(0,subend), true);
			TaskManager<?,?> taskManager = this.serviceRegistrar.getService(TaskManager.class);
			taskManager.execute(factory.createTaskIterator(), this);
		} else if(arg0.getClass().getSimpleName().equals("SearchPubmedMetadataTask")) {
			 pmmds = (List<PubmedMetadata>) arg0.getResults(List.class);
			String pmmdtext = "";
			int i=0;
			for(PubmedMetadata pm : pmmds){
				i=i+1;
				pmmdtext+="["+i+"]"+"<b>"+pm.getTitle()+"</b>"
						+"<br>"+pm.getAuthors()
								+ "<br>"+pm.getJournal()+" "+pm.getPublicdate()
										+ "<br>PMID:"+pm.getId()+"<br><br>";
			}
			
			jep_pubmed_result.setContentType("text/html");
			jep_pubmed_result.setText(pmmdtext);
		}
	}


	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			CardLayout cl = (CardLayout)(main_P.getLayout());
		    cl.show(main_P, (String)e.getItem());
	}	
}


class QueryPANEL
{
	
}
