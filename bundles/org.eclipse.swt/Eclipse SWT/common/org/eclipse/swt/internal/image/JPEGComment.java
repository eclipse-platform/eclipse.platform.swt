package org.eclipse.swt.internal.image;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp 2000
 */

final class JPEGComment extends JPEGVariableSizeSegment {

	public JPEGComment(byte[] reference) {
		super(reference);
	}
	
	public JPEGComment(LEDataInputStream byteStream) {
		super(byteStream);
	}
	
	public int signature() {
		return JPEGFileFormat.COM;
	}
}
