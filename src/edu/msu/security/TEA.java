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