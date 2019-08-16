package it.unitn.disi.informatica.FrancescoPenasa;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;

/**
 * main class
 * @author lithium
 *
 */
public class HelloBPMN {
	
	//======================== PARAMETERS =============================//
	
	//-------------------------- private ----------------------------//
	// mandatory input parameters
	private static String bpmn_url = "";	
	private static String domain_url = "";		
	private static String planner_url = "";
	
	private static String from = "";
	private static String pddl_path = "";
	
	// optional input parameters
	private static String problem_max_conditions = "";
	private static String problem_min_conditions = "";
	
	
	// ====================================== METHODS ==========================================//
	
	// ---------------------------------- private methods ------------------------------------- //
	/**
	 * 
	 * @param url
	 * @return
	 */
	private static String get_domain_name(String url) {
		String domain = null;
		try (BufferedReader br = new BufferedReader(new FileReader(url))) {
			String line;
			while((line = br.readLine()) != null) {
				if (line.contains("domain")) {
					int i = line.indexOf("domain");
					domain = line.substring(i, line.length()-1);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		domain = domain.replaceAll("domain", "");
		domain = domain.replaceAll(" ", "");
		return domain;
	}
		
		
	/**
	 * input handler 
	 * @param args
	 */
	private static void input_manager(String[] args) {
		for (int i = 0; i< args.length; i++){
			String arg = args[i];
			switch (arg) {
			case "-b":
			case "--bpmn":
				bpmn_url = args[++i];
				break;			
				
			case "-d":
			case "--domain":
				domain_url = args[++i];
				break;
				
			case "-p":
			case "--planner":
				planner_url = args[++i];
				break;
				
			case "-max":
				problem_max_conditions = args[++i];
				break;
				
			case "-min":
				problem_min_conditions = args[++i];
				break;	
				
			case "-f":	
			case "--from":
				from = args[++i];
				break;	
				
			case "-pddl":
				pddl_path = args[++i];
				break;	

			
			case "-h":
			case "--help":
				help();
				break;
				
			case "--DEBUG":
				DEBUG();
				break;
				
			default:
				break;
			}
			
		}
		check_mandatory_input();
	}

	
	/**
	 * check emptyness of mandatory inputs
	 */
	private static void check_mandatory_input() {
		if (bpmn_url.isEmpty() || domain_url.isEmpty() || pddl_path.isEmpty() || planner_url.isEmpty()  || from.isEmpty()) {
			System.err.println("error: wrong usage, use java BPMN_PDDL --help to see the correct usage");
		}
	}
	
	
	/**
	 * show help page
	 */
	private static void help() {
		System.out.println(" \n"
				+ "BPMN_PDDL version 0.1 \n" 
				+ "command line:  ./java BPMN_PDDL"
				+ "\n"
				+ "BPMN_PDDL command line arguments: \n"
				+ "------------- mandatory arguments -------------"				
				+ "-b  || --bpmn 	-> to specify the bpmn2 file \n"
				+ "-d  || --domain  -> to specify fact file \n"
				+ "-p  || --planner -> to specify planner executable file and the syntax to use for it (ex. \"/home/user/dev/planners/blackbox -f domain0 -o prob0 -g output0\") \n"
				+ "-f  || --from 	-> to specify the id of the FlowNode that failed in bpmn2 \n"
				+ "--pddl 			-> to specify the directory that contains all the descritions in pddl syntax of bpmn2 elements \n"
				+ "\n"
				+ "------------- optional arguments -------------"
				+ "-h  || --help 	-> for this list \n"
				+ "-max -> to specify the conditions that you want to maximize (ex. \"(fuel)\")\n"
				+ "-min -> to specify the conditions that you want to minimize (ex. \"(+ (time) (price))\"\n"
				+ "\n"
				+ "\n"
				+ "Examples: \n"
				+ "./java BPMN_PDDL -b /home/log/fails/hanoi.bpmn2 -d /home/domains/hanoi_domain.pddl -p \"/home/planners/popf2/plan domain0 prob0 output0\" -f Task_1 \n -pddl /home/prob/hanoi/"
				+ "\n"	
				+ "./java BPMN_PDDL -b /home/log/fails/hanoi.bpmn2 -d /home/domains/hanoi_domain.pddl -p \"/home/planners/popf2/plan domain0 prob0 output0\" -f Task_1 \n -pddl \"/home/prob/hanoi/"
				+ " -min \"(total cost)\" "
				+ "\n"	
				+ "\n");
		System.exit(0);
	}
	
	
	
	// ===================================== main methods ===================================== //
	/**
	 * CALLS ALL THE CLASSES
	 * @param args
	 * @throws CoreException
	 * @throws IOException
	 */
	public static void main(String[] args) {
		
		/* manage input */
		input_manager(args);
		
		/* get domain name */ 
		String domain = get_domain_name(domain_url);
	
		/* extract bpmn */
		Bpmn2Java bpmn = new Bpmn2Java();
		bpmn.init(bpmn_url);	
				
		/* find all the possible dst nodes and set them in an ordered list, the order is the priority */
		TrovaNodi tv = new TrovaNodi(bpmn, from);
		//[["StartEvent_1"], ["Task_1"], ["Task_2", "Task_3", "ExclusiveGateway_1"], ["EndEvent_1"]]
		List<List<String>> possible_dst_nodes = tv.getOutputs(); //
			
		
		ProblemGenerator pg = null;
		Planner planner = null;
		OutputSanitizer ov = null;		
		int DISTANCE = 20;
		List<List<List<List<String>>>> plans = new ArrayList<List<List<List<String>>>>();; // exclusive_bpmn, exclusive, time, actions
		List<String> dst_nodes = new ArrayList<String>();
		
		/* watch all the possible dst nodes */
		boolean test = true;
		for (int i = 1; i < possible_dst_nodes.size() && test; i++) { // starting from 1 because 0 is the from node

			List<Boolean> exit_condition = new ArrayList<Boolean>();

			// generate a problem file and an output file for every node existing in the list node_list, a
			for (int j = 0; j < possible_dst_nodes.get(i).size(); j++){
				// generate problem.pddl file
				try {
					System.out.println("generating problem for : " + from + " -> " + possible_dst_nodes.get(i));
					pg = new ProblemGenerator(domain, "/home/lithium/inputs/input0-files/Task_1", "/home/lithium/inputs/input0-files/" + possible_dst_nodes.get(i).get(j));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String problem_url = pg.getUrl();
				
				// generate output.SOL file using the planner
				planner = new Planner(planner_url, domain_url, problem_url);
				// valutate the output if it works
				ov = new OutputSanitizer (planner.getOutputURL());
				if (ov.getValidity(DISTANCE)) {// funzione che mi restituisce vero o false in base al parametro e alla lunghezza degli stati
					exit_condition.add(true);
					System.err.println("im in"+ ov.getPlans());
					plans.add(ov.getPlans());
					System.out.println("plan added" + ov.getPlans());
					dst_nodes.add(possible_dst_nodes.get(i).get(j));
					test = false;
				}
			}						
		}
		
			

		/* creo un nuovo bpmn2 con i nuovi stati */
		MyFile.createBackup(bpmn_url, true);
		BpmnUpdater bu = null;
		
		for (int i = 0; i<plans.size(); i++) {
			MyFile.createBackup(bpmn_url, false);
			System.err.println(dst_nodes.get(i));
			System.err.println(plans.get(i));
			bu = new BpmnUpdater(plans.get(i), bpmn, from, dst_nodes.get(i));			
		}
		
	}
	
	
	
	
	
	// ====================================== DEBUG =========================================== //
	
	/**
	 * JUST PRINT TO UNDERSTAND IF INPUT IS WHATS INTENDED
	 */
	private static void DEBUG() {
		System.err.println("DEBUG MODE: ON");
	}
}
	
