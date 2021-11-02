  # load imediate and store it in memory 399
  LI R1 255
  LI R2 400
  SD R1 R2
  # Tested SD & LI (and therefore also ADDI)

  # load from memory 104 (white pixel) and store it at memory 400
  LI R3 104
    # Add 1 to R2 to increment memory pointer
  ADDI R2 R2 1
  LD R1 R3
  SD R1 R2
  # Tested LD

  JEQ FIRST R1 R1
  END
FIRST:
  ADDI R2 R2 1
  SD R1 R2
  # Tested JEQ

  JNE SECOND R0 R1
  END
SECOND:
  ADDI R2 R2 1
  SD R1 R2
  # Tested JNE

  JZ THIRD R0
  END
THIRD:
  ADDI R2 R2 1
  SD R1 R2
  # Tested JZ

  JR FOURTH
  END
FOURTH:
  ADDI R2 R2 1
  SD R1 R2
  # Tested JR

  LI R1 1
  MULI R1 R1 255
  ADDI R2 R2 1
  SD R1 R2
  # Tested MULI

  LI R1 0
  LI R5 255
  ADD R1 R0 R5
  ADDI R2 R2 1
  SD R1 R2
  # Tested ADD

  LI R5 510
  SUBI R1 R5 255
  ADDI R2 R2 1
  SD R1 R2
  # Tested SUBI

  END
  # Nine tests