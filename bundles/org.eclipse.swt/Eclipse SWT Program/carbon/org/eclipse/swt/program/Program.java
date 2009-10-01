/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.program;


import org.eclipse.swt.internal.carbon.CFRange;
import org.eclipse.swt.internal.carbon.LSApplicationParameters;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.cocoa.Cocoa;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Instances of this class represent programs and
 * their associated file extensions in the operating
 * system.
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#program">Program snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public final class Program {
	String name;
	byte[] fsRef;

	static final String PREFIX_HTTP = "http://"; //$NON-NLS-1$
	static final String PREFIX_HTTPS = "https://"; //$NON-NLS-1$
	static final String PREFIX_FILE = "file://"; //$NON-NLS-1$

/**
 * Prevents uninitialized instances from being created outside the package.
 */
Program () {
}

/**
 * Finds the program that is associated with an extension.
 * The extension may or may not begin with a '.'.  Note that
 * a <code>Display</code> must already exist to guarantee that
 * this method returns an appropriate result.
 *
 * @param extension the program extension
 * @return the program or <code>null</code>
 *
 * @exception IllegalArgumentException <ul>
 *		<li>ERROR_NULL_ARGUMENT when extension is null</li>
 *	</ul>
 */
public static Program findProgram (String extension) {
	if (extension == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (extension.length () == 0) return null;
	char[] chars;
	if (extension.charAt (0) != '.') {
		chars = new char[extension.length()];
		extension.getChars(0, chars.length, chars, 0);
	} else {
		chars = new char[extension.length() - 1];
		extension.getChars(1, extension.length(), chars, 0);		
	}
	int ext = OS.CFStringCreateWithCharacters(OS.kCFAllocatorDefault, chars, chars.length);
	Program program = null;
	if (ext != 0) {
		byte[] fsRef = new byte[80];
		if (OS.LSGetApplicationForInfo(OS.kLSUnknownType, OS.kLSUnknownCreator, ext, OS.kLSRolesAll, fsRef, null) == OS.noErr) {
			program = getProgram(fsRef);
		}
		OS.CFRelease(ext);
	}
	return program;
}

/**
 * Answer all program extensions in the operating system.  Note
 * that a <code>Display</code> must already exist to guarantee
 * that this method returns an appropriate result.
 *
 * @return an array of extensions
 */
public static String [] getExtensions () {
	final String CFBundleDocumentTypesStr = "CFBundleDocumentTypes";
	char [] chars = new char[CFBundleDocumentTypesStr.length()];
	CFBundleDocumentTypesStr.getChars(0, chars.length, chars, 0);
	int CFBundleDocumentTypes = OS.CFStringCreateWithCharacters(OS.kCFAllocatorDefault, chars, chars.length);
	
	final String CFBundleTypeExtensionsStr = "CFBundleTypeExtensions";
	chars = new char[CFBundleTypeExtensionsStr.length()];
	CFBundleTypeExtensionsStr.getChars(0, chars.length, chars, 0);
	int CFBundleTypeExtensions = OS.CFStringCreateWithCharacters(OS.kCFAllocatorDefault, chars, chars.length);
	
	int folders = Cocoa.NSSearchPathForDirectoriesInDomains (Cocoa.NSAllApplicationsDirectory, Cocoa.NSAllDomainsMask, true);
	int folderCount = OS.CFArrayGetCount(folders);
	int supportedDocumentTypes = OS.CFSetCreateMutable(OS.kCFAllocatorDefault, 0, OS.kCFTypeSetCallBacks());	
	for (int i = 0; i < folderCount; i++) {
		int string = OS.CFArrayGetValueAtIndex(folders, i);
		int folderUrl = OS.CFURLCreateWithFileSystemPath(OS.kCFAllocatorDefault, string, OS.kCFURLPOSIXPathStyle, true);
		if (folderUrl != 0) {
			int bundles = OS.CFBundleCreateBundlesFromDirectory(OS.kCFAllocatorDefault, folderUrl, 0);
			if (bundles != 0) {
				int bundleCount = OS.CFArrayGetCount(bundles);
				for (int j = 0; j < bundleCount; j++) {
					int bundleRef = OS.CFArrayGetValueAtIndex(bundles, j);
					if (bundleRef == 0) continue;
					int documentTypes = OS.CFBundleGetValueForInfoDictionaryKey(bundleRef, CFBundleDocumentTypes);
					if (documentTypes != 0) {
						int count = OS.CFArrayGetCount(documentTypes);
						for (int k = 0; k < count; k++) {
							int documentType = OS.CFArrayGetValueAtIndex(documentTypes, k);
							if (documentType == 0) continue;
							int[] value = new int[1];
							if (OS.CFDictionaryGetValueIfPresent(documentType, CFBundleTypeExtensions, value)) {
								if (value[0] != 0) {
									int extCount = OS.CFArrayGetCount(value[0]);
									for (int x = 0; x < extCount; x++) {
										int ext = OS.CFArrayGetValueAtIndex(value[0], x);
										OS.CFSetAddValue(supportedDocumentTypes, ext);
									}
								}
							}
						}
					}
				}
				OS.CFRelease(bundles);
			}
			OS.CFRelease(folderUrl);
		}
	}
	OS.CFRelease(CFBundleDocumentTypes);
	OS.CFRelease(CFBundleTypeExtensions);
	
	int s = OS.CFStringCreateWithCharacters(OS.kCFAllocatorDefault, new char[]{'*'}, 1);
	OS.CFSetRemoveValue(supportedDocumentTypes, s);
	OS.CFRelease(s);
	
	int count = OS.CFSetGetCount(supportedDocumentTypes);
	String[] extensions = new String[count];
	int [] values = new int[count];
	OS.CFSetGetValues(supportedDocumentTypes, values);
	for (int i = 0; i < count; i++) {
		int ext = values[i];
		int length = OS.CFStringGetLength(ext);
		char[] buffer = new char[length];
		CFRange range = new CFRange();
		range.length = length;
		OS.CFStringGetCharacters(ext, range, buffer);
		extensions[i] = "." + new String(buffer);
	}
	OS.CFRelease(supportedDocumentTypes);
	return extensions;
}

/**
 * Answers all available programs in the operating system.  Note
 * that a <code>Display</code> must already exist to guarantee
 * that this method returns an appropriate result.
 *
 * @return an array of programs
 */
public static Program [] getPrograms () {
	Hashtable bundles = new Hashtable();
	String[] extensions = getExtensions();
	byte[] fsRef = new byte[80];
	for (int i = 0; i < extensions.length; i++) {
		String extension = extensions[i];
		char[] chars = new char[extension.length() - 1];
		extension.getChars(1, extension.length(), chars, 0);
		int ext = OS.CFStringCreateWithCharacters(OS.kCFAllocatorDefault, chars, chars.length);
		if (ext != 0) {
			if (OS.LSGetApplicationForInfo(OS.kLSUnknownType, OS.kLSUnknownCreator, ext, OS.kLSRolesAll, fsRef, null) == OS.noErr) {
				Program program = getProgram(fsRef);
				if (program != null && bundles.get(program.getName()) == null) {
					bundles.put(program.getName(), program);
					fsRef = new byte[80];
				}
			}
			if (OS.VERSION >= 0x1040) {
				int utis = OS.UTTypeCreateAllIdentifiersForTag(OS.kUTTagClassFilenameExtension(), ext, 0);
				if (utis != 0) {
					int utiCount = OS.CFArrayGetCount(utis);
					for (int j = 0; j < utiCount; j++) {
						int uti = OS.CFArrayGetValueAtIndex(utis, j);
						if (uti != 0) {
							int apps = OS.LSCopyAllRoleHandlersForContentType(uti, OS.kLSRolesAll);
							if (apps != 0) {
								int appCount = OS.CFArrayGetCount(apps);
								for (int k = 0; k < appCount; k++) {
									int app = OS.CFArrayGetValueAtIndex(apps, k);
									if (app != 0) {;
										if (OS.LSFindApplicationForInfo(OS.kLSUnknownCreator, app, 0, fsRef, null) == OS.noErr) {
											Program program = getProgram(fsRef);
											if (program != null && bundles.get(program.getName()) == null) {
												bundles.put(program.getName(), program);
												fsRef = new byte[80];
											}
										}
									}
								}
								OS.CFRelease(apps);
							}
						}
					}
					OS.CFRelease(utis);
				}
			}
			OS.CFRelease(ext);
		}
	}
	int count = 0;
	Program[] programs = new Program[bundles.size()];
	Enumeration values = bundles.elements();
	while (values.hasMoreElements()) {
		programs[count++] = (Program)values.nextElement();
	}
	return programs;
}

/**
 * Launches the operating system executable associated with the file or
 * URL (http:// or https://).  If the file is an executable then the
 * executable is launched.  Note that a <code>Display</code> must already
 * exist to guarantee that this method returns an appropriate result.
 *
 * @param fileName the file or program name or URL (http:// or https://)
 * @return <code>true</code> if the file is launched, otherwise <code>false</code>
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when fileName is null</li>
 * </ul>
 */
public static boolean launch (String fileName) {
	if (fileName == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	int rc = -1;
	char[] unescapedChars = new char[] {'%'};
	String lowercaseName = fileName.toLowerCase ();
	if (lowercaseName.startsWith (PREFIX_HTTP) || lowercaseName.startsWith (PREFIX_HTTPS)) {
		unescapedChars = new char[] {'%', '#'};
	} else {
		if (!lowercaseName.startsWith (PREFIX_FILE)) {
			fileName = PREFIX_FILE + fileName;
		}
	}
	char[] chars = new char[fileName.length()];
	fileName.getChars(0, chars.length, chars, 0);
	int str = OS.CFStringCreateWithCharacters(0, chars, chars.length);
	if (str != 0) {
		int unescapedStr = OS.CFStringCreateWithCharacters(0, unescapedChars, unescapedChars.length);
		int escapedStr = OS.CFURLCreateStringByAddingPercentEscapes(OS.kCFAllocatorDefault, str, unescapedStr, 0, OS.kCFStringEncodingUTF8);
		if (escapedStr != 0) {
			int url = OS.CFURLCreateWithString(OS.kCFAllocatorDefault, escapedStr, 0);
			if (url != 0) {
				rc = OS.LSOpenCFURLRef(url, null);
				OS.CFRelease(url);
			}
			OS.CFRelease(escapedStr);
		}
		if (unescapedStr != 0) OS.CFRelease(unescapedStr);
		OS.CFRelease(str);
	}
	return rc == OS.noErr;
}

/**
 * Executes the program with the file as the single argument
 * in the operating system.  It is the responsibility of the
 * programmer to ensure that the file contains valid data for 
 * this program.
 *
 * @param fileName the file or program name
 * @return <code>true</code> if the file is launched, otherwise <code>false</code>
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when fileName is null</li>
 * </ul>
 */
public boolean execute (String fileName) {
	if (fileName == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (OS.VERSION < 0x1040) return launch(fileName);
	int rc = -1;
	int fsRefPtr = OS.NewPtr(fsRef.length);
	if (fsRefPtr != 0) {
		OS.memmove(fsRefPtr, fsRef, fsRef.length);
		LSApplicationParameters params = new LSApplicationParameters();
		params.version = 0;
		params.flags = 0;
		params.application = fsRefPtr;
		if (fileName.length() == 0) {
			rc = OS.LSOpenApplication(params, null);
		} else {
			char[] unescapedChars = new char[] {'%'};
			String lowercaseName = fileName.toLowerCase ();
			if (lowercaseName.startsWith (PREFIX_HTTP) || lowercaseName.startsWith (PREFIX_HTTPS)) {
				unescapedChars = new char[] {'%', '#'};
			} else {
				if (!lowercaseName.startsWith (PREFIX_FILE)) {
					fileName = PREFIX_FILE + fileName;
				}
			}
			char[] chars = new char[fileName.length()];
			fileName.getChars(0, chars.length, chars, 0);
			int str = OS.CFStringCreateWithCharacters(0, chars, chars.length);
			if (str != 0) {
				int unescapedStr = OS.CFStringCreateWithCharacters(0, unescapedChars, unescapedChars.length);
				int escapedStr = OS.CFURLCreateStringByAddingPercentEscapes(OS.kCFAllocatorDefault, str, unescapedStr, 0, OS.kCFStringEncodingUTF8);
				if (escapedStr != 0) {
					int urls = OS.CFArrayCreateMutable(OS.kCFAllocatorDefault, 1, OS.kCFTypeArrayCallBacks ());
					if (urls != 0) {
						int url = OS.CFURLCreateWithString(OS.kCFAllocatorDefault, escapedStr, 0);
						if (url != 0) {
							OS.CFArrayAppendValue(urls, url);
							OS.CFRelease(url);
							rc = OS.LSOpenURLsWithRole(urls, OS.kLSRolesAll, 0, params, null, 0);
						}
						OS.CFRelease(urls);
					}
					OS.CFRelease(escapedStr);
				}
				if (unescapedStr != 0) OS.CFRelease(unescapedStr);
				OS.CFRelease(str);
			}
		}
		OS.DisposePtr(fsRefPtr);
	}
	return rc == OS.noErr;
}

ImageData createImageFromFamily (int family, int type, int maskType, int width, int height) {
	int dataHandle = OS.NewHandle (0);
	int result = OS.GetIconFamilyData (family, type, dataHandle);
	if (result != OS.noErr) {
		OS.DisposeHandle (dataHandle);
		return null;
	}
	int maskHandle = OS.NewHandle (0);
	result = OS.GetIconFamilyData (family, maskType, maskHandle);
	if (result != OS.noErr) {
		OS.DisposeHandle (maskHandle);
		OS.DisposeHandle (dataHandle);
		return null;
	}
	int dataSize = OS.GetHandleSize (dataHandle);
	OS.HLock (dataHandle);
	OS.HLock (maskHandle);
	int[] iconPtr = new int [1];
	int[] maskPtr = new int [1];
	OS.memmove (iconPtr, dataHandle, 4);
	OS.memmove (maskPtr, maskHandle, 4);
	byte[] data = new byte[dataSize];
	OS.memmove (data, iconPtr [0], dataSize);
	byte[] alphaData = new byte[width * height];
	OS.memmove(alphaData, maskPtr[0], alphaData.length);
	OS.HUnlock (maskHandle);
	OS.HUnlock (dataHandle);
	OS.DisposeHandle (maskHandle);
	OS.DisposeHandle (dataHandle);

	ImageData image = new ImageData(width, height, 32, new PaletteData(0xFF0000, 0xFF00, 0xFF), 4, data);
	image.alphaData = alphaData;

	return image;
}

/**
 * Returns the receiver's image data.  This is the icon
 * that is associated with the receiver in the operating
 * system.
 *
 * @return the image data for the program, may be null
 */
public ImageData getImageData () {
	int[] iconRef = new int[1];
	OS.GetIconRefFromFileInfo(fsRef, 0, null, 0, 0, 0, iconRef, null);
	int[] family = new int[1];
	int rc = OS.IconRefToIconFamily(iconRef[0], OS.kSelectorAlLAvailableData, family);
	OS.ReleaseIconRef(iconRef[0]);
	if (rc != OS.noErr) return null;
//	ImageData result = createImageFromFamily(family[0], OS.kLarge32BitData, OS.kLarge8BitMask, 32, 32);
	ImageData result = createImageFromFamily(family[0], OS.kSmall32BitData, OS.kSmall8BitMask, 16, 16);
	OS.DisposeHandle(family[0]);
	if (result == null) {
		RGB[] rgbs = new RGB[] {
			new RGB(0xff, 0xff, 0xff), 
			new RGB(0x5f, 0x5f, 0x5f),
			new RGB(0x80, 0x80, 0x80),
			new RGB(0xC0, 0xC0, 0xC0),
			new RGB(0xDF, 0xDF, 0xBF),
			new RGB(0xFF, 0xDF, 0x9F),
			new RGB(0x00, 0x00, 0x00),
		};  
		result = new ImageData(16, 16, 4, new PaletteData(rgbs)	);
		result.transparentPixel = 6; // use black for transparency
		String[] p= {
			"CCCCCCCCGGG",
			"CFAAAAACBGG",
			"CAAAAAACFBG",
			"CAAAAAACBBB",
			"CAAAAAAAAEB",
			"CAAAAAAAAEB",
			"CAAAAAAAAEB",
			"CAAAAAAAAEB",
			"CAAAAAAAAEB",
			"CAAAAAAAAEB",
			"CAAAAAAAAEB",
			"CAAAAAAAAEB",
			"CDDDDDDDDDB",
			"CBBBBBBBBBB",
		};
		for (int y= 0; y < p.length; y++) {
			for (int x= 0; x < 11; x++) {
				result.setPixel(x+3, y+1, p[y].charAt(x)-'A');
			}
		}
	}
	return result;
}

/**
 * Returns the receiver's name.  This is as short and
 * descriptive a name as possible for the program.  If
 * the program has no descriptive name, this string may
 * be the executable name, path or empty.
 *
 * @return the name of the program
 */
public String getName () {
	return name;
}

static Program getProgram(byte[] fsRef) {
	String name = "";
	int[] namePtr = new int[1];
	OS.LSCopyDisplayNameForRef(fsRef, namePtr);
	if (namePtr[0] != 0) {
		int length = OS.CFStringGetLength(namePtr[0]);
		if (length != 0) {
			char[] buffer= new char[length];
			CFRange range = new CFRange();
			range.length = length;
			OS.CFStringGetCharacters(namePtr[0], range, buffer);
			name = new String(buffer);
		}
		OS.CFRelease(namePtr[0]);
	}
	Program program = new Program();
	program.fsRef = fsRef;
	program.name = name;
	return program;
}

/**
 * Compares the argument to the receiver, and returns true
 * if they represent the <em>same</em> object using a class
 * specific comparison.
 *
 * @param other the object to compare with this object
 * @return <code>true</code> if the object is the same as this object and <code>false</code> otherwise
 *
 * @see #hashCode()
 */
public boolean equals(Object other) {
	if (this == other) return true;
	if (other instanceof Program) {
		final Program program = (Program) other;
		return name.equals(program.name);
	}
	return false;
}

/**
 * Returns an integer hash code for the receiver. Any two 
 * objects that return <code>true</code> when passed to 
 * <code>equals</code> must return the same value for this
 * method.
 *
 * @return the receiver's hash
 *
 * @see #equals(Object)
 */
public int hashCode() {
	return name.hashCode();
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the program
 */
public String toString () {
	return "Program {" + name + "}";
}

}
