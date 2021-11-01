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

  io.end := WireDefault(Bool(false))
  io.branchSel := WireDefault("b00".U)
  io.branchInstruction := WireDefault(Bool(false))
  io.loadFromMem := WireDefault(Bool(false))
  io.registerWrite := WireDefault(Bool(false))
  io.immediate := WireDefault(Bool(false))
  io.aluSel := WireDefault("b00".U)
  io.memoryWrite := WireDefault(Bool(false))

  switch(io.opcode){
    is("b0000".U) { // LD
      io.loadFromMem := Bool(true)
      io.registerWrite := Bool(true)
      }
    is("b0001".U) { // SD
      io.memoryWrite := Bool(true)
    }
    is("b0010".U) { //JNE
      io.branchSel := "b01".U
      io.branchInstruction := Bool(true)
    }
    is("b0011".U) { // JEQ
      io.branchSel := "b10".U
      io.branchInstruction := Bool(true)
    }
    is("b0100".U){ // MULI
      io.registerWrite := Bool(true)
      io.immediate := Bool(true)
      io.aluSel := "b10".U
    }
    is("b0101".B) { // ADD
      io.registerWrite := Bool(true)
      // aluSel := "b00".U
    }
    is("b0110") { // ADDI
      io.registerWrite := Bool(true)
      io.immediate := Bool(true)
    }
    is("b0111".U){ // MULI
      io.registerWrite := Bool(true)
      io.immediate := Bool(true)
      io.aluSel := "b01".U
    }
    is("b1000".U){ // END
      io.end := Bool(true)
    }
  }

}