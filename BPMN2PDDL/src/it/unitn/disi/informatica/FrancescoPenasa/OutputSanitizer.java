/**
 * 
 */
package it.unitn.disi.informatica.FrancescoPenasa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lithium
 *
 */
public class OutputSanitizer {

	List<String> states_list = new ArrayList<String>();
	
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
	public OutputSanitizer(String output_path, String domain_path) {
		
		// collect actions from domain 
		// TODO List<String> actions = collectActions(domain_path);
		
		// verifica quanti output ci sono --> soluzione per il fatto che alcuni planner producono pi\ output
		//output_path = parseOutputPath(output_path);
 
		
		List<String> states = null;
		
		boolean reading = false;
	    //This loop will read lines 
		try (BufferedReader br = new BufferedReader(new FileReader(output_path))) {
			String line;
			while((line = br.readLine()) != null) {
				
				// se non e' il primo stato che leggo
				if (reading) {
					if (line.contains("(") && line.contains(")")){
						states.add(line);
					} else {
						reading = false;
					}
				}
				
				// se e' il primo stato che leggo
				if (line.contains("(") && line.contains(")") && !reading){
					reading = true;
					states = null;
					states = new ArrayList<String>();	
					states.add(line);
				} 				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	
	    sanitizeStates(states);
	}
	
	
	/**
	 * pulisce le righe che dovrebbero contenere delle azioni
	 * @param states
	 */
	private void sanitizeStates(List<String> states) {
		for(String state : states){
			int open = state.lastIndexOf("(") + 1;
			int close = state.indexOf(")");
			if (open < close) {
				states_list.add("(" + state.substring(open, close) + ")");
			} else {
				System.err.println("non sono riuscito a trovare degli stati dal file output");
				System.exit(-11);
			}
		}
	}

	
	/**
	 * 
	 * @param output_path
	 * @return
	 */
	private String parseOutputPath(String output_path) {
		// TODO Auto-generated method stub
		return null;
	}


	private List<String> collectActions(String domain_path) {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * get states
	 * @return
	 */
	public List<String> getStates() {
		return this.states_list;
	}


	public String getMetrics() {
		return null;
	}
		
	

}
