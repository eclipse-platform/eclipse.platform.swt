package org.eclipse.swt.custom;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/*
 * A ScrolledComposite provides scrollbars and will scroll its content when the user
 * uses the scrollbars.
 *
 * <p>If the ScrolledComposite can be configured to stretch the content to be as big as the
 * ScrolledComposite when the composite is resized to a size larger than the minimum width 
 * or minimum height and to provide scrolls when the ScrolledComposite is smaller than the 
 * minimum width and minimum height.  Refer to the methods setExpandHorizontal,
 * setExpandVertical, setMinWidth and setMinHeight.
 *
 * <p>
 * <dl>
 * <dt><b>Styles:</b><dd>H_SCROLL, V_SCROLL
 * </dl>
 */
public class ScrolledComposite extends Composite {

	private Control content;
	private Listener contentListener;
	
	private int minHeight = 0;
	private int minWidth = 0;
	private boolean expandHorizontal = false;
	private boolean expandVertical = false;
	private boolean alwaysShowScroll = false;
	private boolean inResize = false;
	
public ScrolledComposite(Composite parent, int style) {
	super(parent, checkStyle(style));
	
	ScrollBar hBar = getHorizontalBar ();
	if (hBar != null) {
		hBar.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				hScroll();
			}
		});
	}
	
	ScrollBar vBar = getVerticalBar ();
	if (vBar != null) {
		vBar.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				vScroll();
			}
		});
	}
	
	addListener (SWT.Resize,  new Listener () {
		public void handleEvent (Event e) {
			resize();
		}
	});
	
	contentListener = new Listener() {
		public void handleEvent(Event e) {
			if (e.type != SWT.Resize) return;
			resize();
		}
	};
}

private static int checkStyle (int style) {
	int mask = SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER;
	return style & mask;
}
/**
 * Get the content that is being scrolled.
 */
public Control getContent() {
	return content;
}
private void hScroll() {
	if (content == null) return;
	Point location = content.getLocation ();
	ScrollBar hBar = getHorizontalBar ();
	int hSelection = hBar.getSelection ();
	content.setLocation (-hSelection, location.y);
}
private boolean needHScroll(Rectangle contentRect, boolean vVisible) {
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
private boolean needVScroll(Rectangle contentRect, boolean hVisible) {
	ScrollBar vBar = getVerticalBar();
	if (vBar == null) return false;
	
	Rectangle hostRect = getBounds();
	int border = getBorderWidth();
	hostRect.height -= 2*border;
	ScrollBar hBar = getHorizontalBar();
	if (hVisible && hBar != null) hostRect.height -= hBar.getSize().y;
	
	if (!expandHorizontal && contentRect.height > hostRect.height) return true;
	if (expandHorizontal && minHeight > hostRect.height) return true;
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
	return alwaysShowScroll;
}

/**
 * Set the Always Show Scrollbars flag.  True if the scrollbars are 
 * always shown even if they are not required.  False if the scrollbars are only 
 * visible when some part of the composite needs to be scrolled to be seen.
 * The H_SCROLL and V_SCROLL style bits are also required to enable scrollbars in the 
 * horizontal and vertical directions.
 */
public void setAlwaysShowScrollBars(boolean show) {
	alwaysShowScroll = show;
	resize();
}

private void resize() {
	if (content == null || inResize) return;
	inResize = true;
	ScrollBar hBar = getHorizontalBar ();
	ScrollBar vBar = getVerticalBar ();
	Rectangle contentRect = content.getBounds();
	contentRect.x = contentRect.y = 0;
	
	if (!alwaysShowScroll) {
		boolean hVisible = needHScroll(contentRect, false);
		boolean vVisible = needVScroll(contentRect, hVisible);
		if (!hVisible && vVisible) hVisible = needHScroll(contentRect, vVisible);
		if (hBar != null) hBar.setVisible(hVisible);
		if (vBar != null) vBar.setVisible(vVisible);
	}

	Rectangle hostRect = getClientArea();
	if (expandHorizontal) {
		contentRect.width = Math.max(minWidth, hostRect.width);	
	}
	if (expandVertical) {
		contentRect.height = Math.max(minHeight, hostRect.height);
	}

	if (hBar != null) {
		hBar.setMaximum (contentRect.width);
		hBar.setThumb (Math.min (contentRect.width, hostRect.width));
		int hPage = contentRect.width - hostRect.width;
		int hSelection = hBar.getSelection ();
		if (hSelection >= hPage) {
			if (hPage <= 0) hSelection = 0;
			contentRect.x = -hSelection;
		}
	}

	if (vBar != null) {
		vBar.setMaximum (contentRect.height);
		vBar.setThumb (Math.min (contentRect.height, hostRect.height));
		int vPage = contentRect.height - hostRect.height;
		int vSelection = vBar.getSelection ();
		if (vSelection >= vPage) {
			if (vPage <= 0) vSelection = 0;
			contentRect.y = -vSelection;
		}
	}
	
	content.setBounds (contentRect);
	inResize = false;
}

/**
 * Set the content that will be scrolled.
 */
public void setContent(Control content) {
	if (this.content != null && !this.content.isDisposed()) {
		this.content.removeListener(SWT.Resize, contentListener);
		content.setBounds(new Rectangle(-200, -200, 0, 0));	
	}
	
	this.content = content;
	if (this.content != null) {
		this.content.addListener(SWT.Resize, contentListener);
		resize();	
	}
}
/**
 * Configure the ScrolledComposite to resize the content object to be as wide as the 
 * ScrolledComposite when the width of the ScrolledComposite is greater than the
 * minimum width specified in setMinWidth.  If the ScrolledComposite is less than the
 * minimum width, the content will not resized and instead the horizontal scroll bar will be
 * used to view the entire width.
 * If expand is false, this behaviour is turned off.  By default, this behaviour is turned off.
 */
public void setExpandHorizontal(boolean expand) {
	if (expand == expandHorizontal) return;
	expandHorizontal = expand;
	resize();
}
/**
 * Configure the ScrolledComposite to resize the content object to be as tall as the 
 * ScrolledComposite when the height of the ScrolledComposite is greater than the
 * minimum height specified in setMinHeight.  If the ScrolledComposite is less than the
 * minimum height, the content will not resized and instead the vertical scroll bar will be
 * used to view the entire height.
 * If expand is false, this behaviour is turned off.  By default, this behaviour is turned off.
 */
public void setExpandVertical(boolean expand) {
	if (expand == expandVertical) return;
	expandVertical = expand;
	resize();
}
public void setLayout (Layout layout) {
	// do not allow a layout to be set on this class because layout is being handled by the resize listener
	return;
}
/**
 * Specify the minimum height at which the ScrolledComposite will begin scrolling the
 * content with the vertical scroll bar.  This value is only relevant if  
 * setExpandVertical(true) has been set.
 */
public void setMinHeight(int height) {
	if (height == minHeight) return;
	minHeight = Math.max(0, height);
	resize();
}
/**
 * Specify the minimum width at which the ScrolledComposite will begin scrolling the
 * content with the horizontal scroll bar.  This value is only relevant if  
 * setExpandHorizontal(true) has been set.
 */

public void setMinWidth(int width) {
	if (width == minWidth) return;
	minWidth = Math.max(0, width);
	resize();
}

private void vScroll() {
	Control[] children = getChildren();
	if (children.length == 0) return;
	Control content = children[0];
	Point location = content.getLocation ();
	ScrollBar vBar = getVerticalBar ();
	int vSelection = vBar.getSelection ();
	content.setLocation (location.x, -vSelection);
}
}
