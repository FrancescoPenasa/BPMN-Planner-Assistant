	;; domain file: TESTFILE_domain.pddl 
	
	(define (domain TESTFILE)
		(:requirements :typing)
		(:types
			state
			task - state
			startevent - state
		)
	
		(:predicates
			(has ?owner ?state)
			(at ?state)
			(linked ?state ?state)
		)
	
		(:action StartMove 
			:parameters   (?from - state ?to - state) 
			:precondition (and (at ?from) (not (at ?to)) (linked ?from ?to)) 
			:effect       (and (at ?to) (not (at ?from)))) 
	
		(:action TaskMove 
			:parameters   (?from - state ?to - state) 
			:precondition (and (at ?from) (not (at ?to)) (linked ?from ?to)) 
			:effect       (and (at ?to) (not (at ?from)))) 
	
	)