/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.internal;

import java.io.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.*;

/**
 * Customize the icon of a Windows exe
 * 
 * WARNING! This class is not part of SWT API. It is NOT API. It is an internal
 * tool that may be changed or removed at anytime.
 * 
 * Based on MSDN "An In-Depth Look into the Win32 Portable Executable File Format"
 * http://msdn.microsoft.com/msdnmag/issues/02/03/PE2/default.aspx
 */
public class IconExe {
	
	/** 
	 * Retrieve the Desktop icons provided in the Windows executable program.
	 * These icons are typically shown in various places of the Windows desktop.
	 * 
	 * Note.  The Eclipse 3.1 launcher returns the following 6 images
	 * 1. 32x32, 4 bit (Windows 16 colors palette)
	 * 2. 16x16, 4 bit (Windows 16 colors palette)
	 * 3. 16x16, 8 bit (256 colors)
	 * 4. 32x32, 8 bit (256 colors)
	 * 5. 48x48, 4 bit (Windows 16 colors palette)
	 * 6. 48x48, 8 bit (256 colors)
	 * 
	 * @param program the Windows executable e.g c:/eclipse/eclipse.exe
	 */	
	public static ImageData[] loadIcons(String program) throws FileNotFoundException, IOException {
		RandomAccessFile raf = new RandomAccessFile(program, "r");
		IconExe iconExe = new IconExe();
		IconResInfo[] iconInfo = iconExe.getIcons(raf);
		ImageData[] data = new ImageData[iconInfo.length];
		for (int i = 0; i < data.length; i++) data[i] = iconInfo[i].data;
		raf.close();
		return data;
	}
	
	/** 
	 * Replace the Desktop icons provided in the Windows executable program
	 * with icons provided by the user.
	 * 
	 * Note 1. Write access to the executable program is required. As a result, that
	 * program must not be currently running or edited elsewhere.
	 * 
	 * Note 2. Use loadIcons to determine which set of icons (width, height, depth)
	 * is required to replace the icons in the executable program. A user icon
	 * matching exactly the width/height/depth of an executable icon will be written
	 * to the executable and will replace that executable icon. If an executable icon
	 * does not match a user icon, it is left as is. Verify the return value matches
	 * the number of icons to write. Finally, use loadIcons after this operation
	 * to verify the icons have changed as expected.
	 * 
	 * Note 3. The Eclipse 3.1 launcher requires the following 6 images (in any order).
	 * 1. 32x32, 4 bit (Windows 16 colors palette)
	 * 2. 16x16, 4 bit (Windows 16 colors palette)
	 * 3. 16x16, 8 bit (256 colors)
	 * 4. 32x32, 8 bit (256 colors)
	 * 5. 48x48, 4 bit (Windows 16 colors palette)
	 * 6. 48x48, 8 bit (256 colors)
	 * 
	 * Note 4. This function modifies the content of the executable program and may cause
	 * its corruption. 
	 * 
	 * @param program the Windows executable e.g c:/eclipse/eclipse.exe
	 * @return the number of icons written to the program
	 */	
	public static int unloadIcons(String program, ImageData[] icons) throws FileNotFoundException, IOException {
		RandomAccessFile raf = new RandomAccessFile(program, "rw");
		IconExe iconExe = new IconExe();
		IconResInfo[] iconInfo = iconExe.getIcons(raf);
		int cnt = 0;
		for (int i = 0; i < iconInfo.length; i++) {
			for (int j = 0; j < icons.length; j++)
			if (iconInfo[i].data.width == icons[j].width && 
				iconInfo[i].data.height == icons[j].height && 
				iconInfo[i].data.depth == icons[j].depth) {
				raf.seek(iconInfo[i].offset);
				unloadIcon(raf, icons[j]);
				cnt++;
			}
		}
		raf.close();
		return cnt;
	}
	
	/* Implementation */
	
	public static final String VERSION = "20050111";
	
	static final boolean DEBUG = true;
	public static class IconResInfo {
		ImageData data;
		int offset;
		int size;
		IconResInfo next;
	}
	
	IconResInfo[] iconInfo = null;
	int iconCnt;
	
	IconResInfo[] getIcons(RandomAccessFile raf) throws IOException {
		iconInfo = new IconResInfo[4];
		iconCnt = 0;
		IMAGE_DOS_HEADER imageDosHeader = new IMAGE_DOS_HEADER();
		read(raf, imageDosHeader);
		if (imageDosHeader.e_magic != IMAGE_DOS_SIGNATURE) return null;
		int imageNtHeadersOffset = imageDosHeader.e_lfanew;
		raf.seek(imageNtHeadersOffset);
		IMAGE_NT_HEADERS imageNtHeaders = new IMAGE_NT_HEADERS();
		read(raf, imageNtHeaders);
		if (imageNtHeaders.Signature != IMAGE_NT_SIGNATURE) return null;
		
		// DumpResources
		int resourcesRVA = imageNtHeaders.OptionalHeader.DataDirectory[IMAGE_DIRECTORY_ENTRY_RESOURCE].VirtualAddress;
		if (resourcesRVA == 0) return null;
		if (DEBUG) System.out.println("* Resources (RVA= "+resourcesRVA+")");
		IMAGE_SECTION_HEADER imageSectionHeader = new IMAGE_SECTION_HEADER();
		int firstSectionOffset = imageNtHeadersOffset + IMAGE_NT_HEADERS.FIELD_OFFSET_OptionalHeader + imageNtHeaders.FileHeader.SizeOfOptionalHeader;
		raf.seek(firstSectionOffset);
		boolean found = false;
		for (int i = 0; i < imageNtHeaders.FileHeader.NumberOfSections; i++) {
			read(raf, imageSectionHeader);
			if (resourcesRVA >= imageSectionHeader.VirtualAddress && resourcesRVA < imageSectionHeader.VirtualAddress + imageSectionHeader.Misc_VirtualSize) {
				// could check the imageSectionHeader name is .rsrc
				found = true;
				break;
			}
		}
		if (!found) return null;
		int delta = imageSectionHeader.VirtualAddress - imageSectionHeader.PointerToRawData;
		int imageResourceDirectoryOffset = resourcesRVA - delta;
		dumpResourceDirectory(raf, imageResourceDirectoryOffset, imageResourceDirectoryOffset, delta, 0, 0, false);
		if (iconCnt < iconInfo.length) {
			IconResInfo[] newArray = new IconResInfo[iconCnt];
			System.arraycopy(iconInfo, 0, newArray, 0, iconCnt);
			iconInfo = newArray;
		}
		return iconInfo;
	}

void dumpResourceDirectory(RandomAccessFile raf, int imageResourceDirectoryOffset, int resourceBase, int delta, int type, int level, boolean rt_icon_root) throws IOException {
	if (DEBUG) System.out.println("** LEVEL "+level);

	IMAGE_RESOURCE_DIRECTORY imageResourceDirectory = new IMAGE_RESOURCE_DIRECTORY();
	raf.seek(imageResourceDirectoryOffset);
	read(raf, imageResourceDirectory);

	if (DEBUG) {
		String sType = ""+type;
		// level 1 resources are resource types
		if (level == 1) {
			System.out.println("___________________________");
			if (type == RT_ICON) sType = "RT_ICON";
			if (type == RT_GROUP_ICON) sType = "RT_GROUP_ICON";
		}
		System.out.println("Resource Directory ["+sType+"]"+" (Named "+imageResourceDirectory.NumberOfNamedEntries+", ID "+imageResourceDirectory.NumberOfIdEntries+")");
	}
	int IRDE_StartOffset = imageResourceDirectoryOffset + IMAGE_RESOURCE_DIRECTORY.SIZEOF;	
	IMAGE_RESOURCE_DIRECTORY_ENTRY[] imageResourceDirectoryEntries = new IMAGE_RESOURCE_DIRECTORY_ENTRY[imageResourceDirectory.NumberOfIdEntries];
	for (int i = 0; i < imageResourceDirectoryEntries.length; i++) {
		imageResourceDirectoryEntries[i] = new IMAGE_RESOURCE_DIRECTORY_ENTRY();
		read(raf, imageResourceDirectoryEntries[i]);
	}
	for (int i = 0; i < imageResourceDirectoryEntries.length; i++) {
		if (imageResourceDirectoryEntries[i].DataIsDirectory) {
			dumpResourceDirectory(raf, imageResourceDirectoryEntries[i].OffsetToDirectory + resourceBase, resourceBase, delta, imageResourceDirectoryEntries[i].Id, level + 1, rt_icon_root ? true : type == RT_ICON);
		} else {
			// Resource found
			/// pResDirEntry->Name
			IMAGE_RESOURCE_DIRECTORY_ENTRY irde = imageResourceDirectoryEntries[i];
			IMAGE_RESOURCE_DATA_ENTRY data = new IMAGE_RESOURCE_DATA_ENTRY();
			raf.seek(imageResourceDirectoryEntries[i].OffsetToData + resourceBase);
			read(raf, data);
			if (DEBUG) System.out.println("Resource Id "+irde.Id+" Data Offset RVA "+data.OffsetToData+", Size "+data.Size);
			if (rt_icon_root) {
				if (DEBUG) System.out.println("iconcnt "+iconCnt+" |"+iconInfo.length);
				iconInfo[iconCnt] = new IconResInfo();
				iconInfo[iconCnt].data = parseIcon(raf, data.OffsetToData - delta, data.Size);
				iconInfo[iconCnt].offset = data.OffsetToData - delta;
				iconInfo[iconCnt].size = data.Size;	
				iconCnt++;
				if (iconCnt == iconInfo.length) {
					IconResInfo[] newArray = new IconResInfo[iconInfo.length + 4];
					System.arraycopy(iconInfo, 0, newArray, 0, iconInfo.length);
					iconInfo = newArray;
				}
			}
		}
 	}
}

static ImageData parseIcon(RandomAccessFile raf, int offset, int size) throws IOException {
	raf.seek(offset);
	BITMAPINFO bitmapInfo = new BITMAPINFO();
	read(raf, bitmapInfo);
	bitmapInfo.bmiHeader.biHeight /= 2;
	int width = bitmapInfo.bmiHeader.biWidth;
	int height = bitmapInfo.bmiHeader.biHeight;
	int depth = bitmapInfo.bmiHeader.biBitCount;

	PaletteData palette = loadPalette(bitmapInfo.bmiHeader, raf);
	byte[] shapeData = loadData(bitmapInfo.bmiHeader, raf);
	bitmapInfo.bmiHeader.biBitCount = 1;
	byte[] maskData = loadData(bitmapInfo.bmiHeader, raf);
	maskData = convertPad(maskData, width, height, 1, 4, 2);
	bitInvertData(maskData, 0, maskData.length);
	return ImageData.internal_new(
		width,
		height,
		depth,
		palette,
		4,
		shapeData,
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

static byte[] bitInvertData(byte[] data, int startIndex, int endIndex) {
	// Destructively bit invert data in the given byte array.
	for (int i = startIndex; i < endIndex; i++) {
		data[i] = (byte)(255 - data[i - startIndex]);
	}
	return data;
}

static final byte[] convertPad(byte[] data, int width, int height, int depth, int pad, int newPad) {
	if (pad == newPad) return data;
	int stride = (width * depth + 7) / 8;
	int bpl = (stride + (pad - 1)) / pad * pad;
	int newBpl = (stride + (newPad - 1)) / newPad * newPad;
	byte[] newData = new byte[height * newBpl];
	int srcIndex = 0, destIndex = 0;
	for (int y = 0; y < height; y++) {
		System.arraycopy(data, srcIndex, newData, destIndex, newBpl);
		srcIndex += bpl;
		destIndex += newBpl;
	}
	return newData;
}
static PaletteData loadPalette(BITMAPINFOHEADER bih, RandomAccessFile raf) throws IOException {
	int depth = bih.biBitCount;
	if (depth <= 8) {
		int numColors = bih.biClrUsed;
		if (numColors == 0) {
			numColors = 1 << depth;
		} else {
			if (numColors > 256)
				numColors = 256;
		}
		byte[] buf = new byte[numColors * 4];
		raf.read(buf);
		return paletteFromBytes(buf, numColors);
	}
	if (depth == 16) return new PaletteData(0x7C00, 0x3E0, 0x1F);
	if (depth == 24) return new PaletteData(0xFF, 0xFF00, 0xFF0000);
	return new PaletteData(0xFF00, 0xFF0000, 0xFF000000);
}
static PaletteData paletteFromBytes(byte[] bytes, int numColors) {
	int bytesOffset = 0;
	RGB[] colors = new RGB[numColors];
	for (int i = 0; i < numColors; i++) {
		colors[i] = new RGB(bytes[bytesOffset + 2] & 0xFF,
			bytes[bytesOffset + 1] & 0xFF,
			bytes[bytesOffset] & 0xFF);
		bytesOffset += 4;
	}
	return new PaletteData(colors);
}
static byte[] loadData(BITMAPINFOHEADER bih, RandomAccessFile raf) throws IOException {
	int stride = (bih.biWidth * bih.biBitCount + 7) / 8;
	stride = (stride + 3) / 4 * 4; // Round up to 4 byte multiple
	byte[] data = loadData(bih, raf, stride);
	flipScanLines(data, stride, bih.biHeight);
	return data;
}
static void flipScanLines(byte[] data, int stride, int height) {
	int i1 = 0;
	int i2 = (height - 1) * stride;
	for (int i = 0; i < height / 2; i++) {
		for (int index = 0; index < stride; index++) {
			byte b = data[index + i1];
			data[index + i1] = data[index + i2];
			data[index + i2] = b;
		}
		i1 += stride;
		i2 -= stride;
	}
}
static byte[] loadData(BITMAPINFOHEADER bih, RandomAccessFile raf, int stride) throws IOException {
	int dataSize = bih.biHeight * stride;
	byte[] data = new byte[dataSize];
	int cmp = bih.biCompression;
	if (cmp == 0) { // BMP_NO_COMPRESSION
		raf.read(data);
	} else {
		if (DEBUG) System.out.println("ICO cannot be compressed?");
	}
	return data;
}

static void unloadIcon(RandomAccessFile raf, ImageData icon) throws IOException {
	int sizeImage = (((icon.width * icon.depth + 31) / 32 * 4) +
		((icon.width + 31) / 32 * 4)) * icon.height;
	write4(raf, BMPHeaderFixedSize);
	write4(raf, icon.width);
	write4(raf, icon.height * 2);
	writeU2(raf, 1);
	writeU2(raf, icon.depth);
	write4(raf, 0);
	write4(raf, sizeImage);
	write4(raf, 0);
	write4(raf, 0);
	write4(raf, icon.palette.colors != null ? icon.palette.colors.length : 0);
	write4(raf, 0);
	
	byte[] rgbs = paletteToBytes(icon.palette);
	raf.write(rgbs);
	unloadShapeData(raf, icon);
	unloadMaskData(raf, icon);
}
static byte[] paletteToBytes(PaletteData pal) {
	int n = pal.colors == null ? 0 : (pal.colors.length < 256 ? pal.colors.length : 256);
	byte[] bytes = new byte[n * 4];
	int offset = 0;
	for (int i = 0; i < n; i++) {
		RGB col = pal.colors[i];
		bytes[offset] = (byte)col.blue;
		bytes[offset + 1] = (byte)col.green;
		bytes[offset + 2] = (byte)col.red;
		offset += 4;
	}
	return bytes;
}
static void unloadMaskData(RandomAccessFile raf, ImageData icon) {
	ImageData mask = icon.getTransparencyMask();
	int bpl = (icon.width + 7) / 8;
	int pad = mask.scanlinePad;
	int srcBpl = (bpl + pad - 1) / pad * pad;
	int destBpl = (bpl + 3) / 4 * 4;
	byte[] buf = new byte[destBpl];
	int offset = (icon.height - 1) * srcBpl;
	byte[] data = mask.data;
	try {
		for (int i = 0; i < icon.height; i++) {
			System.arraycopy(data, offset, buf, 0, bpl);
			bitInvertData(buf, 0, bpl);
			raf.write(buf, 0, destBpl);
			offset -= srcBpl;
		}
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	}
}
static void unloadShapeData(RandomAccessFile raf, ImageData icon) {
	int bpl = (icon.width * icon.depth + 7) / 8;
	int pad = icon.scanlinePad;
	int srcBpl = (bpl + pad - 1) / pad * pad;
	int destBpl = (bpl + 3) / 4 * 4;
	byte[] buf = new byte[destBpl];
	int offset = (icon.height - 1) * srcBpl;
	byte[] data = icon.data;
	try {
		for (int i = 0; i < icon.height; i++) {
			System.arraycopy(data, offset, buf, 0, bpl);
			raf.write(buf, 0, destBpl);
			offset -= srcBpl;
		}
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	}
}
static boolean readIconGroup(RandomAccessFile raf, int offset, int size) throws IOException {
	raf.seek(offset);
	NEWHEADER newHeader = new NEWHEADER();
	read(raf, newHeader);
	if (newHeader.ResType != RES_ICON) return false;
	RESDIR[] resDir = new RESDIR[newHeader.ResCount];
	for (int i = 0; i < newHeader.ResCount; i++) {
		resDir[i] = new RESDIR();
		read(raf, resDir[i]);
	}
	return true;
}

static void copyFile(String src, String dst) throws FileNotFoundException, IOException {
	File srcFile = new File(src);
	File dstFile = new File(dst);
	FileInputStream in = new FileInputStream(srcFile);
	FileOutputStream out = new FileOutputStream(dstFile);
	int c;
	while ((c = in.read()) != -1) out.write(c); 
	in.close();
	out.close();
}

/* IO utilities to parse Windows executable */
static final int IMAGE_DOS_SIGNATURE = 0x5a4d;
static final int IMAGE_NT_SIGNATURE = 0x00004550;
static final int IMAGE_DIRECTORY_ENTRY_RESOURCE = 2;
static final int RES_ICON = 1;
static final int RT_ICON = 3;
static final int RT_GROUP_ICON = 14;
static final int BMPHeaderFixedSize = 40;
	
public static class IMAGE_DOS_HEADER {
	int e_magic; // WORD
	int e_cblp; // WORD
	int e_cp; // WORD
	int e_crlc; // WORD
	int e_cparhdr; // WORD
	int e_minalloc; // WORD
	int e_maxalloc; // WORD
	int e_ss; // WORD
	int e_sp; // WORD
	int e_csum; // WORD
	int e_ip; // WORD
	int e_cs; // WORD
	int e_lfarlc; // WORD
	int e_ovno; // WORD
	int[] e_res = new int[4]; // WORD[4]
	int e_oemid; // WORD
	int e_oeminfo; // WORD
	int[] e_res2 = new int[10]; // WORD[10]
	int e_lfanew; // LONG
}

public static class IMAGE_FILE_HEADER {
	int Machine; // WORD
	int NumberOfSections; // WORD
	int TimeDateStamp; // DWORD
	int PointerToSymbolTable; // DWORD
	int NumberOfSymbols; // DWORD
	int SizeOfOptionalHeader; // WORD
	int Characteristics; // WORD
};

public static class IMAGE_DATA_DIRECTORY {
	int VirtualAddress; // DWORD
	int Size; // DWORD
}

public static class IMAGE_OPTIONAL_HEADER {
	int Magic; // WORD
	int MajorLinkerVersion; // BYTE
	int MinorLinkerVersion; // BYTE
	int SizeOfCode; // DWORD
	int SizeOfInitializedData; // DWORD
	int SizeOfUninitializedData; // DWORD
	int AddressOfEntryPoint; // DWORD
	int BaseOfCode; // DWORD
	int BaseOfData; // DWORD
	int ImageBase; // DWORD
	int SectionAlignment; // DWORD
	int FileAlignment; // DWORD
	int MajorOperatingSystemVersion; // WORD
	int MinorOperatingSystemVersion; // WORD
	int MajorImageVersion; // WORD
	int MinorImageVersion; // WORD
	int MajorSubsystemVersion; // WORD
	int MinorSubsystemVersion; // WORD
	int Win32VersionValue; // DWORD
	int SizeOfImage; // DWORD
	int SizeOfHeaders; // DWORD
	int CheckSum; // DWORD
	int Subsystem; // WORD
	int DllCharacteristics; // WORD
	int SizeOfStackReserve; // DWORD
	int SizeOfStackCommit; // DWORD
	int SizeOfHeapReserve; // DWORD
	int SizeOfHeapCommit; // DWORD
	int LoaderFlags; // DWORD
	int NumberOfRvaAndSizes; // DWORD
	IMAGE_DATA_DIRECTORY[] DataDirectory = new IMAGE_DATA_DIRECTORY[16];
}
public static class IMAGE_NT_HEADERS {
	int Signature; // DWORD
	IMAGE_FILE_HEADER FileHeader = new IMAGE_FILE_HEADER();
	IMAGE_OPTIONAL_HEADER OptionalHeader = new IMAGE_OPTIONAL_HEADER();
	final static int FIELD_OFFSET_OptionalHeader = 24;  
}
	
public static class IMAGE_SECTION_HEADER {
	int[] Name = new int[8]; // BYTE[8]
	int Misc_VirtualSize; // DWORD (union Misc { DWORD PhysicalAddress; DWORD VirtualSize }
	int VirtualAddress; // DWORD
	int SizeOfRawData; // DWORD
	int PointerToRawData; // DWORD
	int PointerToRelocations; // DWORD
	int PointerToLinenumbers; // DWORD
	int NumberOfRelocations; // WORD
	int NumberOfLinenumbers; // WORD
	int Characteristics; // DWORD
};

public static class IMAGE_RESOURCE_DIRECTORY {
	int Characteristics; // DWORD
	int TimeDateStamp; // DWORD
	int MajorVersion; // WORD
	int MinorVersion; // WORD
	int NumberOfNamedEntries; // WORD - used
	int NumberOfIdEntries; // WORD - used
	final static int SIZEOF = 16;
}

public static class IMAGE_RESOURCE_DIRECTORY_ENTRY {
	// union
	int NameOffset; // DWORD 31 bits
	boolean NameIsString; // DWORD 1 bit
	int Name; // DWORD
	int Id; // WORD
	// union
	int OffsetToData; // DWORD
	int OffsetToDirectory; // DWORD 31 bits
	boolean DataIsDirectory; // DWORD 1 bit
}

public static class IMAGE_RESOURCE_DATA_ENTRY {
	int OffsetToData; // DWORD
	int Size; // DWORD
	int CodePage; // DWORD
	int Reserved; // DWORD
}

public static class NEWHEADER {
	int Reserved; // WORD
	int ResType; // WORD
	int ResCount; // WORD
}

public static class ICONRESDIR {
	int Width; // BYTE
	int Height; // BYTE
	int ColorCount; // BYTE
	int reserved; // BYTE
}

public static class CURSORDIR {
	int Width; // WORD
	int Height; // WORD
}

public static class RESDIR {
	// union
	ICONRESDIR Icon = new ICONRESDIR();
	CURSORDIR Cursor = new CURSORDIR();
	int Planes; // WORD
	int BitCount; // WORD
	int BytesInRes; // DWORD
	int IconCursorId; // WORD
}

public static class BITMAPINFOHEADER {
	int biSize; // DWORD
	int biWidth; // LONG
	int biHeight; // LONG
	int biPlanes; // WORD
	int biBitCount; // WORD
	int biCompression; // DWORD
	int biSizeImage; // DWORD
	int biXPelsPerMeter; // LONG
	int biYPelsPerMeter; // LONG
	int biClrUsed; // DWORD
	int biClrImportant; // DWORD
}

public static class RGBQUAD {
	int rgBlue; // BYTE
	int rgbGreen; // BYTE
	int rgbRed; // BYTE
	int rgbReserved; // BYTE
}
public static class BITMAPINFO {
	BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER();
	RGBQUAD[] bmiColors = null;
}
static void read(RandomAccessFile raf, BITMAPINFOHEADER bih) throws IOException {
	bih.biSize = read4(raf);
	bih.biWidth = read4(raf);
	bih.biHeight = read4(raf);
	bih.biPlanes = readU2(raf);
	bih.biBitCount = readU2(raf);
	bih.biCompression = read4(raf);
	bih.biSizeImage = read4(raf);
	bih.biXPelsPerMeter = read4(raf);
	bih.biYPelsPerMeter = read4(raf);
	bih.biClrUsed = read4(raf);
	bih.biClrImportant = read4(raf);
}
static void read(RandomAccessFile raf, BITMAPINFO bi) throws IOException {
	read(raf, bi.bmiHeader);
	System.out.println("Reading BITMAPINFO");
}
/* Little Endian helpers */
static int readU2(RandomAccessFile raf) throws IOException {
	int b0 = raf.readByte() & 0xFF;
	int b1 = raf.readByte() & 0xFF;
	return (b1 << 8 | b0);
}
static int read4(RandomAccessFile raf) throws IOException {
	int b0 = raf.readByte() & 0xFF;;
	int b1 = raf.readByte() & 0xFF;;
	int b2 = raf.readByte() & 0xFF;;
	int b3 = raf.readByte() & 0xFF;;
	return b3 << 24 | b2 << 16 | b1 << 8 | b0;
}
static void write4(RandomAccessFile raf, int value) throws IOException {
	raf.write(value & 0xFF);
	raf.write((value >> 8) & 0xFF);
	raf.write((value >> 16) & 0xFF);
	raf.write((value >> 24) & 0xFF);
}
static void writeU2(RandomAccessFile raf, int value) throws IOException {
	raf.write(value & 0xFF);
	raf.write((value >> 8) & 0xFF);
}
static void read(RandomAccessFile raf, IMAGE_DOS_HEADER idh) throws IOException {
	idh.e_magic = readU2(raf);
	idh.e_cblp = readU2(raf);
	idh.e_cp = readU2(raf);
	idh.e_crlc = readU2(raf);
	idh.e_cparhdr = readU2(raf);
	idh.e_minalloc = readU2(raf);
	idh.e_maxalloc = readU2(raf);
	idh.e_ss = readU2(raf);
	idh.e_sp = readU2(raf);
	idh.e_csum = readU2(raf);
	idh.e_ip = readU2(raf);
	idh.e_cs = readU2(raf);
	idh.e_lfarlc = readU2(raf);
	idh.e_ovno = readU2(raf);
	for (int i = 0; i < idh.e_res.length; i++) idh.e_res[i] = readU2(raf);
	idh.e_oemid = readU2(raf);
	idh.e_oeminfo = readU2(raf);
	for (int i = 0; i < idh.e_res2.length; i++) idh.e_res2[i] = readU2(raf);
	idh.e_lfanew = read4(raf);
}
static void read(RandomAccessFile raf, IMAGE_FILE_HEADER ifh) throws IOException {
	ifh.Machine = readU2(raf);
	ifh.NumberOfSections = readU2(raf);
	ifh.TimeDateStamp = read4(raf);
	ifh.PointerToSymbolTable = read4(raf);
	ifh.NumberOfSymbols = read4(raf);
	ifh.SizeOfOptionalHeader = readU2(raf);
	ifh.Characteristics  = readU2(raf);
}
static void read(RandomAccessFile raf, IMAGE_DATA_DIRECTORY idd) throws IOException {
	idd.VirtualAddress = read4(raf);
	idd.Size = read4(raf);
}
static void read(RandomAccessFile raf, IMAGE_OPTIONAL_HEADER ioh) throws IOException {
	ioh.Magic = readU2(raf);
	ioh.MajorLinkerVersion = raf.read();
	ioh.MinorLinkerVersion = raf.read();
	ioh.SizeOfCode = read4(raf);
	ioh.SizeOfInitializedData = read4(raf);
	ioh.SizeOfUninitializedData = read4(raf);
	ioh.AddressOfEntryPoint = read4(raf);
	ioh.BaseOfCode = read4(raf);
	ioh.BaseOfData = read4(raf);
	ioh.ImageBase = read4(raf);
	ioh.SectionAlignment = read4(raf);
	ioh.FileAlignment = read4(raf);
	ioh.MajorOperatingSystemVersion = readU2(raf);
	ioh.MinorOperatingSystemVersion = readU2(raf);
	ioh.MajorImageVersion = readU2(raf);
	ioh.MinorImageVersion = readU2(raf);
	ioh.MajorSubsystemVersion = readU2(raf);
	ioh.MinorSubsystemVersion = readU2(raf);
	ioh.Win32VersionValue = read4(raf);
	ioh.SizeOfImage = read4(raf);
	ioh.SizeOfHeaders = read4(raf);
	ioh.CheckSum = read4(raf);
	ioh.Subsystem = readU2(raf);
	ioh.DllCharacteristics = readU2(raf);
	ioh.SizeOfStackReserve = read4(raf);
	ioh.SizeOfStackCommit = read4(raf);
	ioh.SizeOfHeapReserve = read4(raf);
	ioh.SizeOfHeapCommit = read4(raf);
	ioh.LoaderFlags = read4(raf);
	ioh.NumberOfRvaAndSizes = read4(raf);
	for (int i = 0 ; i < ioh.DataDirectory.length; i++) {
		ioh.DataDirectory[i] = new IMAGE_DATA_DIRECTORY();
		read(raf, ioh.DataDirectory[i]);
	}
}
static void read(RandomAccessFile raf, IMAGE_NT_HEADERS inh) throws IOException {
	inh.Signature = read4(raf);
	read(raf, inh.FileHeader);
	read(raf, inh.OptionalHeader);
}
static void read(RandomAccessFile raf, IMAGE_SECTION_HEADER ish) throws IOException {
	for (int i = 0 ; i < ish.Name.length; i++) ish.Name[i] = raf.read();
	ish.Misc_VirtualSize = read4(raf);
	ish.VirtualAddress = read4(raf);
	ish.SizeOfRawData = read4(raf);
	ish.PointerToRawData = read4(raf);
	ish.PointerToRelocations = read4(raf);
	ish.PointerToLinenumbers = read4(raf);
	ish.NumberOfRelocations = readU2(raf);
	ish.NumberOfLinenumbers = readU2(raf);
	ish.Characteristics = read4(raf);
}
static void read(RandomAccessFile raf, IMAGE_RESOURCE_DIRECTORY ird) throws IOException {
	ird.Characteristics = read4(raf);
	ird.TimeDateStamp = read4(raf);
	ird.MajorVersion = readU2(raf);
	ird.MinorVersion = readU2(raf);
	ird.NumberOfNamedEntries = readU2(raf);
	ird.NumberOfIdEntries = readU2(raf);
};
static void read(RandomAccessFile raf, IMAGE_RESOURCE_DIRECTORY_ENTRY irde) throws IOException {
	irde.Name = read4(raf);
	irde.OffsetToData = read4(raf);
	// construct other union members
	irde.NameOffset = irde.Name & ~ (1 << 31);
	irde.NameIsString = (irde.Name & (1 << 31)) != 0;
	irde.Id = irde.Name & 0xFFFF;
	irde.OffsetToDirectory = irde.OffsetToData & ~ (1 << 31);
	irde.DataIsDirectory = (irde.OffsetToData & (1 << 31)) != 0;
}
static void read(RandomAccessFile raf, IMAGE_RESOURCE_DATA_ENTRY irde) throws IOException {
	irde.OffsetToData = read4(raf);
	irde.Size = read4(raf);
	irde.CodePage = read4(raf);
	irde.Reserved = read4(raf);
}
static void read(RandomAccessFile raf, NEWHEADER nh) throws IOException {
	nh.Reserved = readU2(raf);
	nh.ResType = readU2(raf);
	nh.ResCount = readU2(raf);
}
static void read(RandomAccessFile raf, ICONRESDIR i) throws IOException {
	i.Width = raf.read();
	i.Height = raf.read();
	i.ColorCount = raf.read();
	i.reserved = raf.read();
}
static void read(RandomAccessFile raf, CURSORDIR c) throws IOException {
	c.Width = readU2(raf);
	c.Height = readU2(raf);
}
static void read(RandomAccessFile raf, RESDIR rs) throws IOException {
	long start = raf.getFilePointer();
	read(raf, rs.Icon);
	raf.seek(start);
	read(raf, rs.Cursor);
	rs.Planes = readU2(raf);
	rs.BitCount = readU2(raf);
	rs.BytesInRes = read4(raf);
	rs.IconCursorId = readU2(raf);
}
	
}
