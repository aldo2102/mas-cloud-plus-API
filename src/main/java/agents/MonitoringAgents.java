/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agents;

import static jade.core.behaviours.ParallelBehaviour.WHEN_ALL;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import bean.DadosMonitorados;
import bean.ModelsMonitoringForRules;
import bean.ModelsProvisioning;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MonitoringAgents extends Agent {
    
private static final long serialVersionUID = 1L;
	private int controler = 0;
	public int verificationConect = 0;
	ParallelBehaviour s;
	public static ArrayList dataList = new ArrayList();

	public static long tempoInicial;

	protected void setup() {
		System.out.println("My name is " + getLocalName());

		s = new ParallelBehaviour(this, WHEN_ALL) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public int onEnd() {
				System.out.println("Comportamento Composto Finalizado com Sucesso!");
				return 0;
			}
		};
		s.addSubBehaviour(new CyclicBehaviour(this) {

			private static final long serialVersionUID = 1L;

			public void action() {
				jade.lang.acl.ACLMessage msg = null;

				msg = myAgent.receive();
				if (msg != null) {
					// s.addSubBehaviour(new Conection());
					controler = Integer.parseInt(msg.getContent());
					switch (controler) {
					case 1:
						s.addSubBehaviour(new Monitoring());
						break;
					case 2:
						s.addSubBehaviour(new KillMonitoring());
						break;
					case 3:
						// s.addSubBehaviour(new SalveMonitoring());
						break;
					default:
						break;
					}
				}
				controler = -1;
				msg = null;
			}
		});

		addBehaviour(s);

	}

	protected void takeDown() {

		System.out.println("Agent " + getAID().getLocalName() + " finishing.");
	}
	

	

}

class Monitoring extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final ArrayList owners = new ArrayList();
	private CharSequence liness;
	private String[] lines1;
	private String[] lines2;
	private String[] lines3;
	private String[] lines4;
	// go !

	@Override
	public void action() {

		Process p = null;
		MonitoringAgents.tempoInicial = System.currentTimeMillis();

		try {
			try {
				
				String[] command = { VMManager.vagrant2, VMManager.vagrant3,
						"cd " + VMManager.usuarioMasCloud + " && " + VMManager.vagrant
						+ " ssh -c 'cat /proc/meminfo | grep MemTotal' " };
				System.out.println("cd " + VMManager.usuarioMasCloud + " " + VMManager.vagrant
						+ " ssh -c 'cat /proc/meminfo | grep MemTotal'");
				// System.out.println(command);
				ProcessBuilder pb = new ProcessBuilder(command);
				p = pb.start();

				BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line = "";

				Pattern pat = Pattern.compile("[0-9]+");
				while ((line = reader.readLine()) != null) {

					Matcher valores = pat.matcher(line);
					while(valores.find()) {
						VMManager.machine.setMemory(Float.parseFloat(valores.group().toString()));
						System.out.println("MV com "+VMManager.machine.getMemory()+" de memória");
					}
				}



			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}





			/*System.out.println("Monitoring");
			String[] command1 = { VMManager.vagrant2, VMManager.vagrant3,
					"cd " + VMManager.usuarioMasCloud + " && " + VMManager.vagrant
					+ " ssh -c 'sudo apt-get install dstat' " };
			System.out.println("cd " + VMManager.usuarioMasCloud + " " + VMManager.vagrant
					+ " ssh -c 'sudo apt-get install dstat'");
			// System.out.println(command);
			ProcessBuilder pb1 = new ProcessBuilder(command1);
			Process p2 = pb1.start();*/
			ArrayList valoresN = new ArrayList();
			if (VMManager.providerCloud == 2) {
				System.out.println("Monitoring");
				/*String[] command = { VMManager.vagrant2, VMManager.vagrant3,
						"gcloud compute ssh instancenew" + VMManager.model.getCpuSelected()
						+ " --zone us-central1-c --command='dstat --integer  -cmnd --noheaders'  | tee -a stats.csv" };
				System.out.println("gcloud compute ssh instancenew" + VMManager.model.getCpuSelected()
				+ " --zone us-west1-b command 'dstat -cmnd --noheaders --output stats.csv'  | tee -a stats.csv");
				// System.out.println(command);
				ProcessBuilder pb = new ProcessBuilder(command);
				p = pb.start();*/
			}
			String line = "";
			BufferedReader reader=null;
			if (VMManager.providerCloud == 1 || VMManager.providerCloud == 3 || VMManager.providerCloud == 4) {
				System.out.println("Monitoring");
				/*String[] command22 = { VMManager.vagrant2, VMManager.vagrant3,
						"cd " + VMManager.usuarioMasCloud + " && " + VMManager.vagrant
						+ " ssh -c 'sudo apt install dstat' " };
				System.out.println("cd " + VMManager.usuarioMasCloud + " " + VMManager.vagrant
						+ " ssh -c 'dstat -cmnd --noheaders'  | tee -a stats.csv");
				// System.out.println(command);
				ProcessBuilder pb22 = new ProcessBuilder(command22);
				p = pb22.start();
				p.waitFor();*/

				
				
				do{
					String[] command = { VMManager.vagrant2, VMManager.vagrant3,
							"cd " + VMManager.usuarioMasCloud + " && " + VMManager.vagrant
							+ " ssh -c 'dstat --integer  -cmnd --noheaders '  | tee -a stats.csv" };
					//System.out.println("cd " + VMManager.usuarioMasCloud + " && " + VMManager.vagrant
					//		+ " ssh -c 'dstat -cmnd --noheaders'  | tee -a stats.csv");
					// System.out.println(command);
					ProcessBuilder pb = new ProcessBuilder(command);
					p = pb.start();
					reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
					
				}while((line = reader.readLine()) == null );
				
				
				/*try {
					int x=p.waitFor();
					while(x==0) {
						pb = new ProcessBuilder(command);
						p = pb.start();
						x=p.waitFor();
						System.out.println(x);
						p = pb.start();
					}

				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
			}

			/*String splitBy1 = "\n";
			String split = "\\|[ ]{1,}|[ ]{1,}\\|[ ]{1,}|[ ]{1,}\\||[a-zA-Z]\\|[ ]{1,}|[a-zA-Z]\\||[a-zA-Z][ ]{1,}|[ ]{1,}|\\||[a-zA-Z]";*/
			DadosMonitorados date = null;
			lines1 = null;
			lines2 = null;
			lines3 = null;
			lines4 = null;
			liness = null;
			
			
			while ((line = reader.readLine()) != null) {
				//System.out.println(line);
				line=line.trim().replaceAll("0;0","");
				line=line.trim().replaceAll("[a-zA-Z]+\\|"," ");
				line=line.trim().replaceAll("\\s+"," ");
				line=line.trim().replaceAll("\\|+","");
				line=line.trim().replaceAll("[a-zA-Z]+","");
				line=line.trim().replaceAll(" ","P");
				Pattern pat = Pattern.compile("[0-9]+");
				valoresN = null;
				valoresN = new ArrayList();
				Matcher valores = null;
				valores = pat.matcher(line);
				//System.out.println(line);
				String[] lines1 = line.split("[0-9]+");
				
				//System.out.println("Tamanho "+lines1.length );
				while(valores.find()) {
					boolean numeric = true;

					Double num =null;
					try {
						
						//System.out.println("group "+valores.group());
						num = Double.parseDouble(valores.group().toString());
					} catch (NumberFormatException e) {
						numeric = false;
					}

					if(numeric)
						valoresN.add(num);
					else {}
					
						//System.out.println(num + "no is a number");

				}
				//System.out.println("antes "+" - "+valoresN.size());
				/*System.out.println(lines1.length+" Tamanho das linhas "+lines1[5]);
				System.out.println(lines1.length+" Tamanho das linhas "+lines1[6]);
				System.out.println(lines1.length+" Tamanho das linhas "+lines1[7]);
				System.out.println(lines1.length+" Tamanho das linhas "+lines1[8]);
				System.out.println(lines1.length+" Tamanho das linhas "+lines1[9]);
				System.out.println(lines2.length+" Tamanho das linhas "+Arrays.toString(lines2));
				System.out.println(lines3.length+" Tamanho das linhas "+Arrays.toString(lines3));
				System.out.println(lines4.length+" Tamanho das linhas "+Arrays.toString(lines4));*/

				if (valoresN.size() == 13 || valoresN.size() == 14) {
					
					date = new DadosMonitorados();
					try {
						
						if(VMManager.providerCloud == 4 && valoresN.size() == 14) {
							//System.out.println("antes **"+ valoresN.get(0)+"**"+ valoresN.get(1)+"**"+ valoresN.get(2)+"**"+ valoresN.get(3)+"**"+ valoresN.get(4)+"**"+ valoresN.get(5)+"**"+ valoresN.get(6));
							date.setUsrC(Double.parseDouble(valoresN.get(0).toString()));
							date.setSysC(Double.parseDouble(valoresN.get(1).toString()));
							date.setIdlC(Double.parseDouble(valoresN.get(2).toString()));
							date.setWaiC(Double.parseDouble(valoresN.get(3).toString()));
							date.setHiqC(Double.parseDouble(valoresN.get(4).toString()));
	
							date.setUsedM(Double.parseDouble(valoresN.get(6).toString()));
							date.setBuffM(Double.parseDouble(valoresN.get(7).toString()));
							date.setCachM(Double.parseDouble(valoresN.get(8).toString()));
							date.setFreeM(Double.parseDouble(valoresN.get(9).toString()));
	
							date.setRecvN(Double.parseDouble(valoresN.get(10).toString()));
							date.setSendN(Double.parseDouble(valoresN.get(11).toString()));
	
							date.setReadD(Double.parseDouble(valoresN.get(12).toString()));
							date.setWritD(Double.parseDouble(valoresN.get(13).toString()));
							//System.out.println("pois **"+ valoresN.get(7)+"**"+ valoresN.get(8)+"**"+ valoresN.get(9)+"**"+ valoresN.get(10)+"**"+ valoresN.get(11)+"**"+ valoresN.get(12));
						
							
						}
						else {
						
							//System.out.println("antes **"+ valoresN.get(0)+"**"+ valoresN.get(1)+"**"+ valoresN.get(2)+"**"+ valoresN.get(3)+"**"+ valoresN.get(4)+"**"+ valoresN.get(5)+"**"+ valoresN.get(6));
							date.setUsrC(Double.parseDouble(valoresN.get(0).toString()));
							date.setSysC(Double.parseDouble(valoresN.get(1).toString()));
							date.setIdlC(Double.parseDouble(valoresN.get(2).toString()));
							date.setWaiC(Double.parseDouble(valoresN.get(3).toString()));
							date.setHiqC(Double.parseDouble(valoresN.get(4).toString()));
	
							date.setUsedM(Double.parseDouble(valoresN.get(5).toString()));
							date.setFreeM(Double.parseDouble(valoresN.get(6).toString()));
							date.setBuffM(Double.parseDouble(valoresN.get(7).toString()));
							date.setCachM(Double.parseDouble(valoresN.get(8).toString()));
	
							date.setRecvN(Double.parseDouble(valoresN.get(9).toString()));
							date.setSendN(Double.parseDouble(valoresN.get(10).toString()));
	
							date.setReadD(Double.parseDouble(valoresN.get(11).toString()));
							date.setWritD(Double.parseDouble(valoresN.get(12).toString()));
							//System.out.println("pois **"+ valoresN.get(7)+"**"+ valoresN.get(8)+"**"+ valoresN.get(9)+"**"+ valoresN.get(10)+"**"+ valoresN.get(11)+"**"+ valoresN.get(12));
						}


					}
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					


					//System.out.println("REDE "+DadosMonitorados.SUMsendN+" "+date.getSendN()+" "+DadosMonitorados.cont+" REDE");
					DadosMonitorados.cont++;
					DadosMonitorados.SUMsendN = DadosMonitorados.SUMsendN+date.getSendN();
					DadosMonitorados.SUMrecvN = DadosMonitorados.SUMrecvN+date.getRecvN();
					DadosMonitorados.AVGsendN = DadosMonitorados.SUMsendN / DadosMonitorados.cont;
					DadosMonitorados.AVGrecvN = DadosMonitorados.SUMrecvN / DadosMonitorados.cont;

					//System.out.println("CPU "+DadosMonitorados.SUMCPU+" "+date.getUsrC()+" "+DadosMonitorados.cont+" CPU");

					DadosMonitorados.SUMCPU = DadosMonitorados.SUMCPU + date.getUsrC() + date.getSysC();
					DadosMonitorados.SUMCPUidl = DadosMonitorados.SUMCPUidl + date.getIdlC();
					DadosMonitorados.AVGCPU = DadosMonitorados.SUMCPU / DadosMonitorados.cont;
					DadosMonitorados.AVGCPUidl = DadosMonitorados.SUMCPUidl / DadosMonitorados.cont;

					//System.out.println("Memória "+DadosMonitorados.SUMMemory+" "+date.getUsedM()+" "+DadosMonitorados.cont+" Memória");
					DadosMonitorados.SUMMemory = DadosMonitorados.SUMMemory + date.getUsedM();
					DadosMonitorados.AVGMemory = DadosMonitorados.SUMMemory / DadosMonitorados.cont;

					DadosMonitorados.SUMreadD = DadosMonitorados.SUMreadD + date.getReadD();
					DadosMonitorados.AVGreadD= DadosMonitorados.SUMreadD / DadosMonitorados.cont;
					DadosMonitorados.SUMwritD = DadosMonitorados.SUMwritD + date.getWritD();
					DadosMonitorados.AVGwritD= DadosMonitorados.SUMwritD / DadosMonitorados.cont;
					MonitoringAgents.dataList.add(date);

				}


				try {
					ModelsMonitoringForRules m = new ModelsMonitoringForRules();
					m.setTime(System.currentTimeMillis() - MonitoringAgents.tempoInicial);
					m.setCPUUsed(DadosMonitorados.AVGCPUidl);

					//System.out.println(Double.parseDouble(lines1[1]) +" ** "+ Double.parseDouble(lines1[2]));
					//System.out.println(m.getTime());

					//System.out.println(m.getCPUUsed());

					//System.out.println("CPU: "+ VMManager.machine.getCpu()+" ** "+ DadosMonitorados.AVGCPU +" ** "+ date.getSUMCPU() );
					//System.out.println("Memory: " +VMManager.machine.getMemory() +" ** "+ DadosMonitorados.SUMMemory +" ** "+ DadosMonitorados.AVGMemory);
					/*System.out.println(DadosMonitorados.SUMCPUidl);
							System.out.println(DadosMonitorados.AVGCPU);
							System.out.println(DadosMonitorados.AVGCPUidl);
							System.out.println(ModelsMonitoringForRules.getTime()/1000 < 0.5*600); //50% de 10 minutos*/

					if(m==null) {
						System.out.println("null");
					}else {
						//System.out.println(m);
					}

					/*VMManager.kSession.insert(m);
							VMManager.kSession.fireAllRules();*/

				} catch (Throwable t) {
					t.printStackTrace();
				}

				//}


			}

		} catch (NumberFormatException | IOException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.takeDown();
	}

	protected void takeDown() {

	}

}

class KillMonitoring extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final ArrayList owners = new ArrayList();

	@Override
	public void action() {

		System.out.println("KillMonitoring");
		System.out.println(MonitoringAgents.dataList.size());

		/*for (int i = 0; i < MonitoringAgents.dataList.size(); i++) {
			System.out.println("Buff Memory " + ((DadosMonitorados) MonitoringAgents.dataList.get(i)).getBuffM()
					+ " , Cach Memory =" + ((DadosMonitorados) MonitoringAgents.dataList.get(i)).getCachM()
					+ " , Free Memory=" + ((DadosMonitorados) MonitoringAgents.dataList.get(i)).getFreeM()
					+ " , Hiq CPU=" + ((DadosMonitorados) MonitoringAgents.dataList.get(i)).getHiqC() + " , IDL CPU="
					+ ((DadosMonitorados) MonitoringAgents.dataList.get(i)).getIdlC() + "]");
		}*/
		this.takeDown();

	}

	protected void takeDown() {
		// Printout a dismissal message
		System.out.println("Agent-1 "+getAgent().getName()+" terminating.");
	}

}

