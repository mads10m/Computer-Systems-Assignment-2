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
  controlUnit.io.opcode := programMemory.io.instructionRead(31,28)
  programCounter.io.stop := controlUnit.io.end
  programCounter.io.programCounterJump := programMemory.io.instructionRead(27,22)
  
  // Register input
  when(controlUnit.io.branchInstruction){
    registerFile.io.readReg1 := programMemory.io.instructionRead(21,18)
    registerFile.io.readReg2 := programMemory.io.instructionRead(17,14)
  }.otherwise{
    registerFile.io.readReg1 := programMemory.io.instructionRead(23,20)
    registerFile.io.readReg2 := programMemory.io.instructionRead(19,16)
  }
  registerFile.io.writeReg := programMemory.io.instructionRead(27,24)
  registerFile.io.writeEnable := controlUnit.io.registerWrite

  // ALU Input
  alu.io.op1 := registerFile.io.data1
  alu.io.sel := controlUnit.io.aluSel
  alu.io.op2:= Mux(controlUnit.io.immediate, Cat(Fill(16,"b0".U),programMemory.io.instructionRead(19,4)), registerFile.io.data2)

  programCounter.io.jump := WireDefault(false.B)
  // Program counter jump mux
  switch (controlUnit.io.branchSel){
    is("b00".U) {programCounter.io.jump := false.B}
    is("b01".U) {programCounter.io.jump := !alu.io.compRes}
    is("b10".U) {programCounter.io.jump := alu.io.compRes}
  }

  // DATA MEMORY Input
  dataMemory.io.address := registerFile.io.data2(15,0)
  dataMemory.io.dataWrite := registerFile.io.data1
  dataMemory.io.writeEnable := controlUnit.io.memoryWrite


  // Register write data mux
  registerFile.io.writeData := Mux(controlUnit.io.loadFromMem, dataMemory.io.dataRead, alu.io.res)

  io.done := controlUnit.io.end //?


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