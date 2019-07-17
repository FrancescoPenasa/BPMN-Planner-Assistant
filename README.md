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

non vengono presi in considerazione i nomi degli oggetti, eventuali specificità come la cardinalità dei sequenceFlow non è considerata

complex gateway

dev'essere supportato il pacchetto
	> :typing :conditional-effects

flow rappresentati as predicates sat

sistemare gateway direction, oppure assumere che i gateway siano sempre

[ASSUMPTION] messageFlow non può essere un input di Gateway, 
[ASSUMPTION] starEvent non può andare in gateway
[ASSUMPTION] gateways prende in incom e outcom solo cose presenti nella propria pool process 

writeInit_LinkedGateways can be generalized

[BUG] da aggiungere a object Process_1 

exclusiveGateway può essere trattato come uno stato normale

if ONE lane -> only process 
if more lanes --> collaborations, than message flows

DISEGNINO DELLA TYPE HIEARERCHY

il tipo di azione da intraprendere dipende dallo stato in cui si è (at ?x)

message and associations

input option to activate obbligatory intermediateEvent
input option to activate intermediateEvent as endEvent
input option to considerate subprocess
input option to considerate subchoreography
event-based works as exclusive
parallel-event based works as parallel gateway
join  parallel gateway needs to see all the inputs

exclusive con condition è già sistemato 

todo messages

coreography task is an task
collapsed subprocess is a task
[ASSUMPTION] data object funziona sempre
ENDEVENT secondo la documentazione non ha uscite
start event non ha uscite

messageflow può usare eventi di tipo message

v0.1 support process and sequence flow
v0.2 support collaborations and message flow
v0.3 support messages and associations
1 support coreographyes

pagina 79




# v 2.0 

- in input va ->
	- bpmn2 file
	- init state in pddl where the process stopped
	- pddl domain for the bpmn2 file
	- goal states (possono essere iterati per trovare una soluzione)
	- (optional) more ppdl domain about the envirorment (less priority)
	- (optional) again more pddl domain
	- (optional) parametri di preferenza with || per avere più scelte o

- output va ->
	- path trovato con il minor numero di passaggi
	- paths trovati con i parametri di preferenza 

## input_manager 
> DONE

## BPMN2 EXTRACTOR
> DONE

## REACH GOAL FROM INIT WITH PDDL SUPER_DOMAIN
	 ### ASDF
	 ### GENERATE PDDL_PROB_FILE
	 ##### OBJ
	 ##### INIT
	 ##### GOAL

	 ### EXE a PLANNER IN JAVA 

## LOOK AT THE RESULT

## REACH GOAL FROM INIT WITH PDDL DOMAIN

## LOOK AT THE RESULT

## REACH GOAL FROM INIT WITH PDDL SUBDOMAIN

## LOOK AT THE RESULTS

## USE THE RESULTS TO GENERATE A NEW BPMN FILE

## PRINT SOMETHING MEANINGFUL