package com.cytoscape.CytoscapeLiteratureNetwork.internal.view;

import java.awt.event.*;
import javax.swing.event.*;
import java.awt.*;
import javax.swing.*;
import java.net.*;
import java.io.*;


public class InterfaceEntities extends JFrame{
	JButton jb1,jb2,jb3,jb4,jb5=null;
	JLabel jl1,jl2,jl3,jl4,jl5,jl6=null;
	JPanel jpResult,jpsearch,jps1,jps2,jps3=null;
	JComboBox jcb=null;
	JTextField jtd1,jtd2,jtd3;
	static void main(String[] args) {
		// TODO Auto-generated method stub
		InterfaceEntities ie =new InterfaceEntities();
	}
	public InterfaceEntities(){
		final JSlider slider1 = new JSlider(0, 100, 40);
		final JSlider slider2 = new JSlider(0, 10000, 100);
		slider1.setMajorTickSpacing(1);
		slider2.setMajorTickSpacing(5);
		this.add(slider2);
		this.add(slider1);
		this.setSize(500,600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);


	}

}


