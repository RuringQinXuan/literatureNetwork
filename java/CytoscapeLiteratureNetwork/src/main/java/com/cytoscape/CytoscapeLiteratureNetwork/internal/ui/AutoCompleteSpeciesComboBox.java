package com.cytoscape.CytoscapeLiteratureNetwork.internal.ui;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxEditor;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxEditor;

import com.cytoscape.CytoscapeLiteratureNetwork.internal.object.Species;

public class AutoCompleteSpeciesComboBox extends JComboBox{
	private static final long serialVersionUID = 1L;
	public int caretPos=0;
	public JTextField inputField=null;

	public AutoCompleteSpeciesComboBox() {
		setEditor(new BasicComboBoxEditor());
		setEditable(true);
		try {
			Species.readSpecies();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Species> speciesList1 = Species.getSpecies();
		this.addItem("HOMO SAPIEN\t9606");
		for (Species species: speciesList1) {
			this.addItem(species.getOfficialName().toUpperCase()+"\t"+species.getTaxId());
		}
		this.setEditable(true);	
		this.setPreferredSize(new Dimension(400,10));		
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
		inputField.setBounds(140, 70, 20,30);

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
