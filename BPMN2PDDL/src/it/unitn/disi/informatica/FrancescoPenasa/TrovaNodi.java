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

public class TrovaNodi {
	

	private List<List<String>> outputs = new ArrayList<List<String>>();
	
	

	public TrovaNodi(Bpmn2Java bpmn, String from) {
		Definitions def = bpmn.getDef();

		// find starting element		
		bpmn2_bfs(from, def);
		
		
	}

	private void bpmn2_bfs(String from, Definitions def) {
		
		Queue<FlowNode> S = new LinkedList<FlowNode>();
		Map<String, Boolean> visitato = new HashMap<String, Boolean>();
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
		
		boolean parallelGateway = false;
		boolean exclusiveGateway = false;
		outputs = new ArrayList<List<String>>();
		List<String> nodes = null;
		// cycle through all the following node
		while(! S.isEmpty()) {
			FlowNode u = S.poll();
			
			// esamina il nodo
			if (! parallelGateway && ! exclusiveGateway) {
				nodes = new ArrayList<String>();
				nodes.add(u.getId());
				outputs.add(nodes);
			}
			else if ((parallelGateway && exclusiveGateway)|| parallelGateway) {
				// do nothing, parallelGateway are lame!
			} 
			else {
				nodes.add(u.getId());
			}
			
			// se e' un parallelgateweay e un exclusivegateway allora i prossimi nodi saranno valutati in modo diverso
			if (u instanceof ParallelGateway) {
				if (u.getOutgoing().size() != 1) { 	// starter parallel
					parallelGateway = true;
				} else {							// finish parallel
					parallelGateway = false;
				}
			}
			if (u instanceof ExclusiveGateway) {	// starter exc
				if (u.getOutgoing().size() != 1) {
					exclusiveGateway = true;
					nodes = new ArrayList<String>();
					outputs.add(nodes);
				} else {							// finish parallel
					exclusiveGateway = false;
				}
			}
			
			
			
			
			for (SequenceFlow sf : u.getOutgoing()) {
				// esamina l'arco
				
				FlowNode dst = sf.getTargetRef();
				if (! visitato.get(dst.getId())) {
					visitato.put(dst.getId(), true);
					S.add(dst);
				}
			}
		}
	}
	
	public List<List<String>> getOutputs(){
		return outputs;
	}
}

