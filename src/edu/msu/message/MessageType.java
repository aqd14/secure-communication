package edu.msu.message;
/**
 * 
 */

/**
 * @author doquocanh-macbook
 *
 */
public enum MessageType {
	HELLO, 				// Step 1
	PUBLIC_KEY,			// Step 3
	CHALLENGE,			// Step 4a
	RESPONSE,			// Step 4b
	CIPHER_BLOCK,		// Step 10
	HASH				// Step 11
}
