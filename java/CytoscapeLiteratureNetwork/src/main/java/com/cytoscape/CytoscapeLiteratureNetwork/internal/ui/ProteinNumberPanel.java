package com.cytoscape.CytoscapeLiteratureNetwork.internal.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.TaskMonitor;

public class ProteinNumberPanel extends JPanel{
	
	String initialEntityNumber="100";
	String EntityNumber;
	JTextField jtf_protein_number;
	int maxAddNodes=2000;
	
	final JSlider jslid_protein_number;
	public ProteinNumberPanel(){
		
		JLabel jl_protein_number = new JLabel("Maxinum number of proteins: ");
		
		jslid_protein_number = new JSlider(0, maxAddNodes, 40);
		jslid_protein_number.setMinorTickSpacing(10);	
		jslid_protein_number.setMajorTickSpacing(200);		
		jslid_protein_number.setPaintLabels(true);
		
		jtf_protein_number = new JTextField(4);
		jtf_protein_number.setBounds(140, 70, 20,30);
		jtf_protein_number.setText(initialEntityNumber);
		
		jslid_protein_number.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
            	jtf_protein_number.setText(String.valueOf(jslid_protein_number.getValue()));
            }
        });

		jtf_protein_number.addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent ke) {
                String typed = jtf_protein_number.getText();
                jslid_protein_number.setValue(0);
                if(!typed.matches("[0-9]+") || Integer.parseInt(typed)>2000|| Integer.parseInt(typed)<0) {
                	
            		jtf_protein_number.setBackground(Color.RED);
            		JOptionPane.showMessageDialog(null, 
            				                          "Please enter a number of additional nodes between 0 and " + maxAddNodes, 
            											            "Alert", JOptionPane.ERROR_MESSAGE);
            		return;
                }
                int value = Integer.parseInt(typed);
                jslid_protein_number.setValue(value);
                jtf_protein_number.setBackground(Color.WHITE);
            }
        });
		
		this.EntityNumber=jtf_protein_number.getText();
		this.setLayout(new BorderLayout(5,5));
		this.add(jl_protein_number,BorderLayout.WEST);
        this.add(jslid_protein_number,BorderLayout.CENTER);
        this.add(jtf_protein_number,BorderLayout.EAST);
        this.setBorder(BorderFactory.createEtchedBorder()); 
	}
	public String Get_entity_number(){
		
		return EntityNumber;
	}
	public void Initial_entity_number() {
		 jslid_protein_number.setValue(Integer.parseInt(initialEntityNumber));
		 jtf_protein_number.setText(initialEntityNumber);
	}
	
	
}
