;; problem file: gripper_prob0.pddl 

(define (problem gripper_prob0)
	(:domain gripper)

	(:objects
		roomA - ROOM roomB - ROOM roomC - ROOM roomD - ROOM
            o  - OBJ 
            b  - BOX 
            old1 - OBJ 
            old2 - OBJ 
            r - ROBOT 
            t - TRUCK
            left - GRIPPER right - GRIPPER
	)

	(:init
		(holding old1 left) (holding old2 right)
    (at r roomB) (at o roomB) (at b roomC) (at t roomD)
    (free-box b)
	)

	(:goal
		(and (holding o left))
	)
)