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
import java.util.Map;

import org.eclipse.bpmn2.BaseElement;
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
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNPlane;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.di.BpmnDiFactory;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.dd.di.Plane;
import org.eclipse.emf.ecore.xmi.XMLResource;

/**
 * @author lithium
 *
 */
public class BpmnUpdater {

	Definitions def = null;

	/**
	 * prende gli elementi presenti in lis, li fa diventare dei task li collega in
	 * successione al task che corrisponde a task_id_from, SE task_id_to != null
	 * l'ultimo dei nuovi task va a finire nel FlowNode che ha come id task_id_to e
	 * cancella tutti i flowelement outgoings originali di task_id_from ALTRIMENTI
	 * salva tutti gli outgoings originale del task task_id_from e ne sostituisce il
	 * sourceref con l'ultimo dei nuovi task.
	 * 
	 * @param lis       contiene i nuovi stati
	 * @param bpmn
	 * @param url
	 * @param from_elem
	 * @param to_elem
	 */
	public BpmnUpdater(List<List<List<String>>> plans, Bpmn2Java bpmn, String from_elem, String to_elem) {
		this.def = bpmn.getDef();

		List<FlowNode> new_elements = new ArrayList<FlowNode>();
		List<SequenceFlow> new_links = new ArrayList<SequenceFlow>();
		List<SequenceFlow> outgoings = new ArrayList<SequenceFlow>(); // collezione di sf uscenti dal nodo from

		FlowNode from = getFlowNode(from_elem); // nodo iniziale
		FlowNode to = getFlowNode(to_elem); // nodo finale

		from.setName(from.getName() + " interrupted");
		outgoings = from.getOutgoing();
		
		Process p = null;
		for (RootElement re : this.def.getRootElements()) {
			if (re instanceof Process) {	
				p = (Process) re;
				for (FlowElement fe : p.getFlowElements()) {
					if (fe instanceof FlowNode) {
						if (fe.getId().equals(from.getId())) {
							p = (Process) re;
						}
					}
				}
			}
		}
		p.getFlowElements().removeAll(outgoings);
		
		SequenceFlow sf;

		// ----------- AGGIUNTA NUOVI STATI ----------------- //
		if (plans.size() == 1) // no exclusiveGateway
		{
			List<List<String>> times = plans.get(0);
			for (List<String> states : times) {

				if (states.size() == 1) // no parallelsGateway
				{
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

				} else // yes parallelsGateway
				{

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
		} else // yes exclusiveGateway
		{
			ExclusiveGateway eg_from = Bpmn2Factory.eINSTANCE.createExclusiveGateway();
			new_elements.add(eg_from);

			sf = Bpmn2Factory.eINSTANCE.createSequenceFlow();
			sf.setSourceRef(from);
			sf.setTargetRef(eg_from);
			new_links.add(sf);
			sf = null;
			from = null;
			
			ExclusiveGateway eg_to = Bpmn2Factory.eINSTANCE.createExclusiveGateway();
			new_elements.add(eg_to);
	
			for (List<List<String>> plan : plans) {
				from = eg_from;

				List<List<String>> times = plan;
				for (List<String> states : times) {

					if (states.size() == 1) // no parallelsGateway
					{
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

					} else // yes parallelsGateway
					{
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
				sf = Bpmn2Factory.eINSTANCE.createSequenceFlow();
				sf.setSourceRef(from);
				sf.setTargetRef(eg_to);
				new_links.add(sf);
				sf = null;
				from = eg_to;
			}			
		}
			// ----------- FINE AGGIUNTA NUOVI STATI ----------------- //

		
			if (to_elem == null) {
				for (SequenceFlow outgoing : outgoings) {
					
					outgoing.setSourceRef(from);					
					new_links.add(outgoing);
				}
			} else {
				SequenceFlow new_sf = Bpmn2Factory.eINSTANCE.createSequenceFlow();
				new_sf.setSourceRef(from);
				new_sf.setTargetRef(to);

				new_links.add(new_sf);
			}
			
			
			// aggiungo i nuovi elementi
			p.getFlowElements().addAll(new_elements);
			p.getFlowElements().addAll(new_links);			

			
			
			// find the plan where add the new elements
			// and erase outgoing links from the 
						
			// --------------- PER MODIFICARE IL DIAGRAM --------------- //
//			BPMNPlane plane_to_modify = null;			
//			BPMNDiagram d_to_modify = null;			
//			List<BPMNDiagram> diagrams = def.getDiagrams();
//			for (BPMNDiagram d : diagrams) {				
//				BPMNPlane plane = d.getPlane();	
//				for (DiagramElement de : plane.getPlaneElement()) {					
//					if (de instanceof BPMNShape) {
//						BPMNShape bpmns = (BPMNShape) de;
//						if (bpmns.getBpmnElement().getId().equals(from_elem)) {
//							d_to_modify = d;
//							plane_to_modify = plane;
//						}
//					}
//					if (de instanceof BPMNEdge && to_elem != null) {
//						BPMNEdge bpmne = (BPMNEdge) de;
//						for (SequenceFlow outgoing : outgoings) {
//							if (bpmne.getBpmnElement().getId().equals(outgoing.getId())) {
//								plane.getPlaneElement().remove(de);
//							}						
//						}
//					}
//				}				
//			}	
//			
//			BPMNShape shape = null;
//			BPMNEdge edge = null;
//			for (FlowNode elem : new_elements) {
//				shape = BpmnDiFactory.eINSTANCE.createBPMNShape();
//				shape.setBpmnElement(elem);		
//				plane_to_modify.getPlaneElement().add(shape);
//			}
//			for (SequenceFlow link : new_links) {
//				edge = BpmnDiFactory.eINSTANCE.createBPMNEdge();
//				edge.setBpmnElement(link);	
//				plane_to_modify.getPlaneElement().add(edge);
//			}
			// --------------- FINE PER MODIFICARE IL DIAGRAM --------------- //

			def.getDiagrams().removeAll(def.getDiagrams());	
			// devo cancellare il diagramma oggetto e poi crearne uno nuovo con tutti gli elementi che mi servono
			// salvo i cambiamenti
			try {
				Bpmn2Java.getResource().save(null);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	

	private FlowNode getFlowNode(String id) {
		Process p = null;
		FlowNode res = null;

		for (RootElement re : this.def.getRootElements()) {
			if (re instanceof Process) {
				p = (Process) re;
				for (FlowElement fe : p.getFlowElements()) {
					if (fe instanceof FlowNode) {
						if (fe.getId().equals(id)) {
							res = (FlowNode) fe;
							return res;
						}
					}
				}
			}
		}
		return null;
	}
}
