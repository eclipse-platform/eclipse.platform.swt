/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.image;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.Compatibility;

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
//	int start = DATA_OFFSET;
//	int end = DATA_OFFSET + getLength();
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
	if (Compatibility.pow2(headerChunk.getBitDepth()) < getPaletteSize()) {
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
