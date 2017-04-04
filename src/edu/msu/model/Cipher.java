package edu.msu.model;

public class Cipher {
	private byte[] IV;
	private byte[] cipherBlock;
	private byte[] key;
	
	public Cipher() {
		
	}
	
	public Cipher(byte[] IV, byte[] cipherBlock, byte[] key) {
		this.IV = IV;
		this.cipherBlock = cipherBlock;
	}
	
	public byte[] getIV() {
		return IV;
	}
	
	public void setIV(byte[] iV) {
		IV = iV;
	}
	
	public byte[] getCipherBlock() {
		return cipherBlock;
	}
	
	public void setCipherBlock(byte[] cipherBlock) {
		this.cipherBlock = cipherBlock;
	}

	public byte[] getKey() {
		return key;
	}

	public void setKey(byte[] key) {
		this.key = key;
	}

}
