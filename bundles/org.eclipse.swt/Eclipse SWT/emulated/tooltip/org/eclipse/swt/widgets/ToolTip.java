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

public class ToolTip extends Widget {
	Shell parent, tip;
	int x, y;
	int [] borderPolygon;
	boolean spikeAbove, autohide;
	Listener listener;
	TextLayout layoutText, layoutMessage;
	Region region;
	Font boldFont;
	Runnable runnable;
	
	static final int BORDER = 5;
	static final int PADDING = 5;
	static final int INSET = 4;
	static final int TIP_HEIGHT = 20;
	static final int IMAGE_SIZE = 16;
	static final int DELAY = 10000;

public ToolTip (Shell parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	this.autohide = true;
	x = y = -1;	
	Display display = getDisplay ();
	tip = new Shell (parent, SWT.ON_TOP | SWT.NO_TRIM);
	Color background = display.getSystemColor (SWT.COLOR_INFO_BACKGROUND);
	tip.setBackground (background);
	listener = new Listener () {
		public void handleEvent (Event event) {
			switch (event.type) {
				case SWT.Dispose: onDispose (event); break;
				case SWT.Paint: onPaint (event); break;
				case SWT.MouseDown: onMouseDown (event); break;
			}
		}
	};
	addListener (SWT.Dispose, listener);
	tip.addListener (SWT.Paint, listener);
	tip.addListener (SWT.MouseDown, listener);
}

static int checkStyle (int style) {
	int mask = SWT.ICON_ERROR | SWT.ICON_INFORMATION | SWT.ICON_WARNING;
	if ((style & mask) == 0) return style;
	return checkBits (style, SWT.ICON_INFORMATION, SWT.ICON_WARNING, SWT.ICON_ERROR, 0, 0, 0);
}

public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

void configure () {
	Display display = parent.getDisplay ();
	if (x == -1 || y == -1) {
		Point point = display.getCursorLocation ();
		x = point.x;
		y = point.y;
	}
	Monitor monitor = parent.getMonitor ();
	Rectangle dest = monitor.getBounds ();
	Point size = getSize (dest.width / 4);
	int w = size.x;
	int h = size.y;
	int t = (style & SWT.BALLOON) != 0 ? TIP_HEIGHT : 0;
	int i = (style & SWT.BALLOON) != 0 ? 16 : 0;
	tip.setSize (w, h + t);
	int [] polyline;
	spikeAbove = dest.height >= y + size.y + t;
	if (dest.width >= x + size.x) {
		if (dest.height >= y + size.y + t) {
			polyline = new int [] {
				0, 5+t, 1, 5+t, 1, 3+t, 3, 1+t, 5, 1+t, 5, t, 
				16, t, 16, 0, 35, t,
				w-5, t, w-5, 1+t, w-3, 1+t, w-1, 3+t, w-1, 5+t, w, 5+t,
				w, h-5+t, w-1, h-5+t, w-1, h-3+t, w-2, h-3+t, w-2, h-2+t, w-3, h-2+t, w-3, h-1+t, w-5, h-1+t, w-5, h+t,
				5, h+t, 5, h-1+t, 3, h-1+t, 3, h-2+t, 2, h-2+t, 2, h-3+t, 1, h-3+t, 1, h-5+t, 0, h-5+t, 
				0, 5+t};
			borderPolygon = new int[] {
					0, 5+t, 1, 4+t, 1, 3+t, 3, 1+t,  4, 1+t, 5, t, 
					16, t, 16, 1, 35, t,
					w-6, 0+t, w-5, 1+t, w-4, 1+t, w-2, 3+t, w-2, 4+t, w-1, 5+t,
					w-1, h-6+t, w-2, h-5+t, w-2, h-4+t, w-4, h-2+t, w-5, h-2+t, w-6, h-1+t,
					5, h-1+t, 4, h-2+t, 3, h-2+t, 1, h-4+t, 1, h-5+t, 0, h-6+t, 
					0, 5+t};
			tip.setLocation (Math.max (0, x - i), y);
		} else {
			polyline = new int [] {
				0, 5, 1, 5, 1, 3, 3, 1, 5, 1, 5, 0, 
				w-5, 0, w-5, 1, w-3, 1, w-1, 3, w-1, 5, w, 5,
				w, h-5, w-1, h-5, w-1, h-3, w-2, h-3, w-2, h-2, w-3, h-2, w-3, h-1, w-5, h-1, w-5, h,
				35, h, 16, h+t, 16, h,
				5, h, 5, h-1, 3, h-1, 3, h-2, 2, h-2, 2, h-3, 1, h-3, 1, h-5, 0, h-5, 
				0, 5};
			borderPolygon = new int[] {
					0, 5, 1, 4, 1, 3, 3, 1,  4, 1, 5, 0, 
					w-6, 0, w-5, 1, w-4, 1, w-2, 3, w-2, 4, w-1, 5,
					w-1, h-6, w-2, h-5, w-2, h-4, w-4, h-2, w-5, h-2, w-6, h-1,
					36, h-1, 16, h+t-1, 16, h-1,
					5, h-1, 4, h-2, 3, h-2, 1, h-4, 1, h-5, 0, h-6, 
					0, 5};
			tip.setLocation (Math.max (0, x - i), y - size.y - t);
		}
	} else {
		if (dest.height >= y + size.y + t) {
			polyline = new int [] {
				0, 5+t, 1, 5+t, 1, 3+t, 3, 1+t, 5, 1+t, 5, t, 
				w-35, t, w-16, 0, w-16, t,
				w-5, t, w-5, 1+t, w-3, 1+t, w-1, 3+t, w-1, 5+t, w, 5+t,
				w, h-5+t, w-1, h-5+t, w-1, h-3+t, w-2, h-3+t, w-2, h-2+t, w-3, h-2+t, w-3, h-1+t, w-5, h-1+t, w-5, h+t,
				5, h+t, 5, h-1+t, 3, h-1+t, 3, h-2+t, 2, h-2+t, 2, h-3+t, 1, h-3+t, 1, h-5+t, 0, h-5+t, 
				0, 5+t};
			borderPolygon = new int[] {
					0, 5+t, 1, 4+t, 1, 3+t, 3, 1+t,  4, 1+t, 5, t, 
					w-35, t, w-17, 2, w-17, t,
					w-6, t, w-5, 1+t, w-4, 1+t, w-2, 3+t, w-2, 4+t, w-1, 5+t,
					w-1, h-6+t, w-2, h-5+t, w-2, h-4+t, w-4, h-2+t, w-5, h-2+t, w-6, h-1+t,
					5, h-1+t, 4, h-2+t, 3, h-2+t, 1, h-4+t, 1, h-5+t, 0, h-6+t, 
					0, 5+t};
			tip.setLocation (Math.min (dest.width - size.x, x - size.x + i), y);
		} else {
			polyline = new int [] {
				0, 5, 1, 5, 1, 3, 3, 1, 5, 1, 5, 0, 
				w-5, 0, w-5, 1, w-3, 1, w-1, 3, w-1, 5, w, 5,
				w, h-5, w-1, h-5, w-1, h-3, w-2, h-3, w-2, h-2, w-3, h-2, w-3, h-1, w-5, h-1, w-5, h,
				w-16, h, w-16, h+t, w-35, h,
				5, h, 5, h-1, 3, h-1, 3, h-2, 2, h-2, 2, h-3, 1, h-3, 1, h-5, 0, h-5, 
				0, 5};
			borderPolygon = new int[] {
					0, 5, 1, 4, 1, 3, 3, 1,  4, 1, 5, 0, 
					w-6, 0, w-5, 1, w-4, 1, w-2, 3, w-2, 4, w-1, 5,
					w-1, h-6, w-2, h-5, w-2, h-4, w-4, h-2, w-5, h-2, w-6, h-1,
					w-17, h-1, w-17, h+t-2, w-36, h-1,
					5, h-1, 4, h-2, 3, h-2, 1, h-4, 1, h-5, 0, h-6, 
					0, 5};
			tip.setLocation (Math.min (dest.width - size.x, x - size.x + i), y - size.y - t);
		}
	}	
	if ((style & SWT.BALLOON) != 0) {
		if (region != null) region.dispose ();
		region = new Region (display);
		region.add (polyline);
		tip.setRegion (region);
	}
}

public boolean getAutoHide () {
	checkWidget ();
	return autohide;
}

Point getSize (int maxWidth) {
	int textWidth = 0, messageWidth = 0;
	if (layoutText != null) {
		layoutText.setWidth (-1);
		textWidth = layoutText.getBounds ().width;
	}
	if (layoutMessage != null) {
		layoutMessage.setWidth (-1);
		messageWidth = layoutMessage.getBounds ().width;
	}
	int messageTrim = 2 * INSET + 2 * BORDER + 2 * PADDING;
	boolean hasImage = 	layoutText != null && (style & SWT.BALLOON) != 0 && (style & (SWT.ICON_ERROR | SWT.ICON_INFORMATION | SWT.ICON_WARNING)) != 0;
	int textTrim = messageTrim + (hasImage ? IMAGE_SIZE : 0);
	int width = Math.min (maxWidth, Math.max (textWidth + textTrim, messageWidth + messageTrim));
	int textHeight = 0, messageHeight = 0;
	if (layoutText != null) {
		layoutText.setWidth (maxWidth - textTrim);	
		textHeight = layoutText.getBounds ().height;
	}
	if (layoutMessage != null) {
		layoutMessage.setWidth (maxWidth - messageTrim);
		messageHeight = layoutMessage.getBounds ().height;
	}
	int height = 2 * BORDER + 2 * PADDING + messageHeight;
	if (layoutText != null) height += Math.max (IMAGE_SIZE, textHeight) + 2 * PADDING;
	return new Point (width, height);
}

public String getMessage () {
	checkWidget ();
	return layoutMessage != null ? layoutMessage.getText() : "";
}

public Shell getParent () {
	checkWidget ();
	return parent;
}

public String getText () {
	checkWidget ();
	return layoutText != null ? layoutText.getText() : "";
}

public boolean getVisible () {
	checkWidget ();
	return tip.getVisible ();
}

public boolean isVisible () {
	checkWidget ();
	return getVisible ();
}

void onDispose (Event event) {
	removeListener (SWT.Dispose, listener);
	notifyListeners (SWT.Dispose, event);
	event.type = SWT.None;

	if (runnable != null) {
		Display display = getDisplay ();
		display.timerExec (-1, runnable);
	}
	runnable = null;
	tip.dispose ();
	tip = null;
	if (region != null) region.dispose ();
	region = null;
	if (layoutText != null) layoutText.dispose ();
	layoutText = null;
	if (layoutMessage != null) layoutMessage.dispose ();
	layoutMessage = null;
	if (boldFont != null) boldFont.dispose ();
	boldFont = null;
	borderPolygon = null;
}

void onMouseDown (Event event) {
	notifyListeners (SWT.Selection, new Event ());
	setVisible (false);
}

void onPaint (Event event) {
	GC gc = event.gc;
	int x = BORDER + PADDING;
	int y = BORDER + PADDING;
	if ((style & SWT.BALLOON) != 0) {
		if (spikeAbove) y += TIP_HEIGHT;
		gc.drawPolygon (borderPolygon);
	} else {
		Rectangle rect = tip.getClientArea ();
		gc.drawRectangle(rect.x, rect.y, rect.width - 1, rect.height -1);
	} 
	if (layoutText != null) {
		int id = style & (SWT.ICON_ERROR | SWT.ICON_INFORMATION | SWT.ICON_WARNING);
		if ((style & SWT.BALLOON) != 0 && id != 0) {
			Display display = getDisplay ();
			Image image = display.getSystemImage (id);
			Rectangle rect = image.getBounds ();
			gc.drawImage (image, 0, 0, rect.width, rect.height, x, y, IMAGE_SIZE, IMAGE_SIZE);
			x += IMAGE_SIZE;
		}
		x += INSET;
		layoutText.draw (gc, x, y);
		y += 2 * PADDING + Math.max (IMAGE_SIZE, layoutText.getBounds ().height);
	}
	if (layoutMessage != null) {
		x = BORDER + PADDING + INSET;
		layoutMessage.draw (gc, x, y);
	}
}

public void removeSelectionListener (SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

public void setAutoHide (boolean autohide) {
	checkWidget ();
	this.autohide = autohide;
	//TODO - update when visible
}

public void setLocation (int x, int y) {
	checkWidget ();
	if (this.x == x && this.y == y) return;
	this.x = x;
	this.y = y;
	if (tip.getVisible ()) configure ();
}

public void setLocation (Point location) {
	checkWidget ();
	if (location == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	setLocation (location.x, location.y);
}

public void setMessage (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (layoutMessage != null) layoutMessage.dispose();
	layoutMessage = null;
	if (string.length () != 0) {
		Display display = getDisplay (); 
		layoutMessage = new TextLayout (display);
		layoutMessage.setText (string);
	}
	if (tip.getVisible ()) configure ();
}

public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (layoutText != null) layoutText.dispose ();
	layoutText = null;
	if (boldFont != null) boldFont.dispose ();
	boldFont = null;
	if (string.length () != 0) {
		Display display = getDisplay ();
		layoutText = new TextLayout (display);
		layoutText.setText (string);
		Font font = display.getSystemFont ();
		FontData data = font.getFontData () [0];
		boldFont = new Font (display, data.getName (), data.getHeight (), SWT.BOLD);
		TextStyle style = new TextStyle (boldFont, null, null);
		layoutText.setStyle (style, 0, string.length ());
	}
	if (tip.getVisible ()) configure ();
}

public void setVisible (boolean visible) {
	if (visible) configure ();
	tip.setVisible (visible);
	Display display = getDisplay ();
	if (runnable != null) display.timerExec (-1, runnable);
	runnable = null;
	if (autohide && visible) {
		runnable = new Runnable () {
			public void run () {
				if (!isDisposed ()) setVisible (false);
			}
		};
		display.timerExec(DELAY, runnable);
	}
}

}
