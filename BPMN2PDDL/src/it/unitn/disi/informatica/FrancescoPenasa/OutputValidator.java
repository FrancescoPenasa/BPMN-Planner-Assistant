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
public class OutputValidator {

	final int STATE_LIMIT = 100;
	
	public OutputValidator(String output_path, String planner) throws IOException {
		List<String> lim = manage_planner(planner);
		
		FileReader fr = new FileReader(output_path); 
		
		String[] states = new String[STATE_LIMIT];
		states[0] = "";
	    int i = 0;
		
	    boolean plan = false;
		boolean intro = false;
		boolean cycle = true;
	    
		int c; 
	    //This loop will read characters and found 
		//character will be stored in b.
	    while ((c=fr.read()) != -1 && cycle) { 
	    	
	    	
	    	
	    	
	    	if (intro && cycle) {
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
	    	
	    	
	    	// if DELIMITATORE iniziale è ( cerco il primo ( per poter cominciare
	    	if (lim.get(0).equals('(')) {
	    		if ((char) c == '(') {
    			intro = true;
	    		}
	    	} else {	// se il delimitatore iniziale è diverso allora non mi devo preoccupare
	    		intro = true;
	    	}
	    
	    	
	    }
	    for (int it = 0; it < states.length; it++)
	    	if (states[it] != null) {
	    		if (! states[it].contains(";")) 
	    			System.out.print(states[it]);
	    	}
	} 
		
	

	/** find the characters that should open and close the file
	 * 
	 * @param planner
	 * @return
	 */
	private List<String> manage_planner(String planner) {
		planner = planner.toLowerCase();
		List<String>  res = new ArrayList<String>();
		
		switch (planner) {
		case "strips":
			res.add("(");
			break;
		case "sing":
			res.add("4");
			break;
		case "symple":
			res.add("");
			break;
		
			
		default:
			System.err.println("planner not supported, "
					+ "will be used the standard syntax to retrive the output -> (move a b c) ");
			break;
		}
		return res;
	}

}
