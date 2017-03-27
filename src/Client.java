import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 
 */

/**
 * @author aqd14
 *
 */
public class Client {
	private Socket socket;
	/**
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws InterruptedException 
	 * 
	 */
	public Client(String hostName, int port) throws UnknownHostException, IOException, InterruptedException {
        socket = new Socket(hostName, port);
	}
	
	public void startSender() throws IOException, InterruptedException {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        int count = 10;
        while (count-- > 0) {
        	out.write("Hello World!\n");
        	Thread.sleep(2000);
        }
        socket.close();
	}
}
