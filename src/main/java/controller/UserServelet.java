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

import DAO.UserDao;
import model.User;

/**
 *
 * @author aldohenrique
 */
@WebServlet(name = "UserServelet", urlPatterns = {"/UserServelet"})
public class UserServelet extends HttpServlet {
    
    
    private static String INSERT_OR_EDIT = "/user.jsp";
    private static String LIST_USER = "/listUser.jsp";
    private UserDao dao;
    
    
    public UserServelet() {
        super();
        dao = new UserDao();
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String arquivoTela = "";
        String acao = request.getParameter("acao");
        
        if(acao == null){
            acao = "listUser";
        }
        else if(acao.equals("deletar")){
            int userId = Integer.parseInt(request.getParameter("userId"));
            dao.deleteUser(userId);
            
            arquivoTela = LIST_USER;
            request.setAttribute("users", dao.getAllUsers()); 
        }
        else if(acao.equals("edit")){
            arquivoTela = INSERT_OR_EDIT;
            int userId = Integer.parseInt(request.getParameter("userId"));
            User user = dao.getUserById(userId);
            request.setAttribute("user", user);
        }
        else if(acao.equals("listUser")){
            arquivoTela = LIST_USER;
            request.setAttribute("users", dao.getAllUsers());
        }
        else {
            arquivoTela = INSERT_OR_EDIT;
        }
        
        RequestDispatcher view = request.getRequestDispatcher(arquivoTela);
        view.forward(request, response);
        
        
    }

    
    @Override

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User usuario = new User();
        usuario.setName(request.getParameter("name"));
        usuario.setUser(request.getParameter("user"));
        
        usuario.setEmail(request.getParameter("email"));
        String usuarioid = request.getParameter("usuarioid");

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

        usuario.setPassword(password);
        if(usuarioid == null || usuarioid.isEmpty())
        {
            dao.addUser(usuario);
        }
        else
        {
            usuario.setId(Integer.parseInt(usuarioid));
            dao.updateUser(usuario);
        }
        RequestDispatcher view = request.getRequestDispatcher(LIST_USER);
        request.setAttribute("usuarios", dao.getAllUsers());
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
