package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.widgets.Item;
 
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class TableItem extends Item {
	Table parent;
	String [] strings;
	Image [] images;
	boolean checked, grayed;
	Color foreground, background;
	
public TableItem (Table parent, int style) {
	super (parent, style);
	this.parent = parent;
	parent.createItem (this, parent.getItemCount ());
}

public TableItem (Table parent, int style, int index) {
	super (parent, style);
	this.parent = parent;
	parent.createItem (this, index);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

public Color getBackground () {
	checkWidget ();
	return background != null ? background : parent.getBackground ();
}

public Rectangle getBounds (int index) {
	checkWidget();
	//NOT DONE
	return new Rectangle (0, 0, 0, 0);
}

public boolean getChecked () {
	checkWidget();
	if ((parent.style & SWT.CHECK) == 0) return false;
	return checked;
}

public Display getDisplay () {
	Table parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

public Color getForeground () {
	checkWidget ();
	return foreground != null ? foreground : parent.getForeground ();
}

public boolean getGrayed () {
	checkWidget ();
	if ((parent.style & SWT.CHECK) == 0) return false;
	return grayed;
}

public Image getImage (int index) {
	checkWidget();
	if (index == 0) return super.getImage ();
	if (images != null) {
		if (0 <= index && index < images.length) return images [index];
	}
	return null;
}

public Rectangle getImageBounds (int index) {
	checkWidget();
	//NOT DONE
	return new Rectangle (0, 0, 0, 0);
}

public int getImageIndent () {
	checkWidget();
	return 0;
}

public Table getParent () {
	checkWidget();
	return parent;
}

public String getText (int index) {
	checkWidget();
	if (index == 0) return super.getText ();
	int itemIndex = parent.indexOf (this);
	if (itemIndex == -1) error (SWT.ERROR_CANNOT_GET_TEXT);
	if (strings != null) {
		if (0 <= index && index < strings.length) {
			String string = strings [index];
			return string != null ? string : "";
		}
	}
	return "";
}

void releaseChild () {
	super.releaseChild ();
	parent.destroyItem (this);
}

void releaseWidget () {
	super.releaseWidget ();
	background = foreground = null;
	parent = null;
}

public void setBackground (Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	background = color;
//	redraw ();
}

public void setChecked (boolean checked) {
	checkWidget ();
	if ((parent.style & SWT.CHECK) == 0) return;
	int itemIndex = parent.indexOf (this);
	if (itemIndex == -1) return;
	this.checked = checked;
	int [] id = new int [] {itemIndex + 1};
	OS.UpdateDataBrowserItems (parent.handle, 0, id.length, id, OS.kDataBrowserItemNoProperty, OS.kDataBrowserNoItem);
}

public void setForeground (Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	foreground = color;
//	redraw ();
}

public void setGrayed (boolean grayed) {
	checkWidget ();
	if ((parent.style & SWT.CHECK) == 0) return;
	this.grayed = grayed;
//	redraw ();
}

public void setImage (Image [] images) {
	checkWidget();
	if (images == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<images.length; i++) {
		setImage (i, images [i]);
	}
}

public void setImage (int index, Image image) {
	checkWidget();
	if (image != null && image.isDisposed ()) {
		error(SWT.ERROR_INVALID_ARGUMENT);
	}
	int itemIndex = parent.indexOf (this);
	if (itemIndex == -1) return;
	if (index == 0) {
		super.setImage (image);
	}
	//NOT DONE
}

public void setImage (Image image) {
	checkWidget ();
	setImage (0, image);
}

public void setImageIndent (int indent) {
	checkWidget();
	if (indent < 0) return;
}

public void setText (String [] strings) {
	checkWidget();
	if (strings == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<strings.length; i++) {
		String string = strings [i];
		if (string != null) setText (i, string);
	}
}

public void setText (int index, String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int itemIndex = parent.indexOf (this);
	if (itemIndex == -1) return;
	if (index == 0) {
		super.setText (string);
	}
	int columnCount = parent.columnCount;
	if (0 <= index && index < columnCount) {
		if (strings == null) strings = new String [columnCount];
		if (strings.length < columnCount) {
			String [] newStrings = new String [columnCount];
			System.arraycopy (strings, 0, newStrings, 0, strings.length);
			strings = newStrings;
		}
		if (0 <= index && index < strings.length) strings [index] = string;
	}
	int [] id = new int [] {itemIndex + 1};
	OS.UpdateDataBrowserItems (parent.handle, 0, id.length, id, OS.kDataBrowserItemNoProperty, OS.kDataBrowserNoItem);
}

public void setText (String string) {
	checkWidget();
	setText (0, string);
}

}
