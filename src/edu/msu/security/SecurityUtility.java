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
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import edu.msu.model.KeyType;

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
	public static Key readKeyFromFile(KeyType type, String keyFileName) throws IOException {
		InputStream in = new FileInputStream(keyFileName);
		ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));
		try {
			BigInteger m = (BigInteger) oin.readObject();
			BigInteger e = (BigInteger) oin.readObject();
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			Key key;
			if (type == KeyType.PUBLIC)
				key = fact.generatePublic(keySpec);
			else {
				key = fact.generatePrivate(keySpec);
			}
			return key;
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
	public static byte[] encodeRSA(PublicKey key, byte[] data) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, key);
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
	public static byte[] decodeRSA(PrivateKey key, byte[] data) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] plainData = cipher.doFinal(data);
			return plainData;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Hash a given string
	 * @param algorithm
	 * @param data
	 * @return
	 */
	public static byte[] digest(String algorithm, byte[] data) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance(algorithm);
			byte[] hash = digest.digest(data);
			return hash;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Encrypt a plain text using CBC mode
	 * @param IV
	 * @param plainText
	 * @param t
	 * @return
	 */
	public static byte[][] encryptCBCMode(byte[] IV, byte[][] plainText, TEA t) {
		// The number of byte in each block
		int blockLength = 8;
		// 100 bytes plain text should have 13 blocks
		int numOfBlocks = plainText.length;
		// Block cipher to store result
		byte[][] blockCipher = new byte[numOfBlocks][blockLength];
		// Encrypt with CBC mode
		byte[] xorResult = null;
		for (int block = 0; block < numOfBlocks; block ++) {
			if (block == 0) {
				xorResult = xorByteArrays(IV, plainText[0]);
			} else {
				xorResult = xorByteArrays(blockCipher[block-1], plainText[block]);
			}
			blockCipher[block] = t.encrypt(xorResult);
		}
		return blockCipher;
	}
	
	/**
	 * Decrypt a block cipher by using CBC mode
	 * @param IV
	 * @param blockCipher
	 * @param t
	 * @return
	 */
	public static byte[][] decryptCBCMode(byte[] IV, byte[][] blockCipher, TEA t) {
		// The number of byte in each block
		int blockLength = 8;
		// 100 bytes plain text should have 13 blocks
		int numOfBlocks = blockCipher.length;
		// Block cipher to store result
		byte[][] blockPlaintext = new byte[numOfBlocks][blockLength];
		// Encrypt with CBC mode
		byte[] decryptedText = null;
		for (int block = 0; block < numOfBlocks; block ++) {
			decryptedText = t.decrypt(blockCipher[block]);
			if (block == 0) {
				blockPlaintext[0] = xorByteArrays(IV, decryptedText);
			} else {
				blockPlaintext[block] = xorByteArrays(blockCipher[block-1], decryptedText);
			}
		}
		return blockPlaintext;
	}
	
	private static byte[] xorByteArrays(byte[] first, byte[] second) {
		if (first.length != second.length) {
			System.err.println("Lengths of inputs are not equal!");
			return null;
		}
		int length = first.length;
		byte[] result = new byte[length];
		int i = 0;
		for (byte b : first) {
			result[i] = (byte) (b ^ second[i]);
			i ++;
		}
		return result;
	}
	
	public static byte[][] convert1dTo2dArray(byte[] data) {
		int length = data.length;
		int subArrays = data.length/8;
		if (length % 8 != 0) {
			subArrays++;
		}
		byte new2dByteArray[][] = new byte[subArrays][8];
		for (int i = 0; i < subArrays; i++) {
			int copyLength;
			if (i == subArrays - 1) {
				copyLength = length % 8;
			} else {
				copyLength = 8;
			}
			System.arraycopy(data, 8*i, new2dByteArray[i], 0, copyLength);
		}
		return new2dByteArray;
	}
	
	public static byte[] convert2dTo1dArray(byte[][] data) {
		int length = data.length;
		byte[] new1dByteArray = new byte[length*8];
		int counter = 0;
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < data[i].length; j ++) {
				new1dByteArray[counter++] = data[i][j]; 
			}
		}
		return new1dByteArray;
	} 
	
	public static byte[] addPadding(byte[] data) {
		if (data == null) {
			return null;
		}
		
		if (data.length % 8 == 0) {
			return data;
		}
		
		int newLength = data.length;
		if (data.length % 8 != 0) {
			newLength = newLength + (8 - (data.length % 8));
		}
		byte[] paddedData = Arrays.copyOf(data, newLength);
		return paddedData;
	}
}
