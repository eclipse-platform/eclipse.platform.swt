package org.eclipse.swt.internal.image;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp 2000
 */

final class JPEGStartOfImage extends JPEGFixedSizeSegment {

	public JPEGStartOfImage() {
		super();
	}
	
	public JPEGStartOfImage(byte[] reference) {
		super(reference);
	}
	
	public JPEGStartOfImage(LEDataInputStream byteStream) {
		super(byteStream);
	}
	
	public int signature() {
		return JPEGFileFormat.SOI;
	}
	
	public int fixedSize() {
		return 2;
	}
}
