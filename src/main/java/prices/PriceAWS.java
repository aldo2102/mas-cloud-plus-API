package prices;

import agents.VMManager;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import bean.ModelPricesAWS;

public class PriceAWS {

	public static void prices() {

		//String jsonString = callURL("https://a0.awsstatic.com/pricing/1/deprecated/ec2/pricing-reserved-instances.json");
	    //https://a0.awsstatic.com/pricing/1/ec2/linux-od.min.js
	    //https://aws.amazon.com/ec2/pricing/json/linux-od.json
	    //https://a0.awsstatic.com/pricing/1/deprecated/ec2/linux-od.json
	    //String jsonString = callURL("https://aws.amazon.com/ec2/pricing/json/linux-od.json");
		//String jsonString = callURL("linux-od.json");
		
		FileReader reader;
		try {
			reader = new FileReader("aws-linux-od.json");

			JSONTokener tokener = new JSONTokener(reader);
			JSONObject jsonString = new JSONObject(tokener);

			//System.out.println(jsonString.get("config"));
        
			JSONObject jsonArray = (JSONObject) jsonString.get("config");

			JSONObject row = (JSONObject) jsonString.get("config");
			//JSONArray row = row.getJSONObject("config");
			JSONArray jsonArray1 = row.getJSONArray("regions");
			
			JSONArray jsonArray2;
			JSONArray jsonArray3;
			JSONArray jsonArray4;
			
			for(int i=0;i<jsonArray1.length();i++) {
				
				jsonArray4 = jsonArray1.getJSONObject(i).getJSONArray("instanceTypes");
				for(int k=0;k<jsonArray4.length();k++) {

					jsonArray2 = jsonArray4.getJSONObject(k).getJSONArray("sizes");
					for(int j=0;j<jsonArray2.length();j++) {
						
				            jsonArray3 = jsonArray2.getJSONObject(j).getJSONArray("valueColumns");
				            jsonArray3 = new JSONArray("["+ jsonArray3.getJSONObject(0).get("prices").toString() +"]");
				            

				            try {  
				            	ModelPricesAWS item = new ModelPricesAWS();
								//item.setRegion(jsonArray.getJSONObject(i).get("region").toString());
								item.setSize(jsonArray2.getJSONObject(j).get("size").toString());
				                item.setPrice(Double.parseDouble(jsonArray3.getJSONObject(0).get("USD").toString()));
				                item.setvCPU(Double.parseDouble(jsonArray2.getJSONObject(j).get("vCPU").toString()));
				                item.setMemory(Double.parseDouble(jsonArray2.getJSONObject(j).get("memoryGiB").toString()));

				                VMManager.MV.add(item);
						
				            } catch(NumberFormatException e){  
				                System.out.println(jsonArray3.getJSONObject(0).get("USD").toString());
				              } 
					}
				}
			}



				//System.out.println(row);
				/* 
			System.out.println(row.getJSONObject("CP-COMPUTEENGINE-CUSTOM-VM-CORE").toString());
			System.out.println(row.getJSONObject("CP-COMPUTEENGINE-CUSTOM-VM-RAM").toString());

			JSONObject CPU=row.getJSONObject("CP-COMPUTEENGINE-CUSTOM-VM-CORE");
			JSONObject Mem=row.getJSONObject("CP-COMPUTEENGINE-CUSTOM-VM-RAM");


			Double europeCpu=Double.parseDouble(CPU.get("europe").toString());
			Double asiaCpu=Double.parseDouble(CPU.get("asia").toString());
			Double usCpu=Double.parseDouble(CPU.get("us").toString());
				 */



			} catch (JSONException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		public static String callURL(String myURL) {
			System.out.println("Requested URL:" + myURL);
			StringBuilder sb = new StringBuilder();
			URLConnection urlConn = null;
			InputStreamReader in = null;
			try {
				URL url = new URL(myURL);
				urlConn = url.openConnection();
				if (urlConn != null)
					urlConn.setReadTimeout(60 * 1000);
				if (urlConn != null && urlConn.getInputStream() != null) {
					in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
					BufferedReader bufferedReader = new BufferedReader(in);
					if (bufferedReader != null) {
						int cp;
						while ((cp = bufferedReader.read()) != -1) {
							sb.append((char) cp);
						}
						bufferedReader.close();
					}
				}
				in.close();
			} catch (Exception e) {
				throw new RuntimeException("Exception while calling URL:" + myURL, e);
			}

			return sb.toString();
		}

	}