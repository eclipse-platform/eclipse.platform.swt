package org.eclipse.swt.internal.image;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp 2000
 */

final class JPEGArithmeticConditioningTable extends JPEGVariableSizeSegment {

	public JPEGArithmeticConditioningTable(LEDataInputStream byteStream) {
		super(byteStream);
	}
	
	public int signature() {
		return JPEGFileFormat.DAC;
	}
}
