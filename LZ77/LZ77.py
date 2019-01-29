def C(start, win, dict, buffer):
    triplets = []
    i = start
    while i < len(buffer):
        j = i - 1
        start_j = 0
        maxj = 0
        if j > 0:
            while j >= start:
                tamj = 0
                if buffer[i] == buffer[j]:
                    i_aux = i
                    j_aux = j
                    while i_aux < len(buffer)-1 and buffer[i_aux] == buffer[j_aux]:
                        i_aux+=1
                        j_aux+=1
                        tamj+=1
                if tamj > maxj:
                    maxj = tamj
                    start_j = j
                j-=1
            if maxj == 0:
                T = [0,0,buffer[i]]
                triplets.append(T)
                if i > dict and  win < len(buffer):
                    win+=1
     
                i+=1
                start=+1
            else:
                T = [i-start_j,maxj,buffer[i+maxj]]
                triplets.append(T)
                if i > dict and  win < len(buffer):
                    win = win+maxj-1    
                i = i+maxj+1
                start = start+maxj-1
        else:
            T = [0,0,buffer[i]]
            triplets.append(T)
            i+=1
    return triplets
    
def D(triplets):
    buffer = ""
    cursor = 0
    for i in range(0,len(triplets)):
        T = triplets[i]
        if T[0] == 0 and T[1] == 0:
            buffer = buffer + T[2]
            cursor+=1
        else:
            if T[1] > T[0]:
                dif = T[1] - T[0]
                s = cursor - T[0]
                e = s + T[0]
                buffer = buffer + buffer[s:e]
                buffer = buffer + buffer[s:(e-dif+1)]
                buffer = buffer + T[2]
            else:
                s = cursor - T[0]
                e = s + T[1]
                buffer = buffer + buffer[s:e]
                buffer = buffer + T[2]
            cursor = cursor + T[1] +1
    return buffer
            
def printCodeCad(triplets):
    file = open("salida.txt","w") 
    for i in range(0,len(triplets)):
        T = triplets[i]
        print(T[0], end='')
        file.write(str(T[0])) 
        print(',', end='')
        file.write(',')
        print(T[1], end='')
        file.write(str(T[1]))
        print(',', end='')
        file.write(',')
        print(T[2], end='')
        file.write(str(T[2]))
        print(";", end=' ')
        file.write(';') 
    print("")

def readOpc1(filename):
    file  = open(filename, "r")
    w = int(file.readline())
    d = int(file.readline())
    cad = file.readline()
    triplets = C(0,w,d,cad)
    printCodeCad(triplets)
    print("Su salida tambien ha sido guadada en salida.txt")

def readOpc2(filename):
    file  = open(filename, "r")
    triplets = []
    cad =file.readline()
    i = 0
    while i < len(cad)-1:
        T = []
        n = ""
        while cad[i] != ",":
            n = n + cad[i]
            i+=1
        T.append(int(n))
        n = ""
        i+=1
        while cad[i] != ",":
            n = n + cad[i]
            i+=1
        T.append(int(n))
        i+=1
        T.append(cad[i])
        triplets.append(T)
        i+=2
    cad = D(triplets)
    print(cad)


opc = 0
while opc!=3:
    print("---------LZ77---------")
    print("1. Codificar")
    print("2. Decodificar")
    print("3. Salir")
    opc = int(input())
    if opc == 1:
        file = input("- Digite el nombre del archivo con la entrada: (Debe tener en la linea 1 el tamaño ventana, linea 2 el tamaño diccionario, linea 3 la cadena) \n")
        print("----RESULTADO----")
        readOpc1(file)
    elif opc == 2:
        file = input("- Digite el nombre del archivo con la entrada: (Debe tener una linea con una cadena codificada) \n")
        print("----RESULTADO----")
        readOpc2(file)
