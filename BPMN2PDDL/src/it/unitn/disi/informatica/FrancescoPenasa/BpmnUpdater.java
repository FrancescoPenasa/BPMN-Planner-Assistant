/**
 * 
 */
package it.unitn.disi.informatica.FrancescoPenasa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Auditing;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CategoryValue;
import org.eclipse.bpmn2.ChoreographyActivity;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.ConversationLink;
import org.eclipse.bpmn2.DataAssociation;
import org.eclipse.bpmn2.DataInputAssociation;
import org.eclipse.bpmn2.DataObject;
import org.eclipse.bpmn2.DataOutputAssociation;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.Documentation;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.bpmn2.ExtensionDefinition;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.InputOutputSpecification;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.LoopCharacteristics;
import org.eclipse.bpmn2.Monitoring;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.Property;
import org.eclipse.bpmn2.Relationship;
import org.eclipse.bpmn2.ResourceRole;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNPlane;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.ui.diagram.Bpmn2DiagramTypeProvider;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.FeatureMap;

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
	 * @param task_id_from
	 * @param task_id_to
	 */
	public BpmnUpdater(List<String> lis, Bpmn2Java bpmn, String url, String task_id_from, String task_id_to) {
		
		createBPMNBackup(url);
			
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
					if (fe.getId().equals(task_id_from)) {
						fe.setName(fe.getName() + " interrupted");
						
						if (fe instanceof FlowNode) {
							FlowNode fn = (FlowNode) fe;
							outgoings = fn.getOutgoing();
							sf.setSourceRef(fn);
							sf.setTargetRef(new_states.get(0));
						} else {
							System.err.println(task_id_from + " is not an Activity or Event or Gateway, it cannot be a \"state\".");
							System.exit(-10); // TODO  ENUM
						}											
					}
					
					// se il flowelement e' quello in cui voglio arrivare con i nuovi stati
					if (fe.getId().equals(task_id_to)) {
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
		if (task_id_to != null) {
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


	/**
	 * Crea un backup del file bpmn2 nella stessa cartella in cui 
	 * e' presente il file originale.
	 * @param source url del file 
	 */
	private void createBPMNBackup(String source) {
		String dst = source.replaceAll(".bpmn2", "_original.bpmn2");
		
		Path FROM = Paths.get(source);
        Path TO = Paths.get(dst);
        
        // overwrite the destination file if it exists, and copy
        // the file attributes, including the rwx permissions
        CopyOption[] options = new CopyOption[]{
          StandardCopyOption.REPLACE_EXISTING,
          StandardCopyOption.COPY_ATTRIBUTES
        }; 
        
		try {
			Files.copy(FROM, TO, options);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
	}

}
