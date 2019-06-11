;; domain file: TESTFILE_domain.pddl 

(define (domain TESTFILE)
	(:requirements :typing :strips)
	(:types
		Object
		flowObject - Object
		events - flowObject
		activities - flowObject
		gateways - flowObject
		incGateway - gateways
		exGateway - gateways
		parGateway - gateways
	)

	(:predicates
		(has ?owner ?state)
		(at ?state)
		(linked ?state ?state)
		(linked_Parallel_Gateway ?parGateway ?node)
		(linked_Inclusive_Gateway ?incGateway ?node)
		(linked_Exclusive_Gateway ?exGateway ?node)
	)

	(:action fromActivities 
		:parameters   (?from - activities ?to) 
		:precondition (and (at ?from) (not (at ?to)) (linked ?from ?to)) 
		:effect       (and (at ?to) (not (at ?from)))) 

	(:action fromEvent 
		:parameters   (?from - events ?to) 
		:precondition (and (at ?from) (not (at ?to)) (linked ?from ?to)) 
		:effect       (and (at ?to) (not (at ?from)))) 

	(:action fromInclusiveGateway 
		:parameters   (?from - incGateway ?to) 
		:precondition (and (at ?from) (not (at ?to)) (linked_Inclusive_Gateway ?from ?to)) 
		:effect       (and (at ?to) (not (linked_Inclusive_Gateway ?from ?to)))) 

	(:action fromExclusiveGateway 
		:parameters   (?from - exGateway ?to) 
		:precondition (and (at ?from) (linked_Exclusive_Gateway ?from ?to)) 
		:effect       (and  (at ?to) (not (at ?from)))) 

	(:action fromParallelGateway 
		:parameters   (?from - parGateway) 
		:precondition (at ?from) 
		:effect       (and  (not (at ?from)) 
			 (forall (?to - state) 
				 (when (linked_Parallel_Gateway ?from ?to)
				 (and (at ?to) )))))

)