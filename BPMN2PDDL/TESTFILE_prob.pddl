;; problem file: TESTFILE_prob.pddl 

(define (problem TESTFILE)
	(:domain TESTFILE)

	(:objects
		EndEvent_1 - events
		Task_2 - activities
		StartEvent_2 - events
		Task_4 - activities
		Task_8 - activities
		Task_5 - activities
		ExclusiveGateway_1 - exGateway)

	(:init
		(has Process_1 EndEvent_1)
		(has Process_1 Task_2)
		(has Process_1 SequenceFlow_3)
		(has Process_3 StartEvent_2)
		(has Process_3 Task_4)
		(has Process_3 SequenceFlow_4)
		(has Process_3 Task_8)
		(has Process_3 ExclusiveGateway_1)
		(has Process_3 SequenceFlow_2)
		(has Process_3 SequenceFlow_5)
		(has Process_3 Task_5)
		(has Process_3 SequenceFlow_6)
		(linked Task_2 EndEvent_1)
		(linked StartEvent_2 Task_4)
		(linked Task_4 ExclusiveGateway_1)
		(linked Task_8 Task_2)
		(linked Task_5 EndEvent_1)
		(at StartEvent_2)
		(linked_Exclusive_Gateway ExclusiveGateway_1 Task_8)
		(linked_Exclusive_Gateway ExclusiveGateway_1 Task_5))

	(:goal
		(at EndEvent_1))
)