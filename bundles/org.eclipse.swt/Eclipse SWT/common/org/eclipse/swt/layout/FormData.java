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
 * Instances of this class are used to define the attachments 
 * of a control in a <code>FormLayout</code>. 
 * <p>
 * To set a <code>FormData</code> object into a control, you use the 
 * <code>setLayoutData ()</code> method. To define attachments for the 
 * <code>FormData</code>, set the fields directly, like this:
 * <pre>
 * 		FormData data = new FormData();
 * 		data.left = new FormAttachment(0,5);
 * 		data.right = new FormAttachment(100,-5);
 * 		button.setLayoutData(formData);
 * </pre>
 * </p>
 * <p>
 * <code>FormData</code> contains the <code>FormAttachments</code> for 
 * each edge of the control that the <code>FormLayout</code> uses to
 * determine the size and position of the control. <code>FormData</code>
 * objects also allow you to set the width and height of controls within
 * a <code>FormLayout</code>. 
 * </p>
 * 
 * @see FormLayout
 * @see FormAttachment
 * 
 * @since 2.0
 */
public final class FormData {
	/**
	 * height specifies the desired height in pixels
	 */
	public int height;
	/**
	 * width specifies the desired width in pixels
	 */
	public int width;
	/**
	 * left specifies the attachment of the left side of 
	 * the control.
	 */
	public FormAttachment left;
	/**
	 * right specifies the attachment of the right side of
	 * the control.
	 */
	public FormAttachment right;
	/**
	 * top specifies the attachment of the top of the control.
	 */
	public FormAttachment top;
	/**
	 * bottom specifies the attachment of the bottom of the
	 * control.
	 */
	public FormAttachment bottom;
	
	int cacheHeight, cacheWidth;
	FormAttachment cacheLeft, cacheRight, cacheTop, cacheBottom;
	boolean isVisited;
	
public FormData () {
	this (SWT.DEFAULT, SWT.DEFAULT);
}
	
public FormData (int width, int height) {
	this.width = width;
	this.height = height;
}

FormAttachment getBottomAttachment () {
	if (cacheBottom != null) return cacheBottom;
	if (isVisited) return cacheBottom = new FormAttachment (0, cacheHeight);
	if (bottom == null) {
		if (top == null) return cacheBottom = new FormAttachment (0, cacheHeight);
		return cacheBottom = getTopAttachment ().plus (cacheHeight);
	}
	if (bottom.control != null && bottom.control.isDisposed ()) bottom.control = null;
	if (bottom.control == null) return cacheBottom = bottom;
	isVisited = true;
	FormData bottomData = (FormData) bottom.control.getLayoutData ();
	FormAttachment topAttachment = bottomData.getTopAttachment ();
	FormAttachment bottomAttachment = bottomData.getBottomAttachment ();
	isVisited = false;
	switch (bottom.alignment) {
		case SWT.BOTTOM: return cacheBottom = bottomAttachment.plus (bottom.offset);
		case SWT.CENTER:
			FormAttachment bottomHeight = bottomAttachment.minus (topAttachment);
			return cacheBottom = bottomAttachment.minus (bottomHeight.minus (cacheHeight).divide (2));
	}
	return cacheBottom = topAttachment.plus (bottom.offset);	
}

FormAttachment getLeftAttachment () {
	if (cacheLeft != null) return cacheLeft;
	if (isVisited) return cacheLeft = new FormAttachment (0, 0);
	if (left == null) {
		if (right == null) return cacheLeft = new FormAttachment (0, 0);
		return cacheLeft = getRightAttachment ().minus (cacheWidth);
	}
	if (left.control != null && left.control.isDisposed ()) left.control = null;
	if (left.control == null) return cacheLeft = left;
	isVisited = true;
	FormData leftData = (FormData) left.control.getLayoutData ();
	FormAttachment rightAttachment = leftData.getRightAttachment ();
	FormAttachment leftAttachment = leftData.getLeftAttachment ();
	isVisited = false; 
	switch (left.alignment) {
		case SWT.LEFT: return cacheLeft = leftAttachment.plus (left.offset);
		case SWT.CENTER:
			FormAttachment leftWidth = rightAttachment.minus (leftAttachment);
			return cacheLeft = leftAttachment.plus (leftWidth.minus (cacheWidth).divide (2));
	}
	return cacheLeft = rightAttachment.plus (left.offset); 
}	

FormAttachment getRightAttachment () {
	if (cacheRight != null) return cacheRight;
	if (isVisited) return cacheRight = new FormAttachment (0, cacheWidth);
	if (right == null) {
		if (left == null) return cacheRight = new FormAttachment (0, cacheWidth);
		return cacheRight = getLeftAttachment ().plus (cacheWidth);
	}
	if (right.control != null && right.control.isDisposed ()) right.control = null;
	if (right.control == null) return cacheRight = right;
	isVisited = true;
	FormData rightData = (FormData) right.control.getLayoutData ();
	FormAttachment leftAttachment = rightData.getLeftAttachment ();
	FormAttachment rightAttachment = rightData.getRightAttachment ();
	isVisited = false;
	switch (right.alignment) {
		case SWT.RIGHT: return cacheRight = rightAttachment.plus (right.offset);
		case SWT.CENTER:
			FormAttachment rightWidth = rightAttachment.minus (leftAttachment);
			return cacheRight = rightAttachment.minus (rightWidth.minus (cacheWidth).divide (2));
	}
	return cacheRight = leftAttachment.plus (right.offset);
}

FormAttachment getTopAttachment () {
	if (cacheTop != null) return cacheTop;
	if (isVisited) return cacheTop = new FormAttachment (0, 0);
	if (top == null) {
		if (bottom == null) return cacheTop = new FormAttachment (0, 0);
		return cacheTop = getBottomAttachment ().minus (cacheHeight);
	}
	if (top.control != null && top.control.isDisposed ()) top.control = null;
	if (top.control == null) return cacheTop = top;
	isVisited = true;
	FormData topData = (FormData) top.control.getLayoutData ();
	FormAttachment topAttachment = topData.getTopAttachment ();
	FormAttachment bottomAttachment = topData.getBottomAttachment ();
	isVisited = false;
	switch (top.alignment) {
		case SWT.TOP: return cacheTop = topAttachment.plus (top.offset);
		case SWT.CENTER:
			FormAttachment topHeight = bottomAttachment.minus (topAttachment);
			return cacheTop = topAttachment.plus (topHeight.minus (cacheHeight).divide (2));
	}
	return cacheTop = bottomAttachment.plus (top.offset);
}

}
