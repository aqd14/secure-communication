/**
 * 
 */
package edu.msu.model;

import java.io.Serializable;

/**
 * @author doquocanh-macbook
 *
 */
public class Greeting implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
