
import java.io.*;


	/**
	 * Encodes an already formed URL using the <CODE>application/x-www-form-urlencoded</CODE>
	 * MIME format.
	 *
	 * @param s The String (URL) to be converted.
	 *
	 * @return The converted String (URL).
	 */
	 String URLEncode(String s) {
		int questionMark = s.indexOf('?');

		if (questionMark == -1) { // no parameters
			return s;
		} else {
			return s.substring(0, questionMark+1) + encodeURL(s.substring(questionMark+1));
		}
	}

	/**
	 * Base on code in http://forums.java.sun.com/thread.jspa?threadID=409913&messageID=1806947
	 * Converts a String to the <CODE>application/x-www-form-urlencoded</CODE> MIME
  	 * format.
  	 *
  	 * @param s The String to be converted.
  	 *
  	 * @return The converted String.
	 */
	 String encode(String s) {
		StringBuffer out = new StringBuffer();
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();

		DataOutputStream dOut = new DataOutputStream(bOut);

		try {
			dOut.writeUTF(s);
		} catch (IOException ioe) {
			return null;
		}

		ByteArrayInputStream bIn = new ByteArrayInputStream(bOut.toByteArray());

		/* UTF strings have 2 bytes to indicate the size, so skip these	*/
		bIn.read();
		bIn.read();

		int c = bIn.read();
		while (c >= 0) {
			if ((c >= 'a' && c <= 'z')
					|| (c >= 'A' && c <= 'Z')
					|| (c >= '0' && c <= '9')
					|| c == '.' || c == '-' || c == '*' || c == '_') {
				out.append((char) c);
			} else if (c == ' ') {
				out.append('+');
			} else {
				if (c < 128) {
					appendHex(c,out);
				} else if (c < 224) {
					appendHex(c,out);
					appendHex(bIn.read(),out);
				} else if (c < 240) {
					appendHex(c,out);
					appendHex(bIn.read(),out);
					appendHex(bIn.read(),out);
				}

			}
			c = bIn.read();
		}
		return out.toString();
	}

	/**
	 * Base on code in http://forums.java.sun.com/thread.jspa?threadID=409913&messageID=1806947
	 *
	 * Similar to encode but we don't encode the '=' and '&' characters. This
	 * way we can use this to encode an already formed URL (or better, the parameter
	 * part of an already formed URL).
	 */
	 String encodeURL(String s) {
		StringBuffer out = new StringBuffer();
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();

		DataOutputStream dOut = new DataOutputStream(bOut);

		try {
			dOut.writeUTF(s);
		} catch (IOException ioe) {
			return null;
		}

		ByteArrayInputStream bIn = new ByteArrayInputStream(bOut.toByteArray());

		/* UTF strings have 2 bytes to indicate the size, so skip these	*/
		bIn.read();
		bIn.read();

		int c = bIn.read();
		while (c >= 0) {
			if ((c >= 'a' && c <= 'z')
					|| (c >= 'A' && c <= 'Z')
					|| (c >= '0' && c <= '9')
					|| c == '.' || c == '-'
					|| c == '*' || c == '_'
					|| c == '=' || c == '&') {
				out.append((char) c);
			} else if (c == ' ') {
				out.append('+');
			} else {
				if (c < 128) {
					appendHex(c,out);
				} else if (c < 224) {
					appendHex(c,out);
					appendHex(bIn.read(),out);
				} else if (c < 240) {
					appendHex(c,out);
					appendHex(bIn.read(),out);
					appendHex(bIn.read(),out);
				}

			}
			c = bIn.read();
		}
		return out.toString();
	}

	  void appendHex(int arg0, StringBuffer buff){
		buff.append('%');
		if (arg0<16) {
			buff.append('0');
		}
		buff.append(Integer.toHexString(arg0));
	}

