package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.Rect;
 
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
	checkWidget ();
	if (index != 0 && !(0 <= index && index < parent.columnCount)) return new Rectangle (0, 0, 0, 0);;
	Rect rect = new Rect();
	int itemIndex = parent.indexOf (this);
	int id = itemIndex + 1;
	int columnId = index == 0 ? Table.COLUMN_ID : parent.columns [index].id;
	if (OS.GetDataBrowserItemPartBounds (parent.handle, id, columnId, OS.kDataBrowserPropertyEnclosingPart, rect) != OS.noErr) {
		return new Rectangle (0, 0, 0, 0);
	}
	int x = rect.left, y = rect.top;
	int width = rect.right - rect.left;
	int height = rect.bottom - rect.top;
	OS.GetControlBounds (parent.handle, rect);
	x -= rect.left;
	y -= rect.top;
	return new Rectangle (x, y, width, height);
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
	if (index != 0 && !(0 <= index && index < parent.columnCount)) return new Rectangle (0, 0, 0, 0);
	Rect rect = new Rect();
	int itemIndex = parent.indexOf (this);
	int id = itemIndex + 1;
	int columnId = index == 0 ? Table.COLUMN_ID : parent.columns [index].id;
	if (OS.GetDataBrowserItemPartBounds (parent.handle, id, columnId, OS.kDataBrowserPropertyContentPart, rect) != OS.noErr) {
		return new Rectangle (0, 0, 0, 0);
	}
	int x = rect.left, y = rect.top;
	int width = 0;
	if (image != null) {
		Rectangle bounds = image.getBounds ();
		width += bounds.width + 2;
	}
	int height = rect.bottom - rect.top;
	OS.GetControlBounds (parent.handle, rect);
	x -= rect.left;
	y -= rect.top;
	System.out.println(new Rectangle (x, y, width, height));
	return new Rectangle (x, y, width, height);
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
	if (strings != null) {
		if (0 <= index && index < strings.length) {
			String string = strings [index];
			return string != null ? string : "";
		}
	}
	return "";
}

void redraw () {
	int itemIndex = parent.indexOf (this);
	int [] id = new int [] {itemIndex + 1};
	OS.UpdateDataBrowserItems (parent.handle, 0, id.length, id, OS.kDataBrowserItemNoProperty, OS.kDataBrowserNoItem);
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
	redraw ();
}

public void setChecked (boolean checked) {
	checkWidget ();
	if ((parent.style & SWT.CHECK) == 0) return;
	this.checked = checked;
	redraw ();
}

public void setForeground (Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	foreground = color;
	redraw ();
}

public void setGrayed (boolean grayed) {
	checkWidget ();
	if ((parent.style & SWT.CHECK) == 0) return;
	this.grayed = grayed;
	redraw ();
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
	int columnCount = parent.columnCount;
	if (0 <= index && index < columnCount) {
		if (images == null) images = new Image [columnCount];
		if (images.length != columnCount) {
			Image [] newImages = new Image [columnCount];
			System.arraycopy (images, 0, newImages, 0, images.length);
			images = newImages;
		}
		if (0 <= index && index < images.length) images [index] = image;
	}	
	int [] id = new int [] {itemIndex + 1};
	OS.UpdateDataBrowserItems (parent.handle, 0, id.length, id, OS.kDataBrowserItemNoProperty, OS.kDataBrowserNoItem);
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
		if (strings.length != columnCount) {
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
