import java.io.IOException;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

class GameServer{
	public GameServer(int port, String token){
		GameThread gThread = new GameThread(port, token);
		Thread gServer = new Thread(gThread);
		gServer.start();
		System.out.println("Game started at port " + port);
	}
}
class GameThread implements Runnable{
	private int port;
	private String token;
	public GameThread(int port, String token){
		this.port = port;
		this.token = token;
	}
	public void run(){
		try{
			ServerSocket server = new ServerSocket(port);
			Game game = new Game(token);

			// Waiting for player 1
			Socket player1 = server.accept();
			System.out.println("Connection accepted from " + player1.getInetAddress() + ":" + player1.getPort());
			DataOutputStream outToPlayer1 = new DataOutputStream(player1.getOutputStream());
			outToPlayer1.writeBytes(game.getDisplay() + "\n");

			// Waiting for player 2
			Socket player2 = server.accept();
			System.out.println("Connection accepted from " + player2.getInetAddress() + ":" + player2.getPort());
			DataOutputStream outToPlayer2 = new DataOutputStream(player2.getOutputStream());
			outToPlayer2.writeBytes(game.getDisplay() + "\n");

			System.out.println("Game to begin on port " + port);
			while(true){
				// code for game server
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}