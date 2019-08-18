package it.unitn.disi.informatica.FrancescoPenasa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author FrancescoPenasa
 * 
 */
public class OutputSanitizer {

	// ===================================== PARAMETERS ======================================= //
	
	// --------------------------------------- private ---------------------------------------- //	
	private List<List<List<String>>> plans = new ArrayList<List<List<String>>>();
	private boolean no_solution = false;
	
	
	public OutputSanitizer() {}
	
	// ====================================== METHODS ========================================= //
	
	// --------------------------------------- public ----------------------------------------- //
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
	public void sanitize(String output_path) {		
		List<File> outputs = new ArrayList<File>();
		no_solution = false;
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
		
		System.out.println("times:" + output_path); 
		
		for (File plan : outputs) {
			times = new ArrayList<List<String>>();
			
			try (BufferedReader br = new BufferedReader(new FileReader(plan))) {
				while((line = br.readLine()) != null) {
					
					// se e' un commento o una linea vuota non faccio nulla 
					if (line.startsWith(";") || line.length() < 2) {
					} 
					
					// mi salvo il tempo di esecuzione e il nome dellazione
					else if (line.contains("no solution")){
						no_solution = true;
						System.out.println("\n NO SOLUTION \n");
					}
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
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			plans.add(times);
		}		
	}
	
	
	/**
	 * return parsed plan that contains the actions to upgrade the bpmn2 file
	 */
	public List<List<List<String>>> getPlans() {
		return this.plans;
	}


	// TODO 
	public String getMetrics() {
		return null;
	}
	
	/**
	 * clear all the class parameters
	 */
	public void clear() {
		plans.clear();
		no_solution = false;
	}


	/**
	 * get validity of the output file
	 * if the output contains "no solution" or number of actions are more than DISTANCE return false
	 * else return true
	 * @param DISTANCE max number of actions.
	 * @return true if valid, false if not valid
	 */
	public boolean getValidity(int DISTANCE) {
		for (List<List<String>> plan : plans) {
			if (plan.size() > DISTANCE)
				return false;
		}
		if (no_solution || plans.isEmpty()) {
			return false;
		}
		return true;
	}		
}
