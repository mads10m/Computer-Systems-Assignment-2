import chisel3._
import chisel3.util._

class ControlUnit extends Module {
  val io = IO(new Bundle {
    //Define the module interface here (inputs/outputs)
    val opcode = Input(UInt(4.U))

    val end = Output(Bool())
    val BranchSEL = Output(UInt(2.U))
    val loadFromMem = Output(Bool())
    val registerWrite = Output(Bool())
    val immediate = Output(Bool())
    val ALUsel = Output(UInt(2.U))
    val memoryWrite = Output(Bool())
  })

  //Implement this module here

}