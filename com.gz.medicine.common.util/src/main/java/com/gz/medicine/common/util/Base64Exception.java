package com.gz.medicine.common.util;

//CLASS

/**
* Exception for Base64 encoder/decoder class.
* 
* @version 1.1
* @see Base64Test
*/
public class Base64Exception extends Exception {

	public static final long serialVersionUID = 0;

	/**
	 * Creates a new Base64Exception containing message String s.
	 */
	public Base64Exception(char c, int offset) {
		this("illegal character '" + c + "' at position " + offset);
	}

	/**
	 * Creates a new Base64Exception containing message String s.
	 */
	public Base64Exception(String s) {
		super("Malformed Base64 message: " + s);
	}

	/**
	 * Returns message contained in receiver.
	 * 
	 * @return Message String contained in receiver.
	 */
	public String toString() {
		return getMessage();
	}
}
