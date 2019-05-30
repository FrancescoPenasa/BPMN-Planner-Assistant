package it.unitn.disi.informatica.FrancescoPenasa;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.Task;

class ProblemGenerator {
	
	// CONSTANTS for predicates
	final int AT = 0;
	final int HAS = 1;
	
	FileWriter writer = null;
	String probName = null;
	List<String> predicates = null;
	// used to access all the data in the bpmn file
	BPMNtoJava bpmn = null;

	
	public ProblemGenerator() {
		// TODO Auto-generated constructor stub
	}

	public ProblemGenerator(BPMNtoJava bpmn, String probName) throws IOException {
		// init static var
		this.probName = probName;
		this.bpmn = bpmn;
		this.predicates = predicates;
		writer = new FileWriter(probName + "_prob.pddl", true);
		
		// start writing the prob file
		// until (:domain ; first 4 rows
		writeIntro();

		//(:objects
		writeObjects(bpmn.getAllProcess());
		
		//init state
		writeInit(bpmn.getAllProcess());
		
		//goal state
		writeGoal(bpmn.getAllProcess());
		
		//last parenthesissss
		writeOutro();
		
		// testing purpose
		//boolean test = doStuff(bpmn.getAllProcess());
		
		
		// close FileWriter
		writer.flush();
		writer.close();
		
		// rosik
		System.out.flush();	
		System.out.println("Prob generator finished!");
		// TODO Auto-generated constructor stub
	}

	private void writeOutro() throws IOException {
		final String OUTRO = new String(")");
		writer.write(OUTRO);
	}

	private void writeGoal(List<Process> processes) throws IOException {
		final String INTRO = new String("\t(:goal\n");
		final String OUTRO = new String("\t)\n\n");
		
		writer.write(INTRO);
	
		String init = new String("");
		String id = new String("");

		//AT 
		//look EndEvent
		for (Process p : processes) {
			for(FlowElement fe : p.getFlowElements()) {
				if (fe instanceof EndEvent) {
					
					writer.write("\t\t(at " + fe.getId() + ")\n");
				}
			}
		}
		
		writer.write(OUTRO);
		
	}

	private void writeInit(List<Process> processes) throws IOException {
		final String INTRO = new String("\t(:init\n");
		final String OUTRO = new String("\t)\n\n");
		
		writer.write(INTRO);
	
		;
		
		// HAS init
		for (Process p : processes) {
			for(FlowElement fe : p.getFlowElements()) {
				writer.write("\t\t(has " + p.getId() + " " + fe.getId() + ")\n");
				
				// LINKED init
				if (fe instanceof Task) {
					Task from = (Task) fe;
					for (SequenceFlow sf : from.getOutgoing()) {						
						if (sf.getTargetRef() instanceof FlowElement) {
							writer.write("\t\t(linked " + from.getId() + " "
									+ sf.getTargetRef().getId() + ")\n");
						}
					}		
				}
				if (fe instanceof StartEvent) {
					StartEvent from = (StartEvent) fe;
					for (SequenceFlow sf : from.getOutgoing()) {						
						if (sf.getTargetRef() instanceof FlowElement) {
							writer.write("\t\t(linked " + from.getId() + " "
									+ sf.getTargetRef().getId() + ")\n");
						}
					}		
				}
			}
		}
		
		//AT INIT
		//look all tasks, startEvent, stopEvent
		for (Process p : processes) {
			for(FlowElement fe : p.getFlowElements()) {
				if (fe instanceof StartEvent) {					
					writer.write("\t\t(at " + fe.getId() + ")\n");
				}
			}
		}
		
		
		
		writer.write(OUTRO);
		
	}

	//TODO CHANGE Task type
	private void writeObjects(List<Process> processes) throws IOException {
		final String INTRO = new String("\t(:objects\n");
		final String OUTRO = new String("\t)\n\n");
		
		writer.write(INTRO);

		String objName = new String("");
		String objType = new String("");
		
		//look all tasks, startEvent, stopEvent
		for (Process p : processes) {
			for(FlowElement fe : p.getFlowElements()) {
				if (fe instanceof Task || fe instanceof StartEvent || fe instanceof EndEvent) {
					objName = fe.getId();
					objType = "Task";
					writer.write("\t\t" + objName + " - " + objType + "\n");
				}
			}
		}
		
		writer.write(OUTRO);
	}

	/**
	 * write definition and requirements on the domain file
	 * @throws IOException
	 */
	private void writeIntro() throws IOException {	
		final String INTRO = new String(";; problem file: ");
		final String END_FILE_NAME = new String("_prob.pddl \n\n");
		
		writer.write(INTRO + probName + END_FILE_NAME);		
		writer.write("(define (problem " + probName + ")\n");
		writer.write("\t(:domain " + probName + ")\n\n");
	}
}
