package org.eclipse.swt.internal.image;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
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
