package com.gz.medicine.common.util;

//IMPORTS

//CLASS

/**
* Helper class for encoding byte arrays into base64 Strings and vice-versa.
* Base64 is the MIME / RFC1521 standard for turning binary byte streams into
* meaningless "printable" (i.e. a-z A-Z 0-9 etc.) strings for safe shipping
* around by text-based utilities.
*/
public class Base64 {

	// CLASS CONSTANTS

	/** Magic number representing the padding char when decoding base64. */
	public static final int PAD_CHAR = -1;

	/** Magic number representing the whitespace chars when decoding base64. */
	public static final int WHITESPACE_CHAR = -2;

	/** Magic number representing the invalid chars when decoding base64. */
	public static final int INVALID_CHAR = -3;

	// CLASS METHODS

	/**
	 * Encodes a byte array as a Base64 String. The string is encoded with no
	 * line breaks, so cannot be handled easily by text editors etc.
	 * 
	 * @param raw
	 *            Byte Array to be encoded.
	 * @return Byte64 encoded String representation of <CODE>raw</CODE>
	 */
	public static String encode(byte[] raw) {
		return encode(raw, 0);
	}

	// static StringBuffer encoded = new StringBuffer();

	/**
	 * Encodes a byte array as a Base64 String with line breaks. The user must
	 * pass a maximum line length, which should be a multiple of 4 characters
	 * (if not it will be rounded <EM>up</EM>). The String returned will have
	 * have line breaks ('\n') at or before the maximum line length. In
	 * addition, a linebreak will be added at the end of the message. This
	 * whitespace will be ignored by decoders. A line length of zero or less
	 * means no line breaks.
	 * 
	 * @param raw
	 *            Byte Array to be encoded.
	 * @param maxLineLength
	 *            maximum length of line.
	 * @return Byte64 encoded String representation of <CODE>raw</CODE>
	 */
	public static String encode(byte[] raw, int maxLineLength) {
		int linePos = 0;
		StringBuffer encoded = new StringBuffer();
		encoded.setLength(0);
		for (int i = 0; i < raw.length; i += 3) {
			encoded.append(encodeBlock(raw, i));
			// add linebreak if necessary
			if (maxLineLength > 0) {
				linePos += 4;
				if (linePos >= maxLineLength) {
					encoded.append('\n');
					linePos = 0;
				}
			}

		}
		if (maxLineLength > 0) { // add as final linebreak. mebbe.
			encoded.append('\n');
		}

		return encoded.toString();
	}

	/**
	 * Encodes three characters at offset + [0,1,2] in <CODE>raw<CODE> into
	 * base64 characters. <B>Note</B> this is ripe for optimisation!
	 */
	protected static char[] encodeBlock(byte[] raw, int offset) {
		int block = 0;
		int slack = raw.length - offset - 1;
		int end = (slack >= 2) ? 2 : slack;
		for (int i = 0; i <= end; i++) {
			byte b = raw[offset + i];
			int neuter = (b < 0) ? b + 256 : b;
			block += neuter << (8 * (2 - i));
		}
		char[] base64 = new char[4];
		for (int i = 0; i < 4; i++) {
			int sixbit = (block >>> (6 * (3 - i))) & 0x3f;
			base64[i] = getChar(sixbit);
		}
		if (slack < 1)
			base64[2] = '=';
		if (slack < 2)
			base64[3] = '=';
		return base64;
	}

	/**
	 * Given a six bit value, returns the corresponding base64 character
	 * 
	 * @param sixBit
	 *            Six bit integer value.
	 * @return Corresponding base64 character, or '?' for a number outside
	 *         0..63.
	 */
	protected static char getChar(int sixBit) {
		if (sixBit >= 0 && sixBit <= 25)
			return (char) ('A' + sixBit);
		if (sixBit >= 26 && sixBit <= 51)
			return (char) ('a' + (sixBit - 26));
		if (sixBit >= 52 && sixBit <= 61)
			return (char) ('0' + (sixBit - 52));
		if (sixBit == 62)
			return '+';
		if (sixBit == 63)
			return '/';
		return '?';
	}

	/**
	 * Decodes a Base64 String into a byte array. Note that is can handle
	 * embedded whitespace i.e. linebreaks. <B>Note</B> this is ripe for
	 * optimisation!
	 * 
	 * @param base64
	 *            Base64 String to be decoded.
	 * @return Decoded byte Array containing bytes in the original message.
	 * @exception Base64Exception
	 *                If <CODE>base64</CODE> contains an invalid base64
	 *                message, i.e. illegal charaters, too many padding chars
	 *                etc.
	 */
	public static byte[] decode(String base64) throws Base64Exception {
		// validate params
		if ((base64 == null) || (base64.length() < 1)) {
			return null;
		}

		int base64Length = base64.length();
		int pad = 0;
		// count padding chars at end of string
		// only works for no-whitespace base64 strings
		for (int i = base64Length - 1; base64.charAt(i) == '='; i--)
			pad++;

		int guessLength = base64Length * 6 / 8 - pad; // guaranteed max size
		byte[] decode = new byte[guessLength];
		int decodeIdx = 0;
		int block = 0; // hold value of current decoding block
		int blockIdx = 0; // position in four byte decoding block
		int base64Idx = 0;
		int numPadChars = 0; // holds how many pad chars encountered (0,1,2)
		while (base64Idx < base64Length) {
			int value = getValue(base64.charAt(base64Idx));

			base64Idx++;
			switch (value) {
			case WHITESPACE_CHAR: // ignore whitespace
				break;
			case INVALID_CHAR: // throw exception for duff chars
				throw new Base64Exception(base64.charAt(base64Idx), base64Idx);
			case PAD_CHAR:
				numPadChars++;
				value = 0;
			// fall through...
			default:
				// assumes getValue is working!
				block = (block << 6) + value;
				blockIdx++;
			}
			if (blockIdx == 4) { // decoded a four char block to three bytes.
				blockIdx = 0; // reset counter
				decode[decodeIdx++] = (byte) (block >> 16);
				if (numPadChars == 2)
					break;
				if (decodeIdx < guessLength) {
					decode[decodeIdx++] = (byte) ((block >> 8) & 0xff);
					if (numPadChars == 1)
						break;
					if (decodeIdx < guessLength) {
						decode[decodeIdx++] = (byte) (block & 0xff);
					}
				}
				if (numPadChars > 0) {
					break; // hit some pad chars; at end of message
				}
			}

		} // while (base64Idx < base64Length)

		if (decodeIdx == guessLength) {
			return decode; // we guessed length right!
		}

		else { // calculate true length
			if (numPadChars > 2) {
				throw new Base64Exception("Too many padding characters: "
						+ numPadChars);
			}
			int trueLength = decodeIdx; // - numPadChars;
			byte[] trueDecode = new byte[trueLength];
			// copy correct number of bytes into new array
			System.arraycopy(decode, 0, trueDecode, 0, trueLength);

			return trueDecode; // return true message
		}

	} // end of decode()

	/**
	 * Decodes a single Base64 charater, and returns it as a six bit value.
	 * 
	 * @return Six bit value of char <CODE>c</CODE> or PAD_CHAR,
	 *         WHITESPACE_CHAR or INVALID_CHAR.
	 */
	protected static int getValue(char c) {
		if (c >= 'A' && c <= 'Z')
			return c - 'A';
		if (c >= 'a' && c <= 'z')
			return c - 'a' + 26;
		if (c >= '0' && c <= '9')
			return c - '0' + 52;
		if (c == '+')
			return 62;
		if (c == '/')
			return 63;
		if (c == '=')
			return PAD_CHAR;
		if (Character.isWhitespace(c))
			return WHITESPACE_CHAR;

		return INVALID_CHAR;
	}

	static boolean compareByteArrays(byte[] a1, byte[] a2) {
		if ((a1 == null) || (a2 == null)) {
			if (a1 == a2) {
				System.err.println("both arraysnull");
				return true;
			}
			if (a1 == null) {
				System.err.println("first array null, second not");
			} else {
				System.err.println("second array null, first not");
			}
			return false;
		}
		if (a1.length != a2.length) {
			return false;
		}
		for (int i = 0; i < a1.length; i++) {
			byte b1 = a1[i];
			byte b2 = a2[i];
			if (b1 != b2) {
				System.err.println("bytes at [" + i + "] differ: "
						+ Byte.toString(b1) + ", " + Byte.toString(b2));
				return false;
			}
		}
		return true;
	}

}// end of class

