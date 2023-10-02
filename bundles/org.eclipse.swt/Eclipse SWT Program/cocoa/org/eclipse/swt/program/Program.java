/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
package org.eclipse.swt.program;


import java.io.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.widgets.*;

/**
 * Instances of this class represent programs and
 * their associated file extensions in the operating
 * system.
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#program">Program snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public final class Program {
	String name, fullPath, identifier;

	static final String PREFIX_FILE = "file:/"; //$NON-NLS-1$
	static final String PREFIX_HTTP = "http://"; //$NON-NLS-1$
	static final String PREFIX_HTTPS = "https://"; //$NON-NLS-1$

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
	NSAutoreleasePool pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		if (extension == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		if (extension.length () == 0) return null;
		Program program = null;
		char[] chars;
		if (extension.charAt (0) != '.') {
			chars = new char[extension.length()];
			extension.getChars(0, chars.length, chars, 0);
		} else {
			chars = new char[extension.length() - 1];
			extension.getChars(1, extension.length(), chars, 0);
		}
		NSString ext = NSString.stringWithCharacters(chars, chars.length);
		if (ext != null) {
			byte[] fsRef = new byte[80];
			if (OS.LSGetApplicationForInfo(OS.kLSUnknownType, OS.kLSUnknownCreator, ext.id, OS.kLSRolesAll, fsRef, null) == OS.noErr) {
				long url = OS.CFURLCreateFromFSRef(OS.kCFAllocatorDefault(), fsRef);
				if (url != 0) {
					NSString bundlePath = new NSURL(url).path();
					NSBundle bundle = NSBundle.bundleWithPath(bundlePath);
					if (bundle != null) program = getProgram(bundle);
					OS.CFRelease(url);
				}
			}
		}
		return program;
	} finally {
		pool.release();
	}
}

/**
 * Answer all program extensions in the operating system.  Note
 * that a <code>Display</code> must already exist to guarantee
 * that this method returns an appropriate result.
 *
 * @return an array of extensions
 */
public static String [] getExtensions () {
	NSAutoreleasePool pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		NSMutableSet supportedDocumentTypes = (NSMutableSet)NSMutableSet.set();
		NSWorkspace workspace = NSWorkspace.sharedWorkspace();
		NSString CFBundleDocumentTypes = NSString.stringWith("CFBundleDocumentTypes");
		NSString CFBundleTypeExtensions = NSString.stringWith("CFBundleTypeExtensions");
		NSArray array = new NSArray(OS.NSSearchPathForDirectoriesInDomains(OS.NSAllApplicationsDirectory, OS.NSAllDomainsMask, true));
		int count = (int)array.count();
		for (int i = 0; i < count; i++) {
			NSString path = new NSString(array.objectAtIndex(i));
			NSFileManager fileManager = NSFileManager.defaultManager();
			NSDirectoryEnumerator enumerator = fileManager.enumeratorAtPath(path);
			if (enumerator != null) {
				id id;
				while ((id = enumerator.nextObject()) != null) {
					enumerator.skipDescendents();
					NSString filePath = new NSString(id.id);
					NSString fullPath = path.stringByAppendingPathComponent(filePath);
					if (workspace.isFilePackageAtPath(fullPath)) {
						NSBundle bundle = NSBundle.bundleWithPath(fullPath);
						id = bundle != null ? bundle.infoDictionary().objectForKey(CFBundleDocumentTypes) : null;
						if (id != null) {
							NSDictionary documentTypes = new NSDictionary(id.id);
							NSEnumerator documentTypesEnumerator = documentTypes.objectEnumerator();
							while ((id = documentTypesEnumerator.nextObject()) != null) {
								NSDictionary documentType = new NSDictionary(id.id);
								id = documentType.objectForKey(CFBundleTypeExtensions);
								if (id != null) {
									supportedDocumentTypes.addObjectsFromArray(new NSArray(id.id));
								}
							}
						}
					}
				}
			}
		}
		int i = 0;
		String[] exts = new String[(int)supportedDocumentTypes.count()];
		NSEnumerator enumerator = supportedDocumentTypes.objectEnumerator();
		id id;
		while ((id = enumerator.nextObject()) != null) {
			String ext = new NSString(id.id).getString();
			if (!ext.equals("*")) exts[i++] = "." + ext;
		}
		if (i != exts.length) {
			String[] temp = new String[i];
			System.arraycopy(exts, 0, temp, 0, i);
			exts = temp;
		}
		return exts;
	} finally {
		pool.release();
	}
}

static Program getProgram(NSBundle bundle) {
	NSString CFBundleName = NSString.stringWith("CFBundleName");
	NSString CFBundleDisplayName = NSString.stringWith("CFBundleDisplayName");
	NSString fullPath = bundle.bundlePath();
	NSString identifier = bundle.bundleIdentifier();
	id bundleName = bundle.objectForInfoDictionaryKey(CFBundleDisplayName);
	if (bundleName == null) {
		bundleName = bundle.objectForInfoDictionaryKey(CFBundleName);
	}
	if (bundleName == null) {
		if (fullPath == null) return null;
		bundleName = fullPath.lastPathComponent().stringByDeletingPathExtension();
	}
	NSString name = new NSString(bundleName.id);
	Program program = new Program();
	program.name = name.getString();
	if (fullPath != null) program.fullPath = fullPath.getString();
	program.identifier = identifier != null ? identifier.getString() : "";
	return program;
}

/**
 * Answers all available programs in the operating system.  Note
 * that a <code>Display</code> must already exist to guarantee
 * that this method returns an appropriate result.
 *
 * @return an array of programs
 */
public static Program [] getPrograms () {
	NSAutoreleasePool pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		LinkedHashSet<Program> programs = new LinkedHashSet<>();
		NSWorkspace workspace = NSWorkspace.sharedWorkspace();
		NSArray array = new NSArray(OS.NSSearchPathForDirectoriesInDomains(OS.NSAllApplicationsDirectory, OS.NSAllDomainsMask, true));
		int count = (int)array.count();
		for (int i = 0; i < count; i++) {
			NSString path = new NSString(array.objectAtIndex(i));
			NSFileManager fileManager = NSFileManager.defaultManager();
			NSDirectoryEnumerator enumerator = fileManager.enumeratorAtPath(path);
			if (enumerator != null) {
				id id;
				while ((id = enumerator.nextObject()) != null) {
					enumerator.skipDescendents();
					NSString fullPath = path.stringByAppendingPathComponent(new NSString(id.id));
					if (workspace.isFilePackageAtPath(fullPath)) {
						NSBundle bundle = NSBundle.bundleWithPath(fullPath);
						if (bundle != null) {
							Program program = getProgram(bundle);
							if (program != null) programs.add(program);
						}
					}
				}
			}
		}
		return programs.toArray(new Program[programs.size()]);
	} finally {
		pool.release();
	}
}

static NSURL getURL (String fileName) {
	NSString unescapedStr;
	String lowercaseName = fileName.toLowerCase ();
	if (lowercaseName.startsWith (PREFIX_HTTP) || lowercaseName.startsWith (PREFIX_HTTPS) || lowercaseName.startsWith (PREFIX_FILE)) {
		unescapedStr = NSString.stringWith("%#"); //$NON-NLS-1$
	} else {
		unescapedStr = NSString.stringWith("%"); //$NON-NLS-1$
	}
	NSString fullPath = NSString.stringWith(fileName);
	if (NSFileManager.defaultManager().fileExistsAtPath(fullPath)) {
		fullPath = NSURL.fileURLWithPath(fullPath).absoluteString();
	}
	long ptr = OS.CFURLCreateStringByAddingPercentEscapes(0, fullPath.id, unescapedStr.id, 0, OS.kCFStringEncodingUTF8);
	NSString escapedString = new NSString(ptr);
	NSURL url = NSURL.URLWithString(escapedString);
	OS.CFRelease(ptr);
	return url;
}

static boolean isExecutable (String fileName) {
	long ptr = C.malloc(1);
	NSString path = NSString.stringWith(fileName);
	boolean result = false;
	NSFileManager manager = NSFileManager.defaultManager();
	if (manager.fileExistsAtPath(path, ptr)) {
		byte[] isDirectory = new byte[1];
		C.memmove(isDirectory, ptr, 1);
		if (isDirectory[0] == 0 && manager.isExecutableFileAtPath(path)) {
			NSWorkspace ws = NSWorkspace.sharedWorkspace();
			NSString type = ws.typeOfFile(path, 0);
			result = type != null && (ws.type(type, NSString.stringWith("public.unix-executable")) || //$NON-NLS-1$
					OS.UTTypeEqual(type.id, NSString.stringWith("public.shell-script").id)); //$NON-NLS-1$
		}
	}
	C.free(ptr);
	return result;
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
	return launch (fileName, null);
}

/**
 * Launches the operating system executable associated with the file or
 * URL (http:// or https://).  If the file is an executable then the
 * executable is launched. The program is launched with the specified
 * working directory only when the <code>workingDir</code> exists and
 * <code>fileName</code> is an executable.
 * Note that a <code>Display</code> must already exist to guarantee
 * that this method returns an appropriate result.
 *
 * @param fileName the file name or program name or URL (http:// or https://)
 * @param workingDir the name of the working directory or null
 * @return <code>true</code> if the file is launched, otherwise <code>false</code>
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when fileName is null</li>
 * </ul>
 *
 * @since 3.6
 */
public static boolean launch (String fileName, String workingDir) {
	if (fileName == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	NSAutoreleasePool pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		if (workingDir != null && isExecutable(fileName)) {
			try {
				Compatibility.exec(new String[] {fileName}, null, workingDir);
				return true;
			} catch (IOException e) {
				return false;
			}
		}
		NSURL url = getURL(fileName);
		NSWorkspace workspace = NSWorkspace.sharedWorkspace();
		return workspace.openURL(url);
	} finally {
		pool.release();
	}
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
	NSAutoreleasePool pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		NSWorkspace workspace = NSWorkspace.sharedWorkspace();
		NSURL url = getURL(fileName);
		NSArray urls = NSArray.arrayWithObject(url);
		return workspace.openURLs(urls, NSString.stringWith(identifier), 0, null, 0);
	} finally {
		pool.release();
	}
}

/**
 * Returns the receiver's image data.  This is the icon
 * that is associated with the receiver in the operating
 * system.
 *
 * @return the image data for the program, may be null
 */
public ImageData getImageData () {
	NSAutoreleasePool pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		NSWorkspace workspace = NSWorkspace.sharedWorkspace();
		NSString fullPath;
		if (this.fullPath != null) {
			fullPath = NSString.stringWith(this.fullPath);
		} else {
			fullPath = workspace.fullPathForApplication(NSString.stringWith(name));
		}
		if (fullPath != null) {
			NSImage nsImage = workspace.iconForFile(fullPath);
			if (nsImage != null) {
				NSSize size = new NSSize();
				size.width = size.height = 16;
				nsImage.setSize(size);
				nsImage.retain();
				Image image = Image.cocoa_new(Display.getCurrent(), SWT.BITMAP, nsImage);
				ImageData imageData = image.getImageData();
				image.dispose();
				return imageData;
			}
		}
		return null;
	} finally {
		pool.release();
	}
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
@Override
public boolean equals(Object other) {
	if (this == other) return true;
	if (other instanceof final Program program) {
		return name.equals(program.name) && identifier.equals(program.identifier);
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
@Override
public int hashCode() {
	return name.hashCode() ^ identifier.hashCode();
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the program
 */
@Override
public String toString () {
	return "Program {" + name + "}";
}

}
