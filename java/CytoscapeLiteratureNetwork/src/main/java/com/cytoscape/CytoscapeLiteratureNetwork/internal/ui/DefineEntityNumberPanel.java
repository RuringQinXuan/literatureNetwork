package com.cytoscape.CytoscapeLiteratureNetwork.internal.ui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

public class DefineEntityNumberPanel extends JPanel{
	String initialEntityNumber="100";
	String EntityNumber;
	

	
	JTextField jtfSlider;
	public DefineEntityNumberPanel(){
		
		JLabel jlSlider = new JLabel("Maxinum number of proteins: ");
		
		final JSlider sliderEntityNumber = new JSlider(0, 100, 40);
		sliderEntityNumber.setMinorTickSpacing(1);	
		sliderEntityNumber.setMajorTickSpacing(10);		
		sliderEntityNumber.setPaintTicks(true);
		
		 jtfSlider = new JTextField(2);
		jtfSlider.setText(initialEntityNumber);
		
		this.EntityNumber=jtfSlider.getText();
		
		this.setLayout(new BorderLayout(5,5));
		this.add(jlSlider,BorderLayout.WEST);
        this.add(sliderEntityNumber,BorderLayout.CENTER);
        this.add(jtfSlider,BorderLayout.EAST);
        this.setBorder(BorderFactory.createEtchedBorder()); 
	}
	public String Get_entity_number(){
		
		return EntityNumber;
	}
	public String getEntityNumber() {
		return EntityNumber;
	}
}