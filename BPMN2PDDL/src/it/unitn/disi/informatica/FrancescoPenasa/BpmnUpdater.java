/**
 * 
 */
package it.unitn.disi.informatica.FrancescoPenasa;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.InclusiveGateway;
import org.eclipse.bpmn2.ParallelGateway;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNPlane;

/**
 * @author lithium
 *
 */
public class BpmnUpdater {

	//final public Enum<String> ERROR = "-10"; // TO TRANSFORM IN ENUM
	
	/**
	 * prende gli elementi presenti in lis, li fa diventare dei task li collega in successione
	 * al task che corrisponde a task_id_from, 
	 * SE task_id_to != null l'ultimo dei nuovi task va a finire nel
	 * 	FlowNode che ha come id task_id_to e cancella tutti i flowelement outgoings originali di task_id_from 
	 * ALTRIMENTI salva tutti gli outgoings originale del task task_id_from e ne sostituisce il sourceref con l'ultimo
	 *   dei nuovi task.
	 * @param lis contiene i nuovi stati
	 * @param bpmn
	 * @param url
	 * @param from_elem
	 * @param to_elem
	 */
	public BpmnUpdater(List<List<List<String>>> plans, Bpmn2Java bpmn, String from_elem, String to_elem) {
		List<FlowNode> new_elements = new ArrayList<FlowNode>();
		List<SequenceFlow> new_links = new ArrayList<SequenceFlow>();
		FlowNode from;
		FlowNode to;
		SequenceFlow sf;
		
		// no exclusiveGateway
		if (plans.size() == 1) {
			List<List<String>> times = plans.get(0);
			for (List<String> states : times) {
				
				
				if (states.size() == 1) {
					Task t = Bpmn2Factory.eINSTANCE.createTask();
					t.setName(states.get(0));
					new_elements.add(t);
					
					sf = Bpmn2Factory.eINSTANCE.createSequenceFlow();
					sf.setSourceRef(from);
					sf.setTargetRef(t);
					new_links.add(sf);
					
					sf = null;
					from = null;
					from = t;
										
				} else {
					// where starts parallel
					ParallelGateway pre_pg = Bpmn2Factory.eINSTANCE.createParallelGateway();
					new_elements.add(pre_pg);
					
					sf = Bpmn2Factory.eINSTANCE.createSequenceFlow();
					sf.setSourceRef(from);
					sf.setTargetRef(pre_pg);
					new_links.add(sf);					
					sf = null;
					
					// where end parallel
					ParallelGateway post_pg = Bpmn2Factory.eINSTANCE.createParallelGateway();
					new_elements.add(post_pg);
					from = null;
					from = post_pg;
					
					for (String state : states) {
						Task t = Bpmn2Factory.eINSTANCE.createTask();
						t.setName(state);
						new_elements.add(t);
						
						sf = Bpmn2Factory.eINSTANCE.createSequenceFlow();
						sf.setSourceRef(pre_pg);
						sf.setTargetRef(t);
						new_links.add(sf);
						sf = null;
												
						sf = Bpmn2Factory.eINSTANCE.createSequenceFlow();
						sf.setSourceRef(t);
						sf.setTargetRef(post_pg);
						new_links.add(sf);
						sf = null;
					}
				}
			}
		} else {
			ExclusiveGateway eg = Bpmn2Factory.eINSTANCE.createExclusiveGateway();
			new_elements.add(eg);
			
			sf = Bpmn2Factory.eINSTANCE.createSequenceFlow();
			sf.setSourceRef(from);
			sf.setTargetRef(eg);
			new_links.add(sf);					
			sf = null;
			
			sf = null;
			from = null;			
			for (List<List<String>> plan : plans) {
				from = eg;
				
				List<List<String>> times = plan;
				for (List<String> states : times) {
					
					
					if (states.size() == 1) {
						Task t = Bpmn2Factory.eINSTANCE.createTask();
						t.setName(states.get(0));
						new_elements.add(t);
						
						sf = Bpmn2Factory.eINSTANCE.createSequenceFlow();
						sf.setSourceRef(from);
						sf.setTargetRef(t);
						new_links.add(sf);
						
						sf = null;
						from = null;
						from = t;
											
					} else {
						// where starts parallel
						ParallelGateway pre_pg = Bpmn2Factory.eINSTANCE.createParallelGateway();
						new_elements.add(pre_pg);
						
						sf = Bpmn2Factory.eINSTANCE.createSequenceFlow();
						sf.setSourceRef(from);
						sf.setTargetRef(pre_pg);
						new_links.add(sf);					
						sf = null;
						
						// where end parallel
						ParallelGateway post_pg = Bpmn2Factory.eINSTANCE.createParallelGateway();
						new_elements.add(post_pg);
						from = null;
						from = post_pg;
						
						for (String state : states) {
							Task t = Bpmn2Factory.eINSTANCE.createTask();
							t.setName(state);
							new_elements.add(t);
							
							sf = Bpmn2Factory.eINSTANCE.createSequenceFlow();
							sf.setSourceRef(pre_pg);
							sf.setTargetRef(t);
							new_links.add(sf);
							sf = null;
													
							sf = Bpmn2Factory.eINSTANCE.createSequenceFlow();
							sf.setSourceRef(t);
							sf.setTargetRef(post_pg);
							new_links.add(sf);
							sf = null;
						}
					}
			}
		}
		
		
			
		Definitions def = Bpmn2Java.getDef();
		
		
		// Create all the new states and put them in a ordered list
		List<Task> new_states = new ArrayList<Task>();
		for(int i = 0; i < lis.size(); i++) {
			Task state = Bpmn2Factory.eINSTANCE.createTask();
			state.setName(lis.get(i));
			new_states.add(state);
		}
		
		Process p = null;
		FlowNode out = null;
		
		SequenceFlow sf = Bpmn2Factory.eINSTANCE.createSequenceFlow(); // primo SF
		List<SequenceFlow> outgoings = new ArrayList<SequenceFlow>();  // collezione dei nuovi SF
		
		// find the from element, collect all his outgoings in an arraylist, 
		// change his name to see the error, add a sequence flow from him to the first of the new states.
		for (RootElement re : def.getRootElements()) {
			// if there are not collaborations
			if (re instanceof Process) {
				p = (Process) re;
				// TODO support per laneset
				for(FlowElement fe : p.getFlowElements()) {
					
					// se il flowelement e' quello da cui voglio partire per i nuovi stati
					if (fe.getId().equals(from_elem)) {
						fe.setName(fe.getName() + " interrupted");
						
						if (fe instanceof FlowNode) {
							FlowNode fn = (FlowNode) fe;
							outgoings = fn.getOutgoing();
							sf.setSourceRef(fn);
							sf.setTargetRef(new_states.get(0));
						} else {
							System.err.println(from_elem + " is not an Activity or Event or Gateway, it cannot be a \"state\".");
							System.exit(-10); // TODO  ENUM
						}											
					}
					
					// se il flowelement e' quello in cui voglio arrivare con i nuovi stati
					if (fe.getId().equals(to_elem)) {
						out = (FlowNode) fe;
					}
				}
			}
		}
		
		
		List<SequenceFlow> new_links = new ArrayList<SequenceFlow>();
		new_links.add(sf);
		
		// generate a link between all the new states
		for (int i = 1; i < new_states.size(); i++) {
			Task from = new_states.get(i-1);
			Task to = new_states.get(i);
			
			SequenceFlow new_link = Bpmn2Factory.eINSTANCE.createSequenceFlow();
			new_link.setSourceRef(from);
			new_link.setTargetRef(to);
			
			new_links.add(new_link);
		}
		
		// add all the saved outgoings to the last state
		Task last_state = new_states.get(new_states.size() - 1);
		if (to_elem == null) {
			for (SequenceFlow outgoing : outgoings) {
				outgoing.setSourceRef(last_state);
			}
		} else {
			SequenceFlow new_sf = Bpmn2Factory.eINSTANCE.createSequenceFlow();
			new_sf.setSourceRef(last_state);
			new_sf.setTargetRef(out);
			
			new_links.add(new_sf);
		}
		

		
		// aggiungo i nuovi elementi
		p.getFlowElements().addAll(new_states);
		p.getFlowElements().addAll(new_links);
		
		// cancello il diagramma per evitare errori
		def.getDiagrams().removeAll(def.getDiagrams());
		
		// TODO test per fare aggiunte al diagramma piuttosto che cancellarlo
		for (BPMNDiagram bp : def.getDiagrams()) {
			BPMNPlane bplane = bp.getPlane();
			if (bplane.getBpmnElement().equals(p.getId())) {				
			}
		}
		
		
		
		// salvo i cambiamenti
		try {
			Bpmn2Java.getResource().save(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	

}
