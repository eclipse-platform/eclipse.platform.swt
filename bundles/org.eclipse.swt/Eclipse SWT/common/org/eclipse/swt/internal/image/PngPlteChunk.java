package org.eclipse.swt.internal.image;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

class PngPlteChunk extends PngChunk {

PngPlteChunk(byte[] reference){
	super(reference);
}

/**
 * Get the number of colors in this palette.
 */
int getPaletteSize() {
	return getLength() / 3;
}

/**
 * Get a PaletteData object representing the colors
 * stored in this PLTE chunk.
 * The result should be cached as the PLTE chunk
 * does not store the palette data created.
 */
PaletteData getPaletteData() {
	RGB[] rgbs = new RGB[getPaletteSize()];
	int start = DATA_OFFSET;
	int end = DATA_OFFSET + getLength();
	for (int i = 0; i < rgbs.length; i++) {
		int offset = DATA_OFFSET + (i * 3);
		int red = reference[offset] & 0xFF;
		int green = reference[offset + 1] & 0xFF;
		int blue = reference[offset + 2] & 0xFF;
		rgbs[i] = new RGB(red, green, blue);		
	}
	return new PaletteData(rgbs);
}

/**
 * Answer whether the chunk is a valid PLTE chunk.
 */
void validate(PngFileReadState readState, PngIhdrChunk headerChunk) {
	// A PLTE chunk is invalid if no IHDR has been read or if any PLTE,
	// IDAT, or IEND chunk has been read.
	if (!readState.readIHDR
		|| readState.readPLTE
		|| readState.readTRNS
		|| readState.readBKGD
		|| readState.readIDAT
		|| readState.readIEND) 
	{
		SWT.error(SWT.ERROR_INVALID_IMAGE);
	} else {
		readState.readPLTE = true;
	}
	
	super.validate(readState, headerChunk);
	
	// Palettes cannot be included in grayscale images.
	if (!headerChunk.getCanHavePalette()) SWT.error(SWT.ERROR_INVALID_IMAGE);
	
	// Palette chunks' data fields must be event multiples
	// of 3. Each 3-byte group represents an RGB value.
	if (getLength() % 3 != 0) SWT.error(SWT.ERROR_INVALID_IMAGE);	
	
	// Palettes cannot have more entries than 2^bitDepth
	// where bitDepth is the bit depth of the image given
	// in the IHDR chunk.
	if (Math.pow(2, headerChunk.getBitDepth()) < getPaletteSize()) {
		SWT.error(SWT.ERROR_INVALID_IMAGE);
	}
	
	// Palettes cannot have more than 256 entries.
	if (256 < getPaletteSize()) SWT.error(SWT.ERROR_INVALID_IMAGE);
}

void contributeToString(StringBuffer buffer) {
	buffer.append("\n\tPalette size:");
	buffer.append(getPaletteSize());
}

}
