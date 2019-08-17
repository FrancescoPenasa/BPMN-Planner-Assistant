package it.unitn.disi.informatica.FrancescoPenasa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.ParallelGateway;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.SequenceFlow;

/**
 * This Class operate on the file bpmn2 rappresented with Java Objects,
 * the purpose of MyBpmnGraph is to give some methods to visit a graph of a bpmn2 
 * file implementing well-known graphs search algorithms modified to work on bpmn2 files.
 * @author FrancescoPenasa
 *
 */
public class MyBpmnGraph {
	
	// ===================================== PARAMETERS ======================================= //
	
	// --------------------------------------- private ---------------------------------------- //		
	private List<List<String>> outputs; // stores the nodes and thery priority.
	private Definitions def;											// parameter to access the definitions of the bpmn2 file.
	
	
	
	// ==================================== constructor ====================================== //
	/**
	 * constructor: 
	 * use @param bpmn to initialize the class parameter "Definitions def".
	 */
	public MyBpmnGraph(Bpmn2Java bpmn) {
		def = bpmn.getDef();
	}
	
	

	// ====================================== METHODS ========================================= //
	
	// --------------------------------------- public ----------------------------------------- //
	/**
	 * Do a BFS on the bpmn2 file, save visited nodes in a a sorted List<List<FlowNode>>, 
	 * use special cases for ExclusiveGateway nodes and ParallelGateway nodes.
	 * 
	 * nodes retrived from the list of outgoings of a disjunting ExclusiveGateway (until the conjuncing ExclusiveGateway) 
	 * will be stored in the same List<FlowNode> that will be then added to the List<List<FlowNode>>.
	 * 
	 * nodes retrived from the list of outgoings of a disjunting ParallelGateway (until the conjuncing ParallelGateway) 
	 * will not be stored.
	 * 
	 * All the other FlowNode will be stored singularly in a new List<FlowNode> which will be added to the List<List<FlowNode>> 
	 * that contains every other List<FlowNode>.
	 * 
	 * @param from the ID of FlowNode from which the bfs will start.
	 */
	public void bpmn2_bfs(String from) {
		
		// declare DataStructures for the BFS
		Queue<FlowNode> S = new LinkedList<FlowNode>();
		Map<String, Boolean> visitato = new HashMap<String, Boolean>();
		
		
		// find the start FlowNode and init the DataStructures.
		FlowNode start = null;
		for (RootElement re : def.getRootElements()) {
			if (re instanceof Process) {	
				Process p = (Process) re;
				for (FlowElement fe : p.getFlowElements()) {
					if (fe instanceof FlowNode) {
						if (fe.getId().equals(from)) {
							start = (FlowNode)fe;
						}
					}
					visitato.put(fe.getId(), false);
				}
			}
		}			
		S.add(start);
		visitato.put(start.getId(), true);
		
		
		// booleans where to store if the node is related with a Gateway
		boolean parallelGateway = false; 	
		boolean exclusiveGateway = false;
		
		
		// List where to store the FlowNodes.
		outputs = new ArrayList<List<String>>();
		List<String> nodes = null;
		
		
		// cycle through all the nodes following the "FlowNode from"
		while(! S.isEmpty()) {
			FlowNode u = S.poll();
			
			// --- esamina il nodo --- //
			
			// if the node is not related to a ParallelGateway or ExclusiveGateway.
			if (! parallelGateway && ! exclusiveGateway) 						
			{ 						
				nodes = new ArrayList<String>();
				nodes.add(u.getId());
				outputs.add(nodes);
			} 
			
			// if the node is related to a ParallelGateway.	
			else if ((parallelGateway && exclusiveGateway)|| parallelGateway) 	
			{} 
			
			// if the node is related to a ExclusiveGateway and not related to a ParallelGateway.
			else 	
			{ 																	
				nodes.add(u.getId());
			}					
			
			// watch the type of the FlowNode			
			
			// if FlowNode is a ParallelGateway
			if (u instanceof ParallelGateway) 
			{
				// if the ParallelGateway is disjunctive
				if (u.getOutgoing().size() != 1) 
				{ 	
					parallelGateway = true;					
				}
				// if the ParallelGateway is not disjunctive
				else 
				{							
					parallelGateway = false;
				}
			}			
			
			// if FlowNode is a ExclusiveGateway
			if (u instanceof ExclusiveGateway) 
			{	
				// if the ExclusiveGateway is disjunctive
				if (u.getOutgoing().size() != 1) {
					exclusiveGateway = true;
					nodes = new ArrayList<String>();
					outputs.add(nodes);
				} 
				// if the ExclusiveGateway is not disjunctive
				else 
				{							
					exclusiveGateway = false;
				}
			}
			// ---------------------- //
		
			
			for (SequenceFlow sf : u.getOutgoing()) {
				// --- esamina l'arco --- //
				
				FlowNode dst = sf.getTargetRef();
				if (! visitato.get(dst.getId())) {
					visitato.put(dst.getId(), true);
					S.add(dst);
				}
				// ---------------------- //
			}
		}
	}
	
	/**
	 * getter for the ordered list of bpmnElements clustered by exclusiveGateway
	 * @return
	 */
	public List<List<String>> getBfsOrder(){
		return outputs;
	}
}

