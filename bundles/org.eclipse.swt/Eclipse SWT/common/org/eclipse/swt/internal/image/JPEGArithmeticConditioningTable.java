package org.eclipse.swt.internal.image;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

final class JPEGArithmeticConditioningTable extends JPEGVariableSizeSegment {

	public JPEGArithmeticConditioningTable(LEDataInputStream byteStream) {
		super(byteStream);
	}
	
	public int signature() {
		return JPEGFileFormat.DAC;
	}
}
