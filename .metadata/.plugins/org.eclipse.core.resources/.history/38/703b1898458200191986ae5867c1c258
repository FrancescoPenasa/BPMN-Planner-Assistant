;; domain file: TESTFILE_domain.pddl 

(define (domain TESTFILE)
	(:requirements :typing)
	(:types Lane_1 task)
	(:predicates
		(has ?actor ?state)
		(at ?state))

	(:action create_new_account to login_new_account
		:parameters (?fromTask - task ?toTask - task)
		:precondition (and (at ?Task_1)(not (at ?Task_2)))
		:effect (and (at ?Task_2)(not (at ?Task_1))))

)