/**
 * 
 */
package it.unitn.disi.informatica.FrancescoPenasa;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.util.Bpmn2ResourceFactoryImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * @author ubuntu
 *
 */
public class BPMNtoJAVA {

	/**
	 * 
	 */
	
	private Definitions def;
	
	public BPMNtoJAVA(String URL){
		/** does everything that is needed to import a bpmn2 file in java **/
		
		File file = new File(URL);
		
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

		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
		}

		
		DocumentRoot doc = null;
		for (int i = 0; i < resource.getContents().size(); i++) {
			if (resource.getContents().get(i) instanceof DocumentRoot) {
				doc = (DocumentRoot) resource.getContents().get(i);
				break;
			}
		}

		if (doc != null) {
			def = doc.getDefinitions();
			System.out.println(def.getRootElements());
			//print 
			for (RootElement re : def.getRootElements()) {
				if (re instanceof Process) {
					//System.err.println("Parent " + re.getId());
					Process p = (Process) re;
					for(FlowElement fe : p.getFlowElements()) {
						//System.out.println("child " + fe.getId());
					}
				}
				else {
					//System.err.println(re.getId());
				}
			}
		}
	}
	
	public List<Process> getAllProcess() {
		List<Process> allProcess = new ArrayList<Process>();
		for (RootElement re : def.getRootElements()) {
			if (re instanceof Process) {
				allProcess.add((Process)re);
			}
		}
		return allProcess;
	}
}

