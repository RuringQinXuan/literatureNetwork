package com.cytoscape.CytoscapeLiteratureNetwork.internal.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;

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
import com.cytoscape.CytoscapeLiteratureNetwork.internal.view.EntitiesInterface;

import java.net.*;
import java.io.*;

public class Interface extends JFrame implements ActionListener, TaskObserver{
	/**
	 * 
	 */
	private String query;
	
	private static final long serialVersionUID = 1L;
	JPanel jp2,jp11,jp111,jp12,jp13,jp21,jp22,jp23,jp211;
	JButton jb1,jb2,jb3,jb4,jb5,jb6,jb7;
	JLabel jl1,jl2,lj3;
	JTextField jtd1,jtd2;
	JCheckBox jcb1;
	JTextArea jta1,jta2;
	JScrollPane jscrollp1,jscrollp2;
	JSplitPane jsp;
	JComboBox jcb=null;
	
	private List<String> pubmed_ids;
	
	private CyServiceRegistrar serviceRegistrar;
	
	public Interface(CyServiceRegistrar serviceRegistrar){
		this.serviceRegistrar=serviceRegistrar;
		//define 
		
		jl1= new JLabel("Species:");
		jl2= new JLabel("PubMed Query:");
		lj3= new JLabel("PubMed Query Result:");

		//jtd1= new JTextField(30);
		String []chatter={"all Species","Homo sapiens","Hordeum vulgare","Human SARS coronavirus (SARS-CoV)","Human T-cell leukemia virus 1 (HTLV-1)"};
		jcb= new JComboBox(chatter);
		jcb.setSelectedIndex(4);
		jcb.setEditable(true);
		jcb.addActionListener(this);
		String spacies=(String) jcb.getSelectedItem();
		
		jb1= new JButton("delete");
		jb2= new JButton("pause");
		jb3= new JButton("run");
		jb4= new JButton("cancel");
		jb5= new JButton("next");
		jb7 = new JButton("refresh");

		jta1= new JTextArea (30,30);
		String text = "pancreatic cancer";
		jta1.setText(text);
		jta1.setLineWrap(true);
		jta1.setWrapStyleWord(true);
		jta2=new JTextArea(30,30);
		
		jscrollp1=new JScrollPane(jta1);
		jscrollp2=new JScrollPane(jta2);
		jscrollp1.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jscrollp1.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		jscrollp2.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
		jscrollp2.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		
		jsp=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		jsp.setOneTouchExpandable(true);
		jsp.setLayout(new BorderLayout(5,5));
		
		
		MyJPanel jp1=new MyJPanel();	
		jp2=new JPanel();
		jp11=new JPanel();	
		jp111=new JPanel();	
		jp12=new JPanel();
		jp13=new JPanel();	
		jp21=new JPanel();
		jp211=new JPanel();
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
		jp111.add(jl1,BorderLayout.WEST);
		jp111.add(jcb,BorderLayout.CENTER);
		jp11.add(jp111,BorderLayout.CENTER);
		jp11.add(jl2,BorderLayout.SOUTH);
		jp13.add(jb1);
		jp13.add(jb2);
		jp13.add(jb3);
		jp1.add(jp11,BorderLayout.NORTH);
		jp1.add(jscrollp1,BorderLayout.CENTER);
		jp1.add(jp13,BorderLayout.SOUTH);

		// result display site

		jsp.add(jscrollp2,BorderLayout.CENTER);
		jp211.add(jb7);
		jsp.add(jp211,BorderLayout.EAST);
		jp21.add(jb5,BorderLayout.EAST);
		jp21.add(jb4,BorderLayout.WEST);
		
		jp2.add(lj3,BorderLayout.NORTH);
		jp2.add(jsp,BorderLayout.CENTER);
		jp2.add(jp21,BorderLayout.SOUTH);

		jb1.addActionListener(this);
		jb1.setActionCommand("delete");
		jb2.addActionListener(this);
		jb2.setActionCommand("pause");
		jb3.addActionListener(this);
		jb3.setActionCommand("run");
		jb4.addActionListener(this);
		jb4.setActionCommand("cancel");
		jb5.addActionListener(this);
		jb5.setActionCommand("next");
		
		//layout
		
		this.setLayout(new GridLayout(2,1));
		this.add(jp1);
		this.add(jp2);
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
			 EntitiesInterface ei =new EntitiesInterface(serviceRegistrar, pubmed_ids);
			 ei.setVisible(true);
			 setVisible(false); //you can't see me!
			 dispose();
		}
		else if(e.getActionCommand().equals("cancel"))
		{
			 setVisible(false); //you can't see me!
			 dispose();
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
			
			for(PubmedMetadata pm : (List<PubmedMetadata>) arg0.getResults(List.class)) {
				text += pm.getTitle() + "\n";
			}
			
			jta2.setText(text);
		}
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
