/**
 * 
 */
package edu.msu.model;

import java.io.Serializable;

/**
 * @author doquocanh-macbook
 *
 */
public class Hash implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte[] digest;

	/**
	 * 
	 */
	public Hash(byte[] digest) {
		this.digest = digest;
	}
	
	/**
	 * @return the digest
	 */
	public byte[] getDigest() {
		return digest;
	}

	/**
	 * @param digest the digest to set
	 */
	public void setDigest(byte[] digest) {
		this.digest = digest;
	}

	@Override
	public String toString() {
		StringBuilder bd = new StringBuilder("Digest: ");
		for (int i = 0; i < digest.length; i ++) {
			bd.append(digest[i]).append(" ");
		}
		bd.append("\n");
		return bd.toString();
	}
}
