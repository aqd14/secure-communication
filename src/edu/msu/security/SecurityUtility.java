package edu.msu.security;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class SecurityUtility {

	public SecurityUtility() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Read key from stored file
	 * 
	 * @param keyFileName
	 * @return
	 * @throws IOException
	 */
	public static PublicKey readKeyFromFile(String keyFileName) throws IOException {
		InputStream in = new FileInputStream(keyFileName);
		ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));
		try {
			BigInteger m = (BigInteger) oin.readObject();
			BigInteger e = (BigInteger) oin.readObject();
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PublicKey pubKey = fact.generatePublic(keySpec);
			return pubKey;
		} catch (Exception e) {
			throw new RuntimeException("Can't generate public key spec", e);
		} finally {
			oin.close();
		}
	}
	
	/**
	 * Generate key pair with RSA algorithm
	 */
	public static void generateKeyPair(String publicKeyFile, String privateKeyFile) {
		//Generate key pair
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048);
			KeyPair kp = kpg.genKeyPair();
			// Use KeyFactory to store public and private keys into files
			KeyFactory fact = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(), RSAPublicKeySpec.class);
			RSAPrivateKeySpec priv = fact.getKeySpec(kp.getPrivate(), RSAPrivateKeySpec.class);
			saveToFile(publicKeyFile, pub.getModulus(), pub.getPublicExponent());
			saveToFile(privateKeyFile, priv.getModulus(), priv.getPrivateExponent());
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Save modulus and exponent of a key to file
	 * 
	 * @param fileName
	 * @param mod Modulus
	 * @param exp Exponent
	 */
	private static void saveToFile(String fileName, BigInteger mod, BigInteger exp) {
		ObjectOutputStream oout = null;
		try {
			oout = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
			oout.writeObject(mod);
			oout.writeObject(exp);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (oout != null)
				try {
					oout.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	

	/**
	 * Generate to authenticate the connection to server
	 * @param pubKey
	 * @param data Cipher data represents for client's challenge
	 * @return byte array of cipher text
	 */
	public static byte[] encodeRSA(PublicKey pubKey, byte[] data) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			byte[] cipherData = cipher.doFinal(data);
			return cipherData;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Decode cipher text using RSA algorithm
	 * @param pubKey
	 * @param data
	 * @return byte array of plain text
	 */
	public static byte[] decodeRSA(PublicKey pubKey, byte[] data) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, pubKey);
			byte[] plainData = cipher.doFinal(data);
			return plainData;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public String generatePlainText() {
		StringBuilder bd = new StringBuilder();
		for (int i = 0; i < 100; i ++) {
			bd.append("a");
		}
		return bd.toString();
	}
	
	public static String encryptCBCMode(byte[] IV, byte[] plainText) {
		return null;
	}
	
	public static String decryptCBCMode(byte[] IV, byte[] cipherText) {
		return null;
	}
}
