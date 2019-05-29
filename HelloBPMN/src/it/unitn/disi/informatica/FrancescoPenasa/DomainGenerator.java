/**
 * 
 */
package it.unitn.disi.informatica.FrancescoPenasa;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.Task;

/**
 * @author ubuntu
 *
 */
public class DomainGenerator {

	/**
	 * 
	 */
	
	final int OUTPUTDIM = 4;
	
	FileWriter writer = null;
	String output_file = null;
	public DomainGenerator() {
		// TODO Auto-generated constructor stub
	}
	
	public DomainGenerator(BPMNtoJAVA bpmn, String output_file) throws IOException {
		// TODO Auto-generated constructor stub
		this.output_file = output_file;
		
		// open FileWriter
		writer = new FileWriter(output_file + "_domain.pddl", true);
		
		writeIntro();
		writeTypes(bpmn.getAllProcess());
		writePredicates();
		writeTaskActions(bpmn.getAllProcess());
		writeOutro();
		boolean test = doStuff(bpmn.getAllProcess());
		
		
		// close FileWriter
		writer.close();
		
		System.out.flush();
		System.out.println("writer closed");
		
	}


	private void writeOutro() throws IOException {
		final String OUTRO = new String(")");
		writer.write(OUTRO);
	}

	private void writeIntro() throws IOException {
		
		final String INTRO = new String(";; domain file: ");
		final String END_FILE_NAME = new String("_domain.pddl \n\n");
		
		writer.write(INTRO + output_file + END_FILE_NAME);		
		writer.write("(define (domain " + output_file + ")\n");
		writer.write("\t(:requirements :typing)\n");
	}

	private void writeTypes(List<Process> allProcess) throws IOException {
		
		final String INTRO = new String("\t(:types");
		
		List<String> types = new ArrayList<String>();
		
		for (Process p : allProcess) {
			for(LaneSet le : p.getLaneSets()) {
				System.out.println("Lane set: " + le.getId());
				for(Lane l : le.getLanes()) {
					System.out.println("Lane: " + l.getName());
					types.add(l.getId());
				}
			}
		}
		
		writer.write(INTRO);
		for (int i = 0; i<types.size(); i++) {
			writer.write(" " + types.get(i));
		}
		
		//tmp write lets see after
		writer.write(" " + "task");
		
		writer.write(")\n");
		
	}
	
	private void writePredicates() throws IOException {
		final String INTRO = new String("\t(:predicates");
		final String PREDICATE_HAS = new String("\n\t\t(has ?actor ?state)");
		final String PREDICATE_AT = new String("\n\t\t(at ?state)");
		final String OUTRO = new String(")\n\n");
		
		writer.write(INTRO + PREDICATE_HAS + PREDICATE_AT + OUTRO);
	}
	
	
	/**
	 * Crea un'azione usando @param fromTask e @param toTask
	 * mette in output le cose che servono per scrivere l'azione
	 * index 0 nome dell'azione
	 * index 1 i parametri necessari con relativi tipi
	 * usando i predicati (at ?state) (has ?actor ?state)
	 */
	private String [] createActionFromTask(Task fromTask, Task toTask) {
		String[] output = new String[OUTPUTDIM];
				
		// name of the action with a space previously or there is mess
		output[0] = fromTask.getName() + " to " + toTask.getName();
		
		// parameters of the action
		output[1] = "(?fromTask - task ?toTask - task)";
		
		//precondions of the action
		output[2] = "(and (at ?" + fromTask.getId() + ")" + "(not (at ?" + toTask.getId() + ")))";
		
		//effect of the action
		output[3] = "(and (at ?" + toTask.getId() + ")" + "(not (at ?" + fromTask.getId() + ")))";
		
		return output;
	}
	
	
	private void writeTaskActions(List<Process> processes) throws IOException {
			
		final String INTRO = new String("\t(:action ");
		final String PARAMETERS = new String("\n\t\t:parameters ");
		final String PRECONDITIONS = new String("\n\t\t:precondition ");
		final String EFFECT = new String("\n\t\t:effect ");
		final String OUTRO = new String(")\n\n");
		
		String[] output = new String[OUTPUTDIM];
		Task fromTask = null;
		Task toTask = null;
		
		//tmp to test it
		//assumption that every task has a ppol so every task has is child of a process
		for (Process p : processes) {
			for(FlowElement fe : p.getFlowElements()) {
				if (fe instanceof Task) {
					fromTask = (Task) fe;
					for (SequenceFlow sf : fromTask.getOutgoing()) {
						if (sf.getTargetRef() instanceof Task) {
							toTask = (Task) sf.getTargetRef();
							output = createActionFromTask(fromTask, toTask);
							
							writer.write(INTRO + output[0]);
							writer.write(PARAMETERS + output[1]);
							writer.write(PRECONDITIONS + output[2]);
							writer.write(EFFECT + output[3]);
							writer.write(OUTRO);
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
