package org.eclipse.swt.layout;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public final class GridData {
	/**
	 * verticalAlignment specifies how controls will be positioned 
	 * vertically within a cell. 
	 *
	 * The default value is CENTER.
	 *
	 * Possible values are:
	 *
	 * BEGINNING: Position the control at the top of the cell
	 * CENTER: Position the control in the vertical center of the cell
	 * END: Position the control at the bottom of the cell
	 * FILL: Resize the control to fill the cell vertically
	 */
	public int verticalAlignment = CENTER;
	/**
	 * horizontalAlignment specifies how controls will be positioned 
	 * horizontally within a cell. 
	 *
	 * The default value is BEGINNING.
	 *
	 * Possible values are:
	 *
	 * BEGINNING: Position the control at the left of the cell
	 * CENTER: Position the control in the horizontal center of the cell
	 * END: Position the control at the right of the cell
	 * FILL: Resize the control to fill the cell horizontally
	 */
	public int horizontalAlignment = BEGINNING;
	/**
	 * widthHint specifies a minimum width for the column. A value of 
	 * SWT.DEFAULT indicates that no minimum width is specified.
	 *
	 * The default value is SWT.DEFAULT.
	 */
	public int widthHint = SWT.DEFAULT;
	/**
	 * heightHint specifies a minimum height for the row. A value of
	 * SWT.DEFAULT indicates that no minimum height is specified.
	 *
	 * The default value is SWT.DEFAULT.
	 */
	public int heightHint = SWT.DEFAULT;
	/**
	 * horizontalIndent specifies the number of pixels of indentation
	 * that will be placed along the left side of the cell.
	 *
	 * The default value is 0.
	 */
	public int horizontalIndent = 0;
	/**
	 * horizontalSpan specifies the number of column cells that the control
	 * will take up.
	 *
	 * The default value is 1.
	 */
	public int horizontalSpan = 1;
	/**
	 * verticalSpan specifies the number of row cells that the control
	 * will take up.
	 *
	 * The default value is 1.
	 */
	public int verticalSpan = 1;
	/**
	 * grabExcessHorizontalSpace specifies whether the cell will be made
	 * wide enough to fit the remaining horizontal space.
	 *
	 * The default value is false.
	 */	
	public boolean grabExcessHorizontalSpace = false;
	/**
	 * grabExcessVerticalSpace specifies whether the cell will be made
	 * tall enough to fit the remaining vertical space.
	 *
	 * The default value is false.
	 */	
	public boolean grabExcessVerticalSpace = false;

	// Alignment constants.
	public static final int BEGINNING = 1;
	public static final int CENTER = 2;
	public static final int END = 3;
	public static final int FILL = 4;

	// Style constants
	public static final int VERTICAL_ALIGN_BEGINNING =  1 << 1;
	public static final int VERTICAL_ALIGN_CENTER = 1 << 2;
	public static final int VERTICAL_ALIGN_END = 1 << 3;
	public static final int VERTICAL_ALIGN_FILL = 1 << 4;
	public static final int HORIZONTAL_ALIGN_BEGINNING =  1 << 5;
	public static final int HORIZONTAL_ALIGN_CENTER = 1 << 6;
	public static final int HORIZONTAL_ALIGN_END = 1 << 7;
	public static final int HORIZONTAL_ALIGN_FILL = 1 << 8;
	public static final int GRAB_HORIZONTAL = 1 << 9;
	public static final int GRAB_VERTICAL = 1 << 10;
	
	// Style convenience constants
	public static final int FILL_VERTICAL = VERTICAL_ALIGN_FILL | GRAB_VERTICAL;
	public static final int FILL_HORIZONTAL = HORIZONTAL_ALIGN_FILL | GRAB_HORIZONTAL;
	public static final int FILL_BOTH = FILL_VERTICAL | FILL_HORIZONTAL;

	// Private
	int childIndex;
	boolean isItemData = true;
public GridData() {
	super();
}
public GridData(int style) {
	super();

	if ((style & VERTICAL_ALIGN_BEGINNING) != 0)
		verticalAlignment = BEGINNING;
	if ((style & VERTICAL_ALIGN_CENTER) != 0)
		verticalAlignment = CENTER;
	if ((style & VERTICAL_ALIGN_FILL) != 0)
		verticalAlignment = FILL;
	if ((style & VERTICAL_ALIGN_END) != 0)
		verticalAlignment = END;
		
	if ((style & HORIZONTAL_ALIGN_BEGINNING) != 0)
		horizontalAlignment = BEGINNING;
	if ((style & HORIZONTAL_ALIGN_CENTER) != 0)
		horizontalAlignment = CENTER;
	if ((style & HORIZONTAL_ALIGN_FILL) != 0)
		horizontalAlignment = FILL;
	if ((style & HORIZONTAL_ALIGN_END) != 0)
		horizontalAlignment = END;
		
	if ((style & GRAB_HORIZONTAL) != 0)
		grabExcessHorizontalSpace = true;
	else
		grabExcessHorizontalSpace = false;
	if ((style & GRAB_VERTICAL) != 0)
		grabExcessVerticalSpace = true;
	else
		grabExcessVerticalSpace = false;
		
}
boolean isItemData() {
	return isItemData;
}
boolean isSpacerData() {
	return !isItemData;
}
}
