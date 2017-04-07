//
//  TEA.java
//
//  Created by Thomas Dixon on 12/30/05.
//

package edu.msu.security;
import java.security.InvalidKeyException;

public class TEA {

	/**** Constants ****/
	private static final int
	    ROUNDS      = 32,   // iteration count (cycles)
	    BLOCK_SIZE  = 8,    // bytes in a data block (64 bits)
	    KEY_SIZE    = 16,   // key size (128 bits)
	    DELTA       = 0x9E3779B9,
	    D_SUM       = 0xC6EF3720;
	    
	/**** Instance vars ****/
	// Subkeys
	private static int[] S = new int[4];
	
	// False for encipher, true for decipher
	
	public TEA(byte[] key) throws InvalidKeyException {
		if (key == null ) {
            throw new InvalidKeyException("Null key");
        }
        
        if (key.length != KEY_SIZE) {
            throw new InvalidKeyException("Invalid key length (req. 16 bytes)");
        }
        generateSubKeys(key);
	}
	
	/**
	 * Encrypt one block of data with TEA algorithm
	 * 
	 * @param plainText
	 * @param offset
	 * @return cipher block
	 */
	public byte[] encrypt(byte[] plainText) {
		int offset = 0;
		int[] pack = pack(plainText, offset);
		int v0 = pack[0];
		int v1 = pack[1];
        int n = ROUNDS;
        // Encryption
        int sum = 0;
		while (n-- > 0) {
			sum += DELTA;
			v0  += ((v1 << 4 ) + S[0] ^ v1) + (sum ^ (v1 >>> 5)) + S[1];
			v1  += ((v0 << 4 ) + S[2] ^ v0) + (sum ^ (v0 >>> 5)) + S[3];
		}
		// Assign new values
		pack[0] = v0;
		pack[1] = v1;
		byte[] cipher = unpack(pack);
		return cipher;
	}
	
	/**
	 * Decrypt one block of data with TEA algorithm
	 * 
	 * @param cipherText
	 * @param offset
	 * @return plain text block
	 */
	public byte[] decrypt(byte[] cipherText) {
		int offset = 0;
		int[] pack = pack(cipherText, offset);
		int v0 = pack[0];
		int v1 = pack[1];
		int n = ROUNDS;
		int sum = D_SUM;
		while (n-- > 0) {
			v1  -= ((v0 << 4 ) + S[2] ^ v0) + (sum ^ (v0 >>> 5)) + S[3];
			v0  -= ((v1 << 4 ) + S[0] ^ v1) + (sum ^ (v1 >>> 5)) + S[1];
			sum -= DELTA;
		}
		pack[0] = v0;
		pack[1] = v1;
		byte[] plain = unpack(pack);
		return plain;
	}
	
	private byte[] unpack(int[] pack) {
		int v0 = pack[0];
		int v1 = pack[1];
        // Unpack and return
        int outOffset = 0;
        byte[] out = new byte[BLOCK_SIZE];
        out[outOffset++] = (byte)(v0       );
        out[outOffset++] = (byte)(v0 >>>  8);
        out[outOffset++] = (byte)(v0 >>> 16);
        out[outOffset++] = (byte)(v0 >>> 24);
        
        out[outOffset++] = (byte)(v1       );
        out[outOffset++] = (byte)(v1 >>>  8);
        out[outOffset++] = (byte)(v1 >>> 16);
        out[outOffset++] = (byte)(v1 >>> 24);
        return out;
	}
	
	private int[] pack(byte[] in, int inOffset) {
        // Pack bytes into integers
        int v0 = ((in[inOffset++] & 0xFF)      ) |
            ((in[inOffset++] & 0xFF) <<  8) |
            ((in[inOffset++] & 0xFF) << 16) |
            ((in[inOffset++]       ) << 24);
        int v1 = ((in[inOffset++] & 0xFF)      ) |
            ((in[inOffset++] & 0xFF) <<  8) |
            ((in[inOffset++] & 0xFF) << 16) |
            ((in[inOffset++]       ) << 24);
        
        int[] pack = {v0, v1};
        return pack;
	}
    
    // Subkey generator
    public static void generateSubKeys(byte[] key) {
        for(int off=0, i=0; i<4; i++) {
            S[i] = ((key[off++]&0xFF)      ) |
            ((key[off++]&0xFF) <<  8) |
            ((key[off++]&0xFF) << 16) |
            ((key[off++]&0xFF) << 24);
		}
	}
}


///**
// * Implementation of the Tiny Encryption Algorithm (TEA).
// * The Tiny Encryption Algorithm is one of the fastest and most efficient
// * cryptographic algorithms in existence. It was developed by David Wheeler and
// * Roger Needham at the Computer Laboratory of Cambridge University.
// *
// * See http://www.cl.cam.ac.uk/ftp/users/djw3/tea.ps
// *
// * This software was written to provide simple encryption for J2ME.
// * The homepage for this software is http://winterwell.com/software/TEA.php
// *
// * (c) 2008 Joe Halliwell <joe.halliwell@gmail.com>
// *
// * This program is free software: you can redistribute it and/or modify it
// * under the terms of the GNU Lesser General Public License as published by the
// * Free Software Foundation, either version 3 of the License, or (at your
// * option) any later version.
// *
// * This program is distributed in the hope that it will be useful, but WITHOUT
// * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
// * for more details.
// *
// * You should have received a copy of the GNU Lesser General Public License
// * along with this program. If not, see <http://www.gnu.org/licenses/>.
// */
//
//package edu.msu.security;
//
//public class TEA {
//	private final static int SUGAR = 0x9E3779B9;
//	private final static int CUPS  = 32;
//	private final static int UNSUGAR = 0xC6EF3720;
//
//	private int[] S = new int[4];
//
//	/**
//	 * Initialize the cipher for encryption or decryption.
//	 * @param key a 16 byte (128-bit) key
//	 */
//	public TEA(byte[] key) {
//		if (key == null)
//			throw new RuntimeException("Invalid key: Key was null");
//		if (key.length < 16)
//			throw new RuntimeException("Invalid key: Length was less than 16 bytes");
//		for (int off=0, i=0; i<4; i++) {
//			S[i] = ((key[off++] & 0xff)) |
//			((key[off++] & 0xff) <<  8) |
//			((key[off++] & 0xff) << 16) |
//			((key[off++] & 0xff) << 24);
//		}
//	}
//
//	/**
//	 * Encrypt an array of bytes.
//	 * @param clear the cleartext to encrypt
//	 * @return the encrypted text
//	 */
//	public byte[] encrypt(byte[] clear) {
//		int paddedSize = ((clear.length/8) + (((clear.length%8)==0)?0:1)) * 2;
////		if(paddedSize == 8)
////			paddedSize = 0;
//		int[] buffer = new int[paddedSize + 1];
//		buffer[0] = clear.length;
//		pack(clear, buffer, 1);
//		brew(buffer);
//		return unpack(buffer, 0, buffer.length * 4);
//	}
//
//	/**
//	 * Decrypt an array of bytes.
//	 * @param ciper the cipher text to decrypt
//	 * @return the decrypted text
//	 */
//	public byte[] decrypt(byte[] crypt) {
//		assert crypt.length % 4 == 0;
//		assert (crypt.length / 4) % 2 == 1;
//		int[] buffer = new int[crypt.length / 4];
//		pack(crypt, buffer, 0);
//		unbrew(buffer);
//		return unpack(buffer, 1, buffer[0]);
//	}
//
//	void brew(int[] buf) {
//		assert buf.length % 2 == 1;
//		int i, v0, v1, sum, n;
//		i = 1;
//		while (i<buf.length) {
//			n = CUPS;
//			v0 = buf[i];
//			v1 = buf[i+1];
//			sum = 0;
//			while (n-->0) {
//				sum += SUGAR;
//				v0  += ((v1 << 4 ) + S[0] ^ v1) + (sum ^ (v1 >>> 5)) + S[1];
//				v1  += ((v0 << 4 ) + S[2] ^ v0) + (sum ^ (v0 >>> 5)) + S[3];
//			}
//			buf[i] = v0;
//			buf[i+1] = v1;
//			i+=2;
//		}
//	}
//	
//	void unbrew(int[] buf) {
//		assert buf.length % 2 == 1;
//		int i, v0, v1, sum, n;
//		i = 1;
//		while (i<buf.length) {
//			n = CUPS;
//			v0 = buf[i]; 
//			v1 = buf[i+1];
//			sum = UNSUGAR;
//			while (n--> 0) {
//				v1  -= ((v0 << 4 ) + S[2] ^ v0) + (sum ^ (v0 >>> 5)) + S[3];
//				v0  -= ((v1 << 4 ) + S[0] ^ v1) + (sum ^ (v1 >>> 5)) + S[1];
//				sum -= SUGAR;
//			}
//			buf[i] = v0;
//			buf[i+1] = v1;
//			i+=2;
//		}
//	}
//	
//	void pack(byte[] src, int[] dest, int destOffset) {
//		assert destOffset + (src.length / 4) <= dest.length;
//		int i = 0, shift = 24;
//		int j = destOffset;
//		dest[j] = 0;
//		while (i<src.length) {
//			dest[j] |= ((src[i] & 0xff) << shift);
//			if (shift==0) {
//				shift = 24;
//				j++;
//				if (j<dest.length) dest[j] = 0;
//			}
//			else {
//				shift -= 8;
//			}
//			i++;
//		}
//	}
//	
//	byte[] unpack(int[] src, int srcOffset, int destLength) {
//		assert destLength <= (src.length - srcOffset) * 4;
//		byte[] dest = new byte[destLength];
//		int i = srcOffset;
//		int count = 0;
//		for (int j = 0; j < destLength; j++) {
//			dest[j] = (byte) ((src[i] >> (24 - (8*count))) & 0xff);
//			count++;
//			if (count == 4) {
//				count = 0;
//				i++;
//			}
//		}
//		return dest;
//	}
//
//	/* Simple usage example */
//	public static String quote = "Now rise, and show your strength. Be eloquent, and deep, and tender; see, with a clear eye, into Nature, and into life:  spread your white wings of quivering thought, and soar, a god-like spirit, over the whirling world beneath you, up through long lanes of flaming stars to the gates of eternity!";
//	
//	public static void main(String[] args) {
//		/* Create a cipher using the first 16 bytes of the passphrase */
//		TEA tea = new TEA("And is there honey still for tea?".getBytes());
//
//		byte[] original = quote.getBytes();
//
//		/* Run it through the cipher... and back */
//		byte[] crypt = tea.encrypt(original);
//		byte[] result = tea.decrypt(crypt);
//
//		/* Ensure that all went well */
//        String test = new String(result);
//        if (!test.equals(quote))
//		    throw new RuntimeException("Fail");
//	}
//}