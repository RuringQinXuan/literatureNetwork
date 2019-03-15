package com.cytoscape.CytoscapeLiteratureNetwork.internal.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import com.cytoscape.CytoscapeLiteratureNetwork.internal.view.Interface;

public class InterfaceEntity extends JFrame implements ActionListener, TaskObserver{
	
	JButton jb1,jb2,jb3,jb4,jb5=null;
	JLabel jl1,jl2,jl3,jl4,jl5,jl6=null;
	JPanel jpResult,jpSearch,jps1,jps11,jps111,jps112,jps12,jps2,jps3,jps4,jpr1=null;
	JComboBox<?> jcb,speciesJcb;
	JTextField jtf1,jtf2,jtf3;
	JTextArea jta1;
	JScrollPane jscrollp1;
	
	private List<String> pubmed_ids;
	private String type2;
	private List<String> entity_ids=null;
	private CyServiceRegistrar serviceRegistrar;

	public InterfaceEntity(CyServiceRegistrar serviceRegistrar, List<String> ids)
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

		
		jb1= new JButton("delete");
		jb2= new JButton("pause");
		jb3= new JButton("run");
		jb4=new JButton("back");
		jb5=new JButton("import");
				
		JComboBox speciesJcb = new AutoCompleteComboBox();
		try {
			Species.readSpecies();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Species> speciesList1 = Species.getSpecies();
		List<Map<String,String>> speciesMap = new ArrayList<>();
		speciesJcb.addItem("HOMO SAPIEN\t9606");
		for (Species species: speciesList1) {
			Map<String,String> map = new HashMap<>();
			map.put("taxonomyId", ""+species.getTaxId());
			map.put("scientificName", species.getOfficialName().toUpperCase());
			map.put("abbreviatedName", species.getName());
			speciesMap.add(map);
			speciesJcb.addItem(species.getOfficialName().toUpperCase()+"\t"+species.getTaxId());
		}
		jcb= speciesJcb;
		jcb.setEditable(true);
		jcb.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	            myBox(evt);
	            }
	        });
		jcb.setPreferredSize(new Dimension(400,10));
		jl1=new JLabel("Extraction Controls");
		jl2=new JLabel("The Species of Protein :");
		jl3=new JLabel("TaxonomyId");
        jl4=new JLabel("confidence (Score) cutoff: ");
        jl5=new JLabel("Maxinum number of proteins:  ");
        jl6=new JLabel("Entity Query Result:  ");
        
        jta1=new JTextArea(50,60);
        jta1.setLineWrap(true);
		jta1.setWrapStyleWord(true);
		
		jscrollp1=new JScrollPane(jta1);
		
		jtf1=new JTextField(20);
		jtf1.setEditable(false);
		jtf1.setText("9606");
		jtf2=new JTextField(2);
		jtf3=new JTextField(2);

		
        
        jps11=new JPanel();
        jps111=new JPanel();
        jps12=new JPanel();
        jps11.setLayout(new GridLayout(2,1));
        jps111.setLayout(new BorderLayout(5,5));
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
        jps111.add(jl2,BorderLayout.WEST);
        jps111.add(jcb,BorderLayout.CENTER);
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
	public ArrayList<String>  GetSpecies(String Speciesfilename){
		ArrayList<String> result = null;
		//获取文件路径 
		try (FileReader f = new FileReader(Speciesfilename)) {
		    StringBuffer sb = new StringBuffer();
		    while (f.ready()) {
		        char c = (char) f.read();
		        if (c == '\n') {
		        	result.add(sb.toString());
		            sb = new StringBuffer();
		           
		        } else {
		            sb.append(c);
		        }
		    }
		    if (sb.length() > 0) {
		    	result.add(sb.toString());
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return result;
	}

	protected void myBox(ActionEvent evt) {
	    if (jcb.getSelectedItem() != null) {
	    	String text=jcb.getSelectedItem().toString();
	    	String[] taxID=text.split("\t");
	    	System.out.println(Arrays.toString(taxID));
	    	//String ID=taxID[1];
	    	//System.out.println(ID);
	        jtf1.setText(taxID[1]);
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
			Interface Interface =  new Interface(serviceRegistrar);
        	Interface.setVisible(true);
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
				entity_ids.add(et.getType()+"."+et.getEntityID());
			}
			
			jta1.setText(text);
		} else if(arg0.getClass().getSimpleName().equals("BuildNetworkTask")) {
			ShowNetworkFactory factory=new ShowNetworkFactory(this.serviceRegistrar.getService(CyNetworkManager.class),
					this.serviceRegistrar.getService(CyNetworkNaming.class),
					this.serviceRegistrar.getService(CyNetworkFactory.class),
					arg0.getResults(JSONObject.class));
			TaskManager<?,?> taskManager = this.serviceRegistrar.getService(TaskManager.class);
			taskManager.execute(factory.createTaskIterator(), this);
			
		} 
	}
}
class AutoCompleteComboBox extends JComboBox
{
   public int caretPos=0;
   public JTextField inputField=null;
  
   public AutoCompleteComboBox() {
      setEditor(new BasicComboBoxEditor());
      setEditable(true);
   }
  
   public void setSelectedIndex(int index) {
      super.setSelectedIndex(index);
      inputField.setText(getItemAt(index).toString());
      inputField.moveCaretPosition(caretPos);
   }
  
   public void setEditor(ComboBoxEditor editor) {
      super.setEditor(editor);
      if (editor.getEditorComponent() instanceof JTextField) {
         inputField = (JTextField) editor.getEditorComponent();
  
         inputField.addKeyListener(new KeyAdapter() {
            public void keyReleased( KeyEvent ev ) {
               char key=ev.getKeyChar();
               if (! (Character.isLetterOrDigit(key) || Character.isSpaceChar(key) )) return;
  
               caretPos=inputField.getCaretPosition();
               String text="";
               try {
                  text=inputField.getText(0, caretPos);
               }
               catch (javax.swing.text.BadLocationException e) {
                  e.printStackTrace();
               }
  
               for (int i=0; i<getItemCount(); i++) {
                  String element = (String) getItemAt(i);
                  if (element.startsWith(text.toUpperCase())) {
                     setSelectedIndex(i);
                     return;
                  }
               }
            }
         });
      }
   }
}