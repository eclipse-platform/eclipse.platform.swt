package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 * Instances of this class are used used to inform or warn the user.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>ICON_ERROR, ICON_INFORMATION, ICON_QUESTION, ICON_WARNING, ICON_WORKING</dd>
 * <dd>OK, OK | CANCEL</dd>
 * <dd>YES | NO, YES | NO | CANCEL</dd>
 * <dd>RETRY | CANCEL</dd>
 * <dd>ABORT | RETRY | IGNORE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public  class MessageBox extends Dialog {
	String message = "";
	
public MessageBox (Shell parent) {
	this (parent, SWT.OK | SWT.ICON_INFORMATION | SWT.APPLICATION_MODAL);
}

public MessageBox (Shell parent, int style) {
	super (parent, checkStyle (style));
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

public String getMessage () {
	return message;
}

public int open () {

	/* Compute the MessageBox style */
	int buttonBits = 0;
	if ((style & SWT.OK) == SWT.OK) buttonBits = OS.MB_OK;
	if ((style & (SWT.OK | SWT.CANCEL)) == (SWT.OK | SWT.CANCEL)) buttonBits = OS.MB_OKCANCEL;
	if ((style & (SWT.YES | SWT.NO)) == (SWT.YES | SWT.NO)) buttonBits = OS.MB_YESNO;
	if ((style & (SWT.YES | SWT.NO | SWT.CANCEL)) == (SWT.YES | SWT.NO | SWT.CANCEL)) buttonBits = OS.MB_YESNOCANCEL;
	if ((style & (SWT.RETRY | SWT.CANCEL)) == (SWT.RETRY | SWT.CANCEL)) buttonBits = OS.MB_RETRYCANCEL;
	if ((style & (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) == (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) buttonBits = OS.MB_ABORTRETRYIGNORE;
	if (buttonBits == 0) buttonBits = OS.MB_OK;

	int iconBits = 0;
	if ((style & SWT.ICON_ERROR) != 0) iconBits = OS.MB_ICONERROR;
	if ((style & SWT.ICON_INFORMATION) != 0) iconBits = OS.MB_ICONINFORMATION;
	if ((style & SWT.ICON_QUESTION) != 0) iconBits = OS.MB_ICONQUESTION;
	if ((style & SWT.ICON_WARNING) != 0) iconBits = OS.MB_ICONWARNING;
	if ((style & SWT.ICON_WORKING) != 0) iconBits = OS.MB_ICONINFORMATION;

	int modalBits = 0;
	if ((style & SWT.PRIMARY_MODAL) != 0) modalBits = OS.MB_APPLMODAL;
	if ((style & SWT.APPLICATION_MODAL) != 0) modalBits = OS.MB_TASKMODAL;
	if ((style & SWT.SYSTEM_MODAL) != 0) modalBits = OS.MB_SYSTEMMODAL;

	int bits = buttonBits | iconBits | modalBits;
	
	/*
	* Feature in Windows.  System modal is not supported
	* on Windows 95 and NT.  The fix is to convert system
	* modal to task modal.
	*/
	if ((bits & OS.MB_SYSTEMMODAL) != 0) {
		bits |= OS.MB_TASKMODAL;
		bits &= ~OS.MB_SYSTEMMODAL;
	}

	/*
	* Bug in Windows.  In order for MB_TASKMODAL to work,
	* the parent HWND of the MessageBox () call must be NULL.
	* The fix is to force the parent to be NULL when this
	* style is set.
	*/
	int hwndOwner = 0;
	if (parent != null && (bits & OS.MB_TASKMODAL) == 0) {
		hwndOwner = parent.handle;
	}

	/*
	* Feature in Windows.  The focus window is not saved and
	* and restored automatically by the call to MessageBox().
	* The fix is to save and restore the focus window.
	*/
	int hwndFocus = OS.GetFocus ();

	/* Open the message box */
	/* Use the character encoding for the default locale */
	byte [] buffer1 = Converter.wcsToMbcs (0, message, true);
	byte [] buffer2 = Converter.wcsToMbcs (0, title, true);
	int code = OS.MessageBox (hwndOwner, buffer1, buffer2, bits);
	
	/* Restore focus */
	OS.SetFocus (hwndFocus);
	
	/*
	* This code is intentionally commented.  On some
	* platforms, the owner window is repainted right
	* away when a dialog window exits.  This behavior
	* is currently unspecified.
	*/
//	if (hwndOwner != 0) OS.UpdateWindow (hwndOwner);
	
	/* Compute and return the result */
	if (code != 0) {
		int type = bits & 0x0F;
		if (type == OS.MB_OK) return SWT.OK;
		if (type == OS.MB_OKCANCEL) {
			return (code == OS.IDOK) ? SWT.OK : SWT.CANCEL;
		}
		if (type == OS.MB_YESNO) {
			return (code == OS.IDYES) ? SWT.YES : SWT.NO;
		}
		if (type == OS.MB_YESNOCANCEL) {
			if (code == OS.IDYES) return SWT.YES;
			if (code == OS.IDNO) return SWT.NO;
			return SWT.CANCEL;
		}
		if (type == OS.MB_RETRYCANCEL) {
			return (code == OS.IDRETRY) ? SWT.RETRY : SWT.CANCEL;
		}
		if (type == OS.MB_ABORTRETRYIGNORE) {
			if (code == OS.IDRETRY) return SWT.RETRY;
			if (code == OS.IDABORT) return SWT.ABORT;
			return SWT.IGNORE;
		}
	}
	return SWT.CANCEL;
}

public void setMessage (String string) {
	message = string;
}

}
