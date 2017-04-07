package edu.msu.model;

import java.io.Serializable;
import java.util.Arrays;

public class Cipher implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte[] IV;
	private byte[][] cipherBlock;
	private byte[] key;
	
	public Cipher() {
		
	}
	
	public Cipher(byte[] IV, byte[][] cipherBlock, byte[] key) {
		this.IV = IV;
		this.cipherBlock = cipherBlock;
		this.key = key;
	}
	
	public byte[] getIV() {
		return IV;
	}
	
	public void setIV(byte[] iV) {
		IV = iV;
	}
	
	public byte[][] getCipherBlock() {
		return cipherBlock;
	}
	
	public void setCipherBlock(byte[][] cipherBlock) {
		this.cipherBlock = cipherBlock;
	}

	public byte[] getKey() {
		return key;
	}

	public void setKey(byte[] key) {
		this.key = key;
	}
	
	@Override
	public String toString() {
		StringBuilder bd = new StringBuilder("Cipher: \n");
		bd.append("IV: " + Arrays.toString(IV) + "\n");
		bd.append("Key: " + Arrays.toString(key) + "\n");
		bd.append("Cipher block: " + Arrays.toString(cipherBlock) + "\n");
		bd.append("\n");
		return bd.toString();
	}
}
