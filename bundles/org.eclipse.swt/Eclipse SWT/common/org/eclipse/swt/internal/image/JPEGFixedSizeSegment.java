package org.eclipse.swt.internal.image;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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
