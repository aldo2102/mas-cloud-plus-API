package prices;

import agents.VMManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bean.ModelPricesAWS;


public class PriceVangrant {


  public static void prices() {
	  

		try {  

			VMManager.prices.setCpuPrice((double) 1);
			VMManager.prices.setMemoryPrice((double) 1);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}