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
import java.util.Vector;

/**
 * Customize the icon of a Windows exe
 * 
 * WARNING! This class is not part of SWT API. It is NOT API. It is an internal
 * tool that may be changed or removed at anytime.
 * 
 * Based on MSDN "An In-Depth Look into the Win32 Portable Executable File Format"
 */
public class IconExe {
	
	 /**
	 * Replace the Desktop icons provided in the Windows executable program
	 * with matching icons provided by the user.
	 *
 	 * Takes 2 arguments
	 * argument 0: the Windows executable e.g c:/eclipse/eclipse.exe
	 * argument 1: The .ico file to write to the given executable e.g. c:/myApp.ico
	 *
	 * Note 1. Write access to the executable program is required. As a result, that
	 * program must not be currently running or edited elsewhere.
	 * 
	 * Note 2.  The Eclipse 3.1 launcher requires a .ico file with the following 6 images
	 * 1. 32x32, 4 bit (Windows 16 colors palette)
	 * 2. 16x16, 4 bit (Windows 16 colors palette)
	 * 3. 16x16, 8 bit (256 colors)
	 * 4. 32x32, 8 bit (256 colors)
	 * 5. 48x48, 4 bit (Windows 16 colors palette)
	 * 6. 48x48, 8 bit (256 colors)
	 * A user icon matching exactly the width/height/depth of an executable icon will be written
	 * to the executable and will replace that executable icon. If an executable icon
	 * does not match a user icon, it is silently left as is.
	 * 
	 * Note 3. This function modifies the content of the executable program and may cause
	 * its corruption. 
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("Usage: IconExe <windows executable> <ico file>");
			return;
		}
		ImageLoader loader = new ImageLoader();
		try {
			ImageData[] data = loader.load(args[1]);
			int nMissing = unloadIcons(args[0], data);
			if (nMissing != 0) System.err.println("Error - "+nMissing+" icon(s) not replaced in "+args[0]+" using "+args[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* Implementation */

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
	static ImageData[] loadIcons(String program) throws FileNotFoundException, IOException {
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
	 * @param icons to write to the given executable
	 * @return the number of icons from the original program that were not successfully replaced (0 if success)
	 */	
	static int unloadIcons(String program, ImageData[] icons) throws FileNotFoundException, IOException {
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
		return iconInfo.length - cnt;
	}
	
	public static final String VERSION = "20050118";
	
	static final boolean DEBUG = false;
	public static class IconResInfo {
		ImageData data;
		int offset;
		int size;
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

/* ImageData and Image Decoder inlining to avoid dependency on SWT */
public static class RGB {
	
	/**
	 * the red component of the RGB
	 */
	public int red;
	
	/**
	 * the green component of the RGB
	 */
	public int green;
	
	/**
	 * the blue component of the RGB
	 */
	public int blue;
	
	static final long serialVersionUID = 3258415023461249074L;
	
/**
 * Constructs an instance of this class with the given
 * red, green and blue values.
 *
 * @param red the red component of the new instance
 * @param green the green component of the new instance
 * @param blue the blue component of the new instance
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the red, green or blue argument is not between 0 and 255</li>
 * </ul>
 */
public RGB(int red, int green, int blue) {
	if ((red > 255) || (red < 0) ||
		(green > 255) || (green < 0) ||
		(blue > 255) || (blue < 0))
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	this.red = red;
	this.green = green;
	this.blue = blue;
}

/**
 * Compares the argument to the receiver, and returns true
 * if they represent the <em>same</em> object using a class
 * specific comparison.
 *
 * @param object the object to compare with this object
 * @return <code>true</code> if the object is the same as this object and <code>false</code> otherwise
 *
 * @see #hashCode()
 */
public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof RGB)) return false;
	RGB rgb = (RGB)object;
	return (rgb.red == this.red) && (rgb.green == this.green) && (rgb.blue == this.blue);
}

/**
 * Returns an integer hash code for the receiver. Any two 
 * objects which return <code>true</code> when passed to 
 * <code>equals</code> must return the same value for this
 * method.
 *
 * @return the receiver's hash
 *
 * @see #equals(Object)
 */
public int hashCode () {
	return (blue << 16) | (green << 8) | red;
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the <code>RGB</code>
 */
public String toString () {
	return "RGB {" + red + ", " + green + ", " + blue + "}"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ 

//$NON-NLS-4$
}

}
public static class PaletteData {
	
	/**
	 * true if the receiver is a direct palette, 
	 * and false otherwise
	 */
	public boolean isDirect;
	
	/**
	 * the RGB values for an indexed palette, where the
	 * indices of the array correspond to pixel values
	 */
	public RGB[] colors;
	
	/**
	 * the red mask for a direct palette
	 */
	public int redMask;
	
	/**
	 * the green mask for a direct palette
	 */
	public int greenMask;
	
	/**
	 * the blue mask for a direct palette
	 */
	public int blueMask;
	
	/**
	 * the red shift for a direct palette
	 */
	public int redShift;
	
	/**
	 * the green shift for a direct palette
	 */
	public int greenShift;
	
	/**
	 * the blue shift for a direct palette
	 */
	public int blueShift;

/**
 * Constructs a new indexed palette given an array of RGB values.
 *
 * @param colors the array of <code>RGB</code>s for the palette
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 */
public PaletteData(RGB[] colors) {
	if (colors == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.colors = colors;
	this.isDirect = false;
}

/**
 * Constructs a new direct palette given the red, green and blue masks.
 *
 * @param redMask the red mask
 * @param greenMask the green mask
 * @param blueMask the blue mask
 */
public PaletteData(int redMask, int greenMask, int blueMask) {
	this.redMask = redMask;
	this.greenMask = greenMask;
	this.blueMask = blueMask;
	this.isDirect = true;
	this.redShift = shiftForMask(redMask);
	this.greenShift = shiftForMask(greenMask);
	this.blueShift = shiftForMask(blueMask);
}

/**
 * Returns the pixel value corresponding to the given <code>RBG</code>.
 *
 * @param rgb the RGB to get the pixel value for
 * @return the pixel value for the given RGB
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the RGB is not found in the palette</li>
 * </ul>
 */
public int getPixel(RGB rgb) {
	if (rgb == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (isDirect) {
		int pixel = 0;
		pixel |= (redShift < 0 ? rgb.red << -redShift : rgb.red >>> redShift) & redMask;
		pixel |= (greenShift < 0 ? rgb.green << -greenShift : rgb.green >>> greenShift) & greenMask;
		pixel |= (blueShift < 0 ? rgb.blue << -blueShift : rgb.blue >>> blueShift) & blueMask;
		return pixel;
	} else {
		for (int i = 0; i < colors.length; i++) {
			if (colors[i].equals(rgb)) return i;
		}
		/* The RGB did not exist in the palette */
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		return 0;
	}
}

/**
 * Returns an <code>RGB</code> corresponding to the given pixel value.
 *
 * @param pixel the pixel to get the RGB value for
 * @return the RGB value for the given pixel
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the pixel does not exist in the palette</li>
 * </ul>
 */
public RGB getRGB(int pixel) {
	if (isDirect) {
		int r = pixel & redMask;
		r = (redShift < 0) ? r >>> -redShift : r << redShift;
		int g = pixel & greenMask;
		g = (greenShift < 0) ? g >>> -greenShift : g << greenShift;
		int b = pixel & blueMask;
		b = (blueShift < 0) ? b >>> -blueShift : b << blueShift;
		return new RGB(r, g, b);
	} else {
		if (pixel < 0 || pixel >= colors.length) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		return colors[pixel];
	}
}

/**
 * Returns all the RGB values in the receiver if it is an
 * indexed palette, or null if it is a direct palette.
 *
 * @return the <code>RGB</code>s for the receiver or null
 */
public RGB[] getRGBs() {
	return colors;
}

/**
 * Computes the shift value for a given mask.
 *
 * @param mask the mask to compute the shift for
 * @return the shift amount
 *
 * @see PaletteData
 */
int shiftForMask(int mask) {
	for (int i = 31; i >= 0; i--) { 
		if (((mask >> i) & 0x1) != 0) return 7 - i;
	}
	return 32;
}

}
public static class ImageLoader {
	
	/**
	 * the array of ImageData objects in this ImageLoader.
	 * This array is read in when the load method is called,
	 * and it is written out when the save method is called
	 */
	public ImageData[] data;
	
	/**
	 * the width of the logical screen on which the images
	 * reside, in pixels (this corresponds to the GIF89a
	 * Logical Screen Width value)
	 */
	public int logicalScreenWidth;

	/**
	 * the height of the logical screen on which the images
	 * reside, in pixels (this corresponds to the GIF89a
	 * Logical Screen Height value)
	 */
	public int logicalScreenHeight;

	/**
	 * the background pixel for the logical screen (this 
	 * corresponds to the GIF89a Background Color Index value).
	 * The default is -1 which means 'unspecified background'
	 * 
	 */
	public int backgroundPixel;

	/**
	 * the number of times to repeat the display of a sequence
	 * of animated images (this corresponds to the commonly-used
	 * GIF application extension for "NETSCAPE 2.0 01")
	 */
	public int repeatCount;
		
	/*
	 * the set of ImageLoader event listeners, created on demand
	 */
	Vector imageLoaderListeners;

/**
 * Construct a new empty ImageLoader.
 */
public ImageLoader() {
	reset();
}

/**
 * Resets the fields of the ImageLoader, except for the
 * <code>imageLoaderListeners</code> field.
 */
void reset() {
	data = null;
	logicalScreenWidth = 0;
	logicalScreenHeight = 0;
	backgroundPixel = -1;
	repeatCount = 1;
}

/**
 * Loads an array of <code>ImageData</code> objects from the
 * specified input stream. Throws an error if either an error
 * occurs while loading the images, or if the images are not
 * of a supported type. Returns the loaded image data array.
 *
 * @param stream the input stream to load the images from
 * @return an array of <code>ImageData</code> objects loaded from the specified input stream
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the stream is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_INVALID_IMAGE - if the image file contains invalid data</li>
 *    <li>ERROR_IO - if an input/output error occurs while reading data</li>
 *    <li>ERROR_UNSUPPORTED_FORMAT - if the image file contains an unrecognized format</li>
 * </ul>
 */
public ImageData[] load(InputStream stream) {
	if (stream == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	reset();
	data = FileFormat.load(stream, this);
	return data;
}

/**
 * Loads an array of <code>ImageData</code> objects from the
 * file with the specified name. Throws an error if either
 * an error occurs while loading the images, or if the images are
 * not of a supported type. Returns the loaded image data array.
 *
 * @param filename the name of the file to load the images from
 * @return an array of <code>ImageData</code> objects loaded from the specified file
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the file name is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_INVALID_IMAGE - if the image file contains invalid data</li>
 *    <li>ERROR_IO - if an IO error occurs while reading data</li>
 *    <li>ERROR_UNSUPPORTED_FORMAT - if the image file contains an unrecognized format</li>
 * </ul>
 */
public ImageData[] load(String filename) {
	if (filename == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	InputStream stream = null;
	try {
		stream = new FileInputStream(filename);
		return load(stream);
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	} finally {
		try {
			if (stream != null) stream.close();
		} catch (IOException e) {
			// Ignore error
		}
	}
	return null;
}

/**	 
 * Returns <code>true</code> if the receiver has image loader
 * listeners, and <code>false</code> otherwise.
 *
 * @return <code>true</code> if there are <code>ImageLoaderListener</code>s, and <code>false</code> otherwise
 *
 * @see #addImageLoaderListener(ImageLoaderListener)
 * @see #removeImageLoaderListener(ImageLoaderListener)
 */
public boolean hasListeners() {
	return imageLoaderListeners != null && imageLoaderListeners.size() > 0;
}
}
public static class ImageData {
	
	/**
	 * The width of the image, in pixels.
	 */
	public int width;

	/**
	 * The height of the image, in pixels.
	 */
	public int height;

	/**
	 * The color depth of the image, in bits per pixel.
	 * <p>
	 * Note that a depth of 8 or less does not necessarily
	 * mean that the image is palette indexed, or
	 * conversely that a depth greater than 8 means that
	 * the image is direct color.  Check the associated
	 * PaletteData's isDirect field for such determinations.
	 */
	public int depth;

	/**
	 * The scanline padding.
	 * <p>
	 * If one scanline of the image is not a multiple of
	 * this number, it will be padded with zeros until it is.
	 * </p>
	 */
	public int scanlinePad;

	/**
	 * The number of bytes per scanline.
	 * <p>
	 * This is a multiple of the scanline padding.
	 * </p>
	 */
	public int bytesPerLine;

	/**
	 * The pixel data of the image.
	 * <p>
	 * Note that for 16 bit depth images the pixel data is stored
	 * in least significant byte order; however, for 24bit and
	 * 32bit depth images the pixel data is stored in most
	 * significant byte order.
	 * </p>
	 */
	public byte[] data;

	/**
	 * The color table for the image.
	 */
	public PaletteData palette;

	/**
	 * The transparent pixel.
	 * <p>
	 * Pixels with this value are transparent.
	 * </p><p>
	 * The default is -1 which means 'no transparent pixel'.
	 * </p>
	 */
	public int transparentPixel;

	/**
	 * An icon-specific field containing the data from the icon mask.
	 * <p>
	 * This is a 1 bit bitmap stored with the most significant
	 * bit first.  The number of bytes per scanline is
	 * '((width + 7) / 8 + (maskPad - 1)) / maskPad * maskPad'.
	 * </p><p>
	 * The default is null which means 'no transparency mask'.
	 * </p>
	 */
	public byte[] maskData;

	/**
	 * An icon-specific field containing the scanline pad of the mask.
	 * <p>
	 * If one scanline of the transparency mask is not a
	 * multiple of this number, it will be padded with zeros until
	 * it is.
	 * </p>
	 */
	public int maskPad;
	
	/**
	 * The alpha data of the image.
	 * <p>
	 * Every pixel can have an <em>alpha blending</em> value that
	 * varies from 0, meaning fully transparent, to 255 meaning
	 * fully opaque.  The number of bytes per scanline is
	 * 'width'.
	 * </p>
	 */
	public byte[] alphaData;
	
	/**
	 * The global alpha value to be used for every pixel.
	 * <p>
	 * If this value is set, the <code>alphaData</code> field
	 * is ignored and when the image is rendered each pixel
	 * will be blended with the background an amount
	 * proportional to this value.
	 * </p><p>
	 * The default is -1 which means 'no global alpha value'
	 * </p>
	 */
	public int alpha;

	/**
	 * The type of file from which the image was read.
	 * 
	 * It is expressed as one of the following values:
	 * <dl>
	 * <dt><code>IMAGE_BMP</code></dt>
	 * <dd>Windows BMP file format, no compression</dd>
	 * <dt><code>IMAGE_BMP_RLE</code></dt>
	 * <dd>Windows BMP file format, RLE compression if appropriate</dd>
	 * <dt><code>IMAGE_GIF</code></dt>
	 * <dd>GIF file format</dd>
	 * <dt><code>IMAGE_ICO</code></dt>
	 * <dd>Windows ICO file format</dd>
	 * <dt><code>IMAGE_JPEG</code></dt>
	 * <dd>JPEG file format</dd>
	 * <dt><code>IMAGE_PNG</code></dt>
	 * <dd>PNG file format</dd>
	 * </dl>
	 */
	public int type;

	/**
	 * The x coordinate of the top left corner of the image
	 * within the logical screen (this field corresponds to
	 * the GIF89a Image Left Position value).
	 */
	public int x;

	/**
	 * The y coordinate of the top left corner of the image
	 * within the logical screen (this field corresponds to
	 * the GIF89a Image Top Position value).
	 */
	public int y;

	/**
	 * A description of how to dispose of the current image
	 * before displaying the next.
	 * 
	 * It is expressed as one of the following values:
	 * <dl>
	 * <dt><code>DM_UNSPECIFIED</code></dt>
	 * <dd>disposal method not specified</dd>
	 * <dt><code>DM_FILL_NONE</code></dt>
	 * <dd>do nothing - leave the image in place</dd>
	 * <dt><code>DM_FILL_BACKGROUND</code></dt>
	 * <dd>fill with the background color</dd>
	 * <dt><code>DM_FILL_PREVIOUS</code></dt>
	 * <dd>restore the previous picture</dd>
	 * </dl>
	 * (this field corresponds to the GIF89a Disposal Method value)
	 */
	public int disposalMethod;

	/**
	 * The time to delay before displaying the next image
	 * in an animation (this field corresponds to the GIF89a
	 * Delay Time value).
	 */
	public int delayTime;

	/**
	 * Arbitrary channel width data to 8-bit conversion table.
	 */
	static final byte[][] ANY_TO_EIGHT = new byte[9][];
	static {
		for (int b = 0; b < 9; ++b) {
			byte[] data = ANY_TO_EIGHT[b] = new byte[1 << b];
			if (b == 0) continue;
			int inc = 0;
			for (int bit = 0x10000; (bit >>= b) != 0;) inc |= bit;
			for (int v = 0, p = 0; v < 0x10000; v+= inc) data[p++] = (byte)(v >> 8);
		}
	}
	static final byte[] ONE_TO_ONE_MAPPING = ANY_TO_EIGHT[8];

	/**
	 * Scaled 8x8 Bayer dither matrix.
	 */
	static final int[][] DITHER_MATRIX = {
		{ 0xfc0000, 0x7c0000, 0xdc0000, 0x5c0000, 0xf40000, 0x740000, 0xd40000, 0x540000 },
		{ 0x3c0000, 0xbc0000, 0x1c0000, 0x9c0000, 0x340000, 0xb40000, 0x140000, 0x940000 },
		{ 0xcc0000, 0x4c0000, 0xec0000, 0x6c0000, 0xc40000, 0x440000, 0xe40000, 0x640000 },
		{ 0x0c0000, 0x8c0000, 0x2c0000, 0xac0000, 0x040000, 0x840000, 0x240000, 0xa40000 },
		{ 0xf00000, 0x700000, 0xd00000, 0x500000, 0xf80000, 0x780000, 0xd80000, 0x580000 },
		{ 0x300000, 0xb00000, 0x100000, 0x900000, 0x380000, 0xb80000, 0x180000, 0x980000 },
		{ 0xc00000, 0x400000, 0xe00000, 0x600000, 0xc80000, 0x480000, 0xe80000, 0x680000 },
		{ 0x000000, 0x800000, 0x200000, 0xa00000, 0x080000, 0x880000, 0x280000, 0xa80000 }
	};

/**
 * Constructs a new, empty ImageData with the given width, height,
 * depth and palette. The data will be initialized to an (all zero)
 * array of the appropriate size.
 *
 * @param width the width of the image
 * @param height the height of the image
 * @param depth the depth of the image
 * @param palette the palette of the image (must not be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the width or height is negative, or if the depth is not
 *        	one of 1, 2, 4, 8, 16, 24 or 32</li>
 *    <li>ERROR_NULL_ARGUMENT - if the palette is null</li>
 * </ul>
 */
public ImageData(int width, int height, int depth, PaletteData palette) {
	this(width, height, depth, palette,
		4, null, 0, null,
		null, -1, -1, SWT.IMAGE_UNDEFINED,
		0, 0, 0, 0);
}

/**
 * Constructs a new, empty ImageData with the given width, height,
 * depth, palette, scanlinePad and data.
 *
 * @param width the width of the image
 * @param height the height of the image
 * @param depth the depth of the image
 * @param palette the palette of the image
 * @param scanlinePad the padding of each line, in bytes
 * @param data the data of the image
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the width or height is negative, or if the depth is not
 *        	one of 1, 2, 4, 8, 16, 24 or 32</li>
 *    <li>ERROR_NULL_ARGUMENT - if the palette or data is null</li>
 *    <li>ERROR_CANNOT_BE_ZERO - if the scanlinePad is zero</li>
 * </ul>
 */
public ImageData(int width, int height, int depth, PaletteData palette, int scanlinePad, byte[] data) {
	this(width, height, depth, palette,
		scanlinePad, checkData(data), 0, null,
		null, -1, -1, SWT.IMAGE_UNDEFINED,
		0, 0, 0, 0);
}



/**
 * Constructs an <code>ImageData</code> loaded from a file with the
 * specified name. Throws an error if an error occurs loading the
 * image, or if the image has an unsupported type.
 * <p>
 * This constructor is provided for convenience when loading a single
 * image only. If the file contains multiple images, only the first
 * one will be loaded. To load multiple images, use 
 * <code>ImageLoader.load()</code>.
 * </p>
 *
 * @param filename the name of the file to load the image from (must not be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the file name is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_INVALID_IMAGE - if the image file contains invalid data</li>
 *    <li>ERROR_IO if an IO error occurs while reading data</li>
 *    <li>ERROR_UNSUPPORTED_FORMAT - if the image file contains an unrecognized format</li>
 * </ul>
 */
public ImageData(String filename) {
	ImageData[] data = new ImageLoader().load(filename);
	if (data.length < 1) SWT.error(SWT.ERROR_INVALID_IMAGE);
	ImageData i = data[0];
	setAllFields(
		i.width,
		i.height,
		i.depth,
		i.scanlinePad,
		i.bytesPerLine,
		i.data,
		i.palette,
		i.transparentPixel,
		i.maskData,
		i.maskPad,
		i.alphaData,
		i.alpha,
		i.type,
		i.x,
		i.y,
		i.disposalMethod,
		i.delayTime);
}

/**
 * Prevents uninitialized instances from being created outside the package.
 */
ImageData() {
}

/**
 * Constructs an image data by giving values for all non-computable fields.
 * <p>
 * This method is for internal use, and is not described further.
 * </p>
 */
ImageData(
	int width, int height, int depth, PaletteData palette,
	int scanlinePad, byte[] data, int maskPad, byte[] maskData,
	byte[] alphaData, int alpha, int transparentPixel, int type,
	int x, int y, int disposalMethod, int delayTime)
{

	if (palette == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (!(depth == 1 || depth == 2 || depth == 4 || depth == 8
		|| depth == 16 || depth == 24 || depth == 32)) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (width <= 0 || height <= 0) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (scanlinePad == 0) SWT.error (SWT.ERROR_CANNOT_BE_ZERO);

	int bytesPerLine = (((width * depth + 7) / 8) + (scanlinePad - 1))
		/ scanlinePad * scanlinePad;
	setAllFields(
		width,
		height,
		depth,
		scanlinePad,
		bytesPerLine,
		data != null ? data : new byte[bytesPerLine * height],
		palette,
		transparentPixel,
		maskData,
		maskPad,
		alphaData,
		alpha,
		type,
		x,
		y,
		disposalMethod,
		delayTime);
}

/**
 * Initializes all fields in the receiver. This method must be called
 * by all public constructors to ensure that all fields are initialized
 * for a new ImageData object. If a new field is added to the class,
 * then it must be added to this method.
 * <p>
 * This method is for internal use, and is not described further.
 * </p>
 */
void setAllFields(int width, int height, int depth, int scanlinePad,
	int bytesPerLine, byte[] data, PaletteData palette, int transparentPixel,
	byte[] maskData, int maskPad, byte[] alphaData, int alpha,
	int type, int x, int y, int disposalMethod, int delayTime) {

	this.width = width;
	this.height = height;
	this.depth = depth;
	this.scanlinePad = scanlinePad;
	this.bytesPerLine = bytesPerLine;
	this.data = data;
	this.palette = palette;
	this.transparentPixel = transparentPixel;
	this.maskData = maskData;
	this.maskPad = maskPad;
	this.alphaData = alphaData;
	this.alpha = alpha;
	this.type = type;
	this.x = x;
	this.y = y;
	this.disposalMethod = disposalMethod;
	this.delayTime = delayTime;
}

/**	 
 * Invokes internal SWT functionality to create a new instance of
 * this class.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>ImageData</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is subject
 * to change without notice, and should never be called from
 * application code.
 * </p>
 * <p>
 * This method is for internal use, and is not described further.
 * </p>
 */
public static ImageData internal_new(
	int width, int height, int depth, PaletteData palette,
	int scanlinePad, byte[] data, int maskPad, byte[] maskData,
	byte[] alphaData, int alpha, int transparentPixel, int type,
	int x, int y, int disposalMethod, int delayTime)
{
	return new ImageData(
		width, height, depth, palette, scanlinePad, data, maskPad, maskData,
		alphaData, alpha, transparentPixel, type, x, y, disposalMethod, delayTime);
}

ImageData colorMaskImage(int pixel) {
	ImageData mask = new ImageData(width, height, 1, bwPalette(),
		2, null, 0, null, null, -1, -1, SWT.IMAGE_UNDEFINED,
		0, 0, 0, 0);
	int[] row = new int[width];
	for (int y = 0; y < height; y++) {
		getPixels(0, y, width, row, 0);
		for (int i = 0; i < width; i++) {
			if (pixel != -1 && row[i] == pixel) {
				row[i] = 0;
			} else {
				row[i] = 1;
			}
		}
		mask.setPixels(0, y, width, row, 0);
	}
	return mask;
}

static byte[] checkData(byte [] data) {
	if (data == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return data;
}

/**
 * Returns a new instance of the same class as the receiver,
 * whose slots have been filled in with <em>copies</em> of
 * the values in the slots of the receiver. That is, the
 * returned object is a <em>deep copy</em> of the receiver.
 *
 * @return a copy of the receiver.
 */
public Object clone() {
	byte[] cloneData = new byte[data.length];
	System.arraycopy(data, 0, cloneData, 0, data.length);
	byte[] cloneMaskData = null;
	if (maskData != null) {
		cloneMaskData = new byte[maskData.length];
		System.arraycopy(maskData, 0, cloneMaskData, 0, maskData.length);
	}
	byte[] cloneAlphaData = null;
	if (alphaData != null) {
		cloneAlphaData = new byte[alphaData.length];
		System.arraycopy(alphaData, 0, cloneAlphaData, 0, alphaData.length);
	}
	return new ImageData(
		width,
		height,
		depth,
		palette,
		scanlinePad,
		cloneData,
		maskPad,
		cloneMaskData,
		cloneAlphaData,
		alpha,
		transparentPixel,
		type,
		x,
		y,
		disposalMethod,
		delayTime);
}

/**
 * Returns the alpha value at offset <code>x</code> in
 * scanline <code>y</code> in the receiver's alpha data.
 *
 * @param x the x coodinate of the pixel to get the alpha value of
 * @param y the y coordinate of the pixel to get the alpha value of
 * @return the alpha value at the given coordinates
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if either argument is out of range</li>
 * </ul>
 */
public int getAlpha(int x, int y) {
	if (x >= width || y >= height || x < 0 || y < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);

	if (alphaData == null) return 255;
	return alphaData[y * width + x] & 0xFF;
}

/**
 * Returns <code>getWidth</code> alpha values starting at offset
 * <code>x</code> in scanline <code>y</code> in the receiver's alpha
 * data starting at <code>startIndex</code>.
 *
 * @param x the x position of the pixel to begin getting alpha values
 * @param y the y position of the pixel to begin getting alpha values
 * @param getWidth the width of the data to get
 * @param alphas the buffer in which to put the alpha values
 * @param startIndex the offset into the image to begin getting alpha values
 *
 * @exception IndexOutOfBoundsException if getWidth is too large
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if pixels is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if x or y is out of bounds</li>
 *    <li>ERROR_INVALID_ARGUMENT - if getWidth is negative</li>
 * </ul>
 */
public void getAlphas(int x, int y, int getWidth, byte[] alphas, int startIndex) {
	if (alphas == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (getWidth < 0 || x >= width || y >= height || x < 0 || y < 0) SWT.error

(SWT.ERROR_INVALID_ARGUMENT);
	if (getWidth == 0) return;

	if (alphaData == null) {
		int endIndex = startIndex + getWidth;
		for (int i = startIndex; i < endIndex; i++) {
			alphas[i] = (byte)255;
		}
		return;
	}
	// may throw an IndexOutOfBoundsException
	System.arraycopy(alphaData, y * width + x, alphas, startIndex, getWidth);
}

/**
 * Returns the pixel value at offset <code>x</code> in
 * scanline <code>y</code> in the receiver's data.
 *
 * @param x the x position of the pixel to get
 * @param y the y position of the pixel to get
 * @return the pixel at the given coordinates
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if either argument is out of bounds</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_UNSUPPORTED_DEPTH if the depth is not one of 1, 2, 4, 8, 16, 24 or 32</li>
 * </ul>
 */
public int getPixel(int x, int y) {
	if (x >= width || y >= height || x < 0 || y < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int index;
	int theByte;
	int mask;
	if (depth == 1) {
		index = (y * bytesPerLine) + (x >> 3);
		theByte = data[index] & 0xFF;
		mask = 1 << (7 - (x & 0x7));
		if ((theByte & mask) == 0) {
			return 0;
		} else {
			return 1;
		}
	}
	if (depth == 2) {
		index = (y * bytesPerLine) + (x >> 2);
		theByte = data[index] & 0xFF;
		int offset = 3 - (x % 4);
		mask = 3 << (offset * 2);
		return (theByte & mask) >> (offset * 2);
	}
	if (depth == 4) {
		index = (y * bytesPerLine) + (x >> 1);
		theByte = data[index] & 0xFF;
		if ((x & 0x1) == 0) {
			return theByte >> 4;
		} else {
			return theByte & 0x0F;
		}
	}
	if (depth == 8) {
		index = (y * bytesPerLine) + x ;
		return data[index] & 0xFF;
	}
	if (depth == 16) {
		index = (y * bytesPerLine) + (x * 2);
		return ((data[index+1] & 0xFF) << 8) + (data[index] & 0xFF);
	}
	if (depth == 24) {
		index = (y * bytesPerLine) + (x * 3);
		return ((data[index] & 0xFF) << 16) + ((data[index+1] & 0xFF) << 8) +
			(data[index+2] & 0xFF);
	}
	if (depth == 32) {
		index = (y * bytesPerLine) + (x * 4);
		return ((data[index] & 0xFF) << 24) + ((data[index+1] & 0xFF) << 16) +
				((data[index+2] & 0xFF) << 8) + (data[index+3] & 0xFF);
	}
	SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
	return 0;
}

/**
 * Returns <code>getWidth</code> pixel values starting at offset
 * <code>x</code> in scanline <code>y</code> in the receiver's
 * data starting at <code>startIndex</code>.
 *
 * @param x the x position of the first pixel to get
 * @param y the y position of the first pixel to get
 * @param getWidth the width of the data to get
 * @param pixels the buffer in which to put the pixels
 * @param startIndex the offset into the byte array to begin storing pixels
 *
 * @exception IndexOutOfBoundsException if getWidth is too large
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if pixels is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if x or y is out of bounds</li>
 *    <li>ERROR_INVALID_ARGUMENT - if getWidth is negative</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_UNSUPPORTED_DEPTH - if the depth is not one of 1, 2, 4 or 8
 *        (For higher depths, use the int[] version of this method.)</li>
 * </ul>
 */
public void getPixels(int x, int y, int getWidth, byte[] pixels, int startIndex) {
	if (pixels == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (getWidth < 0 || x >= width || y >= height || x < 0 || y < 0) SWT.error

(SWT.ERROR_INVALID_ARGUMENT);
	if (getWidth == 0) return;
	int index;
	int theByte;
	int mask = 0;
	int n = getWidth;
	int i = startIndex;
	int srcX = x, srcY = y;
	if (depth == 1) {
		index = (y * bytesPerLine) + (x >> 3);
		theByte = data[index] & 0xFF;
		while (n > 0) {
			mask = 1 << (7 - (srcX & 0x7));
			if ((theByte & mask) == 0) {
				pixels[i] = 0;
			} else {
				pixels[i] = 1;
			}
			i++;
			n--;
			srcX++;
			if (srcX >= width) {
				srcY++;
				index = srcY * bytesPerLine;
				if (n > 0) theByte = data[index] & 0xFF;
				srcX = 0;
			} else {
				if (mask == 1) {
					index++;
					if (n > 0) theByte = data[index] & 0xFF;
				}
			}
		}
		return;
	}
	if (depth == 2) {
		index = (y * bytesPerLine) + (x >> 2);
		theByte = data[index] & 0xFF;
		int offset;
		while (n > 0) {
			offset = 3 - (srcX % 4);
			mask = 3 << (offset * 2);
			pixels[i] = (byte)((theByte & mask) >> (offset * 2));
			i++;
			n--;
			srcX++;
			if (srcX >= width) {
				srcY++;
				index = srcY * bytesPerLine;
				if (n > 0) theByte = data[index] & 0xFF;
				srcX = 0;
			} else {
				if (offset == 0) {
					index++;
					theByte = data[index] & 0xFF;
				}
			}
		}
		return;
	}
	if (depth == 4) {
		index = (y * bytesPerLine) + (x >> 1);
		if ((x & 0x1) == 1) {
			theByte = data[index] & 0xFF;
			pixels[i] = (byte)(theByte & 0x0F);
			i++;
			n--;
			srcX++;
			if (srcX >= width) {
				srcY++;
				index = srcY * bytesPerLine;
				srcX = 0;
			} else {
				index++;
			}
		}
		while (n > 1) {
			theByte = data[index] & 0xFF;
			pixels[i] = (byte)(theByte >> 4);
			i++;
			n--;
			srcX++;
			if (srcX >= width) {
				srcY++;
				index = srcY * bytesPerLine;
				srcX = 0;
			} else {
				pixels[i] = (byte)(theByte & 0x0F);
				i++;
				n--;
				srcX++;
				if (srcX >= width) {
					srcY++;
					index = srcY * bytesPerLine;
					srcX = 0;
				} else {
					index++;
				}
			}
		}
		if (n > 0) {
			theByte = data[index] & 0xFF;
			pixels[i] = (byte)(theByte >> 4);
		}
		return;
	}
	if (depth == 8) {
		index = (y * bytesPerLine) + x;
		for (int j = 0; j < getWidth; j++) {
			pixels[i] = data[index];
			i++;
			srcX++;
			if (srcX >= width) {
				srcY++;
				index = srcY * bytesPerLine;
				srcX = 0;
			} else {
				index++;
			}
		}
		return;
	}
	SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
}

/**
 * Returns <code>getWidth</code> pixel values starting at offset
 * <code>x</code> in scanline <code>y</code> in the receiver's
 * data starting at <code>startIndex</code>.
 *
 * @param x the x position of the first pixel to get
 * @param y the y position of the first pixel to get
 * @param getWidth the width of the data to get
 * @param pixels the buffer in which to put the pixels
 * @param startIndex the offset into the buffer to begin storing pixels
 *
 * @exception IndexOutOfBoundsException if getWidth is too large
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if pixels is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if x or y is out of bounds</li>
 *    <li>ERROR_INVALID_ARGUMENT - if getWidth is negative</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_UNSUPPORTED_DEPTH - if the depth is not one of 1, 2, 4, 8, 16, 24 or 32</li>
 * </ul>
 */
public void getPixels(int x, int y, int getWidth, int[] pixels, int startIndex) {
	if (pixels == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (getWidth < 0 || x >= width || y >= height || x < 0 || y < 0) SWT.error

(SWT.ERROR_INVALID_ARGUMENT);
	if (getWidth == 0) return;
	int index;
	int theByte;
	int mask;
	int n = getWidth;
	int i = startIndex;
	int srcX = x, srcY = y;
	if (depth == 1) {
		index = (y * bytesPerLine) + (x >> 3);
		theByte = data[index] & 0xFF;
		while (n > 0) {
			mask = 1 << (7 - (srcX & 0x7));
			if ((theByte & mask) == 0) {
				pixels[i] = 0;
			} else {
				pixels[i] = 1;
			}
			i++;
			n--;
			srcX++;
			if (srcX >= width) {
				srcY++;
				index = srcY * bytesPerLine;
				if (n > 0) theByte = data[index] & 0xFF;
				srcX = 0;
			} else {
				if (mask == 1) {
					index++;
					if (n > 0) theByte = data[index] & 0xFF;
				}
			}
		}
		return;
	}		
	if (depth == 2) {
		index = (y * bytesPerLine) + (x >> 2);
		theByte = data[index] & 0xFF;
		int offset;
		while (n > 0) {
			offset = 3 - (srcX % 4);
			mask = 3 << (offset * 2);
			pixels[i] = (byte)((theByte & mask) >> (offset * 2));
			i++;
			n--;
			srcX++;
			if (srcX >= width) {
				srcY++;
				index = srcY * bytesPerLine;
				if (n > 0) theByte = data[index] & 0xFF;
				srcX = 0;
			} else {
				if (offset == 0) {
					index++;
					theByte = data[index] & 0xFF;
				}
			}
		}
		return;
	}
	if (depth == 4) {
		index = (y * bytesPerLine) + (x >> 1);
		if ((x & 0x1) == 1) {
			theByte = data[index] & 0xFF;
			pixels[i] = theByte & 0x0F;
			i++;
			n--;
			srcX++;
			if (srcX >= width) {
				srcY++;
				index = srcY * bytesPerLine;
				srcX = 0;
			} else {
				index++;
			}
		}
		while (n > 1) {
			theByte = data[index] & 0xFF;
			pixels[i] = theByte >> 4;
			i++;
			n--;
			srcX++;
			if (srcX >= width) {
				srcY++;
				index = srcY * bytesPerLine;
				srcX = 0;
			} else {
				pixels[i] = theByte & 0x0F;
				i++;
				n--;
				srcX++;
				if (srcX >= width) {
					srcY++;
					index = srcY * bytesPerLine;
					srcX = 0;
				} else {
					index++;
				}
			}
		}
		if (n > 0) {
			theByte = data[index] & 0xFF;
			pixels[i] = theByte >> 4;
		}
		return;
	}
	if (depth == 8) {
		index = (y * bytesPerLine) + x;
		for (int j = 0; j < getWidth; j++) {
			pixels[i] = data[index] & 0xFF;
			i++;
			srcX++;
			if (srcX >= width) {
				srcY++;
				index = srcY * bytesPerLine;
				srcX = 0;
			} else {
				index++;
			}
		}
		return;
	}
	if (depth == 16) {
		index = (y * bytesPerLine) + (x * 2);
		for (int j = 0; j < getWidth; j++) {
			pixels[i] = ((data[index+1] & 0xFF) << 8) + (data[index] & 0xFF);
			i++;
			srcX++;
			if (srcX >= width) {
				srcY++;
				index = srcY * bytesPerLine;
				srcX = 0;
			} else {
				index += 2;
			}
		}
		return;
	}
	if (depth == 24) {
		index = (y * bytesPerLine) + (x * 3);
		for (int j = 0; j < getWidth; j++) {
			pixels[i] = ((data[index] & 0xFF) << 16) | ((data[index+1] & 0xFF) << 8)
				| (data[index+2] & 0xFF);
			i++;
			srcX++;
			if (srcX >= width) {
				srcY++;
				index = srcY * bytesPerLine;
				srcX = 0;
			} else {
				index += 3;
			}
		}
		return;
	}
	if (depth == 32) {
		index = (y * bytesPerLine) + (x * 4);
		i = startIndex;
		for (int j = 0; j < getWidth; j++) {
			pixels[i] = ((data[index] & 0xFF) << 24) | ((data[index+1] & 0xFF) << 16)
				| ((data[index+2] & 0xFF) << 8) | (data[index+3] & 0xFF);
			i++;
			srcX++;
			if (srcX >= width) {
				srcY++;
				index = srcY * bytesPerLine;
				srcX = 0;
			} else {
				index += 4;
			}
		}
		return;
	}
	SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
}

/**
 * Returns an array of <code>RGB</code>s which comprise the
 * indexed color table of the receiver, or null if the receiver
 * has a direct color model.
 *
 * @return the RGB values for the image or null if direct color
 *
 * @see PaletteData#getRGBs()
 */
public RGB[] getRGBs() {
	return palette.getRGBs();
}

/**
 * Returns an <code>ImageData</code> which specifies the
 * transparency mask information for the receiver, or null if the
 * receiver has no transparency and is not an icon.
 *
 * @return the transparency mask or null if none exists
 */
public ImageData getTransparencyMask() {
	if (getTransparencyType() == SWT.TRANSPARENCY_MASK) {
		return new ImageData(width, height, 1, bwPalette(), maskPad, maskData);
	} else {
		return colorMaskImage(transparentPixel);
	}
}

/**
 * Returns the image transparency type.
 *
 * @return the receiver's transparency type
 */
public int getTransparencyType() {
	if (maskData != null) return SWT.TRANSPARENCY_MASK;
	if (transparentPixel != -1) return SWT.TRANSPARENCY_PIXEL;
	if (alphaData != null) return SWT.TRANSPARENCY_ALPHA;
	return SWT.TRANSPARENCY_NONE;
}

/**
 * Returns the byte order of the receiver.
 * 
 * @return MSB_FIRST or LSB_FIRST
 */
int getByteOrder() {
	return depth != 16 ? MSB_FIRST : LSB_FIRST;
}

/**
 * Returns a copy of the receiver which has been stretched or
 * shrunk to the specified size. If either the width or height
 * is negative, the resulting image will be inverted in the
 * associated axis.
 *
 * @param width the width of the new ImageData
 * @param height the height of the new ImageData
 * @return a scaled copy of the image
 */
public ImageData scaledTo(int width, int height) {
	/* Create a destination image with no data */
	final boolean flipX = (width < 0);
	if (flipX) width = - width;
	final boolean flipY = (height < 0);
	if (flipY) height = - height;

	ImageData dest = new ImageData(
		width, height, depth, palette,
		scanlinePad, null, 0, null,
		null, -1, transparentPixel, type,
		x, y, disposalMethod, delayTime);

	/* Scale the image contents */
	if (palette.isDirect) blit(BLIT_SRC,
		this.data, this.depth, this.bytesPerLine, this.getByteOrder(), 0, 0, this.width, this.height, 0, 0, 0,
		ALPHA_OPAQUE, null, 0, 0, 0,
		dest.data, dest.depth, dest.bytesPerLine, dest.getByteOrder(), 0, 0, dest.width, dest.height, 0, 0, 0,
		flipX, flipY);
	else blit(BLIT_SRC,
		this.data, this.depth, this.bytesPerLine, this.getByteOrder(), 0, 0, this.width, this.height, null, null, null,
		ALPHA_OPAQUE, null, 0, 0, 0,
		dest.data, dest.depth, dest.bytesPerLine, dest.getByteOrder(), 0, 0, dest.width, dest.height, null, null, null,
		flipX, flipY);
	
	/* Scale the image mask or alpha */
	if (maskData != null) {
		dest.maskPad = this.maskPad;
		int destBpl = (dest.width + 7) / 8;
		destBpl = (destBpl + (dest.maskPad - 1)) / dest.maskPad * dest.maskPad;
		dest.maskData = new byte[destBpl * dest.height];
		int srcBpl = (this.width + 7) / 8;
		srcBpl = (srcBpl + (this.maskPad - 1)) / this.maskPad * this.maskPad;
		blit(BLIT_SRC, this.maskData, 1, srcBpl, MSB_FIRST, 0, 0, this.width, this.height, null, null, null,
			ALPHA_OPAQUE, null, 0, 0, 0,
			dest.maskData, 1, destBpl, MSB_FIRST, 0, 0, dest.width, dest.height, null, null, null,
			flipX, flipY);
	} else if (alpha != -1) {
		dest.alpha = this.alpha;
	} else if (alphaData != null) {
		dest.alphaData = new byte[dest.width * dest.height];
		blit(BLIT_SRC,
			this.alphaData, 8, this.width, MSB_FIRST, 0, 0, this.width, this.height, null, null, null,
			ALPHA_OPAQUE, null, 0, 0, 0,
			dest.alphaData, 8, dest.width, MSB_FIRST, 0, 0, dest.width, dest.height, null, null, null,
			flipX, flipY);
	}
	return dest;
}

/**
 * Sets the alpha value at offset <code>x</code> in
 * scanline <code>y</code> in the receiver's alpha data.
 *
 * @param x the x coordinate of the alpha value to set
 * @param y the y coordinate of the alpha value to set
 * @param alpha the value to set the alpha to
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if x or y is out of bounds</li>
 *  </ul>
 */
public void setAlpha(int x, int y, int alpha) {
	if (x >= width || y >= height || x < 0 || y < 0 || alpha < 0 || alpha > 255)
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	
	if (alphaData == null) alphaData = new byte[width * height];
	alphaData[y * width + x] = (byte)alpha;	
}

/**
 * Sets the alpha values starting at offset <code>x</code> in
 * scanline <code>y</code> in the receiver's alpha data to the
 * values from the array <code>alphas</code> starting at
 * <code>startIndex</code>.
 *
 * @param x the x coordinate of the pixel to being setting the alpha values
 * @param y the y coordinate of the pixel to being setting the alpha values
 * @param putWidth the width of the alpha values to set
 * @param alphas the alpha values to set
 * @param startIndex the index at which to begin setting
 *
 * @exception IndexOutOfBoundsException if putWidth is too large
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if pixels is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if x or y is out of bounds</li>
 *    <li>ERROR_INVALID_ARGUMENT - if putWidth is negative</li>
 * </ul>
 */
public void setAlphas(int x, int y, int putWidth, byte[] alphas, int startIndex) {
	if (alphas == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (putWidth < 0 || x >= width || y >= height || x < 0 || y < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (putWidth == 0) return;
	
	if (alphaData == null) alphaData = new byte[width * height];
	// may throw an IndexOutOfBoundsException
	System.arraycopy(alphas, startIndex, alphaData, y * width + x, putWidth);
}

/**
 * Sets the pixel value at offset <code>x</code> in
 * scanline <code>y</code> in the receiver's data.
 *
 * @param x the x coordinate of the pixel to set
 * @param y the y coordinate of the pixel to set
 * @param pixelValue the value to set the pixel to
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if x or y is out of bounds</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_UNSUPPORTED_DEPTH if the depth is not one of 1, 2, 4, 8, 16, 24 or 32</li>
 * </ul>
 */
public void setPixel(int x, int y, int pixelValue) {
	if (x >= width || y >= height || x < 0 || y < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int index;
	byte theByte;
	int mask;
	if (depth == 1) {
		index = (y * bytesPerLine) + (x >> 3);
		theByte = data[index];
		mask = 1 << (7 - (x & 0x7));
		if ((pixelValue & 0x1) == 1) {
			data[index] = (byte)(theByte | mask);
		} else {
			data[index] = (byte)(theByte & (mask ^ -1));
		}
		return;
	}
	if (depth == 2) {
		index = (y * bytesPerLine) + (x >> 2);
		theByte = data[index];
		int offset = 3 - (x % 4);
		mask = 0xFF ^ (3 << (offset * 2));
		data[index] = (byte)((data[index] & mask) | (pixelValue << (offset * 2)));
		return;
	}
	if (depth == 4) {
		index = (y * bytesPerLine) + (x >> 1);
		if ((x & 0x1) == 0) {
			data[index] = (byte)((data[index] & 0x0F) | ((pixelValue & 0x0F) << 4));
		} else {
			data[index] = (byte)((data[index] & 0xF0) | (pixelValue & 0x0F));
		}
		return;
	}
	if (depth == 8) {
		index = (y * bytesPerLine) + x ;
		data[index] = (byte)(pixelValue & 0xFF);
		return;
	}
	if (depth == 16) {
		index = (y * bytesPerLine) + (x * 2);
		data[index + 1] = (byte)((pixelValue >> 8) & 0xFF);
		data[index] = (byte)(pixelValue & 0xFF);
		return;
	}
	if (depth == 24) {
		index = (y * bytesPerLine) + (x * 3);
		data[index] = (byte)((pixelValue >> 16) & 0xFF);
		data[index + 1] = (byte)((pixelValue >> 8) & 0xFF);
		data[index + 2] = (byte)(pixelValue & 0xFF);
		return;
	}
	if (depth == 32) {
		index = (y * bytesPerLine) + (x * 4);
		data[index]  = (byte)((pixelValue >> 24) & 0xFF);
		data[index + 1] = (byte)((pixelValue >> 16) & 0xFF);
		data[index + 2] = (byte)((pixelValue >> 8) & 0xFF);
		data[index + 3] = (byte)(pixelValue & 0xFF);
		return;
	}
	SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
}

/**
 * Sets the pixel values starting at offset <code>x</code> in
 * scanline <code>y</code> in the receiver's data to the
 * values from the array <code>pixels</code> starting at
 * <code>startIndex</code>.
 *
 * @param x the x position of the pixel to set
 * @param y the y position of the pixel to set
 * @param putWidth the width of the pixels to set
 * @param pixels the pixels to set
 * @param startIndex the index at which to begin setting
 *
 * @exception IndexOutOfBoundsException if putWidth is too large
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if pixels is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if x or y is out of bounds</li>
 *    <li>ERROR_INVALID_ARGUMENT - if putWidth is negative</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_UNSUPPORTED_DEPTH if the depth is not one of 1, 2, 4, 8
 *        (For higher depths, use the int[] version of this method.)</li>
 * </ul>
 */
public void setPixels(int x, int y, int putWidth, byte[] pixels, int startIndex) {
	if (pixels == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (putWidth < 0 || x >= width || y >= height || x < 0 || y < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (putWidth == 0) return;
	int index;
	int theByte;
	int mask;
	int n = putWidth;
	int i = startIndex;
	int srcX = x, srcY = y;
	if (depth == 1) {
		index = (y * bytesPerLine) + (x >> 3);
		while (n > 0) {
			mask = 1 << (7 - (srcX & 0x7));
			if ((pixels[i] & 0x1) == 1) {
				data[index] = (byte)((data[index] & 0xFF) | mask);
			} else {
				data[index] = (byte)((data[index] & 0xFF) & (mask ^ -1));
			}
			i++;
			n--;
			srcX++;
			if (srcX >= width) {
				srcY++;
				index = srcY * bytesPerLine;
				srcX = 0;
			} else {
				if (mask == 1) {
					index++;
				}
			}
		}
		return;
	}
	if (depth == 2) {
		byte [] masks = { (byte)0xFC, (byte)0xF3, (byte)0xCF, (byte)0x3F };
		index = (y * bytesPerLine) + (x >> 2);
		int offset = 3 - (x % 4);
		while (n > 0) {
			theByte = pixels[i] & 0x3;
			data[index] = (byte)((data[index] & masks[offset]) | (theByte << (offset * 2)));
			i++;
			n--;
			srcX++;
			if (srcX >= width) {
				srcY++;
				index = srcY * bytesPerLine;
				offset = 0;
				srcX = 0;
			} else {
				if (offset == 0) {
					index++;
					offset = 3;
				} else {
					offset--;
				}
			}
		}
		return;
	}
	if (depth == 4) {
		index = (y * bytesPerLine) + (x >> 1);
		boolean high = (x & 0x1) == 0;
		while (n > 0) {
			theByte = pixels[i] & 0x0F;
			if (high) {
				data[index] = (byte)((data[index] & 0x0F) | (theByte << 4));
			} else {
				data[index] = (byte)((data[index] & 0xF0) | theByte);
			}
			i++;
			n--;
			srcX++;
			if (srcX >= width) {
				srcY++;
				index = srcY * bytesPerLine;
				high = true;
				srcX = 0;
			} else {
				if (!high) index++;
				high = !high;
			}
		}
		return;
	}
	if (depth == 8) {
		index = (y * bytesPerLine) + x;
		for (int j = 0; j < putWidth; j++) {
			data[index] = (byte)(pixels[i] & 0xFF);
			i++;
			srcX++;
			if (srcX >= width) {
				srcY++;
				index = srcY * bytesPerLine;
				srcX = 0;
			} else {
				index++;
			}
		}
		return;
	}
	SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
}

/**
 * Sets the pixel values starting at offset <code>x</code> in
 * scanline <code>y</code> in the receiver's data to the
 * values from the array <code>pixels</code> starting at
 * <code>startIndex</code>.
 *
 * @param x the x position of the pixel to set
 * @param y the y position of the pixel to set
 * @param putWidth the width of the pixels to set
 * @param pixels the pixels to set
 * @param startIndex the index at which to begin setting
 *
 * @exception IndexOutOfBoundsException if putWidth is too large
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if pixels is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if x or y is out of bounds</li>
 *    <li>ERROR_INVALID_ARGUMENT - if putWidth is negative</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_UNSUPPORTED_DEPTH if the depth is not one of 1, 2, 4, 8, 16, 24 or 32</li>
 * </ul>
 */
public void setPixels(int x, int y, int putWidth, int[] pixels, int startIndex) {
	if (pixels == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (putWidth < 0 || x >= width || y >= height || x < 0 || y < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (putWidth == 0) return;
	int index;
	int theByte;
	int mask;
	int n = putWidth;
	int i = startIndex;
	int pixel;
	int srcX = x, srcY = y;
	if (depth == 1) {
		index = (y * bytesPerLine) + (x >> 3);
		while (n > 0) {
			mask = 1 << (7 - (srcX & 0x7));
			if ((pixels[i] & 0x1) == 1) {
				data[index] = (byte)((data[index] & 0xFF) | mask);
			} else {
				data[index] = (byte)((data[index] & 0xFF) & (mask ^ -1));
			}
			i++;
			n--;
			srcX++;
			if (srcX >= width) {
				srcY++;
				index = srcY * bytesPerLine;
				srcX = 0;
			} else {
				if (mask == 1) {
					index++;
				}
			}
		}
		return;
	}
	if (depth == 2) {
		byte [] masks = { (byte)0xFC, (byte)0xF3, (byte)0xCF, (byte)0x3F };
		index = (y * bytesPerLine) + (x >> 2);
		int offset = 3 - (x % 4);
		while (n > 0) {
			theByte = pixels[i] & 0x3;
			data[index] = (byte)((data[index] & masks[offset]) | (theByte << (offset * 2)));
			i++;
			n--;
			srcX++;
			if (srcX >= width) {
				srcY++;
				index = srcY * bytesPerLine;
				offset = 3;
				srcX = 0;
			} else {
				if (offset == 0) {
					index++;
					offset = 3;
				} else {
					offset--;
				}
			}
		}
		return;
	}
	if (depth == 4) {
		index = (y * bytesPerLine) + (x >> 1);
		boolean high = (x & 0x1) == 0;
		while (n > 0) {
			theByte = pixels[i] & 0x0F;
			if (high) {
				data[index] = (byte)((data[index] & 0x0F) | (theByte << 4));
			} else {
				data[index] = (byte)((data[index] & 0xF0) | theByte);
			}
			i++;
			n--;
			srcX++;
			if (srcX >= width) {
				srcY++;
				index = srcY * bytesPerLine;
				high = true;
				srcX = 0;
			} else {
				if (!high) index++;
				high = !high;
			}
		}
		return;
	}
	if (depth == 8) {
		index = (y * bytesPerLine) + x;
		for (int j = 0; j < putWidth; j++) {
			data[index] = (byte)(pixels[i] & 0xFF);
			i++;
			srcX++;
			if (srcX >= width) {
				srcY++;
				index = srcY * bytesPerLine;
				srcX = 0;
			} else {
				index++;
			}
		}
		return;

	}
	if (depth == 16) {
		index = (y * bytesPerLine) + (x * 2);
		for (int j = 0; j < putWidth; j++) {
			pixel = pixels[i];
			data[index] = (byte)(pixel & 0xFF);
			data[index + 1] = (byte)((pixel >> 8) & 0xFF);
			i++;
			srcX++;
			if (srcX >= width) {
				srcY++;
				index = srcY * bytesPerLine;
				srcX = 0;
			} else {
				index += 2;
			}
		}
		return;
	}
	if (depth == 24) {
		index = (y * bytesPerLine) + (x * 3);
		for (int j = 0; j < putWidth; j++) {
			pixel = pixels[i];
			data[index] = (byte)((pixel >> 16) & 0xFF);
			data[index + 1] = (byte)((pixel >> 8) & 0xFF);
			data[index + 2] = (byte)(pixel & 0xFF);
			i++;
			srcX++;
			if (srcX >= width) {
				srcY++;
				index = srcY * bytesPerLine;
				srcX = 0;
			} else {
				index += 3;
			}
		}
		return;
	}
	if (depth == 32) {
		index = (y * bytesPerLine) + (x * 4);
		for (int j = 0; j < putWidth; j++) {
			pixel = pixels[i];
			data[index] = (byte)((pixel >> 24) & 0xFF);
			data[index + 1] = (byte)((pixel >> 16) & 0xFF);
			data[index + 2] = (byte)((pixel >> 8) & 0xFF);
			data[index + 3] = (byte)(pixel & 0xFF);
			i++;
			srcX++;
			if (srcX >= width) {
				srcY++;
				index = srcY * bytesPerLine;
				srcX = 0;
			} else {
				index += 4;
			}
		}
		return;
	}
	SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
}

/**
 * Returns a palette with 2 colors: black & white.
 */
static PaletteData bwPalette() {
	return new PaletteData(new RGB[] {new RGB(0, 0, 0), new RGB(255, 255, 255)});
}

/**
 * Gets the offset of the most significant bit for
 * the given mask.
 */
static int getMSBOffset(int mask) {
	for (int i = 31; i >= 0; i--) {
		if (((mask >> i) & 0x1) != 0) return i + 1;
	}
	return 0;
}

/**
 * Finds the closest match.
 */
static int closestMatch(int depth, byte red, byte green, byte blue, int redMask, int greenMask, int blueMask, byte[] reds, byte[] greens, byte[] blues) {
	if (depth > 8) {
		int rshift = 32 - getMSBOffset(redMask);
		int gshift = 32 - getMSBOffset(greenMask);
		int bshift = 32 - getMSBOffset(blueMask);
		return (((red << 24) >>> rshift) & redMask) |
			(((green << 24) >>> gshift) & greenMask) |
			(((blue << 24) >>> bshift) & blueMask);
	}
	int r, g, b;
	int minDistance = 0x7fffffff;
	int nearestPixel = 0;
	int n = reds.length;
	for (int j = 0; j < n; j++) {
		r = (reds[j] & 0xFF) - (red & 0xFF);
		g = (greens[j] & 0xFF) - (green & 0xFF);
		b = (blues[j] & 0xFF) - (blue & 0xFF);
		int distance = r*r + g*g + b*b;
		if (distance < minDistance) {
			nearestPixel = j;
			if (distance == 0) break;
			minDistance = distance;
		}
	}
	return nearestPixel;
}

static final ImageData convertMask(ImageData mask) {
	if (mask.depth == 1) return mask;
	PaletteData palette = new PaletteData(new RGB[] {new RGB(0, 0, 0), new RGB(255,255,255)});
	ImageData newMask = new ImageData(mask.width, mask.height, 1, palette);
	/* Find index of black in mask palette */
	int blackIndex = 0;
	RGB[] rgbs = mask.getRGBs();
	if (rgbs != null) {
		while (blackIndex < rgbs.length) {
			if (rgbs[blackIndex].equals(palette.colors[0])) break;
			blackIndex++;
		}
	}
	int[] pixels = new int[mask.width];
	for (int y = 0; y < mask.height; y++) {
		mask.getPixels(0, y, mask.width, pixels, 0);
		for (int i = 0; i < pixels.length; i++) {
			if (pixels[i] == blackIndex) {
				pixels[i] = 0;
			} else {
				pixels[i] = 1;
			}
		}
		newMask.setPixels(0, y, mask.width, pixels, 0);
	}
	return newMask;
}

static final byte[] convertPad(byte[] data, int width, int height, int depth, int pad, int newPad) {
	if (pad == newPad) return data;
	int stride = (width * depth + 7) / 8;
	int bpl = (stride + (pad - 1)) / pad * pad;	
	int newBpl = (stride + (newPad - 1)) / newPad * newPad;
	byte[] newData = new byte[height * newBpl];
	int srcIndex = 0, destIndex = 0;
	for (int y = 0; y < height; y++) {
		System.arraycopy(data, srcIndex, newData, destIndex, stride);
		srcIndex += bpl;
		destIndex += newBpl;
	}
	return newData;
}

/**
 * Blit operation bits to be OR'ed together to specify the desired operation.
 */
static final int
	BLIT_SRC = 1,     // copy source directly, else applies logic operations
	BLIT_ALPHA = 2,   // enable alpha blending
	BLIT_DITHER = 4;  // enable dithering in low color modes

/**
 * Alpha mode, values 0 - 255 specify global alpha level
 */
static final int
	ALPHA_OPAQUE = 255,           // Fully opaque (ignores any alpha data)
	ALPHA_TRANSPARENT = 0,        // Fully transparent (ignores any alpha data)
	ALPHA_CHANNEL_SEPARATE = -1,  // Use alpha channel from separate alphaData
	ALPHA_CHANNEL_SOURCE = -2,    // Use alpha channel embedded in sourceData
	ALPHA_MASK_UNPACKED = -3,     // Use transparency mask formed by bytes in alphaData (non-zero is opaque)
	ALPHA_MASK_PACKED = -4,       // Use transparency mask formed by packed bits in alphaData
	ALPHA_MASK_INDEX = -5,        // Consider source palette indices transparent if in alphaData array
	ALPHA_MASK_RGB = -6;          // Consider source RGBs transparent if in RGB888 format alphaData array

/**
 * Byte and bit order constants.
 */
static final int LSB_FIRST = 0;
static final int MSB_FIRST = 1;

/**
 * Data types (internal)
 */
private static final int
	// direct / true color formats with arbitrary masks & shifts
	TYPE_GENERIC_8 = 0,
	TYPE_GENERIC_16_MSB = 1,
	TYPE_GENERIC_16_LSB = 2,
	TYPE_GENERIC_24 = 3,
	TYPE_GENERIC_32_MSB = 4,
	TYPE_GENERIC_32_LSB = 5,
	// palette indexed color formats
	TYPE_INDEX_8 = 6,
	TYPE_INDEX_4 = 7,
	TYPE_INDEX_2 = 8,
	TYPE_INDEX_1_MSB = 9,
	TYPE_INDEX_1_LSB = 10;

/**
 * Blits a direct palette image into a direct palette image.
 * <p>
 * Note: When the source and destination depth, order and masks
 * are pairwise equal and the blitter operation is BLIT_SRC,
 * the masks are ignored.  Hence when not changing the image
 * data format, 0 may be specified for the masks.
 * </p>
 * 
 * @param op the blitter operation: a combination of BLIT_xxx flags
 *        (see BLIT_xxx constants)
 * @param srcData the source byte array containing image data
 * @param srcDepth the source depth: one of 8, 16, 24, 32
 * @param srcStride the source number of bytes per line
 * @param srcOrder the source byte ordering: one of MSB_FIRST or LSB_FIRST;
 *        ignored if srcDepth is not 16 or 32
 * @param srcX the top-left x-coord of the source blit region
 * @param srcY the top-left y-coord of the source blit region
 * @param srcWidth the width of the source blit region
 * @param srcHeight the height of the source blit region
 * @param srcRedMask the source red channel mask
 * @param srcGreenMask the source green channel mask
 * @param srcBlueMask the source blue channel mask
 * @param alphaMode the alpha blending or mask mode, may be
 *        an integer 0-255 for global alpha; ignored if BLIT_ALPHA
 *        not specified in the blitter operations
 *        (see ALPHA_MODE_xxx constants)
 * @param alphaData the alpha blending or mask data, varies depending
 *        on the value of alphaMode and sometimes ignored
 * @param alphaStride the alpha data number of bytes per line
 * @param alphaX the top-left x-coord of the alpha blit region
 * @param alphaY the top-left y-coord of the alpha blit region
 * @param destData the destination byte array containing image data
 * @param destDepth the destination depth: one of 8, 16, 24, 32
 * @param destStride the destination number of bytes per line
 * @param destOrder the destination byte ordering: one of MSB_FIRST or LSB_FIRST;
 *        ignored if destDepth is not 16 or 32
 * @param destX the top-left x-coord of the destination blit region
 * @param destY the top-left y-coord of the destination blit region
 * @param destWidth the width of the destination blit region
 * @param destHeight the height of the destination blit region
 * @param destRedMask the destination red channel mask
 * @param destGreenMask the destination green channel mask
 * @param destBlueMask the destination blue channel mask
 * @param flipX if true the resulting image is flipped along the vertical axis
 * @param flipY if true the resulting image is flipped along the horizontal axis
 */
static void blit(int op,
	byte[] srcData, int srcDepth, int srcStride, int srcOrder,
	int srcX, int srcY, int srcWidth, int srcHeight,
	int srcRedMask, int srcGreenMask, int srcBlueMask,
	int alphaMode, byte[] alphaData, int alphaStride, int alphaX, int alphaY,
	byte[] destData, int destDepth, int destStride, int destOrder,
	int destX, int destY, int destWidth, int destHeight,
	int destRedMask, int destGreenMask, int destBlueMask,
	boolean flipX, boolean flipY) {
	if ((destWidth <= 0) || (destHeight <= 0) || (alphaMode == ALPHA_TRANSPARENT)) return;

	// these should be supplied as params later
	final int srcAlphaMask = 0, destAlphaMask = 0;

	/*** Prepare scaling data ***/
	final int dwm1 = destWidth - 1;
	final int sfxi = (dwm1 != 0) ? (int)((((long)srcWidth << 16) - 1) / dwm1) : 0;
	final int dhm1 = destHeight - 1;
	final int sfyi = (dhm1 != 0) ? (int)((((long)srcHeight << 16) - 1) / dhm1) : 0;

	/*** Prepare source-related data ***/
	final int sbpp, stype;
	switch (srcDepth) {
		case 8:
			sbpp = 1;
			stype = TYPE_GENERIC_8;
			break;
		case 16:
			sbpp = 2;
			stype = (srcOrder == MSB_FIRST) ? TYPE_GENERIC_16_MSB : TYPE_GENERIC_16_LSB;
			break;
		case 24:
			sbpp = 3;
			stype = TYPE_GENERIC_24;
			break;
		case 32:
			sbpp = 4;
			stype = (srcOrder == MSB_FIRST) ? TYPE_GENERIC_32_MSB : TYPE_GENERIC_32_LSB;
			break;
		default:
			//throw new IllegalArgumentException("Invalid source type");
			return;
	}			
	int spr = srcY * srcStride + srcX * sbpp;

	/*** Prepare destination-related data ***/
	final int dbpp, dtype;
	switch (destDepth) {
		case 8:
			dbpp = 1;
			dtype = TYPE_GENERIC_8;
			break;
		case 16:
			dbpp = 2;
			dtype = (destOrder == MSB_FIRST) ? TYPE_GENERIC_16_MSB : TYPE_GENERIC_16_LSB;
			break;
		case 24:
			dbpp = 3;
			dtype = TYPE_GENERIC_24;
			break;
		case 32:
			dbpp = 4;
			dtype = (destOrder == MSB_FIRST) ? TYPE_GENERIC_32_MSB : TYPE_GENERIC_32_LSB;
			break;
		default:
			//throw new IllegalArgumentException("Invalid destination type");
			return;
	}			
	int dpr = ((flipY) ? destY + dhm1 : destY) * destStride + ((flipX) ? destX + dwm1 : destX) * dbpp;
	final int dprxi = (flipX) ? -dbpp : dbpp;
	final int dpryi = (flipY) ? -destStride : destStride;

	/*** Prepare special processing data ***/
	int apr;
	if ((op & BLIT_ALPHA) != 0) {
		switch (alphaMode) {
			case ALPHA_MASK_UNPACKED:
			case ALPHA_CHANNEL_SEPARATE:
				if (alphaData == null) alphaMode = 0x10000;
				apr = alphaY * alphaStride + alphaX;
				break;
			case ALPHA_MASK_PACKED:
				if (alphaData == null) alphaMode = 0x10000;
				alphaStride <<= 3;
				apr = alphaY * alphaStride + alphaX;
				break;
			case ALPHA_MASK_INDEX:
				//throw new IllegalArgumentException("Invalid alpha type");
				return;
			case ALPHA_MASK_RGB:
				if (alphaData == null) alphaMode = 0x10000;
				apr = 0;
				break;
			default:
				alphaMode = (alphaMode << 16) / 255; // prescale
			case ALPHA_CHANNEL_SOURCE:
				apr = 0;
				break;
		}
	} else {
		alphaMode = 0x10000;
		apr = 0;
	}

	/*** Blit ***/
	int dp = dpr;
	int sp = spr;
	if ((alphaMode == 0x10000) && (stype == dtype) &&
		(srcRedMask == destRedMask) && (srcGreenMask == destGreenMask) &&
		(srcBlueMask == destBlueMask) && (srcAlphaMask == destAlphaMask)) {
		/*** Fast blit (straight copy) ***/
		switch (sbpp) {
			case 1:
				for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
					for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
						destData[dp] = srcData[sp];
						sp += (sfx >>> 16);
					}
				}
				break;					
			case 2:
				for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
					for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
						destData[dp] = srcData[sp];
						destData[dp + 1] = srcData[sp + 1];
						sp += (sfx >>> 16) * 2;
					}
				}
				break;
			case 3:
				for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
					for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
						destData[dp] = srcData[sp];
						destData[dp + 1] = srcData[sp + 1];
						destData[dp + 2] = srcData[sp + 2];
						sp += (sfx >>> 16) * 3;
					}
				}
				break;
			case 4:
				for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
					for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
						destData[dp] = srcData[sp];
						destData[dp + 1] = srcData[sp + 1];
						destData[dp + 2] = srcData[sp + 2];
						destData[dp + 3] = srcData[sp + 3];
						sp += (sfx >>> 16) * 4;
					}
				}
				break;
		}
		return;
	}
	/*** Comprehensive blit (apply transformations) ***/
	final int srcRedShift = getChannelShift(srcRedMask);
	final byte[] srcReds = ANY_TO_EIGHT[getChannelWidth(srcRedMask, srcRedShift)];
	final int srcGreenShift = getChannelShift(srcGreenMask);
	final byte[] srcGreens = ANY_TO_EIGHT[getChannelWidth(srcGreenMask, srcGreenShift)];
	final int srcBlueShift = getChannelShift(srcBlueMask);
	final byte[] srcBlues = ANY_TO_EIGHT[getChannelWidth(srcBlueMask, srcBlueShift)];
	final int srcAlphaShift = getChannelShift(srcAlphaMask);
	final byte[] srcAlphas = ANY_TO_EIGHT[getChannelWidth(srcAlphaMask, srcAlphaShift)];

	final int destRedShift = getChannelShift(destRedMask);
	final int destRedWidth = getChannelWidth(destRedMask, destRedShift);
	final byte[] destReds = ANY_TO_EIGHT[destRedWidth];
	final int destRedPreShift = 8 - destRedWidth;
	final int destGreenShift = getChannelShift(destGreenMask);
	final int destGreenWidth = getChannelWidth(destGreenMask, destGreenShift);
	final byte[] destGreens = ANY_TO_EIGHT[destGreenWidth];
	final int destGreenPreShift = 8 - destGreenWidth;
	final int destBlueShift = getChannelShift(destBlueMask);
	final int destBlueWidth = getChannelWidth(destBlueMask, destBlueShift);
	final byte[] destBlues = ANY_TO_EIGHT[destBlueWidth];
	final int destBluePreShift = 8 - destBlueWidth;
	final int destAlphaShift = getChannelShift(destAlphaMask);
	final int destAlphaWidth = getChannelWidth(destAlphaMask, destAlphaShift);
	final byte[] destAlphas = ANY_TO_EIGHT[destAlphaWidth];
	final int destAlphaPreShift = 8 - destAlphaWidth;

	int ap = apr, alpha = alphaMode;
	int r = 0, g = 0, b = 0, a = 0;
	int rq = 0, gq = 0, bq = 0, aq = 0;
	for (int dy = destHeight, sfy = sfyi; dy > 0; --dy,
			sp = spr += (sfy >>> 16) * srcStride,
			ap = apr += (sfy >>> 16) * alphaStride,
			sfy = (sfy & 0xffff) + sfyi,
			dp = dpr += dpryi) {
		for (int dx = destWidth, sfx = sfxi; dx > 0; --dx,
				dp += dprxi,
				sfx = (sfx & 0xffff) + sfxi) {
			/*** READ NEXT PIXEL ***/
			switch (stype) {
				case TYPE_GENERIC_8: {
					final int data = srcData[sp] & 0xff;
					sp += (sfx >>> 16);
					r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
					g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
					b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
					a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
				} break;
				case TYPE_GENERIC_16_MSB: {
					final int data = ((srcData[sp] & 0xff) << 8) | (srcData[sp + 1] & 0xff);
					sp += (sfx >>> 16) * 2;
					r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
					g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
					b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
					a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
				} break;
				case TYPE_GENERIC_16_LSB: {
					final int data = ((srcData[sp + 1] & 0xff) << 8) | (srcData[sp] & 0xff);
					sp += (sfx >>> 16) * 2;
					r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
					g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
					b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
					a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
				} break;
				case TYPE_GENERIC_24: {
					final int data = (( ((srcData[sp] & 0xff) << 8) |
						(srcData[sp + 1] & 0xff)) << 8) |
						(srcData[sp + 2] & 0xff);
					sp += (sfx >>> 16) * 3;
					r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
					g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
					b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
					a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
				} break;
				case TYPE_GENERIC_32_MSB: {
					final int data = (( (( ((srcData[sp] & 0xff) << 8) |
						(srcData[sp + 1] & 0xff)) << 8) |
						(srcData[sp + 2] & 0xff)) << 8) |
						(srcData[sp + 3] & 0xff);
					sp += (sfx >>> 16) * 4;
					r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
					g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
					b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
					a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
				} break;
				case TYPE_GENERIC_32_LSB: {
					final int data = (( (( ((srcData[sp + 3] & 0xff) << 8) |
						(srcData[sp + 2] & 0xff)) << 8) |
						(srcData[sp + 1] & 0xff)) << 8) |
						(srcData[sp] & 0xff);
					sp += (sfx >>> 16) * 4;
					r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
					g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
					b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
					a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
				} break;
			}

			/*** DO SPECIAL PROCESSING IF REQUIRED ***/
			switch (alphaMode) {
				case ALPHA_CHANNEL_SEPARATE:
					alpha = ((alphaData[ap] & 0xff) << 16) / 255;
					ap += (sfx >> 16);
					break;
				case ALPHA_CHANNEL_SOURCE:
					alpha = (a << 16) / 255;
					break;
				case ALPHA_MASK_UNPACKED:
					alpha = (alphaData[ap] != 0) ? 0x10000 : 0;
					ap += (sfx >> 16);
					break;						
				case ALPHA_MASK_PACKED:
					alpha = (alphaData[ap >> 3] << ((ap & 7) + 9)) & 0x10000;
					ap += (sfx >> 16);
					break;
				case ALPHA_MASK_RGB:
					alpha = 0x10000;
					for (int i = 0; i < alphaData.length; i += 3) {
						if ((r == alphaData[i]) && (g == alphaData[i + 1]) && (b == alphaData[i + 2])) {
							alpha = 0x0000;
							break;
						}
					}
					break;
			}
			if (alpha != 0x10000) {
				if (alpha == 0x0000) continue;
				switch (dtype) {
					case TYPE_GENERIC_8: {
						final int data = destData[dp] & 0xff;
						rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
						gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
						bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
						aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
					} break;
					case TYPE_GENERIC_16_MSB: {
						final int data = ((destData[dp] & 0xff) << 8) | (destData[dp + 1] & 0xff);
						rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
						gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
						bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
						aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
					} break;
					case TYPE_GENERIC_16_LSB: {
						final int data = ((destData[dp + 1] & 0xff) << 8) | (destData[dp] & 0xff);
						rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
						gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
						bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
						aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
					} break;
					case TYPE_GENERIC_24: {
						final int data = (( ((destData[dp] & 0xff) << 8) |
							(destData[dp + 1] & 0xff)) << 8) |
							(destData[dp + 2] & 0xff);
						rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
						gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
						bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
						aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
					} break;
					case TYPE_GENERIC_32_MSB: {
						final int data = (( (( ((destData[dp] & 0xff) << 8) |
							(destData[dp + 1] & 0xff)) << 8) |
							(destData[dp + 2] & 0xff)) << 8) |
							(destData[dp + 3] & 0xff);
						rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
						gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
						bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
						aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
					} break;
					case TYPE_GENERIC_32_LSB: {
						final int data = (( (( ((destData[dp + 3] & 0xff) << 8) |
							(destData[dp + 2] & 0xff)) << 8) |
							(destData[dp + 1] & 0xff)) << 8) |
							(destData[dp] & 0xff);
						rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
						gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
						bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
						aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
					} break;
				}
				// Perform alpha blending
				a = aq + ((a - aq) * alpha >> 16);
				r = rq + ((r - rq) * alpha >> 16);
				g = gq + ((g - gq) * alpha >> 16);
				b = bq + ((b - bq) * alpha >> 16);
			}

			/*** WRITE NEXT PIXEL ***/
			final int data = 
				(r >>> destRedPreShift << destRedShift) |
				(g >>> destGreenPreShift << destGreenShift) |
				(b >>> destBluePreShift << destBlueShift) |
				(a >>> destAlphaPreShift << destAlphaShift);
			switch (dtype) {
				case TYPE_GENERIC_8: {
					destData[dp] = (byte) data;
				} break;
				case TYPE_GENERIC_16_MSB: {
					destData[dp] = (byte) (data >>> 8);
					destData[dp + 1] = (byte) (data & 0xff);
				} break;
				case TYPE_GENERIC_16_LSB: {
					destData[dp] = (byte) (data & 0xff);
					destData[dp + 1] = (byte) (data >>> 8);
				} break;
				case TYPE_GENERIC_24: {
					destData[dp] = (byte) (data >>> 16);
					destData[dp + 1] = (byte) (data >>> 8);
					destData[dp + 2] = (byte) (data & 0xff);
				} break;
				case TYPE_GENERIC_32_MSB: {
					destData[dp] = (byte) (data >>> 24);
					destData[dp + 1] = (byte) (data >>> 16);
					destData[dp + 2] = (byte) (data >>> 8);
					destData[dp + 3] = (byte) (data & 0xff);
				} break;
				case TYPE_GENERIC_32_LSB: {
					destData[dp] = (byte) (data & 0xff);
					destData[dp + 1] = (byte) (data >>> 8);
					destData[dp + 2] = (byte) (data >>> 16);
					destData[dp + 3] = (byte) (data >>> 24);
				} break;
			}
		}
	}			
}

/**
 * Blits an index palette image into an index palette image.
 * <p>
 * Note: The source and destination red, green, and blue
 * arrays may be null if no alpha blending or dither is to be
 * performed.
 * </p>
 * 
 * @param op the blitter operation: a combination of BLIT_xxx flags
 *        (see BLIT_xxx constants)
 * @param srcData the source byte array containing image data
 * @param srcDepth the source depth: one of 1, 2, 4, 8
 * @param srcStride the source number of bytes per line
 * @param srcOrder the source byte ordering: one of MSB_FIRST or LSB_FIRST;
 *        ignored if srcDepth is not 1
 * @param srcX the top-left x-coord of the source blit region
 * @param srcY the top-left y-coord of the source blit region
 * @param srcWidth the width of the source blit region
 * @param srcHeight the height of the source blit region
 * @param srcReds the source palette red component intensities
 * @param srcGreens the source palette green component intensities
 * @param srcBlues the source palette blue component intensities
 * @param alphaMode the alpha blending or mask mode, may be
 *        an integer 0-255 for global alpha; ignored if BLIT_ALPHA
 *        not specified in the blitter operations
 *        (see ALPHA_MODE_xxx constants)
 * @param alphaData the alpha blending or mask data, varies depending
 *        on the value of alphaMode and sometimes ignored
 * @param alphaStride the alpha data number of bytes per line
 * @param alphaX the top-left x-coord of the alpha blit region
 * @param alphaY the top-left y-coord of the alpha blit region
 * @param destData the destination byte array containing image data
 * @param destDepth the destination depth: one of 1, 2, 4, 8
 * @param destStride the destination number of bytes per line
 * @param destOrder the destination byte ordering: one of MSB_FIRST or LSB_FIRST;
 *        ignored if destDepth is not 1
 * @param destX the top-left x-coord of the destination blit region
 * @param destY the top-left y-coord of the destination blit region
 * @param destWidth the width of the destination blit region
 * @param destHeight the height of the destination blit region
 * @param destReds the destination palette red component intensities
 * @param destGreens the destination palette green component intensities
 * @param destBlues the destination palette blue component intensities
 * @param flipX if true the resulting image is flipped along the vertical axis
 * @param flipY if true the resulting image is flipped along the horizontal axis
 */
static void blit(int op,
	byte[] srcData, int srcDepth, int srcStride, int srcOrder,
	int srcX, int srcY, int srcWidth, int srcHeight,
	byte[] srcReds, byte[] srcGreens, byte[] srcBlues,
	int alphaMode, byte[] alphaData, int alphaStride, int alphaX, int alphaY,
	byte[] destData, int destDepth, int destStride, int destOrder,
	int destX, int destY, int destWidth, int destHeight,
	byte[] destReds, byte[] destGreens, byte[] destBlues,
	boolean flipX, boolean flipY) {
	if ((destWidth <= 0) || (destHeight <= 0) || (alphaMode == ALPHA_TRANSPARENT)) return;

	/*** Prepare scaling data ***/
	final int dwm1 = destWidth - 1;
	final int sfxi = (dwm1 != 0) ? (int)((((long)srcWidth << 16) - 1) / dwm1) : 0;
	final int dhm1 = destHeight - 1;
	final int sfyi = (dhm1 != 0) ? (int)((((long)srcHeight << 16) - 1) / dhm1) : 0;

	/*** Prepare source-related data ***/
	final int stype;
	switch (srcDepth) {
		case 8:
			stype = TYPE_INDEX_8;
			break;
		case 4:
			srcStride <<= 1;
			stype = TYPE_INDEX_4;
			break;
		case 2:
			srcStride <<= 2;
			stype = TYPE_INDEX_2;
			break;
		case 1:
			srcStride <<= 3;
			stype = (srcOrder == MSB_FIRST) ? TYPE_INDEX_1_MSB : TYPE_INDEX_1_LSB;
			break;
		default:
			//throw new IllegalArgumentException("Invalid source type");
			return;		
	}			
	int spr = srcY * srcStride + srcX;

	/*** Prepare destination-related data ***/
	final int dtype;
	switch (destDepth) {
		case 8:
			dtype = TYPE_INDEX_8;
			break;
		case 4:
			destStride <<= 1;
			dtype = TYPE_INDEX_4;
			break;
		case 2:
			destStride <<= 2;
			dtype = TYPE_INDEX_2;
			break;
		case 1:
			destStride <<= 3;
			dtype = (destOrder == MSB_FIRST) ? TYPE_INDEX_1_MSB : TYPE_INDEX_1_LSB;
			break;
		default:
			//throw new IllegalArgumentException("Invalid source type");
			return;
	}			
	int dpr = ((flipY) ? destY + dhm1 : destY) * destStride + ((flipX) ? destX + dwm1 : destX);
	final int dprxi = (flipX) ? -1 : 1;
	final int dpryi = (flipY) ? -destStride : destStride;

	/*** Prepare special processing data ***/
	int apr;
	if ((op & BLIT_ALPHA) != 0) {
		switch (alphaMode) {
			case ALPHA_MASK_UNPACKED:
			case ALPHA_CHANNEL_SEPARATE:
				if (alphaData == null) alphaMode = 0x10000;
				apr = alphaY * alphaStride + alphaX;
				break;
			case ALPHA_MASK_PACKED:
				if (alphaData == null) alphaMode = 0x10000;
				alphaStride <<= 3;
				apr = alphaY * alphaStride + alphaX;
				break;
			case ALPHA_MASK_INDEX:
			case ALPHA_MASK_RGB:
				if (alphaData == null) alphaMode = 0x10000;
				apr = 0;
				break;
			default:
				alphaMode = (alphaMode << 16) / 255; // prescale
			case ALPHA_CHANNEL_SOURCE:
				apr = 0;
				break;
		}
	} else {
		alphaMode = 0x10000;
		apr = 0;
	}
	final boolean ditherEnabled = (op & BLIT_DITHER) != 0;

	/*** Blit ***/
	int dp = dpr;
	int sp = spr;
	int ap = apr;
	int destPaletteSize = 1 << destDepth;
	if ((destReds != null) && (destReds.length < destPaletteSize)) destPaletteSize = destReds.length;
	byte[] paletteMapping = null;
	boolean isExactPaletteMapping = true;
	switch (alphaMode) {
		case 0x10000:
			/*** If the palettes and formats are equivalent use a one-to-one mapping ***/
			if ((stype == dtype) &&
				(srcReds == destReds) && (srcGreens == destGreens) && (srcBlues == destBlues)) {
				paletteMapping = ONE_TO_ONE_MAPPING;
				break;
			/*** If palettes have not been supplied, supply a suitable mapping ***/
			} else if ((srcReds == null) || (destReds == null)) {
				if (srcDepth <= destDepth) {
					paletteMapping = ONE_TO_ONE_MAPPING;
				} else {
					paletteMapping = new byte[1 << srcDepth];
					int mask = (0xff << destDepth) >>> 8;
					for (int i = 0; i < paletteMapping.length; ++i) paletteMapping[i] = (byte)(i & mask);
				}
				break;
			}
		case ALPHA_MASK_UNPACKED:
		case ALPHA_MASK_PACKED:
		case ALPHA_MASK_INDEX:
		case ALPHA_MASK_RGB:
			/*** Generate a palette mapping ***/
			int srcPaletteSize = 1 << srcDepth;
			paletteMapping = new byte[srcPaletteSize];
			if ((srcReds != null) && (srcReds.length < srcPaletteSize)) srcPaletteSize = srcReds.length;
			for (int i = 0, r, g, b, index; i < srcPaletteSize; ++i) {
				r = srcReds[i] & 0xff;
				g = srcGreens[i] & 0xff;
				b = srcBlues[i] & 0xff;
				index = 0;
				int minDistance = 0x7fffffff;
				for (int j = 0, dr, dg, db, distance; j < destPaletteSize; ++j) {
					dr = (destReds[j] & 0xff) - r;
					dg = (destGreens[j] & 0xff) - g;
					db = (destBlues[j] & 0xff) - b;
					distance = dr * dr + dg * dg + db * db;
					if (distance < minDistance) {
						index = j;
						if (distance == 0) break;
						minDistance = distance;
					}
				}
				paletteMapping[i] = (byte)index;
				if (minDistance != 0) isExactPaletteMapping = false;
			}
			break;
	}
	if ((paletteMapping != null) && (isExactPaletteMapping || ! ditherEnabled)) {
		if ((stype == dtype) && (alphaMode == 0x10000)) {
			/*** Fast blit (copy w/ mapping) ***/
			switch (stype) {
				case TYPE_INDEX_8:
					for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
						for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
							destData[dp] = paletteMapping[srcData[sp] & 0xff];
							sp += (sfx >>> 16);
						}
					}
					break;					
				case TYPE_INDEX_4:
					for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
						for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
							final int v;
							if ((sp & 1) != 0) v = paletteMapping[srcData[sp >> 1] & 0x0f];
							else v = (srcData[sp >> 1] >>> 4) & 0x0f;
							sp += (sfx >>> 16);
							if ((dp & 1) != 0) destData[dp >> 1] = (byte)((destData[dp >> 1] & 0xf0) | v);
							else destData[dp >> 1] = (byte)((destData[dp >> 1] & 0x0f) | (v << 4));
						}
					}
					break;
				case TYPE_INDEX_2:
					for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
						for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
							final int index = paletteMapping[(srcData[sp >> 2] >>> (6 - (sp & 3) * 2)) & 0x03];
							sp += (sfx >>> 16);
							final int shift = 6 - (dp & 3) * 2;
							destData[dp >> 2] = (byte)(destData[dp >> 2] & ~(0x03 << shift) | (index << shift));
						}
					}
					break;					
				case TYPE_INDEX_1_MSB:
					for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
						for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
							final int index = paletteMapping[(srcData[sp >> 3] >>> (7 - (sp & 7))) & 0x01];
							sp += (sfx >>> 16);
							final int shift = 7 - (dp & 7);
							destData[dp >> 3] = (byte)(destData[dp >> 3] & ~(0x01 << shift) | (index << shift));
						}
					}
					break;					
				case TYPE_INDEX_1_LSB:
					for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
						for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
							final int index = paletteMapping[(srcData[sp >> 3] >>> (sp & 7)) & 0x01];
							sp += (sfx >>> 16);
							final int shift = dp & 7;
							destData[dp >> 3] = (byte)(destData[dp >> 3] & ~(0x01 << shift) | (index << shift));
						}
					}
					break;
			}
		} else {
			/*** Convert between indexed modes using mapping and mask ***/
			for (int dy = destHeight, sfy = sfyi; dy > 0; --dy,
					sp = spr += (sfy >>> 16) * srcStride,
					sfy = (sfy & 0xffff) + sfyi,
					dp = dpr += dpryi) {
				for (int dx = destWidth, sfx = sfxi; dx > 0; --dx,
						dp += dprxi,
						sfx = (sfx & 0xffff) + sfxi) {
					int index;
					/*** READ NEXT PIXEL ***/
					switch (stype) {
						case TYPE_INDEX_8:
							index = srcData[sp] & 0xff;
							sp += (sfx >>> 16);
							break;					
						case TYPE_INDEX_4:
							if ((sp & 1) != 0) index = srcData[sp >> 1] & 0x0f;
							else index = (srcData[sp >> 1] >>> 4) & 0x0f;
							sp += (sfx >>> 16);
							break;					
						case TYPE_INDEX_2:
							index = (srcData[sp >> 2] >>> (6 - (sp & 3) * 2)) & 0x03;
							sp += (sfx >>> 16);
							break;					
						case TYPE_INDEX_1_MSB:
							index = (srcData[sp >> 3] >>> (7 - (sp & 7))) & 0x01;
							sp += (sfx >>> 16);
							break;					
						case TYPE_INDEX_1_LSB:
							index = (srcData[sp >> 3] >>> (sp & 7)) & 0x01;
							sp += (sfx >>> 16);
							break;
						default:
							return;
					}
					/*** APPLY MASK ***/
					switch (alphaMode) {
						case ALPHA_MASK_UNPACKED: {
							final byte mask = alphaData[ap];
							ap += (sfx >> 16);
							if (mask == 0) continue;
						} break;
						case ALPHA_MASK_PACKED: {
							final int mask = alphaData[ap >> 3] & (1 << (ap & 7));
							ap += (sfx >> 16);
							if (mask == 0) continue;
						} break;
						case ALPHA_MASK_INDEX: {
							int i = 0;
							while (i < alphaData.length) {
								if (index == (alphaData[i] & 0xff)) break;
							}
							if (i < alphaData.length) continue;
						} break;
						case ALPHA_MASK_RGB: {
							final byte r = srcReds[index], g = srcGreens[index], b = srcBlues[index];
							int i = 0;
							while (i < alphaData.length) {
								if ((r == alphaData[i]) && (g == alphaData[i + 1]) && (b == alphaData[i + 2])) break;
								i += 3;
							}
							if (i < alphaData.length) continue;
						} break;
					}
					index = paletteMapping[index] & 0xff;
			
					/*** WRITE NEXT PIXEL ***/
					switch (dtype) {
						case TYPE_INDEX_8:
							destData[dp] = (byte) index;
							break;
						case TYPE_INDEX_4:
							if ((dp & 1) != 0) destData[dp >> 1] = (byte)((destData[dp >> 1] & 0xf0) | index);
							else destData[dp >> 1] = (byte)((destData[dp >> 1] & 0x0f) | (index << 4));
							break;					
						case TYPE_INDEX_2: {
							final int shift = 6 - (dp & 3) * 2;
							destData[dp >> 2] = (byte)(destData[dp >> 2] & ~(0x03 << shift) | (index << shift));
						} break;					
						case TYPE_INDEX_1_MSB: {
							final int shift = 7 - (dp & 7);
							destData[dp >> 3] = (byte)(destData[dp >> 3] & ~(0x01 << shift) | (index << shift));
						} break;
						case TYPE_INDEX_1_LSB: {
							final int shift = dp & 7;
							destData[dp >> 3] = (byte)(destData[dp >> 3] & ~(0x01 << shift) | (index << shift));
						} break;					
					}
				}
			}
		}
		return;
	}
		
	/*** Comprehensive blit (apply transformations) ***/
	int alpha = alphaMode;
	int index = 0;
	int indexq = 0;
	int lastindex = 0, lastr = -1, lastg = -1, lastb = -1;
	final int[] rerr, gerr, berr;
	if (ditherEnabled) {
		rerr = new int[destWidth + 2];
		gerr = new int[destWidth + 2];
		berr = new int[destWidth + 2];
	} else {
		rerr = null; gerr = null; berr = null;
	}
	for (int dy = destHeight, sfy = sfyi; dy > 0; --dy,
			sp = spr += (sfy >>> 16) * srcStride,
			ap = apr += (sfy >>> 16) * alphaStride,
			sfy = (sfy & 0xffff) + sfyi,
			dp = dpr += dpryi) {
		int lrerr = 0, lgerr = 0, lberr = 0;
		for (int dx = destWidth, sfx = sfxi; dx > 0; --dx,
				dp += dprxi,
				sfx = (sfx & 0xffff) + sfxi) {
			/*** READ NEXT PIXEL ***/
			switch (stype) {
				case TYPE_INDEX_8:
					index = srcData[sp] & 0xff;
					sp += (sfx >>> 16);
					break;
				case TYPE_INDEX_4:
					if ((sp & 1) != 0) index = srcData[sp >> 1] & 0x0f;
					else index = (srcData[sp >> 1] >>> 4) & 0x0f;
					sp += (sfx >>> 16);
					break;
				case TYPE_INDEX_2:
					index = (srcData[sp >> 2] >>> (6 - (sp & 3) * 2)) & 0x03;
					sp += (sfx >>> 16);
					break;
				case TYPE_INDEX_1_MSB:
					index = (srcData[sp >> 3] >>> (7 - (sp & 7))) & 0x01;
					sp += (sfx >>> 16);
					break;
				case TYPE_INDEX_1_LSB:
					index = (srcData[sp >> 3] >>> (sp & 7)) & 0x01;
					sp += (sfx >>> 16);
					break;
			}

			/*** DO SPECIAL PROCESSING IF REQUIRED ***/
			int r = srcReds[index] & 0xff, g = srcGreens[index] & 0xff, b = srcBlues[index] & 0xff;
			switch (alphaMode) {
				case ALPHA_CHANNEL_SEPARATE:
					alpha = ((alphaData[ap] & 0xff) << 16) / 255;
					ap += (sfx >> 16);
					break;
				case ALPHA_MASK_UNPACKED:
					alpha = (alphaData[ap] != 0) ? 0x10000 : 0;
					ap += (sfx >> 16);
					break;						
				case ALPHA_MASK_PACKED:
					alpha = (alphaData[ap >> 3] << ((ap & 7) + 9)) & 0x10000;
					ap += (sfx >> 16);
					break;
				case ALPHA_MASK_INDEX: { // could speed up using binary search if we sorted the indices
					int i = 0;
					while (i < alphaData.length) {
						if (index == (alphaData[i] & 0xff)) break;
					}
					if (i < alphaData.length) continue;
				} break;
				case ALPHA_MASK_RGB: {
					int i = 0;
					while (i < alphaData.length) {
						if ((r == (alphaData[i] & 0xff)) &&
							(g == (alphaData[i + 1] & 0xff)) &&
							(b == (alphaData[i + 2] & 0xff))) break;
						i += 3;
					}
					if (i < alphaData.length) continue;
				} break;
			}
			if (alpha != 0x10000) {
				if (alpha == 0x0000) continue;
				switch (dtype) {
					case TYPE_INDEX_8:
						indexq = destData[dp] & 0xff;
						break;
					case TYPE_INDEX_4:
						if ((dp & 1) != 0) indexq = destData[dp >> 1] & 0x0f;
						else indexq = (destData[dp >> 1] >>> 4) & 0x0f;
						break;
					case TYPE_INDEX_2:
						indexq = (destData[dp >> 2] >>> (6 - (dp & 3) * 2)) & 0x03;
						break;
					case TYPE_INDEX_1_MSB:
						indexq = (destData[dp >> 3] >>> (7 - (dp & 7))) & 0x01;
						break;
					case TYPE_INDEX_1_LSB:
						indexq = (destData[dp >> 3] >>> (dp & 7)) & 0x01;
						break;
				}
				// Perform alpha blending
				final int rq = destReds[indexq] & 0xff;
				final int gq = destGreens[indexq] & 0xff;
				final int bq = destBlues[indexq] & 0xff;
				r = rq + ((r - rq) * alpha >> 16);
				g = gq + ((g - gq) * alpha >> 16);
				b = bq + ((b - bq) * alpha >> 16);
			}

			/*** MAP COLOR TO THE PALETTE ***/
			if (ditherEnabled) {
				// Floyd-Steinberg error diffusion
				r += rerr[dx] >> 4;
				if (r < 0) r = 0; else if (r > 255) r = 255;
				g += gerr[dx] >> 4;
				if (g < 0) g = 0; else if (g > 255) g = 255;
				b += berr[dx] >> 4;
				if (b < 0) b = 0; else if (b > 255) b = 255;
				rerr[dx] = lrerr;
				gerr[dx] = lgerr;
				berr[dx] = lberr;
			}
			if (r != lastr || g != lastg || b != lastb) {
				// moving the variable declarations out seems to make the JDK JIT happier...
				for (int j = 0, dr, dg, db, distance, minDistance = 0x7fffffff; j < destPaletteSize; ++j) {
					dr = (destReds[j] & 0xff) - r;
					dg = (destGreens[j] & 0xff) - g;
					db = (destBlues[j] & 0xff) - b;
					distance = dr * dr + dg * dg + db * db;
					if (distance < minDistance) {
						lastindex = j;
						if (distance == 0) break;
						minDistance = distance;
					}
				}
				lastr = r; lastg = g; lastb = b;
			}
			if (ditherEnabled) {
				// Floyd-Steinberg error diffusion, cont'd...
				final int dxm1 = dx - 1, dxp1 = dx + 1;
				int acc;
				rerr[dxp1] += acc = (lrerr = r - (destReds[lastindex] & 0xff)) + lrerr + lrerr;
				rerr[dx] += acc += lrerr + lrerr;
				rerr[dxm1] += acc + lrerr + lrerr;
				gerr[dxp1] += acc = (lgerr = g - (destGreens[lastindex] & 0xff)) + lgerr + lgerr;
				gerr[dx] += acc += lgerr + lgerr;
				gerr[dxm1] += acc + lgerr + lgerr;
				berr[dxp1] += acc = (lberr = b - (destBlues[lastindex] & 0xff)) + lberr + lberr;
				berr[dx] += acc += lberr + lberr;
				berr[dxm1] += acc + lberr + lberr;
			}

			/*** WRITE NEXT PIXEL ***/
			switch (dtype) {
				case TYPE_INDEX_8:
					destData[dp] = (byte) lastindex;
					break;
				case TYPE_INDEX_4:
					if ((dp & 1) != 0) destData[dp >> 1] = (byte)((destData[dp >> 1] & 0xf0) | lastindex);
					else destData[dp >> 1] = (byte)((destData[dp >> 1] & 0x0f) | (lastindex << 4));
					break;
				case TYPE_INDEX_2: {
					final int shift = 6 - (dp & 3) * 2;
					destData[dp >> 2] = (byte)(destData[dp >> 2] & ~(0x03 << shift) | (lastindex << shift));
				} break;					
				case TYPE_INDEX_1_MSB: {
					final int shift = 7 - (dp & 7);
					destData[dp >> 3] = (byte)(destData[dp >> 3] & ~(0x01 << shift) | (lastindex << shift));
				} break;
				case TYPE_INDEX_1_LSB: {
					final int shift = dp & 7;
					destData[dp >> 3] = (byte)(destData[dp >> 3] & ~(0x01 << shift) | (lastindex << shift));
				} break;					
			}
		}
	}
}

/**
 * Blits an index palette image into a direct palette image.
 * <p>
 * Note: The source and destination masks and palettes must
 * always be fully specified.
 * </p>
 * 
 * @param op the blitter operation: a combination of BLIT_xxx flags
 *        (see BLIT_xxx constants)
 * @param srcData the source byte array containing image data
 * @param srcDepth the source depth: one of 1, 2, 4, 8
 * @param srcStride the source number of bytes per line
 * @param srcOrder the source byte ordering: one of MSB_FIRST or LSB_FIRST;
 *        ignored if srcDepth is not 1
 * @param srcX the top-left x-coord of the source blit region
 * @param srcY the top-left y-coord of the source blit region
 * @param srcWidth the width of the source blit region
 * @param srcHeight the height of the source blit region
 * @param srcReds the source palette red component intensities
 * @param srcGreens the source palette green component intensities
 * @param srcBlues the source palette blue component intensities
 * @param alphaMode the alpha blending or mask mode, may be
 *        an integer 0-255 for global alpha; ignored if BLIT_ALPHA
 *        not specified in the blitter operations
 *        (see ALPHA_MODE_xxx constants)
 * @param alphaData the alpha blending or mask data, varies depending
 *        on the value of alphaMode and sometimes ignored
 * @param alphaStride the alpha data number of bytes per line
 * @param alphaX the top-left x-coord of the alpha blit region
 * @param alphaY the top-left y-coord of the alpha blit region
 * @param destData the destination byte array containing image data
 * @param destDepth the destination depth: one of 8, 16, 24, 32
 * @param destStride the destination number of bytes per line
 * @param destOrder the destination byte ordering: one of MSB_FIRST or LSB_FIRST;
 *        ignored if destDepth is not 16 or 32
 * @param destX the top-left x-coord of the destination blit region
 * @param destY the top-left y-coord of the destination blit region
 * @param destWidth the width of the destination blit region
 * @param destHeight the height of the destination blit region
 * @param destRedMask the destination red channel mask
 * @param destGreenMask the destination green channel mask
 * @param destBlueMask the destination blue channel mask
 * @param flipX if true the resulting image is flipped along the vertical axis
 * @param flipY if true the resulting image is flipped along the horizontal axis
 */
static void blit(int op,
	byte[] srcData, int srcDepth, int srcStride, int srcOrder,
	int srcX, int srcY, int srcWidth, int srcHeight,
	byte[] srcReds, byte[] srcGreens, byte[] srcBlues,
	int alphaMode, byte[] alphaData, int alphaStride, int alphaX, int alphaY,
	byte[] destData, int destDepth, int destStride, int destOrder,
	int destX, int destY, int destWidth, int destHeight,
	int destRedMask, int destGreenMask, int destBlueMask,
	boolean flipX, boolean flipY) {
	if ((destWidth <= 0) || (destHeight <= 0) || (alphaMode == ALPHA_TRANSPARENT)) return;

	// these should be supplied as params later
	final int destAlphaMask = 0;

	/*** Prepare scaling data ***/
	final int dwm1 = destWidth - 1;
	final int sfxi = (dwm1 != 0) ? (int)((((long)srcWidth << 16) - 1) / dwm1) : 0;
	final int dhm1 = destHeight - 1;
	final int sfyi = (dhm1 != 0) ? (int)((((long)srcHeight << 16) - 1) / dhm1) : 0;

	/*** Prepare source-related data ***/
	final int stype;
	switch (srcDepth) {
		case 8:
			stype = TYPE_INDEX_8;
			break;
		case 4:
			srcStride <<= 1;
			stype = TYPE_INDEX_4;
			break;
		case 2:
			srcStride <<= 2;
			stype = TYPE_INDEX_2;
			break;
		case 1:
			srcStride <<= 3;
			stype = (srcOrder == MSB_FIRST) ? TYPE_INDEX_1_MSB : TYPE_INDEX_1_LSB;
			break;
		default:
			//throw new IllegalArgumentException("Invalid source type");
			return;
	}			
	int spr = srcY * srcStride + srcX;

	/*** Prepare destination-related data ***/
	final int dbpp, dtype;
	switch (destDepth) {
		case 8:
			dbpp = 1;
			dtype = TYPE_GENERIC_8;
			break;
		case 16:
			dbpp = 2;
			dtype = (destOrder == MSB_FIRST) ? TYPE_GENERIC_16_MSB : TYPE_GENERIC_16_LSB;
			break;
		case 24:
			dbpp = 3;
			dtype = TYPE_GENERIC_24;
			break;
		case 32:
			dbpp = 4;
			dtype = (destOrder == MSB_FIRST) ? TYPE_GENERIC_32_MSB : TYPE_GENERIC_32_LSB;
			break;
		default:
			//throw new IllegalArgumentException("Invalid destination type");
			return;
	}			
	int dpr = ((flipY) ? destY + dhm1 : destY) * destStride + ((flipX) ? destX + dwm1 : destX) * dbpp;
	final int dprxi = (flipX) ? -dbpp : dbpp;
	final int dpryi = (flipY) ? -destStride : destStride;

	/*** Prepare special processing data ***/
	int apr;
	if ((op & BLIT_ALPHA) != 0) {
		switch (alphaMode) {
			case ALPHA_MASK_UNPACKED:
			case ALPHA_CHANNEL_SEPARATE:
				if (alphaData == null) alphaMode = 0x10000;
				apr = alphaY * alphaStride + alphaX;
				break;
			case ALPHA_MASK_PACKED:
				if (alphaData == null) alphaMode = 0x10000;
				alphaStride <<= 3;
				apr = alphaY * alphaStride + alphaX;
				break;
			case ALPHA_MASK_INDEX:
			case ALPHA_MASK_RGB:
				if (alphaData == null) alphaMode = 0x10000;
				apr = 0;
				break;
			default:
				alphaMode = (alphaMode << 16) / 255; // prescale
			case ALPHA_CHANNEL_SOURCE:
				apr = 0;
				break;
		}
	} else {
		alphaMode = 0x10000;
		apr = 0;
	}

	/*** Comprehensive blit (apply transformations) ***/
	final int destRedShift = getChannelShift(destRedMask);
	final int destRedWidth = getChannelWidth(destRedMask, destRedShift);
	final byte[] destReds = ANY_TO_EIGHT[destRedWidth];
	final int destRedPreShift = 8 - destRedWidth;
	final int destGreenShift = getChannelShift(destGreenMask);
	final int destGreenWidth = getChannelWidth(destGreenMask, destGreenShift);
	final byte[] destGreens = ANY_TO_EIGHT[destGreenWidth];
	final int destGreenPreShift = 8 - destGreenWidth;
	final int destBlueShift = getChannelShift(destBlueMask);
	final int destBlueWidth = getChannelWidth(destBlueMask, destBlueShift);
	final byte[] destBlues = ANY_TO_EIGHT[destBlueWidth];
	final int destBluePreShift = 8 - destBlueWidth;
	final int destAlphaShift = getChannelShift(destAlphaMask);
	final int destAlphaWidth = getChannelWidth(destAlphaMask, destAlphaShift);
	final byte[] destAlphas = ANY_TO_EIGHT[destAlphaWidth];
	final int destAlphaPreShift = 8 - destAlphaWidth;

	int dp = dpr;
	int sp = spr;
	int ap = apr, alpha = alphaMode;
	int r = 0, g = 0, b = 0, a = 0, index = 0;
	int rq = 0, gq = 0, bq = 0, aq = 0;
	for (int dy = destHeight, sfy = sfyi; dy > 0; --dy,
			sp = spr += (sfy >>> 16) * srcStride,
			ap = apr += (sfy >>> 16) * alphaStride,
			sfy = (sfy & 0xffff) + sfyi,
			dp = dpr += dpryi) {
		for (int dx = destWidth, sfx = sfxi; dx > 0; --dx,
				dp += dprxi,
				sfx = (sfx & 0xffff) + sfxi) {
			/*** READ NEXT PIXEL ***/
			switch (stype) {
				case TYPE_INDEX_8:
					index = srcData[sp] & 0xff;
					sp += (sfx >>> 16);
					break;
				case TYPE_INDEX_4:
					if ((sp & 1) != 0) index = srcData[sp >> 1] & 0x0f;
					else index = (srcData[sp >> 1] >>> 4) & 0x0f;
					sp += (sfx >>> 16);
					break;
				case TYPE_INDEX_2:
					index = (srcData[sp >> 2] >>> (6 - (sp & 3) * 2)) & 0x03;
					sp += (sfx >>> 16);
					break;
				case TYPE_INDEX_1_MSB:
					index = (srcData[sp >> 3] >>> (7 - (sp & 7))) & 0x01;
					sp += (sfx >>> 16);
					break;
				case TYPE_INDEX_1_LSB:
					index = (srcData[sp >> 3] >>> (sp & 7)) & 0x01;
					sp += (sfx >>> 16);
					break;
			}

			/*** DO SPECIAL PROCESSING IF REQUIRED ***/
			r = srcReds[index] & 0xff;
			g = srcGreens[index] & 0xff;
			b = srcBlues[index] & 0xff;
			switch (alphaMode) {
				case ALPHA_CHANNEL_SEPARATE:
					alpha = ((alphaData[ap] & 0xff) << 16) / 255;
					ap += (sfx >> 16);
					break;
				case ALPHA_MASK_UNPACKED:
					alpha = (alphaData[ap] != 0) ? 0x10000 : 0;
					ap += (sfx >> 16);
					break;						
				case ALPHA_MASK_PACKED:
					alpha = (alphaData[ap >> 3] << ((ap & 7) + 9)) & 0x10000;
					ap += (sfx >> 16);
					break;
				case ALPHA_MASK_INDEX: { // could speed up using binary search if we sorted the indices
					int i = 0;
					while (i < alphaData.length) {
						if (index == (alphaData[i] & 0xff)) break;
					}
					if (i < alphaData.length) continue;
				} break;
				case ALPHA_MASK_RGB: {
					int i = 0;
					while (i < alphaData.length) {
						if ((r == (alphaData[i] & 0xff)) &&
							(g == (alphaData[i + 1] & 0xff)) &&
							(b == (alphaData[i + 2] & 0xff))) break;
						i += 3;
					}
					if (i < alphaData.length) continue;
				} break;
			}
			if (alpha != 0x10000) {
				if (alpha == 0x0000) continue;
				switch (dtype) {
					case TYPE_GENERIC_8: {
						final int data = destData[dp] & 0xff;
						rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
						gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
						bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
						aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
					} break;
					case TYPE_GENERIC_16_MSB: {
						final int data = ((destData[dp] & 0xff) << 8) | (destData[dp + 1] & 0xff);
						rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
						gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
						bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
						aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
					} break;
					case TYPE_GENERIC_16_LSB: {
						final int data = ((destData[dp + 1] & 0xff) << 8) | (destData[dp] & 0xff);
						rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
						gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
						bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
						aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
					} break;
					case TYPE_GENERIC_24: {
						final int data = (( ((destData[dp] & 0xff) << 8) |
							(destData[dp + 1] & 0xff)) << 8) |
							(destData[dp + 2] & 0xff);
						rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
						gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
						bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
						aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
					} break;
					case TYPE_GENERIC_32_MSB: {
						final int data = (( (( ((destData[dp] & 0xff) << 8) |
							(destData[dp + 1] & 0xff)) << 8) |
							(destData[dp + 2] & 0xff)) << 8) |
							(destData[dp + 3] & 0xff);
						rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
						gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
						bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
						aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
					} break;
					case TYPE_GENERIC_32_LSB: {
						final int data = (( (( ((destData[dp + 3] & 0xff) << 8) |
							(destData[dp + 2] & 0xff)) << 8) |
							(destData[dp + 1] & 0xff)) << 8) |
							(destData[dp] & 0xff);
						rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
						gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
						bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
						aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
					} break;
				}
				// Perform alpha blending
				a = aq + ((a - aq) * alpha >> 16);
				r = rq + ((r - rq) * alpha >> 16);
				g = gq + ((g - gq) * alpha >> 16);
				b = bq + ((b - bq) * alpha >> 16);
			}

			/*** WRITE NEXT PIXEL ***/
			final int data = 
				(r >>> destRedPreShift << destRedShift) |
				(g >>> destGreenPreShift << destGreenShift) |
				(b >>> destBluePreShift << destBlueShift) |
				(a >>> destAlphaPreShift << destAlphaShift);
			switch (dtype) {
				case TYPE_GENERIC_8: {
					destData[dp] = (byte) data;
				} break;
				case TYPE_GENERIC_16_MSB: {
					destData[dp] = (byte) (data >>> 8);
					destData[dp + 1] = (byte) (data & 0xff);
				} break;
				case TYPE_GENERIC_16_LSB: {
					destData[dp] = (byte) (data & 0xff);
					destData[dp + 1] = (byte) (data >>> 8);
				} break;
				case TYPE_GENERIC_24: {
					destData[dp] = (byte) (data >>> 16);
					destData[dp + 1] = (byte) (data >>> 8);
					destData[dp + 2] = (byte) (data & 0xff);
				} break;
				case TYPE_GENERIC_32_MSB: {
					destData[dp] = (byte) (data >>> 24);
					destData[dp + 1] = (byte) (data >>> 16);
					destData[dp + 2] = (byte) (data >>> 8);
					destData[dp + 3] = (byte) (data & 0xff);
				} break;
				case TYPE_GENERIC_32_LSB: {
					destData[dp] = (byte) (data & 0xff);
					destData[dp + 1] = (byte) (data >>> 8);
					destData[dp + 2] = (byte) (data >>> 16);
					destData[dp + 3] = (byte) (data >>> 24);
				} break;
			}
		}
	}			
}

/**
 * Blits a direct palette image into an index palette image.
 * <p>
 * Note: The source and destination masks and palettes must
 * always be fully specified.
 * </p>
 * 
 * @param op the blitter operation: a combination of BLIT_xxx flags
 *        (see BLIT_xxx constants)
 * @param srcData the source byte array containing image data
 * @param srcDepth the source depth: one of 8, 16, 24, 32
 * @param srcStride the source number of bytes per line
 * @param srcOrder the source byte ordering: one of MSB_FIRST or LSB_FIRST;
 *        ignored if srcDepth is not 16 or 32
 * @param srcX the top-left x-coord of the source blit region
 * @param srcY the top-left y-coord of the source blit region
 * @param srcWidth the width of the source blit region
 * @param srcHeight the height of the source blit region
 * @param srcRedMask the source red channel mask
 * @param srcGreenMask the source green channel mask
 * @param srcBlueMask the source blue channel mask
 * @param alphaMode the alpha blending or mask mode, may be
 *        an integer 0-255 for global alpha; ignored if BLIT_ALPHA
 *        not specified in the blitter operations
 *        (see ALPHA_MODE_xxx constants)
 * @param alphaData the alpha blending or mask data, varies depending
 *        on the value of alphaMode and sometimes ignored
 * @param alphaStride the alpha data number of bytes per line
 * @param alphaX the top-left x-coord of the alpha blit region
 * @param alphaY the top-left y-coord of the alpha blit region
 * @param destData the destination byte array containing image data
 * @param destDepth the destination depth: one of 1, 2, 4, 8
 * @param destStride the destination number of bytes per line
 * @param destOrder the destination byte ordering: one of MSB_FIRST or LSB_FIRST;
 *        ignored if destDepth is not 1
 * @param destX the top-left x-coord of the destination blit region
 * @param destY the top-left y-coord of the destination blit region
 * @param destWidth the width of the destination blit region
 * @param destHeight the height of the destination blit region
 * @param destReds the destination palette red component intensities
 * @param destGreens the destination palette green component intensities
 * @param destBlues the destination palette blue component intensities
 * @param flipX if true the resulting image is flipped along the vertical axis
 * @param flipY if true the resulting image is flipped along the horizontal axis
 */
static void blit(int op,
	byte[] srcData, int srcDepth, int srcStride, int srcOrder,
	int srcX, int srcY, int srcWidth, int srcHeight,
	int srcRedMask, int srcGreenMask, int srcBlueMask,
	int alphaMode, byte[] alphaData, int alphaStride, int alphaX, int alphaY,
	byte[] destData, int destDepth, int destStride, int destOrder,
	int destX, int destY, int destWidth, int destHeight,
	byte[] destReds, byte[] destGreens, byte[] destBlues,
	boolean flipX, boolean flipY) {
	if ((destWidth <= 0) || (destHeight <= 0) || (alphaMode == ALPHA_TRANSPARENT)) return;

	// these should be supplied as params later
	final int srcAlphaMask = 0;

	/*** Prepare scaling data ***/
	final int dwm1 = destWidth - 1;
	final int sfxi = (dwm1 != 0) ? (int)((((long)srcWidth << 16) - 1) / dwm1) : 0;
	final int dhm1 = destHeight - 1;
	final int sfyi = (dhm1 != 0) ? (int)((((long)srcHeight << 16) - 1) / dhm1) : 0;

	/*** Prepare source-related data ***/
	final int sbpp, stype;
	switch (srcDepth) {
		case 8:
			sbpp = 1;
			stype = TYPE_GENERIC_8;
			break;
		case 16:
			sbpp = 2;
			stype = (srcOrder == MSB_FIRST) ? TYPE_GENERIC_16_MSB : TYPE_GENERIC_16_LSB;
			break;
		case 24:
			sbpp = 3;
			stype = TYPE_GENERIC_24;
			break;
		case 32:
			sbpp = 4;
			stype = (srcOrder == MSB_FIRST) ? TYPE_GENERIC_32_MSB : TYPE_GENERIC_32_LSB;
			break;
		default:
			//throw new IllegalArgumentException("Invalid source type");
			return;
	}			
	int spr = srcY * srcStride + srcX * sbpp;

	/*** Prepare destination-related data ***/
	final int dtype;
	switch (destDepth) {
		case 8:
			dtype = TYPE_INDEX_8;
			break;
		case 4:
			destStride <<= 1;
			dtype = TYPE_INDEX_4;
			break;
		case 2:
			destStride <<= 2;
			dtype = TYPE_INDEX_2;
			break;
		case 1:
			destStride <<= 3;
			dtype = (destOrder == MSB_FIRST) ? TYPE_INDEX_1_MSB : TYPE_INDEX_1_LSB;
			break;
		default:
			//throw new IllegalArgumentException("Invalid source type");
			return;
	}			
	int dpr = ((flipY) ? destY + dhm1 : destY) * destStride + ((flipX) ? destX + dwm1 : destX);
	final int dprxi = (flipX) ? -1 : 1;
	final int dpryi = (flipY) ? -destStride : destStride;

	/*** Prepare special processing data ***/
	int apr;
	if ((op & BLIT_ALPHA) != 0) {
		switch (alphaMode) {
			case ALPHA_MASK_UNPACKED:
			case ALPHA_CHANNEL_SEPARATE:
				if (alphaData == null) alphaMode = 0x10000;
				apr = alphaY * alphaStride + alphaX;
				break;
			case ALPHA_MASK_PACKED:
				if (alphaData == null) alphaMode = 0x10000;
				alphaStride <<= 3;
				apr = alphaY * alphaStride + alphaX;
				break;
			case ALPHA_MASK_INDEX:
				//throw new IllegalArgumentException("Invalid alpha type");
				return;
			case ALPHA_MASK_RGB:
				if (alphaData == null) alphaMode = 0x10000;
				apr = 0;
				break;
			default:
				alphaMode = (alphaMode << 16) / 255; // prescale
			case ALPHA_CHANNEL_SOURCE:
				apr = 0;
				break;
		}
	} else {
		alphaMode = 0x10000;
		apr = 0;
	}
	final boolean ditherEnabled = (op & BLIT_DITHER) != 0;

	/*** Comprehensive blit (apply transformations) ***/
	final int srcRedShift = getChannelShift(srcRedMask);
	final byte[] srcReds = ANY_TO_EIGHT[getChannelWidth(srcRedMask, srcRedShift)];
	final int srcGreenShift = getChannelShift(srcGreenMask);
	final byte[] srcGreens = ANY_TO_EIGHT[getChannelWidth(srcGreenMask, srcGreenShift)];
	final int srcBlueShift = getChannelShift(srcBlueMask);
	final byte[] srcBlues = ANY_TO_EIGHT[getChannelWidth(srcBlueMask, srcBlueShift)];
	final int srcAlphaShift = getChannelShift(srcAlphaMask);
	final byte[] srcAlphas = ANY_TO_EIGHT[getChannelWidth(srcAlphaMask, srcAlphaShift)];

	int dp = dpr;
	int sp = spr;
	int ap = apr, alpha = alphaMode;
	int r = 0, g = 0, b = 0, a = 0;
	int indexq = 0;
	int lastindex = 0, lastr = -1, lastg = -1, lastb = -1;
	final int[] rerr, gerr, berr;
	int destPaletteSize = 1 << destDepth;
	if ((destReds != null) && (destReds.length < destPaletteSize)) destPaletteSize = destReds.length;
	if (ditherEnabled) {
		rerr = new int[destWidth + 2];
		gerr = new int[destWidth + 2];
		berr = new int[destWidth + 2];
	} else {
		rerr = null; gerr = null; berr = null;
	}
	for (int dy = destHeight, sfy = sfyi; dy > 0; --dy,
			sp = spr += (sfy >>> 16) * srcStride,
			ap = apr += (sfy >>> 16) * alphaStride,
			sfy = (sfy & 0xffff) + sfyi,
			dp = dpr += dpryi) {
		int lrerr = 0, lgerr = 0, lberr = 0;
		for (int dx = destWidth, sfx = sfxi; dx > 0; --dx,
				dp += dprxi,
				sfx = (sfx & 0xffff) + sfxi) {
			/*** READ NEXT PIXEL ***/
			switch (stype) {
				case TYPE_GENERIC_8: {
					final int data = srcData[sp] & 0xff;
					sp += (sfx >>> 16);
					r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
					g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
					b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
					a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
				} break;
				case TYPE_GENERIC_16_MSB: {
					final int data = ((srcData[sp] & 0xff) << 8) | (srcData[sp + 1] & 0xff);
					sp += (sfx >>> 16) * 2;
					r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
					g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
					b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
					a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
				} break;
				case TYPE_GENERIC_16_LSB: {
					final int data = ((srcData[sp + 1] & 0xff) << 8) | (srcData[sp] & 0xff);
					sp += (sfx >>> 16) * 2;
					r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
					g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
					b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
					a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
				} break;
				case TYPE_GENERIC_24: {
					final int data = (( ((srcData[sp] & 0xff) << 8) |
						(srcData[sp + 1] & 0xff)) << 8) |
						(srcData[sp + 2] & 0xff);
					sp += (sfx >>> 16) * 3;
					r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
					g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
					b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
					a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
				} break;
				case TYPE_GENERIC_32_MSB: {
					final int data = (( (( ((srcData[sp] & 0xff) << 8) |
						(srcData[sp + 1] & 0xff)) << 8) |
						(srcData[sp + 2] & 0xff)) << 8) |
						(srcData[sp + 3] & 0xff);
					sp += (sfx >>> 16) * 4;
					r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
					g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
					b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
					a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
				} break;
				case TYPE_GENERIC_32_LSB: {
					final int data = (( (( ((srcData[sp + 3] & 0xff) << 8) |
						(srcData[sp + 2] & 0xff)) << 8) |
						(srcData[sp + 1] & 0xff)) << 8) |
						(srcData[sp] & 0xff);
					sp += (sfx >>> 16) * 4;
					r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
					g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
					b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
					a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
				} break;
			}

			/*** DO SPECIAL PROCESSING IF REQUIRED ***/
			switch (alphaMode) {
				case ALPHA_CHANNEL_SEPARATE:
					alpha = ((alphaData[ap] & 0xff) << 16) / 255;
					ap += (sfx >> 16);
					break;
				case ALPHA_CHANNEL_SOURCE:
					alpha = (a << 16) / 255;
					break;
				case ALPHA_MASK_UNPACKED:
					alpha = (alphaData[ap] != 0) ? 0x10000 : 0;
					ap += (sfx >> 16);
					break;						
				case ALPHA_MASK_PACKED:
					alpha = (alphaData[ap >> 3] << ((ap & 7) + 9)) & 0x10000;
					ap += (sfx >> 16);
					break;
				case ALPHA_MASK_RGB:
					alpha = 0x10000;
					for (int i = 0; i < alphaData.length; i += 3) {
						if ((r == alphaData[i]) && (g == alphaData[i + 1]) && (b == alphaData[i + 2])) {
							alpha = 0x0000;
							break;
						}
					}
					break;
			}
			if (alpha != 0x10000) {
				if (alpha == 0x0000) continue;
				switch (dtype) {
					case TYPE_INDEX_8:
						indexq = destData[dp] & 0xff;
						break;
					case TYPE_INDEX_4:
						if ((dp & 1) != 0) indexq = destData[dp >> 1] & 0x0f;
						else indexq = (destData[dp >> 1] >>> 4) & 0x0f;
						break;
					case TYPE_INDEX_2:
						indexq = (destData[dp >> 2] >>> (6 - (dp & 3) * 2)) & 0x03;
						break;
					case TYPE_INDEX_1_MSB:
						indexq = (destData[dp >> 3] >>> (7 - (dp & 7))) & 0x01;
						break;
					case TYPE_INDEX_1_LSB:
						indexq = (destData[dp >> 3] >>> (dp & 7)) & 0x01;
						break;
				}
				// Perform alpha blending
				final int rq = destReds[indexq] & 0xff;
				final int gq = destGreens[indexq] & 0xff;
				final int bq = destBlues[indexq] & 0xff;
				r = rq + ((r - rq) * alpha >> 16);
				g = gq + ((g - gq) * alpha >> 16);
				b = bq + ((b - bq) * alpha >> 16);
			}

			/*** MAP COLOR TO THE PALETTE ***/
			if (ditherEnabled) {
				// Floyd-Steinberg error diffusion
				r += rerr[dx] >> 4;
				if (r < 0) r = 0; else if (r > 255) r = 255;
				g += gerr[dx] >> 4;
				if (g < 0) g = 0; else if (g > 255) g = 255;
				b += berr[dx] >> 4;
				if (b < 0) b = 0; else if (b > 255) b = 255;
				rerr[dx] = lrerr;
				gerr[dx] = lgerr;
				berr[dx] = lberr;
			}
			if (r != lastr || g != lastg || b != lastb) {
				// moving the variable declarations out seems to make the JDK JIT happier...
				for (int j = 0, dr, dg, db, distance, minDistance = 0x7fffffff; j < destPaletteSize; ++j) {
					dr = (destReds[j] & 0xff) - r;
					dg = (destGreens[j] & 0xff) - g;
					db = (destBlues[j] & 0xff) - b;
					distance = dr * dr + dg * dg + db * db;
					if (distance < minDistance) {
						lastindex = j;
						if (distance == 0) break;
						minDistance = distance;
					}
				}
				lastr = r; lastg = g; lastb = b;
			}
			if (ditherEnabled) {
				// Floyd-Steinberg error diffusion, cont'd...
				final int dxm1 = dx - 1, dxp1 = dx + 1;
				int acc;
				rerr[dxp1] += acc = (lrerr = r - (destReds[lastindex] & 0xff)) + lrerr + lrerr;
				rerr[dx] += acc += lrerr + lrerr;
				rerr[dxm1] += acc + lrerr + lrerr;
				gerr[dxp1] += acc = (lgerr = g - (destGreens[lastindex] & 0xff)) + lgerr + lgerr;
				gerr[dx] += acc += lgerr + lgerr;
				gerr[dxm1] += acc + lgerr + lgerr;
				berr[dxp1] += acc = (lberr = b - (destBlues[lastindex] & 0xff)) + lberr + lberr;
				berr[dx] += acc += lberr + lberr;
				berr[dxm1] += acc + lberr + lberr;
			}

			/*** WRITE NEXT PIXEL ***/
			switch (dtype) {
				case TYPE_INDEX_8:
					destData[dp] = (byte) lastindex;
					break;
				case TYPE_INDEX_4:
					if ((dp & 1) != 0) destData[dp >> 1] = (byte)((destData[dp >> 1] & 0xf0) | lastindex);
					else destData[dp >> 1] = (byte)((destData[dp >> 1] & 0x0f) | (lastindex << 4));
					break;
				case TYPE_INDEX_2: {
					final int shift = 6 - (dp & 3) * 2;
					destData[dp >> 2] = (byte)(destData[dp >> 2] & ~(0x03 << shift) | (lastindex << shift));
				} break;					
				case TYPE_INDEX_1_MSB: {
					final int shift = 7 - (dp & 7);
					destData[dp >> 3] = (byte)(destData[dp >> 3] & ~(0x01 << shift) | (lastindex << shift));
				} break;
				case TYPE_INDEX_1_LSB: {
					final int shift = dp & 7;
					destData[dp >> 3] = (byte)(destData[dp >> 3] & ~(0x01 << shift) | (lastindex << shift));
				} break;					
			}
		}
	}
}

/**
 * Computes the required channel shift from a mask.
 */
static int getChannelShift(int mask) {
	if (mask == 0) return 0;
	int i;
	for (i = 0; ((mask & 1) == 0) && (i < 32); ++i) {
		mask >>>= 1;
	}
	return i;
}

/**
 * Computes the required channel width (depth) from a mask.
 */
static int getChannelWidth(int mask, int shift) {
	if (mask == 0) return 0;
	int i;
	mask >>>= shift;
	for (i = shift; ((mask & 1) != 0) && (i < 32); ++i) {
		mask >>>= 1;
	}
	return i - shift;
}

/**
 * Extracts a field from packed RGB data given a mask for that field.
 */
static byte getChannelField(int data, int mask) {
	final int shift = getChannelShift(mask);
	return ANY_TO_EIGHT[getChannelWidth(mask, shift)][(data & mask) >>> shift];
}

/**
 * Creates an ImageData containing one band's worth of a gradient filled
 * block.  If <code>vertical</code> is true, the band must be tiled
 * horizontally to fill a region, otherwise it must be tiled vertically.
 *
 * @param width the width of the region to be filled
 * @param height the height of the region to be filled
 * @param vertical if true sweeps from top to bottom, else
 *        sweeps from left to right
 * @param fromRGB the color to start with
 * @param toRGB the color to end with
 * @param redBits the number of significant red bits, 0 for palette modes
 * @param greenBits the number of significant green bits, 0 for palette modes
 * @param blueBits the number of significant blue bits, 0 for palette modes
 * @return the new ImageData
 */
static ImageData createGradientBand(
	int width, int height, boolean vertical,
	RGB fromRGB, RGB toRGB,
	int redBits, int greenBits, int blueBits) {
	/* Gradients are drawn as tiled bands */
	final int bandWidth, bandHeight, bitmapDepth;
	final byte[] bitmapData;
	final PaletteData paletteData;
	/* Select an algorithm depending on the depth of the screen */
	if (redBits != 0 && greenBits != 0 && blueBits != 0) {
		paletteData = new PaletteData(0x0000ff00, 0x00ff0000, 0xff000000);
		bitmapDepth = 32;
		if (redBits >= 8 && greenBits >= 8 && blueBits >= 8) {
			/* Precise color */
			final int steps;
			if (vertical) {
				bandWidth = 1;
				bandHeight = height;
				steps = bandHeight > 1 ? bandHeight - 1 : 1;
			} else {
				bandWidth = width;
				bandHeight = 1;
				steps = bandWidth > 1 ? bandWidth - 1 : 1;
			}
			final int bytesPerLine = bandWidth * 4;
			bitmapData = new byte[bandHeight * bytesPerLine];
			buildPreciseGradientChannel(fromRGB.blue, toRGB.blue, steps, bandWidth, bandHeight, vertical, bitmapData, 0, bytesPerLine);
			buildPreciseGradientChannel(fromRGB.green, toRGB.green, steps, bandWidth, bandHeight, vertical, bitmapData, 1, bytesPerLine);
			buildPreciseGradientChannel(fromRGB.red, toRGB.red, steps, bandWidth, bandHeight, vertical, bitmapData, 2, bytesPerLine);
		} else {
			/* Dithered color */
			final int steps;
			if (vertical) {
				bandWidth = (width < 8) ? width : 8;
				bandHeight = height;
				steps = bandHeight > 1 ? bandHeight - 1 : 1;
			} else {
				bandWidth = width;
				bandHeight = (height < 8) ? height : 8;
				steps = bandWidth > 1 ? bandWidth - 1 : 1;
			}
			final int bytesPerLine = bandWidth * 4;
			bitmapData = new byte[bandHeight * bytesPerLine];
			buildDitheredGradientChannel(fromRGB.blue, toRGB.blue, steps, bandWidth, bandHeight, vertical, bitmapData, 0, bytesPerLine, blueBits);
			buildDitheredGradientChannel(fromRGB.green, toRGB.green, steps, bandWidth, bandHeight, vertical, bitmapData, 1, bytesPerLine, greenBits);
			buildDitheredGradientChannel(fromRGB.red, toRGB.red, steps, bandWidth, bandHeight, vertical, bitmapData, 2, bytesPerLine, redBits);			
		}
	} else {
		/* Dithered two tone */
		paletteData = new PaletteData(new RGB[] { fromRGB, toRGB });
		bitmapDepth = 8;
		final int blendi;
		if (vertical) {
			bandWidth = (width < 8) ? width : 8;
			bandHeight = height;
			blendi = (bandHeight > 1) ? 0x1040000 / (bandHeight - 1) + 1 : 1;
		} else {
			bandWidth = width;
			bandHeight = (height < 8) ? height : 8;
			blendi = (bandWidth > 1) ? 0x1040000 / (bandWidth - 1) + 1 : 1;
		}
		final int bytesPerLine = (bandWidth + 3) & -4;
		bitmapData = new byte[bandHeight * bytesPerLine];
		if (vertical) {
			for (int dy = 0, blend = 0, dp = 0; dy < bandHeight;
				++dy, blend += blendi, dp += bytesPerLine) {
				for (int dx = 0; dx < bandWidth; ++dx) {
					bitmapData[dp + dx] = (blend + DITHER_MATRIX[dy & 7][dx]) <
						0x1000000 ? (byte)0 : (byte)1;
				}
			}		
		} else {
			for (int dx = 0, blend = 0; dx < bandWidth; ++dx, blend += blendi) {
				for (int dy = 0, dptr = dx; dy < bandHeight; ++dy, dptr += bytesPerLine) {
					bitmapData[dptr] = (blend + DITHER_MATRIX[dy][dx & 7]) <
						0x1000000 ? (byte)0 : (byte)1;
				}
			}
		}
	}
	return new ImageData(bandWidth, bandHeight, bitmapDepth, paletteData, 4, bitmapData);
}

/* 
 * Fill in gradated values for a color channel
 */
static final void buildPreciseGradientChannel(int from, int to, int steps,
	int bandWidth, int bandHeight, boolean vertical,
	byte[] bitmapData, int dp, int bytesPerLine) {
	int val = from << 16;
	final int inc = ((to << 16) - val) / steps + 1;
	if (vertical) {
		for (int dy = 0; dy < bandHeight; ++dy, dp += bytesPerLine) {
			bitmapData[dp] = (byte)(val >>> 16);
			val += inc;
		}
	} else {
		for (int dx = 0; dx < bandWidth; ++dx, dp += 4) {
			bitmapData[dp] = (byte)(val >>> 16);
			val += inc;
		}
	}		
}

/* 
 * Fill in dithered gradated values for a color channel
 */
static final void buildDitheredGradientChannel(int from, int to, int steps,
	int bandWidth, int bandHeight, boolean vertical,
	byte[] bitmapData, int dp, int bytesPerLine, int bits) {
	final int mask = 0xff00 >>> bits;
	int val = from << 16;
	final int inc = ((to << 16) - val) / steps + 1;
	if (vertical) {
		for (int dy = 0; dy < bandHeight; ++dy, dp += bytesPerLine) {
			for (int dx = 0, dptr = dp; dx < bandWidth; ++dx, dptr += 4) {
				final int thresh = DITHER_MATRIX[dy & 7][dx] >>> bits;
				int temp = val + thresh;
				if (temp > 0xffffff) bitmapData[dptr] = -1;
				else bitmapData[dptr] = (byte)((temp >>> 16) & mask);
			}
			val += inc;
		}
	} else {
		for (int dx = 0; dx < bandWidth; ++dx, dp += 4) {
			for (int dy = 0, dptr = dp; dy < bandHeight; ++dy, dptr += bytesPerLine) {
				final int thresh = DITHER_MATRIX[dy][dx & 7] >>> bits;
				int temp = val + thresh;
				if (temp > 0xffffff) bitmapData[dptr] = -1;
				else bitmapData[dptr] = (byte)((temp >>> 16) & mask);
			}
			val += inc;
		}
	}
}
}
public static class LEDataOutputStream extends OutputStream {
	OutputStream out;
public LEDataOutputStream(OutputStream output) {
	this.out = output;
}
public void close() throws IOException {
	out.close();
}
public void write(byte b[], int off, int len) throws IOException {
	out.write(b, off, len);
}
/**
 * Write the given byte to the output stream.
 */
public void write(int b) throws IOException {
	out.write(b);
}
/**
 * Write the given byte to the output stream.
 */
public void writeByte(byte b) throws IOException {
	out.write(b & 0xFF);
}
/**
 * Write the four bytes of the given integer
 * to the output stream.
 */
public void writeInt(int theInt) throws IOException {
	out.write(theInt & 0xFF);
	out.write((theInt >> 8) & 0xFF);
	out.write((theInt >> 16) & 0xFF);
	out.write((theInt >> 24) & 0xFF);
}
/**
 * Write the two bytes of the given short
 * to the output stream.
 */
public void writeShort(int theShort) throws IOException {
	out.write(theShort & 0xFF);
	out.write((theShort >> 8) & 0xFF);
}
}
public static class LEDataInputStream extends InputStream {
	int position;
	InputStream in;

	/**
	 * The byte array containing the bytes to read.
	 */
	protected byte[] buf;
	
	/**
	 * The current position within the byte array <code>buf</code>. A value
	 * equal to buf.length indicates no bytes available.  A value of
	 * 0 indicates the buffer is full.
	 */
	protected int pos;
	

	public LEDataInputStream(InputStream input) {
		this(input, 512);
	}
	
	public LEDataInputStream(InputStream input, int bufferSize) {
		this.in = input;
		if (bufferSize > 0) {
			buf = new byte[bufferSize];
			pos = bufferSize;
		} 
		else throw new IllegalArgumentException();
	}
	
	public void close() throws IOException {
		buf = null;
		if (in != null) {
			in.close();
			in = null;
		}
	}
	
	/**
	 * Answer how many bytes were read.
	 */
	public int getPosition() {
		return position;
	}
	
	/**
	 * Answers how many bytes are available for reading without blocking
	 */
	public int available() throws IOException {
		if (buf == null) throw new IOException();
		return (buf.length - pos) + in.available();
	}
	
	/**
	 * Answer the next byte of the input stream.
	 */
	public int read() throws IOException {
		if (buf == null) throw new IOException();
		position++;
		if (pos < buf.length) return (buf[pos++] & 0xFF);
		return in.read();
	}
	
	/**
	 * Don't imitate the JDK behaviour of reading a random number
	 * of bytes when you can actually read them all.
	 */
	public int read(byte b[], int off, int len) throws IOException {
		int result;
		int left = len;
		result = readData(b, off, len);
		while (true) {
			if (result == -1) return -1;
			position += result;
			if (result == left) return len;
			left -= result;
			off += result;
			result = readData(b, off, left);
		}
	}
	
	/**
 	 * Reads at most <code>length</code> bytes from this LEDataInputStream and 
 	 * stores them in byte array <code>buffer</code> starting at <code>offset</code>.
 	 * <p>
 	 * Answer the number of bytes actually read or -1 if no bytes were read and 
 	 * end of stream was encountered.  This implementation reads bytes from 
 	 * the pushback buffer first, then the target stream if more bytes are required
 	 * to satisfy <code>count</code>.
	 * </p>
	 * @param buffer the byte array in which to store the read bytes.
	 * @param offset the offset in <code>buffer</code> to store the read bytes.
	 * @param length the maximum number of bytes to store in <code>buffer</code>.
	 *
	 * @return int the number of bytes actually read or -1 if end of stream.
	 *
	 * @exception java.io.IOException if an IOException occurs.
	 */
	private int readData(byte[] buffer, int offset, int length) throws IOException {
		if (buf == null) throw new IOException();
		if (offset < 0 || offset > buffer.length ||
  		 	length < 0 || (length > buffer.length - offset)) {
	 		throw new ArrayIndexOutOfBoundsException();
		 	}
				
		int cacheCopied = 0;
		int newOffset = offset;
	
		// Are there pushback bytes available?
		int available = buf.length - pos;
		if (available > 0) {
			cacheCopied = (available >= length) ? length : available;
			System.arraycopy(buf, pos, buffer, newOffset, cacheCopied);
			newOffset += cacheCopied;
			pos += cacheCopied;
		}
	
		// Have we copied enough?
		if (cacheCopied == length) return length;

		int inCopied = in.read(buffer, newOffset, length - cacheCopied);

		if (inCopied > 0) return inCopied + cacheCopied;
		if (cacheCopied == 0) return inCopied;
		return cacheCopied;
	}
	
	/**
	 * Answer an integer comprised of the next
	 * four bytes of the input stream.
	 */
	public int readInt() throws IOException {
		byte[] buf = new byte[4];
		read(buf);
		return ((((((buf[3] & 0xFF) << 8) | 
			(buf[2] & 0xFF)) << 8) | 
			(buf[1] & 0xFF)) << 8) | 
			(buf[0] & 0xFF);
	}
	
	/**
	 * Answer a short comprised of the next
	 * two bytes of the input stream.
	 */
	public short readShort() throws IOException {
		byte[] buf = new byte[2];
		read(buf);
		return (short)(((buf[1] & 0xFF) << 8) | (buf[0] & 0xFF));
	}
	
	/**
	 * Push back the entire content of the given buffer <code>b</code>.
	 * <p>
	 * The bytes are pushed so that they would be read back b[0], b[1], etc. 
	 * If the push back buffer cannot handle the bytes copied from <code>b</code>, 
	 * an IOException will be thrown and no byte will be pushed back.
	 * </p>
	 * 
	 * @param b the byte array containing bytes to push back into the stream
	 *
	 * @exception 	java.io.IOException if the pushback buffer is too small
	 */
	public void unread(byte[] b) throws IOException {
		int length = b.length;
		if (length > pos) throw new IOException();
		position -= length;
		pos -= length;
		System.arraycopy(b, 0, buf, pos, length);
	}
}
public static abstract class FileFormat {
	static final String FORMAT_PACKAGE = "org.eclipse.swt.internal.image"; //$NON-NLS-1$
	static final String FORMAT_SUFFIX = "FileFormat"; //$NON-NLS-1$
	static final String[] FORMATS = {"WinBMP", "WinBMP", "GIF", "WinICO", "JPEG", "PNG", "TIFF", "OS2BMP"}; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$//$NON-NLS-5$ //$NON-NLS-6$//$NON-NLS-7 $//$NON-NLS-8$
	
	LEDataInputStream inputStream;
	LEDataOutputStream outputStream;
	ImageLoader loader;
	int compression;

byte[] bitInvertData(byte[] data, int startIndex, int endIndex) {
	// Destructively bit invert data in the given byte array.
	for (int i = startIndex; i < endIndex; i++) {
		data[i] = (byte)(255 - data[i - startIndex]);
	}
	return data;
}

/**
 * Return whether or not the specified input stream
 * represents a supported file format.
 */
abstract boolean isFileFormat(LEDataInputStream stream);

abstract ImageData[] loadFromByteStream();

public ImageData[] loadFromStream(LEDataInputStream stream) {
	try {
		inputStream = stream;
		return loadFromByteStream();
	} catch (Exception e) {
		SWT.error(SWT.ERROR_IO, e);
		return null;
	}
}

public static ImageData[] load(InputStream is, ImageLoader loader) {
	LEDataInputStream stream = new LEDataInputStream(is);
	boolean isSupported = false;	
	WinICOFileFormat fileFormat = new WinICOFileFormat();
	if (fileFormat.isFileFormat(stream)) isSupported = true;
	if (!isSupported) SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT);
	fileFormat.loader = loader;
	return fileFormat.loadFromStream(stream);
}
}
public static class WinBMPFileFormat extends FileFormat {
	static final int BMPFileHeaderSize = 14;
	static final int BMPHeaderFixedSize = 40;
	int importantColors;

/**
 * Compress numBytes bytes of image data from src, storing in dest
 * (starting at 0), using the technique specified by comp.
 * If last is true, this indicates the last line of the image.
 * Answer the size of the compressed data.
 */
int compress(int comp, byte[] src, int srcOffset, int numBytes, byte[] dest, boolean last) {
	if (comp == 1) { // BMP_RLE8_COMPRESSION
		return compressRLE8Data(src, srcOffset, numBytes, dest, last);
	}
	if (comp == 2) { // BMP_RLE4_COMPRESSION
		return compressRLE4Data(src, srcOffset, numBytes, dest, last);
	}
	SWT.error(SWT.ERROR_INVALID_IMAGE);
	return 0;
}
int compressRLE4Data(byte[] src, int srcOffset, int numBytes, byte[] dest, boolean last) {
	int sp = srcOffset, end = srcOffset + numBytes, dp = 0;
	int size = 0, left, i, n;
	byte theByte;
	while (sp < end) {
		/* find two consecutive bytes that are the same in the next 128 */
		left = end - sp - 1;
		if (left > 127)
			left = 127;
		for (n = 0; n < left; n++) {
			if (src[sp + n] == src[sp + n + 1])
				break;
		}
		/* if there is only one more byte in the scan line, include it */
		if (n < 127 && n == left)
			n++;
		/* store the intervening data */
		switch (n) {
			case 0:
				break;
			case 1: /* handled separately because 0,2 is a command */
				dest[dp] = 2; dp++; /* 1 byte == 2 pixels */
				dest[dp] = src[sp];
				dp++; sp++;
				size += 2;
				break;
			default:
				dest[dp] = 0; dp++;
				dest[dp] = (byte)(n + n); dp++; /* n bytes = n*2 pixels */
				for (i = n; i > 0; i--) {
					dest[dp] = src[sp];
					dp++; sp++;
				}
				size += 2 + n;
				if ((n & 1) != 0) { /* pad to word */
					dest[dp] = 0;
					dp++;
					size++;
				}
				break;
		}
		/* find the length of the next run (up to 127) and store it */
		left = end - sp;
		if (left > 0) {
			if (left > 127)
				left = 127;
			theByte = src[sp];
			for (n = 1; n < left; n++) {
				if (src[sp + n] != theByte)
					break;
			}
			dest[dp] = (byte)(n + n); dp++; /* n bytes = n*2 pixels */
			dest[dp] = theByte; dp++;
			sp += n;
			size += 2;
		}
	}

	/* store the end of line or end of bitmap codes */
	dest[dp] = 0; dp++;
	if (last) {
		dest[dp] = 1; dp++;
	} else {
		dest[dp] = 0; dp++;
	}
	size += 2;
	
	return size;
}
int compressRLE8Data(byte[] src, int srcOffset, int numBytes, byte[] dest, boolean last) {
	int sp = srcOffset, end = srcOffset + numBytes, dp = 0;
	int size = 0, left, i, n;
	byte theByte;
	while (sp < end) {
		/* find two consecutive bytes that are the same in the next 256 */
		left = end - sp - 1;
		if (left > 254)
			left = 254;
		for (n = 0; n < left; n++) {
			if (src[sp + n] == src[sp + n + 1])
				break;
		}
		/* if there is only one more byte in the scan line, include it */
		if (n == left)
			n++;
		/* store the intervening data */
		switch (n) {
			case 0:
				break;
			case 2: /* handled separately because 0,2 is a command */
				dest[dp] = 1; dp++;
				dest[dp] = src[sp];
				dp++; sp++;
				size += 2;
				/* don't break, fall through */
			case 1: /* handled separately because 0,1 is a command */
				dest[dp] = 1; dp++;
				dest[dp] = src[sp];
				dp++; sp++;
				size += 2;
				break;
			default:
				dest[dp] = 0; dp++;
				dest[dp] = (byte)n; dp++;
				for (i = n; i > 0; i--) {
					dest[dp] = src[sp];
					dp++; sp++;
				}
				size += 2 + n;
				if ((n & 1) != 0) { /* pad to word */
					dest[dp] = 0;
					dp++;
					size++;
				}
				break;
		}
		/* find the length of the next run (up to 255) and store it */
		left = end - sp;
		if (left > 0) {
			if (left > 255)
				left = 255;
			theByte = src[sp];
			for (n = 1; n < left; n++) {
				if (src[sp + n] != theByte)
					break;
			}
			dest[dp] = (byte)n; dp++;
			dest[dp] = theByte; dp++;
			sp += n;
			size += 2;
		}
	}

	/* store the end of line or end of bitmap codes */
	dest[dp] = 0; dp++;
	if (last) {
		dest[dp] = 1; dp++;
	} else {
		dest[dp] = 0; dp++;
	}
	size += 2;
	
	return size;
}
void decompressData(byte[] src, byte[] dest, int stride, int cmp) {
	if (cmp == 1) { // BMP_RLE8_COMPRESSION
		if (decompressRLE8Data(src, src.length, stride, dest, dest.length) <= 0)
			SWT.error(SWT.ERROR_INVALID_IMAGE);
		return;
	}
	if (cmp == 2) { // BMP_RLE4_COMPRESSION
		if (decompressRLE4Data(src, src.length, stride, dest, dest.length) <= 0)
			SWT.error(SWT.ERROR_INVALID_IMAGE);
		return;
	}
	SWT.error(SWT.ERROR_INVALID_IMAGE);
}
int decompressRLE4Data(byte[] src, int numBytes, int stride, byte[] dest, int destSize) {
	int sp = 0;
	int se = numBytes;
	int dp = 0;
	int de = destSize;
	int x = 0, y = 0;
	while (sp < se) {
		int len = src[sp] & 0xFF;
		sp++;
		if (len == 0) {
			len = src[sp] & 0xFF;
			sp++;
			switch (len) {
				case 0: /* end of line */
					y++;
					x = 0;
					dp = y * stride;
					if (dp >= de)
						return -1;
					break;
				case 1: /* end of bitmap */
					return 1;
				case 2: /* delta */
					x += src[sp] & 0xFF;
					sp++;
					y += src[sp] & 0xFF;
					sp++;
					dp = y * stride + x / 2;
					if (dp >= de)
						return -1;
					break;
				default: /* absolute mode run */
					if ((len & 1) != 0) /* odd run lengths not currently supported */
						return -1;
					x += len;
					len = len / 2;
					if (len > (se - sp))
						return -1;
					if (len > (de - dp))
						return -1;
					for (int i = 0; i < len; i++) {
						dest[dp] = src[sp];
						dp++;
						sp++;
					}
					if ((sp & 1) != 0)
						sp++; /* word align sp? */
					break;
			}
		} else {
			if ((len & 1) != 0)
				return -1;
			x += len;
			len = len / 2;
			byte theByte = src[sp];
			sp++;
			if (len > (de - dp))
				return -1;
			for (int i = 0; i < len; i++) {
				dest[dp] = theByte;
				dp++;
			}
		}
	}
	return 1;
}
int decompressRLE8Data(byte[] src, int numBytes, int stride, byte[] dest, int destSize) {
	int sp = 0;
	int se = numBytes;
	int dp = 0;
	int de = destSize;
	int x = 0, y = 0;
	while (sp < se) {
		int len = src[sp] & 0xFF;
		sp++;
		if (len == 0) {
			len = src[sp] & 0xFF;
			sp++;
			switch (len) {
				case 0: /* end of line */
					y++;
					x = 0;
					dp = y * stride;
					if (dp >= de)
						return -1;
					break;
				case 1: /* end of bitmap */
					return 1;
				case 2: /* delta */
					x += src[sp] & 0xFF;
					sp++;
					y += src[sp] & 0xFF;
					sp++;
					dp = y * stride + x;
					if (dp >= de)
						return -1;
					break;
				default: /* absolute mode run */
					if (len > (se - sp))
						return -1;
					if (len > (de - dp))
						return -1;
					for (int i = 0; i < len; i++) {
						dest[dp] = src[sp];
						dp++;
						sp++;
					}
					if ((sp & 1) != 0)
						sp++; /* word align sp? */
					x += len;
					break;
			}
		} else {
			byte theByte = src[sp];
			sp++;
			if (len > (de - dp))
				return -1;
			for (int i = 0; i < len; i++) {
				dest[dp] = theByte;
				dp++;
			}
			x += len;
		}
	}
	return 1;
}
boolean isFileFormat(LEDataInputStream stream) {
	try {
		byte[] header = new byte[18];
		stream.read(header);
		stream.unread(header);
		int infoHeaderSize = (header[14] & 0xFF) | ((header[15] & 0xFF) << 8) | ((header[16] & 0xFF) << 16) | ((header[17] & 0xFF) << 24);
		return header[0] == 0x42 && header[1] == 0x4D && infoHeaderSize >= BMPHeaderFixedSize;
	} catch (Exception e) {
		return false;
	}
}
byte[] loadData(byte[] infoHeader) {
	int width = (infoHeader[4] & 0xFF) | ((infoHeader[5] & 0xFF) << 8) | ((infoHeader[6] & 0xFF) << 16) | ((infoHeader[7] & 0xFF) << 24);
	int height = (infoHeader[8] & 0xFF) | ((infoHeader[9] & 0xFF) << 8) | ((infoHeader[10] & 0xFF) << 16) | ((infoHeader[11] & 0xFF) << 24);
	int bitCount = (infoHeader[14] & 0xFF) | ((infoHeader[15] & 0xFF) << 8);
	int stride = (width * bitCount + 7) / 8;
	stride = (stride + 3) / 4 * 4; // Round up to 4 byte multiple
	byte[] data = loadData(infoHeader, stride);
	flipScanLines(data, stride, height);
	return data;
}
byte[] loadData(byte[] infoHeader, int stride) {
	int height = (infoHeader[8] & 0xFF) | ((infoHeader[9] & 0xFF) << 8) | ((infoHeader[10] & 0xFF) << 16) | ((infoHeader[11] & 0xFF) << 24);
	int dataSize = height * stride;
	byte[] data = new byte[dataSize];
	int cmp = (infoHeader[16] & 0xFF) | ((infoHeader[17] & 0xFF) << 8) | ((infoHeader[18] & 0xFF) << 16) | ((infoHeader[19] & 0xFF) << 24);
	if (cmp == 0) { // BMP_NO_COMPRESSION
		try {
			if (inputStream.read(data) != dataSize)
				SWT.error(SWT.ERROR_INVALID_IMAGE);
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
		}
	} else {
		int compressedSize = (infoHeader[20] & 0xFF) | ((infoHeader[21] & 0xFF) << 8) | ((infoHeader[22] & 0xFF) << 16) | ((infoHeader[23] & 0xFF) << 24);
		byte[] compressed = new byte[compressedSize];
		try {
			if (inputStream.read(compressed) != compressedSize)
				SWT.error(SWT.ERROR_INVALID_IMAGE);
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
		}
		decompressData(compressed, data, stride, cmp);
	}
	return data;
}
int[] loadFileHeader() {
	int[] header = new int[5];
	try {
		header[0] = inputStream.readShort();
		header[1] = inputStream.readInt();
		header[2] = inputStream.readShort();
		header[3] = inputStream.readShort();
		header[4] = inputStream.readInt();
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	}
	if (header[0] != 0x4D42)
		SWT.error(SWT.ERROR_INVALID_IMAGE);
	return header;
}
ImageData[] loadFromByteStream() {
	int[] fileHeader = loadFileHeader();
	byte[] infoHeader = new byte[BMPHeaderFixedSize];
	try {
		inputStream.read(infoHeader);
	} catch (Exception e) {
		SWT.error(SWT.ERROR_IO, e);
	}
	int width = (infoHeader[4] & 0xFF) | ((infoHeader[5] & 0xFF) << 8) | ((infoHeader[6] & 0xFF) << 16) | ((infoHeader[7] & 0xFF) << 24);
	int height = (infoHeader[8] & 0xFF) | ((infoHeader[9] & 0xFF) << 8) | ((infoHeader[10] & 0xFF) << 16) | ((infoHeader[11] & 0xFF) << 24);
	int bitCount = (infoHeader[14] & 0xFF) | ((infoHeader[15] & 0xFF) << 8);
	PaletteData palette = loadPalette(infoHeader);
	if (inputStream.getPosition() < fileHeader[4]) {
		// Seek to the specified offset
		try {
			inputStream.skip(fileHeader[4] - inputStream.getPosition());
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
		}
	}
	byte[] data = loadData(infoHeader);
	this.compression = (infoHeader[16] & 0xFF) | ((infoHeader[17] & 0xFF) << 8) | ((infoHeader[18] & 0xFF) << 16) | ((infoHeader[19] & 0xFF) << 24);
	this.importantColors = (infoHeader[36] & 0xFF) | ((infoHeader[37] & 0xFF) << 8) | ((infoHeader[38] & 0xFF) << 16) | ((infoHeader[39] & 0xFF) << 24);
	int xPelsPerMeter = (infoHeader[24] & 0xFF) | ((infoHeader[25] & 0xFF) << 8) | ((infoHeader[26] & 0xFF) << 16) | ((infoHeader[27] & 0xFF) << 24);
	int yPelsPerMeter = (infoHeader[28] & 0xFF) | ((infoHeader[29] & 0xFF) << 8) | ((infoHeader[30] & 0xFF) << 16) | ((infoHeader[31] & 0xFF) << 24);
	int type = (this.compression == 1 /*BMP_RLE8_COMPRESSION*/) || (this.compression == 2 

/*BMP_RLE4_COMPRESSION*/) ? SWT.IMAGE_BMP_RLE : SWT.IMAGE_BMP;
	return new ImageData[] {
		ImageData.internal_new(
			width,
			height,
			bitCount,
			palette,
			4,
			data,
			0,
			null,
			null,
			-1,
			-1,
			type,
			0,
			0,
			0,
			0)
	};
}
PaletteData loadPalette(byte[] infoHeader) {
	int depth = (infoHeader[14] & 0xFF) | ((infoHeader[15] & 0xFF) << 8);
	if (depth <= 8) {
		int numColors = (infoHeader[32] & 0xFF) | ((infoHeader[33] & 0xFF) << 8) | ((infoHeader[34] & 0xFF) << 16) | ((infoHeader[35] & 0xFF) << 24);
		if (numColors == 0) {
			numColors = 1 << depth;
		} else {
			if (numColors > 256)
				numColors = 256;
		}
		byte[] buf = new byte[numColors * 4];
		try {
			if (inputStream.read(buf) != buf.length)
				SWT.error(SWT.ERROR_INVALID_IMAGE);
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
		}
		return paletteFromBytes(buf, numColors);
	}
	if (depth == 16) return new PaletteData(0x7C00, 0x3E0, 0x1F);
	if (depth == 24) return new PaletteData(0xFF, 0xFF00, 0xFF0000);
	return new PaletteData(0xFF00, 0xFF0000, 0xFF000000);
}
PaletteData paletteFromBytes(byte[] bytes, int numColors) {
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
/**
 * Answer a byte array containing the BMP representation of
 * the given device independent palette.
 */
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
/**
 * Unload the given image's data into the given byte stream
 * using the given compression strategy. 
 * Answer the number of bytes written.
 */
int unloadData(ImageData image, OutputStream out, int comp) {
	int totalSize = 0;
	try {
		if (comp == 0)
			return unloadDataNoCompression(image, out);
		int bpl = (image.width * image.depth + 7) / 8;
		int bmpBpl = (bpl + 3) / 4 * 4; // BMP pads scanlines to multiples of 4 bytes
		int imageBpl = image.bytesPerLine;
		// Compression can actually take twice as much space, in worst case
		byte[] buf = new byte[bmpBpl * 2];
		int srcOffset = imageBpl * (image.height - 1); // Start at last line
		byte[] data = image.data;
		totalSize = 0;
		byte[] buf2 = new byte[32768];
		int buf2Offset = 0;
		for (int y = image.height - 1; y >= 0; y--) {
			int lineSize = compress(comp, data, srcOffset, bpl, buf, y == 0);
			if (buf2Offset + lineSize > buf2.length) {
				out.write(buf2, 0, buf2Offset);
				buf2Offset = 0;
			}
			System.arraycopy(buf, 0, buf2, buf2Offset, lineSize);
			buf2Offset += lineSize;
			totalSize += lineSize;
			srcOffset -= imageBpl;
		}
		if (buf2Offset > 0)
			out.write(buf2, 0, buf2Offset);
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	}
	return totalSize;
}
/**
 * Prepare the given image's data for unloading into a byte stream
 * using no compression strategy.
 * Answer the number of bytes written.
 */
int unloadDataNoCompression(ImageData image, OutputStream out) {
	int bmpBpl = 0;
	try {
		int bpl = (image.width * image.depth + 7) / 8;
		bmpBpl = (bpl + 3) / 4 * 4; // BMP pads scanlines to multiples of 4 bytes
		int linesPerBuf = 32678 / bmpBpl;
		byte[] buf = new byte[linesPerBuf * bmpBpl];
		byte[] data = image.data;
		int imageBpl = image.bytesPerLine;
		int dataIndex = imageBpl * (image.height - 1); // Start at last line
		if (image.depth == 16) {
			for (int y = 0; y < image.height; y += linesPerBuf) {
				int count = image.height - y;
				if (linesPerBuf < count) count = linesPerBuf;
				int bufOffset = 0;
				for (int i = 0; i < count; i++) {
					for (int wIndex = 0; wIndex < bpl; wIndex += 2) {
						buf[bufOffset + wIndex + 1] = data[dataIndex + wIndex + 1];
						buf[bufOffset + wIndex] = data[dataIndex + wIndex];
					}
					bufOffset += bmpBpl;
					dataIndex -= imageBpl;
				}
				out.write(buf, 0, bufOffset);
			}
		} else {
			for (int y = 0; y < image.height; y += linesPerBuf) {
				int tmp = image.height - y;
				int count = tmp < linesPerBuf ? tmp : linesPerBuf;
				int bufOffset = 0;
				for (int i = 0; i < count; i++) {
					System.arraycopy(data, dataIndex, buf, bufOffset, bpl);
					bufOffset += bmpBpl;
					dataIndex -= imageBpl;
				}
				out.write(buf, 0, bufOffset);
			}
		}
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	}
	return bmpBpl * image.height;
}
void flipScanLines(byte[] data, int stride, int height) {
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

}

static class WinICOFileFormat extends FileFormat {
	
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
/**
 * Answer the size in bytes of the file representation of the given
 * icon
 */
int iconSize(ImageData i) {
	int shapeDataStride = (i.width * i.depth + 31) / 32 * 4;
	int maskDataStride = (i.width + 31) / 32 * 4;
	int dataSize = (shapeDataStride + maskDataStride) * i.height;
	int paletteSize = i.palette.colors != null ? i.palette.colors.length * 4 : 0;
	return WinBMPFileFormat.BMPHeaderFixedSize + paletteSize + dataSize;
}
boolean isFileFormat(LEDataInputStream stream) {
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
	switch (i.depth) {
		case 1:
		case 4:
		case 8:
			if (i.palette.isDirect) return false;
			int size = i.palette.colors.length;
			return size == 2 || size == 16 || size == 32 || size == 256;
		case 24:
		case 32:
			return i.palette.isDirect;
	}
	return false;
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
	int width = (infoHeader[4] & 0xFF) | ((infoHeader[5] & 0xFF) << 8) | ((infoHeader[6] & 0xFF) << 16) | ((infoHeader[7] & 0xFF) << 24);
	int height = (infoHeader[8] & 0xFF) | ((infoHeader[9] & 0xFF) << 8) | ((infoHeader[10] & 0xFF) << 16) | ((infoHeader[11] & 0xFF) << 24);
	int depth = (infoHeader[14] & 0xFF) | ((infoHeader[15] & 0xFF) << 8);
	infoHeader[14] = 1;
	infoHeader[15] = 0;
	byte[] maskData = bmpFormat.loadData(infoHeader);
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
		(bitCount == 1 || bitCount == 4 || bitCount == 8 || bitCount == 24 || bitCount == 32)))
			SWT.error(SWT.ERROR_INVALID_IMAGE);
	infoHeader[8] = (byte)(height & 0xFF);
	infoHeader[9] = (byte)((height >> 8) & 0xFF);
	infoHeader[10] = (byte)((height >> 16) & 0xFF);
	infoHeader[11] = (byte)((height >> 24) & 0xFF);
	return infoHeader;
}
}
public static class SWT {
	public static final int IMAGE_ICO = 3;
	public static final int ERROR_IO = 39;
	public static final int ERROR_INVALID_IMAGE = 40;
	public static final int ERROR_NULL_ARGUMENT = 4;
	public static final int ERROR_INVALID_ARGUMENT = 5;
	public static final int ERROR_CANNOT_BE_ZERO = 7;
	public static final int IMAGE_UNDEFINED = -1;
	public static final int ERROR_UNSUPPORTED_DEPTH = 38;
	public static final int TRANSPARENCY_MASK = 1 << 1;
	public static final int ERROR_UNSUPPORTED_FORMAT = 42;
	public static final int TRANSPARENCY_ALPHA = 1 << 0;
	public static final int TRANSPARENCY_NONE = 0x0;
	public static final int TRANSPARENCY_PIXEL = 1 << 2;
	public static final int IMAGE_BMP = 0;
	public static final int IMAGE_BMP_RLE = 1;
	
	public static void error(int code) {
		throw new RuntimeException("Error "+code);
	}
	public static void error(int code, Throwable t) {
		throw new RuntimeException(t);
	}
}
}
