package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class CustomScrollBar extends ScrollBarDelegate {

	private ScrollBar scrollBar;
	private boolean  visible;
	private int increment, minumum, maximum, selection, thumb, pageThumb;
	private int pageIncrement;
	private int style;
	private DefaultScrollBarRenderer renderer;
	private boolean enabled = true;
	private boolean mouseEntered;
	
	

	public CustomScrollBar(Scrollable parent, ScrollBar scrollBar, int checkStyle) {
		this.style = checkStyle(checkStyle);
		this.scrollBar = scrollBar;
		renderer = new DefaultScrollBarRenderer(scrollBar);
		
		scrollBar.state &= ~Widget.DISABLED;
		
		var p = scrollBar.getParent();
		
		
		Listener listener = event -> {
			switch (event.type) {
			case SWT.MouseDown -> onMouseDown(event);
			case SWT.MouseMove -> onMouseMove(event);
			case SWT.MouseUp -> onMouseUp(event);
			case SWT.MouseEnter -> onMouseEnter();
			case SWT.MouseExit -> onMouseExit();
			}
		};
		
		p.addListener(SWT.MouseDown, listener);
		p.addListener(SWT.MouseMove, listener);
		p.addListener(SWT.MouseUp, listener);
		p.addListener(SWT.MouseEnter, listener);
		p.addListener(SWT.MouseExit, listener);
		
	}

	private void onMouseExit() {
		mouseEntered = false;
	}

	private void onMouseEnter() {
		mouseEntered = true;
	}

	private void onMouseUp(Event event) {
	}

	private void onMouseMove(Event event) {
	}

	private void onMouseDown(Event event) {
		if (event.button != 1) {
			return;
		}
		
		if(!getBounds().contains(event.x,event.y)) {
			return;
		}

		int selection = renderer.getSelectionOfPosition(event.x, event.y);
		setSelection(selection, true);
		
		


	}

	static int checkStyle (int style) {
		return Widget.checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
	}
	
	@Override
	boolean getEnabled() {
		return enabled;
	}
	
	@Override
	protected boolean isEnabled() {
		return getEnabled () && getParent().isEnabled ();
	}
	
	@Override
	protected void setEnabled(boolean enabled) {
		this.enabled  = enabled;

	}
	
	@Override
	protected boolean getVisible() {
		return visible;
	}

	@Override
	protected boolean isVisible() {
		return getVisible () && getParent().isVisible ();
	}
	
	@Override
	protected void setVisible(boolean visible) {
		this.visible = visible;

	}

	@Override
	int getIncrement() {
		return increment;
	}

	@Override
	int getMaximum() {
		return maximum;
	}

	@Override
	int getMinimum() {
		return minumum;
	}

	@Override
	int getPageIncrement() {
		return pageIncrement;
	}

	@Override
	protected Scrollable getParent() {
		return scrollBar.getParent();
	}

	@Override
	protected int getSelection() {
		return selection;
	}

	@Override
	protected Point getSize() {
		var b = getParent().getBounds();
		return new Point(10, b.height);
	}

	@Override
	protected int getThumb() {
		return thumb;
	}

	@Override
	protected Rectangle getThumbBounds() {
		
		var b = getParent().getBounds();
		
		return new Rectangle(b.x + b.width - 10, b.y , 10, b.height);
	}

	@Override
	protected Rectangle getThumbTrackBounds() {
		var b = getParent().getBounds();
		
		return new Rectangle(b.x + b.width - 10, b.y , 10, b.height);
	}


	@Override
	protected void setIncrement(int value) {
		this.increment = value;

	}

	@Override
	protected void setMaximum(int value) {
		this.maximum = value;

	}

	@Override
	protected void setMinimum(int value) {
		this.minumum = value;

	}

	@Override
	protected void setPageIncrement(int value) {

		this.pageIncrement = value;
		
	}

	@Override
	protected void setSelection(int selection) {
		setSelection(selection,false);
	}

	private void setSelection(int selection, boolean sendEvent) {
		int min = getMinimum();
		int max = getMaximum();
		int thumb = getThumb();

		// Ensure selection does not exceed max - thumb
		int newValue = Math.max(min, Math.min(selection, max - thumb));

		if (newValue != this.selection) {
			this.selection = newValue;
			if (sendEvent) {
				scrollBar.sendEvent(SWT.Selection, new Event());
			}
		}
	}
	
	
	@Override
	protected void setThumb(int value) {
		this.thumb = value;

	}

	@Override
	protected void setValues(int selection, int minimum, int maximum, int thumb, int increment, int pageIncrement) {

		this.selection = selection;
		this.minumum = minimum;
		this.maximum = maximum;
		this.thumb = thumb;
		this.increment = increment;
		this.pageIncrement = pageIncrement;
		
		
	}

	@Override
	protected void drawBar(GC gc) {
		renderer.paint(gc);
	}

	@Override
	Rectangle getBounds() {
		
		var b = getParent().getBounds();
		
		if(scrollBar.isVertical())
			return new Rectangle(b.x + b.width - 10, b.y, 10, b.height);
		
		if(scrollBar.isHorizontal()) {
			return new Rectangle(b.x, b.y + b.height - 10, b.width, 10);
		}
		
		
		return null;
	}

}
