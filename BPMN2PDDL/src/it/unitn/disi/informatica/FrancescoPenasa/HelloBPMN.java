package it.unitn.disi.informatica.FrancescoPenasa;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
	
	private static String domain_name = "";
	private static String domain_url = "";
	
	private static String problem_obj = "";
	private static String problem_init = "";
	private static String problem_goal = "";
	
	private static String planner_url = "";
	
	private static String from = "";
	
	// optional input parameters
	private static String problem_max_conditions = "";
	private static String problem_min_conditions = "";
	private static String to = null;
	
	public static boolean metrics_usage = false;
	public static String limitator = "parenthesis";
	
	// =========================== METHODS ============================//
	
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
			
			case "-i":
			case "--init":
				problem_init = args[++i];
				break;
			
			case "-o":
			case "--obj":
				problem_obj = args[++i];
				break;
				
			case "-g":
			case "--goal":
				problem_goal = args[++i];
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

			case "-t":	
			case "--to":
				to = args[++i];
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
	 * check that mandatory input parameters aren't empty
	 */
	private static void check_mandatory_input() {
		if (bpmn_url.isEmpty() || domain_url.isEmpty()
				|| problem_goal.isEmpty() || problem_init.isEmpty() || problem_obj.isEmpty()
				|| planner_url.isEmpty()  || from.isEmpty()) {
			System.err.println("wrong usage, use java bpmnpddl --help to see the correct usage");
		}
	}
	
	
	/**
	 * show help page
	 */
	private static void help() {
		System.out.println(" \n"
				+ "introduzione.\n"
				+ "\n"
				+ "-h  || --help -> print this\n"
				+ "-b  || --bpmn -> url of the bpmn2 file\n"
				+ "-d || --domain  -> url of the pddl file that describe the domain\n"
				+ "-o  || --obj 	-> objects to use in PDDL problem file that will be generated (ex. \"right - gripper left - gripper b - box\")\n"
				+ "-i  || --init    -> init states to use in PDDL problem file that will be generated (ex. \"(and (at b roomA) (holding left b))\")\n"
				+ "-g  || --goal    -> goal states to use in PDDL problem file that will be generated (ex. \"(and (at b roomB) (not (holding left b)))\") \n"
				+ "-p  || --planner -> url where to find the planner to use and the syntax to use (ex. \"/home/user/dev/planner/popf2/plan domain0 prob0 output0\")\n"
				+ "-f  || --from -> id of the baseelement that failed in bpmn2\n"
				+ "-t  || --to -> id of the baseelement that you wish to reach with the new states in bpmn2 (must be compilance with the --goal states) \n"
				+ "-max -> conditions that you want to maximize (ex. \"(fuel)\")\n"
				+ "-min -> conditions that you want to minimize (ex. \"(time) (price)\"\n"
				+ "\n"
				+ "\n"
				+ "Usage: -b /home/log/fails/hanoi.bpmn2 -d $path/hanoi_domain.pddl \n"
				+ "-o \"left mid right d1 d2 d3\" -i \"(on d3 left) (on d2 d3) (on d1 d2)\" -g \"(and (on d3 right) (on d2 d3))\"\n"
				+ "-p \"/home/user/dev/planner/popf2/plan domain0 prob0 output0\" --from Task_1 \n"
				+ "\n"	
				+ "Usage: -b /home/log/fails/rocket.bpmn2 -d /home/dev/pddl/rocket_domain.pddl \n"
				+ "-o \"left mid right d1 d2 d3\" -i \"(on d3 left) (on d2 d3) (on d1 d2)\" -g \"(and (on d3 right) (on d2 d3))\"\n"
				+ "-p \"/home/user/dev/planner/blackbox -o domain0 -f prob0  -g output0\" --from Task_1 --to EndEvent_1 \n"
				+ "-min \"(total-cost)\"\n"	
				+ "\n");
		System.exit(0);
	}
	
	
	//=========================== MAIN METHOD ===========================================//
	/**
	 * CALLS ALL THE CLASSES
	 * @param args
	 * @throws CoreException
	 * @throws IOException
	 */
	public static void main(String[] args) {
			
//		/* manage input */
//		input_manager(args);
//		
//		/* get domain name */ 
//		domain_name = get_domain_name(domain_url);
//		
//		/* extract bpmn */
		Bpmn2Java bpmn = new Bpmn2Java();
		bpmn_url = "/home/lithium/dev/eclipse-workspace/bpmnCollection/rocket.bpmn2";
		bpmn.init(bpmn_url);
//		
//		/* generate problem */ 
//		ProblemGenerator pr = null;
//		if (problem_max_conditions.length() == 0 && problem_min_conditions.length() == 0) {
//			try {
//				pr = new ProblemGenerator (domain_name, problem_obj, problem_init, problem_goal);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		else {
//			try {
//				pr = new ProblemGenerator (domain_name, problem_obj, problem_init, problem_goal, problem_max_conditions, problem_min_conditions);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}		
//		}
//		String problem_url = pr.getUrl();
//		
//		/* execute the planner on the domain and problem file and create an output file */
//		Planner planner = new Planner(planner_url, domain_url, problem_url);
//		String output_url = planner.getOutputURL();
		
		
		
		
		// TEST
		String output_url = "/home/lithium/benchmark/outputsanitize/out";
		from = "Task_1";
		to = null;
		
		/* sanitize output file from unwanted data */ 
		OutputSanitizer ov = new OutputSanitizer (output_url);
		List<List<List<String>>> plans = ov.getPlans();
		String metrics = ov.getMetrics();		
		
		
		/* creo un nuovo bpmn2 con i nuovi stati */
		MyFile.createBackup(bpmn_url);
		BpmnUpdater bu = new BpmnUpdater(plans, bpmn, from, to);			
//		
//		
//		/* print */ 
//		System.out.println("finito");
//		System.out.println("piano completato con " + new_states.size() + " stati.");
//		if (metrics != null) {
//			System.out.println("cost totale " + metrics + ".");
//		}
//		System.out.println("aggiunt " + new_states.size() + " task a bpmn.");	
	}
	
	
	
	
	private static String get_domain_name(String url) {
		String domain = null;
		try (BufferedReader br = new BufferedReader(new FileReader(url))) {
			String line;
			while((line = br.readLine()) != null) {
				if (line.contains("domain")) {
					int i = line.indexOf("domain");
					domain = line.substring(i, line.length());
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


	// =========================== DEBUG ============================//
	/**
	 * JUST PRINT TO UNDERSTAND IF INPUT IS WHATS INTENDED
	 */
	private static void DEBUG() {
		System.err.println("DEBUG MODE: ON");
	}
}
	
