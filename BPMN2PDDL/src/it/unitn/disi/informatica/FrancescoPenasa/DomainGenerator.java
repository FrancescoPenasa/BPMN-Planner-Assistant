/**
 * 
 */
package it.unitn.disi.informatica.FrancescoPenasa;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.Task;

/**
 * @author ubuntu
 *
 */
public class DomainGenerator {

	/**
	 * 
	 */
	
	FileWriter writer = null;
	String domainName = null;
	
	
	// used to access all the data in the bpmn file
	BPMNtoJava bpmn = null;
	
	public DomainGenerator() {
		// TODO Auto-generated constructor stub
	}
	
	public DomainGenerator(BPMNtoJava bpmn, String domainName) throws IOException {
		// init static var
		this.domainName = domainName;
		this.bpmn = bpmn;
		writer = new FileWriter(domainName + "_domain.pddl", true);
		
		// start writing the domain file
		writeIntro();
		writeTypes();
		writePredicates();
		writeActions();
		writeOutro();
		
		// testing purpose
		//boolean test = doStuff(bpmn.getAllProcess());
		
		
		// close FileWriter
		writer.flush();
		writer.close();
		
		// rosik
		System.out.flush();	
		System.out.println("Domain generator finished!");
	}
	
	
	

	//---------------------	GOOD ----------------\\
	/**
	 * Close the inital parentesis to make the domain file working
	 * @throws IOException
	 */
	private void writeOutro() throws IOException {
		final String OUTRO = new String(")");
		writer.write(OUTRO);
	}

	
	//---------------------	GOOD ----------------\\
	/**
	 * write definition and requirements on the domain file
	 * @throws IOException
	 */
	private void writeIntro() throws IOException {	
		final String INTRO = new String(";; domain file: ");
		final String END_FILE_NAME = new String("_domain.pddl \n\n");
		
		writer.write(INTRO + domainName + END_FILE_NAME);		
		writer.write("(define (domain " + domainName + ")\n");
		writer.write("\t(:requirements :typing)\n");
	}
	
	//----------------- NEED TO ADD ALL TYPES ------------------\\
	/**
	 * write the types
	 * @param allProcess
	 * @throws IOException
	 */
	private void writeTypes() throws IOException {
		
		List<String>types = new ArrayList<String>();
		types.add("state");
		types.add("task - state");
		types.add("startevent - state");
		
		writer.write("\t(:types\n");
		for (String t : types) {
			writer.write("\t\t" + t + "\n");
		}
		writer.write("\t)\n\n");
	}
	
	
	//--------------- 	NEED TO ADD ALL PREDICATES ------------\\
	private void writePredicates() throws IOException {	
		
		List<String> predicates = new ArrayList<String>();
		predicates.add("(has ?owner ?state)");
		predicates.add("(at ?state)");
		predicates.add("(linked ?state ?state)");
		
		writer.write("\t(:predicates\n");
		for (String t : predicates) {
			writer.write("\t\t" + t + "\n");
		}
		writer.write("\t)\n\n");
	}
							
	

	
	/**
	 * write actions for cases task to something with SeqFlow 
	 * and task to something with MesFlow
	 * @param processes
	 * @throws IOException
	 */
	private void writeFromTaskActions() throws IOException {
		writer.write(""
				+ "\t(:action TaskMove \n"
				+ "\t\t:parameters   (?from - state ?to - state) \n"
				+ "\t\t:precondition (and (at ?from) (not (at ?to)) (linked ?from ?to)) \n"
				+ "\t\t:effect       (and (at ?to) (not (at ?from)))) \n\n");
	}	
	
	/**
	 * write 
	 */
	private void writeGatewayActions() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * assumption: start is always in a lane and his outgoing cannot be messageflow NEVER only 
	 * sequenceFlow
	 * @throws IOException 
	 */
	private void writeStartEventActions() throws IOException {
		writer.write(""
				+ "\t(:action StartMove \n"
				+ "\t\t:parameters   (?from - state ?to - state) \n"
				+ "\t\t:precondition (and (at ?from) (not (at ?to)) (linked ?from ?to)) \n"
				+ "\t\t:effect       (and (at ?to) (not (at ?from)))) \n\n");
	}

	/**
	 * write all types of actions
	 * @throws IOException
	 */
	private void writeActions() throws IOException {
		//this isn't needed now
		//writeStartEventActions();
		
		writeFromTaskActions();
		writeGatewayActions();
		
	}

		
		
		
	

	private boolean doStuff(List<Process> allProcess) throws IOException {
		// TODO Auto-generated method stub
				
		
		for (Process p : allProcess) {
			for(FlowElement fe : p.getFlowElements()) {
//				if (fe instanceof Task)
//					System.err.println(fe);
//					System.err.flush();
					
			}
		}
		return true;
	}

}
