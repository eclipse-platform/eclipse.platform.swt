package org.eclipse.swt.internal.image;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp 2000
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
