import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.HashMap;

class MainServer{
	private static String currToken = "A0";
	private static int currPort = 9000;
	private static HashMap<String, Integer> tokenSocketMap = new HashMap<String, Integer>();
	private static String getNextToken(){
		char part1 = currToken.charAt(0);
		int part2 = Integer.parseInt(currToken.substring(1));
		if(part2 == 99){
			part1++;
			return (part1 + "1");
		}
		else{
			part2++;
			return (part1 + String.valueOf(part2));
		}
	}
	public static void main(String[] args){
		try{
			ServerSocket server = new ServerSocket(6789);
			System.out.println("Main Server started...");
			while(true){
				try{
					Socket connectionSocket = server.accept();
					System.out.println("Connection accepted from " + connectionSocket.getInetAddress() + ":" + connectionSocket.getPort());
					BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
					DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
					int type = Integer.parseInt(inFromClient.readLine());
					System.out.println("Type: " + type);
					if(type == 1){
						currToken = getNextToken();
						tokenSocketMap.put(currToken, currPort);
						GameServer game = new GameServer(currPort, currToken);
						outToClient.writeBytes(String.valueOf(currPort) + "\n");
						currPort++;
						outToClient.writeBytes(currToken + "\n");
					}
					else{
						String token = inFromClient.readLine();
						if(token == null)
							outToClient.writeBytes("incorrect\n");
						else{
							Integer port = tokenSocketMap.get(token);
							if(port == null)
								outToClient.writeBytes("incorrect\n");
							else{
								outToClient.writeBytes(String.valueOf(port) + "\n");
								outToClient.writeBytes(token + "\n");
							}
						}
					}
					System.out.println(tokenSocketMap);
				}
				catch (Exception e) {
					//
				}
			}
		}
		catch (IOException e) {
			//
		}
	}
}