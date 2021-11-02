import chisel3._
import chisel3.util._
import chisel3.iotesters
import chisel3.iotesters.PeekPokeTester
import java.util

object Programs{
  val program1 = Array(
    "h00000000".U(32.W),
    "h00000000".U(32.W),
    "h00000000".U(32.W),
    "h00000000".U(32.W),
    "h00000000".U(32.W),
    "h00000000".U(32.W),
    "h00000000".U(32.W),
    "h00000000".U(32.W),
    "h00000000".U(32.W),
    "h00000000".U(32.W),
    "h00000000".U(32.W),
    "h00000000".U(32.W)
  )

  val program2 = Array(
    "h00000000".U(32.W),
    "h00000000".U(32.W),
    "h00000000".U(32.W),
    "h00000000".U(32.W),
    "h00000000".U(32.W),
    "h00000000".U(32.W),
    "h00000000".U(32.W),
    "h00000000".U(32.W),
    "h00000000".U(32.W),
    "h00000000".U(32.W),
    "h00000000".U(32.W),
    "h00000000".U(32.W)
  )

  val program3 = Array(
    "b01010001000010011000000000000000".U(32.W),
    "b01011000000010100000000000000000".U(32.W),
    "b01010011000000000000000000000000".U(32.W),
    "b01000100001110100000000000000000".U(32.W),
    "b01100101001001000000000000000000".U(32.W),
    "b00110110000010000000000000000000".U(32.W),
    "b00110110000010000100000000000000".U(32.W),
    "b00110110000011000000000000000000".U(32.W),
    "b00110110000011000100000000000000".U(32.W),
    "b00000100010100000000000000000000".U(32.W),
    "b00110110010100000000000000000000".U(32.W),
    "b01110110010100001000000000000000".U(32.W),
    "b00000111011000000000000000000000".U(32.W),
    "b00110110000111000000000000000000".U(32.W),
    "b01010110010100001000000000000000".U(32.W),
    "b00000111011000000000000000000000".U(32.W),
    "b00110110000111000000000000000000".U(32.W),
    "b01110110010110100000000000000000".U(32.W),
    "b00000111011000000000000000000000".U(32.W),
    "b00110110000111000000000000000000".U(32.W),
    "b01010110010110100000000000000000".U(32.W),
    "b00000111011000000000000000000000".U(32.W),
    "b00110110000111000000000000000000".U(32.W),
    "b00110110010000000000000000000000".U(32.W),
    "b01010100000000000000000000000000".U(32.W),
    "b01010110010111001000000000000000".U(32.W),
    "b00010000010001100000000000000000".U(32.W),
    "b01010011000000001000000000000000".U(32.W),
    "b00100000110011100000000000000000".U(32.W),
    "b01010010000000001000000000000000".U(32.W),
    "b00100000100010100000000000000000".U(32.W),
    "b10000000000000000000000000000000".U(32.W)
  )
}