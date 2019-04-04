package com.cytoscape.CytoscapeLiteratureNetwork.internal.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.cytoscape.CytoscapeLiteratureNetwork.internal.ui.AutoCompleteSpeciesComboBox;

public class DefineSpeciesIDPanel extends JPanel {
private static final long serialVersionUID = 1L;
	
	JPanel jps11,jps111,jps112;
	JComboBox speciesJcb;
	
	private static String SpeciesID="9606";
	public DefineSpeciesIDPanel(){
		speciesJcb = new AutoCompleteSpeciesComboBox();

		speciesJcb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SpeciesID=myBox(evt);
			}
		});
		this.SpeciesID=SpeciesID;
		speciesJcb.setPreferredSize(new Dimension(400,10));
		JLabel jl1 = new JLabel("Extraction Controls");
		JLabel jl2 = new JLabel("The Species of Protein :");
		//JLabel jl3 = new JLabel("TaxonomyId");

		jps11=new JPanel();
		jps111=new JPanel();
		//jps12=new JPanel();
		jps11.setLayout(new GridLayout(2,1));
		jps111.setLayout(new BorderLayout());
		//jps12.setLayout(new GridLayout(2,1));

		jps11.add(jl1);
		jps111.add(jl2,BorderLayout.WEST);
		jps111.add(speciesJcb,BorderLayout.CENTER);
		jps11.add(jps111);
		//jps12.add(jl3);
		
		this.setLayout(new BorderLayout());
		this.add(jps11,BorderLayout.CENTER);
		
		//this.add(jps12,BorderLayout.CENTER);
	}
	public static String getSpeciesID() {
		return SpeciesID;
	}
	private String myBox(ActionEvent evt) {
		// TODO Auto-generated method stub
		if (speciesJcb.getSelectedItem() != null) {
			String text=speciesJcb.getSelectedItem().toString();
			String[] taxID=text.split("\t");

			//System.out.println(Arrays.toString(taxID));
			return taxID[1];
		}
		return null;
		
	}
}