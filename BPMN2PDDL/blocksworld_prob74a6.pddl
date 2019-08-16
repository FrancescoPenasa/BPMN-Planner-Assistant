;; problem file: blocksworld_prob0.pddl 

(define (problem blocksworld_prob0)
	(:domain blocksworld)

	(:objects
		 a b c
	)

	(:init
		   (on-table a)(on-table b)(on-table c)  (clear b)(clear c)(free)
	)

	(:goal
		 (and (on b c))
	)
)