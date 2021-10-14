import re

def main():
    label_regex = re.compile(r'([A-Z]+):')
    number_regex = re.compile(r'(?:[^A-Z])(\d+)')

    arr = []
    labels = dict()

    isa = {
        "MULI": "0111",
        "LI": "0000",
        "LD": "0001",
        "SD": "0010",
        "JNE": "0011",
        "JZ": "0100",
        "JEQ": "0101",
        "JR": "0110",
        "ADDI": "1001",
        "ADD": "1000",
        "SUBI": "1010",
        "HALT": "1011"
        }

    registers = dict()
    for i in range(8):
        registers["R" + str(i)] = "{0:03b}".format(i)

    with open("src/erosion.asm","r") as f:
        for i,line in enumerate(f):
            line.strip().upper()
            s = ""
            line = line.replace("\t","").replace(",","")
            for c in line:
                if c == "#":
                    break
                s += (c)
            if not s or s[-1] != '\n':
                s += "\n"
            res = re.search(label_regex, line)
            if (res != None):
                labels[res.group(1)] = "{0:06b}".format(i)
                continue
            if s != "\n":
                arr.append(s)

    with open("erosion.bin","w") as f:
        for line in arr:
            res = re.search(number_regex, line)
            if(res != None):
                for i in range(res.lastindex):
                    num = res.group(i)
                    line = line.replace(num, " {0:06b}".format(int(num)))
            for l in labels:
                line = line.replace(l, str(labels[l]))
            for i in isa:
                line = line.replace(i, isa[i])
            for r in registers:
                line = line.replace(r, str(registers[r]))
            line = line.replace(" ","")
            line = line.replace("\n", "0" * (32-len(line)) + "\n")
            f.write(line)

if __name__ == "__main__":
    main()