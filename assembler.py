import re

def main():
    label_regex = re.compile(r'([A-Z]+):')
    number_regex = re.compile(r'(?:[^A-Z])(\d+)')
    li_regex = re.compile(r'LI (R\d) (\d+)')
    jz_regex = re.compile(r'(?:JZ) (\w+ R\d)')

    arr = []
    labels = dict()

    isa = {
        "LD": "0000",
        "SD": "00010000",
        "JNE": "0010",
        # "JZ": "0011",
        "JEQ": "0011",
        "JR": "0100",
        "MULI": "0101",
        "ADDI": "0110",
        "ADD": "0111",
        "SUBI": "1000",
        "END": "1001"
        }

    registers = dict()
    for i in range(16):
        registers["R" + str(i)] = "{0:04b}".format(i)

    # Remove whitespaces, empty lines, comments
    with open("src/erosion.asm","r") as f:
        for i,line in enumerate(f):
            line.strip().upper()
            s = ""
            line = line.replace("\t","").replace(",","")
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

    with open("erosion.bin","w") as f:
        for line in arr:
            res = re.search(jz_regex, line)
            if res != None:
                line = f'JEQ {res.group(1)} R0\n'
            res = re.search(li_regex, line)
            if res != None:
                line = f'ADDI {res.group(1)} R0 {res.group(2)}\n'
            res = re.search(number_regex, line)
            if(res != None):
                for i in range(res.lastindex):
                    num = res.group(i)
                    line = line.replace(num, " {0:05b}".format(int(num))).replace(":","")
            for l in labels:
                line = line.replace(l, str(labels[l]))
                # print(f'{l}, {str(labels[l])}')
            for i in isa:
                line = line.replace(i, isa[i])
            for r in registers:
                line = line.replace(r, str(registers[r]))
            line = line.replace(" ","")
            line = line.replace("\n", "0" * (32-len(line)) + "\n")
            f.write(line)

if __name__ == "__main__":
    main()