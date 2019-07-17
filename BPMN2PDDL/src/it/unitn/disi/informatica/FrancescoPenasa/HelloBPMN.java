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
	// mandatory input variables
	public static String URL = "";
	public static String SUPER_DOMAIN = "";
	public static String OBJ = "";
	public static String INIT = "";
	public static String GOAL = "";
	public static String PLANNER = "";
	public static String OUTPUT = "";
	
	// optional input
	public static String DOMAIN = "";
	public static String SUBDOMAIN = "";
	public static String BEST_CONDITIONS = "";
	
	
	
	
	
	// =========================== METHODS ============================//
	//--------------------------- input handlers ------------//
	//can easily work from cli
	private static void input_manager(String[] args) {
		for (int i = 0; i< args.length; i++){
			String arg = args[i];
			switch (arg) {
			case "-b":
			case "--bpmn":
				URL = args[++i];
				break;
			
			case "-i":
			case "--init":
				INIT = args[++i];
				break;
			
			case "-o":
			case "--obj":
				OBJ = args[++i];
				break;
			
			case "-sd":
			case "--sudomainPDDL":
				SUPER_DOMAIN = args[++i];
				
				break;
			case "-g":
			case "--goal":
				GOAL = args[++i];
				break;
						
			case "-d":
			case "--domainPDDL":
				DOMAIN = args[++i];
				break;
				
			case "-dd":
			case "--submainPDDL":
				SUBDOMAIN = args[++i];
				break;
				
			case "-bc":
			case "--goal_condition":
				BEST_CONDITIONS = args[++i];
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
		check_input_validity();
	}
	
	// check that mandatory parameters aren't empty
	private static void check_input_validity() {
		if (URL.isEmpty() || SUPER_DOMAIN.isEmpty() || OBJ.isEmpty() || INIT.isEmpty() || GOAL.isEmpty()) {
			System.err.println("wrong usage, use java bpmnpddl --help for the correct one");
		}
	}
	
	// show the help page
	private static void help() {
		System.out.println("stampare papiro di informazioni; necessario planner con supporto a :strips, a :fluents per "
				+ "le best conditions e a tutti i requirements specificati nel pddl domain input ovviamente.");
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
		//BPMNtoJava bpmn = new BPMNtoJava("/home/lithium/dev/eclipse-workspace/bpmnCollection/test.bpmn2");
		
		// ----------- PHASE 1 ---------- //
		/* CREATE A PROBLEM PDDL FILE FOR THE EASY TASK */ 
		// name of domain, string with objs, strign describing init, string describing goal
		//ProblemGenerator pr = new ProblemGenerator ("pippo", "robot1 robot2 robot3", "(at robot1) (has robot2 robot3)", "(to roboton)");
		//ProblemGenerator pr = new ProblemGenerator (DOMAIN, OBJ, INIT, GOAL);		
		
		/* execute the planner on the domain and problem file and create an output file */
		//Planner planner = new Planner(PLANNER, DOMAIN, PROBLEM, OUTPUT, type PLANNER);
		/*Planner planner = new Planner("/home/lithium/dev/bpmnAndPddlEx/pddl/blackbox", 
				"/home/lithium/dev/bpmnAndPddlEx/pddl/exercise/rocket_domain.pddl", 
				"/home/lithium/dev/bpmnAndPddlEx/pddl/exercise/rocket_prob.pddl", "/home/lithium/rocket_sol.txt", "blackbox");
		*/
		/* watch the output file */ 
		//-------------------------------//
		//(OUTPUT, type PLANNER);
		OutputValidator ov = new OutputValidator ("/home/lithium/rocket_sol.txt", "sing");

		/* stampo la riuscita o meno della situa */ 
		
		
		/* creo un nuovo bpmn2 con il risultato */
		
		
		//  ----------- PHASE 2 ------------ //
		/* genero un problema per ogni set di best conditions */
		// eseguo la fase uno con laggiunta delle best practice
		
		

		/* STAMPO LA FINE */
		
	}
	
	// =========================== DEBUG TIME ============================//
	/**
	 * JUST PRINT TO UNDERSTAND IF INPUT IS WHATS INTENDED
	 */
	private static void DEBUG() {
		System.out.println("URL BPMN2: " + URL);
		System.out.println("SUPER_DOMAIN: " + SUPER_DOMAIN);
		System.out.println("OBJ: " + INIT);
		System.out.println("INIT: " + INIT);
		System.out.println("GOAL: " + GOAL);
		System.out.println("DOMAIN: " + DOMAIN);
		System.out.println("SUBDOMAIN: " + SUBDOMAIN);
		System.out.println("BEST_CONDITIONS: " + BEST_CONDITIONS);
	}
}
	
