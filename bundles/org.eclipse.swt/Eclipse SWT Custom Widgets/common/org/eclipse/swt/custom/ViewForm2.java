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


import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;

/**
* DO NOT USE - UNDER CONSTRUCTION
*
* @ since 3.0
*/

/**
 * Instances of this class implement a Composite that lays out three
 * children horizontally and allows programmatic control of layout and
 * border parameters. ViewForm is used in the workbench to implement a
 * view's label/menu/toolbar local bar.
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BORDER, FLAT</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(None)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */

public class ViewForm2 extends Composite {

	/**
	 * marginWidth specifies the number of pixels of horizontal margin
	 * that will be placed along the left and right edges of the form.
	 *
	 * The default value is 0.
	 */
 	public int marginWidth = 0;
	/**
	 * marginHeight specifies the number of pixels of vertical margin
	 * that will be placed along the top and bottom edges of the form.
	 *
	 * The default value is 0.
	 */
 	public int marginHeight = 0;
 	/**
	 * horizontalSpacing specifies the number of pixels between the right
	 * edge of one cell and the left edge of its neighbouring cell to
	 * the right.
	 *
	 * The default value is 1.
	 */
 	public int horizontalSpacing = 1;
	/**
	 * verticalSpacing specifies the number of pixels between the bottom
	 * edge of one cell and the top edge of its neighbouring cell underneath.
	 *
	 * The default value is 1.
	 */
 	public int verticalSpacing = 1;
	
	/**
	 * Color of innermost line of drop shadow border.
	 */
	public static RGB borderInsideRGB  = new RGB (132, 130, 132);
	/**
	 * Color of middle line of drop shadow border.
	 */
	public static RGB borderMiddleRGB  = new RGB (143, 141, 138);
	/**
	 * Color of outermost line of drop shadow border.
	 */
	public static RGB borderOutsideRGB = new RGB (171, 168, 165);
	
	// SWT widgets
	private Control topLeft;
	private Control topCenter;
	private Control topRight;
	private Control content;
	
	// Configuration and state info
	private boolean separateTopCenter = false;
	private boolean showBorder = false;
	
	private int BORDER_TOP = 0;
	private int BORDER_BOTTOM = 0;
	private int BORDER_LEFT = 0;
	private int BORDER_RIGHT = 0;
	
	private Color borderColor1;
	private Color borderColor2;
	private Color borderColor3;
	
	private Rectangle oldArea;
	private static final int OFFSCREEN = -200;
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
 * @see SWT#BORDER
 * @see SWT#FLAT
 * @see #getStyle()
 */		
public ViewForm2(Composite parent, int style) {
	super(parent, checkStyle(style));
	
	borderColor1 = new Color(getDisplay(), borderInsideRGB);
	borderColor2 = new Color(getDisplay(), borderMiddleRGB);
	borderColor3 = new Color(getDisplay(), borderOutsideRGB);
	setBorderVisible((style & SWT.BORDER) != 0);

	addPaintListener(new PaintListener() {
		public void paintControl(PaintEvent event) {
			onPaint(event.gc);
		}
	});
	addControlListener(new ControlAdapter(){
		public void controlResized(ControlEvent e) {
			onResize();
		}
	});
	
	addListener(SWT.Dispose, new Listener() {
		public void handleEvent(Event e) {
			onDispose();
		}
	});	
}

static int checkStyle (int style) {
	int mask = SWT.FLAT | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
	return style & mask | SWT.NO_REDRAW_RESIZE;
}

protected void checkSubclass () {
	String name = getClass().getName ();
	String validName = ViewForm2.class.getName();
	if (!validName.equals(name)) {
		SWT.error (SWT.ERROR_INVALID_SUBCLASS);
	}
}

public Point computeSize(int wHint, int hHint, boolean changed) {
	checkWidget();
	// size of title bar area
	Point leftSize = new Point(0, 0);
	if (topLeft != null) {
		leftSize = topLeft.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	}
	Point centerSize = new Point(0, 0);
	if (topCenter != null) {
		 centerSize = topCenter.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	}
	Point rightSize = new Point(0, 0);
	if (topRight != null) {
		 rightSize = topRight.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	}
	Point size = new Point(0, 0);
	// calculate width of title bar
	if (separateTopCenter ||
	    (wHint != SWT.DEFAULT &&  leftSize.x + centerSize.x + rightSize.x > wHint)) {
		size.x = leftSize.x + rightSize.x;
		if (leftSize.x > 0 && rightSize.x > 0) size.x += horizontalSpacing;
		size.x = Math.max(centerSize.x, size.x);
		size.y = Math.max(leftSize.y, rightSize.y);
		if (topCenter != null){
			size.y += centerSize.y;
			if (topLeft != null ||topRight != null)size.y += verticalSpacing;
		}	
	} else {
		size.x = leftSize.x + centerSize.x + rightSize.x;
		int count = -1;
		if (leftSize.x > 0) count++;
		if (centerSize.x > 0) count++;
		if (rightSize.x > 0) count++;
		if (count > 0) size.x += count * horizontalSpacing;
		size.y = Math.max(leftSize.y, Math.max(centerSize.y, rightSize.y));
	}
	
	if (content != null) {
		Point contentSize = new Point(0, 0);
		contentSize = content.computeSize(SWT.DEFAULT, SWT.DEFAULT); 
		size.x = Math.max (size.x, contentSize.x);
		size.y += contentSize.y;
		if (size.y > contentSize.y) size.y += verticalSpacing;
	}
	
	size.x += 2 * marginWidth;
	size.y += 2 * marginHeight;
	
	if (wHint != SWT.DEFAULT) size.x  = wHint;
	if (hHint != SWT.DEFAULT) size.y = hHint;
	
	Rectangle trim = computeTrim(0, 0, size.x, size.y);
	return new Point (trim.width, trim.height);
}
public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget ();
	int trimX = x - BORDER_LEFT;
	int trimY = y - BORDER_TOP;
	int trimWidth = width + BORDER_LEFT + BORDER_RIGHT;
	int trimHeight = height + BORDER_TOP + BORDER_BOTTOM;
	return new Rectangle(trimX, trimY, trimWidth, trimHeight);
}
public Rectangle getClientArea() {
	checkWidget();
	Rectangle clientArea = super.getClientArea();
	clientArea.x += BORDER_LEFT;
	clientArea.y += BORDER_TOP;
	clientArea.width -= BORDER_LEFT + BORDER_RIGHT;
	clientArea.height -= BORDER_TOP + BORDER_BOTTOM;
	return clientArea;
}
/**
* Returns the content area.
* 
* @return the control in the content area of the pane or null
*/
public Control getContent() {
	//checkWidget();
	return content;
}
/**
* Returns Control that appears in the top center of the pane.
* Typically this is a toolbar.
* 
* @return the control in the top center of the pane or null
*/
public Control getTopCenter() {
	//checkWidget();
	return topCenter;
}
/**
* Returns the Control that appears in the top left corner of the pane.
* Typically this is a label such as CLabel.
* 
* @return the control in the top left corner of the pane or null
*/
public Control getTopLeft() {
	//checkWidget();
	return topLeft;
}
/**
* Returns the control in the top right corner of the pane.
* Typically this is a Close button or a composite with a Menu and Close button.
* 
* @return the control in the top right corner of the pane or null
*/
public Control getTopRight() {
	//checkWidget();
	return topRight;
}
public void layout (boolean changed) {
	checkWidget();
	Rectangle rect = getClientArea();
	
	Point leftSize = new Point(0, 0);
	if (topLeft != null && !topLeft.isDisposed()) {
		leftSize = topLeft.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	}
	Point centerSize = new Point(0, 0);
	if (topCenter != null && !topCenter.isDisposed()) {
		 centerSize = topCenter.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	}
	Point rightSize = new Point(0, 0);
	if (topRight != null && !topRight.isDisposed()) {
		 rightSize = topRight.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	}
	
	int minTopWidth = leftSize.x + centerSize.x + rightSize.x + 2*marginWidth;
	int count = -1;
	if (leftSize.x > 0) count++;
	if (centerSize.x > 0) count++;
	if (rightSize.x > 0) count++;
	if (count > 0) minTopWidth += count * horizontalSpacing;
		
	int x = rect.x + rect.width - marginWidth;
	int y = rect.y + marginHeight;
	
	boolean top = false;
	if (separateTopCenter || minTopWidth > rect.width) {
		int topHeight = Math.max(rightSize.y, leftSize.y);
		if (topRight != null && !topRight.isDisposed()) {
			top = true;
			x -= rightSize.x;
			topRight.setBounds(x, y, rightSize.x, topHeight);
			x -= horizontalSpacing;
		}
		if (topLeft != null && !topLeft.isDisposed()) {
			top = true;
			leftSize = topLeft.computeSize(x - rect.x - marginWidth, SWT.DEFAULT);
			topLeft.setBounds(rect.x + marginWidth, y, leftSize.x, topHeight);
		}
		if (top)y += topHeight + verticalSpacing;
		if (topCenter != null && !topCenter.isDisposed()) {
			top = true;
			centerSize = topCenter.computeSize(rect.width - 2 * marginWidth, SWT.DEFAULT);
			topCenter.setBounds(rect.x + rect.width - marginWidth - centerSize.x, y, centerSize.x, centerSize.y);
			y += centerSize.y + verticalSpacing;
		}		
	} else {
		int topHeight = Math.max(rightSize.y, Math.max(centerSize.y, leftSize.y));
		if (topRight != null && !topRight.isDisposed()) {
			top = true;
			x -= rightSize.x;
			topRight.setBounds(x, y, rightSize.x, topHeight);
			x -= horizontalSpacing;
		}
		if (topCenter != null && !topCenter.isDisposed()) {
			top = true;
			x -= centerSize.x;
			topCenter.setBounds(x, y, centerSize.x, topHeight);
			x -= horizontalSpacing;
		}
		if (topLeft != null && !topLeft.isDisposed()) {
			top = true;
			leftSize = topLeft.computeSize(x - rect.x - marginWidth, topHeight);
			topLeft.setBounds(rect.x + marginWidth, y, leftSize.x, topHeight);
		}
		if (top)y += topHeight + verticalSpacing;
	}

	if (content != null && !content.isDisposed()) {
		 content.setBounds(rect.x + marginWidth, y, rect.width - 2 * marginWidth, rect.y + rect.height - y - marginHeight);
	}
}
void onDispose() {
	if (borderColor1 != null) {
		borderColor1.dispose();
	}
	borderColor1 = null;
	
	if (borderColor2 != null) {
		borderColor2.dispose();
	}
	borderColor2 = null;
	
	if (borderColor3 != null) {
		borderColor3.dispose();
	}
	borderColor3 = null;
	
	topLeft = null;
	topCenter = null;
	topRight = null;
	content = null;
	oldArea = null;
}

void onPaint(GC gc) {
	Rectangle d = super.getClientArea();
	
	if (showBorder) {
		if ((getStyle() & SWT.FLAT) !=0) {
			gc.setForeground(borderColor1);
			gc.drawRectangle(d.x, d.y, d.x + d.width - 1, d.y + d.height - 1);
		} else {
			gc.setForeground(borderColor1);
			gc.drawRectangle(d.x, d.y, d.x + d.width - 3, d.y + d.height - 3);
		
			gc.setForeground(borderColor2);
			gc.drawLine(d.x + 1,           d.y + d.height - 2, d.x + d.width - 1, d.y + d.height - 2);
			gc.drawLine(d.x + d.width - 2, d.y + 1,            d.x + d.width - 2, d.y + d.height - 1);
		
			gc.setForeground(borderColor3);
			gc.drawLine(d.x + 2,           d.y + d.height - 1, d.x + d.width - 2, d.y + d.height - 1);
			gc.drawLine(d.x + d.width - 1, d.y + 2,            d.x + d.width - 1, d.y + d.height - 2);
		}
	}
	gc.setForeground(getForeground());
}
void onResize() {
	layout();
	
	Rectangle area = super.getClientArea();
	if (oldArea == null || oldArea.width == 0 || oldArea.height == 0) {
		redraw();
	} else {
		int width = 0;
		if (oldArea.width < area.width) {
			width = area.width - oldArea.width + BORDER_RIGHT;
		} else if (oldArea.width > area.width) {
			width = BORDER_RIGHT;			
		}
		redraw(area.x + area.width - width, area.y, width, area.height, false);
		
		int height = 0;
		if (oldArea.height < area.height) {
			height = area.height - oldArea.height + BORDER_BOTTOM;		
		}
		if (oldArea.height > area.height) {
			height = BORDER_BOTTOM;		
		}
		redraw(area.x, area.y + area.height - height, area.width, height, false);
	}
	oldArea = area;
}
/**
* Sets the content.
* Setting the content to null will remove it from 
* the pane - however, the creator of the content must dispose of the content.
* 
* @param content the control to be displayed in the content area or null
* 
* @exception SWTException <ul>
*    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
*    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
*    <li>ERROR_INVALID_ARGUMENT - if the control is not a child of this ViewForm</li>
* </ul>
*/
public void setContent(Control content) {
	checkWidget();
	if (content != null && content.getParent() != this) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (this.content != null && !this.content.isDisposed()) {
		this.content.setBounds(OFFSCREEN, OFFSCREEN, 0, 0);
	}
	this.content = content;
	layout();
}

public void setFont(Font f) {
	super.setFont(f);
	if (topLeft != null && !topLeft.isDisposed()) topLeft.setFont(f);
	if (topCenter != null && !topCenter.isDisposed()) topCenter.setFont(f);
	if (topRight != null && !topRight.isDisposed()) topRight.setFont(f);
	
	layout();
}
/**
 * Sets the layout which is associated with the receiver to be
 * the argument which may be null.
 * <p>
 * Note : ViewForm does not use a layout class to size and position its children.
 * </p>
 *
 * @param layout the receiver's new layout or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLayout (Layout layout) {
	checkWidget();
	return;
}
/**
* Set the control that appears in the top center of the pane.
* Typically this is a toolbar.
* The topCenter is optional.  Setting the topCenter to null will remove it from 
* the pane - however, the creator of the topCenter must dispose of the topCenter.
* 
* @param topCenter the control to be displayed in the top center or null
* 
* @exception SWTException <ul>
*    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
*    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
*    <li>ERROR_INVALID_ARGUMENT - if the control is not a child of this ViewForm</li>
* </ul>
*/
public void setTopCenter(Control topCenter) {
	checkWidget();
	if (topCenter != null && topCenter.getParent() != this) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (this.topCenter != null && !this.topCenter.isDisposed()) {
		this.topCenter.setBounds(OFFSCREEN, OFFSCREEN, 0, 0);
	}
	this.topCenter = topCenter;
	layout();
}
/**
* Set the control that appears in the top left corner of the pane.
* Typically this is a label such as CLabel.
* The topLeft is optional.  Setting the top left control to null will remove it from 
* the pane - however, the creator of the control must dispose of the control.
* 
* @param c the control to be displayed in the top left corner or null
* 
* @exception SWTException <ul>
*    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
*    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
*    <li>ERROR_INVALID_ARGUMENT - if the control is not a child of this ViewForm</li>
* </ul>
*/
public void setTopLeft(Control c) {
	checkWidget();
	if (c != null && c.getParent() != this) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (this.topLeft != null && !this.topLeft.isDisposed()) {
		this.topLeft.setBounds(OFFSCREEN, OFFSCREEN, 0, 0);
	}
	this.topLeft = c;
	layout();
}
/**
* Set the control that appears in the top right corner of the pane.
* Typically this is a Close button or a composite with a Menu and Close button.
* The topRight is optional.  Setting the top right control to null will remove it from 
* the pane - however, the creator of the control must dispose of the control.
* 
* @param c the control to be displayed in the top right corner or null
* 
* @exception SWTException <ul>
*    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
*    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
*    <li>ERROR_INVALID_ARGUMENT - if the control is not a child of this ViewForm</li>
* </ul>
*/
public void setTopRight(Control c) {
	checkWidget();
	if (c != null && c.getParent() != this) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (this.topRight != null && !this.topRight.isDisposed()) {
		this.topRight.setBounds(OFFSCREEN, OFFSCREEN, 0, 0);
	}
	this.topRight = c;
	layout();
}
/**
* Specify whether the border should be displayed or not.
* 
* @param show true if the border should be displayed
* 
* @exception SWTException <ul>
*    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
*    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
* </ul>
*/
public void setBorderVisible(boolean show) {
	checkWidget();
	if (showBorder == show) return;
	
	showBorder = show;
	if (showBorder) {
		if ((getStyle() & SWT.FLAT)!= 0) {
			BORDER_LEFT = BORDER_TOP = BORDER_RIGHT = BORDER_BOTTOM = 1;
		} else {
			BORDER_LEFT = BORDER_TOP = 1;
			BORDER_RIGHT = BORDER_BOTTOM = 3;
		}
	} else {
		BORDER_BOTTOM = BORDER_TOP = BORDER_LEFT = BORDER_RIGHT = 0;
	}

	layout();
	redraw();
}
/**
* If true, the topCenter will always appear on a separate line by itself, otherwise the 
* topCenter will appear in the top row if there is room and will be moved to the second row if
* required.
* 
* @param show true if the topCenter will always appear on a separate line by itself
* 
* @exception SWTException <ul>
*    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
*    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
* </ul>
*/
public void setTopCenterSeparate(boolean show) {
	checkWidget();
	separateTopCenter = show;
	layout();
}

}