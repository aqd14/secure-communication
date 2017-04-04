package edu.msu.communication;
import java.io.IOException;

/**
 * 
 */

/**
 * @author aqd14
 *
 */
public class SecureCommunication {
	
	private static final int PORT = 2017;
	private static final String HOST = "localhost";
	/**
	 * 
	 */
	public SecureCommunication() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		startServer();
		startClient();
	}
	
	public static void startServer() {
		(new Thread() {
			@Override
			public void run() {
				try {
					System.out.println("Creating server!");
					Server server = new Server(PORT);
					server.establishConnection();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public static void startClient() {
		System.out.println("Creating client!");
		(new Thread() {
			@Override
			public void run() {
				try {
					Client client = new Client(HOST, PORT);
					client.startSender();
				} catch (IOException | InterruptedException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
}
