package it.unitn.disi.informatica.FrancescoPenasa;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.InclusiveGateway;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.ParallelGateway;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.Task;

class ProblemGenerator {
	
	private FileWriter writer = null;
	final private String DIR = "";


	/**
	 * 
	 * @param domain
	 * @param objects
	 * @param init
	 * @param goals
	 * @throws IOException
	 */
	public ProblemGenerator(String domain, String objects, String init, String goals) throws IOException {
		
		// open the writer 
		OutputWriter w = new OutputWriter (domain + "_prob0.pddl");
		writer = w.getFileWriter();
		
		// start writing the prob file
		// until (:domain ; first 4 rows
		writeIntro(domain);
		
		//(:objects
		writeObjects(objects);
		
		//init state
		writeInit(init);
		
		//goal state
		writeGoal(goals);
		
		//last parenthesiS
		writeOutro();
		
		writer.flush();
		writer.close();
		
		System.out.flush();	
		System.out.println("Standard Prob generator finished!");
	}
	

	/**
	 * constructor n.2 the one with the 
	 * @param domain
	 * @param objects
	 * @param init
	 * @param goals
	 * @param costrain
	 * @param condition
	 * @throws IOException
	 */
	public ProblemGenerator(String domain, String objects, String init, String goals, String [] costrains, boolean [] condition) throws IOException {
		
		// open the writer 
		OutputWriter w = new OutputWriter (domain + "_prob0.pddl");
		writer = w.getFileWriter();
		
		// start writing the prob file
		// until (:domain ; first 4 rows
		writeIntro(domain, costrains);
		
		//(:objects
		writeObjects(objects);
		
		//init state
		writeInit(init, costrains);		

		//goal state
		writeGoal(goals);
		
		//goal state
		writeMetric(costrains, condition);
		
		//last parenthesiS
		writeOutro();
		
		writer.flush();
		writer.close();
		
		System.out.flush();	
		System.out.println("Constrains Prob generator finished!");
	}


	/**
	 * write definition and requirements on the domain file
	 * @throws IOException
	 */
	private void writeIntro(String DOMAIN) throws IOException {		
		writer.write(";; problem file: " + DOMAIN + "_prob0.pddl \n\n");		
		writer.write("(define (problem " + DOMAIN + "_prob0)\n");
		writer.write("\t(:domain " + DOMAIN + ")\n\n");
	}
	
	/**
	 * write definition and requirements on the domain file
	 * @throws IOException
	 */
	private void writeIntro(String DOMAIN, String [] costrains) throws IOException {	
		String constrain = "";
		for (int i = 0; i < costrains.length; i++) {
			constrain += costrains[i].replaceAll(" ", "_");
		}
		writer.write(";; problem file: " + DOMAIN + "_" + constrain + ".pddl \n\n");		
		writer.write("(define (problem " + DOMAIN + "_" + constrain + ")\n");
		writer.write("\t(:domain " + DOMAIN + ")\n\n");
	}
	
	
	//-------------------- INIT -------------------///
	// (at rabbit a) (at carrot b) (at box c)
	/**
	 * 
	 * @param INIT
	 * @throws IOException
	 */
	private void writeInit(String INIT) throws IOException {
		writer.write("\t(:init\n");
		writer.write("\t\t" + INIT + "\n");
		writer.write("\t)\n\n");
	}
	
	//-------------------- INIT -------------------///
	// (at rabbit a) (at carrot b) (at box c)
	/**
	 * 
	 * @param INIT
	 * @throws IOException
	 */
	private void writeInit(String INIT, String [] costrains) throws IOException {
		writer.write("\t(:init\n");
		writer.write("\t\t" + INIT + "\n");
		for (int i = 0; i < costrains.length; i++) {
			writer.write("\t\t(= " + costrains[i] + "0)\n");
		}
		writer.write("\t)\n\n");
	}
	
	
	//------------------------ OBJECTS ------------------------//
	// OBJ should be something like carrot rabbit box a b c
	/**
	 * 
	 * @param OBJ
	 * @throws IOException
	 */
	private void writeObjects(String OBJ) throws IOException {
		writer.write("\t(:objects\n");
		writer.write("\t\t" + OBJ + "\n");
		writer.write("\t)\n\n");
	}
	

	

	/**
	 * 
	 * @throws IOException
	 */
	private void writeOutro() throws IOException {
		final String OUTRO = new String(")");
		writer.write(OUTRO);
	}

	
	//-------------- GOAL ----------------------------//
	private void writeGoal(String goal) throws IOException {
		writer.write("\t(:goal\n");
		writer.write("\t\t" + goal + "\n");
		writer.write("\t)\n");
	}

	private void writeMetric(String[] costrains, boolean [] condition) throws IOException {
		writer.write("\t(:metric \n");
		
		for (int i = 0; i < costrains.length; i++) {
			if (condition[i]) {
				writer.write( "\t\t(minimize (" + costrains[i] + "))\n");
			}
			else {
				writer.write( "\t\t(maximize (" + costrains[i] + "))\n");
			}
		}
		
		writer.write("\t)\n");		
	}


	public Object getPath() {
		// TODO Auto-generated method stub
		return "pat";
	}
}