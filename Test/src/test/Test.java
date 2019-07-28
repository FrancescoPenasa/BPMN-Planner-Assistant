package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.util.Bpmn2ResourceFactoryImpl;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

public class Test {

	public static void main(String[] args) throws CoreException {
		File file = new File("/home/lithium/dev/eclipse-workspace/bpmnCollection/simple.bpmn2");
		// Create a resource set.
		ResourceSet resourceSet = new ResourceSetImpl();

		// Register the default resource factory -- only needed for stand-alone!
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("bpmn2",
				new Bpmn2ResourceFactoryImpl());

		// Register the package -- only needed for stand-alone!
		Bpmn2Package ecorePackage = Bpmn2Package.eINSTANCE;

		// Get the URI of the model file.
		URI fileURI = URI.createFileURI(file.getAbsolutePath());

		// Demand load the resource for this file.
		Resource resource = resourceSet.getResource(fileURI, true);

		// Print the contents of the resource to System.out.


		DocumentRoot doc = null;
		for (int i = 0; i < resource.getContents().size(); i++) {
			if (resource.getContents().get(i) instanceof DocumentRoot) {
				doc = (DocumentRoot) resource.getContents().get(i);
				break;
			}
		}
		
		Definitions def = null;
		if (doc != null) {
			def = doc.getDefinitions();
			for (RootElement re : def.getRootElements()) {
				
				if (re instanceof Process) {
					System.out.println(re.getId());
					Process p = (Process) re;
					for(FlowElement fe : p.getFlowElements()) {
						System.out.println(fe.getId());
					}
				}
				else {
					System.err.println(re.getId());
				}
			}
		}
		
		
		for (RootElement p : def.getRootElements()) {
			if (p instanceof Process) {
				for(FlowElement fe : ((Process)p).getFlowElements()) {
					// trovato il task da cui aggiungere modifiche
					if (fe.getId().equals("Task_1")) {
						System.out.println("vala mona");
						
						Task from = (Task) fe;
						Task task_to_add = Bpmn2Factory.eINSTANCE.createTask();
						
						from.setName("nuovo Task 2");

						task_to_add.setId("Task_7");
						task_to_add.setName("task aggiunto");
						
						
						// cambia i sequenceFlow in uscita da from per farli andare in task
						for (SequenceFlow sf : from.getOutgoing()) {						
							if (sf.getTargetRef() instanceof FlowElement) {
								sf.setTargetRef(task_to_add);
							}
						}
						
					}
				}
			}
			
		}
		
	}
}