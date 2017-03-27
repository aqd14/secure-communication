import java.io.IOException;

/**
 * 
 */

/**
 * @author aqd14
 *
 */
public class SecureCommunication {
	
	private static final int PORT = 61100;
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
	
	
	private static void startServer() {
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					Server server = new Server(PORT);
					server.establishConnection();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		t.run();
	}
	
	private static void startClient() {
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					Client client = new Client(HOST, PORT);
					client.startSender();
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		t.run();
	}
}
