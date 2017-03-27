import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 */

/**
 * @author aqd14
 *
 */
public class Server {
	private ServerSocket serverSocket;
	/**
	 * @throws IOException 
	 * 
	 */
	public Server(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}
	
	public void establishConnection() throws IOException {
		Socket connectionSocket = serverSocket.accept();
		PrintWriter out = new PrintWriter(connectionSocket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		String line = null;
        while ((line = in.readLine()) != null) {
        	out.println(line);
        }
	}
}
