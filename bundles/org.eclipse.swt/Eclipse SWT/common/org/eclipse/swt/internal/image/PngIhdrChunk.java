/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.image;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

class PngIhdrChunk extends PngChunk {
	static final int IHDR_DATA_LENGTH = 13;

	static final int WIDTH_DATA_OFFSET = DATA_OFFSET + 0;
	static final int HEIGHT_DATA_OFFSET = DATA_OFFSET + 4;
	static final int BIT_DEPTH_OFFSET = DATA_OFFSET + 8;
	static final int COLOR_TYPE_OFFSET = DATA_OFFSET + 9;
	static final int COMPRESSION_METHOD_OFFSET = DATA_OFFSET + 10;
	static final int FILTER_METHOD_OFFSET = DATA_OFFSET + 11;
	static final int INTERLACE_METHOD_OFFSET = DATA_OFFSET + 12;

	static final byte COLOR_TYPE_GRAYSCALE = 0;
	static final byte COLOR_TYPE_RGB = 2;
	static final byte COLOR_TYPE_PALETTE = 3;
	static final byte COLOR_TYPE_GRAYSCALE_WITH_ALPHA = 4;
	static final byte COLOR_TYPE_RGB_WITH_ALPHA = 6;

	static final int INTERLACE_METHOD_NONE = 0;
	static final int INTERLACE_METHOD_ADAM7 = 1;

	static final int FILTER_NONE = 0;
	static final int FILTER_SUB = 1;
	static final int FILTER_UP = 2;
	static final int FILTER_AVERAGE = 3;
	static final int FILTER_PAETH = 4;

	static final byte[] ValidBitDepths = {1, 2, 4, 8, 16};
	static final byte[] ValidColorTypes = {0, 2, 3, 4, 6};

	int width, height;
	byte bitDepth, colorType, compressionMethod, filterMethod, interlaceMethod;

PngIhdrChunk(int width, int height, byte bitDepth, byte colorType, byte compressionMethod, byte filterMethod, byte interlaceMethod) {
	super(IHDR_DATA_LENGTH);
	setType(TYPE_IHDR);
	setWidth(width);
	setHeight(height);
	setBitDepth(bitDepth);
	setColorType(colorType);
	setCompressionMethod(compressionMethod);
	setFilterMethod(filterMethod);
	setInterlaceMethod(interlaceMethod);
	setCRC(computeCRC());
}

/**
 * Construct a PNGChunk using the reference bytes
 * given.
 */
PngIhdrChunk(byte[] reference) {
	super(reference);
	if (reference.length <= IHDR_DATA_LENGTH) SWT.error(SWT.ERROR_INVALID_IMAGE);
	width = getInt32(WIDTH_DATA_OFFSET);
	height = getInt32(HEIGHT_DATA_OFFSET);
	bitDepth = reference[BIT_DEPTH_OFFSET];
	colorType = reference[COLOR_TYPE_OFFSET];
	compressionMethod = reference[COMPRESSION_METHOD_OFFSET];
	filterMethod = reference[FILTER_METHOD_OFFSET];
	interlaceMethod = reference[INTERLACE_METHOD_OFFSET];
}

@Override
int getChunkType() {
	return CHUNK_IHDR;
}

/**
 * Get the image's width in pixels.
 */
int getWidth() {
	return width;
}

/**
 * Set the image's width in pixels.
 */
void setWidth(int value) {
	setInt32(WIDTH_DATA_OFFSET, value);
	width = value;
}

/**
 * Get the image's height in pixels.
 */
int getHeight() {
	return height;
}

/**
 * Set the image's height in pixels.
 */
void setHeight(int value) {
	setInt32(HEIGHT_DATA_OFFSET, value);
	height = value;
}

/**
 * Get the image's bit depth.
 * This is limited to the values 1, 2, 4, 8, or 16.
 */
byte getBitDepth() {
	return bitDepth;
}

/**
 * Set the image's bit depth.
 * This is limited to the values 1, 2, 4, 8, or 16.
 */
void setBitDepth(byte value) {
	reference[BIT_DEPTH_OFFSET] = value;
	bitDepth = value;
}

/**
 * Get the image's color type.
 * This is limited to the values:
 * 0 - Grayscale image.
 * 2 - RGB triple.
 * 3 - Palette.
 * 4 - Grayscale with Alpha channel.
 * 6 - RGB with Alpha channel.
 */
byte getColorType() {
	return colorType;
}

/**
 * Set the image's color type.
 * This is limited to the values:
 * 0 - Grayscale image.
 * 2 - RGB triple.
 * 3 - Palette.
 * 4 - Grayscale with Alpha channel.
 * 6 - RGB with Alpha channel.
 */
void setColorType(byte value) {
	reference[COLOR_TYPE_OFFSET] = value;
	colorType = value;
}

/**
 * Get the image's compression method.
 * This value must be 0.
 */
byte getCompressionMethod() {
	return compressionMethod;
}

/**
 * Set the image's compression method.
 * This value must be 0.
 */
void setCompressionMethod(byte value) {
	reference[COMPRESSION_METHOD_OFFSET] = value;
	compressionMethod = value;
}

/**
 * Get the image's filter method.
 * This value must be 0.
 */
byte getFilterMethod() {
	return filterMethod;
}

/**
 * Set the image's filter method.
 * This value must be 0.
 */
void setFilterMethod(byte value) {
	reference[FILTER_METHOD_OFFSET] = value;
	filterMethod = value;
}

/**
 * Get the image's interlace method.
 * This value is limited to:
 * 0 - No interlacing used.
 * 1 - Adam7 interlacing used.
 */
byte getInterlaceMethod() {
	return interlaceMethod;
}

/**
 * Set the image's interlace method.
 * This value is limited to:
 * 0 - No interlacing used.
 * 1 - Adam7 interlacing used.
 */
void setInterlaceMethod(byte value) {
	reference[INTERLACE_METHOD_OFFSET] = value;
	interlaceMethod = value;
}

/**
 * Answer whether the chunk is a valid IHDR chunk.
 */
@Override
void validate(PngFileReadState readState, PngIhdrChunk headerChunk) {
	// An IHDR chunk is invalid if any other chunk has
	// been read.
	if (readState.readIHDR
		|| readState.readPLTE
		|| readState.readIDAT
		|| readState.readIEND)
	{
		SWT.error(SWT.ERROR_INVALID_IMAGE);
	} else {
		readState.readIHDR = true;
	}

	super.validate(readState, headerChunk);

	if (length != IHDR_DATA_LENGTH) SWT.error(SWT.ERROR_INVALID_IMAGE);
	if (compressionMethod != 0) SWT.error(SWT.ERROR_INVALID_IMAGE);
	if (interlaceMethod != INTERLACE_METHOD_NONE &&
		interlaceMethod != INTERLACE_METHOD_ADAM7) {
			SWT.error(SWT.ERROR_INVALID_IMAGE);
	}

	boolean colorTypeIsValid = false;
	for (byte validColorType : ValidColorTypes) {
		if (validColorType == colorType) {
			colorTypeIsValid = true;
			break;
		}
	}
	if (!colorTypeIsValid) SWT.error(SWT.ERROR_INVALID_IMAGE);

	boolean bitDepthIsValid = false;
	for (byte validBitDepth : ValidBitDepths) {
		if (validBitDepth == bitDepth) {
			bitDepthIsValid = true;
			break;
		}
	}
	if (!bitDepthIsValid) SWT.error(SWT.ERROR_INVALID_IMAGE);

	if ((colorType == COLOR_TYPE_RGB
		|| colorType == COLOR_TYPE_RGB_WITH_ALPHA
		|| colorType == COLOR_TYPE_GRAYSCALE_WITH_ALPHA)
		&& bitDepth < 8)
	{
			SWT.error(SWT.ERROR_INVALID_IMAGE);
	}

	if (colorType == COLOR_TYPE_PALETTE && bitDepth > 8) {
		SWT.error(SWT.ERROR_INVALID_IMAGE);
	}
}

String getColorTypeString() {
	return switch (colorType) {
	case COLOR_TYPE_GRAYSCALE -> "Grayscale";
	case COLOR_TYPE_RGB -> "RGB";
	case COLOR_TYPE_PALETTE -> "Palette";
	case COLOR_TYPE_GRAYSCALE_WITH_ALPHA -> "Grayscale with Alpha";
	case COLOR_TYPE_RGB_WITH_ALPHA -> "RGB with Alpha";
	default -> "Unknown - " + colorType;
	};
}

String getFilterMethodString() {
	return switch (filterMethod) {
	case FILTER_NONE -> "None";
	case FILTER_SUB -> "Sub";
	case FILTER_UP -> "Up";
	case FILTER_AVERAGE -> "Average";
	case FILTER_PAETH -> "Paeth";
	default -> "Unknown";
	};
}

String getInterlaceMethodString() {
	return switch (interlaceMethod) {
	case INTERLACE_METHOD_NONE -> "Not Interlaced";
	case INTERLACE_METHOD_ADAM7 -> "Interlaced - ADAM7";
	default -> "Unknown";
	};
}

@Override
void contributeToString(StringBuilder buffer) {
	buffer.append("\n\tWidth: ");
	buffer.append(width);
	buffer.append("\n\tHeight: ");
	buffer.append(height);
	buffer.append("\n\tBit Depth: ");
	buffer.append(bitDepth);
	buffer.append("\n\tColor Type: ");
	buffer.append(getColorTypeString());
	buffer.append("\n\tCompression Method: ");
	buffer.append(compressionMethod);
	buffer.append("\n\tFilter Method: ");
	buffer.append(getFilterMethodString());
	buffer.append("\n\tInterlace Method: ");
	buffer.append(getInterlaceMethodString());
}

boolean getMustHavePalette() {
	return colorType == COLOR_TYPE_PALETTE;
}

boolean getCanHavePalette() {
	return colorType != COLOR_TYPE_GRAYSCALE &&
		colorType != COLOR_TYPE_GRAYSCALE_WITH_ALPHA;
}

/**
 * Answer the pixel size in bits based on the color type
 * and bit depth.
 */
int getBitsPerPixel() {
	return switch (colorType) {
	case COLOR_TYPE_RGB_WITH_ALPHA -> 4 * bitDepth;
	case COLOR_TYPE_RGB -> 3 * bitDepth;
	case COLOR_TYPE_GRAYSCALE_WITH_ALPHA -> 2 * bitDepth;
	case COLOR_TYPE_GRAYSCALE, COLOR_TYPE_PALETTE -> bitDepth;
	default -> {
		SWT.error(SWT.ERROR_INVALID_IMAGE);
		yield 0;
	}
	};
}

/**
 * Answer the pixel size in bits based on the color type
 * and bit depth.
 */
int getSwtBitsPerPixel() {
	return switch (colorType) {
	case COLOR_TYPE_RGB_WITH_ALPHA, COLOR_TYPE_RGB, COLOR_TYPE_GRAYSCALE_WITH_ALPHA -> 24;
	case COLOR_TYPE_GRAYSCALE, COLOR_TYPE_PALETTE -> Math.min(bitDepth, 8);
	default -> {
		SWT.error(SWT.ERROR_INVALID_IMAGE);
		yield 0;
	}
	};
}

int getFilterByteOffset() {
	if (bitDepth < 8) return 1;
	return getBitsPerPixel() / 8;
}

boolean usesDirectColor() {
	return switch (colorType) {
	case COLOR_TYPE_GRAYSCALE, COLOR_TYPE_GRAYSCALE_WITH_ALPHA, COLOR_TYPE_RGB, COLOR_TYPE_RGB_WITH_ALPHA -> true;
	default -> false;
	};
}

PaletteData createGrayscalePalette() {
	int depth = Math.min(bitDepth, 8);
	int max = (1 << depth) - 1;
	int delta = 255 / max;
	int gray = 0;
	RGB[] rgbs = new RGB[max + 1];
	for (int i = 0; i <= max; i++) {
		rgbs[i] = new RGB(gray, gray, gray);
		gray += delta;
	}
	return new PaletteData(rgbs);
}

PaletteData getPaletteData() {
	return switch (colorType) {
	case COLOR_TYPE_GRAYSCALE -> createGrayscalePalette();
	case COLOR_TYPE_GRAYSCALE_WITH_ALPHA, COLOR_TYPE_RGB, COLOR_TYPE_RGB_WITH_ALPHA -> new PaletteData(0xFF0000, 0xFF00, 0xFF);
	default -> null;
	};
}



}
