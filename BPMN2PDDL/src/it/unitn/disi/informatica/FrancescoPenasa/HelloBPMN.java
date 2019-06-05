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

	private static String domain_file;
	private static String prob_file;
	private static String URL;

	//can easily work from cli
	private static void input_manager(String[] args) {
		for (int i = 2; i< args.length; i++){
			String arg = args[i];
			switch (arg) {
			case "-i":
				URL = args[++i];
				break;
				
			case "-d":
				domain_file = args[++i];
				break;
				
			case "p":
				prob_file = args[++i];	
				break;			
			default:
				break;
			}
		}
		for (int i = 0 ; i<args.length ; i++)
			System.out.println(args[i]);
	}
	
	
	
	public static void main(String[] args) throws CoreException, IOException {
		
		// manage input
		input_manager(args);
		
		// tmp URL FOR TEST
		String URL1 = new String("/home/francesco/dev/eclipse-workspace/bpmnCollection/test.bpmn2");
		String URL2 = new String("/home/ubuntu/dev/bpmnAndPddlEx/bpmn/test.bpmn2");
		
		// extract from bpmn
		BPMNtoJava bpmn = new BPMNtoJava(URL1);
		
		// generate domain
		// and output domain   /*class for generate*/
		DomainGenerator dg = new DomainGenerator(bpmn, "TESTFILE");
		
		// input modifier if neededdd
		
		
		//TODO ADD A PREDICATE wich tell you where states are linked
		// to write it in actions, predicates, add a type, and prob init
		// generate prob file
		// output prob file
		
		// change how action works
		ProblemGenerator pg = new ProblemGenerator(bpmn, "TESTFILE");
		
	}
}
	
