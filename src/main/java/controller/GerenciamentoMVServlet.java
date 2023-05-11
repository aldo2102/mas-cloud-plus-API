package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import agents.VMManager;
import bean.ModelPricesAWS;

/**
 * Servlet implementation class GerenciamentoMVServlet
 */
@WebServlet("/RequestVM")
@MultipartConfig(maxFileSize = 100 * 1024 * 1024)  // 100MB max
public class GerenciamentoMVServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GerenciamentoMVServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		

        HttpSession session = request.getSession(true);
        //set a string session attribute

        
        response.setContentType("text/html;charset=UTF-8");
        if(session.getAttribute("usuario")==null) {
        	request.getRequestDispatcher("view/instanciar/instanciar.jsp").forward(request, response);
        }
        else {
        	request.getRequestDispatcher("view/instanciar/terminal.jsp").forward(request, response);
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		
		
		int createDeleteVM = Integer.parseInt(request.getParameter("createDeleteVM"));
		if(createDeleteVM==0) {
				String command = request.getParameter("command");
		        int incremmet = Integer.parseInt(request.getParameter("incremmet"));
		        int providerCloud = Integer.parseInt(request.getParameter("providerCloud"));
		        float timeVariable = Float.parseFloat(request.getParameter("timeVariable"));
		        float cpuUsageVariable = Float.parseFloat(request.getParameter("cpuUsageVariable"));
		        float priceVariable = Float.parseFloat(request.getParameter("priceVariable"));
		        float SLATime = Float.parseFloat(request.getParameter("SLATime"));
		        float SLAResource = Float.parseFloat(request.getParameter("SLAResource"));
		        float SLAPrice = Float.parseFloat(request.getParameter("SLAPrice"));
		        String box = request.getParameter("box");
		        String usuarioMasCloud = request.getParameter("UserMasCloud");
		        int escolhaDecisao = Integer.parseInt(request.getParameter("choiceDecision"));
		        String userGoogle = request.getParameter("userGoogle");
		        String filename = null;
		        String adressFile = null;

		        String adressFilePrivate = null;
		        String adressFilePublic = null;
		        
		        String extension = "" ;
		        Part inputFile = request.getPart("keySecretJson");
			    if(inputFile != null) {
			    	try {
				        filename = Paths.get(inputFile.getSubmittedFileName()).getFileName().toString();
				        if(filename.toString().split("\\.").length==2) {
				        	extension = "."+filename.toString().split("\\.")[1];
				        }
				        else {
				        	extension = "";
				        }
				        adressFile = "/home/aldohenrique/wildfly-21.0.0.Final/bin/"+ usuarioMasCloud +"/" + usuarioMasCloud + extension;
				        System.out.println(adressFilePrivate);
				        File myObj = new File(adressFile); 
				        if (myObj.delete()) { 
				          System.out.println("Deleted the file: " + myObj.getName());
				        } else {
				          System.out.println("Failed to delete the file.");
				        } 
				        inputFile.write(adressFile);
			    	}
			        catch(Exception e) {
			        	
			        }
			        
			        try {
				        inputFile = request.getPart("keyPrivate");
				        filename = Paths.get(inputFile.getSubmittedFileName()).getFileName().toString();
				        if(filename.toString().split("\\.").length==2) {
				        	extension = "."+filename.toString().split("\\.")[1];
				        }
				        else {
				        	extension = "";
				        }
				        adressFilePrivate = "/home/aldohenrique/wildfly-21.0.0.Final/bin/"+ usuarioMasCloud +"/" + usuarioMasCloud + extension;
				        System.out.println(adressFilePrivate);
				        File myObj = new File(adressFilePrivate); 
				        if (myObj.delete()) { 
				          System.out.println("Deleted the file: " + myObj.getName());
				        } else {
				          System.out.println("Failed to delete the file.");
				        } 
				        inputFile.write(adressFilePrivate);
				        

				        final File file = new File(adressFilePrivate);
				        file.setReadable(true, false);
				        file.setExecutable(false, false);
				        file.setWritable(false, false);
				        
			        }
			        catch(Exception e) {
			        	
			        }
			        
			        try {
			        
				        inputFile = request.getPart("keyPublic");
				        filename = Paths.get(inputFile.getSubmittedFileName()).getFileName().toString();
				        if(filename.toString().split("\\.").length==2) {
				        	extension = "."+filename.toString().split("\\.")[1];
				        }
				        else {
				        	extension = "";
				        }
				        adressFilePublic = "/home/aldohenrique/wildfly-21.0.0.Final/bin/"+ usuarioMasCloud +"/" + usuarioMasCloud + extension;
				        System.out.println(adressFilePrivate);
				        File myObj = new File(adressFilePublic); 
				        if (myObj.delete()) { 
				          System.out.println("Deleted the file: " + myObj.getName());
				        } else {
				          System.out.println("Failed to delete the file.");
				        } 
				        inputFile.write(adressFilePublic);
				        final File file = new File(adressFilePrivate);
				        file.setReadable(true, false);
				        file.setExecutable(false, false);
				        file.setWritable(false, false);
				        
			        }
			        catch(Exception e) {
			        	
			        }
			        
			        
			        
		        }
		      
			    
			   
			    //Key Private<input type="file" name="keyPrivate"><br>
			    //Key Public<input type="file" name="keyPublic"><br>
			    //User <input type="text" name='userGoogle' value="vagrant">
		        
		        /*VMManager reste = new VMManager();
		        
		       */
		        
		        VMManager.starter(command,incremmet,providerCloud,timeVariable,cpuUsageVariable,priceVariable,box,usuarioMasCloud,escolhaDecisao,adressFile,adressFilePrivate,adressFilePublic,userGoogle);
		        
		      //Obtain the session object, create a new session if doesn't exist
		        HttpSession session = request.getSession(true);
		        //set a string session attribute
		        session.setAttribute("usuario", usuarioMasCloud);
		        
		        response.setContentType("text/html;charset=UTF-8");
		        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		        response.setHeader("Location", "index.jsp?page=3");
	        	RequestDispatcher view = request.getRequestDispatcher("index.jsp?page=3");
	        	view.forward(request, response);
		}
		else if(createDeleteVM==1) {

	           HttpSession session = request.getSession(true);
				VMManager.stopMonitoring();
				VMManager.killVM();
		        session.setAttribute("usuario", null);
				
		        response.setContentType("text/html;charset=UTF-8");

		        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		        response.setHeader("Location", "index.jsp?page=2");
	        	RequestDispatcher view = request.getRequestDispatcher("index.jsp?page=2");
	        	view.forward(request, response);
		}
		
		else if(createDeleteVM==2) {

	           HttpSession session = request.getSession(true);
				VMManager.stopMonitoring();
				VMManager.suspendVM();
		        session.setAttribute("usuario", null);
				
		        response.setContentType("text/html;charset=UTF-8");

		        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		        response.setHeader("Location", "index.jsp?page=2");
	        	RequestDispatcher view = request.getRequestDispatcher("index.jsp?page=2");
	        	view.forward(request, response);
		}
		else if(createDeleteVM==3) {

	           HttpSession session = request.getSession(true);
				VMManager.stopMonitoring();
				VMManager.haltVM();
		        session.setAttribute("usuario", null);
				
		        response.setContentType("text/html;charset=UTF-8");

		        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		        response.setHeader("Location", "index.jsp?page=2");
	        	RequestDispatcher view = request.getRequestDispatcher("index.jsp?page=2");
	        	view.forward(request, response);
		}
		
		
	}

}
