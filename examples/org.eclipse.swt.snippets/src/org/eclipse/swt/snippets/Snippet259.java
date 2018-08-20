/*******************************************************************************
 * Copyright (c) 2007, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Detect drag in a custom control
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.3
 */
import static org.eclipse.swt.events.MouseListener.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet259 {

static class MyList extends Canvas {
	int selection;
	String [] items = new String [0];
	static final int INSET_X = 2;
	static final int INSET_Y = 2;

static int checkStyle (int style) {
	style &= ~(SWT.H_SCROLL | SWT.V_SCROLL);
	style |= SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE;
	return style;
}

public MyList (Composite parent, int style) {
	super (parent, checkStyle (style));
	super.setDragDetect (false);
	addMouseListener(mouseDownAdapter(event -> {
		GC gc = new GC(MyList.this);
		Rectangle client = getClientArea();
		int index = 0, x = client.x + INSET_X, y = client.y + INSET_Y;
		while (index < items.length) {
			Point pt = gc.stringExtent(items[index]);
			Rectangle item = new Rectangle(x, y, pt.x, pt.y);
			if (item.contains(event.x, event.y))
				break;
			y += pt.y;
			if (!client.contains(x, y))
				return;
			index++;
		}
		gc.dispose();
		if (index == items.length || !client.contains(x, y)) {
			return;
		}
		selection = index;
		redraw();
		update();
		dragDetect(event);
	}));
	addPaintListener (event -> {
		GC gc = event.gc;
		Color foreground = event.display.getSystemColor (SWT.COLOR_LIST_FOREGROUND);
		Color background = event.display.getSystemColor (SWT.COLOR_LIST_BACKGROUND);
		Color selectForeground = event.display.getSystemColor (SWT.COLOR_LIST_SELECTION_TEXT);
		Color selectBackground = event.display.getSystemColor (SWT.COLOR_LIST_SELECTION);
		gc.setForeground (foreground);
		gc.setBackground (background);
		MyList.this.drawBackground (gc, event.x, event.y, event.width, event.height);
		int x = INSET_X, y = INSET_Y;
		for (int i=0; i<items.length; i++) {
			Point pt = gc.stringExtent(items [i]);
			gc.setForeground (i == selection ? selectForeground : foreground);
			gc.setBackground (i == selection ? selectBackground : background);
			gc.drawString (items [i], x, y);
			y += pt.y;
		}

	});
}

@Override
public Point computeSize (int wHint, int hHint, boolean changed) {
	GC gc = new GC (this);
	int index = 0, width = 0, height = 0;
	while (index < items.length) {
		Point pt = gc.stringExtent(items [index]);
		width = Math.max (width, pt.x);
		height += pt.y;
		index++;
	}
	gc.dispose ();
	width += INSET_X * 2;
	height += INSET_Y * 2;
	Rectangle rect = computeTrim (0, 0, width, height);
	return new Point (rect.width, rect.height);
}

public String [] getItems () {
	checkWidget ();
	return items;
}

public void setItems (String [] items) {
	checkWidget ();
	if (items == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	this.items = items;
	redraw ();
}
}

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout (new FillLayout ());
	MyList t1 = new MyList (shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	t1.setItems (new String [] {"Some", "items", "in", "the", "list"});
	t1.addDragDetectListener (event -> System.out.println ("Drag started ..."));
	MyList t2 = new MyList (shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	t2.setItems (new String [] {"Some", "items", "in", "another", "list"});
	t2.addDragDetectListener (event -> System.out.println ("Drag started ..."));
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}