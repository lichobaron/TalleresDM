###################
#Partir : Parte en parte iguales una matriz.
###Parámetros:
#   - M : Matriz a partir.
#   - p : Cantidad de partes en que se quiere partir.
##################
def partir(M, p):
    tam = len(M) * len(M[0])
    rangos = []
    if tam >= p:
        rangos = []
        par = round(tam / p)
        print("->", par)
        aux = 1
        for i in range(0, len(M[0])):
            for j in range(0, len(M)):
                if aux == 1:
                    x = []
                    x.append(i)
                    x.append(j)
                    rangos.append(x)
                if aux == par:
                    x = []
                    x.append(i)
                    x.append(j)
                    rangos.append(x)
                    aux = 0
                aux += 1
    print(rangos)


M = [[1, 2], [3, 4], [5, 6], [7, 8]]
partir(M, 7)
