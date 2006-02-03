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
	int focusIndex;
	int spacing;
	int yCurrentScroll;
	Listener listener;
	boolean inDispose;
	static final int HEADER_HEIGHT = 24;	
	
public ExpandBar (Composite parent, int style) {
	super (parent, checkStyle (style));
	items = new ExpandItem [4];	
	focusIndex = -1;
	
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
			}
		}
	};
	int [] events = new int [] { 
		SWT.Dispose, 
		SWT.MouseDown,
		SWT.MouseUp, 
		SWT.Paint,
		SWT.Resize,
		SWT.KeyDown,
		SWT.FocusIn,
		SWT.FocusOut,
	};
	for (int i = 0; i < events.length; i++) {
		addListener (events [i], listener);	
	}
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
				height += ExpandBar.HEADER_HEIGHT;
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
	if (itemCount == 1) focusIndex = 0;
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
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
	if (itemCount == 0) {
		focusIndex = -1;
	} else {
		if (focusIndex == index && focusIndex == itemCount) {
			focusIndex = index - 1;
			items [focusIndex].redraw ();
		}
	}
	item.redraw ();
	layoutItems (index, true);
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
			y += ExpandBar.HEADER_HEIGHT + spacing;
		}
		for (int i = index; i < itemCount; i++) {
			ExpandItem item = items [i];
			item.setBounds (spacing, y, 0, 0, true, false);
			if (item.expanded) y += item.height;
			y += ExpandBar.HEADER_HEIGHT + spacing;
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

void setScrollbar () {
	if (itemCount == 0) return;
	ScrollBar verticalBar = getVerticalBar ();
	if (verticalBar == null) return;
	int height = getClientArea ().height;
	ExpandItem item = items [itemCount - 1];
	int maxHeight = item.y + ExpandBar.HEADER_HEIGHT + spacing;
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
	this.spacing = spacing;
	int width = Math.max (0, getClientArea ().width - spacing * 2);
	for (int i = 0; i < itemCount; i++) {
		ExpandItem item = items[i];
		if (item.width != width) item.setBounds (0, 0, width, item.height, false, true);
	}
	layoutItems (0, true);
	redraw ();
}

void showItem (int index) {
	ExpandItem item = items [index];
	Control control = item.control;
	if (control != null && !control.isDisposed ()) {
		control.setVisible (item.expanded);
	}
	item.redraw ();
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
}

void onFocus () {
	if (focusIndex != -1) items [focusIndex].redraw ();
}

void onKeyDown (Event event) {
	if (focusIndex == -1) return;
	ExpandItem item = items [focusIndex];
	switch (event.keyCode) {
		case 13: /* Return */
		case 32: /* Space */
			item.expanded = !item.expanded;
			showItem (focusIndex);
			Event ev = new Event ();
			ev.item = item;
			sendEvent (item.expanded ? SWT.Expand :SWT.Collapse, ev);
			break;
		case SWT.ARROW_UP:
			if (focusIndex > 0) {
				item.redraw ();
				focusIndex--;
				items [focusIndex].redraw ();
			}
			break;
		case SWT.ARROW_DOWN:
			if (focusIndex < itemCount - 1) {
				item.redraw ();
				focusIndex++;
				items [focusIndex].redraw ();
			}
			break;
	}
}

void onMouseDown (Event event) {
	int x = event.x;
	int y = event.y;
	for (int i = 0; i < itemCount; i++) {
		ExpandItem item = items[i];
		boolean hover = item.x <= x && x < (item.x + item.width) && item.y <= y && y < (item.y + ExpandBar.HEADER_HEIGHT); 
		if (hover) {
			items [focusIndex].redraw ();
			focusIndex = i;
			items [focusIndex].redraw ();
			forceFocus ();
			break;
		}
	}		
}

void onMouseUp (Event event) {
	int x = event.x;
	int y = event.y;
	if (focusIndex == -1) return;
	ExpandItem item = items [focusIndex];
	boolean hover = item.x <= x && x < (item.x + item.width) && item.y <= y && y < (item.y + ExpandBar.HEADER_HEIGHT); 
	if (hover) {
		item.expanded = !item.expanded;
		showItem (focusIndex);
		Event ev = new Event ();
		ev.item = item;
		notifyListeners (item.expanded ? SWT.Expand : SWT.Collapse, ev);
	}
}

void onPaint (Event event) {
	boolean hasFocus = isFocusControl ();
	for (int i = 0; i < itemCount; i++) {
		ExpandItem item = items [i];
		item.drawItem (event.gc, hasFocus && i == focusIndex);
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

}
