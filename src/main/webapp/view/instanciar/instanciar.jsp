
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<meta name="viewport"
	content="width=device-width,
  minimum-scale=1,initial-scale=1">
<%@ page contentType="text/html; charset=UTF-8"%>
<script src="https://code.jquery.com/jquery-3.3.1.min.js">
	
</script>

<script
	src="https://unpkg.com/jquery.terminal/js/jquery.terminal.min.js">
	
</script>

<link rel="stylesheet"
	href="https://unpkg.com/jquery.terminal/css/jquery.terminal.min.css" />


<style>
#pageloader {
	background: rgba(255, 255, 255, 0.8);
	display: none;
	height: 100%;
	position: fixed;
	width: 100%;
	z-index: 9999;
}

#pageloader img {
	left: 50%;
	margin-left: -32px;
	margin-top: -12px;
	top: 50%;
	width: 200px;
}
</style>


<br>
<br>
<br>
<script> var user=null </script>
<%@ page import="util.DBConnection" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<% 
Connection connection = DBConnection.createConnection();


connection = DBConnection.createConnection();
/*ArrayList listaDeUsuario = new ;

try {
    Statement stmt = connection.createStatement();
    
    ResultSet tabela = stmt.executeQuery("select * from users");
    
    while(tabela.next()){
//        User usuario = new User();
//        usuario.setId(tabela.getInt("id"));
//        usuario.setName(tabela.getString("name"));
//        usuario.setEmail(tabela.getString("email"));
//        usuario.setPassword(tabela.getString("password"));
//        usuario.setUser(tabela.getString("user"));
        
//        listaDeUsuario.add(usuario);
    }
    
} catch (SQLException ex) {
}*/


 HttpSession session2 = request.getSession();
if (session2.getAttribute("use") == null){
	response.setContentType("text/html;charset=UTF-8");
	response.sendRedirect("index.jsp?page=6");
	RequestDispatcher view = request.getRequestDispatcher("index.jsp?page=6");
	view.forward(request, response);
}
	%>

<section class="banner-section set-bg">
	<div class="container">
		<div class="banner-card">

			<div id="pageloader">

				<img src="img/cloud.gif" alt="processing..." width=100 />
				<h3>Processing...</h3>
			</div>
			

			<%
			 HttpSession session1 = request.getSession();
			String username = (String) session1.getAttribute("usuario");
			
			
			if (username == null || username == "") {
				
				out.println("<p style='margin-botton:30px'>Create VM</p>");
			} else {
			
				response.setContentType("text/html;charset=UTF-8");
				response.sendRedirect("index.jsp?page=3");
				RequestDispatcher view = request.getRequestDispatcher("index.jsp?page=3");
				view.forward(request, response);
			}
			%>
			<form action="GerenciamentoMVServlet" method="post" id="instanciar" enctype="multipart/form-data"
				onsubmit="carregar()">

				
					 <input type="hidden" name='command' value="Teste">
				
					 <input type="hidden" name='incremmet' value=3>
				<p class="provider">
					Provider 
							<select required id="providerCloud" name='providerCloud' required onchange="provedores()">
								<optgroup label="Public Cloud">
								   <option  selected="true" disabled="disabled">Select a provider</option>
								 	<option value=1>Google Cloud</option>
								 	<option>AWS</option>
								 	<option>Azure</option>
								</optgroup>
								<optgroup label="Private Cloud">
								 	<option value=3>Virtual Box</option>
								</optgroup>
							</select>
							<br>
							<div id="google" style="display:none">
							Key Secret (Json)<input type="file" name="keySecretJson"><br>
							Key Private<input type="file" name="keyPrivate"><br>
							Key Public<input type="file" name="keyPublic"><br>
							User Provider <input type="text" name='userGoogle' value="vagrant">
							</div>
				</p>
				
				<p class="provider">
					Provisioning choice model 
						<select required name='choiceDecision'  >
							<optgroup label="Manual Choices">
							  <option value="1">Heuristic</option>
							  <option value="2">Optimization</option>
							  <option value="3">Metaheuristic</option>
							</optgroup>  
							<optgroup label="Autonomous Choices">
							  <option value="4">Autonomous Choice</option>
							</optgroup>  
						</select>
					
				</p>
				
				<div class="provider">
					<p>
						Time Priority <input type="number" name='timeVariable' value=3>
					</p>
					<p>
						CPU Usage Priority <input type="number" name='cpuUsageVariable'
							value=3>
					</p>
					<p>
						Cost Priority <input type="number" name='priceVariable' value=3>
					</p>
				</div>
				<br>
				<p  class="provider">
					BOX 
					<select id="box" required  name='box' required >
						<option value="HenSchu/DebianNginxJava">Debian</option>
						<option value="generic/ubuntu2110">Ubuntu 21</option>
						<option value="krisjey/centos7.9-gui">Centos7.9</option>
					</select>
				</p>
				<p>
					<input type="hidden"  type="text" name='usuarioMasCloud' required value="<%= session1.getAttribute("use") %>">
				</p>
				<input type="hidden" name='createDeleteVM' value=0>

				<p>
					<input type="Submit" value="Send" onclick="carregar()">
				</p>
			</form>


			<script>
				function carregar() {
					document.getElementById("pageloader").style.display = "block";
				}
				
				function provedores(){
					provedor = document.getElementById("providerCloud").value
					if(provedor==1){
						 document.getElementById("google").style.display="block";
					}
					else{

						 document.getElementById("google").style.display="none";
					}
				}
			</script>

		</div>
	</div>

</section>
