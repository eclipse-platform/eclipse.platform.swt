package org.eclipse.swt.internal.image;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.*;

abstract class JPEGFixedSizeSegment extends JPEGSegment {

	public JPEGFixedSizeSegment() {
		reference = new byte[fixedSize()];
		setSegmentMarker(signature());
	}
	
	public JPEGFixedSizeSegment(byte[] reference) {
		super(reference);
	}
	
	public JPEGFixedSizeSegment(LEDataInputStream byteStream) {
		reference = new byte[fixedSize()];
		try {
			byteStream.read(reference);
		} catch (Exception e) { 
			SWT.error(SWT.ERROR_IO, e);
		}
	}
	
	abstract public int fixedSize();

	public int getSegmentLength() {
		return fixedSize() - 2;
	}
	
	public void setSegmentLength(int length) {
	}
}
