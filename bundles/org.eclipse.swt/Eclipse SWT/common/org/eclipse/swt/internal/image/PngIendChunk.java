package org.eclipse.swt.internal.image;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */

import org.eclipse.swt.*;

class PngIendChunk extends PngChunk {

PngIendChunk(byte[] reference){
	super(reference);
}

/**
 * Answer whether the chunk is a valid IEND chunk.
 */
void validate(PngFileReadState readState, PngIhdrChunk headerChunk) {
	// An IEND chunk is invalid if no IHDR has been read.
	// Or if a palette is required and has not been read.
	// Or if no IDAT chunk has been read.
	if (!readState.readIHDR
		|| (headerChunk.getMustHavePalette() && !readState.readPLTE)
		|| !readState.readIDAT
		|| readState.readIEND) 
	{
		SWT.error(SWT.ERROR_INVALID_IMAGE);
	} else {
		readState.readIEND = true;
	}
	
	super.validate(readState, headerChunk);
	
	// IEND chunks are not allowed to have any data.
	if (getLength() > 0) SWT.error(SWT.ERROR_INVALID_IMAGE);
}

}