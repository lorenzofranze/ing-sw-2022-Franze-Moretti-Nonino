import random

x = input("modalità di gioco:" )
          
if x=='1' or x=='2':
    for i in range(0, int(x)+1):
        name = "simple"+str(i+1)+".txt"
        afile = open(name, "w" )
        afile.write(str(x)+'\n')
        afile.write('player'+str(i+1)+'\n')
        for j in range (0, 99999):
            line = str(random.randint(-1, 10))
            afile.write(line+'\n')
        afile.close()
          
if x=='3' or x=='4':
    for i in range(0, int(x)-1):
        name = "complex"+str(i+1)+".txt"
        afile = open(name, "w" )
        afile.write(str(x)+'\n')
        afile.write('player'+str(i+1)+'\n')
        for j in range (0, 999999):
            line = str(random.randint(-1, 12))
            if line == '11':
                line='y'
            elif line == '12':
                line='n'
            afile.write(line+'\n')
        afile.close()

            
print('fatto')
