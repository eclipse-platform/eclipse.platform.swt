package org.eclipse.swt.custom;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
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
 * <code><pre>
 * public static void main (String [] args) {
 *         Display display = new Display ();
 *         Color red = display.getSystemColor(SWT.COLOR_RED);
 *         Shell shell = new Shell (display);
 *         shell.setLayout(new FillLayout());
 *         ScrolledComposite sc = new ScrolledComposite(shell, SWT.H_SCROLL | SWT.V_SCROLL);
 *         Composite c = new Composite(sc, SWT.NONE);
 *         c.setBackground(red);
 *         sc.setContent(c);
 *         GridLayout layout = new GridLayout();
 *         layout.numColumns = 5;
 *         c.setLayout(layout);
 *         for (int i = 0; i < 10; i++) {
 *                 Button b1 = new Button(c, SWT.PUSH);
 *                 b1.setText("button "+i);
 *         }
 *         Point pt = c.computeSize(SWT.DEFAULT, SWT.DEFAULT);
 *         c.setSize(pt);
 *         shell.open ();
 *         while (!shell.isDisposed ()) {
 *             if (!display.readAndDispatch ()) display.sleep ();
 *         }
 *         display.dispose ();
 * }
 * </pre></code>
 * 
 * 2) The second way imitates the way a browser would work.  Set the minimum size of
 * the control and the ScrolledComposite will show scroll bars if the visible area is 
 * less than the minimum size of the control and it will expand the size of the control 
 * if the visible area is greater than the minimum size.  This requires invoking 
 * both setMinWidth(), setMinHeight() and setExpandHorizontal(), setExpandVertical().
 * 
 * <code><pre>
 * public static void main (String [] args) {
 *         Display display = new Display ();
 *         Color red = display.getSystemColor(SWT.COLOR_RED);
 *         Shell shell = new Shell (display);
 *         shell.setLayout(new FillLayout());
 *         ScrolledComposite sc = new ScrolledComposite(shell, SWT.H_SCROLL | SWT.V_SCROLL);
 *         Composite c = new Composite(sc, SWT.NONE);
 *         c.setBackground(red);
 *         sc.setContent(c);
 *         GridLayout layout = new GridLayout();
 *         layout.numColumns = 5;
 *         c.setLayout(layout);
 *         for (int i = 0; i < 10; i++) {
 *                 Button b1 = new Button(c, SWT.PUSH);
 *                 b1.setText("button "+i);
 *         }
 *         Point pt = c.computeSize(SWT.DEFAULT, SWT.DEFAULT);
 *         sc.setExpandHorizontal(true);
 *         sc.setExpandVertical(true);
 *         sc.setMinWidth(pt.x);
 *         sc.setMinHeight(pt.y);
 *         shell.open ();
 *         while (!shell.isDisposed ()) {
 *             if (!display.readAndDispatch ()) display.sleep ();
 *         }
 *         display.dispose ();
 * }
 * </pre></code>
 * 
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

private void init() {
	ScrollBar vBar = getVerticalBar ();
	if (vBar != null) {
		vBar.setMaximum (0);
		vBar.setThumb (0);
		vBar.setSelection(0);
	}
	ScrollBar hBar = getHorizontalBar ();
	if (hBar != null) {
		hBar.setMaximum (0);
		hBar.setThumb (0);
		hBar.setSelection(0);
	}
	if (content != null) {
		content.setLocation(0, 0);
	}
	layout();
}

public void layout(boolean changed) {
	if (content == null) return;
	Rectangle contentRect = content.getBounds();
	ScrollBar hBar = getHorizontalBar ();
	ScrollBar vBar = getVerticalBar ();
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
			if (hPage <= 0) {
				hSelection = 0;
				hBar.setSelection(0);
			}
			contentRect.x = -hSelection;
		}
	}

	if (vBar != null) {
		vBar.setMaximum (contentRect.height);
		vBar.setThumb (Math.min (contentRect.height, hostRect.height));
		int vPage = contentRect.height - hostRect.height;
		int vSelection = vBar.getSelection ();
		if (vSelection >= vPage) {
			if (vPage <= 0) {
				vSelection = 0;
				vBar.setSelection(0);
			}
			contentRect.y = -vSelection;
		}
	}
	
	content.setBounds (contentRect);
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

private void resize() {
	if (inResize) return;
	inResize = true;
	layout();
	inResize = false;
}

/**
 * Set the Always Show Scrollbars flag.  True if the scrollbars are 
 * always shown even if they are not required.  False if the scrollbars are only 
 * visible when some part of the composite needs to be scrolled to be seen.
 * The H_SCROLL and V_SCROLL style bits are also required to enable scrollbars in the 
 * horizontal and vertical directions.
 */
public void setAlwaysShowScrollBars(boolean show) {
	if (show == alwaysShowScroll) return;
	alwaysShowScroll = show;
	ScrollBar hBar = getHorizontalBar ();
	if (hBar != null && alwaysShowScroll) hBar.setVisible(true);
	ScrollBar vBar = getVerticalBar ();
	if (vBar != null && alwaysShowScroll) vBar.setVisible(true);
	init();
}

/**
 * Set the content that will be scrolled.
 */
public void setContent(Control content) {
	if (this.content != null && !this.content.isDisposed()) {
		this.content.removeListener(SWT.Resize, contentListener);
		this.content.setBounds(new Rectangle(-200, -200, 0, 0));	
	}
	
	this.content = content;
	if (this.content != null) {
		init();
		this.content.addListener(SWT.Resize, contentListener);
	} else {
		ScrollBar hBar = getHorizontalBar ();
		if (hBar != null) hBar.setVisible(alwaysShowScroll);
		ScrollBar vBar = getVerticalBar ();
		if (vBar != null) vBar.setVisible(alwaysShowScroll);
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
	init();
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
	init();
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
	init();
}
/**
 * Specify the minimum width at which the ScrolledComposite will begin scrolling the
 * content with the horizontal scroll bar.  This value is only relevant if  
 * setExpandHorizontal(true) has been set.
 */

public void setMinWidth(int width) {
	if (width == minWidth) return;
	minWidth = Math.max(0, width);
	init();
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
