package org.eclipse.swt.internal.image;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import java.io.*;

final class LEDataOutputStream extends OutputStream {
	OutputStream out;
public LEDataOutputStream(OutputStream output) {
	this.out = output;
}
public void close() throws IOException {
	out.close();
}
public void write(byte b[], int off, int len) throws IOException {
	out.write(b, off, len);
}
/**
 * Answer the next byte of the input stream.
 */
public void write(int b) throws IOException {
	out.write(b);
}
/**
 * Answer the next byte of the input stream.
 */
public void writeByte(byte b) throws IOException {
	out.write(b & 0xFF);
}
/**
 * Answer an integer comprised of the next
 * four bytes of the input stream.
 */
public void writeInt(int theInt) throws IOException {
	out.write(theInt & 0xFF);
	out.write((theInt >> 8) & 0xFF);
	out.write((theInt >> 16) & 0xFF);
	out.write((theInt >> 24) & 0xFF);
}
/**
 * Answer an integer comprised of the next
 * two bytes of the input stream.
 */
public void writeShort(int theShort) throws IOException {
	out.write(theShort & 0xFF);
	out.write((theShort >> 8) & 0xFF);
}
}
