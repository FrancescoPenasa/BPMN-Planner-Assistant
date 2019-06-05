;; problem file: TESTFILE_prob.pddl 

(define (problem TESTFILE)
	(:domain TESTFILE)

	(:objects
		StartEvent_1 - state
		EndEvent_1 - state
		Task_1 - state
		Task_2 - state
		Task_3 - state
		StartEvent_2 - state
		Task_4 - state)

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
		(linked StartEvent_1 Task_1)
		(linked Task_1 Task_2)
		(linked Task_2 EndEvent_1)
		(linked Task_1 Task_3)
		(linked Task_3 Task_2)
		(linked Task_4 Task_3)
		(linked Task_1 Task_3)
		(linked Task_3 Task_2)
		(linked Task_4 Task_3)
		(linked StartEvent_2 Task_4)
		(linked Task_1 Task_3)
		(linked Task_3 Task_2)
		(linked Task_4 Task_3)
		(at StartEvent_1)
		(at StartEvent_2))

	(:goal
		(at EndEvent_1))
)