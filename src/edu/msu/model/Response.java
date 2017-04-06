package edu.msu.model;

public class Response {
	
	private byte[] response;
	
	public Response() {
		// TODO Auto-generated constructor stub
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
		StringBuilder bd = new StringBuilder("Challenge: ");
		for (int i = 0; i < response.length; i ++) {
			bd.append(response[i]).append(" ");
		}
		return bd.toString();
	}
}
