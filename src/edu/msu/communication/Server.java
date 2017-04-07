package edu.msu.communication;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

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
public class Server {
	private ServerSocket serverSocket;
	// Keys pair
	PublicKey publicKey;
	PrivateKey privateKey;
	
	/**
	 * @throws IOException 
	 * 
	 */
	public Server(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}
	
	public void establishConnection() throws IOException, InvalidKeyException {
		System.out.println("[Server Side] - Listening to client...\n");
		Socket connectionSocket = serverSocket.accept();
		System.out.println("[Server Side] - Connected client: " + connectionSocket.getInetAddress().getHostName() + "\n");
		
		// Instantiate I/O streams
		ObjectOutputStream out = new ObjectOutputStream(connectionSocket.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(connectionSocket.getInputStream());
		// Receive message from client
		try {
			System.out.println("[Server Side][GREETINGS] - Wating for message from client\n");
			Greeting fromClient = (Greeting) in.readObject();
			System.out.println("[Server Side][GREETINGS] - Received message from client: " + fromClient.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        // Step 2. Server generate key pair
		System.out.println("[Server Side][KEY PAIR] - Generating key pair\n");
		generateKeyPair();
		System.out.println("[Server Side][KEY PAIR] - Finished generating key pair\n");
//        SecurityUtility.generateKeyPair(PUBLIC_KEY_FILE, PRIVATE_KEY_FILE);
       
        // Step 3. Server send public modulus and public exponent to client
		System.out.println("[Server Side][PUBLIC KEY] - Sending public key to client\n");
        out.writeObject(publicKey);
        out.flush();
        System.out.println("[Server Side][PUBLIC KEY] - Sent public key to client: \n" + publicKey + " \n");
        
		// Step 4.b Server decrypts and sends back number x to client
		// Get private key to decode
		
        try {
        	System.out.println("[Server Side][CHALLENGE] - Waiting for challenge from client\n");
			Challenge c = (Challenge) in.readObject();
			System.out.println("[Server Side][CHALLENGE] - Received challenge from client\n");
			byte[] challenge = c.getChallenge();
			byte[] response = SecurityUtility.decodeRSA(privateKey, challenge);
			System.out.println("[Server Side][RESPONSE] - Sending response to client\n");
			Response r = new Response(response);
			out.writeObject(r);
			out.flush();
			System.out.println("[Server Side][RESPONSE] - Sent response to client\n");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // Step 11. Server decrypts the cipher text blocks to get P1,P2,..
        Cipher cipher;
        byte[][] plainTextBlock = null;
		try {
			System.out.println("[Server Side][CIPHER] - Waiting for cipher from client\n");
			cipher = (Cipher) in.readObject();
			System.out.println("[Server Side][CIPHER] - Received cipher from client\n");
			System.out.println(cipher);
	        byte[] encryptedKey = cipher.getKey();
	        byte[] decryptedKey = SecurityUtility.decodeRSA(privateKey, encryptedKey);
	        TEA t = new TEA(decryptedKey);
	        plainTextBlock = SecurityUtility.decryptCBCMode(cipher.getIV(), cipher.getCipherBlock(), t);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // Step 12. Server computes the hash of the decrypted message and sends the hash to client
        byte[] plainText = Utils.convert2dTo1dArray(plainTextBlock);
        plainText = Utils.removePadding(plainText);
        System.out.println("[Server Side][DERYPTION] - Decrypted plain text: " + new String(plainText) + "\n");
        byte[] digest = SecurityUtility.digest("SHA-256", plainText);
        Hash h = new Hash(digest);
        System.out.println("[Server Side][HASH] - Sending hash to client\n");
        out.writeObject(h);
        out.flush();
        System.out.println("[Server Side][HASH] - Sent hash to client\n");
	}
	
	/**
	 * Generate public and private key with RSA algorithm
	 */
	private void generateKeyPair() {
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048);
			KeyPair kp = kpg.genKeyPair();
			publicKey = kp.getPublic();
			privateKey = kp.getPrivate();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
