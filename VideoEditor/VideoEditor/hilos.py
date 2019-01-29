import threading

######
#worker = Función que realiza el hilo
def worker ():
    for i in range(0,100):
        print(i)

#lista de hilos
threads = []

#Creación de hilos y asignación de función a los hilos
for i in range(0,3):
    t = threading.Thread(target=worker)
    threads.append(t)
    t.start()

#Espera que todos los hilos terminen de ejecutarse
for i in range(0,len(threads)):
    threads[i].join()

for i in range(0,10):
    print("b")    