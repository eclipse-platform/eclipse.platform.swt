package org.eclipse.swt.internal.image;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import java.io.*;

final class WinICOFileFormat extends FileFormat {
/**
 * Answer the size in bytes of the file representation of the given
 * icon
 */
int iconSize(ImageData i) {
	int shapeDataStride = (i.width * i.depth + 31) / 32 * 4;
	int maskDataStride = (i.width + 31) / 32 * 4;
	int dataSize = (shapeDataStride + maskDataStride) * i.height;
	return WinBMPFileFormat.BMPHeaderFixedSize + (i.palette.colors.length * 4) + dataSize;
}
public static boolean isICOFile(LEDataInputStream stream) {
	try {
		byte[] header = new byte[4];
		stream.read(header);
		stream.unread(header);
		return header[0] == 0 && header[1] == 0 && header[2] == 1 && header[3] == 0;
	} catch (Exception e) {
		return false;
	}
}
boolean isValidIcon(ImageData i) {
	if (!((i.depth == 1) || (i.depth == 4) || (i.depth == 8)))
		return false;
	int size = i.palette.colors.length;
	return ((size == 2) || (size == 16) || (size == 32) || (size == 256));
}
int loadFileHeader(LEDataInputStream byteStream) {
	int[] fileHeader = new int[3];
	try {
		fileHeader[0] = byteStream.readShort();
		fileHeader[1] = byteStream.readShort();
		fileHeader[2] = byteStream.readShort();
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	}
	if ((fileHeader[0] != 0) || (fileHeader[1] != 1))
		SWT.error(SWT.ERROR_INVALID_IMAGE);
	int numIcons = fileHeader[2];
	if (numIcons <= 0)
		SWT.error(SWT.ERROR_INVALID_IMAGE);
	return numIcons;
}
int loadFileHeader(LEDataInputStream byteStream, boolean hasHeader) {
	int[] fileHeader = new int[3];
	try {
		if (hasHeader) {
			fileHeader[0] = byteStream.readShort();
			fileHeader[1] = byteStream.readShort();
		} else {
			fileHeader[0] = 0;
			fileHeader[1] = 1;
		}
		fileHeader[2] = byteStream.readShort();
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	}
	if ((fileHeader[0] != 0) || (fileHeader[1] != 1))
		SWT.error(SWT.ERROR_INVALID_IMAGE);
	int numIcons = fileHeader[2];
	if (numIcons <= 0)
		SWT.error(SWT.ERROR_INVALID_IMAGE);
	return numIcons;
}
ImageData[] loadFromByteStream() {
	int numIcons = loadFileHeader(inputStream);
	int[][] headers = loadIconHeaders(numIcons);
	ImageData[] icons = new ImageData[headers.length];
	for (int i = 0; i < icons.length; i++) {
		icons[i] = loadIcon(headers[i]);
	}
	return icons;
}
/**
 * Load one icon from the byte stream.
 */
ImageData loadIcon(int[] iconHeader) {
	byte[] infoHeader = loadInfoHeader(iconHeader);
	WinBMPFileFormat bmpFormat = new WinBMPFileFormat();
	bmpFormat.inputStream = inputStream;
	PaletteData palette = bmpFormat.loadPalette(infoHeader);
	byte[] shapeData = bmpFormat.loadData(infoHeader);
	int depth = (infoHeader[14] & 0xFF) | ((infoHeader[15] & 0xFF) << 8);
	infoHeader[14] = 1;
	infoHeader[15] = 0;
	byte[] maskData = bmpFormat.loadData(infoHeader);
	bitInvertData(maskData, 0, maskData.length);
	int infoWidth = (infoHeader[4] & 0xFF) | ((infoHeader[5] & 0xFF) << 8) | ((infoHeader[6] & 0xFF) << 16) | ((infoHeader[7] & 0xFF) << 24);
	int infoHeight = (infoHeader[8] & 0xFF) | ((infoHeader[9] & 0xFF) << 8) | ((infoHeader[10] & 0xFF) << 16) | ((infoHeader[11] & 0xFF) << 24);
	return ImageData.internal_new(
		infoWidth,
		infoHeight,
		depth,
		palette,
		4,
		shapeData,
//		4,
		2,
		maskData,
		null,
		-1,
		-1,
		SWT.IMAGE_ICO,
		0,
		0,
		0,
		0);
}
int[][] loadIconHeaders(int numIcons) {
	int[][] headers = new int[numIcons][7];
	try {
		for (int i = 0; i < numIcons; i++) {
			headers[i][0] = inputStream.read();
			headers[i][1] = inputStream.read();
			headers[i][2] = inputStream.readShort();
			headers[i][3] = inputStream.readShort();
			headers[i][4] = inputStream.readShort();
			headers[i][5] = inputStream.readInt();
			headers[i][6] = inputStream.readInt();
		}
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	}
	return headers;
}
byte[] loadInfoHeader(int[] iconHeader) {
	int width = iconHeader[0];
	int height = iconHeader[1];
	int numColors = iconHeader[2]; // the number of colors is in the low byte, but the high byte must be 0
	if (numColors == 0) numColors = 256; // this is specified: '00' represents '256' (0x100) colors
	if ((numColors != 2) && (numColors != 8) && (numColors != 16) &&
		(numColors != 32) && (numColors != 256))
		SWT.error(SWT.ERROR_INVALID_IMAGE);
	if (inputStream.getPosition() < iconHeader[6]) {
		// Seek to the specified offset
		try {
			inputStream.skip(iconHeader[6] - inputStream.getPosition());
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
			return null;
		}
	}
	byte[] infoHeader = new byte[WinBMPFileFormat.BMPHeaderFixedSize];
	try {
		inputStream.read(infoHeader);
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	}
	if (((infoHeader[12] & 0xFF) | ((infoHeader[13] & 0xFF) << 8)) != 1)
		SWT.error(SWT.ERROR_INVALID_IMAGE);
	int infoWidth = (infoHeader[4] & 0xFF) | ((infoHeader[5] & 0xFF) << 8) | ((infoHeader[6] & 0xFF) << 16) | ((infoHeader[7] & 0xFF) << 24);
	int infoHeight = (infoHeader[8] & 0xFF) | ((infoHeader[9] & 0xFF) << 8) | ((infoHeader[10] & 0xFF) << 16) | ((infoHeader[11] & 0xFF) << 24);
	int bitCount = (infoHeader[14] & 0xFF) | ((infoHeader[15] & 0xFF) << 8);
	if (height == infoHeight && bitCount == 1) height /= 2;
	if (!((width == infoWidth) && (height * 2 == infoHeight) &&
		((bitCount == 1) || (bitCount == 4) || (bitCount == 8))))
			SWT.error(SWT.ERROR_INVALID_IMAGE);
	infoHeader[8] = (byte)(height & 0xFF);
	infoHeader[9] = (byte)((height >> 8) & 0xFF);
	infoHeader[10] = (byte)((height >> 16) & 0xFF);
	infoHeader[11] = (byte)((height >> 24) & 0xFF);
	return infoHeader;
}
/**
 * Unload a single icon
 */
void unloadIcon(ImageData icon) {
	int sizeImage = (((icon.width * icon.depth + 31) / 32 * 4) +
		((icon.width + 31) / 32 * 4)) * icon.height;
	try {
		outputStream.writeInt(WinBMPFileFormat.BMPHeaderFixedSize);
		outputStream.writeInt(icon.width);
		outputStream.writeInt(icon.height * 2);
		outputStream.writeShort(1);
		outputStream.writeShort((short)icon.depth);
		outputStream.writeInt(0);
		outputStream.writeInt(sizeImage);
		outputStream.writeInt(0);
		outputStream.writeInt(0);
		outputStream.writeInt(icon.palette.colors.length);
		outputStream.writeInt(0);
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	}
	
	byte[] rgbs = new WinBMPFileFormat().paletteToBytes(icon.palette);
	try {
		outputStream.write(rgbs);
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	}
	unloadShapeData(icon);
	unloadMaskData(icon);
}
/**
 * Unload the icon header for the given icon, calculating the offset.
 */
void unloadIconHeader(ImageData i) {
	int headerSize = 16;
	int offset = headerSize + 6;
	int iconSize = iconSize(i);
	try {
		outputStream.writeByte((byte)i.width);
		outputStream.writeByte((byte)i.height);
		outputStream.writeShort(i.palette.colors.length);
		outputStream.writeShort(0);
		outputStream.writeShort(0);
		outputStream.writeInt(iconSize);
		outputStream.writeInt(offset);
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	}
}
void unloadIntoByteStream(ImageData image) {
	if (!isValidIcon(image))
		SWT.error(SWT.ERROR_INVALID_IMAGE);
	try {
		outputStream.writeShort(0);
		outputStream.writeShort(1);
		outputStream.writeShort(1);
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	}
	unloadIconHeader(image);
	unloadIcon(image);
}
/**
 * Unload the mask data for an icon. The data is flipped vertically
 * and inverted.
 */
void unloadMaskData(ImageData icon) {
	int bpl = (icon.width + 7) / 8;
	int pad = 4;
	int srcBpl = (bpl + pad - 1) / pad * pad;
	int destBpl = (bpl + 3) / 4 * 4;
	byte[] buf = new byte[destBpl];
	int offset = (icon.height - 1) * srcBpl;
	byte[] data = icon.getTransparencyMask().data;
	try {
		for (int i = 0; i < icon.height; i++) {
			System.arraycopy(data, offset, buf, 0, bpl);
			bitInvertData(buf, 0, bpl);
			outputStream.write(buf, 0, destBpl);
			offset -= srcBpl;
		}
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	}
}
/**
 * Unload the shape data for an icon. The data is flipped vertically.
 */
void unloadShapeData(ImageData icon) {
	int bpl = (icon.width * icon.depth + 7) / 8;
	int pad = 4;
	int srcBpl = (bpl + pad - 1) / pad * pad;
	int destBpl = (bpl + 3) / 4 * 4;
	byte[] buf = new byte[destBpl];
	int offset = (icon.height - 1) * srcBpl;
	byte[] data = icon.data;
	try {
		for (int i = 0; i < icon.height; i++) {
			System.arraycopy(data, offset, buf, 0, bpl);
			outputStream.write(buf, 0, destBpl);
			offset -= srcBpl;
		}
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	}
}
}
