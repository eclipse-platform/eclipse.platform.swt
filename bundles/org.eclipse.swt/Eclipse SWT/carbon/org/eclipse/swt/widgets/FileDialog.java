/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

 
import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.carbon.*;

/**
 * Instances of this class allow the user to navigate
 * the file system and select or enter a file name.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SAVE, OPEN, MULTI</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles SAVE and OPEN may be specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#filedialog">FileDialog snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample, Dialog tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class FileDialog extends Dialog {
	String [] filterNames = new String [0];
	String [] filterExtensions = new String [0];
	String [] fileNames = new String[0];	
	String filterPath = "", fileName = "";
	int filterIndex = 0;
	boolean overwrite = false;
	static final char EXTENSION_SEPARATOR = ';';

/**
 * Constructs a new instance of this class given only its parent.
 *
 * @param parent a shell which will be the parent of the new instance
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public FileDialog (Shell parent) {
	this (parent, SWT.APPLICATION_MODAL);
}

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a shell which will be the parent of the new instance
 * @param style the style of dialog to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 * 
 * @see SWT#SAVE
 * @see SWT#OPEN
 * @see SWT#MULTI
 */
public FileDialog (Shell parent, int style) {
	super (parent, checkStyle (parent, style));
	checkSubclass ();
}

/**
 * Returns the path of the first file that was
 * selected in the dialog relative to the filter path, or an
 * empty string if no such file has been selected.
 * 
 * @return the relative path of the file
 */
public String getFileName () {
	return fileName;
}

/**
 * Returns a (possibly empty) array with the paths of all files
 * that were selected in the dialog relative to the filter path.
 * 
 * @return the relative paths of the files
 */
public String [] getFileNames () {
	return fileNames;
}

/**
 * Returns the file extensions which the dialog will
 * use to filter the files it shows.
 *
 * @return the file extensions filter
 */
public String [] getFilterExtensions () {
	return filterExtensions;
}

/**
 * Get the 0-based index of the file extension filter
 * which was selected by the user, or -1 if no filter
 * was selected.
 * <p>
 * This is an index into the FilterExtensions array and
 * the FilterNames array.
 * </p>
 *
 * @return index the file extension filter index
 * 
 * @see #getFilterExtensions
 * @see #getFilterNames
 * 
 * @since 3.4
 */
public int getFilterIndex () {
	return filterIndex;
}

/**
 * Returns the names that describe the filter extensions
 * which the dialog will use to filter the files it shows.
 *
 * @return the list of filter names
 */
public String [] getFilterNames () {
	return filterNames;
}

/**
 * Returns the directory path that the dialog will use, or an empty
 * string if this is not set.  File names in this path will appear
 * in the dialog, filtered according to the filter extensions.
 *
 * @return the directory path string
 * 
 * @see #setFilterExtensions
 */
public String getFilterPath () {
	return filterPath;
}

/**
 * Returns the flag that the dialog will use to
 * determine whether to prompt the user for file
 * overwrite if the selected file already exists.
 *
 * @return true if the dialog will prompt for file overwrite, false otherwise
 * 
 * @since 3.4
 */
public boolean getOverwrite () {
	return overwrite;
}

int eventProc (int callBackSelector, int callBackParms, int callBackUD) {
	switch (callBackSelector) {
		case OS.kNavCBPopupMenuSelect:
			NavCBRec cbRec = new NavCBRec ();
			OS.memmove (cbRec, callBackParms, NavCBRec.sizeof);
			if (cbRec.eventData.eventDataParms.param != 0) {
				NavMenuItemSpec spec = new NavMenuItemSpec ();
				OS.memmove (spec, cbRec.eventData.eventDataParms.param, NavMenuItemSpec.sizeof);
				int index = spec.menuType;
				if (0 <= index && index < filterExtensions.length) {
					filterIndex = index;
				}
			}
			break;
	}
	return 0;
}

int filterProc (int theItem, int infoPtr, int callBackUD, int filterMode) {
	if (filterMode == OS.kNavFilteringBrowserList) {
		if (filterExtensions != null && 0 <= filterIndex && filterIndex < filterExtensions.length) {
			NavFileOrFolderInfo info = new NavFileOrFolderInfo();
			OS.memmove (info, infoPtr, NavFileOrFolderInfo.sizeof);
			if (!info.isFolder) {
				OS.AECoerceDesc (theItem, OS.typeFSRef, theItem);
				byte [] fsRef = new byte [80];
				if (OS.AEGetDescData (theItem, fsRef, fsRef.length) == OS.noErr) {
					int url = OS.CFURLCreateFromFSRef (OS.kCFAllocatorDefault, fsRef);
					if (url != 0) {
						int ext = OS.CFURLCopyPathExtension (url);
						OS.CFRelease (url);
						if (ext != 0) {
							char [] buffer= new char [OS.CFStringGetLength (ext)];
							if (buffer.length > 0) {
								CFRange range = new CFRange ();
								range.length = buffer.length;
								OS.CFStringGetCharacters (ext, range, buffer);
							}
							OS.CFRelease (ext);
							String extension = new String (buffer);
							String extensions = filterExtensions [filterIndex];
							int start = 0, length = extensions.length ();
							while (start < length) {
								int index = extensions.indexOf (EXTENSION_SEPARATOR, start);
								if (index == -1) index = length;
								String filter = extensions.substring (start, index).trim ();
								if (filter.equals ("*") || filter.equals ("*.*")) return 1;
								if (filter.startsWith ("*.")) filter = filter.substring (2);
								if (filter.toLowerCase ().equals(extension.toLowerCase ())) return 1;
								start = index + 1;
							}
							return 0;
						}
					}
				}
				return 0;
			}
		}
	}
	return 1;
}

String getString (int cfString) {
	if (cfString == 0) return "";
	int length = OS.CFStringGetLength (cfString);
	char [] buffer= new char [length];
	CFRange range = new CFRange ();
	range.length = length;
	OS.CFStringGetCharacters (cfString, range, buffer);
	return new String (buffer);
}

/**
 * Makes the dialog visible and brings it to the front
 * of the display.
 *
 * @return a string describing the absolute path of the first selected file,
 *         or null if the dialog was cancelled or an error occurred
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the dialog has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the dialog</li>
 * </ul>
 */
public String open () {
	String fullPath = null;
	fileNames = new String [0];
		
	int titlePtr = 0;
	if (title != null) {
		char [] buffer = new char [title.length ()];
		title.getChars (0, buffer.length, buffer, 0);
		titlePtr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	}
	int fileNamePtr = 0;
	if (fileName != null) {
		char [] buffer = new char [fileName.length ()];
		fileName.getChars (0, buffer.length, buffer, 0);
		fileNamePtr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);		
	}
		
	NavDialogCreationOptions options = new NavDialogCreationOptions ();
	options.windowTitle = options.clientName = titlePtr;
	options.parentWindow = OS.GetControlOwner (parent.handle);
	options.optionFlags = OS.kNavSupportPackages | OS.kNavAllowInvisibleFiles;
	options.location_h = -1;
	options.location_v = -1;
	options.saveFileName = fileNamePtr;
	options.modality = OS.kWindowModalityAppModal;

	int extensions = 0;
	Callback filterCallback = null, eventCallback = null;
	int [] outDialog = new int [1];
	if ((style & SWT.SAVE) != 0) {
		if (!overwrite) options.optionFlags |= OS.kNavDontConfirmReplacement;
		OS.NavCreatePutFileDialog (options, 0, 0, 0, 0, outDialog);		
	} else {
		if ((style & SWT.MULTI) != 0) options.optionFlags |= OS.kNavAllowMultipleFiles;
		int filterProc = 0, eventProc = 0;
		if (filterExtensions != null && filterExtensions.length != 0) {
			extensions = options.popupExtension = OS.CFArrayCreateMutable (OS.kCFAllocatorDefault, filterExtensions.length, 0);
			for (int i = 0; i < filterExtensions.length; i++) {
				String str = filterExtensions [i];
				if (filterNames != null && filterNames.length > i) {
					str = filterNames [i];
				}
				char [] chars = new char [str.length ()];
				str.getChars (0, chars.length, chars, 0);
				int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, chars, chars.length);
				if (ptr != 0) OS.CFArrayAppendValue (extensions, ptr);
			}			
			filterCallback = new Callback (this, "filterProc", 4);
			filterProc = filterCallback.getAddress();
			if (filterProc == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
			eventCallback = new Callback (this, "eventProc", 3);
			eventProc = eventCallback.getAddress();
			if (eventProc == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
		}
		OS.NavCreateGetFileDialog(options, 0, eventProc, 0, filterProc, 0, outDialog);
	}
	if (outDialog [0] != 0) {
		if (filterPath != null && filterPath.length () > 0) {
			char [] chars = new char [filterPath.length ()];
			filterPath.getChars (0, chars.length, chars, 0);
			int str = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, chars, chars.length);
			if (str != 0) {
				int url = OS.CFURLCreateWithFileSystemPath (OS.kCFAllocatorDefault, str, OS.kCFURLPOSIXPathStyle, false);
				if (url != 0) {
					byte [] fsRef = new byte [80];
					if (OS.CFURLGetFSRef (url, fsRef)) {
						AEDesc params = new AEDesc ();
						if (OS.AECreateDesc (OS.typeFSRef, fsRef, fsRef.length, params) == OS.noErr) {
							OS.NavCustomControl (outDialog [0], OS.kNavCtlSetLocation, params);
							OS.AEDisposeDesc (params);
						}
					}
					OS.CFRelease (url);
				}
				OS.CFRelease (str);
			}
		}
		if (filterExtensions != null && 0 <= filterIndex && filterIndex < filterExtensions.length) {
			NavMenuItemSpec spec = new NavMenuItemSpec ();
			spec.menuType = filterIndex;
			OS.NavCustomControl (outDialog [0], OS.kNavCtlSelectCustomType, spec);
		}
		OS.NavDialogRun (outDialog [0]);
		int action = OS.NavDialogGetUserAction (outDialog [0]);
		switch (action) {
			case OS.kNavUserActionOpen:
			case OS.kNavUserActionChoose:							
			case OS.kNavUserActionSaveAs: {
				NavReplyRecord record = new NavReplyRecord ();
				OS.NavDialogGetReply (outDialog [0], record);
				AEDesc selection = new AEDesc ();
				selection.descriptorType = record.selection_descriptorType;
				selection.dataHandle = record.selection_dataHandle;
				int [] count = new int [1];
				OS.AECountItems (selection, count);
				if (count [0] > 0) {
					fileNames = new String [count [0]];
					int maximumSize = 80; // size of FSRef
					int dataPtr = OS.NewPtr (maximumSize);
					int[] aeKeyword = new int [1];
					int[] typeCode = new int [1];
					int[] actualSize = new int [1];
												
					if ((style & SWT.SAVE) != 0) {
						if (OS.AEGetNthPtr (selection, 1, OS.typeFSRef, aeKeyword, typeCode, dataPtr, maximumSize, actualSize) == OS.noErr) {
							byte[] fsRef = new byte[actualSize[0]];
							OS.memmove (fsRef, dataPtr, actualSize [0]);
							int pathUrl = OS.CFURLCreateFromFSRef (OS.kCFAllocatorDefault, fsRef);
							
							/* Filter path */
							int pathString = OS.CFURLCopyFileSystemPath (pathUrl, OS.kCFURLPOSIXPathStyle);
							filterPath = getString (pathString);
							OS.CFRelease (pathString);

							/* Full path */
							int fullUrl = OS.CFURLCreateCopyAppendingPathComponent (OS.kCFAllocatorDefault, pathUrl, record.saveFileName, false);
							int fullString = OS.CFURLCopyFileSystemPath (fullUrl, OS.kCFURLPOSIXPathStyle);
							fullPath = getString (fullString);
							OS.CFRelease (fullString);
							OS.CFRelease (fullUrl);

							/* File name */
							fileName = fileNames [0] = getString (record.saveFileName);
							
							OS.CFRelease (pathUrl);
						}
					} else {
						for (int i = 0; i < count [0]; i++) {
							if (OS.AEGetNthPtr (selection, i+1, OS.typeFSRef, aeKeyword, typeCode, dataPtr, maximumSize, actualSize) == OS.noErr) {
								byte[] fsRef = new byte[actualSize[0]];
								OS.memmove (fsRef, dataPtr, actualSize [0]);
								int url = OS.CFURLCreateFromFSRef (OS.kCFAllocatorDefault, fsRef);
								int fullString = OS.CFURLCopyFileSystemPath (url, OS.kCFURLPOSIXPathStyle);
								
								/* File path */
								int pathUrl = OS.CFURLCreateCopyDeletingLastPathComponent (OS.kCFAllocatorDefault, url);
								int pathString = OS.CFURLCopyFileSystemPath (pathUrl, OS.kCFURLPOSIXPathStyle);
								String path = getString (pathString);
								OS.CFRelease (pathString);
								OS.CFRelease (pathUrl);

								if (i == 0) {
									/* Full path */
									fullPath = getString (fullString);

									/* Filter path */
									filterPath = path;

									/* File name */
									int fileString = OS.CFURLCopyLastPathComponent (url);
									fileName = fileNames [0] = getString (fileString);
									OS.CFRelease (fileString);
								} else {									
									if (path.equals (filterPath)) {
										int fileString = OS.CFURLCopyLastPathComponent (url);
										fileNames [i] = getString (fileString);
										OS.CFRelease (fileString);
									} else {
										fileNames [i] = getString (fullString);
									}
								}
								OS.CFRelease (fullString);
								OS.CFRelease (url);
							}
						}
					}
					OS.DisposePtr (dataPtr);
				}
				OS.NavDisposeReply (record);
			}
		}
	}

	if (titlePtr != 0) OS.CFRelease (titlePtr);
	if (fileNamePtr != 0) OS.CFRelease (fileNamePtr);	
	if (outDialog [0] != 0) OS.NavDialogDispose (outDialog [0]);
	if (extensions != 0) {
		int count = OS.CFArrayGetCount (extensions);
		for (int i = 0; i < count; i++) {
			OS.CFRelease (OS.CFArrayGetValueAtIndex (extensions, i));
		}			
		OS.CFRelease (extensions);
	}
	if (filterCallback != null) filterCallback.dispose();
	if (eventCallback != null) eventCallback.dispose();
	return fullPath;	
}

/**
 * Set the initial filename which the dialog will
 * select by default when opened to the argument,
 * which may be null.  The name will be prefixed with
 * the filter path when one is supplied.
 * 
 * @param string the file name
 */
public void setFileName (String string) {
	fileName = string;
}

/**
 * Set the file extensions which the dialog will
 * use to filter the files it shows to the argument,
 * which may be null.
 * <p>
 * The strings are platform specific. For example, on
 * some platforms, an extension filter string is typically
 * of the form "*.extension", where "*.*" matches all files.
 * For filters with multiple extensions, use semicolon as
 * a separator, e.g. "*.jpg;*.png".
 * </p>
 *
 * @param extensions the file extension filter
 * 
 * @see #setFilterNames to specify the user-friendly
 * names corresponding to the extensions
 */
public void setFilterExtensions (String [] extensions) {
	filterExtensions = extensions;
}

/**
 * Set the 0-based index of the file extension filter
 * which the dialog will use initially to filter the files
 * it shows to the argument.
 * <p>
 * This is an index into the FilterExtensions array and
 * the FilterNames array.
 * </p>
 *
 * @param index the file extension filter index
 * 
 * @see #setFilterExtensions
 * @see #setFilterNames
 * 
 * @since 3.4
 */
public void setFilterIndex (int index) {
	filterIndex = index;
}

/**
 * Sets the names that describe the filter extensions
 * which the dialog will use to filter the files it shows
 * to the argument, which may be null.
 * <p>
 * Each name is a user-friendly short description shown for
 * its corresponding filter. The <code>names</code> array must
 * be the same length as the <code>extensions</code> array.
 * </p>
 *
 * @param names the list of filter names, or null for no filter names
 * 
 * @see #setFilterExtensions
 */
public void setFilterNames (String [] names) {
	filterNames = names;
}

/**
 * Sets the directory path that the dialog will use
 * to the argument, which may be null. File names in this
 * path will appear in the dialog, filtered according
 * to the filter extensions. If the string is null,
 * then the operating system's default filter path
 * will be used.
 * <p>
 * Note that the path string is platform dependent.
 * For convenience, either '/' or '\' can be used
 * as a path separator.
 * </p>
 *
 * @param string the directory path
 * 
 * @see #setFilterExtensions
 */
public void setFilterPath (String string) {
	filterPath = string;
}

/**
 * Sets the flag that the dialog will use to
 * determine whether to prompt the user for file
 * overwrite if the selected file already exists.
 *
 * @param overwrite true if the dialog will prompt for file overwrite, false otherwise
 * 
 * @since 3.4
 */
public void setOverwrite (boolean overwrite) {
	this.overwrite = overwrite;
}
}
