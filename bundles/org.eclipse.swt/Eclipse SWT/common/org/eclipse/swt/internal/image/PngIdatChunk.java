package org.eclipse.swt.internal.image;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;

class PngIdatChunk extends PngChunk {

PngIdatChunk(byte[] reference){
	super(reference);
}

/**
 * Answer whether the chunk is a valid IDAT chunk.
 */
void validate(PngFileReadState readState, PngIhdrChunk headerChunk) {
	if (!readState.readIHDR
		|| (headerChunk.getMustHavePalette() && !readState.readPLTE)
		|| readState.readIEND) 
	{
		SWT.error(SWT.ERROR_INVALID_IMAGE);
	} else {
		readState.readIDAT = true;
	}
	
	super.validate(readState, headerChunk);
}

byte getDataByteAtOffset(int offset) {
	return reference[DATA_OFFSET + offset];
}

}
