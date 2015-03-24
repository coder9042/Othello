import java.io.IOException;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.ServerSocket;


class GameServer{
	public GameServer(int port, String token){
		GameThread gThread = new GameThread(port, token);
		Thread gServer = new Thread(gThread);
		gServer.start();
		System.out.println("Game started at port " + port);
	}
}
class GameThread implements Runnable{
	private int serverPort;
	private String token;
	private static final char RED = 'r';
	private static final char BLUE = 'b';
	private int player1Addr;
	private int player2Addr;
	public GameThread(int port, String token){
		this.serverPort = port;
		this.token = token;
	}
	public void run(){
		try{
			ServerSocket socket = new ServerSocket(serverPort);

			Game game = new Game(token);

			File newFile = new File("data/game_"+serverPort+".txt");
			PrintWriter p = new PrintWriter(new BufferedWriter(new FileWriter(newFile)));
			System.out.println(p);
			p.println(game.getDisplay());
			p.close();

			Socket player1 = socket.accept();
			player1Addr = player1.getPort();
			System.out.println("Connection accepted from " + player1.getPort());
			DataOutputStream outToPlayer1 = new DataOutputStream(player1.getOutputStream());
			outToPlayer1.writeBytes(game.getDisplay() + "\n");

			Socket player2 = socket.accept();
			player2Addr = player2.getPort();
			System.out.println("Connection accepted from " + player2.getPort());
			DataOutputStream outToPlayer2 = new DataOutputStream(player2.getOutputStream());
			outToPlayer2.writeBytes(game.getDisplay() + "\n");

			socket.close();
			player1.close();
			player2.close();
			

			System.out.println("Game to begin on port " + serverPort);


			DatagramSocket server = new DatagramSocket(serverPort);

			DatagramPacket sendPacket;
			DatagramPacket receivePacket;
			String gameBoard;
			byte receiveData[];
			String pos[] = null;
			int x = -1;
			int y = -1;
			int chance = 1;
			while(!game.isGameOver()){
				// code for game server

				System.out.println("Chance : " + chance);
				receiveData = new byte[1024];
				receivePacket = new DatagramPacket(receiveData, receiveData.length);
				server.receive(receivePacket);

				String value = new String(receivePacket.getData());
				System.out.println("Received: " + value);
				String ar[] = value.split(",");

				int turn = Integer.parseInt(ar[0].trim());
				x = Integer.parseInt(ar[1].trim());
				y = Integer.parseInt(ar[2].trim());
				System.out.println("Packet received from : " + turn);

				if(turn == chance){
					char color = '.';
					if(chance == 1)
						color = RED;
					else
						color = BLUE;
					
					System.out.println("i,j : " + x+","+y);
					game.move(x, y, color);

					gameBoard = game.getDisplay();
					game.display();
					PrintWriter p2 = new PrintWriter(new BufferedWriter(new FileWriter(new File("data/game_"+serverPort+".txt"))));
					p2.println(gameBoard);
					p2.close();
					/*sendData = gameBoard.getBytes(); 
					sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(player1), playerPort1);
					server.send(sendPacket);
					System.out.println("Board sent back to " + player1 +":"+playerPort1);
					sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(player2), playerPort2);
					server.send(sendPacket);
					System.out.println("Board sent back to " + player2 +":"+playerPort2);*/

					if(chance == 1)
						chance = 2;
					else
						chance = 1;

					game.display();
				}
				
			}
			System.out.println("Game over on port: " + serverPort);
			server.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}