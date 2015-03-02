<%@ page import="java.io.*, java.net.*" %>
<%!
    int type;
    String token;
    String submit;
    Socket socket;
    Socket gameSocket;
    int port;
    DataOutputStream outToServer;
    BufferedReader inFromServer;
    String gameBoard;
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
        </script>
    </head>
    <body>
        <% 
            try{
                submit = request.getParameter("submit");
                if(submit != null){
                    type = Integer.parseInt(request.getParameter("game_type"));
                    socket = new Socket("127.0.0.1", 6789);
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

                        gameSocket = new Socket("127.0.0.1", port);
                        inFromServer = new BufferedReader(new InputStreamReader(gameSocket.getInputStream()));
                        gameBoard = inFromServer.readLine();

                        final Socket fixedSocket = gameSocket;
                        Thread listeningThread = new Thread(new Runnable(){
                            public void run(){
                                try{
                                    BufferedReader inputStream = new BufferedReader(new InputStreamReader(fixedSocket.getInputStream()));
                                    String board = inputStream.readLine();
                                }
                                catch(IOException e){
                                    //
                                }
                            }
                        });
                        listeningThread.start();
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
        <div id="header">
            OTHELLO ONLINE
        </div>
        <div id="main">
            <%
                ind = 0;
                for(int i=0;i<8;i++){
                    for(int j=0;j<8;j++){
                        out.println(gameBoard.charAt(ind++));
                    }
                    %>
                        <br/>
                    <%
                }
            %>
        </div>
    </body>
</html>