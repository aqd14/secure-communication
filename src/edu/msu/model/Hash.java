/**
 * 
 */
package edu.msu.model;

/**
 * @author doquocanh-macbook
 *
 */
public class Hash {
	
	private byte[] hash;

	/**
	 * 
	 */
	public Hash() {
		
	}

	/**
	 * @return the hash
	 */
	public byte[] getHash() {
		return hash;
	}

	/**
	 * @param hash the hash to set
	 */
	public void setHash(byte[] hash) {
		this.hash = hash;
	}
	
	@Override
	public String toString() {
		StringBuilder bd = new StringBuilder("Hash: ");
		for (int i = 0; i < hash.length; i ++) {
			bd.append(hash[i]).append(" ");
		}
		return bd.toString();
	}
}
