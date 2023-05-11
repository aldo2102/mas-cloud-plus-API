
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
 <meta name="viewport" content="width=device-width,
  minimum-scale=1,initial-scale=1">
<%@ page contentType="text/html; charset=UTF-8" %>
 <script src=
  "https://code.jquery.com/jquery-3.3.1.min.js">
 </script>

 <script src=
"https://unpkg.com/jquery.terminal/js/jquery.terminal.min.js">
 </script>

 <link rel="stylesheet" href=
"https://unpkg.com/jquery.terminal/css/jquery.terminal.min.css" />

 <style type="text/css">
  .terminal,span,.cmd,div {
   --color: rgba(0, 128, 0, 0.99);
  }

  .terminal,
  span {
   --size: 1.4;
  }
  
#pageloader
{
  background: rgba( 255, 255, 255, 0.8 );
  display: none;
  height: 100%;
  position: fixed;
  width: 100%;
  z-index: 9999;
}

#pageloader img
{
  left: 50%;
  margin-left: -32px;
  margin-top: -32px;
  position: absolute;
  top: 50%;
}
</style>
 
 <br>
 <br>
 <br>
 
<script> var user=null </script>

<% 

HttpSession session2 = request.getSession();
if (session2.getAttribute("use") == null){
	response.setContentType("text/html;charset=UTF-8");
	response.sendRedirect("index.jsp?page=6");
	RequestDispatcher view = request.getRequestDispatcher("index.jsp?page=6");
	view.forward(request, response);
}
	%>
	


<section class="banner-section set-bg" >
		<div class="container">
			<div class="banner-card">
			
					<div id="pageloader">
					   
					   <img src="img/cloud.gif" alt="processing..." width=100/>
					   <h3>Processing...</h3>
					</div>
			
				   <%
				        String username = (String)session.getAttribute("usuario");
				        if(username==null || username == "")  {
				
				        	response.setContentType("text/html;charset=UTF-8");
				        	response.sendRedirect("index.jsp?page=6");
				        	RequestDispatcher view = request.getRequestDispatcher("index.jsp?page=6");
				        	view.forward(request, response);
				        }
				        else{
				        	out.println("<p style='margin-botton:30px'>Hello "+username+"</p>");
				        	out.println("<script>user =  '"+username+"'</script>");
				        }
				    %> 
    
    				<form style="float:left;margin-top:30px" action="GerenciamentoMVServlet" method="post" id="suspender" onsubmit="carregar()">
    				
			            <input type="hidden" name='createDeleteVM' value=2>
				            <p><input type="submit" value="Suspend VM" onclick="carregar()"></p>
					</form>	
					
					
    				<form style="float:left;margin-top:30px" action="GerenciamentoMVServlet" method="post" id="halt" onsubmit="carregar()">
    				
			            <input type="hidden" name='createDeleteVM' value=3>
				            <p><input type="submit" value="Halt VM" onclick="carregar()"></p>
					</form>	
    
    				<form style="float:left;margin-top:30px"  action="GerenciamentoMVServlet" method="post" id="deletar" onsubmit="carregar()">
    				
			            <input type="hidden" name='createDeleteVM' value=1>
				            <p><input type="submit" value="Finish VM" onclick="carregar()"></p>
					</form>	
 
				 	<form style="clear:both"  id="ideal_form" method="post" onsubmit="return false">
				            <p>Command <input type="text" name='command' id="command" value="ls" /></p>
				            <div>
							    <input type="button" id="connectBtn" value="CONNECT" onclick="connect()" />
							    <input type="button" id="sendBtn" value="SEND" onclick="send()" disable="true" />
							    <input type="button" id="cancelBtn" value="Cancel" onclick="closeSocket()" disable="true" />
						    </div>
					</form>		
<div id="loading">Carregando </div>
  
   <script type="text/javascript">
   
    var webSocket;
    var output = document.getElementById("divID");

    var connectBtn = document.getElementById("connectBtn");
    var sendBtn = document.getElementById("sendBtn");
    var wsUrl = (location.protocol == "https:" ? "wss://" : "ws://") + location.hostname + (location.port ? ':'+location.port: '') + "/Web-MASCloud/toUpper";

	 document.getElementById("loading").style.display = "block";
	 
    function connect() {
      // open the connection if one does not exist
      if (webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED) {
        return;
      }

      updateOutput("Trying to establish a connection");

      // Create a websocket
      webSocket = new WebSocket(wsUrl);

      webSocket.onopen = function(event) {
        updateOutput("Connected!");
        connectBtn.disabled = true;
        sendBtn.disabled = false;
        output.innerHTML = ""
        output.innerHTML = localStorage.getItem('terminal')

		output.innerHTML = sessionStorage.getItem('terminal');
        
      };

      webSocket.onmessage = function(event) {

        document.getElementById("command").readOnly = true;
        updateOutput(event.data);
      };

      webSocket.onclose = function(event) {
        updateOutput("Connection Closed");
        connectBtn.disabled = false;
        sendBtn.disabled = true;
        document.getElementById("command").readOnly = true;
        document.getElementById("connectBtn").click();
        output.scrollTop = output.scrollHeight;
      };
    }

    document.getElementById("connectBtn").click();
    
    function send() {
      var text = []
      text.push(document.getElementById("command").value);

      document.getElementById("command").readOnly = true;
      text.push(user);
      webSocket.send(text);
    }

    function closeSocket() {
      webSocket.close();

    }

    function updateOutput(text) {
     document.getElementById("command").readOnly = true;
	 try {

		 let valor= document.getElementById("command").value;
		 
		 text2 = text.split(" ")
		 console.log(text2[text2.length-1]+" "+user);
		 output = document.getElementById("divID")
		 
		 if(text2[text2.length-1]==user){
			 text2[text2.length-1]="";
			 console.log("teste1")
			 result=""
			 for(let i=0;i<text2.length;i++){
				 result += text2[i]+" " 
			 }

			 console.log("teste2")
		      output.innerHTML += " > "+result+"&#13;";
		 }
		 else {
		      output.innerHTML += " "+text+"&#13;";
		 }

		 output.scrollTop = output.scrollHeight;
		 
		 document.getElementById("command").readOnly = false;
		 document.getElementById("loading").style.display = "none";
		 
		 localStorage.setItem('terminal',output.innerHTML);
		 
		 
		 sessionStorage.setItem('terminal',output.innerHTML);


		 
	}
	catch(err) {
	  //document.getElementById("divID").innerHTML = err.message;
	}


    }
    
 
 var commands = []
 var last = commands.length
 $('input').on('keydown', function(e) {


	 if(commands.length>0){
	  if(e.key=='ArrowUp' && last>=1){
		 if(last>1)
			  	last--;
		  console.log(last+" "+commands[last-1])
		  document.getElementById("command").value = commands[last-1]
	  }
	  else if(e.key=='ArrowDown' && last<commands.length){
		  last++;
		  console.log(last+" "+commands[last-1])
		  document.getElementById("command").value = commands[last-1]
	  }
	  
	 }
	 else if(e.key=='Enter'){
		    document.getElementById("sendBtn").click();
	  }

	  document.getElementById("command").focus();
	});

 /*
 var divID = document.getElementById("divID");
 $('#ideal_form').submit(function(e){
	 e.preventDefault(); // avoid to execute the actual submit of the form.
	 

	 console.log(document.getElementById("command").value," ", commands[commands.length-1])
	 if(document.getElementById("command").value != commands[commands.length-1]){
		 commands.push(document.getElementById("command").value);
	 }

	 last = commands.length
	 document.getElementById("command").readOnly = true;
	 let terminal= document.getElementById("divID").innerHTML;
	 let valor= document.getElementById("command").value;
	 document.getElementById("loading").style.display = "block";
	  //let estrategia = document.getElementById("strategy").value;
	  let form = $(this);
	  let url = 'commandsTerminal';
	  let post_data = form.serialize();
	  $('#divID', form).html('Please wait...');
	  $.ajax({
	      type: 'POST',
	      url: url, 
	      data: post_data,
	      success: function(response,textStatus,jqXHR){
			document.getElementById("divID").innerHTML = terminal+" "+valor+" > "+response+"&#13;";


			 document.getElementById("loading").style.display = "none";
			 var textarea = document.getElementById('divID');
			 textarea.scrollTop = textarea.scrollHeight;

			 document.getElementById("command").readOnly = false;
	      	},
	        error: function (error) {
	            alert('error; ' + eval(error));
	            document.getElementById("command").readOnly = false;
	        }
	      
	  	});

	  document.getElementById("command").focus();
    });
 
*/
 function carregar(){
	    document.getElementById("pageloader").style.display="block"
	  	  document.getElementById("command").focus();
	}
 
document.getElementById("loading").style.display = "none";

document.getElementById("command").focus();

 
 
 /*
  $('body').terminal({
   iam: function (name) {
    this.echo('Hello, ' + name +
     '. Welcome to GeeksForGeeks');
   },
   founder: function () {
    this.echo('Sandeep Jain');
   },
   help: function () {
    this.echo('iam - iam command and '
    + 'pass your name as argument\n'
    + 'founder to know the founder');
   },
  }, {
   greetings: 'GeeksForGeeks - A place to'
    + ' learn DS, Algo and Computer '
    + 'Science for free'
  });*/
 </script>
		</div>
	  </div>
		
    </section>
	<textarea readonly id="divID" rows="10" style="border:1px solid; width: 100%; height: 300px;overflow: scroll; z-index:99;background:black; color:white"></textarea>
	   
        
