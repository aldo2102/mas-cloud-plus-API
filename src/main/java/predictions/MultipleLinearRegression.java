/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package predictions;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;

import com.opencsv.CSVReader;

import Jama.Matrix;
import Jama.QRDecomposition;
import bean.ModelsProvisioning;
import agents.VMManager;
import prices.PriceAWS;
import prices.PriceAWS;
import prices.PriceAzure;
import prices.PriceGoogleCloud;
import prices.PriceVangrant;
import provisioning.Grasp;

public class MultipleLinearRegression {

	int N; // number of
	int p; // number of dependent variables
	Matrix beta; // regression coefficients
	private double SSE; // sum of squared
	private double SST; // sum of squared

	public MultipleLinearRegression(String base) {
		action(base);
	}
	
	public MultipleLinearRegression() {}

	public MultipleLinearRegression(double[][] x, double[] y) {
		if (x.length != y.length)
			throw new RuntimeException("dimensions don't agree");
		N = y.length;
		p = x[0].length;

		Matrix X = new Matrix(x);

		// create matrix from vector
		Matrix Y = new Matrix(y, N);

		// find least squares solution
		QRDecomposition qr = new QRDecomposition(X);
		// System.out.println(Y.toString().toString()+" "+N+" "+y.length);

		beta = qr.solve(Y);

		// mean of y[] values
		double sum = 0.0;
		for (int i = 0; i < N; i++)
			sum += y[i];
		double mean = sum / N;

		// total variation to be accounted for
		for (int i = 0; i < N; i++) {
			double dev = y[i] - mean;
			SST += dev * dev;
		}

		// variation not accounted for
		Matrix residuals = X.times(beta).minus(Y);
		SSE = residuals.norm2() * residuals.norm2();

	}

	public double beta(int j) {
		return beta.get(j, 0);
	}

	public double R2() {
		return 1.0 - SSE / SST;
	}
	
	

	public void action(String base) {

		double x[][] = null;
		double y[] = null;
		double x1[][] = null;
		double y1[] = null;
		double x2[][] = null;
		double y2[] = null;
		double x3[][] = null;
		double y3[] = null;
		double x4[][] = null;
		double y4[] = null;
		double x5[][] = null;
		double y5[] = null;
		
		String[] nextLine;
		String strFile;
		CSVReader reader = null;
		int lineNumber = 0;
		int cont = 1;
		try {
			// csv file containing data
			strFile = base;
			/*
			 * reader = new CSVReader(new FileReader(strFile)); reader.close();
			 */
			reader = new CSVReader(new FileReader(strFile));
			while ((nextLine = reader.readNext()) != null) {
				lineNumber++;
			}

			x = new double[lineNumber][3];
			y = new double[lineNumber];
			x1 = new double[lineNumber][3];
			y1 = new double[lineNumber];
			x2 = new double[lineNumber][3];
			y2 = new double[lineNumber];
			
			x3 = new double[lineNumber][3];
			y3 = new double[lineNumber];
			x4 = new double[lineNumber][3];
			y4 = new double[lineNumber];
			x5 = new double[lineNumber][3];
			y5 = new double[lineNumber];
			
			reader = new CSVReader(new FileReader(strFile));
			lineNumber = 0;
			while ((nextLine = reader.readNext()) != null) {

				// nextLine[] is an array of values from the line

				if (nextLine.length > 1) {
					if (Character.isDigit(nextLine[0].charAt(0))) {
						double CPUNotUsed = (double) Double.parseDouble(nextLine[0]);
						//long agents = (long)  (Double.parseDouble(nextLine[4]));  //Integer.parseInt(nextLine[3]);
						double vCPU = (double) (Double.parseDouble(nextLine[2]));
						double CPUUsed = (double) Double.parseDouble(nextLine[1]);
						double time = (double) Double.parseDouble(nextLine[10]);
						double memoryUsed = (double)  Double.parseDouble(nextLine[4]);
						double memory = (double)  Double.parseDouble(nextLine[5])/1000000;
						memoryUsed = ((memoryUsed/1000)*100)/ memory; // Porcentagem de uso

						
						if(memory>0 && vCPU>0 && time>0) {
							x[lineNumber][0] = 1;
							x[lineNumber][1] = Math.log10(Math.abs(memory));
							x[lineNumber][2] = Math.log10(Math.abs(vCPU));
							y[lineNumber] = Math.log10(time);
							
							x1[lineNumber][0] = 1;
							x1[lineNumber][1] = memory;
							x1[lineNumber][2] =  vCPU;
							y1[lineNumber] = CPUUsed;
							
							x2[lineNumber][0] = 1;
							x2[lineNumber][1] = Math.log10(Math.abs(memory));
							x2[lineNumber][2] =  Math.log10(Math.abs(vCPU));
							y2[lineNumber] = Math.log10(Math.abs(memoryUsed));
						
						//Com logs
							x3[lineNumber][0] = 1;
							x3[lineNumber][1] = Math.log10(Math.abs(memory));
							x3[lineNumber][2] = Math.log10(Math.abs(vCPU));
							y3[lineNumber] = time;
							
							x4[lineNumber][0] = 1;
							x4[lineNumber][1] = Math.log10(Math.abs(memory));
							x4[lineNumber][2] = Math.log10(Math.abs(vCPU));
							y4[lineNumber] = Math.log10(Math.abs(CPUUsed));
							
							x5[lineNumber][0] = 1;
							x5[lineNumber][1] = Math.log10(Math.abs(memory));
							x5[lineNumber][2] = Math.log10(Math.abs(vCPU));
							y5[lineNumber] = Math.log10(Math.abs(memoryUsed));
						}

					}
					// System.out.println(nextLine[0] +" "+nextLine[1]+" "+nextLine[2]+"
					// "+nextLine[3]+" "+nextLine[4]);
				}
				lineNumber++;
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		System.out.println("Regressao ");
		MultipleLinearRegression regression = new MultipleLinearRegression(x, y);

		System.out.printf("Time = %.2f + %.2f beta1 + %.2f beta2  (R^2 = %.2f)\n", regression.beta(0),
				regression.beta(1), regression.beta(2), /*regression.beta(3),*/ regression.R2());

		System.out.println("Regressao 1");
		MultipleLinearRegression regression1 = new MultipleLinearRegression(x1, y1);

		System.out.printf("CPU Used = %.2f + %.2f beta1 + %.2f beta2 + (R^2 = %.2f)\n",
				regression1.beta(0), regression1.beta(1), regression1.beta(2), /*regression1.beta(3),*/ regression1.R2());

		System.out.println("Regressao 2");
		MultipleLinearRegression regression2 = new MultipleLinearRegression(x2, y2);

		System.out.printf("Memory Used = %.2f + %.2f beta1 + %.2f beta2  (R^2 = %.2f)\n", regression2.beta(0),
				regression2.beta(1), regression2.beta(2), regression2.R2());

		System.out.println("Regressao 3");
		MultipleLinearRegression regression3 = new MultipleLinearRegression(x3, y3);

		System.out.printf("Log CPU Time with CPU = %.2f + %.2f beta1 + %.2f beta2    (R^2 = %.2f)\n",
				regression3.beta(0), regression3.beta(1), regression3.beta(2), regression3.R2());

		System.out.println("Regressao 4");
		MultipleLinearRegression regression4 = new MultipleLinearRegression(x4, y4);

		System.out.printf("Log Memory with CPU = %.2f + %.2f beta1 + %.2f beta2   (R^2 = %.2f)\n",
				regression4.beta(0), regression4.beta(1), regression4.beta(2), regression4.R2());
		
		System.out.println("Regressao 5");
		MultipleLinearRegression regression5 = new MultipleLinearRegression(x5, y5);
		
		System.out.printf("Log Memory with CPU = %.2f + %.2f beta1 + %.2f beta2   (R^2 = %.2f)\n",
				regression5.beta(0), regression5.beta(1), regression5.beta(2), regression5.R2());
		

		FileWriter arq2 = null;
		PrintWriter gravarArq2 = null;
		try {
			String localTime = java.time.Clock.systemUTC().instant().toString();
			
			arq2 = new FileWriter("baseRegression"+VMManager.usuarioMasCloud+".csv", true);

			gravarArq2 = new PrintWriter(arq2);

			gravarArq2.append("Time " + regression.beta(0) + " " + regression.beta(1) + " beta1 " + regression.beta(2)
					+ " beta2 + " /*+ regression.beta(3) + " beta3 ;*/+"  R^2 " + regression.R2() + "\n");

			gravarArq2.append("CPU " + regression1.beta(0) + " " + regression1.beta(1) + " beta1 " + regression1.beta(2)
					+ " beta2 " + /*regression1.beta(3) + " beta3  ;*/"    R^2 " + regression1.R2() + "\n");

			gravarArq2.append(
					"Memory " + regression2.beta(0) + " " + "+ " + regression2.beta(1) + " beta1 " + regression2.beta(2)
							+ " beta2 " + /*regression4.beta(3) + " beta3 ;*/"    R^2 " + regression2.R2() + "\n");
			arq2.close();
			gravarArq2.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		
		VMManager.provisioning.setR2Time(regression.R2());
		//VMManager.provisioning.setR2CPU(regression1.R2());
		double time = Double.MAX_VALUE;
		double timeTemp = 0, cpuTemp = 0, memoryTemp = 0;
		// csv file containing data
		strFile = base;
		try {
			reader = new CSVReader(new FileReader(strFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double t1 = 0, sumNoUsed = 0;
		int contator = 1;
		VMManager.provisioning.setCpuAvg(0);
		VMManager.provisioning.setCpuSum(0);
		cont = 0;
		
		
		if(VMManager.providerCloud==3) {

			PriceAWS pa = new PriceAWS();
			
			pa.prices();
		}
		
		if(VMManager.providerCloud==4) {

			PriceAzure pa = new PriceAzure();
			
			pa.prices();
		}

		double cpus[] = new double[VMManager.MV.size()];
		double memoirs[] = new double[VMManager.MV.size()];

		for(int z = 0;z < VMManager.MV.size();z++) {
			cpus[z]=VMManager.MV.get(z).getvCPU();
			memoirs[z]=VMManager.MV.get(z).getvCPU();
		}
		
		//int cpus[] = {1,2,4,6,8,12,16,24,32,36,40,48,64,72,96};
		
		while (cont < 14) {


			VMManager.model = new ModelsProvisioning();

			timeTemp=Math.pow(10,regression.beta(0) + regression.beta(1)*Math.log10(memoirs[cont])+
					regression.beta(2)*Math.log10(cpus[cont]));
		
			cpuTemp=(regression1.beta(0) + regression1.beta(1)*(memoirs[cont])+regression1.beta(2)*(cpus[cont])>0?
					regression1.beta(0) + regression1.beta(1)*memoirs[cont]+regression1.beta(2)*cpus[cont]:5);
			
			
			memoryTemp = ((Math.pow(10,regression2.beta(0) + regression2.beta(1) * Math.log10(memoirs[cont]) + 
					regression2.beta(2) * Math.log10(cpus[cont]))>0)?
					Math.pow(10,regression2.beta(0) + regression2.beta(1) * Math.log10(memoirs[cont]) + 
							regression2.beta(2) * Math.log10(cpus[cont])):0.00001);
			
			
			t1 = VMManager.provisioning.getCpuSum() + cpuTemp;
			VMManager.provisioning.setCpuSum(t1);
			VMManager.provisioning.setCpuAvg(VMManager.provisioning.getCpuSum() / contator);
			VMManager.model.setCpuUSED(cpuTemp);
			VMManager.model.setMemoryUSED(memoryTemp);

			VMManager.model.setCpu((int)cpus[cont]);
			VMManager.model.setTime(timeTemp);

			Double priceReal = Double.MAX_VALUE;
			
			
			for(int k=0;k<VMManager.MV.size();k++) {
				int cpu = (int) VMManager.MV.get(k).getvCPU();
				double memoria = VMManager.MV.get(k).getMemory();

				if(cpu ==  cpus[cont] && memoria <= cpus[cont]/0.25) {
					priceReal = VMManager.MV.get(k).getPrice();
				}
			}
				
			
			
			VMManager.model.setPrice(priceReal);
			
			
			VMManager.model.setBalance(((memoryTemp + cpuTemp) / ((priceReal + timeTemp*0.4)*cpus[cont])*100000));
			
			sumNoUsed = sumNoUsed + VMManager.model.getCpuNoUsed();
			VMManager.provisioning.setCpuNoUsedAvg(sumNoUsed / contator);

			if (VMManager.provisioning.getCpuMax() < cpuTemp) {
				VMManager.provisioning.setCpuMax(cpuTemp);

				
			}
			VMManager.valuesCpus.add(VMManager.model);

			System.out.println( " ------- # --------");
			System.out.println( " CPU " + VMManager.model.getCpuUSED() + " CPU " + cpus[cont]);
			System.out.println( " AVG " + VMManager.model.getCpuAvg() + " max " + VMManager.model.getCpuMax() + " tempo "+VMManager.model.getTime());
			System.out.println( " Balan�o " + VMManager.model.getBalance() + " Pre�o "+ VMManager.model.getBalance());
			System.out.println( " Memory use " +VMManager.model.getMemoryUSED());
			System.out.println( " Price " +VMManager.model.getPrice());
			System.out.println( " ------- # --------");
			
			
			cont++;
			

		}

		for (int i = 0; i < VMManager.valuesCpus.size(); i++) {

			ModelsProvisioning m = (ModelsProvisioning) VMManager.valuesCpus.get(i);
			


			if (i == 0) {
				VMManager.model.setTimeSelected(Double.MAX_VALUE);
				VMManager.model.setPriceSelected(Double.MAX_VALUE);
				m.setCpu(VMManager.valuesCpus.get(i).getCpu());
				m.setMemory(VMManager.valuesCpus.get(i).getMemory());
			} else {
				m.setCpu(VMManager.valuesCpus.get(i).getCpu());
			}

			try {
					System.out.println("Decisao "+VMManager.escolhaDecisao);
					if (VMManager.escolhaDecisao == 1 ) {
				
						VMManager.kSession.insert(m);
						VMManager.kSession.fireAllRules();
					}
	
					if (VMManager.escolhaDecisao == 2 ) {
						m = executarPython(m,base);
						break;
					}
					if (VMManager.escolhaDecisao == 3 ) {
						Grasp.grasp();
						break;
					}
				

			} catch (Throwable t) {
				t.printStackTrace();
			}

		}


		// System.out.println("Tamanho "+VMManager.provisioning.getCpusCandidatesSize());
		VMManager.provisioning.setBestBalance(Double.MIN_VALUE);
		for (int i = 0; i < VMManager.provisioning.getCpusCandidatesSize(); i++) {
			// System.out.println("CPU "+VMManager.provisioning.getCpusCandidates(i).getCpu()+"
			// Tempo "+VMManager.provisioning.getCpusCandidates(i).getTime());
			try {

				System.out.println(VMManager.provisioning.getBestBalance()+" "+ VMManager.model.getCpusCandidates(i).getBalance());
				if (VMManager.escolhaDecisao == 1) {
					VMManager.kSession.insert(i);
					VMManager.kSession.fireAllRules();
				}

			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		FileWriter arq;
		PrintWriter gravarArq = null;

		try {
			arq = new FileWriter("statsCPUSelected.csv", true);
			gravarArq = new PrintWriter(arq);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		gravarArq.append(VMManager.transformationAgentQty + "," + VMManager.provisioning.getCpuUsedSelected() + "\n");
		gravarArq.close();

		// System.out.println(VMManager.provisioning.getCpusCandidates()+" KKKKK
		// "+VMManager.transformationAgentQty + "," +VMManager.model.getCpuSelected());

	}
	
	        

	  public ArrayList<MultipleLinearRegression> action2(String base) {

	      double x[][] = null;
	      double y[] = null;
	      double x1[][] = null;
	      double y1[] = null;
	      double x2[][] = null;
	      double y2[] = null;
	      double x3[][] = null;
	      double y3[] = null;
	      double x4[][] = null;
	      double y4[] = null;
	      double x5[][] = null;
	      double y5[] = null;
	      
	      String[] nextLine;
	      String strFile;
	      CSVReader reader = null;
	      int lineNumber = 0;
	      int cont = 1;


	try {
				// csv file containing data
				strFile = base;
				/*
				 * reader = new CSVReader(new FileReader(strFile)); reader.close();
				 */
				reader = new CSVReader(new FileReader(strFile));
				while ((nextLine = reader.readNext()) != null) {
					lineNumber++;
				}

				x = new double[lineNumber][3];
				y = new double[lineNumber];
				x1 = new double[lineNumber][3];
				y1 = new double[lineNumber];
				x2 = new double[lineNumber][3];
				y2 = new double[lineNumber];
				
				x3 = new double[lineNumber][3];
				y3 = new double[lineNumber];
				x4 = new double[lineNumber][3];
				y4 = new double[lineNumber];
				x5 = new double[lineNumber][3];
				y5 = new double[lineNumber];
				
				reader = new CSVReader(new FileReader(strFile));
				lineNumber = 0;
				while ((nextLine = reader.readNext()) != null) {

					// nextLine[] is an array of values from the line

					if (nextLine.length > 10) {
						if (Character.isDigit(nextLine[0].charAt(0)) ) {
							double CPUNotUsed = (double) Double.parseDouble(nextLine[0]);
							//long agents = (long)  (Double.parseDouble(nextLine[4]));  //Integer.parseInt(nextLine[3]);
							double vCPU = (double) (Double.parseDouble(nextLine[2]));
							double CPUUsed = (double) Double.parseDouble(nextLine[1]);
							double time = (double) Double.parseDouble(nextLine[10]);
							double memoryUsed = (double)  Double.parseDouble(nextLine[4]);
							double memory = (double)  Double.parseDouble(nextLine[5])/1000000;
							memoryUsed = ((memoryUsed/1000)*100)/ memory; // Porcentagem de uso

							
							if(memory>0 && vCPU>0 && time>0) {
								x[lineNumber][0] = 1;
								x[lineNumber][1] = Math.log10(Math.abs(memory));
								x[lineNumber][2] = Math.log10(Math.abs(vCPU));
								y[lineNumber] = Math.log10(time);
								
								x1[lineNumber][0] = 1;
								x1[lineNumber][1] = memory;
								x1[lineNumber][2] =  vCPU;
								y1[lineNumber] = CPUUsed;
								
								x2[lineNumber][0] = 1;
								x2[lineNumber][1] = Math.log10(Math.abs(memory));
								x2[lineNumber][2] =  Math.log10(Math.abs(vCPU));
								y2[lineNumber] = Math.log10(Math.abs(memoryUsed));
							
							//Com logs
								x3[lineNumber][0] = 1;
								x3[lineNumber][1] = Math.log10(Math.abs(memory));
								x3[lineNumber][2] = Math.log10(Math.abs(vCPU));
								y3[lineNumber] = time;
								
								x4[lineNumber][0] = 1;
								x4[lineNumber][1] = Math.log10(Math.abs(memory));
								x4[lineNumber][2] = Math.log10(Math.abs(vCPU));
								y4[lineNumber] = Math.log10(Math.abs(CPUUsed));
								
								x5[lineNumber][0] = 1;
								x5[lineNumber][1] = Math.log10(Math.abs(memory));
								x5[lineNumber][2] = Math.log10(Math.abs(vCPU));
								y5[lineNumber] = Math.log10(Math.abs(memoryUsed));
							}

						}
						// System.out.println(nextLine[0] +" "+nextLine[1]+" "+nextLine[2]+"
						// "+nextLine[3]+" "+nextLine[4]);
					}
					lineNumber++;
				}
			} catch (Exception e) {

				e.printStackTrace();
			}

	    //System.out.println("Regressao ");
			MultipleLinearRegression regression = new MultipleLinearRegression(x, y);

			MultipleLinearRegression regression1 = new MultipleLinearRegression(x1, y1);

			MultipleLinearRegression regression2 = new MultipleLinearRegression(x2, y2);

			MultipleLinearRegression regression3 = new MultipleLinearRegression(x3, y3);

			MultipleLinearRegression regression4 = new MultipleLinearRegression(x4, y4);

			MultipleLinearRegression regression5 = new MultipleLinearRegression(x5, y5);
			

	    ArrayList<MultipleLinearRegression> ar =  new ArrayList<MultipleLinearRegression>();

	    ar.add(regression);
	    ar.add(regression1);
	    ar.add(regression2);

	    return ar;


	  }

	Process mProcess;

	public static ModelsProvisioning executarPython(ModelsProvisioning m, String base) {
		try {

			int number1 = 0;
			
	        
	        
	        ProcessBuilder pb = new ProcessBuilder("/usr/bin/python2.7", "otimizacaoOrtoos.py",  base,"1","1","1");
			

			pb.command("/usr/bin/python2.7", "otimizacaoOrtoos.py",  base,"1","1","1");

			pb.redirectErrorStream(true);

			Process p = pb.start();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

			
			double vCPU = 0;
			double memoria = 0;
			String s = "";
			while ((s = in.readLine()) != null) {
				System.out.println(s);
				try {
					s=s.trim().replaceAll("0;0","");
					s=s.trim().replaceAll("\\s+"," ");
					String[] parts = s.split(" ");

					if(parts[0] == "Tempo") {
			            double tempo = Double.parseDouble(parts[1]);
			            VMManager.provisioning.setTimeSelected(tempo);
			            VMManager.provisioning.setTimesCandidates(tempo);
			            VMManager.model.setTimeSelected(tempo);
			            VMManager.model.setTimesCandidates(tempo);
					}
			        else if(parts[0] == "Custo") {
			            double price = Double.parseDouble(parts[1]);
						VMManager.provisioning.setPriceSelected(price);
			            VMManager.model.setPriceSelected(price);
			        }
			        else if(parts[0] == "Prioridade") {
			        	double prioridade = Double.parseDouble(parts[1]);
			        }
			        else if(parts[0].equals("vCPU")) {
			        	
			        	vCPU= Double.parseDouble(parts[1]);
			        	VMManager.model.setCpuSelected((int)vCPU);
			        	VMManager.model.setCpu((int)vCPU);
			        	VMManager.machine.setCpu((int)vCPU);

			        	System.out.println(VMManager.model.getCpuSelected());
			        	System.out.println(VMManager.model.getCpu());
			        	System.out.println(VMManager.machine.getCpu());
			        }
			        else if(parts[0] == "Memoria") {
			        	memoria = Double.parseDouble(parts[1]);
						VMManager.provisioning.setMemoryUSEDSelected(memoria);
						VMManager.model.setMemoryUSEDSelected(memoria);
			        	VMManager.machine.setMemory((float)memoria);
			        }
					
				} catch (Exception e) {

					System.out.println(e);
				}
			}

		} catch (Exception e) {

			System.out.println(e);
		}
		

		
		

		double memory = m.getMemoryUSED() * VMManager.cpuUsageVariable;
		double cpu = m.getCpuUSED() * VMManager.cpuUsageVariable;
		double price = m.getPrice() * VMManager.priceVariable;

		double val = memory + cpu + price;

		m.setBalance(val);

		//System.out.println("Drools regra Balanco price " + m.getBalance() + " CPU " + m.getMemoryUSED());
		VMManager.provisioning.setCpusCandidates(m);
		//System.out.println("Drools regra Balanco price " + m.getBalance() + " CPU " + m.getMemoryUSED());
		return m;
	}
}
