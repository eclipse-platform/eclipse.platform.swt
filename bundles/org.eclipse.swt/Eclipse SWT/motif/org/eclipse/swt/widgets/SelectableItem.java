package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * This class implements common behavior of TreeItem and TableItem.
 */
abstract class SelectableItem extends Item {
	protected static final int CHECKBOX_PADDING = 1;	// Space behind check box, before item image or text
		
	private SelectableItemWidget parent;				// parent widget of the receiver
	private boolean isSelected = false;					// item selection state
	private boolean isChecked = false;					// item checked state. Can be one of checked and unchecked
	private boolean isGrayed = false;					// item grayed state. The gray state is combined with the 
														// checked state to create gray checked and gray unchecked.
/**
 * Create a new instance of the receiver.
 * @param parent - widget the receiver is created in 
 * @param style - widget style. see Widget class for details
 */
SelectableItem(SelectableItemWidget parent, int style) {
	super(parent, style);
	setParent(parent);	
}
public void dispose() {
	if (!isValidWidget ()) return;
	super.dispose();
	doDispose();
}
void doDispose() {
	setParent(null);
}
/**
 * Draw the check box of the receiver at 'position' using 'gc'.
 * @param gc - GC to draw on. 
 * @param destinationPosition - position on the GC to draw at.
 * @return Answer the position where drawing stopped.
 */
Point drawCheckbox(GC gc, Point position) {
	SelectableItemWidget parent = getSelectableParent();
	Image image;
	Point imageExtent;
	Rectangle imageBounds;
	int imageOffset;
	int xInset;
	int yInset;

	if (getGrayed() == true) {
		image = parent.getGrayUncheckedImage();
	}
	else {
		image = parent.getUncheckedImage();
	}
	if (image != null) {
		imageExtent = parent.getCheckBoxExtent();
		imageOffset = (parent.getItemHeight() - imageExtent.y) / 2;
		gc.drawImage(image, position.x, position.y + imageOffset);
		if (getChecked() == true) {
			image = parent.getCheckMarkImage();
			imageBounds = image.getBounds();
			xInset = (imageExtent.x - imageBounds.width) / 2;
			yInset = (imageExtent.y - imageBounds.height) / 2;
			gc.drawImage(image, position.x + xInset, position.y + imageOffset + yInset);
		}
		position.x += imageExtent.x;
	}
	position.x += CHECKBOX_PADDING;									// leave extra space behind check box	
	return position;
}
void drawInsertMark(GC gc, Point position) {
	SelectableItemWidget parent = getSelectableParent();
	Point selectionExtent = getSelectionExtent();
	final int markerWidth = getInsertMarkWidth();
	int insertMarkYOffset = 0;

	if (selectionExtent == null) {
		return;
	}
	if (parent.isInsertAfter()) {
		insertMarkYOffset = selectionExtent.y - markerWidth;
	}
	gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
	gc.fillRectangle(position.x, position.y + insertMarkYOffset, selectionExtent.x, markerWidth);
	gc.setBackground(parent.getBackground());
}
/**
 * Answer the bounding rectangle of the item check box.
 * All points within this rectangle hit the check box.
 */
Rectangle getCheckboxBounds() {
	SelectableItemWidget parent = getSelectableParent();
	Point checkBoxExtent;
	int redrawPosition;
	Rectangle checkboxBounds = new Rectangle(0, 0, 0, 0);

	if (isCheckable() == true) {
		checkboxBounds.x = getCheckboxXPosition();
		redrawPosition = parent.getRedrawY(this);
		if (redrawPosition != -1) {
			checkboxBounds.y = redrawPosition;
		}
		checkBoxExtent = parent.getCheckBoxExtent();
		checkboxBounds.width = checkBoxExtent.x;
		checkboxBounds.height = checkBoxExtent.y;
		checkboxBounds.y += (parent.getItemHeight() - checkBoxExtent.y) / 2;
	}
	return checkboxBounds;
}
/**
 * Answer the x position of the item check box
 */
abstract int getCheckboxXPosition();
/**
 * Return whether or not the receiver is checked.
 * Always return false if the parent of the receiver does not
 * have the CHECK style.
 */
public boolean getChecked() {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	boolean checked = false;
	
	if (isCheckable() == true) {
		checked = isChecked;
	}
	return checked;
}
/**
 * Answer the display of the receiver's parent widget.
 */
public Display getDisplay() {
	SelectableItemWidget parent = getSelectableParent();

	if (parent == null) {
		error(SWT.ERROR_WIDGET_DISPOSED);
	}
	return parent.getDisplay();
}

/**
 * Gets the grayed state.
 * <p>
 * @return the item grayed state.
 *
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */
public boolean getGrayed () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	boolean grayed = false;
	
	if (isCheckable() == true) {
		grayed = isGrayed;
	}
	return grayed;
}
/**
 * Return the width in pixels of the line drawn to indicate the 
 * drop insert position during a drag and drop operation.
 */
int getInsertMarkWidth() {
	return 2;
}
/**
 * Answer the parent widget of the receiver.
 */
SelectableItemWidget getSelectableParent() {
	return parent;
}
/**
 * Answer the background color to use for drawing the 
 * selection rectangle.
 */
Color getSelectionBackgroundColor() {
	Display display = getSelectableParent().getDisplay();	

	return display.getSystemColor(SWT.COLOR_LIST_SELECTION);
}
/**
 * Return the size of the rectangle drawn to indicate the
 * selected state of the receiver.
 */
abstract Point getSelectionExtent();
/** 
 * Answer the foreground color to use for drawing the 
 * selection rectangle.
 */
Color getSelectionForegroundColor() {
	Display display = getSelectableParent().getDisplay();	

	return display.getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT);
}
/**
 * Return the x position of the selection rectangle
 */
abstract int getSelectionX();
/**
 * Answer whether 'posiiton' is inside the item check box.
 * @return
 *	true - item check box hit
 *	false - item check box not hit
 */
boolean isCheckHit(Point position) {
	boolean isCheckHit = false;
	
	if (isCheckable() == true) {
		isCheckHit = getCheckboxBounds().contains(position);
	}
	return isCheckHit;
}
/**
 * Return whether or not the receiver has a check box and can 
 * be checked.
 */
boolean isCheckable() {
	return (getSelectableParent().getStyle() & SWT.CHECK) != 0;
}
/**
 * Answer whether the receiver is selected.
 * @return 
 *	true - the receiver is selected
 * 	false - the receiver is not selected
 */
boolean isSelected() {
	return isSelected;
}
/**
 * Redraw the insert mark
 * @param yPosition - y position in the receiver's client area 
 *	where the item should be drawn.
 */
void redrawInsertMark(int yPosition) {
	SelectableItemWidget parent = getSelectableParent();
	Point selectionExtent = getSelectionExtent();
	int redrawHeight = getInsertMarkWidth();

	if (selectionExtent != null) {
		parent.redraw(getSelectionX(), yPosition, selectionExtent.x, redrawHeight, false);
		parent.redraw(getSelectionX(), yPosition + selectionExtent.y - redrawHeight, selectionExtent.x, redrawHeight, false);
	}	
}
/**
 * Redraw the selection
 * @param yPosition - y position in the receiver's client area 
 *	where the item should be drawn.
 */
void redrawSelection(int yPosition) {
	SelectableItemWidget parent = getSelectableParent();
	Point selectionExtent = getSelectionExtent();

	if (selectionExtent != null) {
		parent.redraw(getSelectionX(), yPosition, selectionExtent.x, selectionExtent.y, false);
	}
}
/**
 * Set the checked state to 'checked' if the parent of the 
 * receiver has the CHECK style.
 */
public void setChecked(boolean checked) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	SelectableItemWidget parent = getSelectableParent();
	Rectangle redrawRectangle = getCheckboxBounds();

	if (isCheckable() == true && isChecked != checked) {
		isChecked = checked;
		parent.redraw(
			redrawRectangle.x, redrawRectangle.y, 
			redrawRectangle.width, redrawRectangle.height, false);
	}	
}

/**
 * Sets the grayed state.
 * <p>
 * @param grayed the new grayed state.
 *
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */
public void setGrayed (boolean grayed) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	SelectableItemWidget parent = getSelectableParent();
	Rectangle redrawRectangle = getCheckboxBounds();

	if (isCheckable() == true && isGrayed != grayed) {
		isGrayed = grayed;
		parent.redraw(
			redrawRectangle.x, redrawRectangle.y, 
			redrawRectangle.width, redrawRectangle.height, false);
	}	
}

/**
 * Set the receiver's parent widget to 'parent'.
 */
void setParent(SelectableItemWidget parent) {
	this.parent = parent;
}
/**
 * Set whether the receiver is selected.
 * @param selected - true=the receiver is selected
 * 	false=the receiver is not selected
 */
void setSelected(boolean selected) {
	isSelected = selected;
}
}
