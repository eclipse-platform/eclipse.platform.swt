package org.eclipse.swt.widgets;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;

abstract class GtkFileDialog extends Dialog {

	String answer;
	int handle;
	char separator = System.getProperty ("file.separator").charAt (0);

GtkFileDialog (Shell parent, int style) {
	super (parent, style);
}

public String open () {
	byte [] titleBytes = Converter.wcsToMbcs (null, title, true);
	handle = OS.gtk_file_selection_new (titleBytes);
	if (parent!=null) {
		OS.gtk_window_set_transient_for(handle, parent.topHandle());
	}
	answer = null;
	preset();
	int response = OS.gtk_dialog_run(handle);
	if (response == OS.GTK_RESPONSE_OK) {
		int lpFilename = OS.gtk_file_selection_get_filename (handle);
		int filenameLength = OS.strlen (lpFilename);
		byte [] filenameBytes = new byte [filenameLength];
		OS.memmove (filenameBytes, lpFilename, filenameLength);
		String osAnswer = new String( Converter.mbcsToWcs (null, filenameBytes) );
		interpretOsAnswer(osAnswer);
	}
	OS.gtk_widget_destroy(handle);
	return answer;
}

/*
 * Subclasses must implement this to set things like
 * the filter or the initial selection, just before opening
 * the dialog.
 */
abstract void preset();

/*
 * Subclasses must implement this to set the right state
 * of the dialog just after its loop returned.
 */
abstract void interpretOsAnswer(String osAnswer);

/*
 * This is just a convenience function to help share code between subclasses
 */
int calculateLastSeparatorIndex(String x) {
	int separatorIndex = x.indexOf (separator);
	int index = separatorIndex;
	while (index != -1) {
		separatorIndex = index;
		index = x.indexOf (separator, index + 1);
	}
	return separatorIndex;
}
}
