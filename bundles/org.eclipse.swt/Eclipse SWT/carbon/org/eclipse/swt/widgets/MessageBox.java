package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.carbon.*;

public  class MessageBox extends Dialog {
	String message = "";
	

public MessageBox (Shell parent) {
	this (parent, SWT.OK | SWT.ICON_INFORMATION | SWT.APPLICATION_MODAL);
}

public MessageBox (Shell parent, int style) {
	super (parent, checkStyle (style));
	checkSubclass ();
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

int getCFString (String id) {
	String string = SWT.getMessage(id);
	char [] buffer = new char [string.length ()];
	string.getChars (0, buffer.length, buffer, 0);
	return OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
}

public int open () {
	short alertType = OS.kAlertPlainAlert;
	if ((style & SWT.ICON_ERROR) != 0) alertType = OS.kAlertStopAlert;
	if ((style & SWT.ICON_INFORMATION) != 0) alertType = OS.kAlertNoteAlert;
	if ((style & SWT.ICON_QUESTION) != 0) alertType = OS.kAlertNoteAlert;
	if ((style & SWT.ICON_WARNING) != 0) alertType = OS.kAlertCautionAlert;
	if ((style & SWT.ICON_WORKING) != 0) alertType = OS.kAlertNoteAlert;
	
	int error = 0;
	int explanation = 0;
	String errorString = (title != "") ? title : ((message != "") ? message : null);
	String explanationString = (title == "") ? null : ((message != "") ? message : null);
	if (errorString != null) {
		char [] buffer = new char [errorString.length ()];
		errorString.getChars (0, buffer.length, buffer, 0);
		error = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	} 
	if (explanationString != null) {
		char [] buffer = new char [explanationString.length ()];
		explanationString.getChars (0, buffer.length, buffer, 0);
		explanation = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);		
	}
	
	AlertStdCFStringAlertParamRec param = new AlertStdCFStringAlertParamRec ();
	param.version = OS.kStdCFStringAlertVersionOne;
	param.position = (short)OS.kWindowAlertPositionParentWindowScreen;
	int defaultStr = 0, cancelStr = 0, otherStr = 0;
	int mask = (SWT.YES | SWT.NO | SWT.OK | SWT.CANCEL | SWT.ABORT | SWT.RETRY | SWT.IGNORE);
	int bits = style & mask;
	switch (bits) {
		case SWT.OK:
			param.defaultButton = (short)OS.kAlertStdAlertOKButton;
			param.defaultText = OS.kAlertDefaultOKText;
			break;
		case SWT.CANCEL:
			param.defaultButton = (short)OS.kAlertStdAlertOKButton;
			param.defaultText = defaultStr = getCFString ("SWT_Cancel");
			break;
		case SWT.OK | SWT.CANCEL:
			param.defaultButton = (short)OS.kAlertStdAlertOKButton;
			param.defaultText = OS.kAlertDefaultOKText;
			param.cancelButton = (short)OS.kAlertStdAlertCancelButton;
			param.cancelText = OS.kAlertDefaultCancelText;
			break;
		case SWT.YES:
			param.defaultButton = (short)OS.kAlertStdAlertOKButton;
			param.defaultText = defaultStr = getCFString ("SWT_Yes");
			break;
		case SWT.NO:
			param.cancelButton = (short)OS.kAlertStdAlertOKButton;
			param.cancelText = defaultStr = getCFString ("SWT_No");
			break;
		case SWT.YES | SWT.NO:
			param.defaultButton = (short)OS.kAlertStdAlertOKButton;
			param.defaultText = defaultStr = getCFString ("SWT_Yes");
			param.cancelButton = (short)OS.kAlertStdAlertCancelButton;
			param.cancelText = cancelStr = getCFString ("SWT_No");
			break;
		case SWT.YES | SWT.NO | SWT.CANCEL:				
			param.defaultButton = (short)OS.kAlertStdAlertOKButton;
			param.defaultText = defaultStr = getCFString ("SWT_Yes");
			param.otherText = cancelStr = getCFString ("SWT_No");
			param.cancelButton = (short)OS.kAlertStdAlertCancelButton;
			param.cancelText = OS.kAlertDefaultCancelText;
			break;
		case SWT.RETRY | SWT.CANCEL:
			param.defaultButton = (short)OS.kAlertStdAlertOKButton;
			param.defaultText = defaultStr = getCFString ("SWT_Retry");
			param.cancelButton = (short)OS.kAlertStdAlertCancelButton;
			param.cancelText = OS.kAlertDefaultCancelText;
			break;
		case SWT.ABORT | SWT.RETRY | SWT.IGNORE:
			param.defaultButton = (short)OS.kAlertStdAlertOKButton;
			param.defaultText = defaultStr = getCFString ("SWT_Abort");
			param.otherText = cancelStr = getCFString ("SWT_Retry");
			param.cancelButton = (short)OS.kAlertStdAlertCancelButton;
			param.cancelText = otherStr = getCFString ("SWT_Ignore");
			break;
	}
	
	int [] dialogRef= new int [1];
	int result = OS.CreateStandardAlert (alertType, error, explanation, param, dialogRef);
	if (error != 0) OS.CFRelease(error);
	if (explanation != 0) OS.CFRelease(explanation);
	if (defaultStr != 0) OS.CFRelease(defaultStr);
	if (cancelStr != 0) OS.CFRelease(cancelStr);
	if (otherStr != 0) OS.CFRelease(otherStr);
	
	if (dialogRef[0] != 0) {
		short [] outItemHit = new short [1];
		OS.RunStandardAlert(dialogRef[0], 0, outItemHit);
		if (outItemHit [0] != 0) {
			switch (bits) {
				case SWT.OK:
					return SWT.OK;
				case SWT.CANCEL:
					return SWT.CANCEL;
				case SWT.OK | SWT.CANCEL:
					if (outItemHit [0] == OS.kAlertStdAlertOKButton) return SWT.OK;
					return SWT.CANCEL;
				case SWT.YES:
					return SWT.YES;
				case SWT.NO:
					return SWT.NO;
				case SWT.YES | SWT.NO:
					if (outItemHit [0] == OS.kAlertStdAlertOKButton) return SWT.YES;
					return SWT.NO;
				case SWT.YES | SWT.NO | SWT.CANCEL:
					if (outItemHit [0] == OS.kAlertStdAlertOKButton) return SWT.YES;
					if (outItemHit [0] == OS.kAlertStdAlertOtherButton) return SWT.NO;
					return SWT.CANCEL;
				case SWT.RETRY | SWT.CANCEL:
					if (outItemHit [0] == OS.kAlertStdAlertOKButton) return SWT.RETRY;
					return SWT.CANCEL;
				case SWT.ABORT | SWT.RETRY | SWT.IGNORE:
					if (outItemHit [0] == OS.kAlertStdAlertOKButton) return SWT.ABORT;
					if (outItemHit [0] == OS.kAlertStdAlertOtherButton) return SWT.RETRY;
					return SWT.IGNORE;
			}
		}
	}
	return SWT.CANCEL;
}

public void setMessage (String string) {
	message = string;
}

}
