package agents;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.builder.KieFileSystem;

import com.opencsv.CSVReader;

import bean.ConfCurrentMachine;
import bean.DadosMonitorados;
import bean.ModelPricesAWS;
import bean.ModelsConfigMachines;
import bean.ModelsMonitoringForRules;
import bean.ModelsProvisioning;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;
import predictions.MultipleLinearRegression;
import prices.PriceAWS;
import prices.PriceGoogleCloud;
import prices.PriceVangrant;
import provisioning.Grasp;

public class VMManager extends Agent {
	public static long transformationAgentQty;
	public static int incremmet;

	public static PlatformController platform;
	public static AgentController geral = null;
	// public static int QuantCPU =0;
	private static final long serialVersionUID = 1L;
	public static ArrayList<ModelsProvisioning> valuesCpus = new ArrayList<ModelsProvisioning>();
	public static ArrayList<ModelPricesAWS> MV = new ArrayList<ModelPricesAWS>();
	public static ModelsProvisioning model = new ModelsProvisioning();
	public static ModelsConfigMachines prices = new ModelsConfigMachines();
	public static ConfCurrentMachine machine = new ConfCurrentMachine();
	public static String verification;
	public static int vm = 0; // verificaÃ§Ã£o do estatus da maquina
	public static ModelsMonitoringForRules monitoringRules = new ModelsMonitoringForRules();
	public static KieServices ks;
	public static KieContainer kContainer;
	public static KieSession kSession;
	public static long tempoModel;
	public static String usuario;
	public static int providerCloud;
	public static float timeVariable;
	public static float cpuUsageVariable;
	public static float priceVariable;
	public static int minimumTime;
	public static int lineNumber;
	public static String command;
	public static String box;
	public static String usuarioMasCloud;

	public static ModelsProvisioning provisioning = new ModelsProvisioning();
	public static int escolhaDecisao;

	public static String vagrant;
	public static String vagrant2;
	public static String vagrant3;

	public static int monitoring;

	public static String strFile;

	// Inicia a execução
	public static void starter(String command, int incremmet, int providerCloud, float timeVariable,
			float cpuUsageVariable, float priceVariable, String box, String usuarioMasCloud, int escolhaDecisao,String adressFile,
			String adressFilePrivate,String adressFilePublic, String userGoogle) {

		System.out.println("MAS-Cloud 2.1");

		VMManager.command = command;
		VMManager.incremmet = incremmet;
		VMManager.providerCloud = providerCloud;
		VMManager.timeVariable = timeVariable;
		VMManager.cpuUsageVariable = cpuUsageVariable;
		VMManager.priceVariable = priceVariable;
		VMManager.box = box;
		VMManager.usuarioMasCloud = usuarioMasCloud;
		VMManager.escolhaDecisao = escolhaDecisao;

		// Argumento 4 diz o tempo
		// Argumento 5 diz o Uso de CPU
		// Argumento 6 diz o Pre�o
		monitoring = 0;
		create();
		String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
		System.out.println(OS);

		ProcessBuilder processBuilder = new ProcessBuilder();

		if (OS.equals("windows 10")) {
			vagrant3 = "/c";
			vagrant2 = "cmd.exe";
			vagrant = "C:\\Vagrant\\bin\\vagrant.exe";
		} else {
			vagrant3 = "-c";
			vagrant2 = "/bin/bash";
			vagrant = "/opt/vagrant/bin/vagrant";

		}

		Runtime rt = Runtime.instance();

		// p.setParameter(Profile.MAIN_HOST, "localhost");
		int port = 10000;
		int controler = 1;

		ServerSocket socket = null;
		try {
			Random gerador = new Random();
			socket = new ServerSocket(0);
			port = socket.getLocalPort() + (gerador.nextInt(25) + 1);
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		ProfileImpl pi = new ProfileImpl();
		pi.setParameter(Profile.MAIN_PORT, port + "");

		ContainerController cc = Runtime.instance().createMainContainer(pi);
		System.out.println("-Main-Container" + port);

		System.out.println(jade.core.Runtime.instance());
		// ContainerController mainContainer = rt.createMainContainer(p);

		try {
			platform = cc.getPlatformController();
		} catch (ControllerException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		// timeVariable = timeVariable / 100;
		// cpuUsageVariable = cpuUsageVariable / 100;
		// priceVariable = priceVariable / 100;
		System.out.println(timeVariable + " " + cpuUsageVariable + " " + priceVariable);

		VMManager.usuarioMasCloud = "" + usuarioMasCloud;

		strFile = "" + usuarioMasCloud + "/" + "base.csv";

		File f = new File(strFile);
		System.out.println(strFile);
		String s;
		Process p;
		try {
			/*
			 * p = Runtime.getRuntime().exec("pwd"); BufferedReader br = new
			 * BufferedReader(new InputStreamReader(p.getInputStream())); while ((s =
			 * br.readLine()) != null) System.out.println("line: " + s); p.waitFor();
			 * System.out.println("exit: " + p.exitValue()); p.destroy();
			 */
		} catch (Exception e) {
		}
		if (f.exists() && !f.isDirectory()) {
			System.out.println("Arquivo base já existe");
		} else {
			System.out.println("Arquivo base não existe");
			try {

				if (f.createNewFile()) {
					System.out.println("File created: " + f.getName());
				} else {
					System.out.println("File already exists.");
				}
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
		}

		// String commandTrans = command;
		// transformationAgentQty = Integer.parseInt(args[1]);
		transformationAgentQty = command.hashCode();

		System.out.println("Agents of Transfomation " + transformationAgentQty);
		System.out.println(command);
		System.out.println(transformationAgentQty);
		System.out.println("Quantidade de agentes:" + transformationAgentQty);
		/*
		 * selecionar o provedor de Nuvem 1. Google Cloud 2. AWS
		 */

		try {
			File diretorio = new File(usuarioMasCloud);
			diretorio.mkdir();
		} catch (Exception ex) {
			System.out.println(ex);
		}

		try {

			KieServices nks = KieServices.Factory.get();
			KieFileSystem kfs = ks.newKieFileSystem();
			kContainer = ks.getKieClasspathContainer();
			System.out.println("1 ----");
			System.out.println(KieServices.Factory.get().toString());
			kSession = kContainer.newKieSession("ksession-rules");
			System.out.println("3 ----");

			tempoModel = System.currentTimeMillis();

		} catch (Exception t) {
			t.printStackTrace();

			// System.exit(1);
		}

		Process pr = null;

		BufferedReader br = null;
		try {
			File f2 = new File(usuarioMasCloud + "/" + "falha.csv");
			if (f2.exists() && !f2.isDirectory()) {
				br = new BufferedReader(new FileReader(usuarioMasCloud + "/" + "falha.csv"));
				try {
					StringBuilder sb = new StringBuilder();
					String line = br.readLine();
					String[] lines1;
					while (line != null) {
						System.out.println(line);
						lines1 = line.split(",");
						sb.append(line);
						sb.append(System.lineSeparator());
						line = br.readLine();
						// System.out.println("Teste leitura "+lines1[0]+" && "+lines1[1]+" &&
						// "+lines1[2]+"
						// "+lines1[lines1.length-2]);
					}
					br.close();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				vm++;
			}
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		FileWriter arqFalha = null;
		PrintWriter gravarArqFalha = null;
		File f2 = new File(usuarioMasCloud + "/" + "falha.csv");
		if (f2.exists() && !f2.isDirectory()) {

			try {
				arqFalha = new FileWriter(usuarioMasCloud + "/" + "falha.csv", false);
				gravarArqFalha = new PrintWriter(arqFalha);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			gravarArqFalha.append(
					command + ", " + incremmet + ", " + providerCloud + ", " + timeVariable + ", " + cpuUsageVariable
							+ ", " + priceVariable + ", " + box + ", " + usuarioMasCloud + ", " + escolhaDecisao);
			gravarArqFalha.close();
		}

		// Google Cloud
		if (providerCloud == 1) {
			try {
				PriceGoogleCloud.prices();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			minimumTime = 600;
			System.out.println(prices.getCpuPrice());
			System.out.println(prices.getMemoryPrice());
		} // AWS
		else if (providerCloud == 2) {
			try {
				PriceAWS.prices();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (providerCloud == 3 || providerCloud == 4) {
			PriceVangrant.prices();
			System.out.println(prices.getCpuPrice());
			System.out.println(prices.getMemoryPrice());
		}

		BufferedReader reader;
		String line;
		provisioning.setCpuUsedSelected(0);
		provisioning.setPriceSelected(Double.MAX_VALUE);
		provisioning.setTimeSelected(Double.MAX_VALUE);
		provisioning.setBestBalance(Double.MIN_VALUE);
		ModelsMonitoringForRules.setTotalSteps(365);
		valuesCpus = new ArrayList<ModelsProvisioning>();
		model = new ModelsProvisioning(0, 0, 0, 0, 0);
		monitoringRules = new ModelsMonitoringForRules(0);

		model.setCpuUsedSelected(0);

		model.setTimeSelected(Double.MAX_VALUE);
		model.setBestBalance(Double.MIN_VALUE);
		ModelsMonitoringForRules.setTotalSteps(365);
		valuesCpus = new ArrayList<ModelsProvisioning>();
		model = new ModelsProvisioning(0, 0, 0, 0, 0);
		monitoringRules = new ModelsMonitoringForRules(0);
		provisioning.setTimesCandidates(0);

		System.out.println("plataforma " + platform.getState());
		synchronized (platform) {
			String[] nextLine;

			int cont = 1;
			CSVReader reader2 = null;

			// csv file containing data
			try {
				System.out.println(strFile);
				File fl = new File(strFile);
				if (!fl.exists()) {
					fl.createNewFile();
				} else {
					System.out.println("File already exists");
				}
				reader2 = new CSVReader(new FileReader(strFile));
				lineNumber = 0;
				while ((nextLine = reader2.readNext()) != null) {
					if (nextLine.length > 1) {
						lineNumber++;
					}
				}
				reader2.close();

			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Quantidade de linhas " + lineNumber);
		}

		if (lineNumber < 13) {
			switch (lineNumber) {
			case 0:
				model.setCpuSelected(1);
				break;
			case 1:
				model.setCpuSelected(2);
				break;
			case 2:
				model.setCpuSelected(4);
				break;
			case 3:
				model.setCpuSelected(6);
				break;
			case 4:
				model.setCpuSelected(8);
				break;
			case 5:
				model.setCpuSelected(12);
				break;
			case 6:
				model.setCpuSelected(16);
				break;
			case 7:
				model.setCpuSelected(24);
				break;
			case 8:
				model.setCpuSelected(36);
				break;
			case 9:
				model.setCpuSelected(36);
				break;
			case 10:
				model.setCpuSelected(40);
				break;
			case 11:
				model.setCpuSelected(48);
				break;
			case 12:
				model.setCpuSelected(64);
				break;
			case 13:
				model.setCpuSelected(72);
				break;
			case 14:
				model.setCpuSelected(96);
				break;
			}
		} else {
			try {

				System.out.println(strFile);
				long startTime = System.nanoTime();
				if (escolhaDecisao == 1 || escolhaDecisao == 2) {

					System.out.println("---------------- Entrou na regressão ----------------");
					MultipleLinearRegression Prov = new MultipleLinearRegression(strFile);
				}
				if (escolhaDecisao == 3) {

					System.out.println("---------------- Entrou na GRASP ----------------");
					Grasp.grasp();
				}

				long stopTime = System.nanoTime();

				long tempoEscolha = stopTime - startTime;
				System.out.println("Tempo de escolha " + tempoEscolha);

				FileWriter arqTempoEscolha = null;
				File f1 = new File(usuarioMasCloud + "/" + "tempoEscolhas.csv");

				try {
					arqTempoEscolha = new FileWriter(usuarioMasCloud + "/" + "tempoEscolhas.csv", true);
					// gravarArqTempoEscolha = new PrintWriter(arqTempoEscolha);

					BufferedWriter gravarArqTempoEscolha = new BufferedWriter(arqTempoEscolha);
					gravarArqTempoEscolha.write(tempoEscolha + ", " + escolhaDecisao + ", " + lineNumber + " \n");
					gravarArqTempoEscolha.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (RuntimeException t) {
				System.out.println(t.getMessage() + " " + t.toString());
				System.out.println("---------------- Aten��o entrou em Cath, Verifique ----------------");
				Random r = new Random();
				int r2 = (int) Math.pow(2, r.nextInt(12));
				switch (r2) {
				case 0:
					model.setCpuSelected(1);
					break;
				case 1:
					model.setCpuSelected(2);
					break;
				case 2:
					model.setCpuSelected(4);
					break;
				case 3:
					model.setCpuSelected(6);
					break;
				case 4:
					model.setCpuSelected(8);
					break;
				case 5:
					model.setCpuSelected(12);
					break;
				case 6:
					model.setCpuSelected(16);
					break;
				case 7:
					model.setCpuSelected(24);
					break;
				case 8:
					model.setCpuSelected(32);
					break;
				case 9:
					model.setCpuSelected(36);
					break;
				case 10:
					model.setCpuSelected(40);
					break;
				case 11:
					model.setCpuSelected(48);
					break;
				case 12:
					model.setCpuSelected(64);
					break;
				case 13:
					model.setCpuSelected(72);
					break;
				case 14:
					model.setCpuSelected(96);
					break;
				}
				machine.setCpu(r2);
				model.setCpuSelected(r2);
			}

			System.out.println(" ________________________________________________________________");
			verification = "";
			vm = 0;
			System.exit(1);
			System.out.println("Time: " + model.getTimeSelected() + " CpuUSED " + model.getCpuSelected()
					+ " cpu alocation " + provisioning.getCpuUsedSelected());

		}

		ProcessBuilder pb = new ProcessBuilder();

		// AWS
		if (providerCloud == 1) {
			String str = "Vagrant.configure(\"2\") do |config|\n"
					+ "  config.vm.box = \"google/gce\"\n"
					+ "\n"
					+ "  config.vm.provider :google do |google, override|\n"
					+ "    google.google_project_id = \"cloudvagrant\"\n"
					+ "    google.disk_name = 'bigdata'\n"
					+ "    google.machine_type = 'e2-standard-4'\n"
					+ "    google.image = 'bigdata'\n"
					+ "    google.google_json_key_location = \"KEY.json\"\n"
					+ "    override.ssh.username = \""+userGoogle+"\"\n"
					+ "    override.ssh.private_key_path = \"KEY\"\n"
					+ "  end\n"
					+ "\n"
					+ "end";

			System.out.println("Quantidade de CPUs " + model.getCpuSelected());
			// usuarioMasCloud = "aldohenrique";
			try {

				System.out.println(usuarioMasCloud + "/" + "Vagrantfile");
				File myObj = new File(usuarioMasCloud + "/" + "Vagrantfile");
				if (myObj.createNewFile()) {
					System.out.println("File created: " + myObj.getName());
				} else {
					System.out.println("File already exists.");
				}
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}

			try (FileWriter writer = new FileWriter(usuarioMasCloud + "/" + "Vagrantfile");
					BufferedWriter bw = new BufferedWriter(writer)) {

				bw.write(str);

			} catch (IOException e) {
				System.err.format("IOException: %s%n", e);
			}
		}

		// AWS
		if (providerCloud == 2) {
			String str = " class Hash" + "def slice(*keep_keys)" + "h = {}"
					+ "keep_keys.each { |key| h[key] = fetch(key) if has_key?(key) }" + "h"
					+ "end unless Hash.method_defined?(:slice)" + "def except(*less_keys)" + "slice(*keys - less_keys)"
					+ "end unless Hash.method_defined?(:except)" + "end"

					+ "Vagrant.configure('2') do |config|" + "config.vm.box = 'dummy'"

					+ "config.vm.provider :aws do |aws, override|" + "aws.access_key_id = 'KEY'"
					+ "aws.secret_access_key = 'KEY'"
					+ "aws.security_groups = ['aldo']" + "aws.keypair_name = 'KEY'"
					+ "aws.instance_type='t2.2xlarge'" + "aws.ami = 'ami-049fe7573cec0ed0f'"
					+ "aws.region = 'us-east-1'" + "override.ssh.username = 'ubuntu'"
					+ "override.ssh.private_key_path = 'KEY.pem'" + "end" + "end";

			System.out.println("Quantidade de CPUs " + model.getCpuSelected());
			// usuarioMasCloud = "aldohenrique";
			try {

				System.out.println(usuarioMasCloud + "/" + "Vagrantfile");
				File myObj = new File(usuarioMasCloud + "/" + "Vagrantfile");
				if (myObj.createNewFile()) {
					System.out.println("File created: " + myObj.getName());
				} else {
					System.out.println("File already exists.");
				}
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}

			try (FileWriter writer = new FileWriter(usuarioMasCloud + "/" + "Vagrantfile");
					BufferedWriter bw = new BufferedWriter(writer)) {

				bw.write(str);

			} catch (IOException e) {
				System.err.format("IOException: %s%n", e);
			}
		}

		if (providerCloud == 3) {

			String mvAws = "t2.micro";

			System.out.println("CPU SELECIONADO " + model.getCpuSelected());
			System.out.println("-- CPU SELECIONADO " + machine.getCpu());
			switch (model.getCpuSelected()) {
			case 1:
				mvAws = "t2.micro";
				break;
			case 2:
				mvAws = "m4.large";
				break;
			/*
			 * case 4: mvAws = "m4.xlarge"; break;
			 */
			case 4:
				mvAws = "r4.xlarge";
				break;
			/*
			 * case 6: mvAws = "m4.2xlarge"; break;
			 */
			case 6:
				mvAws = "r4.xlarge";
				break;
			case 8:
				mvAws = "p3.2xlarge";
				break;
			case 16:
				mvAws = "m5.4xlarge";
				break;
			/*
			 * case 24: mvAws = "m5zn.6xlarge"; break;
			 */
			case 32:
				mvAws = "r5ad.8xlarge";
				break;
			case 36:
				mvAws = "d2.8xlarge";
				break;
			case 40:
				mvAws = "m4.10xlarge";
				break;
			case 48:
				mvAws = "r5n.12xlarge";
				break;
			case 64:
				mvAws = "p2.16xlarge";
				break;
			case 72:
				mvAws = "i3.metal";
				break;
			case 96:
				mvAws = "m5.24xlarge";
				break;
			default:
				System.out.println("N�o escolheu nenhuma MV");
				mvAws = "r4.xlarge";
				break;

			}

			if (escolhaDecisao == 3 && lineNumber >= 15) {
				mvAws = model.getSize();
				System.out.println("----------------" + model.getSize());
			}

			/*
			 * String str = " class Hash\n" + "  def slice(*keep_keys)\n" + "    h = {}\n" +
			 * "    keep_keys.each { |key| h[key] = fetch(key) if has_key?(key) }\n" +
			 * "    h\n" + "  end unless Hash.method_defined?(:slice)\n" +
			 * "  def except(*less_keys)\n" + "    slice(*keys - less_keys)\n" +
			 * "  end unless Hash.method_defined?(:except)\n" + "end\n" + "\n" + "\n" +
			 * "Vagrant.configure(\"2\") do |config|\n" + "  config.vm.box = \"dummy\"\n" +
			 * "\n" + "  config.vm.provider :aws do |aws, override|\n" +
			 * "    aws.access_key_id = \"KEY\"\n" +
			 * "    aws.secret_access_key = \"KEY\"\n"
			 * + "    aws.security_groups = ['aldo']\n" +
			 * "    aws.keypair_name = \"KEY\"\n" +
			 * "    aws.instance_type=\""+mvAws+"\"\n" +
			 * "    aws.ami = \"ami-049fe7573cec0ed0f\"\n" +
			 * "    aws.region = \"us-east-1\"\n" +
			 * "    override.ssh.username = \"ubuntu\"\n" +
			 * "    override.ssh.private_key_path = \"KEY.pem\"\n" + "  end\n"
			 * + "end";
			 */
			String box_version=  "1.0.0";
			if(box.equals("generic/ubuntu2110")) {
				 box_version =  "4.1.16";
			}
			else if(box.equals("krisjey/centos7.9-gui")) {
				 box_version =  "0.7.2";
			}
			else{
				 box_version =  "1.0.0";
			}

			String str = "Vagrant.configure(\"2\") do |config|\n" + "  config.vm.box = \"" + box + "\"\n"
					+ "  config.vm.box_version = \""+box_version+"\"\n"
					+ "  config.vm.synced_folder '.', '/vagrant', disabled: true \n" + "end";
			System.out.println(str);
			System.out.println("Quantidade de CPUs " + model.getCpuSelected());
			// usuarioMasCloud = "aldohenrique";
			try {

				System.out.println(usuarioMasCloud + "/" + "Vagrantfile");
				File myObj = new File(usuarioMasCloud + "/" + "Vagrantfile");
				if (myObj.createNewFile()) {
					System.out.println("File created: " + myObj.getName());
				} else {
					System.out.println("File already exists.");
				}
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}

			try (FileWriter writer = new FileWriter(usuarioMasCloud + "/" + "Vagrantfile");
					BufferedWriter bw = new BufferedWriter(writer)) {

				bw.write(str);

			} catch (IOException e) {
				System.err.format("IOException: %s%n", e);
			}

		}

		if (providerCloud == 4) {

			String mvAws = "Basic_A0";

			System.out.println("CPU SELECIONADO " + model.getCpuSelected());
			System.out.println("-- CPU SELECIONADO " + machine.getCpu());
			switch (model.getCpuSelected()) {
			case 1:
				mvAws = "Standard_DS1_v2";
				break;
			case 2:
				mvAws = "Standard_D2as_v4";
				break;
			/*
			 * case 4: mvAws = "m4.xlarge"; break;
			 */
			case 4:
				mvAws = "Standard_B4ms";
				break;
			/*
			 * case 6: mvAws = "m4.2xlarge"; break; case 6: mvAws = "Standard_PB6s"; break;
			 */
			case 6:
				mvAws = "Standard_A8m_v2";
				break;
			case 8:
				mvAws = "Standard_A8m_v2";
				break;
			case 16:
				mvAws = "Standard_D16as_v4";
				break;
			/*
			 * case 24: mvAws = "m5zn.6xlarge"; break;
			 */
			case 32:
				mvAws = "Standard_D32as_v5";
				break;
			case 36:
				mvAws = "Standard_FX36mds";
				break;
			case 40:
				mvAws = "Standard_ND40rs_v2";
				break;
			case 48:
				mvAws = "Standard_D48as_v5";
				break;
			case 64:
				mvAws = "Standard_D64as_v5";
				break;
			case 72:
				mvAws = "Standard_F72s_v2";
				break;
			case 96:
				mvAws = "Standard_D96s_v5";
				break;
			default:
				System.out.println("N�o escolheu nenhuma MV");
				mvAws = "Standard_A8m_v2";
				break;

			}

			if (escolhaDecisao == 3 && lineNumber >= 13) {
				mvAws = model.getSize();
				System.out.println("----------------" + model.getSize());
			} else {
				provisioning.setSize(mvAws);
			}

			String str = " Vagrant.configure('2') do |config|\n" + "   config.vm.box = \"azure\"\n" + "\n"
					+ "  # use local ssh key to connect to remote vagrant box\n"
					+ "  config.ssh.private_key_path = '~/.ssh/KEY.pem'\n"
					+ "  config.vm.provider :azure do |az, override|\n" + "\n"
					+ "    # each of the below values will default to use the env vars named as below if not specified explicitly\n"
					+ "    az.tenant_id = 'KEY'\n"
					+ "    az.client_id = 'KEY'\n"
					+ "    az.client_secret = 'KEY'\n"
					+ "    az.subscription_id = 'KEY'\n"
					+ "    #az.location = 'southcentralus'\n" + "    #az.vm_image = 'masaopenmp'\n"
					+ "    #az.vm_name = 'aztest2'\n" + "    az.vm_size = '" + mvAws + "'\n"
					+ "    az.vm_password = 'masCloud'\n" + "    az.admin_username = \"mascloud\"\n"
					+ "    az.vm_storage_account_type = \"Standard_LRS\"\n"
					+ "    #az.resource_group_name = \"KEY\"\n" + 
					"    az.location = 'eastus'\n"
					+ "    az.vm_vhd_uri = 'KEY.vhd'\n"
					+ "    az.vm_operating_system = 'Linux'\n" + 
					"    az.wait_for_destroy = true\n" + "   end\n" + "\n"
					+ "end\n" + "";
			System.out.println(str);
			System.out.println("Quantidade de CPUs " + model.getCpuSelected());
			// usuarioMasCloud = "aldohenrique";
			try {

				System.out.println(usuarioMasCloud + "/" + "Vagrantfile");
				File myObj = new File(usuarioMasCloud + "/" + "Vagrantfile");
				if (myObj.createNewFile()) {
					System.out.println("File created: " + myObj.getName());
				} else {
					System.out.println("File already exists.");
				}
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}

			try (FileWriter writer = new FileWriter(usuarioMasCloud + "/" + "Vagrantfile");
					BufferedWriter bw = new BufferedWriter(writer)) {

				bw.write(str);

			} catch (IOException e) {
				System.err.format("IOException: %s%n", e);
			}

		}

		try {
			OutputStream rsyncStdIn;
			if (providerCloud == 2) {
				String[] command1 = { vagrant2, vagrant3, "printf 'Y\n' | gcloud compute instances describe instancenew"
						+ model.getCpuSelected() + " --zone us-central1-c " };
				pb = new ProcessBuilder(command1);
				pr = pb.start();
				System.out.println("Created VM");
				rsyncStdIn = pr.getOutputStream();
				rsyncStdIn.write("aldoaldo".getBytes());
				pr.waitFor();
				reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
				String split = "[ ]{2,}natIP: ";
				String[] lines = null;
				while ((line = reader.readLine()) != null) {
					lines = line.split("\n");
					lines = lines[0].split(split);
					if (lines.length > 1) {
						machine.setIp(lines[1]);
						break;
					}
				}
			}
			if (providerCloud == 1 || providerCloud == 3 || providerCloud == 4) {

				System.out.println("cd " + usuarioMasCloud + " && " + vagrant + " box add " + box);
				String[] command1 = { vagrant2, vagrant3,
						"cd " + usuarioMasCloud + " && " + vagrant + " box add " + box };
				pb = new ProcessBuilder(command1);
				pr = pb.start();

				pr.waitFor();
				reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));

				while ((line = reader.readLine()) != null) {
					System.out.println(line);
				}

				System.out.println("cd " + usuarioMasCloud + " && " + vagrant + " up");
				String[] command2 = { vagrant2, vagrant3, "cd " + usuarioMasCloud + " && " + vagrant + " up" };

				pb = new ProcessBuilder(command2);
				pb.redirectErrorStream(true);
				Process p2 = pb.start();
				BufferedReader reader2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
				String line2 = "";
				while ((line2 = reader2.readLine()) != null) {
					System.out.println(line2);
					if(line2.contains("Waiting for Communicator to become available")) {

						Thread.sleep(120000);
						try {
							p2.wait(120000);
						}
						catch(Exception e) {
							
						}
						try {
							p2.destroyForcibly();
						}
						catch(Exception e) {
							
						}
						//timeout - kill the process. 
						try {
							p2.destroy(); // consider using destroyForcibly instead
						}
						catch(Exception e) {
							
						}
						try {
							break;
						}
						catch(Exception e) {
							
						}
						
					}
				}
				System.out.println("create MV");
				
				//p2.waitFor();
				Thread.sleep(5000);

			}

			if (providerCloud == 2) {
				String[] command2 = { vagrant2, vagrant3, "gcloud compute instances instancenew"
						+ model.getCpuSelected() + " --zone us-central1-c --command=\"echo 'np' >executando\"" };
				pb = new ProcessBuilder(command2);
				System.out.println("gcloud compute ssh instancenew" + model.getCpuSelected()
						+ " --zone us-central1-c command \"echo 'executando' >executando\"");
				pr = pb.start();
				rsyncStdIn = pr.getOutputStream();
				rsyncStdIn.write("aldoaldo".getBytes());
				rsyncStdIn = pr.getOutputStream();
				rsyncStdIn.write("aldoaldo".getBytes());
				pr.waitFor();
			}
			if (providerCloud == 1 || providerCloud == 3 || providerCloud == 4) {
				String[] command2 = { vagrant2, vagrant3,
						"cd " + usuarioMasCloud + " && " + vagrant + " ssh -c \"echo 'np' >executando\"" };
				pb = new ProcessBuilder(command2);
				System.out.println("gcloud compute ssh instancenew" + model.getCpuSelected()
						+ " --zone us-central1-c command \"echo 'executando' >executando\"");
				pr = pb.start();
				rsyncStdIn = pr.getOutputStream();
				rsyncStdIn.write("aldoaldo".getBytes());
				rsyncStdIn = pr.getOutputStream();
				rsyncStdIn.write("aldoaldo".getBytes());
				pr.waitFor();
			}

		} catch (NumberFormatException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Process p = null;
		if (providerCloud == 2) {
			try {
				String[] command2 = { vagrant2, vagrant3, "gcloud compute ssh instancenew" + model.getCpuSelected()
						+ " --zone us-central1-c --command='nproc'" };

				pb = new ProcessBuilder(command2);
				pr = pb.start();
				p = pb.start();
				OutputStream rsyncStdIn = pr.getOutputStream();
				rsyncStdIn.write("aldoaldo".getBytes());
				pr.waitFor();
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (providerCloud == 1 || providerCloud == 3 || providerCloud == 4) {
			try {
				String[] command2 = { vagrant2, vagrant3,
						"cd " + usuarioMasCloud + " && " + vagrant + " ssh -c 'nproc'" };

				pb = new ProcessBuilder(command2);
				pr = pb.start();
				p = pb.start();

				reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				line = "";
				System.out.println("CPU nproc");
				machine.setCpu(0);
				try {
					while ((line = reader.readLine()) != null) {
						System.out.println("CPU nproc" + line);
						machine.setCpu(Integer.parseInt(line));
					}
				} catch (NumberFormatException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pr.waitFor();
				try {

					String[] command3 = { vagrant2, vagrant3, "cd " + usuarioMasCloud + " && " + vagrant
							+ " ssh -c 'cat /proc/meminfo | grep MemTotal' " };
					System.out.println(
							"cd " + usuarioMasCloud + " " + vagrant + " ssh -c 'cat /proc/meminfo | grep MemTotal'");
					// System.out.println(command);
					pb = new ProcessBuilder(command3);
					p = pb.start();

					reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
					line = "";

					Pattern pat = Pattern.compile("[0-9]+");
					while ((line = reader.readLine()) != null) {

						Matcher valores = pat.matcher(line);
						while (valores.find()) {
							machine.setMemory(Float.parseFloat(valores.group().toString()));
							System.out.println("MV com " + machine.getMemory() + " de memória");
						}
					}

					pr.waitFor();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		line = "";

		try {
			while ((line = reader.readLine()) != null) {
				System.out.println("CPU nproc" + line);
				machine.setCpu(Integer.parseInt(line));
			}
			pr.waitFor();
		} catch (NumberFormatException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		model.getCpu();

		new Thread(new Runnable() {
			public void run() {

				try {

					geral = platform.createNewAgent("StarterAgent", "agents.StarterAgent", null);
					geral.start();

				} catch (ControllerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				synchronized (geral) {
					try {
						System.out.println("Waiting");
						geral.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}).start();

		/*
		 * Process pp;
		 * 
		 * int alternative = model.getCpuSelected(); try { arqFalha = new
		 * FileWriter(usuarioMasCloud + "/" + "falha.csv", true); } catch (IOException
		 * e1) { // TODO Auto-generated catch block e1.printStackTrace(); }
		 * gravarArqFalha = new PrintWriter(arqFalha); //gravarArqFalha.append(", " +
		 * ii); gravarArqFalha.close(); verificationBase();
		 * 
		 * provisioning.setCpuUsedSelected(0); provisioning.setMemoryUSEDSelected(0);
		 * model.setTimeSelected(Double.MAX_VALUE);
		 * ModelsMonitoringForRules.setTotalSteps(365); valuesCpus = new
		 * ArrayList<ModelsProvisioning>(); model = new ModelsProvisioning(0, 0, 0, 0,
		 * 0); monitoringRules = new ModelsMonitoringForRules(0);
		 * System.out.println("\n\n Nova quantidade de Agentes " +
		 * transformationAgentQty + " \n\n"); System.out.println(alternative +
		 * " valores " + model.getCpuSelected());
		 * 
		 * if (alternative != model.getCpuSelected() //|| ii + 1 ==
		 * Integer.parseInt(args[0]) )
		 * 
		 * try {
		 * 
		 * if (providerCloud == 2) { String[] command1 = { vagrant2, vagrant3,
		 * "echo 'Y\n' | gcloud compute instances delete instancenew" + alternative +
		 * " --zone 'us-central1-c'" };
		 * System.out.println("echo 'Y\n' | gcloud compute instances delete" +
		 * "instancenew" + model.getCpuSelected() + " --zone 'us-central1-c'); "); pb =
		 * new ProcessBuilder(command1); }
		 * 
		 * if (providerCloud == 3 || providerCloud == 4) { String[] command1 = {
		 * vagrant2, vagrant3, "echo 'Y\n' " + vagrant + " halt" };
		 * System.out.println("echo 'Y\n' " + vagrant + " halt "); pb = new
		 * ProcessBuilder(command1); }
		 * 
		 * pp = pb.start(); System.out.println("Excluindo MV"); OutputStream rsyncStdIn
		 * = pr.getOutputStream(); rsyncStdIn.write("aldoaldo".getBytes()); reader = new
		 * BufferedReader(new InputStreamReader(pp.getInputStream())); line = "";
		 * 
		 * try { while ((line = reader.readLine()) != null) { System.out.println(line);
		 * } } catch (NumberFormatException | IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 * 
		 * pp.waitFor(); } catch (IOException | InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 * 
		 * //}
		 * 
		 * tempoModel = System.currentTimeMillis() - tempoModel; FileWriter arq;
		 * PrintWriter gravarArq = null; try { arq = new FileWriter(usuarioMasCloud +
		 * "/" + "statsTempo.csv", true); gravarArq = new PrintWriter(arq); } catch
		 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 * gravarArq.append(tempoModel + "\n"); gravarArq.close();
		 * 
		 * try { arqFalha = new FileWriter(usuarioMasCloud + "/" + "falha.csv", true); }
		 * catch (IOException e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); }
		 * 
		 * stopPlatform(); System.out.println(command);
		 * System.out.println("------------------- Final -----------------------");
		 * gravarArqFalha = new PrintWriter(arqFalha); gravarArqFalha.append(", right");
		 * gravarArqFalha.close(); System.exit(0);
		 */
	}

	public static void create() {

		// get a JADE runtime
		jade.core.Runtime rt = jade.core.Runtime.instance();

		// p.setParameter(Profile.MAIN_HOST, "localhost");
		int port = 10000;
		int controler = 1;

		ServerSocket socket = null;
		try {
			Random gerador = new Random();
			socket = new ServerSocket(0);
			port = socket.getLocalPort() + (gerador.nextInt(25) + 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// while (controler == 1) {
		ProfileImpl p = new ProfileImpl();
		p.setParameter(Profile.MAIN_PORT, port + "");
		// p.setParameter(Profile.MAIN_HOST, "localhost");
		// p.setParameter(Profile.GUI, "false");

		p.setParameter(Profile.CONTAINER_NAME, "Main-Container" + port);
		try {
			rt.shutDown();

			ContainerController cc = jade.core.Runtime.instance().createMainContainer(p);
			System.out.println("-Main-Container" + port);

			System.out.println(jade.core.Runtime.instance());
			// ContainerController mainContainer = rt.createMainContainer(p);

			platform = cc.getPlatformController();

			controler = 1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		// }

	}

	public static void stopPlatform() {
		System.out.println("stop");

		try {
			// platform.notifyAll();
			platform.kill();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void stopMonitoring() {

		System.out.println("2");
		FileWriter arq;
		PrintWriter gravarArq = null;
		synchronized (VMManager.platform) {

			try {
				System.out.println("Monitoramento " + "" + VMManager.usuarioMasCloud + "/" + "base.csv");
				arq = new FileWriter("" + VMManager.usuarioMasCloud + "/" + "base.csv", true);
				gravarArq = new PrintWriter(arq);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MonitoringAgents.tempoInicial = (System.currentTimeMillis() - MonitoringAgents.tempoInicial);

			System.out.println(DadosMonitorados.AVGCPUidl + "," + DadosMonitorados.AVGCPU + ","
					+ VMManager.model.getCpuSelected() + "," + VMManager.transformationAgentQty + ","
					+ DadosMonitorados.getAVGMemory() + "," + VMManager.machine.getMemory() + ","
					+ DadosMonitorados.AVGsendN + "," + DadosMonitorados.AVGrecvN + "," + DadosMonitorados.AVGreadD
					+ "," + DadosMonitorados.AVGwritD + "," + (MonitoringAgents.tempoInicial) + ","
					+ provisioning.getBestBalance() + "," + VMManager.machine.getCpu() + "\n");

			// System.out.println(DadosMonitorados.AVGMemory + ", " +
			// DadosMonitorados.AVGCPU + ", "
			// + VMManager.machine.getMemory() + ", " + VMManager.machine.getCpu() + "\n");

			if (DadosMonitorados.AVGMemory > 0 && DadosMonitorados.AVGCPU > 0 && VMManager.machine.getMemory() > 0
					&& VMManager.machine.getCpu() > 0) {
				gravarArq.append(DadosMonitorados.AVGCPUidl + "," + DadosMonitorados.AVGCPU + ","
						+ VMManager.machine.getCpu() + "," + VMManager.transformationAgentQty + ","
						+ DadosMonitorados.getAVGMemory() + "," + VMManager.machine.getMemory() + ","
						+ DadosMonitorados.AVGsendN + "," + DadosMonitorados.AVGrecvN + "," + DadosMonitorados.AVGreadD
						+ "," + DadosMonitorados.AVGwritD + "," + (MonitoringAgents.tempoInicial) + ","
						+ provisioning.getBestBalance() + "," + provisioning.getSize() + ","
						+ provisioning.getPriceSelected() + "\n");
			}
			gravarArq.close();
			System.out.println("StopMonitoring");
			try {
				VMManager.platform.notifyAll();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		try {
			// VMManager.stopPlatform();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void killVM() {
		System.out.println("cd " + usuarioMasCloud + " && " + vagrant + " destroy -f && rm -Rf .vagrant");
		String[] command2 = { vagrant2, vagrant3, "cd " + usuarioMasCloud + " && " + vagrant + " destroy -f && rm -Rf .vagrant" };

		ProcessBuilder pb = new ProcessBuilder();

		pb = new ProcessBuilder(command2);
		pb.redirectErrorStream(true);
		Process p2;
		try {
			p2 = pb.start();

			BufferedReader reader2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
			String line2 = "";
			while ((line2 = reader2.readLine()) != null) {
				System.out.println(line2);
			}
			System.out.println("delete MV");
			p2.waitFor();
			Thread.sleep(5000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void suspendVM() {
		System.out.println("cd " + usuarioMasCloud + " && " + vagrant + " suspend");
		String[] command2 = { vagrant2, vagrant3, "cd " + usuarioMasCloud + " && " + vagrant + " suspend" };

		ProcessBuilder pb = new ProcessBuilder();

		pb = new ProcessBuilder(command2);
		pb.redirectErrorStream(true);
		Process p2;
		try {
			p2 = pb.start();

			BufferedReader reader2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
			String line2 = "";
			while ((line2 = reader2.readLine()) != null) {
				System.out.println(line2);
			}
			System.out.println("delete MV");
			p2.waitFor();
			Thread.sleep(5000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void haltVM() {
		System.out.println("cd " + usuarioMasCloud + " && " + vagrant + " halt");
		String[] command2 = { vagrant2, vagrant3, "cd " + usuarioMasCloud + " && " + vagrant + " halt" };

		ProcessBuilder pb = new ProcessBuilder();

		pb = new ProcessBuilder(command2);
		pb.redirectErrorStream(true);
		Process p2;
		try {
			p2 = pb.start();

			BufferedReader reader2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
			String line2 = "";
			while ((line2 = reader2.readLine()) != null) {
				System.out.println(line2);
			}
			System.out.println("delete MV");
			p2.waitFor();
			Thread.sleep(5000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void verificationBase() {
		System.out.println("Verification");
		if (machine.getCpu() > 0) {
			try {
				Scanner file;
				PrintWriter writer;

				try {

					file = new Scanner(new File(usuarioMasCloud + "/" + "base.csv"));
					writer = new PrintWriter(usuarioMasCloud + "/" + "auxbase.csv");

					while (file.hasNext()) {
						String line = file.nextLine();
						if (!line.isEmpty()) {
							writer.write(line);
							writer.write("\n");
						}
					}

					file.close();
					writer.close();

				} catch (FileNotFoundException ex) {
					// Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
				}

				Process pr;
				ProcessBuilder pb = null;
				/*
				 * String[] command = { vagrant2,
				 * vagrant3,"cp base.csv /home/"+usuario+"/auxbase.csv"}; ProcessBuilder pb =
				 * new ProcessBuilder(command); pr = pb.start(); pr.waitFor();
				 */
				double r2TimeAux = provisioning.getR2Time();
				double r2CPUaux = provisioning.getR2CPU();

				FileWriter arq = null;
				PrintWriter gravarArq = null;
				try {
					arq = new FileWriter(usuarioMasCloud + "/" + "auxbase.csv", true);
					gravarArq = new PrintWriter(arq);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				gravarArq.append(DadosMonitorados.AVGCPUidl + "," + transformationAgentQty + "," + machine.getCpu()
						+ "," + DadosMonitorados.AVGCPU + "," + MonitoringAgents.tempoInicial + ","
						+ DadosMonitorados.AVGMemory + "," + timeVariable + "," + cpuUsageVariable + "," + priceVariable
						+ ",\"" + command + "\"\n");
				arq.close();
				gravarArq.close();
				System.out.println(DadosMonitorados.AVGCPUidl + "," + transformationAgentQty + "," + machine.getCpu()
						+ "," + DadosMonitorados.AVGCPU + "," + MonitoringAgents.tempoInicial + ","
						+ DadosMonitorados.AVGMemory + "," + timeVariable + "," + cpuUsageVariable + "," + priceVariable
						+ ",\"" + command + "\"\n");

				System.out.println("R2 time atual " + r2TimeAux + " R2 atualizado " + provisioning.getR2Time());
				System.out.println("R2 CPU atual " + r2CPUaux + " R2 atualizado " + provisioning.getR2CPU());

				// if(r2TimeAux<=provisioning.getR2Time() &&
				// r2CPUaux<=provisioning.getR2CPU()){
				System.out.println("=========== MELHOROU A BASE ===========");

				FileWriter arq2 = null;
				PrintWriter gravarArq2 = null;
				System.out.println("Time estimado " + provisioning.getTimeSelected() + " " + tempoModel + "\n");
				System.out.println("CPU estimado " + model.getCpuUSED() + " " + DadosMonitorados.AVGCPU + "\n");
				System.out
						.println("Memory estimado " + model.getMemoryUSED() + " " + DadosMonitorados.AVGMemory + "\n");

				try {
					arq2 = new FileWriter(usuarioMasCloud + "/" + "baseRegression22.csv", true);
					gravarArq2 = new PrintWriter(arq2);

					gravarArq2.append("Time estimado " + model.getTimeSelected() + " " + tempoModel + "\n");
					gravarArq2.append("CPU estimado " + model.getCpuUSED() + " " + DadosMonitorados.AVGCPU + "\n");
					gravarArq2.append(
							"Memory estimado " + model.getMemoryUSED() + " " + DadosMonitorados.AVGMemory + "\n");

					arq2.close();
					gravarArq2.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// String[] command1 = { vagrant2, vagrant3,"cp /home/"+usuario+"/auxbase.csv
				// base.csv"};
				// ProcessBuilder pb = new ProcessBuilder(command1);
				// pr = pb.start();
				// pr.waitFor();
				// }

				try {

					file = new Scanner(new File(usuarioMasCloud + "/" + "auxbase.csv"));
					writer = new PrintWriter(usuarioMasCloud + "/" + "base.csv");

					while (file.hasNext()) {
						String line = file.nextLine();
						if (!line.isEmpty()) {
							writer.write(line);
							writer.write("\n");
						}
					}

					file.close();
					writer.close();

				} catch (FileNotFoundException ex) {
					// Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
				}

				String[] command2 = { vagrant2, vagrant3, "rm " + usuarioMasCloud + "/auxbase.csv" };
				pb = new ProcessBuilder(command2);
				pr = pb.start();
				pr.waitFor();

			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
