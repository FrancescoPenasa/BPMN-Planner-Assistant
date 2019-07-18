/**
 * 
 */
package it.unitn.disi.informatica.FrancescoPenasa;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lithium
 *
 */
public class OutputSanitizer {

	final int STATE_LIMIT = 100;
	List<String> states_list = new ArrayList<String>();
	private String planner;
	
	/**
	 * open the output file and collect all the states
	 * @param output_path
	 * @param planner
	 * @throws IOException
	 */
	public OutputSanitizer(String output_path, String planner) throws IOException {
		this.planner = planner;
		List<String> lim = manage_planner();
		
		FileReader fr = new FileReader(output_path); 
		
		String [] states = new String[STATE_LIMIT];
		states[0] = "";
	    int i = 0;
		
	    boolean plan = false;
		boolean reading = false;
	    
		int c; 
	    //This loop will read characters and found 
		//character will be stored in b.
	    while ((c=fr.read()) != -1) { 
	    	
	    	if (reading) {
		    	// find the first action
		    	if ((char) c == '(' || plan) {
		    		plan = true;
			    	states[i] += (char) c;
			    	
			    	// se finisce un azione passa all'elemento successivo dell'arrey;
			    	if ((char) c == ')') {
			    		states[++i] = "\n";
			    		plan = false;
			    	}
		    	}
	    	}  	    	
	    	
	    	/* for the cases that starts with a parenthesys */ 
	    	if (lim.get(0).equals('(')) { // if DELIMITATORE iniziale è ( cerco il primo ( per poter cominciare
	    		if ((char) c == '(') {
	    			reading = true;
	    		}
	    	} else {	// se il delimitatore iniziale è diverso allora non mi devo preoccupare
	    		reading = true;
	    	}
	    }
	    
	    // export the results	    
	    export_states(states);
	    
	}
	
	
	/**
	 * get states
	 * @return
	 */
	public List<String> getStates() {
		return this.states_list;
	}
		
	

	/**
	 * export states
	 * @param states
	 */
	private void export_states(String[] states) {		
		for (int i = 0; i < states.length; i++) {
			if (states[i] != null) {
				if ((states[i].contains(";")) && this.planner == "sing") { 		// exception for singularity planner
				} else {
					this.states_list.add(states[i]);
				}
			}
		}
	}



	
	/** find the characters that should open and close the file
	 * 
	 * @param planner
	 * @return
	 */
	private List<String> manage_planner() {
		planner = planner.toLowerCase();
		List<String>  res = new ArrayList<String>();
		
		switch (planner) {
		case "strips":
			res.add("(");
			res.add(")");
			break;
		case "blackbox":
			res.add("(");
			res.add(")");
			break;
		case "sing":
			res.add("");
			res.add(";");
			break;				
		default:
			System.err.println("planner not supported, "
					+ "will be used the standard syntax to retrive the output -> (move a b c) ");
			break;
		}
		return res;
	}

}
