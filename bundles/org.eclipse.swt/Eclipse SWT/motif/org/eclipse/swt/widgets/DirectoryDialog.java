package org.eclipse.swt.widgets;

/*
* Licensed Materials - Property of IBM,
* SWT - The Simple Widget Toolkit,
* (c) Copyright IBM Corp 1998, 1999.
*/

/* Imports */
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;

/**
* A DirectoryDialog allows the user to navigate
* the file and select a directory.
*/

/* Class Definition */
public /*final*/ class DirectoryDialog extends Dialog {
	String filterPath = "";
	boolean cancel = true;
	String message = "";
public DirectoryDialog (Shell parent) {
	this (parent, SWT.PRIMARY_MODAL);
}
public DirectoryDialog (Shell parent, int style) {
	super (parent, style);
}
int activate (int widget, int client, int call) {
	cancel = client == OS.XmDIALOG_CANCEL_BUTTON;
	OS.XtUnmanageChild (widget);
	return 0;
}
public String getFilterPath () {
	return filterPath;
}
public String getMessage () {
	return message;
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
	byte [] buffer2 = Converter.wcsToMbcs (null, "*", true);
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

	byte [] buffer7 = Converter.wcsToMbcs (null, "Selection", true);
	int xmStringPtr4 = OS.XmStringParseText (
		buffer7,
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
		OS.XmNfilterLabelString, xmStringPtr4
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
	child = OS.XmFileSelectionBoxGetChild (dialog, OS.XmDIALOG_LIST);
	if (child != 0) {
		int parent2 = OS.XtParent(child);
		if (parent2 !=0) OS.XtUnmanageChild (parent2);
	}
	child = OS.XmFileSelectionBoxGetChild (dialog, OS.XmDIALOG_LIST_LABEL);
	if (child != 0) OS.XtUnmanageChild (child);
	child = OS.XmFileSelectionBoxGetChild (dialog, OS.XmDIALOG_TEXT);
	if (child != 0) OS.XtUnmanageChild (child);
	child = OS.XmFileSelectionBoxGetChild (dialog, OS.XmDIALOG_SELECTION_LABEL);
	if (child != 0) OS.XtUnmanageChild (child);
	OS.XmStringFree (xmStringPtr1);
	OS.XmStringFree (xmStringPtr2);
	OS.XmStringFree (xmStringPtr3);
	OS.XmStringFree (xmStringPtr4);

	// Add label widget for message text.
	byte [] buffer4 = Converter.wcsToMbcs (null, message, true);
	int [] parseTable = Display.getDefault ().parseTable;
	int xmString1 = OS.XmStringParseText (
		buffer4,
		0,
		OS.XmFONTLIST_DEFAULT_TAG, 
		OS.XmCHARSET_TEXT, 
		parseTable,
		parseTable.length,
		0);
	int [] argList = {
		OS.XmNlabelType, OS.XmSTRING,
		OS.XmNlabelString, xmString1
	};
	int textArea = OS.XmCreateLabel(dialog, name, argList, argList.length/2);
	OS.XtManageChild(textArea);

	/* Hook the callbacks. */
	Callback callback = new Callback (this, "activate", 3);
	int address = callback.getAddress ();
	OS.XtAddCallback (dialog, OS.XmNokCallback, address, OS.XmDIALOG_OK_BUTTON);
	OS.XtAddCallback (dialog, OS.XmNcancelCallback, address, OS.XmDIALOG_CANCEL_BUTTON);

	/* Open the dialog and dispatch events. */
	cancel = true;
	OS.XtManageChild (dialog);
	
//BOGUS - should be a pure OS message loop (no SWT AppContext)
	while (OS.XtIsRealized (dialog) && OS.XtIsManaged (dialog))
		if (!appContext.readAndDispatch ()) appContext.sleep ();

	/* Set the new path, file name and filter. */
	String directoryPath="";
	if (!cancel) {
		int [] argList2 = {OS.XmNdirMask, 0};
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
			directoryPath = new String (Converter.mbcsToWcs (null, buffer));
		}
		OS.XmStringFree (xmString3);
		int length = directoryPath.length ();
		if (length != 0) {
			if (directoryPath.charAt (length -1) == '/') {
				directoryPath = directoryPath.substring (0, length - 1);
			} else {
				if (length > 1 && directoryPath.charAt (length - 2) == '/' && directoryPath.charAt (length - 1) == '*') {
					directoryPath = directoryPath.substring (0, length - 2);
				}
			}
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
	return directoryPath;
}
public void setFilterPath (String string) {
	filterPath = string;
}
public void setMessage (String string) {
	message = string;
}
}
