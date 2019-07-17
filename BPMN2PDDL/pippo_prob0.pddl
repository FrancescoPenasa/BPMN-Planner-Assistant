;; problem file: pippo_prob0.pddl 

(define (problem pippo_prob0)
	(:domain pippo)

	(:objects
		robot1 robot2 robot3
	)

	(:init
		(at robot1) (has robot2 robot3)
	)

	(:goal
		(to roboton)
	)
)