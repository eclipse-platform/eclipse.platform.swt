package org.eclipse.swt.internal.image;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import java.io.*;

final class LEDataInputStream extends InputStream {
	int position;
	PushbackInputStream in;

	public LEDataInputStream(InputStream input) {
		this(input, 512);
	}
	
	public LEDataInputStream(InputStream input, int bufferSize) {
		this.in = new PushbackInputStream(input, bufferSize);
	}
	
	public void close() throws IOException {
		in.close();
	}
	
	/**
	 * Answer how many bytes were read.
	 */
	public int getPosition() {
		return position;
	}
	
	/**
	 * Answer the next byte of the input stream.
	 */
	public int read() throws IOException {
		position++;
		return in.read();
	}
	
	/**
	 * Don't imitate the JDK behaviour of reading a random number
	 * of bytes when you can actually read them all.
	 */
	public int read(byte b[], int off, int len) throws IOException {
		int result;
		int left = len;
		result = in.read(b, off, len);
		while (true) {
			if (result == -1)
				return -1;
			position += result;
			if (result == left)
				return len;
			left -= result;
			off += result;
			result = in.read(b, off, left);
		}
	}
	
	/**
	 * Answer an integer comprised of the next
	 * four bytes of the input stream.
	 */
	public int readInt() throws IOException {
		byte[] buf = new byte[4];
		read(buf);
		return ((((((buf[3] & 0xFF) << 8) | 
			(buf[2] & 0xFF)) << 8) | 
			(buf[1] & 0xFF)) << 8) | 
			(buf[0] & 0xFF);
	}
	
	/**
	 * Answer a short comprised of the next
	 * two bytes of the input stream.
	 */
	public short readShort() throws IOException {
		byte[] buf = new byte[2];
		read(buf);
		return (short)(((buf[1] & 0xFF) << 8) | (buf[0] & 0xFF));
	}
	
	public void unread(byte[] b) throws IOException {
		position -= b.length;
		in.unread(b);
	}
}
