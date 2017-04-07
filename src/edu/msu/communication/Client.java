package edu.msu.communication;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.PublicKey;
import java.util.Arrays;

import edu.msu.model.Challenge;
import edu.msu.model.Cipher;
import edu.msu.model.Greeting;
import edu.msu.model.Hash;
import edu.msu.model.Response;
import edu.msu.security.SecurityUtility;
import edu.msu.security.TEA;
import edu.msu.security.Utils;

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
		Greeting greeting = new Greeting("Greetings from client!\n");
		System.out.println("[Client Side][GREETINGS] - Sending greetings to server\n");
		out.writeObject(greeting);
    	out.flush();
    	System.out.println("[Client Side][GREETINGS] - Sent greetings to server\n");
    	
    	// Step 4. Receive public key from server
    	System.out.println("[Client Side][PUBLIC KEY] - Waiting for public key from server\n");
    	PublicKey publicKey = (PublicKey) in.readObject();
    	System.out.println("[Client Side][PUBLIC KEY] - Received public key from server: \n" + publicKey + "\n");
    	// Client send challenge to server
    	byte[] data = new BigInteger("39e858f86df9b909a8c87cb8d9ad599", 16).toByteArray();
    	System.out.println("[Client Side][CHALLENGE] - Plain challenge: " + Arrays.toString(data) + "\n");
    	byte[] challenge = SecurityUtility.encodeRSA(publicKey, data);
//		System.out.println("[Client Side] Challenge: " + new String(challenge) + "\n");
    	Challenge c = new Challenge(challenge);
    	System.out.println("[Client Side][CHALLENGE] - Sending challenge to server\n");
    	out.writeObject(c);
    	out.flush();
    	System.out.println("[Client Side][CHALLENGE] - " + c);
    	System.out.println("[Client Side][CHALLENGE] - Sent challenge to server\n");
    	
    	// Receive response from server and check validity
    	Response r = (Response) in.readObject();
     	System.out.println("[Client Side][RESPONSE] - Received response from server\n");
     	System.out.println("[Client Side][RESPONSE] - " + r);
    	if (Arrays.equals(data, r.getResponse())) {
    		System.out.println("[Client Side][RESPONSE] - Response from server is correct!\n");
    	} else {
    		System.err.println("[Client Side][RESPONSE] - Response from server is intcorrect! Close connection!\n");
    		socket.close();
    		return;
    	}
    	
    	// Step 5. Client chooses a string of 100 'a's as plain text blocks
    	byte[] plainText = generatePlainText().getBytes(StandardCharsets.UTF_8);
    	
    	// Step 6. Client computes hash of plain text
    	byte[] digest = SecurityUtility.digest("SHA-256", plainText);
    	
    	// Step 7. Client chooses random 128-bit key K
//    	String key = "39e858f86df9b909a8c87cb8d9ad599";
    	byte key[] = new BigInteger("39e858f86df9b909a8c87cb8d9ad599", 16).toByteArray();
    	
    	// Step 8. Client encrypts plain text string using CBC mode and TEA as block cipher. Choose IV of all zeros
    	TEA t = new TEA(key);
    	byte[] IV = new byte[8];
    	byte[][] plainTextBlock = Utils.convert1dTo2dArray(plainText);
    	byte[][] cipherBlock = SecurityUtility.encryptCBCMode(IV, plainTextBlock, t);
    	
    	// Step 9. Client encrypts key K using public key of server
    	byte[] encryptedKey = SecurityUtility.encodeRSA(publicKey, key);
    	
    	// Step 10. Client sends encrypted cipher text blocks IV,C1,C2,... K_U to server
    	Cipher cipherObj = new Cipher(IV, cipherBlock, encryptedKey);
    	System.out.println("[Client Side][CIPHER] - Sending cipher to server\n");
    	out.writeObject(cipherObj);
    	out.flush();
    	System.out.println("[Client Side][CIPHER] - Sent cipher to server\n");
    	
    	// Step 13. Client closes the connection is the hash H is correct
    	System.out.println("[Client Side][HASH] - Waiting for hash from server\n");
    	Hash h = (Hash) in.readObject();
    	System.out.println("[Client Side][HASH] - Received hash from server\n");
    	if (Arrays.equals(h.getDigest(), digest)) {
    		System.out.println("[Client Side][HASH] - Hashes are matched!\n");
    		System.out.println("[Client Side][HASH] - " + h + "\n");
    	} else {
    		System.err.println("[Client Side][HASH] - Hashes are not matched!\n");
    		System.out.println("[Client Side][HASH] - " + h + "\n");
    	}
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
