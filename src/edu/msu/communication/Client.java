package edu.msu.communication;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.PublicKey;

import edu.msu.model.Cipher;
import edu.msu.model.Greeting;
import edu.msu.security.SecurityUtility;
import edu.msu.security.TEA;

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
	
	public void startSender() throws IOException, InterruptedException, ClassNotFoundException, InvalidKeyException {
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
//        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        // Step 1. Client says hello to server
		Greeting greeting = new Greeting("Greetings from client!");
		out.writeObject(greeting);
    	out.flush();
    	
    	// Step 4. Receive public key from server
    	PublicKey publicKey;
    	System.out.println("Waiting for response from server");
    	// Client send challenge to server
    	byte[] data = new BigInteger("39e858f86df9b909a8c87cb8d9ad599", 16).toByteArray();
    	while ((publicKey = (PublicKey) in.readObject()) != null) {
    		byte[] challenge = SecurityUtility.encodeRSA(publicKey, data);
    	}
    	// Wait for response from server
    	
    	// Step 5. Client chooses a string of 100 �a�s (�aaaaaaaaa�...a�) as plain text blocks
    	String plainText = generatePlainText();
    	
    	// Step 6. Client computes hash of plain text
    	byte[] hash = SecurityUtility.digest("SHA-256", plainText.getBytes(StandardCharsets.UTF_8));
    	
    	// Step 7. Client chooses random 128-bit key K
    	byte key[] = new BigInteger("39e858f86df9b909a8c87cb8d9ad599", 16).toByteArray();
    	
    	// Step 8. Client encrypts plain text string using CBC mode and TEA as block cipher. Choose IV of all zeros
    	TEA t = new TEA(key);
    	byte[] IV = new byte[8];
    	byte[][] plainData = SecurityUtility.convert1dTo2dArray(plainText.getBytes(StandardCharsets.UTF_8));
    	byte[][] cipher = SecurityUtility.encryptCBCMode(IV, plainData, t);
    	
    	// Step 9. Client encrypts key K using public key of server
    	byte[] encryptedKey = SecurityUtility.encodeRSA(publicKey, key);
    	
    	// Step 10. Client sends encrypted cipher text blocks IV,C1,C2,... K_U to server
    	Cipher cipherObj = new Cipher(IV, cipher, encryptedKey);
    	
    	// Step 13. Client closes the connection is the hash H is correct
        socket.close();
	}
	
	/**
	 * Generate a plain text contains 100 'a'
	 * @return
	 */
	private String generatePlainText() {
		StringBuilder bd = new StringBuilder();
		for (int i = 0; i < 100; i ++) {
			bd.append("a");
		}
		return bd.toString();
	}
}
