package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;

abstract class ScrollBarDelegate {


	abstract boolean getEnabled() ;

	abstract int getIncrement() ;

	abstract int getMaximum() ;

	abstract int getMinimum() ;

	abstract int getPageIncrement() ;


	 abstract int getSelection();

	 abstract Point getSize();

	 abstract int getThumb();

	 abstract Rectangle getThumbBounds();

	 abstract Rectangle getThumbTrackBounds();

	 abstract boolean getVisible();

	 abstract boolean isEnabled();

	 abstract boolean isVisible();

	abstract void setEnabled(boolean enabled);

	abstract void setIncrement(int value);

	abstract void setMaximum(int value);

	abstract void setMinimum(int value);

	abstract void setPageIncrement(int value);

	abstract void setSelection(int selection);

	abstract void setThumb(int value);

	abstract void setValues(int selection, int minimum, int maximum, int thumb, int increment,
			int pageIncrement);

	abstract void setVisible(boolean visible);

	abstract Scrollable getParent();

	void destroyWidget() {
		// TODO Auto-generated method stub
		
	}

	void releaseHandle() {
		// TODO Auto-generated method stub
		
	}

	void releaseParent() {
		// TODO Auto-generated method stub
		
	}

	abstract void drawBar(GC gc);

	abstract Rectangle getBounds();

}
