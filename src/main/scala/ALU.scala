import chisel3._
import chisel3.util._

class ALU extends Module {
  val io = IO(new Bundle {
    val sel = Input(UInt(2.W))
    val op1 = Input(UInt(32.W))
    val op2 = Input(UInt(32.W))
    val res = Output(UInt(32.W))
    val compRes = Output(Bool())
  })

  //Implement this module here

  io.res := WireDefault(0.U)

  switch(io.sel) {
    is("b00".U) { io.res := io.op1 + io.op2 }
    is("b01".U) { io.res := io.op1 - io.op2 }
    is("b10".U) { io.res := io.op1 * io.op2 }
  }

  io.compRes := io.op1 === io.op2
}
