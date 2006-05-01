/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

public class ExpandBar extends Composite {	
	ExpandItem [] items;
	int itemCount;
	ExpandItem focusItem;
	int spacing;
	int yCurrentScroll;
	Font font;
	Color foreground;
	Listener listener;
	boolean inDispose;
	
public ExpandBar (Composite parent, int style) {
	super (parent, checkStyle (style));
	items = new ExpandItem [4];	

	listener = new Listener () {
		public void handleEvent (Event event) {
			switch (event.type) {
				case SWT.Dispose:		onDispose (event);        	break;
				case SWT.MouseDown:		onMouseDown (event);   		break;
				case SWT.MouseUp:		onMouseUp (event);   		break;
				case SWT.Paint:			onPaint (event);     		break;
				case SWT.Resize:		onResize ();     			break;
				case SWT.KeyDown:		onKeyDown (event);    		break;
				case SWT.FocusIn:		onFocus ();     			break;
				case SWT.FocusOut:		onFocus ();     			break;
				case SWT.Traverse:		onTraverse (event);   		break;
			}
		}
	};
	addListener (SWT.Dispose, listener);
	addListener (SWT.MouseDown, listener);
	addListener (SWT.MouseUp, listener);
	addListener (SWT.Paint, listener);
	addListener (SWT.Resize, listener);
	addListener (SWT.KeyDown, listener);
	addListener (SWT.FocusIn, listener);
	addListener (SWT.FocusOut, listener);
	addListener (SWT.Traverse, listener);
	
	ScrollBar verticalBar = getVerticalBar ();
	if (verticalBar != null) {
		verticalBar.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event event) {
				onScroll (event);
			}
		});
	}
}

public void addExpandListener (ExpandListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Expand, typedListener);
	addListener (SWT.Collapse, typedListener);
}

static int checkStyle (int style) {
	return style & ~SWT.H_SCROLL;
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int height = 0, width = 0;
	if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
		if (itemCount > 0) {
			height += spacing;
			GC gc = new GC (this);
			for (int i = 0; i < itemCount; i++) {
				ExpandItem item = items [i];
				height += item.getHeaderHeight ();
				if (item.expanded) height += item.height;
				height += spacing;
				width = Math.max (width, item.getPreferredWidth (gc));			
			}
			gc.dispose ();
		}
	}
	if (width == 0) width = DEFAULT_WIDTH;
	if (height == 0) height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	Rectangle trim = computeTrim (0, 0, width, height);
	return new Point (trim.width, trim.height);	
}

void createItem (ExpandItem item, int style, int index) {
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	if (itemCount == items.length) {
		ExpandItem [] newItems = new ExpandItem [itemCount + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	System.arraycopy (items, index, items, index + 1, itemCount - index);
	items [index] = item;
	itemCount++;
	if (focusItem == null) focusItem = item;
	item.width = Math.max (0, getClientArea ().width - spacing * 2);
	layoutItems (index, true);
}

void destroyItem (ExpandItem item) {
	if (inDispose) return;
	int index = 0;
	while (index < itemCount) {
		if (items [index] == item) break;
		index++;
	}
	if (index == itemCount) return;
	if (item == focusItem) {
		int focusIndex = index > 0 ? index - 1 : 1;
		if (focusIndex < itemCount) {
			focusItem = items [focusIndex];
			focusItem.redraw ();
		} else {
			focusItem = null;
		}
	}
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
	item.redraw ();
	layoutItems (index, true);
}

int getBandHeight () {
	if (font == null) return ExpandItem.CHEVRON_SIZE;
	GC gc = new GC (this);
	FontMetrics metrics = gc.getFontMetrics ();
	gc.dispose ();
	return Math.max (ExpandItem.CHEVRON_SIZE, metrics.getHeight ());
}

public Color getForeground () {
	checkWidget ();
	if (foreground == null) {
		Display display = getDisplay ();
		return display.getSystemColor (SWT.COLOR_TITLE_FOREGROUND);
	}
	return foreground;
}

public ExpandItem getItem (int index) {
	checkWidget ();
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);	
	return items [index];
}

public int getItemCount () {
	checkWidget ();
	return itemCount;
}

public ExpandItem [] getItems () {
	checkWidget ();
	ExpandItem [] result = new ExpandItem [itemCount];
	System.arraycopy (items, 0, result, 0, itemCount);
	return result;
}

public int getSpacing () {
	checkWidget ();
	return spacing;
}

public int indexOf (ExpandItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i = 0; i < itemCount; i++) {
		if (items [i] == item) return i;
	}
	return -1;
}

void layoutItems (int index, boolean setScrollbar) {
	if (index < itemCount) {
		int y = spacing - yCurrentScroll;
		for (int i = 0; i < index; i++) {
			ExpandItem item = items [i];
			if (item.expanded) y += item.height;
			y += item.getHeaderHeight() + spacing;
		}
		for (int i = index; i < itemCount; i++) {
			ExpandItem item = items [i];
			item.setBounds (spacing, y, 0, 0, true, false);
			if (item.expanded) y += item.height;
			y += item.getHeaderHeight() + spacing;
		}
	}
	if (setScrollbar) setScrollbar ();
}

public void removeExpandListener (ExpandListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Expand, listener);
	eventTable.unhook (SWT.Collapse, listener);	
}

public void setFont(Font font) {
	super.setFont (font);
	this.font = font;
	layoutItems (0, true);
}

public void setForeground (Color color) {
	super.setForeground (color);
	foreground = color;
}

void setScrollbar () {
	if (itemCount == 0) return;
	ScrollBar verticalBar = getVerticalBar ();
	if (verticalBar == null) return;
	int height = getClientArea ().height;
	ExpandItem item = items [itemCount - 1];
	int maxHeight = item.y + getBandHeight () + spacing;
	if (item.expanded) maxHeight += item.height;

	//claim bottom free space
	if (yCurrentScroll > 0 && height > maxHeight) {
		yCurrentScroll = Math.max (0, yCurrentScroll + maxHeight - height);
		layoutItems (0, false);
	}
	maxHeight += yCurrentScroll;
	
	int selection = Math.min (yCurrentScroll, maxHeight);
	int increment = verticalBar.getIncrement ();
	int pageIncrement = verticalBar.getPageIncrement ();
	verticalBar.setValues(selection, 0, maxHeight, height, increment, pageIncrement);
	verticalBar.setVisible(maxHeight > height);
}

public void setSpacing (int spacing) {
	checkWidget ();
	if (spacing < 0) return;
	if (spacing == this.spacing) return;
	this.spacing = spacing;
	int width = Math.max (0, getClientArea ().width - spacing * 2);
	for (int i = 0; i < itemCount; i++) {
		ExpandItem item = items[i];
		if (item.width != width) item.setBounds (0, 0, width, item.height, false, true);
	}
	layoutItems (0, true);
	redraw ();
}

void showItem (ExpandItem item) {
	Control control = item.control;
	if (control != null && !control.isDisposed ()) {
		control.setVisible (item.expanded);
	}
	item.redraw ();
	int index = indexOf (item);
	layoutItems (index + 1, true);
}

void onDispose (Event event) {
	removeListener (SWT.Dispose, listener);
	notifyListeners (SWT.Dispose, event);
	event.type = SWT.None;
	/*
	 * Usually when an item is disposed, destroyItem will change the size of the items array, 
	 * reset the bounds of all the tabs and manage the widget associated with the tab.
	 * Since the whole folder is being disposed, this is not necessary.  For speed
	 * the inDispose flag is used to skip over this part of the item dispose.
	 */
	inDispose = true;
	
	for (int i = 0; i < itemCount; i++) {				
		items [i].dispose ();
	}
	items = null;
	font = null;
	foreground = null;
	focusItem = null;
}

void onFocus () {
	if (focusItem != null) focusItem.redraw ();
}

void onKeyDown (Event event) {
	if (focusItem == null) return;
	switch (event.keyCode) {
		case 13: /* Return */
		case 32: /* Space */
			Event ev = new Event ();
			ev.item = focusItem;
			sendEvent (focusItem.expanded ? SWT.Collapse :SWT.Expand, ev);
			focusItem.expanded = !focusItem.expanded;
			showItem (focusItem);
			break;
		case SWT.ARROW_UP: {
			int focusIndex = indexOf (focusItem);
			if (focusIndex > 0) {
				focusItem.redraw ();
				focusItem = items [focusIndex - 1];
				focusItem.redraw ();
			}
			break;
		}
		case SWT.ARROW_DOWN: {
			int focusIndex = indexOf (focusItem);
			if (focusIndex < itemCount - 1) {
				focusItem.redraw ();
				focusItem = items [focusIndex + 1];
				focusItem.redraw ();
			}
			break;
		}
	}
}

void onMouseDown (Event event) {
	int x = event.x;
	int y = event.y;
	for (int i = 0; i < itemCount; i++) {
		ExpandItem item = items[i];
		boolean hover = item.x <= x && x < (item.x + item.width) && item.y <= y && y < (item.y + getBandHeight ()); 
		if (hover && item != focusItem) {
			focusItem.redraw ();
			focusItem = item;
			focusItem.redraw ();
			forceFocus ();
			break;
		}
	}		
}

void onMouseUp (Event event) {
	int x = event.x;
	int y = event.y;
	if (focusItem == null) return;
	boolean hover = focusItem.x <= x && x < (focusItem.x + focusItem.width) && focusItem.y <= y && y < (focusItem.y + getBandHeight ()); 
	if (hover) {
		Event ev = new Event ();
		ev.item = focusItem;
		notifyListeners (focusItem.expanded ? SWT.Collapse : SWT.Expand, ev);
		focusItem.expanded = !focusItem.expanded;
		showItem (focusItem);
	}
}

void onPaint (Event event) {
	boolean hasFocus = isFocusControl ();
	for (int i = 0; i < itemCount; i++) {
		ExpandItem item = items [i];
		item.drawItem (event.gc, hasFocus && item == focusItem);
	}
}

void onResize () {
	Rectangle rect = getClientArea ();	
	int width = Math.max (0, rect.width - spacing * 2);
	for (int i = 0; i < itemCount; i++) {
		ExpandItem item = items[i];
		item.setBounds (0, 0, width, item.height, false, true);
	}
	setScrollbar ();
}

void onScroll (Event event) {
	ScrollBar verticalBar = getVerticalBar ();
	if (verticalBar != null) {
		yCurrentScroll = verticalBar.getSelection();
		layoutItems (0, false);
	}
}

void onTraverse (Event event) {
	switch (event.detail) {
		case SWT.TRAVERSE_TAB_NEXT:
		case SWT.TRAVERSE_TAB_PREVIOUS:
			event.doit = true;
			break;
	}
}

}
