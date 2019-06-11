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
import org.eclipse.core.internal.resources.File;

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
		
		OutputWriter w = new OutputWriter (domainName + "_domain.pddl");
		writer = w.getFileWriter();
		
		
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
	
	
	
/**
 * 
 * @param name
 * @throws IOException
 */
	

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
	 * conditional effects needed for "when"
	 * typing needed for types 
	 * @throws IOException
	 */
	private void writeIntro() throws IOException {	
		final String INTRO = new String(";; domain file: ");
		final String END_FILE_NAME = new String("_domain.pddl \n\n");
		
		writer.write(INTRO + domainName + END_FILE_NAME);		
		writer.write("(define (domain " + domainName + ")\n");
		writer.write("\t(:requirements :typing :strips)\n");
	}
	
	//----------------- NEED TO ADD ALL TYPES ------------------\\
	/**
	 * write the types
	 * @param allProcess
	 * @throws IOException
	 */
	private void writeTypes() throws IOException {
		
		List<String>types = new ArrayList<String>();
		
		types.add("flowObject - Object");
		
		// An Event is something that “happens” during the
		// course of a Process (see page 238) or a
		// Choreography
		types.add("events - flowObject");
		types.add("startEvents - events");
		types.add("endEvents - events");
		types.add("intermediateEvents - events");
		
		// An Activity is a generic term for work that company
		// performs in a process
		types.add("activities - flowObject");
		types.add("task - activities");
		types.add("subProcess - activities");
		
		// A Gateway is used to control the divergence and
		// convergence of Sequence Flows in a Process
		types.add("gateways - flowObject");
		types.add("incclusiveGateway - gateways");
		types.add("exclusiveGateway - gateways");
		types.add("parallelGateway - gateways");
		types.add("eventBasedGateway - gateways");
		types.add("complexGateway - gateways");
		
		types.add("data - Object");
		types.add("dataObject - data");
		types.add("dataInput - data");
		types.add("dataOutput - data");
		types.add("dataStores - data");
		
		types.add("connectingObjects - Object");
		
		// A Sequence Flow is used to show the order that
		// Activities will be performed in a Process
		types.add("sequenceFlow - connectingObjects");
		// A Message Flow is used to show the flow of
		// Messages between two Participants
		types.add("messageFlow - connectingObjects");
		types.add("associations - connectingObjects");
		types.add("dataAssociations - connectingObjects");
		
		types.add("swimlanes - Object");
		types.add("pools - swimlanes");
		types.add("lanes - swimlanes");
		
		// nope
		types.add("artifact - Object");
		types.add("group - artifact");
		types.add("textAnnotation - artifact");
		
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
		predicates.add("(linked_Parallel_Gateway ?parGateway ?node)");
		predicates.add("(linked_Inclusive_Gateway ?incGateway ?node)");
		predicates.add("(linked_Exclusive_Gateway ?exGateway ?node)");
		
		// true if an event it has been reached
		predicates.add("(eventConfirm ?event)");
		
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
	private void writeActivitiesAction() throws IOException {
		writer.write(""
				+ "\t(:action fromActivities \n"
				+ "\t\t:parameters   (?from - activities ?to) \n"
				+ "\t\t:precondition (and (at ?from) (not (at ?to)) (linked ?from ?to)) \n"
				+ "\t\t:effect       (and (at ?to) (not (at ?from)))) \n\n");
	}	
	
	/**
	 * write 
	 */
	private void writeGatewayActions() throws IOException {
		// inclusive gateway case
		writer.write(""
				+ "\t(:action fromInclusiveGateway \n"
				+ "\t\t:parameters   (?from - incGateway ?to) \n"
				+ "\t\t:precondition (and (at ?from) (not (at ?to)) (linked_Inclusive_Gateway ?from ?to)) \n"
				+ "\t\t:effect       (and (at ?to) (not (linked_Inclusive_Gateway ?from ?to)))) \n\n");
		
		writer.write(""
				+ "\t(:action fromExclusiveGateway \n"
				+ "\t\t:parameters   (?from - exGateway ?to) \n"
				+ "\t\t:precondition (and (at ?from) (linked_Exclusive_Gateway ?from ?to)) \n"
				+ "\t\t:effect       (and  (at ?to) (not (at ?from)))) \n\n"); 
		
		writer.write(""
				+ "\t(:action fromParallelGateway \n"
				+ "\t\t:parameters   (?from - parGateway) \n"
				+ "\t\t:precondition (at ?from) \n"
				+ "\t\t:effect       (and  (not (at ?from)) \n" 
				+ "\t\t\t (forall (?to - state) \n" 
				+ "\t\t\t\t (when (linked_Parallel_Gateway ?from ?to)\n"
				+ "\t\t\t\t (and (at ?to) )))))\n\n");
		
	}
	
	/**
	 * StartEvent can go with SequenceFlow to flowObjects except itself
	 * IntermediateEvent can go with SequenceFlow to flowObjects except itself
	 * EndEvent can't have any outcome.
	 * 
	 * 
	 * @throws IOException 
	 */
	private void writeEventsAction() throws IOException {
		// moving from an event
		writer.write(""
				+ "\t(:action fromStartEvent \n"
				+ "\t\t:parameters   (?from - events ?to - flowObject) \n"
				+ "\t\t:precondition (and (at ?from) (not (at ?to)) (linked ?from ?to)) \n"
				+ "\t\t:effect       (and (at ?to) (not (at ?from)))) \n\n");
		
		// confirm to have reached an IntermediateEvent
		writer.write(""
				+ "\t(:action intermediateEventConfirm \n"
				+ "\t\t:parameters   (?x - event) \n"
				+ "\t\t:precondition (at ?x) \n"
				+ "\t\t:effect       (eventConfirm ?x) \n\n");
		
		
	}

	/**
	 * write all types of actions
	 * @throws IOException
	 */
	private void writeActions() throws IOException {
		//this isn't needed now
		//writeStartEventActions();

		writeActivitiesAction();
		writeEventsAction();
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
