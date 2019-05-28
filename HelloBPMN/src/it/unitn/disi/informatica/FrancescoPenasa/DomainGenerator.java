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

/**
 * @author ubuntu
 *
 */
public class DomainGenerator {

	/**
	 * 
	 */
	
	final String INTRO = new String("HELLO HELLO");
	
	
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
		writeActions();
		doStuff(bpmn.getAllProcess());
		
		
		// close FileWriter
		writer.close();
		
		System.out.println("writer closed");
		
	}

	
	private void writeActions() {
		// TODO Auto-generated method stub
		
	}

	private void writePredicates() {
		// TODO Auto-generated method stub
		
	}

	private void writeIntro() throws IOException {
		
		final String INTRO = new String(""
				+ ";; domain file: ");
		final String END_FILE_NAME = new String(""
				+ "_domain.pddl \n\n");
		
		writer.write(INTRO + output_file + END_FILE_NAME);		
		writer.write("(define (domain " + output_file + ")\n");
		writer.write("\t(:requirements :typing)\n");
	}

	
	private void writeTypes(List<Process> allProcess) throws IOException {
		
		final String INTRO = new String(""
				+ "\t(:types ");
		
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
			writer.write(types.get(i) + " ");
		}
		writer.write(")\n");
		
	}
	
	private void doStuff(List<Process> allProcess) throws IOException {
		// TODO Auto-generated method stub
		writer.write(INTRO);
		
		
		for (Process p : allProcess) {
			for(FlowElement fe : p.getFlowElements()) {
				
				//System.out.println("FlowElement: " + fe.getId());
			}
		}
	}

}
