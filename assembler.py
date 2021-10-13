import re

def main():
    regex = re.compile(r'([A-Z]+):')

    arr = []
    labels = dict()

    isa = {"LI": "00000"}
    registers = dict()
    for i in range(8):
        registers["R" + str(i)] = "{0:03b}".format(i)
        print("R" + str(i) + ": " + registers["R" + str(i)])

    with open("src/erosion.asm","r") as f:
        for line in f:
            line.strip()
            s = ""
            for c in line:
                if c == "#":
                    break
                if c == "\t" or c == ",":
                    continue
                s += (c)
            if not s or s[-1] != '\n':
                    s += "\n"
            if s != "\n":
                arr.append(s)  

    for i,line in enumerate(arr):
        group = re.search(regex, line)
        if(group != None):
            labels[group.group(1)] = i
            arr.pop(i)

    with open("erosion.b","w") as f:
        for line in arr:
            for l in labels:
                line = line.replace(l, str(labels[l]))
            for r in registers:
                line = line.replace(r, str(registers[r]))
            f.write(line)

if __name__ == "__main__":
    main()