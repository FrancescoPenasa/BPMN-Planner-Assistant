/**
 * 
 */
package it.unitn.disi.informatica.FrancescoPenasa;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	
	
	final int OUTPUT_DIM = 5;
	
	// CONSTANTS for WriteActions()
	final String START_ACTION = new String("\t(:action ");
	final String PARAMETERS_ACTION = new String("\n\t\t:parameters ");
	final String PRECONDITIONS_ACTION = new String("\n\t\t:precondition ");
	final String EFFECT_ACTION = new String("\n\t\t:effect ");
	final String END_ACTION = new String(")\n\n");	
	final String[] ACTION =  {START_ACTION, PARAMETERS_ACTION, 
			PRECONDITIONS_ACTION, EFFECT_ACTION, END_ACTION};
	
	// CONSTATNS for Types
	final String TASK_TYPE = new String("Task");
	
	
	FileWriter writer = null;
	String domainName = null;
	List<String> types = null;
	
	//this hsouldnt be here
	List<String> predicates = null;
	
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
		writeTypes(bpmn.getAllProcess());
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
	 * to get the types used
	 */
	public List<String> getAllTypes(){
		return types;
	}
	public List<String> getAllPredicates(){
		return predicates;
	}


	/**
	 * Close the inital parentesis to make the domain file working
	 * @throws IOException
	 */
	private void writeOutro() throws IOException {
		final String OUTRO = new String(")");
		writer.write(OUTRO);
	}

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

	/**
	 * write the types
	 * @param allProcess
	 * @throws IOException
	 */
	private void writeTypes(List<Process> processes) throws IOException {
		final String INTRO = new String("\t(:types");
		
		types = new ArrayList<String>();	
		
		//TODO CHANGE
		for (Process p : processes) {
			for(LaneSet le : p.getLaneSets()) {
				System.out.println("Lane set: " + le.getId());
				for(Lane l : le.getLanes()) {
					System.out.println("Lane: " + l.getName());
					types.add(l.getId());
				}
			}
		}
		
		//TODO
		//tmp write the type task lets see after
		types.add(TASK_TYPE);
		
		writer.write(INTRO);
		for (int i = 0; i<types.size(); i++) {
			writer.write(" " + types.get(i));
		}
		
		writer.write(")\n");
	}
	
	private void writePredicates() throws IOException {
		final String INTRO = new String("\t(:predicates");
		final String PREDICATE_HAS = new String("\n\t\t(has ?actor ?state)");
		final String PREDICATE_AT = new String("\n\t\t(at ?state)");
		final String OUTRO = new String(")\n\n");
		
		predicates = new ArrayList<String>();	
		predicates.add(PREDICATE_AT);
		predicates.add(PREDICATE_HAS);
		
		writer.write(INTRO + PREDICATE_HAS + PREDICATE_AT + OUTRO);
	}
	
	
	/**
	 * Crea un'azione usando @param fromTask e @param toTask
	 * mette in output le cose che servono per scrivere l'azione
	 * index 0 nome dell'azione
	 * index 1 i parametri necessari con relativi tipi
	 * usando i predicati (at ?state) (has ?actor ?state)
	 * devo cambiare il nome perchè fa anche da startevent a flowelement
	 */
	private String [] createActionFromTask(FlowElement fromState, FlowElement toState) {
		String[] output = new String[OUTPUT_DIM];
				
		// name of the action with a space previously or there is mess
		String from = fromState.getName().replaceAll(" ", "_");
		String to = toState.getName().replaceAll(" ", "_");
		output[0] = from + "_To_" + to;
		
		// parameters of the action
		output[1] = "(?" + fromState.getId() + " - " + TASK_TYPE + " ?" + toState.getId() + " - " + TASK_TYPE + ")";
		
		//preconditions of the action
		output[2] = "(and (at ?" + fromState.getId() + ")" + "(not (at ?" + toState.getId() + ")))";
		
		//effect of the action
		output[3] = "(and (at ?" + toState.getId() + ")" + "(not (at ?" + fromState.getId() + ")))";
		
		// because it looks good
		output[4] = "";
		return output;
	}
	
	/**
	 * takes all the task in the bpmn file 
	 * @param processes
	 * @throws IOException
	 */
	private void writeTaskActions(List<Process> processes, List<Collaboration> collaborations) throws IOException {
		String[] output = new String[OUTPUT_DIM];
		Task fromTask = null;
		FlowElement toElement = null;
		
		// every sequenceFlow from a Task in the bpmn file
		for (Process p : processes) {
			for(FlowElement fe : p.getFlowElements()) {
				if (fe instanceof Task) {
					fromTask = (Task) fe;
					
					// all objects connected to the Task fromTask with a SequenceFlow
					for (SequenceFlow sf : fromTask.getOutgoing()) {						
						if (sf.getTargetRef() instanceof FlowElement) {
							toElement = sf.getTargetRef();
							System.out.println("\n\n ::: ");
							System.out.println(toElement.getId());
							output = createActionFromTask(fromTask, toElement);
							
							for(int i = 0; i<OUTPUT_DIM; i++) {
								writer.write(ACTION[i] + output[i]);
							}						
						}
					}		
				}
			}
		}
		
		// every messageFlow from a Task in the bpmn file
		for (Collaboration c : collaborations) {
			for(MessageFlow mf : c.getMessageFlows()) {
				if (mf.getSourceRef() instanceof Task) {
					fromTask = (Task) mf.getSourceRef();
					toElement = (FlowElement) mf.getTargetRef();
					output = createActionFromTask(fromTask, toElement);
					for(int i = 0; i<OUTPUT_DIM; i++) {
						writer.write(ACTION[i] + output[i]);
					}
				}							
			}
		}
	}
		
	
	
	private void writeGatewayActions(List<Process> processes) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * write all types of actions
	 * @throws IOException
	 */
	private void writeActions() throws IOException {
		writeStartEventActions(bpmn.getAllProcess());
		writeTaskActions(bpmn.getAllProcess(), bpmn.getAllCollaborations());
		writeGatewayActions(bpmn.getAllProcess());
		
	}

	
	
	/**
	 * assumption: start is always in a lane and his outgoing cannot be messageflow NEVER only 
	 * sequenceFlow
	 * @throws IOException 
	 */

	private void writeStartEventActions(List<Process> processes) throws IOException {
		String[] output = new String[OUTPUT_DIM];
		StartEvent start = null;
		FlowElement toElement = null;
		
		// every sequenceFlow from a Task in the bpmn file
		for (Process p : processes) {
			for(FlowElement fe : p.getFlowElements()) {
				if (fe instanceof StartEvent) {
					start = (StartEvent) fe;
					
					// all objects connected to the Task fromTask with a SequenceFlow
					for (SequenceFlow sf : start.getOutgoing()) {						
						if (sf.getTargetRef() instanceof FlowElement) {
							toElement = sf.getTargetRef();
							System.out.println("\n\n ::: ");
							System.out.println(toElement.getId());
							output = createActionFromTask(start, toElement);
							
							for(int i = 0; i<OUTPUT_DIM; i++) {
								writer.write(ACTION[i] + output[i]);
							}						
						}
					}		
				}
			}
		}
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
