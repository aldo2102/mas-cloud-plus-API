package bean;

import java.util.ArrayList;

public class ModelsProvisioning {
	
	int cpu=0;
	double memory=0;
	double cpuAvg=0;
	double time=0;
	double cpuUSED=0;
	double memoryUSED=0;
	double memoryUSEDSelected=0;
	double cpuUsedSelected=0;
	int cpuSelected=0;
	double timeSelected=0;
	double cpuSum=0;
	double cpuMax=0;
	double cpuNoUsed=0;
	double  cpuNoUsedAvg=0;
	double value=Double.MAX_VALUE;
	double costbenefit =Double.MAX_VALUE;
	double R2Time =0;
	double R2CPU =0;
	double Balance;
	double bestBalance;
	double price=0;
	double priceSelected=0;
	String size;
	
	

	public double getMemory() {
		return memory;
	}

	public void setMemory(double memory) {
		this.memory = memory;
	}

	public double getMemoryUSEDSelected() {
		return memoryUSEDSelected;
	}

	public void setMemoryUSEDSelected(double memoryUSEDSelected) {
		this.memoryUSEDSelected = memoryUSEDSelected;
	}

	public double getMemoryUSED() {
		return memoryUSED;
	}

	public void setMemoryUSED(double memoryUSED) {
		this.memoryUSED = memoryUSED;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getPriceSelected() {
		return priceSelected;
	}

	public void setPriceSelected(double priceSelected) {
		this.priceSelected = priceSelected;
	}

	public double getBestBalance() {
		return bestBalance;
	}

	public void setBestBalance(double bestBalance) {
		this.bestBalance = bestBalance;
	}

	public double getBalance() {
		return Balance;
	}

	public void setBalance(double balance) {
		Balance = balance;
	}
	ArrayList<ModelsProvisioning> cpusCandidates=new ArrayList<ModelsProvisioning>();
	ArrayList<Double>timesCandidates=new ArrayList<Double>();
	
	public ModelsProvisioning(){}
	
	public double getR2Time() {
		return R2Time;
	}

	public void setR2Time(double r2Time) {
		R2Time = r2Time;
	}

	public double getR2CPU() {
		return R2CPU;
	}

	public void setR2CPU(double r2cpu) {
		R2CPU = r2cpu;
	}

	public ModelsProvisioning(int cpu, double time, double cpuUSED, int cpuSelected, double cpuNoUsed) {
		super();
		this.cpu = cpu;
		this.time = time;
		this.cpuUSED = cpuUSED;
		this.cpuSelected = cpuSelected;
		this.cpuNoUsed = cpuNoUsed;
	}
	public double getCostbenefit() {
		return costbenefit;
	}
	public void setCostbenefit(double costbenefit) {
		this.costbenefit = costbenefit;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	
	public int getTimesCandidates() {
		return timesCandidates.size();
	}
	public double getTimesCandidates(int i) {
		return timesCandidates.get(i);
	}
	public void setTimesCandidates(double timeSelected) {
		this.timesCandidates.add(timeSelected);
	}
	public int getCpusCandidatesSize() {
		return cpusCandidates.size();
	}
	public ArrayList<ModelsProvisioning> getCpusCandidates() {
		return cpusCandidates;
	} 
	public ModelsProvisioning getCpusCandidates(int i) {
		return cpusCandidates.get(i);
	}
	public void setCpusCandidates(ModelsProvisioning cpuSelected) {
		System.out.println(cpusCandidates.size());
		cpusCandidates.add(cpuSelected);
	}
	
	public int getCpu() {
		return this.cpu;
	}
	public void setCpu(int cpu) {
		this.cpu = cpu;
	}
	public double getCpuAvg() {
		return cpuAvg;
	}
	public void setCpuAvg(double cpuAvg) {
		this.cpuAvg = cpuAvg;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public double getCpuUSED() {
		return cpuUSED;
	}
	public void setCpuUSED(double cpuUSED) {
		this.cpuUSED = cpuUSED;
	}
	public double getCpuUsedSelected() {
		return this.cpuUsedSelected;
	}
	public void setCpuUsedSelected(double cpuUsedSelected) {
		this.cpuUsedSelected = cpuUsedSelected;
	}
	public int getCpuSelected() {
		return cpuSelected;
	}
	public void setCpuSelected(int cpuSelected) {
		this.cpuSelected = cpuSelected;
	}
	public double getTimeSelected() {
		return timeSelected;
	}
	public void setTimeSelected(double timeSelected) {
		this.timeSelected = timeSelected;
	}
	public double getCpuSum() {
		return cpuSum;
	}
	public void setCpuSum(double cpuSum) {
		this.cpuSum = cpuSum;
	}
	public double getCpuMax() {
		return cpuMax;
	}
	public void setCpuMax(double cpuMax) {
		this.cpuMax = cpuMax;
	}
	public double getCpuNoUsed() {
		return cpuNoUsed;
	}
	public void setCpuNoUsed(double cpuNoUsed) {
		this.cpuNoUsed = cpuNoUsed;
	}
	public double getCpuNoUsedAvg() {
		return cpuNoUsedAvg;
	}
	public void setCpuNoUsedAvg(double cpuNoUsedAvg) {
		this.cpuNoUsedAvg = cpuNoUsedAvg;
	}
	

	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	
	
	
}
