package org.eclipse.swt.internal.image;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

final class JPEGArithmeticConditioningTable extends JPEGVariableSizeSegment {

	public JPEGArithmeticConditioningTable(LEDataInputStream byteStream) {
		super(byteStream);
	}
	
	public int signature() {
		return JPEGFileFormat.DAC;
	}
}
