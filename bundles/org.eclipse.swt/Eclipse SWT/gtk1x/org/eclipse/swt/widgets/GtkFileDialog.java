package org.eclipse.swt.widgets;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;

abstract class GtkFileDialog extends Dialog {

	String answer;
	int handle, okButtonHandle, cancelButtonHandle;
	char separator = System.getProperty ("file.separator").charAt (0);

GtkFileDialog (Shell parent, int style) {
	super (parent, style);
}

/**
 * Actually create the GtkFileSelection dialog widget.
 * Set the correct title.
 * Get the pointers to the buttons.
 */
void createGtkDialog() {
	/* create */
	byte [] titleBytes = Converter.wcsToMbcs (null, title, true);
	handle = OS.gtk_file_selection_new (titleBytes);
	if (parent!=null) {
		OS.gtk_window_set_modal(handle, true);
		OS.gtk_window_set_transient_for(handle, parent.topHandle());
	}	
	/* buttons */
	GtkFileSelection dialog = new GtkFileSelection ();
	OS.memmove (dialog, handle, GtkFileSelection.sizeof);
	okButtonHandle = dialog.ok_button;
	cancelButtonHandle = dialog.cancel_button;
}

/**
 * Deals with the filter.
 */
void setUpFilter() {
	/*
	// Calculate the fully-specified file name and convert to bytes
	StringBuffer stringBuffer = new StringBuffer ();
	if (filterPath == null) {
		filterPath = "";
	} else {
		if (filterPath.length () > 0) {
			stringBuffer.append (filterPath);
			if (filterPath.charAt (filterPath.length () - 1) != separator) {
				stringBuffer.append (separator);
			}
		}
	}
	if (fileName == null) {
		fileName = "";
	} else {
		stringBuffer.append (fileName);
	}
	fullPath = stringBuffer.toString ();
	byte [] fullPathBytes = Converter.wcsToMbcs (null, fullPath, true);
	OS.gtk_file_selection_set_filename (handle, fullPathBytes);
	
	// Set the extension
	if (filterNames == null) filterNames = new String [0];
	if (filterExtensions == null) filterExtensions = new String [0];
	if (filterExtensions.length == 1) {
		String ext = filterExtensions [0];
		byte [] extBytes = Converter.wcsToMbcs (null, ext, true);
		OS.gtk_file_selection_complete (handle, extBytes);
	}
	*/
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
	createGtkDialog();
	setUpFilter();

	/* Hook callbacks */
	Callback destroyCallback = new Callback (this, "destroyFunc", 2);
	int destroyFunc = destroyCallback.getAddress ();
	byte [] destroy = Converter.wcsToMbcs (null, "destroy", true);
	OS.gtk_signal_connect (handle, destroy, destroyFunc, handle);
	byte [] clicked = Converter.wcsToMbcs (null, "clicked", true);
	Callback okCallback = new Callback (this, "okFunc", 2);
	int okFunc = okCallback.getAddress ();
	Callback cancelCallback = new Callback (this, "cancelFunc", 2);
	int cancelFunc = cancelCallback.getAddress ();
	OS.gtk_signal_connect (okButtonHandle, clicked, okFunc, handle);
	OS.gtk_signal_connect (cancelButtonHandle, clicked, cancelFunc, handle);

	/* Show the dialog */
	answer = null;
	OS.gtk_widget_show_now (handle);
	OS.gtk_main ();

	destroyCallback.dispose ();
	okCallback.dispose ();
	cancelCallback.dispose ();
	return answer;
}

abstract boolean getAnswer();

String getFileNameFromOS() {
	int lpFilename = OS.gtk_file_selection_get_filename (handle);
	int filenameLength = OS.strlen (lpFilename);
	byte [] filenameBytes = new byte [filenameLength];
	OS.memmove (filenameBytes, lpFilename, filenameLength);
	return new String (Converter.mbcsToWcs (null, filenameBytes));
}

int calculateLastSeparatorIndex(String x) {
	int separatorIndex = x.indexOf (separator);
	int index = separatorIndex;
	while (index != -1) {
		separatorIndex = index;
		index = x.indexOf (separator, index + 1);
	}
	return separatorIndex;
}


/*
 * The callback functions.
 */
int okFunc (int widget, int callData) {
	if (getAnswer()) OS.gtk_widget_destroy (callData);
	return 0;
}
int cancelFunc (int widget, int callData) {
	answer = null;
	OS.gtk_widget_destroy (callData);
	return 0;
}
int destroyFunc (int widget, int callData) {
	OS.gtk_main_quit ();
	return 0;
}

}

