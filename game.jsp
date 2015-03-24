<%@ page import="java.io.*, java.net.*" %>
<%!
    int type;
    String ipAddress;
    String token;
    String submit;
    Socket socket;
    Socket gameSocket;
    int turn;
    int port;
    DataOutputStream outToServer;
    BufferedReader inFromServer;
    String gameBoard="";
    String myAddress;
    String myPort;
    int ind;
%> 
<html>
    <head>
        <title>OTHELLO</title>
        <link rel="stylesheet" type="text/css" href="css/game.css" />
        <script type="text/javascript">
            function home () {
                window.location = "index.html";
            }
            function send(x, y, addr, port, turn){
                //alert(x+","+y+","+addr+","+port);
                var res = "";
                var xmlHttp = new XMLHttpRequest();
                //alert(turn);
                if(xmlHttp != null){
                    xmlHttp.open("GET", "sendData.jsp?row="+x+"&col="+y+"&addr="+addr+"&port="+port+"&turn="+turn, false);
                    xmlHttp.send(null);
                    res = xmlHttp.responseText;
                    //alert(res);
                }
            }

        </script>
    </head>
    <body>
        <% 
            try{
                submit = request.getParameter("submit");
                if(submit != null){
                    turn = Integer.parseInt(request.getParameter("firstmove"));
                    ipAddress = request.getParameter("server");
                    type = Integer.parseInt(request.getParameter("game_type"));
                    socket = new Socket(ipAddress, 6789);
                    outToServer = new DataOutputStream(socket.getOutputStream());
                    inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    if(type == 1){
                        outToServer.writeBytes("1\n");
                    }
                    else{
                        token = request.getParameter("token_num");
                        outToServer.writeBytes("2\n");
                        outToServer.writeBytes(token + "\n");
                    }
                    String reply = inFromServer.readLine();

                    if(reply.compareTo("incorrect") == 0){
                        %>
                            Incorrect token. <a href="index.html">Go back</a>.
                        <%
                    }
                    else{
                        port = Integer.parseInt(reply);
                        token = inFromServer.readLine();
                        socket.close();
                        //out.println(ipAddress+":"+port);

                        gameSocket = new Socket(ipAddress, port);
                        //out.println(gameSocket.getInetAddress().getLocalHost().toString());
                        inFromServer = new BufferedReader(new InputStreamReader(gameSocket.getInputStream()));
                        gameBoard = inFromServer.readLine();
                        
                        //gameSocket.close();
                        //gameBoard = "................................................................";
                        %>

                        <div id="header">
                            OTHELLO ONLINE
                        </div>
                        <div id="main">
                            <table align="center" borders="all" >
                            <%
                                ind = 0;
                                for(int i=0;i<8;i++){
                                    %>
                                    <tr>
                                    <%
                                    for(int j=0;j<8;j++){
                                        %>
                                        <td>
                                        <%
                                        if(gameBoard.charAt(ind) == '.'){
                                        %>
                                        <input type="button" value="" id="cell#<%= i+"_"+j %>" class="cell" onclick="send(<% out.println(print(i,j,ipAddress,port,turn)); %>);" />
                                        <%
                                        }
                                        else if(gameBoard.charAt(ind) == 'r'){
                                        %>
                                        <input type="button" id="cell#<%= i+"_"+j %>" class="red_cell" />
                                        <%
                                        }
                                        else if(gameBoard.charAt(ind) == 'b'){
                                        %>
                                        <input type="button" id="cell#<%= i+"_"+j %>" class="blue_cell" />
                                        <%
                                        }
                                        %>
                                        </td>
                                        <%
                                        ind++;
                                    }
                                    %>
                                    </tr>
                                    <%
                                }
                            %>
                        </table>
                        </div>

                        <%
                    }             
                }
                else{
                    %>
                        <script> home(); </script>
                    <%
                }
            }
            catch(ConnectException e){
                %>
                    Server down. Try again later.
                <%
            }
        %>
        <%!
            /*public void sendToServer(String p, String q){
                try{
					int x = Integer.parseInt(p.trim());
					int y = Integer.parseInt(q.trim());
                    outToServer = new DataOutputStream(gameSocket.getOutputStream());
                    outToServer.writeBytes(x+","+y+"\n");
                }
                catch(Exception e){
                    //
                }
            }*/
            public String print(int i, int j, String ipAddress, int port, int turn){
                return "'"+i+"','"+j+"','"+ipAddress+"','"+port+"','"+turn+"'";
            }
        %>
        <script>
        function update(){
            var xmlHttp = new XMLHttpRequest();
            var board;
            if(xmlHttp != null){
                var port = '<%=port %>';
                xmlHttp.open("GET", "data/game_"+port+".txt", false);
                xmlHttp.send(null);
                board = xmlHttp.responseText;
                //alert(board);
            }
            var ind = 0;
            for (var i = 0; i < 8; i++) {
                for(var j = 0; j< 8; j++){
                    var elem = document.getElementById("cell#"+i+"_"+j);
                    //alert(elem);
                    var curr = board.substring(ind, ind+1);
                    //alert(curr);
                    if(curr == "."){
                        elem.className = 'cell';
                    }
                    else if(board.charAt(ind) == 'r'){
                        //alert("red");
                        elem.className = 'red_cell';
                        elem.onclick = '';
                    }
                    else if(board.charAt(ind) == 'b'){
                        elem.className = 'blue_cell';
                        elem.onclick = '';
                    }
                    ind += 1;
                }
            }
        }
        

        window.setInterval(function(){
          update();
        }, 2000);


        </script>
    </body>
</html>