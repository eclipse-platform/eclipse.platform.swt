package org.eclipse.swt.internal.image;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

final class JPEGEndOfImage extends JPEGFixedSizeSegment {

	public JPEGEndOfImage() {
		super();
	}
	
	public JPEGEndOfImage(byte[] reference) {
		super(reference);
	}
	
	public int signature() {
		return JPEGFileFormat.EOI;
	}
	
	public int fixedSize() {
		return 2;
	}
}
