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
	
	// optional input parameters
	public static String problem_max_conditions = "";
	public static String problem_min_conditions = "";
		
	
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
			case "--planner-url":
				planner_url = args[++i];
				break;
				
			case "-max":
				problem_max_conditions = args[++i];
				break;
				
			case "-min":
				problem_min_conditions = args[++i];
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
				|| planner_url.isEmpty()) {
			System.err.println("wrong usage, use java bpmnpddl --help to see the correct usage");
		}
	}
	
	
	/**
	 * show help page
	 */
	private static void help() {
		System.out.println("stampare papiro di informazioni; necessario planner con supporto a :strips, a :fluents per "
				+ "le best conditions e a tutti i requirements specificati nel pddl domain input ovviamente."
				+ ""
				+ "-h || --help -> print this"
				+ "-b || --bpmn -> url of the bpmn2 file"
				+ "-dn || --domain-name -> name of the domain (ex. -dn hanoi), you can find it in the pddl domain file after (define (domain $DOMAIN)"
				+ "-du || --domain-url  -> url of the pddl file that describe the domain"
				+ "-o || --obj 	-> objectsto use in PDDL problem fileS that will be generated"
				+ "-i || --init -> "
				+ "-g || --goal -> "
				+ "-p || --planner-url -> url where to find the planner to use"
				+ ""
				+ "-max -> conditions that you want to maximize (ex. \"(fuel)\")"
				+ "-min -> conditions that you want to minimize (ex. \"(time) (price)\""
				+ ""
				+ "Usage: -b /home/log/fails/hanoi.bpmn2 -dn hanoi -du $path/hanoi_domain.pddl "
				+ "-o \"left mid right d1 d2 d3\" -i \"(on d3 left) (on d2 d3) (on d1 d2)\" -g \"(and (on d3 right) (on d2 d3)\""
				+ "-p \"/home/user/dev/planner/popf2/plan domain0 prob0 output0\""
				+ ""	
				+ "Usage: -b /home/log/fails/rocket.bpmn2 -dn rocket -du /home/dev/pddl/rocket_domain.pddl "
				+ "-o \"left mid right d1 d2 d3\" -i \"(on d3 left) (on d2 d3) (on d1 d2)\" -g \"(and (on d3 right) (on d2 d3)\""
				+ "-p \"/home/user/dev/planner/blackbox -o domain0 -f prob0  -g output0\""
				+ "-min \"(total-cost)\""	
				+ "");
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
		//input_manager(args);
		
		/* extract bpmn */
		//BpmnToJava bpmn = new BpmnToJava(bpmn_url);
		Bpmn2Java bpmn = new Bpmn2Java("/home/lithium/dev/eclipse-workspace/bpmnCollection/simple.bpmn2");
		
		/* ------------------------- PHASE 1 ------------------------ */
		/* ----- CREATE A PROBLEM PDDL FILE FOR THE EASY TASK ------- */ 
		/* ---------------------------------------------------------- */
		
		// input ex: ("pippo", "robot1 - r robot2 - t robot3", "(at robot1) (has robot2 robot3)", "(to roboton)");
		//ProblemGenerator pr = new ProblemGenerator (domain_name, problem_obj, problem_init, problem_goal);				
		//String problem_url = pr.getUrl();
		
		/* execute the planner on the domain and problem file and create an output file */
		// input ex: ("/home/lithium/dev/bpmnAndPddlEx/pddl/blackbox", "/home/lithium/dev/bpmnAndPddlEx/pddl/exercise/rocket_domain.pddl", 
		//		"/home/lithium/dev/bpmnAndPddlEx/pddl/exercise/rocket_prob.pddl", "/home/lithium/rocket_sol.txt", "blackbox");
		//Planner planner = new Planner(planner_url, domain_url, problem_url);
		//String output_url = planner.getOutputURL();
		
		// testing
		String output_url = "/home/lithium/dev/eclipse-workspace/bpmnCollection/simple.bpmn2";
		//---
		
		/* sanitize output file from unwanted data */ 
		OutputSanitizer ov = new OutputSanitizer (output_url, domain_url);
		
		// testing//
		List<String> lis = new ArrayList<String>();
		lis.add("(gira_le_uova)");
		lis.add("(fai_frittata)");
		String url = "/home/lithium/dev/eclipse-workspace/bpmnCollection/simple.bpmn2";
		String init = "Task_1";
		String to = "Task_3";	
		//---
		
		/* creo un nuovo bpmn2 con i nuovi stati */
		BpmnUpdater bu = new BpmnUpdater(lis, bpmn, url, init, to);			
		
		
		/* print */ 
		System.out.println("finito");
		System.out.println("piano completato con " + lis.size() + " stati.");
		System.out.println("cost totale " + lis.size());
		System.out.println("aggiunt " + lis.size() + " task a bpmn.");
	
		/* STAMPO LA FINE */		
	}
	
	// =========================== DEBUG ============================//
	/**
	 * JUST PRINT TO UNDERSTAND IF INPUT IS WHATS INTENDED
	 */
	private static void DEBUG() {
		System.err.println("DEBUG MODE: ON");
	}
}
	
