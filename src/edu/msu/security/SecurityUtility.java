package edu.msu.security;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class SecurityUtility {

	public SecurityUtility() {
		// TODO Auto-generated constructor stub
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
				xorResult = Utils.xorByteArrays(IV, plainText[0]);
			} else {
				xorResult = Utils.xorByteArrays(blockCipher[block-1], plainText[block]);
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
				blockPlaintext[0] = Utils.xorByteArrays(IV, decryptedText);
			} else {
				blockPlaintext[block] = Utils.xorByteArrays(blockCipher[block-1], decryptedText);
			}
		}
		return blockPlaintext;
	}
}
