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
	private String player1;
	private String player2;
	private static final char RED = 'r';
	private static final char BLUE = 'b';
	private int playerPort1;
	private int playerPort2;
	public GameThread(int port, String token){
		this.serverPort = port;
		this.token = token;
	}
	public void run(){
		try{
			DatagramSocket server = new DatagramSocket(serverPort);
			File newFile = new File("data/game_"+serverPort+".txt");
			PrintWriter p = new PrintWriter(new BufferedWriter(new FileWriter(newFile)));
			p.close();
			DatagramPacket sendPacket;
			DatagramPacket receivePacket;
			String gameBoard;
			byte sendData[] = new byte[1024];
			byte receiveData[];
			Game game = new Game(token);

			// Waiting for player 1
			
			receiveData = new byte[1024];
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			server.receive(receivePacket);
			player1 = new String(receivePacket.getData());
			player1 = player1.trim();
			System.out.println(player1 + " connected to " + serverPort);

			receiveData = new byte[1024];
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			server.receive(receivePacket);
			playerPort1 =Integer.parseInt(new String(receivePacket.getData()).trim());

			gameBoard = game.getDisplay();
			sendData = gameBoard.getBytes(); 
			sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(player1), playerPort1);
			server.send(sendPacket);
			System.out.println("Game board sent to " + player1);

			// Waiting for player 2

			receiveData = new byte[1024];
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			server.receive(receivePacket);
			player2 = new String(receivePacket.getData());
			player2 = player2.trim();
			System.out.println(player2 + " connected to " + serverPort);

			receiveData = new byte[1024];
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			server.receive(receivePacket);
			playerPort2 =Integer.parseInt(new String(receivePacket.getData()).trim());

			gameBoard = game.getDisplay();
			sendData = gameBoard.getBytes(); 
			sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(player2), playerPort2);
			server.send(sendPacket);
			System.out.println("Game board sent to " + player2);
			

			System.out.println("Game to begin on port " + serverPort);
			String pos[] = null;
			int x = -1;
			int y = -1;
			String chance = player1;
			while(!game.isGameOver()){
				// code for game server

				System.out.println("Chance : " + chance);
				receiveData = new byte[1024];
				receivePacket = new DatagramPacket(receiveData, receiveData.length);
				server.receive(receivePacket);
				String ip = receivePacket.getAddress().toString().substring(1);
				System.out.println("Packet received from : " + ip);
				if(ip.compareTo(chance) == 0){
					char color = '.';
					if(chance.compareTo(player1) == 0)
						color = RED;
					else
						color = BLUE;

					String positions = new String(receivePacket.getData());
					String ar[] = positions.split(",");

					x = Integer.parseInt(ar[0].trim());
					y = Integer.parseInt(ar[1].trim());
					System.out.println("i,j : " + x+","+y);
					game.move(x, y, color);

					gameBoard = game.getDisplay();
					p = new PrintWriter(new BufferedWriter(new FileWriter(newFile)));
					p.println(gameBoard);
					p.close();
					/*sendData = gameBoard.getBytes(); 
					sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(player1), playerPort1);
					server.send(sendPacket);
					System.out.println("Board sent back to " + player1 +":"+playerPort1);
					sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(player2), playerPort2);
					server.send(sendPacket);
					System.out.println("Board sent back to " + player2 +":"+playerPort2);*/

					if(chance.compareTo(player1) == 0)
						chance = player2;
					else
						chance = player1;

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