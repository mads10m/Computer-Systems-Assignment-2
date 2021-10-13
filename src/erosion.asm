# TODO don't set registers to 0 (except if we read from them)
INIT:
	LI R0, 19 	#
	LI R1, 0 	# x counter is register R1
	LI R2, 0 	# y counter is register R2
	LI R3, 0 	#
	LI R4, 0 	#
	LI R5, 0 	#
	LI R5, 0 	#
	LI R6, 0 	#
	LI R7, 20	#

XLOOP:
	LI R2, 0
YLOOP:
	# Run the erosion
	# TODO remove border pixels


	# Calc index of input pixel
	# TODO can mabye use less registers
	MULI R3, R2, 20
	ADD R4, R1, R3

	# Test if it is at the border
	# x = 0
	JZ SETBLACK, R1
	# x = 19
	JEQ SETBLACK, R1, R0
	# y = 0
	JZ SETBLACK, R2
	# y = 19
	JEQ SETBLACK, R2, R0

	# Load inpiu pixel
	LD, R3, R4


	# If image is black
	JZ WPIXEL, R3

	# If cross is not detected
	# x - 1
	SUBI R5, R4, 1
	LD R6, R5
	JZ SETBLACK, R6

	# x + 1
	ADDI R5, R4, 1
	LD R6, R5
	JZ SETBLACK, R6

	# y - 1
	SUBI R5, R4, 20
	LD R6, R5
	JZ SETBLACK, R6

	# y + 1
	ADDI R5, R4, 20
	LD R6, R5
	JZ SETBLACK, R6

	# R3 is already 255
	JR WPIXEL
SETBLACK:
	LI R3, 0
WPIXEL:
	ADDI R5, R4, 400
	SD R3, R5
CONTINUE:
	# Test if y is 20. Jump to YLOOP if true
	ADDI R2, 1
	JNE YLOOP, R2, R7
	# Test if x is 20. Jump to XLOOP if true
	ADDI R1, 1
	JNE XLOOP, R1, R7
HALT:
	HALT
