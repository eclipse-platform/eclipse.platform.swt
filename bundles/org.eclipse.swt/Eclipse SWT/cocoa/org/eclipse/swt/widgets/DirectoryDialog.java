/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.*;


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
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#directorydialog">DirectoryDialog snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample, Dialog tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class DirectoryDialog extends Dialog {
	NSOpenPanel panel;
	String message = "", filterPath = "";

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
	String directoryPath = null;
	panel = NSOpenPanel.openPanel();

	// Install a callback so editing keys work.
	int /*long*/ panelClass = 0;
	int /*long*/ swtPanelClass = 0;
	String className = "SWTFileDialogPanel";
	Callback performKeyEquivalentCallback = null;
	performKeyEquivalentCallback = new Callback(this, "performKeyEquivalent", 3);
	int /*long*/ proc = performKeyEquivalentCallback.getAddress();
	if (proc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	swtPanelClass = OS.objc_allocateClassPair(OS.object_getClass(panel.id), className, 0);
	OS.class_addMethod(swtPanelClass, OS.sel_performKeyEquivalent_, proc, "@:@");
	OS.objc_registerClassPair(swtPanelClass);
	panelClass = OS.object_setClass(panel.id, swtPanelClass);
	
	OS.objc_msgSend(panel.id, OS.sel_setShowsHiddenFiles_, true);
	panel.setCanCreateDirectories(true);
	panel.setAllowsMultipleSelection((style & SWT.MULTI) != 0);
	panel.setTitle(NSString.stringWith(title != null ? title : ""));
	panel.setMessage(NSString.stringWith(message != null ? message : ""));
	panel.setCanChooseFiles(false);
	panel.setCanChooseDirectories(true);
	NSApplication application = NSApplication.sharedApplication();
	if (parent != null && (style & SWT.SHEET) != 0) {
		application.beginSheet(panel, parent.view.window (), null, 0, 0);
	}
	Display display = parent != null ? parent.getDisplay() : Display.getCurrent();
	display.setModalDialog(this);
	NSString dir = (filterPath != null && filterPath.length() > 0) ? NSString.stringWith(filterPath) : null;
	int /*long*/ response = panel.runModalForDirectory(dir, null);
	if (parent != null && (style & SWT.SHEET) != 0) {
		application.endSheet(panel, 0);
	}
	display.setModalDialog(null);
	OS.object_setClass(panel.id, panelClass);
	OS.objc_disposeClassPair(swtPanelClass);
	if (performKeyEquivalentCallback != null) performKeyEquivalentCallback.dispose();
	if (response == OS.NSFileHandlingPanelOKButton) {
		NSString filename = panel.filename();
		directoryPath = filterPath = filename.getString();
	}
//	options.optionFlags = OS.kNavSupportPackages | OS.kNavAllowOpenPackages | OS.kNavAllowInvisibleFiles;
	return directoryPath;
}

int /*long*/ performKeyEquivalent(int /*long*/ id, int /*long*/ sel, int /*long*/ arg) {
	NSEvent nsEvent = new NSEvent(arg);
	int stateMask = 0;
	int /*long*/ selector = 0;
	int /*long*/ modifierFlags = nsEvent.modifierFlags();
	if ((modifierFlags & OS.NSAlternateKeyMask) != 0) stateMask |= SWT.ALT;
	if ((modifierFlags & OS.NSShiftKeyMask) != 0) stateMask |= SWT.SHIFT;
	if ((modifierFlags & OS.NSControlKeyMask) != 0) stateMask |= SWT.CONTROL;
	if ((modifierFlags & OS.NSCommandKeyMask) != 0) stateMask |= SWT.COMMAND;
	if (stateMask == SWT.COMMAND) {
		short keyCode = nsEvent.keyCode ();
		switch (keyCode) {
			case 7: /* X */
				selector = OS.sel_cut_;
				break;
			case 8: /* C */
				selector = OS.sel_copy_;
				break;
			case 9: /* V */
				selector = OS.sel_paste_;
				break;
			case 0: /* A */
				selector = OS.sel_selectAll_;
				break;
		}
		
		if (selector != 0) {
			NSApplication.sharedApplication().sendAction(selector, null, panel);
			return 1;
		}
	}

	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	return OS.objc_msgSendSuper(super_struct, sel, arg);
}

/**
 * Sets the dialog's message, which is a description of
 * the purpose for which it was opened. This message will be
 * visible on the dialog while it is open.
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
