package org.eclipse.swt.internal.image;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp 2000
 */

import org.eclipse.swt.*;

abstract class JPEGVariableSizeSegment extends JPEGSegment {

	public JPEGVariableSizeSegment(byte[] reference) {
		super(reference);
	}
	
	public JPEGVariableSizeSegment(LEDataInputStream byteStream) {
		try {
			byte[] header = new byte[4];
			byteStream.read(header);
			reference = header; // to use getSegmentLength()
			byte[] contents = new byte[getSegmentLength() + 2];
			contents[0] = header[0];
			contents[1] = header[1];
			contents[2] = header[2];
			contents[3] = header[3];
			byteStream.read(contents, 4, contents.length - 4);
			reference = contents;
		} catch (Exception e) {
			SWT.error(SWT.ERROR_IO, e);
		}
	}
}
