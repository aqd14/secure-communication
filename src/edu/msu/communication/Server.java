package edu.msu.communication;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;

import edu.msu.security.SecurityUtility;

/**
 * 
 */

/**
 * @author aqd14
 *
 */
public class Server {
	private ServerSocket serverSocket;
	
	final String PUBLIC_KEY_FILE = "keys" + File.separator + "public.key";
	final String PRIVATE_KEY_FILE = "keys" + File.separator + "private.key";
	
	/**
	 * @throws IOException 
	 * 
	 */
	public Server(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}
	
	public void establishConnection() throws IOException {
		System.out.println("Listening to client...");
		Socket connectionSocket = serverSocket.accept();
		System.out.println("Connected client: " + connectionSocket.getInetAddress().getHostName());
		BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		String line = null;
        while ((line = in.readLine()) != null) {
        	System.out.println(line);
        }
        // Server generate key pair and store in files
        SecurityUtility.generateKeyPair(PUBLIC_KEY_FILE, PRIVATE_KEY_FILE);
        // Server send public modulus and public exponent to client
        PublicKey publicKey = SecurityUtility.readKeyFromFile(PUBLIC_KEY_FILE);
        ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(connectionSocket.getOutputStream()));
        out.writeObject(publicKey);
        System.out.println("End of while!");
	}
}
