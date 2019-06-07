# thesis

the code is a java plug-in project

## import the code as java plug-in

## install emf dep in eclipse
https://www.vogella.com/tutorials/EclipseEMF/article.html 

## install bpmn dep in eclipse
https://www.eclipse.org/bpmn2-modeler/ 

## in manifest add dependencies
org.eclipse.bpmn2.edit 
org.eclipse.bpmn2.editor
org.eclipse.bpmn2.modeler.core
org.eclipse.bpmn2.modeler.help
org.eclipse.bpmn2.modeler.ui
org.eclipse.core.runtime

### tested on ubuntu and linux mint with openjdk8-jdk and eclipse ee


# to do
- aggiungere eccezione per quando si ha un solo process e nessuna collaboration
- è possibile che ci siano collaborations e procees allo stesso livello
- priorità :
  	   - gateway
	   - events
	   - lanes
	   - conversation
	   - coreography
- capire cosa cambia in conv, coll e process diagram
- controllare uml2pddl in activity diagram

v0.1 -> support events, activityes, gateways and connecting objects
v0.2 -> support swim lanes,
v0.3 -> support artifacts


exit code 1 -> output file already exist 