package bean;

public class ModelPricesAWS implements java.lang.Comparable<ModelPricesAWS>  {
	private String region;
	private String size;
	private double price;
	private double vCPU;
	private double memory;
	private int provider;
	private double time;
	
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getvCPU() {
		return vCPU;
	}
	public void setvCPU(double vCPU) {
		this.vCPU = vCPU;
	}
	public double getMemory() {
		return memory;
	}
	public void setMemory(double memory) {
		this.memory = memory;
	}

	public int getProvider() {
		return provider;
	}
	public void setProvider(int provider) {
		this.provider = provider;
	}

	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	

	

	  public int compareTo(ModelPricesAWS prices){
	        if(this.price == prices.price){
	            return 0;
	        } else if(this.price < prices.price){
	            return -1;
	        } else{
	            return 1;
	        }
	    }
	
	
	
	
}
