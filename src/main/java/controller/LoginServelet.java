/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DAO.UserDao;
import model.User;

/**
 *
 * @author aldohenrique
 */
@WebServlet(name = "LoginServelet", urlPatterns = {"/LoginServelet"})
public class LoginServelet extends HttpServlet {
    
    
    private static String INSERT_OR_EDIT = "/user.jsp";
    private static String LIST_USER = "?page=5";
    private UserDao dao;
    
    
    public LoginServelet() {
        super();
        dao = new UserDao();
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
    	HttpSession session = request.getSession();
    	session.invalidate();
    	RequestDispatcher view;
    	response.setContentType("text/html;charset=UTF-8");
		response.sendRedirect("index.jsp?page=6");
   	 view = request.getRequestDispatcher("?page=6");
   
   
   
   //request.setAttribute("usuarios", dao.getAllUsers());
   view.forward(request, response);
        
        
    }

    
    @Override

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User usuario = new User();
        
        

        String password = request.getParameter("password");
        
        try {
			password = Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA1").digest(password.getBytes("UTF-8")));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        usuario = dao.getLogin(request.getParameter("user"), password);
        RequestDispatcher view;
        System.out.println(usuario);
        if(usuario==null) {
        	response.setContentType("text/html;charset=UTF-8");
    		response.sendRedirect("index.jsp?page=6");
       	 	view = request.getRequestDispatcher("?page=6");
        	 view = request.getRequestDispatcher("?page=6");
        }
        else {
        	 HttpSession session = request.getSession();
        	 session.setAttribute("use", usuario.getName());

         	response.setContentType("text/html;charset=UTF-8");
     		response.sendRedirect("index.jsp?page=5");
        	 view = request.getRequestDispatcher("?page=5");
        }
        
        
        //request.setAttribute("usuarios", dao.getAllUsers());
        view.forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
