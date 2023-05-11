package controller;

import javax.servlet.http.HttpSession;

//import org.apache.log4j.Logger;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

@ServerEndpoint("/toUpper")
public class ToUpperWebsocket {

  //private static final Logger LOGGER = Logger.getLogger(ToUpperWebsocket.class);

  @OnOpen
  public void onOpen(Session session) {
	  
	  System.out.println(String.format("WebSocket opened: %s", session.getId()));
  }

  @OnMessage
  public void onMessage(String txt, Session session) throws IOException {
	System.out.println(String.format("WebSocket opened: %s", session.getId()));
	//for(int i=0;i<1000;i++)
		//session.getBasicRemote().sendText(txt.toUpperCase()+" "+i);

    //String user = (String) session.getAttribute("usuario");
	String command = (String) txt.split(",")[0];
	String user = (String) txt.split(",")[1];
	session.getBasicRemote().sendText(command+" "+user);
	System.out.println(txt);
	 try {
		  
	      
	      String vagrant3 = "-c";
	      String vagrant2 = "/bin/bash";
	      String vagrant = "/opt/vagrant/bin/vagrant";
	      
	      System.out.println("cd " + user + " && vagrant ssh -c '"+command+" && exit'");
	      
	      String[] command1 = { vagrant2, vagrant3, "cd " + user + " && vagrant ssh -c '"+command+" && exit'" };
	      ProcessBuilder pb = new ProcessBuilder(command1);

	        System.out.println(command);
	      Process pr = pb.start();
	      
	      //Process p = Runtime.getRuntime().exec("cd "+user+" && vagrant ssh");
	      BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
	      

			BufferedReader reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));

		    String line;
			while ((line = reader.readLine()) != null) {

		        String s = line;

		    	session.getBasicRemote().sendText(s);
		        System.out.println(s);
			}
	      


	      pr.waitFor();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

  }

  @OnClose
  public void onClose(CloseReason reason, Session session) {

		System.out.println(String.format("Closing a WebSocket (%s) due to %s", session.getId(), reason.getReasonPhrase()));
  }

  @OnError
  public void onError(Session session, Throwable t) {

	  System.out.println(String.format("Error in WebSocket session %s%n", session == null ? "null" : session.getId())+" "+ t);
  }
}