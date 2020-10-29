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
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cocoa.*;


/**
 * Instances of this class allow the user to navigate
 * the file system and select a directory.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#directorydialog">DirectoryDialog snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample, Dialog tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class DirectoryDialog extends Dialog {
	Callback callback_performKeyEquivalent;
	Callback completion_handler_callback;
	NSOpenPanel panel;
	String directoryPath;
	String message = "", filterPath = "";
	long method_performKeyEquivalent = 0;
	long methodImpl_performKeyEquivalent = 0;

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
public DirectoryDialog (Shell parent) {
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
 */
public DirectoryDialog (Shell parent, int style) {
	super (parent, checkStyle (parent, style));
	if (Display.getSheetEnabled ()) {
		if (parent != null && (style & SWT.SHEET) != 0) this.style |= SWT.SHEET;
	}
	checkSubclass ();
}

long _completionHandler (long result) {
	handleResponse(result);
	return result;
}

long _performKeyEquivalent (long id, long sel, long event) {
	boolean result = false;
	NSEvent nsEvent = new NSEvent(event);
	NSWindow window = nsEvent.window ();
	if (window != null) {
		result = parent.display.performKeyEquivalent(window, nsEvent);
	}
	return result ? 1 : 0;
}

/**
 * Returns the path which the dialog will use to filter
 * the directories it shows.
 *
 * @return the filter path
 *
 * @see #setFilterPath
 */
public String getFilterPath () {
	return filterPath;
}

/**
 * Returns the dialog's message, which is a description of
 * the purpose for which it was opened. This message will be
 * visible on the dialog while it is open.
 *
 * @return the message
 */
public String getMessage () {
	return message;
}

void handleResponse (long response) {
	if (parent != null && (style & SWT.SHEET) != 0) {
		NSApplication.sharedApplication().stopModal();
	}
	Display display = parent != null ? parent.getDisplay() : Display.getCurrent();
	display.setModalDialog(null);
	if (response  == OS.NSFileHandlingPanelOKButton) {
		NSString filename = panel.filename();
		directoryPath = filterPath = filename.getString();
	}
	releaseHandles();
}

/**
 * Makes the dialog visible and brings it to the front
 * of the display.
 *
 * @return a string describing the absolute path of the selected directory,
 *         or null if the dialog was cancelled or an error occurred
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the dialog has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the dialog</li>
 * </ul>
 */
public String open () {
	directoryPath = null;
	panel = NSOpenPanel.openPanel();
	if (panel == null) {
		return null;
	}

	callback_performKeyEquivalent = new Callback(this, "_performKeyEquivalent", 3);
	long proc = callback_performKeyEquivalent.getAddress();
	method_performKeyEquivalent = OS.class_getInstanceMethod(OS.class_NSSavePanel, OS.sel_performKeyEquivalent_);
	if (method_performKeyEquivalent != 0) {
		methodImpl_performKeyEquivalent = OS.method_setImplementation(method_performKeyEquivalent, proc);
	}

	/*
	 * This line is intentionally commented. Don't show hidden files forcefully,
	 * instead allow Directory dialog to use the system preference.
	 */
	//	OS.objc_msgSend(panel.id, OS.sel_setShowsHiddenFiles_, true);
	panel.setCanCreateDirectories(true);
	panel.setAllowsMultipleSelection((style & SWT.MULTI) != 0);
	panel.setTitle(NSString.stringWith(title != null ? title : ""));
	panel.setMessage(NSString.stringWith(message != null ? message : ""));
	panel.setCanChooseFiles(false);
	panel.setCanChooseDirectories(true);
	panel.setTreatsFilePackagesAsDirectories(true);

	NSString dir = (filterPath != null && filterPath.length() > 0) ? NSString.stringWith(filterPath) : null;
	panel.setDirectoryURL(NSURL.fileURLWithPath(dir));

	Display display = parent != null ? parent.getDisplay() : Display.getCurrent();
	display.setModalDialog(this, panel);

	if (parent != null && (style & SWT.SHEET) != 0) {
		completion_handler_callback = new Callback(this, "_completionHandler", 1);
		long handler = completion_handler_callback.getAddress();
		OS.beginSheetModalForWindow(panel, parent.view.window(), handler);
		NSApplication.sharedApplication().runModalForWindow(parent.view.window());
	} else {
		long response = panel.runModal();
		handleResponse(response);
	}

//	options.optionFlags = OS.kNavSupportPackages | OS.kNavAllowOpenPackages | OS.kNavAllowInvisibleFiles;
	return directoryPath;
}

void releaseHandles () {
	if (method_performKeyEquivalent != 0) {
		OS.method_setImplementation(method_performKeyEquivalent, methodImpl_performKeyEquivalent);
	}
	if (callback_performKeyEquivalent != null) callback_performKeyEquivalent.dispose();
	callback_performKeyEquivalent = null;

	if (completion_handler_callback != null) {
		completion_handler_callback.dispose();
		completion_handler_callback = null;
	}
	panel = null;
}

/**
 * Sets the dialog's message, which is a description of
 * the purpose for which it was opened. This message will be
 * visible on the dialog while it is open.
 * <p>
 * NOTE: This operation is a hint and is not supported on some platforms. For
 * example, on Windows (Vista and later), the <code>DirectoryDialog</code>
 * doesn't have any provision to set a message.
 * </p>
 *
 * @param string the message
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 */
public void setMessage (String string) {
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	message = string;
}

/**
 * Sets the path that the dialog will use to filter
 * the directories it shows to the argument, which may
 * be null. If the string is null, then the operating
 * system's default filter path will be used.
 * <p>
 * Note that the path string is platform dependent.
 * For convenience, either '/' or '\' can be used
 * as a path separator.
 * </p>
 *
 * @param string the filter path
 */
public void setFilterPath (String string) {
	filterPath = string;
}
}
