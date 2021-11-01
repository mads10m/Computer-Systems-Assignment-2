import chisel3._
import chisel3.util._

class ALU extends Module {
  val io = IO(new Bundle {
    //Define the module interface here (inputs/outputs)
    val io = IO (new Bundle {
      val operand1 = Input(UInt(32.U))
      val operand2 = Input(UInt(32.U))
      val ALUsel   = Input(UInt(2.U))

      val Result   = Output(UInt(32.U))
      val zeroFlag = Output(Bool())
      val legFlag  = Output(Bool())
      val greFlag  = Output(Bool())
    })
  })

  //Implement this module here
  io.Result := WireDefault(0.U)

  switch(io.ALUsel) {
    is(0.U) {
      sum := operand1 + operand2
    }
    is(1.U) {
      sum := operand1 - operand2

    }
    is(2.U) {
      sum := operand1 * operand2
    }
  }


  io.Result := sum

  // Set flags
  io.zeroFlag := sum === 0
  io.greFlag := sum > 0
  io.legFlag := sum < 0
}