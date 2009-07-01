/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.custom;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * A ScrolledComposite provides scrollbars and will scroll its content when the user
 * uses the scrollbars.
 *
 *
 * <p>There are two ways to use the ScrolledComposite:
 * 
 * <p>
 * 1) Set the size of the control that is being scrolled and the ScrolledComposite 
 * will show scrollbars when the contained control can not be fully seen.
 * 
 * 2) The second way imitates the way a browser would work.  Set the minimum size of
 * the control and the ScrolledComposite will show scroll bars if the visible area is 
 * less than the minimum size of the control and it will expand the size of the control 
 * if the visible area is greater than the minimum size.  This requires invoking 
 * both setMinWidth(), setMinHeight() and setExpandHorizontal(), setExpandVertical().
 * 
 * <code><pre>
 * public static void main (String [] args) {
 *      Display display = new Display ();
 *      Color red = display.getSystemColor(SWT.COLOR_RED);
 *      Color blue = display.getSystemColor(SWT.COLOR_BLUE);
 *      Shell shell = new Shell (display);
 *      shell.setLayout(new FillLayout());
 * 	
 *      // set the size of the scrolled content - method 1
 *      final ScrolledComposite sc1 = new ScrolledComposite(shell, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
 *      final Composite c1 = new Composite(sc1, SWT.NONE);
 *      sc1.setContent(c1);
 *      c1.setBackground(red);
 *      GridLayout layout = new GridLayout();
 *      layout.numColumns = 4;
 *      c1.setLayout(layout);
 *      Button b1 = new Button (c1, SWT.PUSH);
 *      b1.setText("first button");
 *      c1.setSize(c1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
 *      
 *      // set the minimum width and height of the scrolled content - method 2
 *      final ScrolledComposite sc2 = new ScrolledComposite(shell, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
 *      sc2.setExpandHorizontal(true);
 *      sc2.setExpandVertical(true);
 *      final Composite c2 = new Composite(sc2, SWT.NONE);
 *      sc2.setContent(c2);
 *      c2.setBackground(blue);
 *      layout = new GridLayout();
 *      layout.numColumns = 4;
 *      c2.setLayout(layout);
 *      Button b2 = new Button (c2, SWT.PUSH);
 *      b2.setText("first button");
 *      sc2.setMinSize(c2.computeSize(SWT.DEFAULT, SWT.DEFAULT));
 *      
 *      Button add = new Button (shell, SWT.PUSH);
 *      add.setText("add children");
 *      final int[] index = new int[]{0};
 *      add.addListener(SWT.Selection, new Listener() {
 *          public void handleEvent(Event e) {
 *              index[0]++;
 *              Button button = new Button(c1, SWT.PUSH);
 *              button.setText("button "+index[0]);
 *              // reset size of content so children can be seen - method 1
 *              c1.setSize(c1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
 *              c1.layout();
 *              
 *              button = new Button(c2, SWT.PUSH);
 *              button.setText("button "+index[0]);
 *              // reset the minimum width and height so children can be seen - method 2
 *              sc2.setMinSize(c2.computeSize(SWT.DEFAULT, SWT.DEFAULT));
 *              c2.layout();
 *          }
 *      });
 * 
 *      shell.open ();
 *      while (!shell.isDisposed ()) {
 *          if (!display.readAndDispatch ()) display.sleep ();
 *      }
 *      display.dispose ();
 * }
 * </pre></code>
 *
 * <dl>
 * <dt><b>Styles:</b><dd>H_SCROLL, V_SCROLL
 * </dl>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#scrolledcomposite">ScrolledComposite snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public class ScrolledComposite extends Composite {

	Control content;
	Listener contentListener;
	Listener filter;
	
	int minHeight = 0;
	int minWidth = 0;
	boolean expandHorizontal = false;
	boolean expandVertical = false;
	boolean alwaysShowScroll = false;
	boolean showFocusedControl = false;

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
 * @see SWT#H_SCROLL
 * @see SWT#V_SCROLL
 * @see #getStyle()
 */	
public ScrolledComposite(Composite parent, int style) {
	super(parent, checkStyle(style));
	super.setLayout(new ScrolledCompositeLayout());
	ScrollBar hBar = getHorizontalBar ();
	if (hBar != null) {
		hBar.setVisible(false);
		hBar.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				hScroll();
			}
		});
	}
	
	ScrollBar vBar = getVerticalBar ();
	if (vBar != null) {
		vBar.setVisible(false);
		vBar.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				vScroll();
			}
		});
	}
	
	contentListener = new Listener() {
		public void handleEvent(Event e) {
			if (e.type != SWT.Resize) return;
			layout(false);
		}
	};
	
	filter = new Listener() {
		public void handleEvent(Event event) {
			if (event.widget instanceof Control) {
				Control control = (Control) event.widget;
				if (contains(control)) showControl(control);
			}
		}
	};
	
	addDisposeListener(new DisposeListener() {
		public void widgetDisposed(DisposeEvent e) {
			getDisplay().removeFilter(SWT.FocusIn, filter);
		}
	});
}

static int checkStyle (int style) {
	int mask = SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
	return style & mask;
}

boolean contains(Control control) {
	if (control == null || control.isDisposed()) return false;

	Composite parent = control.getParent();
	while (parent != null && !(parent instanceof Shell)) {
		if (this == parent) return true;
		parent = parent.getParent();
	}
	return false;
}

/**
 * Returns the Always Show Scrollbars flag.  True if the scrollbars are 
 * always shown even if they are not required.  False if the scrollbars are only 
 * visible when some part of the composite needs to be scrolled to be seen.
 * The H_SCROLL and V_SCROLL style bits are also required to enable scrollbars in the 
 * horizontal and vertical directions.
 * 
 * @return the Always Show Scrollbars flag value
 */
public boolean getAlwaysShowScrollBars() {
	//checkWidget();
	return alwaysShowScroll;
}

/**
 * Returns <code>true</code> if the content control 
 * will be expanded to fill available horizontal space.
 *
 * @return the receiver's horizontal expansion state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public boolean getExpandHorizontal() {
	checkWidget();
	return expandHorizontal;
}

/**
 * Returns <code>true</code> if the content control 
 * will be expanded to fill available vertical space.
 *
 * @return the receiver's vertical expansion state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public boolean getExpandVertical() {
	checkWidget();
	return expandVertical;
}

/**
 * Returns the minimum width of the content control.
 *
 * @return the minimum width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public int getMinWidth() {
	checkWidget();
	return minWidth;
}

/**
 * Returns the minimum height of the content control.
 *
 * @return the minimum height
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public int getMinHeight() {
	checkWidget();
	return minHeight;
}

/**
 * Get the content that is being scrolled.
 * 
 * @return the control displayed in the content area
 */
public Control getContent() {
	//checkWidget();
	return content;
}

/**
 * Returns <code>true</code> if the receiver automatically scrolls to a focused child control 
 * to make it visible. Otherwise, returns <code>false</code>.
 * 
 * @return a boolean indicating whether focused child controls are automatically scrolled into the viewport
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.4
 */
public boolean getShowFocusedControl() {
	checkWidget();
	return showFocusedControl;
}

void hScroll() {
	if (content == null) return;
	Point location = content.getLocation ();
	ScrollBar hBar = getHorizontalBar ();
	int hSelection = hBar.getSelection ();
	content.setLocation (-hSelection, location.y);
}
boolean needHScroll(Rectangle contentRect, boolean vVisible) {
	ScrollBar hBar = getHorizontalBar();
	if (hBar == null) return false;
	
	Rectangle hostRect = getBounds();
	int border = getBorderWidth();
	hostRect.width -= 2*border;
	ScrollBar vBar = getVerticalBar();
	if (vVisible && vBar != null) hostRect.width -= vBar.getSize().x;
	
	if (!expandHorizontal && contentRect.width > hostRect.width) return true;
	if (expandHorizontal && minWidth > hostRect.width) return true;
	return false;
}

boolean needVScroll(Rectangle contentRect, boolean hVisible) {
	ScrollBar vBar = getVerticalBar();
	if (vBar == null) return false;
	
	Rectangle hostRect = getBounds();
	int border = getBorderWidth();
	hostRect.height -= 2*border;
	ScrollBar hBar = getHorizontalBar();
	if (hVisible && hBar != null) hostRect.height -= hBar.getSize().y;
	
	if (!expandVertical && contentRect.height > hostRect.height) return true;
	if (expandVertical && minHeight > hostRect.height) return true;
	return false;
}

/**
 * Return the point in the content that currently appears in the top left 
 * corner of the scrolled composite.
 * 
 * @return the point in the content that currently appears in the top left 
 * corner of the scrolled composite.  If no content has been set, this returns
 * (0, 0).
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 */
public Point getOrigin() {
	checkWidget();
	if (content == null) return new Point(0, 0);
	Point location = content.getLocation();
	return new Point(-location.x, -location.y);
}
/**
 * Scrolls the content so that the specified point in the content is in the top 
 * left corner.  If no content has been set, nothing will occur.  
 * 
 * Negative values will be ignored.  Values greater than the maximum scroll 
 * distance will result in scrolling to the end of the scrollbar.
 *
 * @param origin the point on the content to appear in the top left corner 
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_INVALID_ARGUMENT - value of origin is outside of content
 * </ul>
 * @since 2.0
 */
public void setOrigin(Point origin) {
	setOrigin(origin.x, origin.y);
}
/**
 * Scrolls the content so that the specified point in the content is in the top 
 * left corner.  If no content has been set, nothing will occur.  
 * 
 * Negative values will be ignored.  Values greater than the maximum scroll 
 * distance will result in scrolling to the end of the scrollbar.
 *
 * @param x the x coordinate of the content to appear in the top left corner 
 * 
 * @param y the y coordinate of the content to appear in the top left corner 
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 */
public void setOrigin(int x, int y) {
	checkWidget();
	if (content == null) return;
	ScrollBar hBar = getHorizontalBar ();
	if (hBar != null) {
		hBar.setSelection(x);
		x = -hBar.getSelection ();
	} else {
		x = 0;
	}
	ScrollBar vBar = getVerticalBar ();
	if (vBar != null) {
		vBar.setSelection(y);
		y = -vBar.getSelection ();
	} else {
		y = 0;
	}
	content.setLocation(x, y);
}
/**
 * Set the Always Show Scrollbars flag.  True if the scrollbars are 
 * always shown even if they are not required.  False if the scrollbars are only 
 * visible when some part of the composite needs to be scrolled to be seen.
 * The H_SCROLL and V_SCROLL style bits are also required to enable scrollbars in the 
 * horizontal and vertical directions.
 * 
 * @param show true to show the scrollbars even when not required, false to show scrollbars only when required
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setAlwaysShowScrollBars(boolean show) {
	checkWidget();
	if (show == alwaysShowScroll) return;
	alwaysShowScroll = show;
	ScrollBar hBar = getHorizontalBar ();
	if (hBar != null && alwaysShowScroll) hBar.setVisible(true);
	ScrollBar vBar = getVerticalBar ();
	if (vBar != null && alwaysShowScroll) vBar.setVisible(true);
	layout(false);
}

/**
 * Set the content that will be scrolled.
 * 
 * @param content the control to be displayed in the content area
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setContent(Control content) {
	checkWidget();
	if (this.content != null && !this.content.isDisposed()) {
		this.content.removeListener(SWT.Resize, contentListener);
		this.content.setBounds(new Rectangle(-200, -200, 0, 0));	
	}
	
	this.content = content;
	ScrollBar vBar = getVerticalBar ();
	ScrollBar hBar = getHorizontalBar ();
	if (this.content != null) {
		if (vBar != null) {
			vBar.setMaximum (0);
			vBar.setThumb (0);
			vBar.setSelection(0);
		}
		if (hBar != null) {
			hBar.setMaximum (0);
			hBar.setThumb (0);
			hBar.setSelection(0);
		}
		content.setLocation(0, 0);
		layout(false);
		this.content.addListener(SWT.Resize, contentListener);
	} else {
		if (hBar != null) hBar.setVisible(alwaysShowScroll);
		if (vBar != null) vBar.setVisible(alwaysShowScroll);
	}
}
/**
 * Configure the ScrolledComposite to resize the content object to be as wide as the 
 * ScrolledComposite when the width of the ScrolledComposite is greater than the
 * minimum width specified in setMinWidth.  If the ScrolledComposite is less than the
 * minimum width, the content will not be resized and instead the horizontal scroll bar will be
 * used to view the entire width.
 * If expand is false, this behaviour is turned off.  By default, this behaviour is turned off.
 * 
 * @param expand true to expand the content control to fill available horizontal space
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setExpandHorizontal(boolean expand) {
	checkWidget();
	if (expand == expandHorizontal) return;
	expandHorizontal = expand;
	layout(false);
}
/**
 * Configure the ScrolledComposite to resize the content object to be as tall as the 
 * ScrolledComposite when the height of the ScrolledComposite is greater than the
 * minimum height specified in setMinHeight.  If the ScrolledComposite is less than the
 * minimum height, the content will not be resized and instead the vertical scroll bar will be
 * used to view the entire height.
 * If expand is false, this behaviour is turned off.  By default, this behaviour is turned off.
 * 
 * @param expand true to expand the content control to fill available vertical space
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setExpandVertical(boolean expand) {
	checkWidget();
	if (expand == expandVertical) return;
	expandVertical = expand;
	layout(false);
}
/**
 * Sets the layout which is associated with the receiver to be
 * the argument which may be null.
 * <p>
 * Note: No Layout can be set on this Control because it already
 * manages the size and position of its children.
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
 * Specify the minimum height at which the ScrolledComposite will begin scrolling the
 * content with the vertical scroll bar.  This value is only relevant if  
 * setExpandVertical(true) has been set.
 * 
 * @param height the minimum height or 0 for default height
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMinHeight(int height) {
	setMinSize(minWidth, height);
}
/**
 * Specify the minimum width and height at which the ScrolledComposite will begin scrolling the
 * content with the horizontal scroll bar.  This value is only relevant if  
 * setExpandHorizontal(true) and setExpandVertical(true) have been set.
 * 
 * @param size the minimum size or null for the default size
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMinSize(Point size) {
	if (size == null) {
		setMinSize(0, 0);
	} else {
		setMinSize(size.x, size.y);
	}
}
/**
 * Specify the minimum width and height at which the ScrolledComposite will begin scrolling the
 * content with the horizontal scroll bar.  This value is only relevant if  
 * setExpandHorizontal(true) and setExpandVertical(true) have been set.
 * 
 * @param width the minimum width or 0 for default width
 * @param height the minimum height or 0 for default height
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMinSize(int width, int height) {
	checkWidget();
	if (width == minWidth && height == minHeight) return;
	minWidth = Math.max(0, width);
	minHeight = Math.max(0, height);
	layout(false);
}
/**
 * Specify the minimum width at which the ScrolledComposite will begin scrolling the
 * content with the horizontal scroll bar.  This value is only relevant if  
 * setExpandHorizontal(true) has been set.
 * 
 * @param width the minimum width or 0 for default width
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMinWidth(int width) {
	setMinSize(width, minHeight);
}

/**
 * Configure the receiver to automatically scroll to a focused child control
 * to make it visible.
 * 
 * If show is <code>false</code>, show a focused control is off.  
 * By default, show a focused control is off.
 * 
 * @param show <code>true</code> to show a focused control.
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.4
 */
public void setShowFocusedControl(boolean show) {
	checkWidget();
	if (showFocusedControl == show) return;
	Display display = getDisplay();
	display.removeFilter(SWT.FocusIn, filter);
	showFocusedControl = show;
	if (!showFocusedControl) return;
	display.addFilter(SWT.FocusIn, filter);
	Control control = display.getFocusControl();
	if (contains(control)) showControl(control);
}

/**
 * Scrolls the content of the receiver so that the control is visible.
 *
 * @param control the control to be shown
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the control is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public void showControl(Control control) {
	checkWidget ();
	if (control == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (control.isDisposed ()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (!contains(control)) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	
	Rectangle itemRect = getDisplay().map(control.getParent(), this, control.getBounds());
	Rectangle area = getClientArea();
	Point origin = getOrigin();
	if (itemRect.x < 0) {
		origin.x = Math.max(0, origin.x + itemRect.x);
	} else {
		if (area.width < itemRect.x + itemRect.width) origin.x = Math.max(0, origin.x + itemRect.x + Math.min(itemRect.width, area.width) - area.width);
	}
	if (itemRect.y < 0) {
		origin.y = Math.max(0, origin.y + itemRect.y);
	} else {
		if (area.height < itemRect.y + itemRect.height) origin.y = Math.max(0, origin.y + itemRect.y + Math.min(itemRect.height, area.height) - area.height);
	}
	setOrigin(origin);
}

void vScroll() {
	if (content == null) return;
	Point location = content.getLocation ();
	ScrollBar vBar = getVerticalBar ();
	int vSelection = vBar.getSelection ();
	content.setLocation (location.x, -vSelection);
}
}
