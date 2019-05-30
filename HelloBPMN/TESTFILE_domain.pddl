;; domain file: TESTFILE_domain.pddl 

(define (domain TESTFILE)
	(:requirements :typing)
	(:types Task)
	(:predicates
		(has ?owner ?state)
		(at ?state)
		(linked ?state ?state))

	(:action Start_Event_1_To_Task_1
		:parameters (?StartEvent_1 - Task ?Task_1 - Task)
		:precondition (and (at ?StartEvent_1)(not (at ?Task_1))(linked ?StartEvent_1 ?Task_1))
		:effect (and (at ?Task_1)(not (at ?StartEvent_1))))

	(:action Start_Event_2_To_Task_4
		:parameters (?StartEvent_2 - Task ?Task_4 - Task)
		:precondition (and (at ?StartEvent_2)(not (at ?Task_4))(linked ?StartEvent_2 ?Task_4))
		:effect (and (at ?Task_4)(not (at ?StartEvent_2))))

	(:action Task_1_To_Task_2
		:parameters (?Task_1 - Task ?Task_2 - Task)
		:precondition (and (at ?Task_1)(not (at ?Task_2))(linked ?Task_1 ?Task_2))
		:effect (and (at ?Task_2)(not (at ?Task_1))))

	(:action Task_2_To_End_Event_1
		:parameters (?Task_2 - Task ?EndEvent_1 - Task)
		:precondition (and (at ?Task_2)(not (at ?EndEvent_1))(linked ?Task_2 ?EndEvent_1))
		:effect (and (at ?EndEvent_1)(not (at ?Task_2))))

	(:action Task_1_To_Task_3
		:parameters (?Task_1 - Task ?Task_3 - Task)
		:precondition (and (at ?Task_1)(not (at ?Task_3))(linked ?Task_1 ?Task_3))
		:effect (and (at ?Task_3)(not (at ?Task_1))))

	(:action Task_3_To_Task_2
		:parameters (?Task_3 - Task ?Task_2 - Task)
		:precondition (and (at ?Task_3)(not (at ?Task_2))(linked ?Task_3 ?Task_2))
		:effect (and (at ?Task_2)(not (at ?Task_3))))

	(:action Task_4_To_Task_3
		:parameters (?Task_4 - Task ?Task_3 - Task)
		:precondition (and (at ?Task_4)(not (at ?Task_3))(linked ?Task_4 ?Task_3))
		:effect (and (at ?Task_3)(not (at ?Task_4))))

)