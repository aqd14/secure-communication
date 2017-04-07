package edu.msu.model;

import java.io.Serializable;

public class Response implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte[] response;
	
	public Response(byte[] response) {
		this.response = response;
	}

	/**
	 * @return the response
	 */
	public byte[] getResponse() {
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(byte[] response) {
		this.response = response;
	}
	
	@Override
	public String toString() {
		StringBuilder bd = new StringBuilder("Reponse: ");
		for (int i = 0; i < response.length; i ++) {
			bd.append(response[i]).append(" ");
		}
		bd.append("\n");
		return bd.toString();
	}
}
