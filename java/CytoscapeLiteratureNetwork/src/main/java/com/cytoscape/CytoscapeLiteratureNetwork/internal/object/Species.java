package com.cytoscape.CytoscapeLiteratureNetwork.internal.object;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.print.DocFlavor.URL;

public class Species {
	private static List<Species> allSpecies;
	private static Map<Integer, Species> taxIdSpecies;
	private int taxon_id;
	private String officialName;


	public Species(String line) {
		if (line.startsWith("#")) {
			this.taxon_id=0;
			this.officialName="";
			return;
		}
		
		String columns[] = line.trim().split("\t");
		if (columns.length >= 4)
			try {
				int tax = Integer.parseInt(columns[0]);
				//System.out.println(tax);
				init(tax,  columns[3].trim());
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
				init(0, columns[3].trim());
			}


	}

	public int getTaxId() { return taxon_id; }

	public String getOfficialName() { return officialName; }


	public static List<Species> getSpecies() {
		return allSpecies;
	}

	private void init(int tax, String oName) {
		this.taxon_id = tax;
		this.officialName = oName;
		
	}
	public static List<Species> readSpecies() throws Exception {
		String speciesdir="/species_viruses_string11.txt";
		allSpecies = new ArrayList<Species>();
		InputStream stream = null;
		try {
			java.net.URL resource = Species.class.getResource(speciesdir);	
			stream = resource.openConnection().getInputStream();

		} catch (Exception e) {
			e.printStackTrace();
		}		
		try (Scanner scanner = new Scanner(stream)) {

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();

				if(line.startsWith("#") || line.trim().isEmpty())
					continue;
				
				Species s = new Species(line);

				if (s.officialName != null) {
					allSpecies.add(s);
					//taxIdSpecies.put(new Integer(s.getTaxId()), s);
				}
			}
			scanner.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return allSpecies;
	}
}
