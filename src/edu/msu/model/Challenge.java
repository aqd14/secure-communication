package edu.msu.model;

public class Challenge {

	private byte[] challenge;
	
	/**
	 * 
	 */
	public Challenge() {
		
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
		return bd.toString();
	}

}
