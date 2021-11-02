import chisel3._
import chisel3.util._

class ControlUnit extends Module {
  val io = IO(new Bundle {
    val opcode = Input(UInt(4.W))

    val end = Output(Bool())
    val branchSel = Output(UInt(2.W))
    val branchInstruction = Output(Bool())
    val loadFromMem = Output(Bool())
    val registerWrite = Output(Bool())
    val immediate = Output(Bool())
    val aluSel = Output(UInt(2.W))
    val memoryWrite = Output(Bool())
  })

  io.end := WireDefault(false.B)
  io.branchSel := WireDefault("b00".U)
  io.branchInstruction := WireDefault(false.B)
  io.loadFromMem := WireDefault(false.B)
  io.registerWrite := WireDefault(false.B)
  io.immediate := WireDefault(false.B)
  io.aluSel := WireDefault("b00".U)
  io.memoryWrite := WireDefault(false.B)

  switch(io.opcode) {
    is("b0000".U) { // LD
      io.loadFromMem := true.B
      io.registerWrite := true.B
    }
    is("b0001".U) { // SD
      io.memoryWrite := true.B
    }
    is("b0010".U) { //JNE
      io.branchSel := "b01".U
      io.branchInstruction := true.B
    }
    is("b0011".U) { // JEQ
      io.branchSel := "b10".U
      io.branchInstruction := true.B
    }
    is("b0100".U) { // MULI
      io.registerWrite := true.B
      io.immediate := true.B
      io.aluSel := "b10".U
    }
    is("b0101".U) { // ADD
      io.registerWrite := true.B
      // aluSel := "b00".U
    }
    is("b0110".U) { // ADDI
      io.registerWrite := true.B
      io.immediate := true.B
      // aluSel := "b00".U
    }
    is("b0111".U) { // SUBI
      io.registerWrite := true.B
      io.immediate := true.B
      io.aluSel := "b01".U
    }
    is("b1000".U) { // END
      io.end := true.B
    }
  }

}
