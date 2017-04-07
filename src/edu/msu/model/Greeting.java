/**
 * 
 */
package edu.msu.model;

/**
 * @author doquocanh-macbook
 *
 */
public class Greeting {
	private String message;
	/**
	 * 
	 */
	public Greeting(String sms) {
		this.setMessage(sms);
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param sms the sms to set
	 */
	public void setMessage(String sms) {
		this.message = sms;
	}

}
