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
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;
import jade.wrapper.StaleProxyException;
import bean.DadosMonitorados;
import bean.ModelsMonitoringForRules;
import bean.ModelsProvisioning;

import java.net.InetAddress;
import java.text.SimpleDateFormat;

@SuppressWarnings("unused")
public class ManagerG extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	ParallelBehaviour s = new ParallelBehaviour();
	SequentialBehaviour seqBehaviour = new SequentialBehaviour();

	protected void setup() {
		synchronized (this) {
			System.out.println("Agent " + getLocalName() + " started.");

			addBehaviour(new StartGM());
			addBehaviour(new StartMonitoring());
			/*try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			addBehaviour(new StartAPP());

			// s.addSubBehaviour(seqBehaviour);
			
			//System.out.println("Stop Monitoring");
			//addBehaviour(new StopMonitoring());
			// addBehaviour(new SerializableMonitoring());

			// s.addSubBehaviour(pr);

		}

	}

	protected void takeDown() {
		System.out.println("Agent " + getAID().getLocalName() + " finishing.");

	}

}

class StartAPP extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void action() {

		Process p = null;

		String finalizado = "";
		try {
			if (VMManager.providerCloud == 2) {
				System.out.println("Executando MASE");

				String[] command = { VMManager.vagrant2, VMManager.vagrant3,
						"gcloud compute ssh instancenew" + VMManager.model.getCpuSelected()
								+ " --zone us-central1-c --command='" + VMManager.command + "'" };

				ProcessBuilder pb = new ProcessBuilder(command);
				p = pb.start();
				OutputStream rsyncStdIn = p.getOutputStream();
				rsyncStdIn.write("aldoaldo".getBytes());
				BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line = "";
				String[] lines1;
				while (((line = reader.readLine()) != null)) {

					System.out.println(line);
				}
			}

			if (VMManager.providerCloud == 1 || VMManager.providerCloud == 3 || VMManager.providerCloud == 4) {
				System.out.println("Executando MASE - Vagrant");

				String teste = "";
				int verification = 0;
				/*while (!teste.equals("poweroff (virtualbox)")) {
					System.out.println(teste);
					String[] command = { VMManager.vagrant2, VMManager.vagrant3,
							"cd " + VMManager.usuarioMasCloud + " && " + VMManager.vagrant + " status" };
					
                                        System.out.println("P "+p);
					if(p!=null) {
						try {
					
							Thread.sleep(10000);
							p.waitFor();
							System.out.println("waitFor");
							p.destroy();
							System.out.println("destroy");
						} 
						catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					ProcessBuilder pb = new ProcessBuilder(command);
					p = pb.start();
					// OutputStream rsyncStdIn = p.getOutputStream();
					// rsyncStdIn.write("aldoaldo".getBytes());
					BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String line = "";
					String[] lines1;
					int count = 0;
					while (((line = reader.readLine()) != null)) {

						String split = "default                   ";
						lines1 = line.split(split);

						if (lines1.length > 1) {
							teste = lines1[1];
							System.out.println(lines1[0] + " - " + lines1[1]);
							if (lines1[1].equals("running (virtualbox)") || lines1[1].equals("running (aws)") || lines1[1].equals("running (azure)")) {
								verification = 0;
								System.out.println("11 " + verification);
							}
							// if(!lines1[1].equals("running (virtualbox)") || !lines1[1].equals("running
							// (aws)"))
							else {
								System.out.println("22 " + verification);
								verification = 1;
								break;
							}
							System.out.println(verification + " Rodando " + count);
						}

						System.out.println("44 "+line);
						count++;
						
					}

					
					if (verification == 1) {
						System.out.println("33 " + verification);
						break;
					}
				}*/
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Erro 11");
			e.printStackTrace();
		}
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			VMManager.stopPlatform();
			e.printStackTrace();
		}

		// VMManager.stopPlatform();
		Conection();
		System.out.println(finalizado);

	}

	public void Conection() {
		Process p1;

		try {
			if (VMManager.providerCloud == 2) {
				p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 " + VMManager.machine.getIp());
				// System.out.println("ping -c 1 "+VMManager.machine.getIp());
				String[] command2 = { VMManager.vagrant2, VMManager.vagrant3,
						"gcloud compute ssh instancenew" + VMManager.model.getCpuSelected()
								+ " --zone us-central1-c --command='cat executando'" };
				System.out.println("gcloud compute ssh instancenew" + VMManager.model.getCpuSelected()
						+ " --zone us-central1-c --command='cat executando'");
				ProcessBuilder pb = new ProcessBuilder(command2);
				Process pr;
				pr = pb.start();
				OutputStream rsyncStdIn = pr.getOutputStream();
				rsyncStdIn.write("aldoaldo".getBytes());
				String line = "";
				BufferedReader reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
				while ((line = reader.readLine()) != null) {
					VMManager.verification = line;
				}
				pr.waitFor();
				int returnVal = p1.waitFor();
				//Thread.sleep(2000);
				boolean reachable = (returnVal == 0);
				if (reachable && VMManager.verification.equals("executando")) {
					System.out.println("Connect");
					VMManager.vm = 1;
				} else {
					VMManager.vm = 0;
					System.out.println("No Connect");
					/*
					 * try { String[] command = { VMManager.vagrant2, VMManager.
					 * vagrant3,"echo 'Y\n' | gcloud compute instances delete instancenew"+Starter.
					 * Starter.model.getCpuSelected()+" --zone 'us-central1-c'"}; pb = new
					 * ProcessBuilder(command); Process pp = pb.start(); rsyncStdIn =
					 * pr.getOutputStream (); rsyncStdIn.write ("aldoaldo".getBytes ()); reader =
					 * new BufferedReader(new InputStreamReader(pp.getInputStream())); line="";
					 * 
					 * try { while ((line = reader.readLine()) != null) { System.out.println(line);
					 * } } catch (NumberFormatException | IOException e) { // TODO Auto-generated
					 * catch block e.printStackTrace(); } pp.waitFor();
					 * 
					 * 
					 * } catch (IOException|InterruptedException e) { // TODO Auto-generated catch
					 * block e.printStackTrace(); }
					 */
				}
			}
			/*
			 * if (VMManager.providerCloud == 3) { p1 =
			 * java.lang.Runtime.getRuntime().exec("ping -c 1 " +
			 * VMManager.machine.getIp()); //
			 * System.out.println("ping -c 1 "+VMManager.machine.getIp()); String[]
			 * command2 = { VMManager.vagrant2, VMManager.vagrant3,
			 * "vagrant ssh -c'cat executando'" };
			 * System.out.println("vagrant ssh -c'cat executando'"); ProcessBuilder pb = new
			 * ProcessBuilder(command2); Process pr; pr = pb.start(); String line = "";
			 * BufferedReader reader = new BufferedReader(new
			 * InputStreamReader(pr.getInputStream())); while ((line = reader.readLine()) !=
			 * null) { VMManager.verification = line; } pr.waitFor(); int returnVal =
			 * p1.waitFor(); Thread.sleep(2000); boolean reachable = (returnVal == 0); if
			 * (reachable && VMManager.verification.equals("executando")) {
			 * System.out.println("Connect"); VMManager.vm = 1; } else {
			 * VMManager.vm = 0; System.out.println("No Connect");
			 * 
			 * } }
			 */

		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

class StartMonitoring extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void action() {

		System.out.println("Monitoring");
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.addReceiver(new AID("MonitoringAgents", AID.ISLOCALNAME));
		message.setContent("1");
		myAgent.send(message);
		System.out.println("1");
	}

}

class SerializableMonitoring extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void action() {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.addReceiver(new AID("MonitoringAgents", AID.ISLOCALNAME));
		message.setContent("3");
		myAgent.send(message);
		System.out.println("3");
	}

}




class StartGM extends OneShotBehaviour {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	public void action() {
		AgentController novoAgent = null;
		VMManager.platform = myAgent.getContainerController();

		try {
			novoAgent = VMManager.platform.createNewAgent("MonitoringAgents", "agents.MonitoringAgents", null);
		} catch (ControllerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			novoAgent.start();
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	protected void takeDown() {

	}

}
