package org.eclipse.swt.internal.image;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp 2000
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
