package org.eclipse.swt.internal.image;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;

public class PngChunkReader {
	LEDataInputStream inputStream;
	PngFileReadState readState;
	PngIhdrChunk headerChunk;
	PngPlteChunk paletteChunk;
	
PngChunkReader(LEDataInputStream inputStream) {
	this.inputStream = inputStream;
	readState = new PngFileReadState();
	headerChunk = null;
}

PngIhdrChunk getIhdrChunk() {
	if (headerChunk == null) {
		try { 
			PngChunk chunk = PngChunk.readNextFromStream(inputStream);
			headerChunk = (PngIhdrChunk) chunk;
			headerChunk.validate(readState, null);
		} catch (ClassCastException e) {
			SWT.error(SWT.ERROR_INVALID_IMAGE);
		}
	}
	return headerChunk;
}

PngChunk readNextChunk() {
	if (headerChunk == null) return getIhdrChunk();
	
	PngChunk chunk = PngChunk.readNextFromStream(inputStream);
	switch (chunk.getChunkType()) {
		case PngChunk.CHUNK_tRNS:
			((PngTrnsChunk) chunk).validate(readState, headerChunk, paletteChunk);
			break;
		case PngChunk.CHUNK_PLTE:
			chunk.validate(readState, headerChunk);
			paletteChunk = (PngPlteChunk) chunk;
			break;
		default:
			chunk.validate(readState, headerChunk);
	}
	if (readState.readIDAT && !(chunk.getChunkType() == PngChunk.CHUNK_IDAT)) {
		readState.readPixelData = true;
	}
	return chunk;
}

boolean readPixelData() {
	return readState.readPixelData;
}

boolean hasMoreChunks() {
	return !readState.readIEND;
}

}
