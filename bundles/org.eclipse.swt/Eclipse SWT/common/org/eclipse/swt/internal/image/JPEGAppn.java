package org.eclipse.swt.internal.image;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

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
