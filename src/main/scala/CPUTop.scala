import chisel3._
import chisel3.util._

class CPUTop extends Module {
  val io = IO(new Bundle {
    val done = Output(Bool ())
    val run = Input(Bool ())
    //This signals are used by the tester for loading and dumping the memory content, do not touch
    val testerDataMemEnable = Input(Bool ())
    val testerDataMemAddress = Input(UInt (16.W))
    val testerDataMemDataRead = Output(UInt (32.W))
    val testerDataMemWriteEnable = Input(Bool ())
    val testerDataMemDataWrite = Input(UInt (32.W))
    //This signals are used by the tester for loading and dumping the memory content, do not touch
    val testerProgMemEnable = Input(Bool ())
    val testerProgMemAddress = Input(UInt (16.W))
    val testerProgMemDataRead = Output(UInt (32.W))
    val testerProgMemWriteEnable = Input(Bool ())
    val testerProgMemDataWrite = Input(UInt (32.W))
  })

  //Creating components
  val programCounter = Module(new ProgramCounter())
  val dataMemory = Module(new DataMemory())
  val programMemory = Module(new ProgramMemory())
  val registerFile = Module(new RegisterFile())
  val controlUnit = Module(new ControlUnit())
  val alu = Module(new ALU())

  //Connecting the modules
  programCounter.io.run := io.run
  programMemory.io.address := programCounter.io.programCounter

  ////////////////////////////////////////////
  //Continue here with your connections
  ////////////////////////////////////////////
  controlUnit.io.opcode := programMemory.io.instructionRead(32,29)
  programCounter.io.stop := controlUnit.io.end
  programCounter.io.programCounterJump := programMemory.io.instructionRead(28,23)
  
  // Register input
  when(controlUnit.io.branchInstruction){
    registerFile.io.readReg1 := programMemory.io.instructionRead(22,19)
    registerFile.io.readReg2 := programMemory.io.instructionRead(18,15)
  }.otherwise{
    registerFile.io.readReg1 := programMemory.io.instructionRead(24,21)
    registerFile.io.readReg2 := programMemory.io.instructionRead(20,17)
  }
  registerFile.io.writeReg := programMemory.io.instructionRead(28,25)
  registerFile.io.writeEnable := controlUnit.io.registerWrite

  // ALU Input
  alu.io.op1 := registerFile.io.data1
  alu.io.sel := controlUnit.io.aluSel
  alu.io.op2:= Mux(controlUnit.io.immediate, Cat(Fill(16."b0"),programMemory.io.instructionRead(20,16)), registerFile.io.data2)

  // Program counter jump mux
  switch (controlUnit.io.branchSel){
    is("b00") {programCounter.io.jump := Bool(false)}
    is("b01") {programCounter.io.jump := !alu.io.compRes}
    is("b10") {programCounter.io.jump := alu.io.compRes}
  }

  // DATA MEMORY Input
  dataMemory.io.address := registerFile.io.data2(15,0)
  dataMemory.io.dataWrite := registerFile.io.data1
  dataMemory.io.writeEnable := controlUnit.io.memoryWrite


  // Register write data mux
  registerFile.io.writeData := Mux(controlUnit.io.loadFromMem, dataMemory.io.dataRead, alu.io.res)


  //This signals are used by the tester for loading the program to the program memory, do not touch
  programMemory.io.testerAddress := io.testerProgMemAddress
  io.testerProgMemDataRead := programMemory.io.testerDataRead
  programMemory.io.testerDataWrite := io.testerProgMemDataWrite
  programMemory.io.testerEnable := io.testerProgMemEnable
  programMemory.io.testerWriteEnable := io.testerProgMemWriteEnable
  //This signals are used by the tester for loading and dumping the data memory content, do not touch
  dataMemory.io.testerAddress := io.testerDataMemAddress
  io.testerDataMemDataRead := dataMemory.io.testerDataRead
  dataMemory.io.testerDataWrite := io.testerDataMemDataWrite
  dataMemory.io.testerEnable := io.testerDataMemEnable
  dataMemory.io.testerWriteEnable := io.testerDataMemWriteEnable
}