package it.unitn.disi.informatica.FrancescoPenasa;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.util.Bpmn2ResourceFactoryImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * convert bpmn2 elements from the file URL in java Objects 
 * and gives some getter and setter to have access to the file BpmnElements.
 * 
 * @author FrancescoPenasa
 * 
 */
public class Bpmn2Java {


	// ============ PARAMETERS ============== //
	// ------------ private ------------- //
	private static Definitions def;
	private static Resource resource;
	
	/** constructor **/
	public Bpmn2Java (){		
		
	}
	
	// ============ METHODS ============== //
	// ------------ public ------------- //
	/**
	 * convert bpmn2 elements from the file URL in java Objects
	 * @param URL path to the bpmn2 file
	 */
	public void init(String URL) {
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
		this.setResource(resourceSet.getResource(fileURI, true));
		
		try {
			getResource().save(Collections.EMPTY_MAP);
		} catch (IOException e) {}

		DocumentRoot doc = null;
		for (int i = 0; i < getResource().getContents().size(); i++) {
			if (getResource().getContents().get(i) instanceof DocumentRoot) {
				doc = (DocumentRoot) getResource().getContents().get(i);
				break;
			}			
		}		
		if (doc != null) {
			setDef(doc.getDefinitions());
		}			
	}

		
	
	// --- SETTER AND GETTER METHODS --- //
	public static Definitions getDef() {
		return def;
	}

	private static void setDef(Definitions def) {
		Bpmn2Java.def = def;
	}

	public static Resource getResource() {
		return resource;
	}

	public static void setResource(Resource resource) {
		Bpmn2Java.resource = resource;
	}
	
	/**
	 * get All Process in an ArrayList from the bpmn2 file
	 * @return ArrayList<Process>
	 */
	public List<Process> getAllProcess() {
		List<Process> allProcess = new ArrayList<Process>();
		for (RootElement re : getDef().getRootElements()) {
			if (re instanceof Process) {
				allProcess.add((Process)re);				
			}
		}
		return allProcess;
	}
	
	// -------- end of the class ----------- //	
}

