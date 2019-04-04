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

public class Interface extends JFrame implements ActionListener, TaskObserver,KeyListener,ItemListener{
	/**
	 * 
	 */
	private String query;
	//private ArrayList<String> chatter;
	private static final long serialVersionUID = 1L;
	JPanel jp2,jp11,jp111,jp12,jp13,jp21,jp22,jp23,sp,mainP,ip,ip1,ip2,ip11,ip12,ip121,ip13;
	JButton jb1,jb2,jb3,jb4,jb5,jb6,jbi1,jbi21,jbi22;
	JLabel jl1,jl2,jl3,jli1,jli21,jli22,jli23,jTextField1;
	JTextField jtfb,jtd1,jtd2;
	JCheckBox jcb1;
	JTextArea jta1,jta2,jta3;
	JEditorPane pane;
	JScrollPane jscrollp1,jscrollp2,jscrollp3;
	JSplitPane jsp;
	JComboBox jcb=null;
	
	private List<String> pubmed_ids;
	
	private CyServiceRegistrar serviceRegistrar;

	
	public Interface(CyServiceRegistrar serviceRegistrar){
		this.serviceRegistrar=serviceRegistrar;
		//search  panel
		jl1= new JLabel("Species:");
		jl2= new JLabel("PubMed Query:");
		jl3= new JLabel("PubMed Query Result:");
			
		jb1= new JButton("delete");
		jb3= new JButton("run");
		jb4= new JButton("cancel");
		jb5= new JButton("next");

		
		jta1= new JTextArea (30,30);
		String text = "breast cancer";
		jta1.setText(text);
		jta1.setLineWrap(true);
		jta1.setWrapStyleWord(true);
		pane=new JEditorPane();
		pane.setSize(30, 30);
		jta2=new JTextArea(30,30);
		
		jscrollp1=new JScrollPane(jta1);
		jscrollp2=new JScrollPane(pane);
		jscrollp1.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jscrollp1.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		jscrollp2.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
		jscrollp2.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		
		jsp=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		jsp.setOneTouchExpandable(true);
		jsp.setLayout(new BorderLayout(5,5));
		
		sp=new JPanel();
		MyJPanel jp1=new MyJPanel();	
		jp2=new JPanel();
		jp11=new JPanel();	
		jp111=new JPanel();	
		jp12=new JPanel();
		jp13=new JPanel();	
		jp21=new JPanel();
		jp22=new JPanel();	
		jp23=new JPanel();
		jp1.setLayout(new BorderLayout(5,5));
		jp1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
		jp2.setLayout(new BorderLayout(5,5));
		jp2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
		jp11.setLayout(new BorderLayout(5,5));
		jp111.setLayout(new BorderLayout(5,5));
		jp21.setLayout(new BorderLayout(5,5));
		
		// search site
		jp11.add(jl2,BorderLayout.SOUTH);
		jp13.add(jb1);
		//jp13.add(jb2);
		jp13.add(jb3);
		jp1.add(jp11,BorderLayout.NORTH);
		jp1.add(jscrollp1,BorderLayout.CENTER);
		jp1.add(jp13,BorderLayout.SOUTH);

		// result display site

		jsp.add(jscrollp2,BorderLayout.CENTER);
		jp21.add(jb5,BorderLayout.EAST);
		jp21.add(jb4,BorderLayout.WEST);
		
		jp2.add(jl3,BorderLayout.NORTH);
		jp2.add(jsp,BorderLayout.CENTER);
		jp2.add(jp21,BorderLayout.SOUTH);

		
		jb1.addActionListener(this);
		jb1.setActionCommand("delete");
//		jb2.addActionListener(this);
//		jb2.setActionCommand("pause");
		jb3.addActionListener(this);
		jb3.setActionCommand("run");
		jb4.addActionListener(this);
		jb4.setActionCommand("cancel");
		jb5.addActionListener(this);
		jb5.setActionCommand("next");
		
		//input panel
		jli1 = new JLabel("Enter PMID List:");
		jli21= new JLabel("A: Paste a PMID list");
		jli22= new JLabel("Or"); 
		JLabel jli31 = new JLabel("B:Choose From a File");
		JLabel jlexp = new JLabel("exmaple:27137076");
		jTextField1 = new JLabel("");
		jta3=new JTextArea(30,50);
		jscrollp3=new JScrollPane(jta3);
		
		jbi1 = new JButton("clear");
		JButton jbi2 = new JButton("Browse");
		jbi21= new JButton("cancel");
		jbi22= new JButton("next");
			
		ip=new JPanel();
		ip1=new JPanel();
		JPanel ip21 = new JPanel();
		JPanel ip22 = new JPanel();
		JPanel ip221 = new JPanel();
		JPanel ip222 = new JPanel();
		JPanel ip23 = new JPanel();
		ip2=new JPanel();
		JPanel ip3 = new JPanel();
		
		ip.setLayout(new BorderLayout(5,5));
		ip.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
		ip1.setLayout(new BorderLayout(5,5));
		ip1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
		ip21.setLayout(new BorderLayout(5,5));
		ip22.setLayout(new BorderLayout(5,5));
		ip221.setLayout(new BorderLayout(5,5));
		ip23.setLayout(new BorderLayout(5,5));
		ip2.setLayout(new BorderLayout(5,5));
		ip2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
		ip3.setLayout(new BorderLayout(5,5));
		ip3.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
	
		//ip1.add(jli1,BorderLayout.WEST);
		
		ip21.add(jli1,BorderLayout.WEST);
		
		ip221.add(jli21,BorderLayout.NORTH);
		ip221.add(jlexp,BorderLayout.CENTER);
		ip221.add(jli22,BorderLayout.SOUTH);
		
		ip222.add(jbi1);
		
		ip22.add(ip221,BorderLayout.WEST);
		ip22.add(jscrollp3,BorderLayout.CENTER);
		ip22.add(ip222,BorderLayout.EAST);

		ip23.add(jli31,BorderLayout.WEST);
		ip23.add(jbi2,BorderLayout.CENTER);
		ip23.add(jTextField1,BorderLayout.EAST);
		
		ip2.add(ip21,BorderLayout.NORTH);
		ip2.add(ip22,BorderLayout.CENTER);
		ip2.add(ip23,BorderLayout.SOUTH);
		
		ip3.add(jbi21,BorderLayout.WEST);
		ip3.add(jbi22,BorderLayout.EAST);
		
		jbi1.addActionListener(this);
		jbi1.setActionCommand("clear");
		jbi2.addActionListener(this);
		jbi2.setActionCommand("browse");
		jbi21.addActionListener(this);
		jbi21.setActionCommand("cancel");
		jbi22.addActionListener(this);
		jbi22.setActionCommand("ip_next");
		
		//layout
		String SearchPANEL = "Search literature information from PubMed";
		String InputPANEL = "Input literature information";
		mainP=new JPanel(new CardLayout());
		
		//search panel
		sp.setLayout(new GridLayout(2,1));
		sp.add(jp1);
		sp.add(jp2);
		//input panel
		//ip.add(ip1,BorderLayout.NORTH);
		ip.add(ip2,BorderLayout.CENTER);
		ip.add(ip3,BorderLayout.SOUTH);
		//main panel
		mainP.add(sp,SearchPANEL);
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

		//Method came from the ItemListener class implementation,
		//contains functionality to process the combo box item selecting


		//this.setLayout(new GridLayout(2,1));

		//this.add(jp2);
		this.setSize(800,
                600);
		this.setTitle("Literature Netwrok");
		//this.setIconImage((new ImageIcon("images/images.jpeg")).getImage());
		this.setLocation(100, 100);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//this.setVisible(true);
		this.addKeyListener(jp1);
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("delete"))
		{
			jta1.setText("");
		}
		else if(e.getActionCommand().equals("pause"))
		{
			System.out.println("click bb");
		}
		else if(e.getActionCommand().equals("run"))
		{
			SearchPubmedIDFactory factory=new SearchPubmedIDFactory(jta1.getText());
			TaskManager<?,?> taskManager = this.serviceRegistrar.getService(TaskManager.class);
			taskManager.execute(factory.createTaskIterator(), this);
		}
		else if(e.getActionCommand().equals("next"))
		{
			 InterfaceEntity ei =new InterfaceEntity(serviceRegistrar, pubmed_ids);
			 ei.setVisible(true);
			 setVisible(false); //you can't see me!
			 dispose();
		}
		else if(e.getActionCommand().equals("ip_next"))
		{
			 pubmed_ids=Arrays.asList(jta3.getText().split("\\s+"));
			 InterfaceEntity ei =new InterfaceEntity(serviceRegistrar, pubmed_ids);
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
			jta3.setText("");
		}
		else if(e.getActionCommand().equals("browse"))
		{
			JFileChooser fileDlg = new JFileChooser();
		    fileDlg.showOpenDialog(this);
		    String filename = fileDlg.getSelectedFile().getAbsolutePath();
		    jTextField1.setText(filename);

		    FileInputStream fis;
			try {
				fis = new FileInputStream(filename);
				byte buffer[] = new byte[fis.available()];
			    fis.read(buffer);
			    String message = new String(buffer);
			    jta3.setText(message);
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
			SearchPubmedMetadataFactory factory=new SearchPubmedMetadataFactory(pubmed_ids);
			TaskManager<?,?> taskManager = this.serviceRegistrar.getService(TaskManager.class);
			taskManager.execute(factory.createTaskIterator(), this);
		} else if(arg0.getClass().getSimpleName().equals("SearchPubmedMetadataTask")) {
			String text = "";
			int i=0;
			for(PubmedMetadata pm : (List<PubmedMetadata>) arg0.getResults(List.class)) {
				i=i+1;
				text+="["+i+"]"+"<b>"+pm.getTitle()+"</b>"
						+"<br>"+pm.getAuthors()
								+ "<br>"+pm.getJournal()+" "+pm.getPublicdate()
										+ "<br>PMID:"+pm.getId()+"<br><br>";
			}
			
			pane.setContentType("text/html");
			pane.setText(text);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		CardLayout cl = (CardLayout)(mainP.getLayout());
	    cl.show(mainP, (String)e.getItem());
	}

}




class  MyJPanel extends  JPanel implements KeyListener
{
	/**
	 * 
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("search"+e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}

class SearchPanel
{
	
}
