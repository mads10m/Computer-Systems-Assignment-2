import chisel3._
import chisel3.iotesters.PeekPokeTester

class ControlUnitTester(dut: ControlUnit) extends PeekPokeTester(dut) {
  println("LD")
  poke(dut.io.opcode, "b0000".U) // LD
  expect(dut.io.end, false.B)
  expect(dut.io.branchSel, false.B)
  expect(dut.io.branchInstruction, false.B)
  expect(dut.io.loadFromMem, true.B)
  expect(dut.io.registerWrite, true.B)
  expect(dut.io.aluSel, "b00".U)
  expect(dut.io.memoryWrite, false.B)
  step(1)

  println("SD")
  poke(dut.io.opcode, "b0001".U) // SD
  expect(dut.io.end, false.B)
  expect(dut.io.branchSel, false.B)
  expect(dut.io.branchInstruction, false.B)
  expect(dut.io.loadFromMem, false.B)
  expect(dut.io.registerWrite, false.B)
  expect(dut.io.immediate, false.B)
  expect(dut.io.aluSel, "b00".U)
  expect(dut.io.memoryWrite, true.B)
  step(1)

  println("JNE")
  poke(dut.io.opcode, "b0010".U) // JNE
  expect(dut.io.end, false.B)
  expect(dut.io.branchSel, "b01".U)
  expect(dut.io.branchInstruction, true.B)
  expect(dut.io.loadFromMem, false.B)
  expect(dut.io.registerWrite, false.B)
  expect(dut.io.immediate, false.B)
  expect(dut.io.aluSel, "b00".U)
  expect(dut.io.memoryWrite, false.B)
  step(1)

  println("JEQ")
  poke(dut.io.opcode, "b0011".U)
  expect(dut.io.end, false.B)
  expect(dut.io.branchSel, "b10".U)
  expect(dut.io.branchInstruction, true.B)
  expect(dut.io.loadFromMem, false.B)
  expect(dut.io.registerWrite, false.B)
  expect(dut.io.immediate, false.B)
  expect(dut.io.aluSel, "b00".U)
  expect(dut.io.memoryWrite, false.B)
  step(1)

  println("MULI")
  poke(dut.io.opcode, "b0100".U)
  expect(dut.io.end, false.B)
  expect(dut.io.branchSel, "b00".U)
  expect(dut.io.branchInstruction, false.B)
  expect(dut.io.loadFromMem, false.B)
  expect(dut.io.registerWrite, true.B)
  expect(dut.io.immediate, true.B)
  expect(dut.io.aluSel, "b10".U)
  expect(dut.io.memoryWrite, false.B)
  step(1)

  println("ADD")
  poke(dut.io.opcode, "b0101".U)
  expect(dut.io.end, false.B)
  expect(dut.io.branchSel, "b00".U)
  expect(dut.io.branchInstruction, false.B)
  expect(dut.io.loadFromMem, false.B)
  expect(dut.io.registerWrite, true.B)
  expect(dut.io.immediate, false.B)
  expect(dut.io.aluSel, "b00".U)
  expect(dut.io.memoryWrite, false.B)
  step(1)

  println("ADDI")
  poke(dut.io.opcode, "b0110".U)
  expect(dut.io.end, false.B)
  expect(dut.io.branchSel, "b00".U)
  expect(dut.io.branchInstruction, false.B)
  expect(dut.io.loadFromMem, false.B)
  expect(dut.io.registerWrite, true.B)
  expect(dut.io.immediate, true.B)
  expect(dut.io.aluSel, "b00".U)
  expect(dut.io.memoryWrite, false.B)
  step(1)

  println("SUBI")
  poke(dut.io.opcode, "b0111".U)
  expect(dut.io.end, false.B)
  expect(dut.io.branchSel, "b00".U)
  expect(dut.io.branchInstruction, false.B)
  expect(dut.io.loadFromMem, false.B)
  expect(dut.io.registerWrite,true.B)
  expect(dut.io.aluSel, "b01".U)
  expect(dut.io.memoryWrite,false.B)
  step(1)

  println("End")
  poke(dut.io.opcode,"b1000".U)
  expect(dut.io.end,true.B)
  expect(dut.io.branchSel,"b00".U)
  expect(dut.io.branchInstruction, false.B)
  expect(dut.io.loadFromMem,false.B)
  expect(dut.io.registerWrite,false.B)
  expect(dut.io.aluSel,"b00".U)
  expect(dut.io.memoryWrite,false.B)
}

object ControlUnitTester {
  def main(args: Array[String]): Unit = {
    println("Testing Control Unit")
    iotesters.Driver.execute(
      Array("--generate-vcd-output", "on",
        "--target-dir", "generated",
        "--top-name", "ProgramCounter"),
      () => new ControlUnit()) {
      c => new ControlUnitTester(c)
    }
  }
}