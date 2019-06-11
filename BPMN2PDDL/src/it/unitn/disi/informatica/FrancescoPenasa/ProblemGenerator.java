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
	
	// CONSTANTS for predicates
	final int AT = 0;
	final int HAS = 1;
	final int LINKED = 2;
	
	FileWriter writer = null;
	String probName = null;
	
	// used to access all the data in the bpmn file
	BPMNtoJava bpmn = null;

	
	public ProblemGenerator() {
		// TODO Auto-generated constructor stub
	}

	public ProblemGenerator(BPMNtoJava bpmn, String probName) throws IOException {
		// init static var
		this.probName = probName;
		this.bpmn = bpmn;
		
		OutputWriter w = new OutputWriter (probName + "_prob.pddl");
		writer = w.getFileWriter();
		
		// start writing the prob file
		// until (:domain ; first 4 rows
		writeIntro();

		//(:objects
		writeObjects(bpmn.getAllProcess());
		
		//init state
		writeInit(bpmn.getAllProcess(), bpmn.getAllCollaborations());
		
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

	
	
	//-------------------- INIT -------------------///
	private void writeInit(List<Process> processes, List<Collaboration> collaborations) throws IOException {
		// intro
		writer.write("\t(:init");
	
		
		writeInit_Has(processes);

		writeInit_Linked(processes, collaborations);

		writeInit_At(processes);
		
		writeInit_LinkedGateways(processes);
		
		//outro
		writer.write(")\n\n");
	}

	private void writeInit_LinkedGateways(List<Process> processes) throws IOException {
		for (Process p : processes) {
			// for sequenceFlow
			for(FlowElement fe : p.getFlowElements()) {
				if (fe instanceof InclusiveGateway) {
					InclusiveGateway from = (InclusiveGateway) fe;
					for (SequenceFlow sf : from.getOutgoing()) {						
						if (sf.getTargetRef() instanceof FlowElement) {
							writer.write("\n\t\t(linked_Inclusive_Gateway " + from.getId() + " "
									+ sf.getTargetRef().getId() + ")");
						}
					}		
				}
				if (fe instanceof ExclusiveGateway) {
					ExclusiveGateway from = (ExclusiveGateway) fe;
					for (SequenceFlow sf : from.getOutgoing()) {						
						if (sf.getTargetRef() instanceof FlowElement) {
							writer.write("\n\t\t(linked_Exclusive_Gateway " + from.getId() + " "
									+ sf.getTargetRef().getId() + ")");
						}
					}		
				}
				if (fe instanceof ParallelGateway) {
					ParallelGateway from = (ParallelGateway) fe;
					for (SequenceFlow sf : from.getOutgoing()) {						
						if (sf.getTargetRef() instanceof FlowElement) {
							writer.write("\n\t\t(linked_Parallel_Gateway " + from.getId() + " "
									+ sf.getTargetRef().getId() + ")");
						}
					}		
				}
			}
		}		
	}

	private void writeInit_At(List<Process> processes) throws IOException {
		for (Process p : processes) {
			for(FlowElement fe : p.getFlowElements()) {
				if (fe instanceof StartEvent) {					
					writer.write("\n\t\t(at " + fe.getId() + ")");
				}
			}
		}
	}
	
	
	/**
	 * find outgoing sequenFlow and messageFlow from every 1..1 node in bpmn file
	 * @param processes
	 * @throws IOException
	 */
	private void writeInit_Linked(List<Process> processes, List<Collaboration> collaborations) throws IOException {
		for (Process p : processes) {
			// for sequenceFlow
			for(FlowElement fe : p.getFlowElements()) {
				if (fe instanceof Task) {
					Task from = (Task) fe;
					for (SequenceFlow sf : from.getOutgoing()) {						
						if (sf.getTargetRef() instanceof FlowElement) {
							writer.write("\n\t\t(linked " + from.getId() + " "
									+ sf.getTargetRef().getId() + ")");
						}
					}		
				}
				if (fe instanceof Event) {
					Event from = (Event) fe;
					for (SequenceFlow sf : from.getOutgoing()) {						
						if (sf.getTargetRef() instanceof FlowElement) {
							writer.write("\n\t\t(linked " + from.getId() + " "
									+ sf.getTargetRef().getId() + ")");
						}
					}		
				}
			}
		}
			
			// for messageFlow
			for (Collaboration c : collaborations) {
				for(MessageFlow mf : c.getMessageFlows()) {
					if (mf.getSourceRef() instanceof Task && mf.getTargetRef() instanceof FlowElement) {
						Task from = (Task) mf.getSourceRef();
						FlowElement to = (FlowElement) mf.getTargetRef();
						writer.write("\n\t\t(linked " + from.getId() + " "
								+ to.getId() + ")");
					}
					if (mf.getSourceRef() instanceof Event && mf.getTargetRef() instanceof FlowElement) {
						Event from = (Event) mf.getSourceRef();
						FlowElement to = (FlowElement) mf.getTargetRef();
						writer.write("\n\t\t(linked " + from.getId() + " "
								+ to.getId() + ")");
					}
				}
			
		}
	}

	/**
	 * write the owner of each state
	 * @param processes
	 * @throws IOException
	 */
	private void writeInit_Has(List<Process> processes) throws IOException {
		for (Process p : processes) {
			for(FlowElement fe : p.getFlowElements()) {
				writer.write("\n\t\t(has " + p.getId() + " " + fe.getId() + ")");
			}
		}
	}

	
	//------------------------ OBJECTS ------------------------//
	//TODO ADD objects
	private void writeObjects(List<Process> processes) throws IOException {
		writer.write("\t(:objects");

		writeObjects_State(processes);
		writeObjects_Gate(processes);
		
		writer.write(")\n\n");
	}

	private void writeObjects_Gate(List<Process> processes) throws IOException {
		String objName = new String("");
		String objType = new String("");
		
		//look all Gateway
		for (Process p : processes) {
			for(FlowElement fe : p.getFlowElements()) {
				if (fe instanceof InclusiveGateway) {
					InclusiveGateway g = (InclusiveGateway) fe;
					g.getGatewayDirection();
					objName = fe.getId();    
					objType = "incGateway";
					writer.write("\n\t\t" + objName + " - " + objType);
				}
				if (fe instanceof ExclusiveGateway) {
					objName = fe.getId();    
					objType = "exGateway";
					writer.write("\n\t\t" + objName + " - " + objType);
				}
				if (fe instanceof ParallelGateway) {
					objName = fe.getId();    
					objType = "parGateway";
					writer.write("\n\t\t" + objName + " - " + objType);
				}
			}
		}	
	}

	
	/**
	 * il tipo state rappresenta i nodi che hanno cardinalita 1..1
	 * @param processes
	 * @throws IOException
	 */
	private void writeObjects_State(List<Process> processes) throws IOException {
		String objName = new String("");
		String objType = new String("");
		//look all tasks, startEvent, endEvent
		for (Process p : processes) {
			for(FlowElement fe : p.getFlowElements()) {
				if (fe instanceof Task ) {
					objName = fe.getId();
					objType = "activities";
					writer.write("\n\t\t" + objName + " - " + objType);
				}
				if (fe instanceof StartEvent ) {
					objName = fe.getId();
					objType = "startEvents";
					writer.write("\n\t\t" + objName + " - " + objType);
				}
				if (fe instanceof EndEvent ) {
					objName = fe.getId();
					objType = "endEvents";
					writer.write("\n\t\t" + objName + " - " + objType);
				}
			}
		}
	}
	
	

	//-------------------- OK ---------------------------//
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


//------------------ ok ------------------------//
	private void writeOutro() throws IOException {
		final String OUTRO = new String(")");
		writer.write(OUTRO);
	}

//------------------------OK-------------------------//
	/**
	 * write goal
	 * @param processes
	 * @throws IOException
	 */
	
	
	//-------------- GOAL ----------------------------//
	private void writeGoal(List<Process> processes) throws IOException {
		writer.write("\t(:goal");
		
		//find all EndEvent in the bpmn2 file
		for (Process p : processes) {
			for(FlowElement fe : p.getFlowElements()) {
				if (fe instanceof EndEvent) {
					writer.write("\n\t\t(at " + fe.getId() + ")");
				}
			}
		}
		writer.write(")\n");
	}
}




/*// every messageFlow from a Task in the bpmn file
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
}*/