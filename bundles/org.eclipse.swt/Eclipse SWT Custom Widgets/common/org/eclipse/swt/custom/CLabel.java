package org.eclipse.swt.custom;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * A Label which supports aligned text and/or an image and different border styles.
 * <p>
 * If there is not enough space a SmartLabel uses the following strategy to fit the 
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
 * <dd>SHADOW_IN, SHADOW_OUT, SHADOW_NONE</dd>
 * <dt><b>Events:</b>
 * <dd></dd>
 * </dl>
 */
public class CLabel extends Canvas {

	/** Gap between icon and text */
	private static final int GAP = 5;
	/** Left and right margins */
	private static final int INDENT = 3;
	/** a string inserted in the middle of text that has been shortened */
	private static final String ellipsis = "...";
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
	private Image gradientImage;
	private Color[] gradientColors;
	private int[] gradientPercents;

	
/**
 * Create a CLabel with the given border style as a child of parent.
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

}
/**
 * Check the style bits to ensure that no invalid styles are applied.
 */
private static int checkStyle (int style) {
	int mask = SWT.SHADOW_IN | SWT.SHADOW_OUT | SWT.SHADOW_NONE;
	style = style & mask;
	style |= SWT.NO_FOCUS | SWT.NO_BACKGROUND;
	return style;
}
public Point computeSize(int wHint, int hHint, boolean changed) {
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
 */
public int getAlignment() {
	return align;
}
/**
 * Return the CLabel's image or <code>null</code>.
 */
public Image getImage() {
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
 */
public String getText() {
	return text;
}
public String getToolTipText () {
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
private void onDispose(DisposeEvent event) {
	if (gradientImage != null) {
		gradientImage.dispose();
	}
	gradientImage = null;
	gradientColors = null;
	gradientPercents = null;
	backgroundImage = null;
}
/* 
 * Process the paint event
 */
private void onPaint(PaintEvent event) {
	Rectangle rect = getClientArea();
	if (rect.width == 0 || rect.height == 0) return;
	
	boolean shortenText = false;
	String t = text;
	Image i = image;
	int availableWidth = rect.width - 2*hIndent;
	Point extent = getTotalSize(i, t);
	if (extent.x > availableWidth) {
		i = null;
		extent = getTotalSize(i, t);
		if (extent.x > availableWidth) {
			shortenText = true;
		}
	}
	
	GC gc = event.gc;
		
	// shorten the text
	if (shortenText) {
		t = shortenText(gc, text, availableWidth);
		extent = getTotalSize(i, t);
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
	if (backgroundImage != null) {
		Rectangle imageRect = backgroundImage.getBounds();
		try {
			gc.drawImage(backgroundImage, 0, 0, imageRect.width, imageRect.height,
			                              0, 0, rect.width, rect.height);
		} catch(SWTException e) {
			gc.setBackground(getBackground());
			gc.fillRectangle(rect);
		}
	} else {
		gc.setBackground(getBackground());
		gc.fillRectangle(rect);
	}
	// draw border
	int style = getStyle();
	if ((style & SWT.SHADOW_IN) != 0 || (style & SWT.SHADOW_OUT) != 0) {
		paintBorder(gc, rect);
	}
	// draw the image		
	if (i != null) {
		Rectangle imageRect = i.getBounds();
		gc.drawImage(i, 0, 0, imageRect.width, imageRect.height, 
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
 */
public void setAlignment(int align) {
	if (align != SWT.LEFT && align != SWT.RIGHT && align != SWT.CENTER) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (this.align != align) {
		this.align = align;
		redraw();
	}
}
/**
 * Specify a gradiant of colours to be draw in the background of the CLabel.
 * For example to draw a gradiant that varies from dark blue to blue and then to
 * white, use the following call to setBackground:
 * <pre>
 *	clabel.setBackground(new Color[]{display.getSystemColor(SWT.COLOR_DARK_BLUE), 
 *		                           display.getSystemColor(SWT.COLOR_BLUE),
 *		                           display.getSystemColor(SWT.COLOR_WHITE), 
 *		                           display.getSystemColor(SWT.COLOR_WHITE)},
 *		               new int[] {25, 50, 100});
 * </pre>
 *
 * @param colors an array of Color that specifies the colors to appear in the gradiant 
 *               in order of appearance left to right.  The value <code>null</code> clears the
 *               background gradiant. The value <code>null</code> can be used inside the array of 
 *               Color to specify the background color.
 * @param percents an array of integers between 0 and 100 specifying the percent of the width 
 *                 of the widget at which the color should change.  The size of the percents array must be one 
 *                 less than the size of the colors array.
 */
public void setBackground(Color[] colors, int[] percents) {
	if (colors != null) {
		if (percents == null || percents.length != colors.length - 1) 
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		for (int i = 0; i < percents.length; i++) {
			if (percents[i] < 0 || percents[i] > 100)
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			if (i > 0 && percents[i] < percents[i-1])
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
	}
	
	// Are these settings the same as before?
	if (gradientImage == null && gradientColors == null && colors == null) {
		if (backgroundImage != null) {
			backgroundImage = null;
			redraw();
		}
		return;
	}
	if (gradientColors != null && colors != null 
	    && gradientColors.length == colors.length) {
		boolean same = false;
		for (int i = 0; i < gradientColors.length; i++) {
			same = gradientColors[i].equals(colors[i]);
			if (!same) break;
		}
		if (same) {
			for (int i = 0; i < gradientPercents.length; i++) {
				same = gradientPercents[i] == percents[i];
				if (!same) break;
			}
		}
		if (same) return;
	}
	
	// Cleanup
	if (gradientImage != null) {
		gradientImage.dispose();
	}
	gradientImage = null;
	gradientColors = null;
	gradientPercents = null;
	backgroundImage = null;
	
	// Draw gradient onto an image
	if (colors != null) {
		Color[] colorsCopy = null;
		Display display = getDisplay();
		if (display.getDepth() < 15) {
			colorsCopy = new Color[]{colors[0]};
		} else {
			colorsCopy = colors;
		}
		
		int x = 0; int y = 0;
		int width = 100; int height = 10;
		Image temp = new Image(display, width, height);
		GC gc = new GC(temp);
		if (colorsCopy.length == 1) {
			gc.setBackground(colorsCopy[0]);
			gc.fillRectangle(temp.getBounds());
		}
		int start = 0;
		int end = 0;
		for (int j = 0; j < colorsCopy.length - 1; j++) {
			Color startColor = colorsCopy[j];
			if (startColor == null) startColor = getBackground();
			RGB rgb1 = startColor.getRGB();
			Color endColor = colorsCopy[j+1];
			if (endColor == null) endColor = getBackground();
			RGB rgb2   = endColor.getRGB();
			start = end;
			end = (width) * percents[j] / 100;
			int range = Math.max(1, end - start);
			for (int k = 0; k < (end - start); k++) {
				int r = rgb1.red + k*(rgb2.red - rgb1.red)/range;
				r = (rgb2.red > rgb1.red) ? Math.min(r, rgb2.red) : Math.max(r, rgb2.red);
				int g = rgb1.green + k*(rgb2.green - rgb1.green)/range;
				g = (rgb2.green > rgb1.green) ? Math.min(g, rgb2.green) : Math.max(g, rgb2.green);
				int b = rgb1.blue + k*(rgb2.blue - rgb1.blue)/range;
				b = (rgb2.blue > rgb1.blue) ? Math.min(b, rgb2.blue) : Math.max(b, rgb2.blue);
				Color color = new Color(display, r, g, b); 
				gc.setBackground(color);					
				gc.fillRectangle(start + k,y,1,height);
				gc.setBackground(getBackground());
				color.dispose();
			}
		}
		gc.dispose();
		gradientImage = temp;
		gradientColors = colorsCopy;
		gradientPercents = percents;
		backgroundImage = temp;
	}
	
	redraw();
}
public void setBackground(Image image) {
	if (image == backgroundImage) return;
	
	if (gradientImage != null) {
		gradientImage.dispose();
		gradientImage = null;
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
 */
public void setImage(Image image) {
	if (image != this.image) {
		this.image = image;
		redraw();
	}
}
/**
 * Set the label's text.
 * The value <code>null</code> clears it.
 */
public void setText(String text) {
	if (text == null) text = "";
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
