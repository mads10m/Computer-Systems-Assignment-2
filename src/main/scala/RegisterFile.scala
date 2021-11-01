import chisel3._
import chisel3.util._

class RegisterFile extends Module {
  val io = IO(new Bundle {
    //Define the module interface here (inputs/outputs)
    val reg1 = Input(UInt(4.U))
    val reg2 = Input(UInt(4.U))
    val writeReg = Input(UInt(4.U))
    val data = Input(UInt(32.U))
    
    val writeEnable = Input(Bool())

    val OutData1 = Output(UInt(32.U))
    val OutData2 = Output(UInt(32.U))
  })

  //Implement this module here
  val registers = Reg(Vec(16, UInt(32.U)))
  // R0 = 0
  registers(0) := 0.U

  // Overwirte register 
  when(io.writeEnable) {
    // Don't overwirte R0
    when(io.reg1 > 0.U) {
      register(io.reg1) := io.reg1
    }
    // Dont know about reg2...
    // Don't overwirte R0
    //when(io.reg2 > 0.U) {
    //  register(io.reg2) := io.reg2
    //}
  }

  // Set output
  io.Output1 := registers(io.reg1)
  io.Output2 := registers(io.reg2)

}