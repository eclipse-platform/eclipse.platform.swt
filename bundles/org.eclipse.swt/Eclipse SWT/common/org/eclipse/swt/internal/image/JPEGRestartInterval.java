package org.eclipse.swt.internal.image;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
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
