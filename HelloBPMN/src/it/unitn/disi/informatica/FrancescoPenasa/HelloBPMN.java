package it.unitn.disi.informatica.FrancescoPenasa;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.util.Bpmn2ResourceFactoryImpl;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

public class HelloBPMN {

	public static void main(String[] args) throws CoreException {

		String URL1 = new String("/home/ubuntu/dev/bpmnAndPddlEx/bpmn/Bp1.bpmn2");
		String URL2 = new String("/home/ubuntu/dev/bpmnAndPddlEx/bpmn/test.bpmn2");
		
		BPMNtoJAVA bpmn = new BPMNtoJAVA(URL2);
		
		List<Process> allProcess = bpmn.getAllProcess();
		for (Process p : allProcess) {
			System.out.println(p.getId());
		}
	}

}