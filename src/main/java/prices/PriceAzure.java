/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prices;



import agents.VMManager;
import bean.ModelPricesAWS;
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

public class PriceAzure {

	public static void prices() {

		FileReader reader;
		try {
			reader = new FileReader("azureprice-export.json");

			JSONTokener tokener = new JSONTokener(reader);
			JSONObject jsonString = new JSONObject(tokener);

			System.out.println(jsonString.get("mv"));

			JSONArray rows = (JSONArray) jsonString.get("mv");

			for (int i = 0; i < rows.length(); i++) {
				System.out.println(rows.getJSONObject(i).get("name").toString());
				
				ModelPricesAWS item = new ModelPricesAWS();
				item.setSize(rows.getJSONObject(i).get("name").toString());
                item.setPrice(Double.parseDouble(rows.getJSONObject(i).get("linuxPrice").toString()));
                item.setvCPU(Double.parseDouble(rows.getJSONObject(i).get("numberOfCores").toString()));
                item.setMemory(Double.parseDouble(rows.getJSONObject(i).get("memoryInMB").toString())/1000);

                VMManager.MV.add(item);
				
			}
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