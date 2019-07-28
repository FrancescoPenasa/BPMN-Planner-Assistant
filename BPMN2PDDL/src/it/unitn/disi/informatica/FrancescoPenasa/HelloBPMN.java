package it.unitn.disi.informatica.FrancescoPenasa;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.util.Bpmn2ResourceFactoryImpl;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * main class
 * @author lithium
 *
 */
public class HelloBPMN {
	
	//======================== PARAMETERS =============================//
	
	//--------------------------public ----------------------------//
	// mandatory input parameters
	public static String bpmn_url = "";
	
	public static String domain_name = "";
	public static String domain_url = "";
	
	public static String problem_obj = "";
	public static String problem_init = "";
	public static String problem_goal = "";
	
	public static String planner_url = "";
	
	public static String from_state_id = "";
	
	// optional input parameters
	public static String problem_max_conditions = "";
	public static String problem_min_conditions = "";
	public static String to_state_id = null;
	
		
	
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
						
			case "-du":
			case "--domain-url":
				domain_url = args[++i];
				break;
				
			case "-dn":
			case "--domain-name":
				domain_name = args[++i];
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
				from_state_id = args[++i];
				break;	

			case "-t":	
			case "--to":
				to_state_id = args[++i];
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
		if (bpmn_url.isEmpty() || domain_name.isEmpty() || domain_url.isEmpty()
				|| problem_goal.isEmpty() || problem_init.isEmpty() || problem_obj.isEmpty()
				|| planner_url.isEmpty()  || from_state_id.isEmpty()) {
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
				+ "-dn || --domain-name -> name of the domain (ex. -dn hanoi), you can find it in the pddl domain file after (define (domain $DOMAIN)\n"
				+ "-du || --domain-url  -> url of the pddl file that describe the domain\n"
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
				+ "Usage: -b /home/log/fails/hanoi.bpmn2 -dn hanoi -du $path/hanoi_domain.pddl \n"
				+ "-o \"left mid right d1 d2 d3\" -i \"(on d3 left) (on d2 d3) (on d1 d2)\" -g \"(and (on d3 right) (on d2 d3))\"\n"
				+ "-p \"/home/user/dev/planner/popf2/plan domain0 prob0 output0\" --from Task_1 \n"
				+ "\n"	
				+ "Usage: -b /home/log/fails/rocket.bpmn2 -dn rocket -du /home/dev/pddl/rocket_domain.pddl \n"
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
	public static void main(String[] args) throws CoreException, IOException {
			
		/* manage input */
		input_manager(args);
		
		/* extract bpmn */
		Bpmn2Java bpmn = new Bpmn2Java(bpmn_url);
		
		/* generate problem */ 
		ProblemGenerator pr = new ProblemGenerator (domain_name, problem_obj, problem_init, problem_goal);				
		String problem_url = pr.getUrl();
		
		/* execute the planner on the domain and problem file and create an output file */
		Planner planner = new Planner(planner_url, domain_url, problem_url);
		String output_url = planner.getOutputURL();
				
		/* sanitize output file from unwanted data */ 
		OutputSanitizer ov = new OutputSanitizer (output_url, domain_url);
		List<String> new_states = ov.getStates();
		String metrics = ov.getMetrics();
		
		/* creo un nuovo bpmn2 con i nuovi stati */
		BpmnUpdater bu = new BpmnUpdater(new_states, bpmn, bpmn_url, from_state_id, to_state_id);			
		
		
		/* print */ 
		System.out.println("finito");
		System.out.println("piano completato con " + new_states.size() + " stati.");
		if (metrics != null) {
			System.out.println("cost totale " + metrics + ".");
		}
		System.out.println("aggiunt " + new_states.size() + " task a bpmn.");	
	}
	
	// =========================== DEBUG ============================//
	/**
	 * JUST PRINT TO UNDERSTAND IF INPUT IS WHATS INTENDED
	 */
	private static void DEBUG() {
		System.err.println("DEBUG MODE: ON");
	}
}
	
