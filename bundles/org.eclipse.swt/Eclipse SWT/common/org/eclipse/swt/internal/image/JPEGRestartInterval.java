package org.eclipse.swt.internal.image;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp 2000
 */

final class JPEGRestartInterval extends JPEGFixedSizeSegment {

	public JPEGRestartInterval(LEDataInputStream byteStream) {
		super(byteStream);
	}
	
	public int signature() {
		return JPEGFileFormat.DRI;
	}
	
	public int getRestartInterval() {
		return ((reference[4] & 0xFF) << 8 | (reference[5] & 0xFF));
	}

	public int fixedSize() {
		return 6;
	}
}
