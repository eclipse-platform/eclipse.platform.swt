/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.custom;


import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.accessibility.*;

/**
 * A Label which supports aligned text and/or an image and different border styles.
 * <p>
 * If there is not enough space a CLabel uses the following strategy to fit the 
 * information into the available space:
 * <pre>
 * 		ignores the indent in left align mode
 * 		ignores the image and the gap
 * 		shortens the text by replacing the center portion of the label with an ellipsis
 * 		shortens the text by removing the center portion of the label
 * </pre>
 * <p>
 * <dl>
 * <dt><b>Styles:</b>
 * <dd>LEFT, RIGHT, CENTER, SHADOW_IN, SHADOW_OUT, SHADOW_NONE</dd>
 * <dt><b>Events:</b>
 * <dd></dd>
 * </dl>
 * 
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class CLabel extends Canvas {

	/** Gap between icon and text */
	private static final int GAP = 5;
	/** Left and right margins */
	private static final int INDENT = 3;
	/** a string inserted in the middle of text that has been shortened */
	private static final String ellipsis = "..."; //$NON-NLS-1$
	/** the alignment. Either CENTER, RIGHT, LEFT. Default is LEFT*/
	private int align = SWT.LEFT;
	private int hIndent = INDENT;
	private int vIndent = INDENT;
	/** the current text */
	private String text;
	/** the current icon */
	private Image image;
	// The tooltip is used for two purposes - the application can set
	// a tooltip or the tooltip can be used to display the full text when the
	// the text has been truncated due to the label being too short.
	// The appToolTip stores the tooltip set by the application.  Control.tooltiptext 
	// contains whatever tooltip is currently being displayed.
	private String appToolTipText;
	
	private Image backgroundImage;
	private Color[] gradientColors;
	private int[] gradientPercents;
	private boolean gradientVertical;

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a widget which will be the parent of the new instance (cannot be null)
 * @param style the style of widget to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 * </ul>
 *
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#CENTER
 * @see SWT#SHADOW_IN
 * @see SWT#SHADOW_OUT
 * @see SWT#SHADOW_NONE
 * @see #getStyle
 */
public CLabel(Composite parent, int style) {
	super(parent, checkStyle(style));
	
	if ((style & SWT.CENTER) != 0) align = SWT.CENTER;
	if ((style & SWT.RIGHT) != 0)  align = SWT.RIGHT;
	if ((style & SWT.LEFT) != 0)   align = SWT.LEFT;
	
	addPaintListener(new PaintListener(){
		public void paintControl(PaintEvent event) {
			onPaint(event);
		}
	});
	
	addDisposeListener(new DisposeListener(){
		public void widgetDisposed(DisposeEvent event) {
			onDispose(event);
		}
	});

	initAccessible();

}
/**
 * Check the style bits to ensure that no invalid styles are applied.
 */
private static int checkStyle (int style) {
	int mask = SWT.SHADOW_IN | SWT.SHADOW_OUT | SWT.SHADOW_NONE | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
	style = style & mask;
	style |= SWT.NO_FOCUS;
	if ((style & (SWT.CENTER | SWT.RIGHT)) == 0) style |= SWT.LEFT;
	//TEMPORARY CODE
	/*
	 * The default background on carbon and some GTK themes is not a solid color 
	 * but a texture.  To show the correct default background, we must allow
	 * the operating system to draw it and therefore, we can not use the 
	 * NO_BACKGROUND style.  The NO_BACKGROUND style is not required on platforms
	 * that use double buffering which is true in both of these cases.
	 */
	String platform = SWT.getPlatform();
	if ("carbon".equals(platform) || "gtk".equals(platform)) return style; //$NON-NLS-1$ //$NON-NLS-2$
	return style | SWT.NO_BACKGROUND;
}

//protected void checkSubclass () {
//	String name = getClass().getName ();
//	String validName = CLabel.class.getName();
//	if (!validName.equals(name)) {
//		SWT.error (SWT.ERROR_INVALID_SUBCLASS);
//	}
//}

public Point computeSize(int wHint, int hHint, boolean changed) {
	checkWidget();
	Point e = getTotalSize(image, text);
	if (wHint == SWT.DEFAULT){
		e.x += 2*hIndent;
	} else {
		e.x = wHint;
	}
	if (hHint == SWT.DEFAULT) {
		e.y += 2*vIndent;
	} else {
		e.y = hHint;
	}
	return e;
}
/**
 * Draw a rectangle in the given colors.
 */
private void drawBevelRect(GC gc, int x, int y, int w, int h, Color topleft, Color bottomright) {
	gc.setForeground(bottomright);
	gc.drawLine(x+w, y,   x+w, y+h);
	gc.drawLine(x,   y+h, x+w, y+h);
	
	gc.setForeground(topleft);
	gc.drawLine(x, y, x+w-1, y);
	gc.drawLine(x, y, x,     y+h-1);
}
/**
 * Returns the alignment.
 * The alignment style (LEFT, CENTER or RIGHT) is returned.
 * 
 * @return SWT.LEFT, SWT.RIGHT or SWT.CENTER
 */
public int getAlignment() {
	//checkWidget();
	return align;
}
/**
 * Return the CLabel's image or <code>null</code>.
 * 
 * @return the image of the label or null
 */
public Image getImage() {
	//checkWidget();
	return image;
}
/**
 * Compute the minimum size.
 */
private Point getTotalSize(Image image, String text) {
	Point size = new Point(0, 0);
	
	if (image != null) {
		Rectangle r = image.getBounds();
		size.x += r.width;
		size.y += r.height;
	}
		
	GC gc = new GC(this);
	if (text != null && text.length() > 0) {
		Point e = gc.textExtent(text);
		size.x += e.x;
		size.y = Math.max(size.y, e.y);
		if (image != null) size.x += GAP;
	} else {
		size.y = Math.max(size.y, gc.getFontMetrics().getHeight());
	}
	gc.dispose();
	
	return size;
}
public void setToolTipText (String string) {
	super.setToolTipText (string);
	appToolTipText = super.getToolTipText();
}	
/**
 * Return the Label's text.
 * 
 * @return the text of the label or null
 */
public String getText() {
	//checkWidget();
	return text;
}
public String getToolTipText () {
	checkWidget();
	return appToolTipText;
}
/**
 * Paint the Label's border.
 */
private void paintBorder(GC gc, Rectangle r) {
	Display disp= getDisplay();

	Color c1 = null;
	Color c2 = null;
	
	int style = getStyle();
	if ((style & SWT.SHADOW_IN) != 0) {
		c1 = disp.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
		c2 = disp.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);
	}
	if ((style & SWT.SHADOW_OUT) != 0) {		
		c1 = disp.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
		c2 = disp.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
	}
		
	if (c1 != null && c2 != null) {
		gc.setLineWidth(1);
		drawBevelRect(gc, r.x, r.y, r.width-1, r.height-1, c1, c2);
	}
}
private void initAccessible() {
	Accessible accessible = getAccessible();
	accessible.addAccessibleListener(new AccessibleAdapter() {
		public void getName(AccessibleEvent e) {
			e.result = getText();
		}
		
		public void getHelp(AccessibleEvent e) {
			e.result = getToolTipText();
		}
	});
		
	accessible.addAccessibleControlListener(new AccessibleControlAdapter() {
		public void getChildAtPoint(AccessibleControlEvent e) {
			Point pt = toControl(new Point(e.x, e.y));
			e.childID = (getBounds().contains(pt)) ? ACC.CHILDID_SELF : ACC.CHILDID_NONE;
		}
		
		public void getLocation(AccessibleControlEvent e) {
			Rectangle location = getBounds();
			Point pt = toDisplay(new Point(location.x, location.y));
			e.x = pt.x;
			e.y = pt.y;
			e.width = location.width;
			e.height = location.height;
		}
		
		public void getChildCount(AccessibleControlEvent e) {
			e.detail = 0;
		}
		
		public void getRole(AccessibleControlEvent e) {
			e.detail = ACC.ROLE_LABEL;
		}
		
		public void getState(AccessibleControlEvent e) {
			e.detail = ACC.STATE_READONLY;
		}
	});
}
private void onDispose(DisposeEvent event) {
	gradientColors = null;
	gradientPercents = null;
	backgroundImage = null;
	text = null;
	image = null;
	appToolTipText = null;
}
/* 
 * Process the paint event
 */
private void onPaint(PaintEvent event) {
	Rectangle rect = getClientArea();
	if (rect.width == 0 || rect.height == 0) return;
	
	boolean shortenText = false;
	String t = text;
	Image img = image;
	int availableWidth = rect.width - 2*hIndent;
	Point extent = getTotalSize(img, t);
	if (extent.x > availableWidth) {
		img = null;
		extent = getTotalSize(img, t);
		if (extent.x > availableWidth) {
			shortenText = true;
		}
	}
	
	GC gc = event.gc;
		
	// shorten the text
	if (shortenText) {
		t = shortenText(gc, text, availableWidth);
		extent = getTotalSize(img, t);
		if (appToolTipText == null) {
			super.setToolTipText(text);
		}
	} else {
		super.setToolTipText(appToolTipText);
	}
		
	// determine horizontal position
	int x = rect.x + hIndent;
	if (align == SWT.CENTER) {
		x = (rect.width-extent.x)/2;
	}
	if (align == SWT.RIGHT) {
		x = rect.width-extent.x - hIndent;
	}
	
	// draw a background image behind the text
	try {
		if (backgroundImage != null) {
			// draw a background image behind the text
			Rectangle imageRect = backgroundImage.getBounds();
			gc.drawImage(backgroundImage, 0, 0, imageRect.width, imageRect.height,
				0, 0, rect.width, rect.height);
		} else if (gradientColors != null) {
			// draw a gradient behind the text
			final Color oldBackground = gc.getBackground();
			if (gradientColors.length == 1) {
				if (gradientColors[0] != null) gc.setBackground(gradientColors[0]);
				gc.fillRectangle(0, 0, rect.width, rect.height);
			} else {
				final Color oldForeground = gc.getForeground();
				Color lastColor = gradientColors[0];
				if (lastColor == null) lastColor = oldBackground;
				for (int i = 0, pos = 0; i < gradientPercents.length; ++i) {
					gc.setForeground(lastColor);
					lastColor = gradientColors[i + 1];
					if (lastColor == null) lastColor = oldBackground;
					gc.setBackground(lastColor);
					if (gradientVertical) {
						final int gradientHeight = (gradientPercents[i] * rect.height / 100) - pos;
						gc.fillGradientRectangle(0, pos, rect.width, gradientHeight, true);
						pos += gradientHeight;
					} else {
						final int gradientWidth = (gradientPercents[i] * rect.width / 100) - pos;
						gc.fillGradientRectangle(pos, 0, gradientWidth, rect.height, false);
						pos += gradientWidth;
					}
				}
				gc.setForeground(oldForeground);
			}
			gc.setBackground(oldBackground);
		} else {
			if ((getStyle() & SWT.NO_BACKGROUND) != 0) {
				gc.setBackground(getBackground());
				gc.fillRectangle(rect);
			}
		}
	} catch (SWTException e) {
		if ((getStyle() & SWT.NO_BACKGROUND) != 0) {
			gc.setBackground(getBackground());
			gc.fillRectangle(rect);
		}
	}

	// draw border
	int style = getStyle();
	if ((style & SWT.SHADOW_IN) != 0 || (style & SWT.SHADOW_OUT) != 0) {
		paintBorder(gc, rect);
	}
	// draw the image		
	if (img != null) {
		Rectangle imageRect = img.getBounds();
		gc.drawImage(img, 0, 0, imageRect.width, imageRect.height, 
		                x, (rect.height-imageRect.height)/2, imageRect.width, imageRect.height);
		x += imageRect.width + GAP;
	}
	// draw the text
	if (t != null) {
		int textHeight = gc.getFontMetrics().getHeight();
		gc.setForeground(getForeground());
		gc.drawText(t, x, rect.y + (rect.height-textHeight)/2, true);
	}
}
/**
 * Set the alignment of the CLabel.
 * Use the values LEFT, CENTER and RIGHT to align image and text within the available space.
 * 
 * @param align the alignment style of LEFT, RIGHT or CENTER
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the value of align is not one of SWT.LEFT, SWT.RIGHT or SWT.CENTER</li>
 * </ul>
 */
public void setAlignment(int align) {
	checkWidget();
	if (align != SWT.LEFT && align != SWT.RIGHT && align != SWT.CENTER) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (this.align != align) {
		this.align = align;
		redraw();
	}
}

public void setBackground (Color color) {
	super.setBackground (color);
	// Are these settings the same as before?
	if (color != null && backgroundImage == null && 
		gradientColors == null && gradientPercents == null) {
		Color background = getBackground();
		if (color.equals(background)) {
			return;
		}		
	}
	backgroundImage = null;
	gradientColors = null;
	gradientPercents = null;
	redraw ();
}

/**
 * Specify a gradient of colours to be drawn in the background of the CLabel.
 * <p>For example, to draw a gradient that varies from dark blue to blue and then to
 * white and stays white for the right half of the label, use the following call 
 * to setBackground:</p>
 * <pre>
 *	clabel.setBackground(new Color[]{display.getSystemColor(SWT.COLOR_DARK_BLUE), 
 *		                           display.getSystemColor(SWT.COLOR_BLUE),
 *		                           display.getSystemColor(SWT.COLOR_WHITE), 
 *		                           display.getSystemColor(SWT.COLOR_WHITE)},
 *		               new int[] {25, 50, 100});
 * </pre>
 *
 * @param colors an array of Color that specifies the colors to appear in the gradient 
 *               in order of appearance from left to right;  The value <code>null</code> 
 *               clears the background gradient; the value <code>null</code> can be used 
 *               inside the array of Color to specify the background color.
 * @param percents an array of integers between 0 and 100 specifying the percent of the width 
 *                 of the widget at which the color should change; the size of the percents 
 *                 array must be one less than the size of the colors array.
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the values of colors and percents are not consistant</li>
 * </ul>
 */
public void setBackground(Color[] colors, int[] percents) {
	setBackground(colors, percents, false);
}
/**
 * Specify a gradient of colours to be drawn in the background of the CLabel.
 * <p>For example, to draw a gradient that varies from dark blue to white in the vertical,
 * direction use the following call 
 * to setBackground:</p>
 * <pre>
 *	clabel.setBackground(new Color[]{display.getSystemColor(SWT.COLOR_DARK_BLUE), 
 *		                           display.getSystemColor(SWT.COLOR_WHITE)},
 *		                 new int[] {100}, true);
 * </pre>
 *
 * @param colors an array of Color that specifies the colors to appear in the gradient 
 *               in order of appearance from left/top to right/bottom;  The value <code>null</code> 
 *               clears the background gradient; the value <code>null</code> can be used 
 *               inside the array of Color to specify the background color.
 * @param percents an array of integers between 0 and 100 specifying the percent of the width/height 
 *                 of the widget at which the color should change; the size of the percents 
 *                 array must be one less than the size of the colors array.
 * @param vertical indicate the direction of the gradient.  True is vertical and false is horizontal.
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the values of colors and percents are not consistant</li>
 * </ul>
 * 
 * @since 3.0
 */
public void setBackground(Color[] colors, int[] percents, boolean vertical) {	
	checkWidget();
	if (colors != null) {
		if (percents == null || percents.length != colors.length - 1) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (getDisplay().getDepth() < 15) {
			// Don't use gradients on low color displays
			colors = new Color[] { colors[0] };
			percents = new int[] { };
		}
		for (int i = 0; i < percents.length; i++) {
			if (percents[i] < 0 || percents[i] > 100) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
			if (i > 0 && percents[i] < percents[i-1]) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
		}
	}
	
	// Are these settings the same as before?
	final Color background = getBackground();
	if (backgroundImage == null) {
		if ((gradientColors != null) && (colors != null) && 
			(gradientColors.length == colors.length)) {
			boolean same = false;
			for (int i = 0; i < gradientColors.length; i++) {
				same = (gradientColors[i] == colors[i]) ||
					((gradientColors[i] == null) && (colors[i] == background)) ||
					((gradientColors[i] == background) && (colors[i] == null));
				if (!same) break;
			}
			if (same) {
				for (int i = 0; i < gradientPercents.length; i++) {
					same = gradientPercents[i] == percents[i];
					if (!same) break;
				}
			}
			if (same && this.gradientVertical == vertical) return;
		}
	} else {
		backgroundImage = null;
	}
	// Store the new settings
	if (colors == null) {
		gradientColors = null;
		gradientPercents = null;
		gradientVertical = false;
	} else {
		gradientColors = new Color[colors.length];
		for (int i = 0; i < colors.length; ++i)
			gradientColors[i] = (colors[i] != null) ? colors[i] : background;
		gradientPercents = new int[percents.length];
		for (int i = 0; i < percents.length; ++i)
			gradientPercents[i] = percents[i];
		gradientVertical = vertical;
	}
	// Refresh with the new settings
	redraw();
}
/**
 * Set the image to be drawn in the background of the label.
 * 
 * @param image the image to be drawn in the background
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setBackground(Image image) {
	checkWidget();
	if (image == backgroundImage) return;
	if (image != null) {
		gradientColors = null;
		gradientPercents = null;
	}
	backgroundImage = image;
	redraw();
	
}
public void setFont(Font font) {
	super.setFont(font);
	redraw();
}
/**
 * Set the label's Image.
 * The value <code>null</code> clears it.
 * 
 * @param image the image to be displayed in the label or null
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setImage(Image image) {
	checkWidget();
	if (image != this.image) {
		this.image = image;
		redraw();
	}
}
/**
 * Set the label's text.
 * The value <code>null</code> clears it.
 * 
 * @param text the text to be displayed in the label or null
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setText(String text) {
	checkWidget();
	if (text == null) text = ""; //$NON-NLS-1$
	if (! text.equals(this.text)) {
		this.text = text;
		redraw();
	}
}
/**
 * Shorten the given text <code>t</code> so that its length doesn't exceed
 * the given width. The default implementation replaces characters in the
 * center of the original string with an ellipsis ("...").
 * Override if you need a different strategy.
 */
protected String shortenText(GC gc, String t, int width) {
	if (t == null) return null;
	int w = gc.textExtent(ellipsis).x;
	int l = t.length();
	int pivot = l/2;
	int s = pivot;
	int e = pivot+1;
	while (s >= 0 && e < l) {
		String s1 = t.substring(0, s);
		String s2 = t.substring(e, l);
		int l1 = gc.textExtent(s1).x;
		int l2 = gc.textExtent(s2).x;
		if (l1+w+l2 < width) {
			t = s1 + ellipsis + s2;
			break;
		}
		s--;
		e++;
	}
	return t;
}
}
