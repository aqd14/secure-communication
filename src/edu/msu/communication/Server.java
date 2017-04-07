package edu.msu.communication;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

import edu.msu.model.Cipher;
import edu.msu.model.Greeting;
import edu.msu.model.KeyType;
import edu.msu.security.SecurityUtility;
import edu.msu.security.TEA;

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
	
	public void establishConnection() throws IOException, InvalidKeyException {
		System.out.println("Listening to client...");
		Socket connectionSocket = serverSocket.accept();
		System.out.println("Connected client: " + connectionSocket.getInetAddress().getHostName());
		
		// Instantiate I/O streams
		ObjectOutputStream out = new ObjectOutputStream(connectionSocket.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(connectionSocket.getInputStream());
		// Receive message from client
		try {
			Greeting fromClient = (Greeting) in.readObject();
			System.out.println("Message from client: " + fromClient.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        // Step 2. Server generate key pair and store in files
        SecurityUtility.generateKeyPair(PUBLIC_KEY_FILE, PRIVATE_KEY_FILE);
       
        // Step 3. Server send public modulus and public exponent to client
        PublicKey publicKey = (PublicKey)SecurityUtility.readKeyFromFile(KeyType.PUBLIC, PUBLIC_KEY_FILE);
        out.writeObject(publicKey);
        
        // Step 4.b Server decrypts and sends back number x to client
        
        // Step 11. Server decrypts the cipher text blocks to get P1,P2,..
        Cipher cipher = new Cipher(); // Get Cipher object from input stream
        byte[] encryptedKey = cipher.getKey();
        PrivateKey privateKey = (PrivateKey)SecurityUtility.readKeyFromFile(KeyType.PRIVATE, PRIVATE_KEY_FILE);
        byte[] decryptedKey = SecurityUtility.decodeRSA(privateKey, encryptedKey);
        TEA t = new TEA(decryptedKey);
        byte[][] plainText = SecurityUtility.decryptCBCMode(cipher.getIV(), cipher.getCipherBlock(), t);
        
        // Step 12. Server computes the hash of the decrypted message and sends the hash to client
        byte[] digest = SecurityUtility.digest("SHA-256", SecurityUtility.convert2dTo1dArray(plainText));
	}
}
