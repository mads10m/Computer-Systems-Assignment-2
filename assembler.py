import re
import sys
import os

prepend=["import chisel3._\n",
"import chisel3.util._\n",
"import chisel3.iotesters\n",
"import chisel3.iotesters.PeekPokeTester\n",
"import java.util\n",
"\n",
"object Programs {\n",
"  val program1 = Array(\n"]

postfix = [")\n","}\n"]


def main():
    if len(sys.argv)!=2:
        print(f'Given arguments: {str(sys.argv)}')
        print("Using erosion.asm as assembler code file")
        asm_file = "erosion.asm"
    else:
        asm_file = sys.argv[1]

    label_regex = re.compile(r'([A-Z]+):')
    number_regex = re.compile(r' (\d+)')
    li_regex = re.compile(r'LI (R\d+) (\d+)')
    jz_regex = re.compile(r'JZ (\w+ R\d+)')
    jr_regex = re.compile(r'JR (\w+)')
    ld_regex = re.compile(r'LD (R\d+) (R\d+)')
    r_regex = re.compile(r'(R\d+)')

    arr = []
    labels = dict()

    isa = {
        "LD": "0000",
        "SD": "00010000",
        "JNE": "0010",
        # "JZ": "0011",
        "JEQ": "0011",
        # "JR": "0100",
        "MULI": "0100",
        "ADDI": "0110",
        "ADD": "0101",
        "SUBI": "0111",
        "END": "1000"
        }

    registers = dict()
    for i in range(16):
        registers["R" + str(i)] = "{0:04b}".format(i)

    # Remove whitespaces, empty lines, comments
    with open(asm_file,"r") as f:
        for i,line in enumerate(f):
            line = line.strip().upper().replace("\t","").replace(",","")
            s = ""
            for c in line:
                if c == "#":
                    break
                if c != "\n":
                    s += c
            if s:
                if s[-1] != ":":
                    s += "\n"
                if s != "\n":
                    arr.append(s)

    # Create labels and remove them from the code
    for i, line in enumerate(arr):
        res = re.search(label_regex, line)
        if res != None:
            labels[res.group(1)] = "{0:06b}".format(i)
            arr.pop(i)

    # with open("out.bin","w") as f:
    with open(os.path.join(os.getcwd(),"src","test","scala","Programs.scala"),"w") as f:
        for line in prepend:
            f.write(line)
        for line in arr:
            res = re.search(jr_regex, line)
            if res != None:
                line = f'JEQ {res.group(1)} R0 R0\n'
            res = re.search(jz_regex, line)
            if res != None:
                line = f'JEQ {res.group(1)} R0\n'
            res = re.search(li_regex, line)
            if res != None:
                line = f'ADDI {res.group(1)} R0 {res.group(2)}\n'
            res = re.search(ld_regex,line)
            if res != None:
                line = f'LD {res.group(1)} R0 {res.group(2)}\n'
            res = re.search(number_regex, line)
            if(res != None):
                for i in range(res.lastindex):
                    num = res.group(i)
                    line = line.replace(num, " {0:016b}".format(int(num)))
            for l in labels:
                line = line.replace(l, str(labels[l]))
                # print(f'{l}, {str(labels[l])}')
            for i in isa:
                line = line.replace(i, isa[i])
            res = re.findall(r_regex, line)
            if(res != None):
                for r in res:
                    line = line.replace(r, registers[r])
            line = line.replace(" ","").replace(":","").replace("\n","")
            line = line + "0" * (32-len(line))
            line = f'"b{line}".U(32.W),'
            line = line + "\n"
            f.write(line)
        for line in postfix:
            f.write(line)

if __name__ == "__main__":
    main()