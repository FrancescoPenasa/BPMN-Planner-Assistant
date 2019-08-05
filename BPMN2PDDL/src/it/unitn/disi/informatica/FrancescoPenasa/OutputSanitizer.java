/**
 * 
 */
package it.unitn.disi.informatica.FrancescoPenasa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

/**
 * @author lithium
 *
 */
public class OutputSanitizer {

	List<List<List<String>>> plans = new ArrayList<List<List<String>>>();
	
	/**
	 * cerca il file output, perch[ non e' detto che sia uguale a quello immesso nel planner alcuni planner
	 * generano file tipo output.txt1 output.txt2. PRENDO sempre il primo (o l'ultimo devo ancora decidere)
	 * dal file output prendo le righe piu' in basso nel file che contengono delle parentesi e sono consecutive
	 * pulisco le righe raccolte, raccogliendo solo il testo all'interno della parentesi
	 * metto in state_list le azioni pulite e circondate da parentesi
	 * @param output_path
	 * @param planner
	 * @throws IOException
	 */
	public OutputSanitizer(String output_path) {		
		List<File> outputs = new ArrayList<File>();
		
		// mi salvo tutti gli output ottenuti, che hanno sempre la forma "output_path_x.SOL"
		File f = null;
		boolean file_exist = true;
		int i = 1;
		while (file_exist) {
			f = new File(output_path + "_" + i + ".SOL");
			if(f.exists() && !f.isDirectory()) { 
			    outputs.add(f);
			    i++;
			} else {
				file_exist = false;
			}
		}
		
		
		
		
		
		List<List<String>> times = new ArrayList<List<String>>();
		List<String> states = null;
		
		String line;
		String state;
		int from; 
		int to;
		int time;
		
		// per ogni file di output mi salvo il piano
		for (File plan : outputs) {
			times = new ArrayList<List<String>>();
			
			try (BufferedReader br = new BufferedReader(new FileReader(plan))) {
				while((line = br.readLine()) != null) {
					
					// se e' un commento o una linea vuota non faccio nulla 
					if (line.startsWith(";") || line.length() < 2) {
					} 
					
					// mi salvo il tempo di esecuzione e il nome dellazione
					else {
						time = Integer.valueOf(String.valueOf(line.charAt(0)));		
						from = line.indexOf("(") + 1; 
						to = line.indexOf(")");
						state = line.substring(from, to);
												
						if (time == times.size()) {
							states = new ArrayList<String>();
							states.add(state);
							times.add(states);
							
						} else {
							times.get(time).add(state);							
						}
					}
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			plans.add(times);
			times = null;
		}
		
		
		
	
	
	}
	
	




	/**
	 * get states
	 * @return
	 */
	public List<List<List<String>>> getPlans() {
		return this.plans;
	}
	


	// TODO 
	public String getMetrics() {
		return null;
	}
		
	

}
