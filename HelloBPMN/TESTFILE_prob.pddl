;; problem file: TESTFILE_prob.pddl 

(define (problem TESTFILE)
	(:domain TESTFILE)

	(:objects
		StartEvent_1 - Task
		EndEvent_1 - Task
		Task_1 - Task
		Task_2 - Task
		Task_3 - Task
		StartEvent_2 - Task
		Task_4 - Task
	)

	(:init
		(has Process_1 StartEvent_1)
		(has Process_1 EndEvent_1)
		(has Process_1 Task_1)
		(has Process_1 Task_2)
		(has Process_1 SequenceFlow_1)
		(has Process_1 SequenceFlow_2)
		(has Process_1 SequenceFlow_3)
		(has Process_2 Task_3)
		(has Process_3 StartEvent_2)
		(has Process_3 Task_4)
		(has Process_3 SequenceFlow_4)
		(at StartEvent_1)
		(at StartEvent_2)
	)

	(:goal
		(at EndEvent_1)
	)

)