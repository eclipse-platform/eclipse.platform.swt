package org.eclipse.swt.widgets;

/*
* Licensed Materials - Property of IBM,
* SWT - The Simple Widget Toolkit,
* (c) Copyright IBM Corp 1998, 1999.
*/

/**
*	A message box is used to inform or warn the
* the user that a particular situation has occurred.
*
* Styles
*
*	ICON_ERROR, ICON_INFORMATION, ICON_QUESTION, ICON_WARNING, ICON_WORKING,
*	OK, OK_CANCEL, YES_NO, YES_NO_CANCEL
*
* Events
*
**/

/* Imports */
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/* Class Definition */
public /*final*/ class MessageBox extends Dialog {
	int button;
	String message = "";
public MessageBox (Shell parent) {
	this (parent, SWT.OK | SWT.ICON_INFORMATION | SWT.APPLICATION_MODAL);
}
public MessageBox (Shell parent, int style) {
	super (parent, checkStyle (style));
}
int activate (int widget, int client, int call) {
	OS.XtUnmanageChild (widget);
	button = client;
	return 0;
}
static int checkStyle (int style) {
	if ((style & (SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL)) == 0) style |= SWT.APPLICATION_MODAL;
	int mask = (SWT.YES | SWT.NO | SWT.OK | SWT.CANCEL | SWT.ABORT | SWT.RETRY | SWT.IGNORE);
	int bits = style & mask;
	if (bits == SWT.OK || bits == SWT.CANCEL || bits == (SWT.OK | SWT.CANCEL)) return style;
	if (bits == SWT.YES || bits == SWT.NO || bits == (SWT.YES | SWT.NO) || bits == (SWT.YES | SWT.NO | SWT.CANCEL)) return style;
	if (bits == (SWT.RETRY | SWT.CANCEL) || bits == (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) return style;
	style = (style & ~mask) | SWT.OK;
	return style;
}
int createHandle (int parentHandle, int [] argList) {
	if ((style & SWT.ICON_ERROR) != 0) return OS.XmCreateErrorDialog (parentHandle, null, argList, argList.length / 2);
	if ((style & SWT.ICON_INFORMATION) != 0) return OS.XmCreateInformationDialog (parentHandle, null, argList, argList.length / 2);
	if ((style & SWT.ICON_QUESTION) != 0) return OS.XmCreateQuestionDialog (parentHandle, null, argList, argList.length / 2);
	if ((style & SWT.ICON_WARNING) != 0) return OS.XmCreateWarningDialog (parentHandle, null, argList, argList.length / 2);
	return OS.XmCreateMessageDialog (parentHandle, null, argList, argList.length / 2);
}
public String getMessage () {
	return message;
}
public int open () {
	
	/* Create the dialog.*/
	boolean destroyContext;
	Display appContext = Display.getCurrent ();
	if (destroyContext = (appContext == null)) appContext = new Display ();
	int display = appContext.xDisplay;
	int parentHandle = appContext.shellHandle;
	if (parent != null && parent.getDisplay () == appContext)
		parentHandle = parent.shellHandle;

	/* Compute the dialog title */	
	/*
	* Feature in Motif.  It is not possible to set a shell
	* title to an empty string.  The fix is to set the title
	* to be a single space.
	*/
	String string = title;
	if (string.length () == 0) string = " ";
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	int xmStringPtr = OS.XmStringParseText (
		buffer,
		0,
		OS.XmFONTLIST_DEFAULT_TAG, 
		OS.XmCHARSET_TEXT, 
		null,
		0,
		0);
	/*
	* Feature in Motif.  The modal values are only hints
	* to the window manager.  For example Enlightenment treats all modes 
	* as SWT.APPLICATION_MODAL.  The generic Motif
	* Window Manager honours all modes.
	*/
	int dialogStyle = OS.XmDIALOG_MODELESS;
	if ((style & SWT.PRIMARY_MODAL) != 0) dialogStyle = OS.XmDIALOG_PRIMARY_APPLICATION_MODAL;
	if ((style & SWT.APPLICATION_MODAL) != 0) dialogStyle = OS.XmDIALOG_FULL_APPLICATION_MODAL;
	if ((style & SWT.SYSTEM_MODAL) != 0) dialogStyle = OS.XmDIALOG_SYSTEM_MODAL;
	if (parent != null && dialogStyle == OS.XmDIALOG_MODELESS) {
		dialogStyle = OS.XmDIALOG_PRIMARY_APPLICATION_MODAL;
	}
	int [] argList = {
		OS.XmNnoResize, 1,
		OS.XmNresizePolicy, OS.XmRESIZE_NONE,
		OS.XmNdialogStyle, dialogStyle,
		OS.XmNdialogTitle, xmStringPtr,
	};
	int dialog = createHandle (parentHandle, argList);
	OS.XmStringFree (xmStringPtr);
	setMessage (dialog);
	setButtons (dialog);
	
	/* Hook the callbacks. */
	Callback callback = new Callback (this, "activate", 3);
	int address = callback.getAddress ();
	OS.XtAddCallback (dialog, OS.XmNokCallback, address, OS.XmDIALOG_OK_BUTTON);
	OS.XtAddCallback (dialog, OS.XmNcancelCallback, address, OS.XmDIALOG_CANCEL_BUTTON);
	OS.XtAddCallback (dialog, OS.XmNhelpCallback, address, OS.XmDIALOG_HELP_BUTTON);

	/* Open the dialog and dispatch events. */
/*
	shell == nil ifFalse: [
		shell minimized ifTrue: [shell minimized: false]].
*/
	OS.XtManageChild (dialog);
	
//BOGUS - should be a pure OS message loop (no SWT AppContext)
	while (OS.XtIsRealized (dialog) && OS.XtIsManaged (dialog))
		if (!appContext.readAndDispatch()) appContext.sleep ();

	/* Destroy the dialog and update the display. */
	if (OS.XtIsRealized (dialog)) OS.XtDestroyWidget (dialog);
	if (destroyContext) appContext.dispose ();
	callback.dispose ();
	
//	(shell == nil or: [shell isDestroyed not]) ifTrue: [dialog xtDestroyWidget].
//	OSWidget updateDisplay.
//	entryPoint unbind.

	if ((style & (SWT.YES | SWT.NO | SWT.CANCEL)) == (SWT.YES | SWT.NO | SWT.CANCEL)) {
		if (button == OS.XmDIALOG_OK_BUTTON) return SWT.YES;
		if (button == OS.XmDIALOG_CANCEL_BUTTON) return SWT.NO;
		return SWT.CANCEL;
	};
	if ((style & (SWT.YES | SWT.NO)) == (SWT.YES | SWT.NO)) {
		return (button == OS.XmDIALOG_OK_BUTTON) ? SWT.YES : SWT.NO;
	}
	if ((style & (SWT.OK | SWT.CANCEL)) == (SWT.OK | SWT.CANCEL)) {
		return (button == OS.XmDIALOG_OK_BUTTON) ? SWT.OK : SWT.CANCEL;
	}
	if ((style & SWT.OK) == SWT.OK) return SWT.OK;
	if ((style & (SWT.RETRY | SWT.CANCEL)) == (SWT.RETRY | SWT.CANCEL)) {
		return (button == OS.XmDIALOG_OK_BUTTON) ? SWT.RETRY : SWT.CANCEL;
	}
	if ((style & (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) == (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) {
		if (button == OS.XmDIALOG_OK_BUTTON) return SWT.ABORT;
		if (button == OS.XmDIALOG_CANCEL_BUTTON) return SWT.RETRY;
		return SWT.IGNORE;
	}
	return SWT.CANCEL;
}
void setButtons (int dialogHandle) {
	
	/* Get the button children */
	int ok = OS.XmMessageBoxGetChild (dialogHandle, OS.XmDIALOG_OK_BUTTON);
	int cancel = OS.XmMessageBoxGetChild (dialogHandle, OS.XmDIALOG_CANCEL_BUTTON);
	int help = OS.XmMessageBoxGetChild (dialogHandle, OS.XmDIALOG_HELP_BUTTON);
	OS.XtUnmanageChild (help);

	/* Set the button labels */
	if ((style & (SWT.OK | SWT.CANCEL)) == (SWT.OK | SWT.CANCEL)) return;
	if ((style & SWT.OK) == SWT.OK) {
		OS.XtUnmanageChild (cancel);
		return;
	}
	if ((style & (SWT.YES | SWT.NO | SWT.CANCEL)) == (SWT.YES | SWT.NO | SWT.CANCEL)) {
		OS.XtManageChild (help);
		byte [] buffer1 = Converter.wcsToMbcs (null, "Yes", true);
		int xmString1 = OS.XmStringParseText (
			buffer1,
			0,
			OS.XmFONTLIST_DEFAULT_TAG, 
			OS.XmCHARSET_TEXT, 
			null,
			0,
			0);
		byte [] buffer2 = Converter.wcsToMbcs (null, "No", true);
		int xmString2 = OS.XmStringParseText (
			buffer2,
			0,
			OS.XmFONTLIST_DEFAULT_TAG, 
			OS.XmCHARSET_TEXT, 
			null,
			0,
			0);
		byte [] buffer3 = Converter.wcsToMbcs (null, "Cancel", true);
		int xmString3 = OS.XmStringParseText (
			buffer3,
			0,
			OS.XmFONTLIST_DEFAULT_TAG, 
			OS.XmCHARSET_TEXT, 
			null,
			0,
			0);
		int [] argList = {OS.XmNokLabelString, xmString1, OS.XmNcancelLabelString, xmString2, OS.XmNhelpLabelString, xmString3};
		OS.XtSetValues (dialogHandle, argList, argList.length / 2);
		OS.XmStringFree (xmString1);  OS.XmStringFree (xmString2);  OS.XmStringFree (xmString3);
		return;
	}
	if ((style & (SWT.YES | SWT.NO)) == (SWT.YES | SWT.NO)) {
		byte [] buffer1 = Converter.wcsToMbcs (null, "Yes", true);
		int xmString1 = OS.XmStringParseText (
			buffer1,
			0,
			OS.XmFONTLIST_DEFAULT_TAG, 
			OS.XmCHARSET_TEXT, 
			null,
			0,
			0);
		byte [] buffer2 = Converter.wcsToMbcs (null, "No", true);
		int xmString2 = OS.XmStringParseText (
			buffer2,
			0,
			OS.XmFONTLIST_DEFAULT_TAG, 
			OS.XmCHARSET_TEXT, 
			null,
			0,
			0);
		int [] argList = {OS.XmNokLabelString, xmString1, OS.XmNcancelLabelString, xmString2};
		OS.XtSetValues (dialogHandle, argList, argList.length / 2);
		OS.XmStringFree (xmString1);  OS.XmStringFree (xmString2);
		return;
	}
	if ((style & (SWT.RETRY | SWT.CANCEL)) == (SWT.RETRY | SWT.CANCEL)) {
		byte [] buffer1 = Converter.wcsToMbcs (null, "Retry", true);
		int xmString1 = OS.XmStringParseText (
			buffer1,
			0,
			OS.XmFONTLIST_DEFAULT_TAG, 
			OS.XmCHARSET_TEXT, 
			null,
			0,
			0);
		byte [] buffer2 = Converter.wcsToMbcs (null, "Cancel", true);
		int xmString2 = OS.XmStringParseText (
			buffer2,
			0,
			OS.XmFONTLIST_DEFAULT_TAG, 
			OS.XmCHARSET_TEXT, 
			null,
			0,
			0);
		int [] argList = {OS.XmNokLabelString, xmString1, OS.XmNcancelLabelString, xmString2};
		OS.XtSetValues (dialogHandle, argList, argList.length / 2);
		OS.XmStringFree (xmString1);  OS.XmStringFree (xmString2);
		return;		
	}
	if ((style & (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) == (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) {
		OS.XtManageChild (help);
		byte [] buffer1 = Converter.wcsToMbcs (null, "Abort", true);
		int xmString1 = OS.XmStringParseText (
			buffer1,
			0,
			OS.XmFONTLIST_DEFAULT_TAG, 
			OS.XmCHARSET_TEXT, 
			null,
			0,
			0);
		byte [] buffer2 = Converter.wcsToMbcs (null, "Retry", true);
		int xmString2 = OS.XmStringParseText (
			buffer2,
			0,
			OS.XmFONTLIST_DEFAULT_TAG, 
			OS.XmCHARSET_TEXT, 
			null,
			0,
			0);
		byte [] buffer3 = Converter.wcsToMbcs (null, "Ignore", true);
		int xmString3 = OS.XmStringParseText (
			buffer3,
			0,
			OS.XmFONTLIST_DEFAULT_TAG, 
			OS.XmCHARSET_TEXT, 
			null,
			0,
			0);
		int [] argList = {OS.XmNokLabelString, xmString1, OS.XmNcancelLabelString, xmString2, OS.XmNhelpLabelString, xmString3};
		OS.XtSetValues (dialogHandle, argList, argList.length / 2);
		OS.XmStringFree (xmString1);  OS.XmStringFree (xmString2); OS.XmStringFree (xmString3);
		return;		
	}
}
void setMessage (int dialogHandle) {
	String text = message;
	int label = OS.XmMessageBoxGetChild (dialogHandle, OS.XmDIALOG_MESSAGE_LABEL);
	if (label != 0) {
//		(fontList := OSWidget resourceAt: XmNfontList handle: widget) == 0 ifFalse: [
//			text := OSWidget wrapText: message font: fontList width: DisplayWidth * 3 // 5]].
	}
	byte [] buffer = Converter.wcsToMbcs (null, text, true);
	int [] parseTable = Display.getDefault ().parseTable;
	int xmStringPtr = OS.XmStringParseText (
		buffer,
		0,
		OS.XmFONTLIST_DEFAULT_TAG, 
		OS.XmCHARSET_TEXT, 
		parseTable,
		parseTable.length,
		0);
	int [] argList = {OS.XmNmessageString, xmStringPtr};
	OS.XtSetValues (dialogHandle, argList, argList.length / 2);
	OS.XmStringFree (xmStringPtr);
}
public void setMessage (String string) {
	message = string;
}
}
