package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public class TrayIcon extends Widget {
	Image image;
	String toolTipText;
	boolean visible;

public TrayIcon (Display display) {
	checkSubclass ();
	if (display == null) display = Display.getCurrent ();
	if (display == null) display = Display.getDefault ();
	if (!display.isValidThread ()) {
		error (SWT.ERROR_THREAD_INVALID_ACCESS);
	}
	this.visible = true;
}

public void addSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
}

Image getImage () {
	checkWidget ();
	return image;
}

public String getToolTipText () {
	checkWidget ();
	return toolTipText;
}

public boolean getVisible () {
	checkWidget ();
	return visible;
}

public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
}

public void setImage (Image image) {
	checkWidget ();
	if (image != null && image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	this.image = image;
}

public void setToolTipText (String string) {
	checkWidget ();
	toolTipText = string;
}

public void setVisible (boolean visible) {
	checkWidget ();
	if (this.visible == visible) return;
	if (visible) sendEvent (SWT.Show);
	this.visible = visible;
	if (!visible) sendEvent (SWT.Hide);
}
}
