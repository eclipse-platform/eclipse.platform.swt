package org.eclipse.swt.widgets;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */
 
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;

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
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public /*final*/ class FileDialog extends Dialog {
	String [] filterNames = new String [0];
	String [] filterExtensions = new String [0];
	String filterPath = "";
	String fullPath = "";
	String fileName = "";
	boolean cancel = true;
	static final String FILTER = "*";

public FileDialog (Shell parent) {
	this (parent, SWT.PRIMARY_MODAL);
}
public FileDialog (Shell parent, int style) {
	super (parent, style);
}
int activate (int widget, int client, int call) {
	cancel = client == OS.XmDIALOG_CANCEL_BUTTON;
	OS.XtUnmanageChild (widget);
	return 0;
}
public String getFileName () {
	return fileName;
}
public String [] getFileNames () {
	return new String [] {fileName};
}
public String [] getFilterExtensions () {
	return filterExtensions;
}
public String [] getFilterNames () {
	return filterNames;
}
public String getFilterPath () {
	return filterPath;
}
public String open () {

	/* Get the parent */
	boolean destroyContext;
	Display appContext = Display.getCurrent ();
	if (destroyContext = (appContext == null)) appContext = new Display ();
	int display = appContext.xDisplay;
	int parentHandle = appContext.shellHandle;
	if ((parent != null) && (parent.getDisplay () == appContext))
		parentHandle = parent.shellHandle;

	/* Compute the dialog title */	
	/*
	* Feature in Motif.  It is not possible to set a shell
	* title to an empty string.  The fix is to set the title
	* to be a single space.
	*/
	String string = title;
	if (string.length () == 0) string = " ";
	byte [] buffer1 = Converter.wcsToMbcs (null, string, true);
	int xmStringPtr1 = OS.XmStringParseText (
		buffer1,
		0,
		OS.XmFONTLIST_DEFAULT_TAG, 
		OS.XmCHARSET_TEXT, 
		null,
		0,
		0);

	/* Compute the filter */
	String mask = FILTER;
	if (filterExtensions == null) filterExtensions = new String [0];
	if (filterNames == null) filterNames = new String [0];
	if (filterExtensions.length != 0) {
		/* Motif does not support multiple filters, so ignore them
		 * if there are more than one, or if there is a ; separator.
		 */
		if (filterExtensions.length == 1) {
			String filter = filterExtensions [0];
			if (filter.indexOf (';', 0) == -1) mask = filter;
		}
	}
	byte [] buffer2 = Converter.wcsToMbcs (null, mask, true);
	int xmStringPtr2 = OS.XmStringParseText (
		buffer2,
		0,
		OS.XmFONTLIST_DEFAULT_TAG, 
		OS.XmCHARSET_TEXT, 
		null,
		0,
		0);

	/* Compute the filter path */
	if (filterPath == null) filterPath = "";
	byte [] buffer3 = Converter.wcsToMbcs (null, filterPath, true);
	int xmStringPtr3 = OS.XmStringParseText (
		buffer3,
		0,
		OS.XmFONTLIST_DEFAULT_TAG, 
		OS.XmCHARSET_TEXT, 
		null,
		0,
		0);

	/* Create the dialog */
	int [] argList1 = {
		OS.XmNresizePolicy, OS.XmRESIZE_NONE,
		OS.XmNdialogStyle, OS.XmDIALOG_PRIMARY_APPLICATION_MODAL,
		OS.XmNwidth, OS.XDisplayWidth (display, OS.XDefaultScreen (display)) * 4 / 9,
		OS.XmNdialogTitle, xmStringPtr1,
		OS.XmNpattern, xmStringPtr2,
		OS.XmNdirectory, xmStringPtr3,
	};
	/*
	* Feature in Linux.  For some reason, the XmCreateFileSelectionDialog()
	* will not accept NULL for the widget name.  This works fine on the other
	* Motif platforms and in the other XmCreate calls on Linux.  The fix is
	* to pass in a NULL terminated string, not a NULL pointer.
	*/
	byte [] name = new byte [] {0};
	int dialog = OS.XmCreateFileSelectionDialog (parentHandle, name, argList1, argList1.length / 2);
	int child = OS.XmFileSelectionBoxGetChild (dialog, OS.XmDIALOG_HELP_BUTTON);
	if (child != 0) OS.XtUnmanageChild (child);
	OS.XmStringFree (xmStringPtr1);
	OS.XmStringFree (xmStringPtr2);
	OS.XmStringFree (xmStringPtr3);
/*
	string := OSWidget xmStringAt: XmNdirectory handle: dialog.
	OSWidget xmStringAt: XmNdirSpec put: string, fileName handle: dialog.

	"Select the matching file in the list and scroll to show it."
	child := dialog xmFileSelectionBoxGetChild: XmDIALOGLIST.
	child isNull ifFalse: [
		string := OSWidget xmStringAt: XmNdirSpec handle: dialog.
		string := PlatformConverter wcsToMbcs: 0 buffer: string.
		xmString := OSXmString xmStringCreateLocalized: string asPSZ.
		child
			xmListSelectItem: xmString notify: false;
			xmListSetItem: xmString.
		xmString xmStringFree.

		"Bug in Solaris.  For some reason, the horizontal scroll bar in the dialog
		list refuses to be displayed.  This stops the dialog list from scrolling
		horizontally and displaying the file names.  This does not happen on other
		Motif platforms.  The fix is to force the horizontal scroll bar to be displayed
		by explicitly setting the scroll bar display policy."
		OSWidget resourceAt: XmNscrollBarDisplayPolicy put: XmSTATIC handle: child].
*/

	/* Hook the callbacks. */
	Callback callback = new Callback (this, "activate", 3);
	int address = callback.getAddress ();
	OS.XtAddCallback (dialog, OS.XmNokCallback, address, OS.XmDIALOG_OK_BUTTON);
	OS.XtAddCallback (dialog, OS.XmNcancelCallback, address, OS.XmDIALOG_CANCEL_BUTTON);

	/* Open the dialog and dispatch events. */
	cancel = true;
/*
	shell == nil ifFalse: [
		shell minimized ifTrue: [shell minimized: false]].
*/
	OS.XtManageChild (dialog);
	
//BOGUS - should be a pure OS message loop (no SWT AppContext)
	while (OS.XtIsRealized (dialog) && OS.XtIsManaged (dialog))
		if (!appContext.readAndDispatch ()) appContext.sleep ();

	/* Set the new path, file name and filter. */
	fullPath = "";
	if (!cancel) {
		filterPath = fullPath = "";
		int [] argList2 = {OS.XmNdirectory, 0, OS.XmNdirSpec, 0};
		OS.XtGetValues (dialog, argList2, argList2.length / 2);
		int xmString3 = argList2 [1];
		int ptr = OS.XmStringUnparse (
			xmString3,
			null,
			OS.XmCHARSET_TEXT,
			OS.XmCHARSET_TEXT,
			null,
			0,
			OS.XmOUTPUT_ALL);
		if (ptr != 0) {
			int length = OS.strlen (ptr);
			byte [] buffer = new byte [length];
			OS.memmove (buffer, ptr, length);
			OS.XtFree (ptr);
			filterPath = new String (Converter.mbcsToWcs (null, buffer));
		}
		OS.XmStringFree (xmString3);
		int xmString4 = argList2 [3];
		ptr = OS.XmStringUnparse (
			xmString4,
			null,
			OS.XmCHARSET_TEXT,
			OS.XmCHARSET_TEXT,
			null,
			0,
			OS.XmOUTPUT_ALL);
		if (ptr != 0) {
			int length = OS.strlen (ptr);
			byte [] buffer = new byte [length];
			OS.memmove (buffer, ptr, length);
			OS.XtFree (ptr);
			fullPath = new String (Converter.mbcsToWcs (null, buffer));
		}
		OS.XmStringFree (xmString4);
		int length = filterPath.length ();
		if ((length != 0) && (filterPath.charAt (length - 1) == '/')) {
			filterPath = filterPath.substring (0, length - 1);
			int index = fullPath.length () - 1;
			while ((index >= 0) && (fullPath.charAt (index) != '/')) --index;
			fileName = fullPath.substring (index, fullPath.length ());
		}
	}

	/* Destroy the dialog and update the display. */
	if (OS.XtIsRealized (dialog)) OS.XtDestroyWidget (dialog);
	if (destroyContext) appContext.dispose ();
	callback.dispose ();
	
//	(shell == nil or: [shell isDestroyed not]) ifTrue: [dialog xtDestroyWidget].
//	OSWidget updateDisplay.
//	entryPoint unbind.

	if (cancel) return null;
	return fullPath;
}
public void setFileName (String string) {
	fileName = string;
}
public void setFilterExtensions (String [] extensions) {
	filterExtensions = extensions;
}
public void setFilterNames (String [] names) {
	filterNames = names;
}
public void setFilterPath (String string) {
	filterPath = string;
}
}
