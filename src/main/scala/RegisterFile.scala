import chisel3._
import chisel3.util._

class RegisterFile extends Module {
  val io = IO(new Bundle {
    val readReg1 = Input(UInt(4.W))
    val readReg2 = Input(UInt(4.W))
    val writeEnable = Input(Bool())
    val writeReg = Input(UInt(4.W))
    val writeData = Input(UInt(32.W))
    val data1 = Output(UInt(32.W))
    val data2 = Output(UInt(32.W))
  })
  
  val regFile = RegInit(VecInit(Seq.fill(16)(0.U(32.W))))

  regFile(0) := 0.U

  io.data1 := regFile(io.readReg1)
  io.data2 := regFle(io.readReg2)
  when (io.writeEnable){
    regFile(io.writeReg) := io.writeData
  }
}