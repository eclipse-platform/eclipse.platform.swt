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
package org.eclipse.swt.layout;

import org.eclipse.swt.*;

/**
 * <code>GridData</code> is the layout data object associated with 
 * <code>GridLayout</code>. To set a <code>GridData</code> object into a 
 * control, you use the <code>setLayoutData ()</code> method. 
 * <p>
 * There are two ways to create a <code>GridData</code> object with certain 
 * fields set. The first is to set the fields directly, like this:
 * <pre>
 * 		GridData gridData = new GridData();
 * 		gridData.horizontalAlignment = GridData.FILL;
 * 		gridData.grabExcessHorizontalSpace = true;
 * 		button1.setLayoutData(gridData);
 * </pre>
 * The second is to take advantage of convenience style bits defined 
 * by <code>GridData</code>:
 * <pre>
 *      button1.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
 * </pre>
 * </p>
 * <p>
 * NOTE: Do not reuse <code>GridData</code> objects. Every control in a 
 * <code>Composite</code> that is managed by a <code>GridLayout</code>
 * must have a unique <code>GridData</code> object. If the layout data 
 * for a control in a <code>GridLayout</code> is null at layout time, 
 * a unique <code>GridData</code> object is created for it.
 * </p>
 * 
 * @see GridLayout
 */
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
	public static final int BEGINNING = SWT.BEGINNING;
	public static final int CENTER = 2;
	public static final int END = 3;
	public static final int FILL = SWT.FILL;

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
	/**
	 * FILL_VERTICAL = VERTICAL_ALIGN_FILL | GRAB_VERTICAL
	 */	
	public static final int FILL_VERTICAL = VERTICAL_ALIGN_FILL | GRAB_VERTICAL;
	/**
	 * FILL_HORIZONTAL = HORIZONTAL_ALIGN_FILL | GRAB_HORIZONTAL
	 */	
	public static final int FILL_HORIZONTAL = HORIZONTAL_ALIGN_FILL | GRAB_HORIZONTAL;
	/**
	 * FILL_BOTH = FILL_VERTICAL | FILL_HORIZONTAL
	 */	
	public static final int FILL_BOTH = FILL_VERTICAL | FILL_HORIZONTAL;

	// Private
	int childIndex;
	boolean isItemData = true;
	int hSpan;
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

/*
* @since 3.0
*/
public GridData(int horizontalAlignment, int verticalAlignment, boolean grabExcessHorizontalSpace, boolean grabExcessVerticalSpace) {
	this(horizontalAlignment, verticalAlignment, grabExcessHorizontalSpace, grabExcessVerticalSpace, 1, 1);
}

/*
* @since 3.0
*/
public GridData(int horizontalAlignment, int verticalAlignment, boolean grabExcessHorizontalSpace, boolean grabExcessVerticalSpace, int horizontalSpan, int verticalSpan) {
	super();
	this.horizontalAlignment = horizontalAlignment;
	this.verticalAlignment = verticalAlignment;
	this.grabExcessHorizontalSpace = grabExcessHorizontalSpace;
	this.grabExcessVerticalSpace = grabExcessVerticalSpace;
	this.horizontalSpan = horizontalSpan;
	this.verticalSpan = verticalSpan;
}

/*
* @since 3.0
*/
public GridData (int width, int height) {
	super();
	this.widthHint = width;
	this.heightHint = height;
}
boolean isItemData() {
	return isItemData;
}
boolean isSpacerData() {
	return !isItemData;
}
}
