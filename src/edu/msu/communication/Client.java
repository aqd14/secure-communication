package edu.msu.communication;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PublicKey;

import edu.msu.security.SecurityUtility;

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
	
	public void startSender() throws IOException, InterruptedException, ClassNotFoundException {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        // Client says hello to server
    	out.write("Hello from client!\n");
    	out.flush();
    	
    	// Receive public key from server
    	ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
    	PublicKey publicKey;
    	System.out.println("Waiting for response from server");
//    	Thread.sleep(5000);
    	byte[] data = new BigInteger("39e858f86df9b909a8c87cb8d9ad599", 16).toByteArray();
    	while ((publicKey = (PublicKey) in.readObject()) != null) {
    		byte[] challenge = SecurityUtility.encodeRSA(publicKey, data);
    	}
    	
    	// Send challenge to server
    	
    	// Close socket
        socket.close();
	}
}
