import chisel3._
import chisel3.iotesters
import chisel3.iotesters.PeekPokeTester

class ALUTester(dut: ALU) extends PeekPokeTester(dut){

  // 2 + 3 = 5
  // 2 == 3 = false
  poke(dut.io.op1, 2.U)
  poke(dut.io.op2, 3.U)
  poke(dut.io.sel, "b00".U)
  step(1)
  expect(dut.io.res, 5.U)
  expect(dut.io.compRes, false.B)

  // 3 - 3 = 0
  // 3 == 3 = true
  poke(dut.io.op1, 3.U)
  poke(dut.io.op2, 3.U)
  poke(dut.io.sel, "b01".U)
  step(1)
  expect(dut.io.res, 0.U)
  expect(dut.io.compRes, true.B)

  //
  poke(dut.io.op1, 10.U)
  poke(dut.io.op2, 5.U)
  poke(dut.io.sel, "b10".U)
  step(1)
  expect(dut.io.res, 50.U)
  expect(dut.io.compRes, false.B)
}

object ALUTester {
  def main(args: Array[String]): Unit = {
    println("Testing ALU")
    iotesters.Driver.execute(
      Array("--generate-vcd-output", "on",
        "--target-dir", "generated",
        "--top-name", "ProgramCounter"),
      () => new ALU()) {
      c => new ALUTester(c)
    }
  }
}
