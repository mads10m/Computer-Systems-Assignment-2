# Instruction set
# LI R I -> ADDI R R0 I
# LD 0000 -> LD R R0 mem(R)
# SD 0001 -> SD R0 R mem(R)

# JNE 0010 T R R
# JZ T R -> JEQ T R R0
# JEQ 0011 T R R
# JR T -> JEQ T R0 R0

# MULI 0100 R R I
# ADD 0101 R R R
# ADDI 0110 R R I
# SUBI 0111 R R I

# END 1000

# TODO don't set registers to 0 (except if we read from them)
INIT:
	# LI R0, 0
	LI R1, 19 	#
	# LI R2, 0 	# x counter is register R2
	# LI R3, 0 	# y counter is register R3
	# LI R4, 0 	#
	# LI R5, 0 	#
	# LI R6, 0 	#
	# LI R7, 0 	#
	LI R8, 20	#

XLOOP:
	LI R3, 0
YLOOP:
	# Calc index of input pixel
	MULI R4, R3, 20
	ADD R5, R2, R4

	# Test if it is at the border
	# x = 0
	JZ SETBLACK, R2
	# x = 19
	JEQ SETBLACK, R2, R1
	# y = 0
	JZ SETBLACK, R3
	# y = 19
	JEQ SETBLACK, R3, R1

	# Load input pixel
	LD R4, R5


	# If image is black
	JZ WPIXEL, R4

	# If cross is not detected
	# x - 1
	SUBI R6, R5, 1
	LD R7, R6
	JZ SETBLACK, R7

	# x + 1
	ADDI R6, R5, 1
	LD R7, R6
	JZ SETBLACK, R7

	# y - 1
	SUBI R6, R5, 20
	LD R7, R6
	JZ SETBLACK, R7

	# y + 1
	ADDI R6, R5, 20
	LD R7, R6
	JZ SETBLACK, R7

	# R4 is already 255
	JR WPIXEL
SETBLACK:
	LI R4, 0
WPIXEL:
	ADDI R6, R5, 400
	SD R4, R6
CONTINUE:
	# Test if y is 20. Jump to YLOOP if true
	ADDI R3, R3, 1
	JNE YLOOP, R3, R8

	# Test if x is 20. Jump to XLOOP if true
	ADDI R2, R2, 1
	JNE XLOOP, R2, R8
HALT:
	END
