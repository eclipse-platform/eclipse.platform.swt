package org.eclipse.swt.internal.image;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import java.io.*;

final class LEDataInputStream extends InputStream {
	int position;
	InputStream in;

	/**
	 * The byte array containing the bytes to read.
	 */
	protected byte[] buf;
	
	/**
	 * The current position within the byte array <code>buf</code>. A value
	 * equal to buf.length indicates no bytes available.  A value of
	 * 0 indicates the buffer is full.
	 */
	protected int pos;
	

	public LEDataInputStream(InputStream input) {
		this(input, 512);
	}
	
	public LEDataInputStream(InputStream input, int bufferSize) {
		this.in = input;
		if (bufferSize > 0) {
			buf = new byte[bufferSize];
			pos = bufferSize;
		} 
		else throw new IllegalArgumentException();
	}
	
	public void close() throws IOException {
		if (in != null) {
			in.close();
			in = null;
			buf = null;
		}
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
		if (buf == null) throw new IOException();
		position++;
		if (pos < buf.length) return (buf[pos++] & 0xFF);
		return in.read();
	}
	
	/**
	 * Don't imitate the JDK behaviour of reading a random number
	 * of bytes when you can actually read them all.
	 */
	public int read(byte b[], int off, int len) throws IOException {
		int result;
		int left = len;
		result = readData(b, off, len);
		while (true) {
			if (result == -1) return -1;
			position += result;
			if (result == left) return len;
			left -= result;
			off += result;
			result = readData(b, off, left);
		}
	}
	
	/**
 	 * Reads at most <code>length</code> bytes from this LEDataInputStream and 
 	 * stores them in byte array <code>buffer</code> starting at <code>offset</code>.
 	 * <p>
 	 * Answer the number of bytes actually read or -1 if no bytes were read and 
 	 * end of stream was encountered.  This implementation reads bytes from 
 	 * the pushback buffer first, then the target stream if more bytes are required
 	 * to satisfy <code>count</code>.
	 * </p>
	 * @param buffer the byte array in which to store the read bytes.
	 * @param offset the offset in <code>buffer</code> to store the read bytes.
	 * @param length the maximum number of bytes to store in <code>buffer</code>.
	 *
	 * @return int the number of bytes actually read or -1 if end of stream.
	 *
	 * @exception java.io.IOException if an IOException occurs.
	 */
	private int readData(byte[] buffer, int offset, int length) throws IOException {
		if (buf == null) throw new IOException();
		if (offset < 0 || offset > buffer.length ||
  		 	length < 0 || (length > buffer.length - offset)) {
	 		throw new ArrayIndexOutOfBoundsException();
		 	}
				
		int copiedBytes = 0;
		int copyLength = 0;
		int newOffset = offset;
	
		// Are there pushback bytes available?
		if (pos < buf.length) {
			copyLength = (buf.length - pos >= length) ? length : buf.length - pos;
			System.arraycopy(buf, pos, buffer, newOffset, copyLength);
			newOffset += copyLength;
			copiedBytes += copyLength;
			pos += copyLength;
		}
	
		// Have we copied enough?
		if (copyLength == length) return length;

		int inCopied = in.read(buffer, newOffset, length - copiedBytes);

		if (inCopied > 0) return inCopied + copiedBytes;
		if (copiedBytes == 0) return inCopied;
		return copiedBytes;
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
		unread(b, 0, b.length);
	}
	
	/**
	 * Push back <code>length</code> number of bytes in <code>buffer</code> 
	 * starting at <code>offset</code>.
	 * <p>
	 * The bytes are pushed so that they would be read back buffer[offset], 
	 * buffer[offset+1], etc. If the push back buffer cannot handle the bytes
	 * copied from <code>buffer</code>, an IOException will be thrown. 
	 * Some of the bytes may already be in the buffer after the exception
	 * is thrown.
	 * </p>
	 * 
	 * @param buffer the byte array containing bytes to push back into the stream
	 * @param offset the location to start taking bytes to push back
	 * @param length the number of bytes to push back
	 *
	 * @exception 	java.io.IOException if the pushback buffer becomes, or is, full
	 */
	private void unread(byte[] buffer, int offset, int length) throws IOException {
		if (offset < 0 || offset > buffer.length ||
			length < 0 || (length > buffer.length - offset)) {
			throw new ArrayIndexOutOfBoundsException();
			}
		for (int i = offset + length - 1; i >= offset; i--)
			unread(buffer[i]); 
	}

	/**
	 * Push back one <code>byte</code>.  
	 * <p>
	 * Takes the byte <code>oneByte</code> and puts in the local buffer of bytes
	 * to read back before accessing the target input stream.
	 * </p>
	 * 
	 * @param oneByte the byte to push back into the stream.
	 *
	 * @exception java.io.IOException if the pushback buffer is already full.
	 */
	private void unread(int oneByte) throws IOException {
		if (buf == null || pos == 0) throw new IOException();
		buf[--pos] = (byte)oneByte;
	}
	
}