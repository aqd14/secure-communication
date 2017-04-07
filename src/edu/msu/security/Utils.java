package edu.msu.security;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.Arrays;

public class Utils {

	public Utils() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Save modulus and exponent of a key to file
	 * 
	 * @param fileName
	 * @param mod Modulus
	 * @param exp Exponent
	 */
	public static void saveToFile(String fileName, BigInteger mod, BigInteger exp) {
		ObjectOutputStream oout = null;
		try {
			oout = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
			oout.writeObject(mod);
			oout.writeObject(exp);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (oout != null) {
				try {
					oout.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static byte[] xorByteArrays(byte[] first, byte[] second) {
		if (first.length != second.length) {
			System.err.println("Lengths of inputs are not equal!");
			return null;
		}
		int length = first.length;
		byte[] result = new byte[length];
		int i = 0;
		for (byte b : first) {
			result[i] = (byte) (b ^ second[i]);
			i ++;
		}
		return result;
	}
	
	public static byte[][] convert1dTo2dArray(byte[] data) {
		int length = data.length;
		int subArrays = data.length/8;
		if (length % 8 != 0) {
			subArrays++;
		}
		byte new2dByteArray[][] = new byte[subArrays][8];
		for (int i = 0; i < subArrays; i++) {
			int copyLength;
			if (i == subArrays - 1) {
				copyLength = length % 8;
			} else {
				copyLength = 8;
			}
			System.arraycopy(data, 8*i, new2dByteArray[i], 0, copyLength);
		}
		return new2dByteArray;
	}
	
	public static byte[] convert2dTo1dArray(byte[][] data) {
		int length = data.length;
		byte[] new1dByteArray = new byte[length*8];
		int counter = 0;
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < data[i].length; j ++) {
				new1dByteArray[counter++] = data[i][j]; 
			}
		}
		return new1dByteArray;
	} 
	
	/**
	 * Add '0' to byte array to fit with 8-byte block data
	 * @param data
	 * @return
	 */
	public static byte[] addPadding(byte[] data) {
		if (data == null) {
			return null;
		}
		
		if (data.length % 8 == 0) {
			return data;
		}
		
		int newLength = data.length;
		if (data.length % 8 != 0) {
			newLength = newLength + (8 - (data.length % 8));
		}
		byte[] paddedData = Arrays.copyOf(data, newLength);
		return paddedData;
	}
	
	/**
	 * Remove '0' at the end of byte aray, which were padded before
	 * @param data
	 * @return
	 */
	public static byte[] removePadding(byte[] data) {
		int i = data.length;
		while (i-- > 0 && data[i] == 0) {}
		
		byte[] output = new byte[i+1];
		System.arraycopy(data, 0, output, 0, i+1);
		return output;
	}
}
