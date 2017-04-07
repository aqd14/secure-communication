package edu.msu.model;

import java.io.Serializable;

public class Challenge implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte[] challenge;
	
	/**
	 * 
	 */
	public Challenge(byte[] challenge) {
		this.challenge = challenge;
	}

	/**
	 * @return the challenge
	 */
	public byte[] getChallenge() {
		return challenge;
	}

	/**
	 * @param challenge the challenge to set
	 */
	public void setChallenge(byte[] challenge) {
		this.challenge = challenge;
	}
	
	@Override
	public String toString() {
		StringBuilder bd = new StringBuilder("Challenge: ");
		for (int i = 0; i < challenge.length; i ++) {
			bd.append(challenge[i]).append(" ");
		}
		bd.append("\n");
		return bd.toString();
	}

}
