;; problem file: pippo_with_condition1_price.pddl 

(define (problem pippo_with_condition1_price)
	(:domain pippo_with_condition1)

	(:objects
		robot1 - t robot2 - r robot3
	)

	(:init
		(at robot1) (has robot2 robot3)
		(= (price) 0)
	)

	(:goal
		(to roboton)
	)
	(:metric 
		maximize (price)
	)
)