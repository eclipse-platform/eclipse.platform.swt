/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public class TrayItem extends Item {
	Tray parent;
	String toolTipText;
	boolean visible = true;
	
public TrayItem (Tray parent, int style) {
	super (parent, style);
	this.parent = parent;
	parent.createItem (this, parent.getItemCount ());
}

public void addSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
}

public String getToolTipText () {
	checkWidget ();
	return toolTipText;
}

public boolean getVisible () {
	checkWidget ();
	return visible;
}

void releaseChild () {
	super.releaseChild ();
	parent.destroyItem (this);
}

public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
}

public void setImage (Image image) {
	checkWidget ();
	if (image != null && image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	super.setImage (image);
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
