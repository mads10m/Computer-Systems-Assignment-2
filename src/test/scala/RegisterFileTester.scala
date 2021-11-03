import chisel3._
import chisel3.iotesters
import chisel3.iotesters.PeekPokeTester

class RegisterFileTester(dut: RegisterFile) extends PeekPokeTester(dut) {

  // read R0=0 on reg1 and reg2, and write 16 to R1
  poke(dut.io.readReg1,0.U)
  poke(dut.io.readReg2,0.U)
  poke(dut.io.writeEnable,true.B)
  poke(dut.io.writeReg,1.U)
  poke(dut.io.writeData,16.U)
  expect(dut.io.data1,0.U)
  expect(dut.io.data2,0.U)
  step(1)

  // Read R1=16 and R0=0 to reg1 and reg2, and write 20 to R15
  poke(dut.io.readReg1,1.U)
  poke(dut.io.readReg2,0.U)
  poke(dut.io.writeEnable,true.B)
  poke(dut.io.writeReg,15.U)
  poke(dut.io.writeData,20.U)
  expect(dut.io.data1,16.U)
  expect(dut.io.readReg2,0.U)
  step(1)

  // Read R1=16 and R15=20, try and write 1 to R0
  poke(dut.io.readReg1,1.U)
  poke(dut.io.readReg2,15.U)
  poke(dut.io.writeEnable,true.B)
  poke(dut.io.writeReg,0.U)
  poke(dut.io.writeData,1.U)
  expect(dut.io.data1,16)
  expect(dut.io.data2,20)
  step(1)

  // Read R0=0 and write 255 to R15 with writeEnable=false
  poke(dut.io.readReg1,0.U)
  expect(dut.io.data1,0.U)
  poke(dut.io.writeEnable,false.B)
  poke(dut.io.writeReg,15.U)
  poke(dut.io.writeData,255.U)
  step(1)

  // Read R0=0 and Read R15=20
  poke(dut.io.readReg1,0.U)
  expect(dut.io.data1,0.U)
  poke(dut.io.readReg2,15.U)
  expect(dut.io.data2,20.U)
}

object RegisterFileTester {
  def main(args: Array[String]): Unit = {
    println("Testing Register File")
    iotesters.Driver.execute(
      Array("--generate-vcd-output", "on",
        "--target-dir", "generated",
        "--top-name", "ProgramCounter"),
      () => new RegisterFile()) {
      c => new RegisterFileTester(c)
    }
  }
}
