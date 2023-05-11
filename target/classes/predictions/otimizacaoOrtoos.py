# -*- coding: utf-8 -*-
from ortools.linear_solver import pywraplp
import numpy as np
import sys
import csv
import requests
import json
from operator import is_not
from functools import partial
from more_itertools import locate

quantidadeProvedores2=2

takeClosest = lambda num,collection:min(collection,key=lambda x:abs(x-num))


responseAws = requests.get(
    "https://aws.amazon.com/ec2/pricing/json/linux-od.json")


responseAzure = open('azureprice-export.json',)

aws = True
azure = False

# returns JSON object as
# a dictionary
data = json.load(responseAzure)
 
# Iterating through the json
# list


my_json_string = "[ " + responseAws.text + " ]"

my_json_string = my_json_string

to_python = json.loads(my_json_string)
to_python = to_python[0]['config']['regions']
maquinas = []
i = 0
y = 0


instancias=0
#Contador da AWS
if(aws == True):
  for regions in to_python:
      #region[i][0]= regions["region"]
      for instanceTypes in regions["instanceTypes"]:
          for sizes in instanceTypes["sizes"]:
            instancias=instancias+1


#Contador da Azure
            
if(azure == True):
  for linhas in data:
    instancias=instancias+1

provedor = np.zeros(instancias)
region = np.random.uniform(1, 999, (instancias, instancias))
instancias2=0

quantidadeIntancias = instancias

mv = np.empty(quantidadeIntancias, dtype=object)   

#Valores da AWS
if(aws == True):
  for regions in to_python:
      #region[i][0]= regions["region"]
      for instanceTypes in regions["instanceTypes"]:
          for sizes in instanceTypes["sizes"]:
                  region[2][i] = sizes["vCPU"]
                  region[3][i] = sizes["memoryGiB"]
                  region[4][i] = sizes["valueColumns"][0]["prices"]["USD"]
                  region[5][i] = 1
                  provedor[instancias2] = 1
                  mv[i] = sizes["size"]
                  i = i + 1
                  instancias2 = instancias2+1

#Valores da azure
if(azure == True):
  for linhas in data:
        region[2][i] = float(linhas["numberOfCores"])
        region[3][i] = float(linhas["memoryInMB"]/1000)
        region[4][i] = float(linhas["linuxPrice"])
        region[5][i] = 2
        mv[i] = linhas["name"]
        
        i = i + 1
        provedor[instancias2] = 2
        instancias2 = instancias2+1
        #print(linhas["numberOfCores"]," ",linhas["memoryInMB"]/1000," ",linhas["linuxPrice"])           
 
solver = pywraplp.Solver('LinearProgrammingExample',
                         pywraplp.Solver.GLOP_LINEAR_PROGRAMMING)


def mi2ai(n, m, line_length):
    return n * M + m


def ai2mi(index, line_length):
    return [(index // line_length), index % line_length]



# Parametros

#uC = 0.01
#uT = 1- uC

p = 1
#c_n = [1,2,4,8,16,32,64,128,256,512]

ClsC = 164  #Tem de ser um numero na base 2
ClsM = 1024


flag =1  #prioridade 0 - Custo*Desperdicio e 1 - Tempo
#Cls = ClsMemory

arguments = sys.argv[1:]
count = len(arguments)
alphaT = 1
alphaC = 1
alphaD = 1
print(count)
if(count > 0):
  print(arguments[0])
  print(arguments[1])
  arquivo = arguments[0]
  alphaT = float(arguments[1])
  alphaC = float(arguments[2])
  alphaD = float(arguments[3])

else:
  arquivo = 'teste2.csv'



num_lines=0
with open(arquivo, newline='') as csvfile:
    spamreader = csv.reader(csvfile, delimiter=',')
    for row in spamreader:
      try:
        float(row[2])
        num_lines=num_lines+1
      except ValueError:
            print()
#num_lines = sum(1 for line in open(arquivo))
#print(num_lines)
c_n = np.random.rand(num_lines)*2
m_n = np.random.rand(num_lines)*2
N = len(c_n)


QuantiUsuario = 1  #Tem de ser MENOR que CLS


M = QuantiUsuario

T_n_m = np.zeros((M, N))

D_n_m = np.zeros((M, N))


n = 0
mediaT = float(0.00)
mediaD = float(0.00)
print()
with open(arquivo, newline='') as csvfile:
    spamreader = csv.reader(csvfile, delimiter=',')
    for row in spamreader:
        try:
          
            memoryTeste = int(float(row[5])/1000000)
            memoryTeste = float(100-(((float(row[4])/1000)*100)/memoryTeste))
            #print(memoryTeste)
            if(memoryTeste > 70):
              float(row[2])
              #CPU no USED, Agents, vCPU, CPU USED, Time
              c_n[n] = row[2] #cpu
              m_n[n] = int(float(row[5])/1000000) #memory
              
              T_n_m[0][n] = float(row[10])  #tempo
              T_n_m[0][N-n-1] = float(row[10])  #tempo

              Dmemory = 100-(((float(row[4])/1000)*100)/m_n[n])
              
              D_n_m[0][n] = (float(row[0]) + Dmemory)# 0 Uso de CPU e 5 Memoria
              D_n_m[0][N-n-1] = (float(row[0]) + Dmemory)# 0 Uso de CPU e 5 Memoria
              mediaT = float(row[10]) + mediaT #
              mediaD = float(row[0]) + mediaD + (Dmemory) #
              #print("Teste55 ",T_n_m[0][n])
              #print("Teste56 ",D_n_m[0][n])
              n = n + 1
            else:
              T_n_m[0][n] = sys.maxint
              T_n_m[0][N-n-1] = sys.maxint
              D_n_m[0][n] = sys.maxint
              D_n_m[0][N-n-1] =  sys.maxint
              c_n[n] = sys.maxint

        except:
            n = n
p = 0.05




c_n2 = list(dict.fromkeys(c_n))
m_n2 = np.zeros((instancias2, len(c_n2)*quantidadeIntancias))
provedorEsc = np.empty(len(c_n2)*quantidadeIntancias, dtype=object)  
teste4 = np.zeros(len(c_n2)*quantidadeIntancias)



ii=0
for m in range(0, quantidadeIntancias-2):
  for n in range(0, len(c_n2)):
    if(int(float(region[2][m])) == int(float(c_n2[n])) and int(region[5][m])==1 ):
      m_n2[m][ii] = float(region[3][m])
      #print(" -1 ",m_n2[m][ii])
      teste4[ii] = c_n2[n]
      ii=ii+1
        
for m in range(0, quantidadeIntancias-2):
  for n in range(0, len(c_n2)):
    if(int(float(region[2][m])) == int(float(c_n2[n])) and int(region[5][m])==2 ):
      m_n2[m][ii] = float(region[3][m])
      teste4[ii] = c_n2[n]
      ii=ii+1
#mvI = list(dict.fromkeys(mvI))
print(ii)
print(teste4)
      
print(m_n2)
i=0

mvI = np.empty((quantidadeProvedores2,ii), dtype=object)
b_m = np.zeros(num_lines)

provedorPreco = np.zeros(quantidadeIntancias)
print(len(c_n2))
tert=0
n=0
ii=0
print("Teste1 ",num_lines," ",len(region[5])," ",len(c_n2))

#Calculos para AWS

if(aws == True):
  for n in range(0,len(c_n)):
      controleCPU=0
      for m in range(0, len(region[2])):
       if(int(region[5][m])==1 and ii < N):
          
          cpus=list(locate(region[2], lambda x: x == c_n[n]))
          
          #print(c_n[n]," ",ii," ",controleCPU," ",cpus)
          if(controleCPU in cpus):
              men=0
              memorias = []
              for o in range(0,len(region[3])):
                if(region[2][o]==c_n[n]):
                  memorias.append(region[3][o])
              #print(memorias)
              
              if(memorias != []):
                men=min(enumerate(memorias), key=lambda x: abs(x[1]-(float(m_n[n]))))
                #print(men)
                #print(region[2][m]," ", c_n[n]," ", int(region[3][m])," ",men," ",int(m_n[n]))
                
                if(int(float(region[2][m])) == int(float(c_n[n]))  and (int(region[3][m])+1==int(men[1]) or int(region[3][m])-1==int(men[1]) or int(region[3][m])==int(men[1]))):
                
                  #print("Teste3 ",c_n[n]," ",c_n2[n]," ",men[1]," ",region[3][m])
                  try:
                    print(region[4][m]," ",T_n_m[0][n])
                    b_m[ii] = (region[4][m]*T_n_m[0][n])
                    
                    provedorPreco[n] = provedor[m]
                    mvI[0][ii]=mv[m]
                    print("-- ",mv[m]," ",region[2][m])
                    ii=ii+1
                    break
                  except ValueError:
                      print(ValueError)
                      t=0
        
          controleCPU=controleCPU+1 
print("----------")   
ii=0   




#Calculos para Azure
if(azure==True):
  for n in range(0,len(c_n)):
      controleCPU=0
      for m in range(0, len(region[2])):
       if(int(region[5][m])==2 and ii < N):
          
          cpus=list(locate(region[2], lambda x: x == c_n[n]))
          
          #print(c_n[n]," ",ii," ",controleCPU," ",cpus)
          if(controleCPU in cpus):
              men=0
              memorias = []
              for o in range(0,len(region[3])):
                if(region[2][o]==c_n[n]):
                  memorias.append(region[3][o])
              #print(memorias)
              
              if(memorias != []):
                men=min(enumerate(memorias), key=lambda x: abs(x[1]-(float(m_n[n]))))
                #print(men)
                #print(region[2][m]," ", c_n[n]," ", int(region[3][m])," ",men," ",int(m_n[n]))
                
                if(int(float(region[2][m])) == int(float(c_n[n]))  and (int(region[3][m])+1==int(men[1]) or int(region[3][m])-1==int(men[1]) or int(region[3][m])==int(men[1]))):
                
                  #print("Teste3 ",c_n[n]," ",c_n2[n]," ",men[1]," ",region[3][m])
                  try:
                   #print(region[4][m]," ",T_n_m[0][n])
                    b_m[ii] = (region[4][m]*T_n_m[0][n])
                    
                    provedorPreco[n] = provedor[m]
                    mvI[1][ii]=mv[m]
                    #print("-- ",mv[m]," ",region[2][m])
                    ii=ii+1
                    break
                  except ValueError:
                      print(ValueError)
                      t=0
        
          controleCPU=controleCPU+1 
         
    

print(mvI[0])
print("teste ",len(provedorPreco))

A_n_m = np.random.uniform(1, 1, (M, N))

C_n_m = np.zeros((M, N))


mediaC = float(0.00)
for m in range(0, M):
    for n in range(0, N):
        #print("t ",b_m[n])
        C_n_m[m, n] = b_m[n]
        mediaC = float(C_n_m[m, n]) + mediaC

print(b_m)
T = float(mediaT/n)
print("Tempo médio "+str(T))

C = float(mediaC/n)
print("Custo médio "+str(C))

D = float(mediaD/n)*1.1
print("Desperdicio médio "+str(D))

print()

'''for m in range(0, M):
    for n in range(0, N):
        #A_n_m[0, m] = D_n_m[n][m]+b_m[n]+T_n_m[0][n]
        print(c_n[n])'''

print("Matriz Desperdicio")
strmatrix = ''
for m in range(0, M):
    strmatrix += '[ '
    for n in range(0, N):
        strmatrix += str("{:.2f}".format(D_n_m[m, n])) + " "
    strmatrix += ']\n'
print(strmatrix)

print("Matriz Tempo")
strmatrix = ''
for m in range(0, M):
    strmatrix += '[ '
    for n in range(0, N):
        strmatrix += str("{:.2f}".format(T_n_m[m, n])) + " "
    strmatrix += ']\n'
print(strmatrix)

print("Matriz Custo")
strmatrix = ''
for m in range(0, M):
    strmatrix += '[ '
    for n in range(0, N):
        strmatrix += str("{:.2f}".format(C_n_m[m, n])) + " "
    strmatrix += ']\n'
print(strmatrix)

# # Variaveis de Decisï¿½es

#

cv = lambda x: np.std(x, ddof=1) / np.mean(x) * 100 
TCoV = cv(T_n_m[0])
DCoV = cv(D_n_m[0])
CCoV = cv(C_n_m[0])

print(str(T)+" "+str(TCoV*0.5)+" "+str(T*0.3))
TCoV = T-min(TCoV*0.5, T*0.3)
print(TCoV)
print(str(D)+" "+str(DCoV*2)+" "+str(D*0.3))
DCoV = D-min(DCoV*1, D*0.1)
print("D")
print((DCoV))
print((D))
#DCoV=D
while True:

    X_n_m = []
    for n in range(0, N):
        for m in range(0, M):
            X_n_m.append(
                solver.NumVar(0, 1,
                              "X[" + str(n) + "," + str(m) + "]"))


    Tp_n_m = []
    for n in range(0, N):
        for m in range(0, M):
            Tp_n_m.append(
                solver.NumVar(0, solver.infinity(),
                              "Tp[" + str(n) + "," + str(m) + "]"))   

    Cp_n_m = []
    for n in range(0, N):
        for m in range(0, M):
            Cp_n_m.append(
                solver.NumVar(0, solver.infinity(),
                              "Cp[" + str(n) + "," + str(m) + "]"))

    Dp_n_m = []
    for n in range(0, N):
        for m in range(0, M):
            Dp_n_m.append(
                solver.NumVar(0, solver.infinity(),
                              "dp[" + str(n) + "," + str(m) + "]"))


                                                
    solver.NumVariables()
    # %%
    head = 0


    '''ct = solver.Constraint(1, ClsC, str(head))
    for n in range(0, M):
        head += 1
        for m in range(0, N):
          ct.SetCoefficient(X_n_m[mi2ai(n, m, N)], c_n[m])

    ct = solver.Constraint(1, ClsM, str(head))
    for n in range(0, M):
        head += 1
        for m in range(0, N):
          ct.SetCoefficient(X_n_m[mi2ai(n, m, N)], m_n[m])'''

    for n in range(0, M):
        const1 = solver.Constraint(1, 1 , str(head))
        head += 1
        for m in range(0, N):
            const1.SetCoefficient(X_n_m[mi2ai(n, m, N)], 1)

    for n in range(0, M):
        const2 = solver.Constraint(-solver.infinity(), TCoV, str(head))
        for m in range(0, N):
          head += 1
          const2.SetCoefficient(X_n_m[mi2ai(n, m, N)],  T_n_m[n, m]*(alphaT))


    for n in range(0, M):
        const3 = solver.Constraint(0,C, str(head))
        for m in range(0, N):
            head += 1
            const3.SetCoefficient(X_n_m[mi2ai(n, m, N)],  C_n_m[n, m]*(alphaC))

    for n in range(0, M):
        const4 = solver.Constraint(0,DCoV, str(head))
        for m in range(0, N):
            head += 1
            const4.SetCoefficient(X_n_m[mi2ai(n, m, N)], D_n_m[n, m]*(alphaD))



    # # Solver

    objective = solver.Objective()



    for n in range(0, M):
        for m in range(0, N):
            objective.SetCoefficient(X_n_m[mi2ai(n, m, N)], D_n_m[n, m])
            objective.SetCoefficient(X_n_m[mi2ai(n, m, N)], T_n_m[n, m])
            objective.SetCoefficient(X_n_m[mi2ai(n, m, N)], C_n_m[n, m])

    objective.SetMinimization()

    status = solver.Solve()


    #Imprimiremos a solucao
    if status == pywraplp.Solver.OPTIMAL:
        #print('Solution:')
        break
        #print('Objective value =', solver.Objective().Value())
    else:
        print('Objective value =', solver.Objective().Value())
        #T = (T*1.05)
        #C = (C*1.05)
        D = (D*1.1)
        print(str(T)+" "+str(C)+" "+str(D))
        print('The problem does not have an optimal solution.')

############ fim while

strmatrix = ''
strmatrix += '[ '
for n in range(0, N):
    strmatrix += str(int(c_n[n])) + " "
strmatrix += ']\n\n'
#print(strmatrix)

strmatrix = ''
strmatrix += '[ '
for n in range(0, N):
    strmatrix += str(int(m_n[n])) + " "
strmatrix += ']\n\n'
#print(strmatrix)

strmatrix = ''
for n in range(0, M):
    strmatrix += '[ '
    for m in range(0, N):
        strmatrix += str(X_n_m[mi2ai(n, m, N)].solution_value()) + " "

    strmatrix += ']\n'
print(strmatrix)

#print(strmatrix)
#print(M)
#print(N)

cont = 999999999999999
sM = 0
sN = 0
for n in range(0, M):
    for m in range(0, N):
        if (float(X_n_m[mi2ai(n, m, N)].solution_value()) > 0):

            if( float(X_n_m[mi2ai(n, m, N)].solution_value()) > 0 and ((float(X_n_m[mi2ai(n, m, N)].solution_value())*D_n_m[n, m]) +  (float(X_n_m[mi2ai(n, m, N)].solution_value())*T_n_m[n, m]) +  (float(X_n_m[mi2ai(n, m, N)].solution_value())*C_n_m[n, m])) >= cont):
              cont = ((float(X_n_m[mi2ai(n, m, N)].solution_value())*D_n_m[n, m]) + (float(X_n_m[mi2ai(n, m, N)].solution_value())*T_n_m[n, m]) + (float(X_n_m[mi2ai(n, m, N)].solution_value())*C_n_m[n, m]))
              sM = m
              sN = n
            
print(X_n_m[mi2ai(sN, sM, N)].solution_value())
print("Será alocado ", c_n[sM])
print()
if(X_n_m[mi2ai(sN, sM, N)].solution_value()<1):
    print("1")
    print("Tempo ", T_n_m[sN, sM] + T_n_m[sN, sM] * X_n_m[mi2ai(sN, sM, N)].solution_value())
    print("Custo ", C_n_m[sN, sM] + C_n_m[sN, sM] * X_n_m[mi2ai(sN, sM, N)].solution_value())
    print("Desperdicio ", D_n_m[sN, sM] + D_n_m[sN, sM] * X_n_m[mi2ai(sN, sM, N)].solution_value())
    print()
    #print("Posição Matriz resultado ", m)
    #print("Recursos ", Cls)
    #print("Prioridade %s" % (sys.argv[1]))

else:
    print("2")
    print("Tempo ", T_n_m[sN, sM])
    print("Custo ", C_n_m[sN, sM])
    print("Desperdicio ", D_n_m[sN, sM])
    print()
    #print("Posição Matriz resultado ", m)
    #print("Recursos ", Cls)
    #print("Prioridade %s" % (sys.argv[1]))

x = int(provedorPreco[sM])
print("1 - AWS, 2 - Azure ")
print("Provedor ",int(provedorPreco[sM]))
print("vCPU ", str(c_n[sM]))
print("Memoria ", str(m_n[sM]))
'''for n in range(0, 2):
    for m in range(0, len(mvI[n])):
      print(mvI[n][m])'''
      
print("Instancia ", str(sN))
print("Instancia ", str(mvI[x-1][sM]))
