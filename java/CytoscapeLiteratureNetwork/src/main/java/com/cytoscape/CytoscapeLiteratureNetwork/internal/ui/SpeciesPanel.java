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

public class SpeciesPanel extends JPanel {
private static final long serialVersionUID = 1L;
	
	
	JComboBox speciesJcb;
	
	private static String SpeciesID="9606";
	public SpeciesPanel(){
		
		//species input part
		speciesJcb = new AutoCompleteSpeciesComboBox();
		speciesJcb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SpeciesID=myBox(evt);
			}
		});
		speciesJcb.setPreferredSize(new Dimension(400,10));
		
		//species label part
		JLabel jl_species = new JLabel("Species: ");
		
		//species panel
		this.setLayout(new BorderLayout());
		this.add(jl_species,BorderLayout.WEST);
		this.add(speciesJcb,BorderLayout.CENTER);

	}
	
	public static String getSpeciesID() {
		return SpeciesID;
	}
	
	private String myBox(ActionEvent evt) {
		// TODO Auto-generated method stub
		if (speciesJcb.getSelectedItem() != null) {
			String text=speciesJcb.getSelectedItem().toString().trim();
			String[] taxID=text.split("\t");
			//System.out.println(Arrays.toString(taxID));
			return taxID[1];
		}
		return null;
		
	}
	
}
