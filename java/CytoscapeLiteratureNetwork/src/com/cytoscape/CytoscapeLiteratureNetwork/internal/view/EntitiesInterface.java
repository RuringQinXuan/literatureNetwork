package com.cytoscape.CytoscapeLiteratureNetwork.internal.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.FinishStatus;
import org.cytoscape.work.ObservableTask;
import org.cytoscape.work.TaskManager;
import org.cytoscape.work.TaskObserver;

import com.cytoscape.CytoscapeLiteratureNetwork.internal.object.PubmedEntity;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.object.PubmedMetadata;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.task.BuildNetworkFactory;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.task.SearchEntityFactory;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.task.SearchPubmedIDFactory;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.task.SearchPubmedMetadataFactory;
import com.cytoscape.CytoscapeLiteratureNetwork.internal.view.Interface;

public class EntitiesInterface extends JFrame implements ActionListener, TaskObserver{
	
	JButton jb1,jb2,jb3,jb4,jb5=null;
	JLabel jl1,jl2,jl3,jl4,jl5,jl6=null;
	JPanel jpResult,jpSearch,jps1,jps11,jps111,jps112,jps12,jps2,jps3,jps4,jpr1=null;
	JComboBox<?> jcb;
	JTextField jtf1,jtf2,jtf3;
	JTextArea jta1;
	JScrollPane jscrollp1;
	
	private List<String> pubmed_ids;
	private String type2;
	private List<String> entity_ids=null;
	private CyServiceRegistrar serviceRegistrar;

	public EntitiesInterface(CyServiceRegistrar serviceRegistrar, List<String> ids)
	{
		this.serviceRegistrar=serviceRegistrar;
		this.pubmed_ids=ids;
		
		final JSlider slider1 = new JSlider(0, 100, 40);
		final JSlider slider2 = new JSlider(0, 10000, 100);
		slider1.setMinorTickSpacing(1);
		slider2.setMinorTickSpacing(1);
		slider1.setMajorTickSpacing(10);
		slider2.setMajorTickSpacing(500);
		slider1.setPaintTicks(true);
        //slider1.setPaintLabels(true);
		slider2.setPaintTicks(true);
        //slider2.setPaintLabels(true);
		
		jb5=new JButton("import");
		jb4=new JButton("back");
		jb4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the contents of the JTextArea component.
            	Interface Interface = null; // new Interface();
            	Interface.setVisible(true);
            	setVisible(false); //you can't see me!
            	dispose();
            }
			});
		
		jb1= new JButton("delete");
		jb2= new JButton("pause");
		jb3= new JButton("run");
	
		
		String []chatter={"homo sapiens\t9606"};
		JComboBox jComboBox = new JComboBox(chatter);
		jcb= jComboBox;
		jcb.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	            myBox(evt);
	            }
	        });
		
		jl1=new JLabel("Extraction Controls");
		jl2=new JLabel("Entity Type");
		jl3=new JLabel("Entity Type Content ");
        jl4=new JLabel("confidence (Score) cutoff: ");
        jl5=new JLabel("Maxinum number of proteins:  ");
        jl6=new JLabel("Entity Query Result:  ");
        
        jta1=new JTextArea(50,60);
        jta1.setLineWrap(true);
		jta1.setWrapStyleWord(true);
		
		jscrollp1=new JScrollPane(jta1);
		
		jtf1=new JTextField(20);
		jtf1.setText("9606");
		jtf2=new JTextField(2);
		jtf3=new JTextField(2);

		
        
        jps11=new JPanel();
        jps111=new JPanel();
        jps12=new JPanel();
        jps11.setLayout(new GridLayout(2,1));
        jps111.setLayout(new GridLayout(1,2));
        jps12.setLayout(new GridLayout(2,1));
        
       
        jps1=new JPanel();
        jps2=new JPanel();
        jps3=new JPanel();
        jps4=new JPanel();
        
        jps1.setLayout(new BorderLayout(5,5));
		jps2.setLayout(new BorderLayout(5,10));
		jps3.setLayout(new BorderLayout(5,5));
		jps1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
		jps2.setBorder(BorderFactory.createEtchedBorder());  
		jps3.setBorder(BorderFactory.createEtchedBorder());  

        jpSearch=new JPanel();
        jpSearch.setBorder(BorderFactory.createEmptyBorder(1, 10, 0, 10)); 
        jpSearch.setLayout(new GridLayout(4,1,5,5));

        jpr1=new JPanel();
        jpr1.setLayout(new BorderLayout(5,5));
        jpResult =new JPanel();
        jpResult.setLayout(new BorderLayout(5,5));
        jpResult.setBorder(BorderFactory.createEmptyBorder(1, 10, 10, 10)); 

        jps11.add(jl1);
        jps111.add(jl2);
        jps111.add(jcb);
        jps11.add(jps111);
        jps12.add(jl3);
        jps12.add(jtf1);
        
        jps1.add(jps11,BorderLayout.WEST);
        jps1.add(jps12,BorderLayout.CENTER);
        jps2.add(jl4,BorderLayout.WEST);
        jps2.add(slider1,BorderLayout.CENTER);
        jps2.add(jtf2,BorderLayout.EAST);
        
        jps3.add(jl5,BorderLayout.WEST);
        jps3.add(slider2,BorderLayout.CENTER);
        jps3.add(jtf3,BorderLayout.EAST);
        
        jps4.add(jb1);
        jps4.add(jb2);
        jps4.add(jb3);
        
        
        jpSearch.add(jps1);
        jpSearch.add(jps2);
        jpSearch.add(jps3);
        jpSearch.add(jps4);
        
        jpr1.add(jb4,BorderLayout.WEST);
        jpr1.add(jb5,BorderLayout.EAST);
        
        jpResult.add(jl6,BorderLayout.NORTH);
        jpResult.add(jscrollp1,BorderLayout.CENTER);
        jpResult.add(jpr1,BorderLayout.SOUTH);
        
		jb1.addActionListener(this);
		jb1.setActionCommand("delete");
		jb2.addActionListener(this);
		jb2.setActionCommand("pause");
		jb3.addActionListener(this);
		jb3.setActionCommand("run");
		jb4.addActionListener(this);
		jb4.setActionCommand("back");
		jb5.addActionListener(this);
		jb5.setActionCommand("import");
        
        this.setLayout(new GridLayout(2,1));
        this.add(jpSearch);
		this.add(jpResult);
		Insets insets1 = this.getInsets();
		this.setSize(800 + insets1.left + insets1.right,
                600 + insets1.top + insets1.bottom);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}

	protected void myBox(ActionEvent evt) {
	    if (jcb.getSelectedItem() != null) {
	    	String text=jcb.getSelectedItem().toString();
	    	System.out.println(text);
	    	String[] taxID=text.split("\t");
	    	System.out.println(Arrays.toString(taxID));
	    	//String ID=taxID[1];
	    	//System.out.println(ID);
	        //jtf1.setText();
	    }
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("delete"))
		{
			jtf1.setText("");
			jtf2.setText("0.4");
			jtf3.setText("100");
		}
		else if(e.getActionCommand().equals("pause"))
		{
			System.out.println("click bb");
		}
		else if(e.getActionCommand().equals("run"))
		{
			type2=jtf1.getText();
			SearchEntityFactory factory=new  SearchEntityFactory(type2,pubmed_ids);
			TaskManager<?,?> taskManager = this.serviceRegistrar.getService(TaskManager.class);
			taskManager.execute(factory.createTaskIterator(), this);
			
		}
		else if(e.getActionCommand().equals("import"))
		{
			if(jta1.getText()!=null){
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
		//todo
		if(arg0.getClass().getSimpleName().equals("SearchEntityTask")) {
			String text = "";
			entity_ids= new ArrayList();
			for(PubmedEntity et : (List<PubmedEntity>) arg0.getResults(List.class)) {
				text += et.getType()+"."+et.getEntityID() +"\t"+et.getName()+ "\n";
				entity_ids.add(et.getEntityID());
			}
			
			jta1.setText(text);
		} 
	}
}
