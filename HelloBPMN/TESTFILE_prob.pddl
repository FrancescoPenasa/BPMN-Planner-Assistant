;; problem file: TESTFILE_prob.pddl 

(define (problem TESTFILE)
	(:domain TESTFILE)

	(:objects
		StartEvent_1 - task
		EndEvent_1 - task
		Task_1 - task
		Task_2 - task
		Task_3 - task
		StartEvent_2 - task
		Task_4 - task
	)

	(:init
		(has Process_1 StartEvent_1)
		(linked StartEvent_1 Task_1)
		(has Process_1 EndEvent_1)
		(has Process_1 Task_1)
		(linked Task_1 Task_2)
		(has Process_1 Task_2)
		(linked Task_2 EndEvent_1)
		(has Process_1 SequenceFlow_1)
		(has Process_1 SequenceFlow_2)
		(has Process_1 SequenceFlow_3)
		(has Process_2 Task_3)
		(has Process_3 StartEvent_2)
		(linked StartEvent_2 Task_4)
		(has Process_3 Task_4)
		(has Process_3 SequenceFlow_4)
		(at StartEvent_1)
		(at StartEvent_2)
	)

	(:goal
		(at EndEvent_1)
	)

)