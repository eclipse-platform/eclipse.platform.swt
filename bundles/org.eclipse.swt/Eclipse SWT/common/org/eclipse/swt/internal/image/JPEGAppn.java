package org.eclipse.swt.internal.image;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;

final class JPEGAppn extends JPEGVariableSizeSegment {

	public JPEGAppn(byte[] reference) {
		super(reference);
	}
	
	public JPEGAppn(LEDataInputStream byteStream) {
		super(byteStream);
	}
	
	public boolean verify() {
		int marker = getSegmentMarker();
		return marker >= JPEGFileFormat.APP0 && marker <= JPEGFileFormat.APP15;
	}
}
