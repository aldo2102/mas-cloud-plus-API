package DAO;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author aldohenrique
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;
import util.DBConnection;

import java.sql.Statement;
public class UserDao {
    
    private Connection connection;
    
    public void addUser(User usuario){
        connection = DBConnection.createConnection();
        
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("insert into users(name,email,password,user) values (?, ?, ?, ? )");
        
            preparedStatement.setString(1, usuario.getName());
            preparedStatement.setString(2,usuario.getEmail());
            
            
            preparedStatement.setString(3,  usuario.getPassword());
            
            preparedStatement.setString(4, usuario.getUser());
            
            preparedStatement.executeUpdate();
            
            
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public void deleteUser(int userId){
        connection = DBConnection.createConnection();
        
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("delete from users where userid=?");
            
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
     public void updateUser(User user) {
        
        connection = DBConnection.createConnection();;
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("update users set name=?, email=?, password=?, user=?" +
                            "where id=?");
            // Parameters start with 1
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3,user.getPassword());
            preparedStatement.setString(4, user.getUser());
            preparedStatement.setInt(5, user.getId());
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
     
     public List<User> getAllUsers(){
        connection = DBConnection.createConnection();
        List<User> listaDeUsuario = new ArrayList<User>();
        
        try {
            Statement stmt = connection.createStatement();
            
            ResultSet tabela = stmt.executeQuery("select * from users");
            
            while(tabela.next()){
                User usuario = new User();
                usuario.setId(tabela.getInt("id"));
                usuario.setName(tabela.getString("name"));
                usuario.setEmail(tabela.getString("email"));
                usuario.setPassword(tabela.getString("password"));
                usuario.setUser(tabela.getString("user"));
                
                listaDeUsuario.add(usuario);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listaDeUsuario;
        
     }
     

     
     public User getLogin(String userL, String senha) {
         
         connection = DBConnection.createConnection();
         User user = new User();
         try {
        	 System.out.print("select * from users where user="+userL+" and password="+senha);
             PreparedStatement preparedStatement = connection
                     .prepareStatement("select * from users where user=? and password=?");
             preparedStatement.setString(1, userL);
             preparedStatement.setString(2, senha);
             ResultSet rs = preparedStatement.executeQuery();

             if (rs.next()) {
             	user.setId(rs.getInt("id"));
             	user.setName(rs.getString("name"));
                 user.setEmail(rs.getString("email"));
                 user.setPassword(rs.getString("password"));
                 user.setUser(rs.getString("user"));
             }
             else {
            	 user=null; 
             }

         } catch (SQLException e) {
             e.printStackTrace();
         }

         return user;
     }
     
   public User getUserById(int userId) {
        
        connection = DBConnection.createConnection();;
        User user = new User();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select * from users where userid=?");
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
            	user.setId(rs.getInt("id"));
            	user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setUser(rs.getString("user"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
     
     
     
     
     
     
     
    
    
}
