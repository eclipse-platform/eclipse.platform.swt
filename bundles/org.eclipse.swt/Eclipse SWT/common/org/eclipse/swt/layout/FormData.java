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
import org.eclipse.swt.widgets.*;

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

FormAttachment getBottomAttachment (int spacing) {
	if (cacheBottom != null) return cacheBottom;
	if (isVisited) return cacheBottom = new FormAttachment (0, cacheHeight);
	if (bottom == null) {
		if (top == null) return cacheBottom = new FormAttachment (0, cacheHeight);
		return cacheBottom = getTopAttachment (spacing).plus (cacheHeight);
	}
	Control control = bottom.control;
	if (control != null && control.isDisposed ()) bottom.control = control = null;
	if (control == null) return cacheBottom = bottom;
	isVisited = true;
	FormData formData = (FormData) control.getLayoutData ();
	FormAttachment bottomAttachment = formData.getBottomAttachment (spacing);
	switch (bottom.alignment) {
		case SWT.BOTTOM: 
			cacheBottom = bottomAttachment.plus (bottom.offset);
			break;
		case SWT.CENTER: {
			FormAttachment topAttachment = formData.getTopAttachment (spacing);
			FormAttachment bottomHeight = bottomAttachment.minus (topAttachment);
			cacheBottom = bottomAttachment.minus (bottomHeight.minus (cacheHeight).divide (2));
			break;
		}
//		case SWT.TOP:
//		case SWT.DEFAULT:
		default: {
			FormAttachment topAttachment = formData.getTopAttachment (spacing);
			cacheBottom = topAttachment.plus (bottom.offset - spacing);	
			break;
		}
	}
	isVisited = false;
	return cacheBottom;
}

FormAttachment getLeftAttachment (int spacing) {
	if (cacheLeft != null) return cacheLeft;
	if (isVisited) return cacheLeft = new FormAttachment (0, 0);
	if (left == null) {
		if (right == null) return cacheLeft = new FormAttachment (0, 0);
		return cacheLeft = getRightAttachment (spacing).minus (cacheWidth);
	}
	Control control = left.control;
	if (control != null && control.isDisposed ()) left.control = control = null;
	if (control == null) return cacheLeft = left;
	isVisited = true;
	FormData formData = (FormData) control.getLayoutData ();
	FormAttachment leftAttachment = formData.getLeftAttachment (spacing);
	switch (left.alignment) {
		case SWT.LEFT:
			cacheLeft = leftAttachment.plus (left.offset);
			break;
		case SWT.CENTER: {
			FormAttachment rightAttachment = formData.getRightAttachment (spacing);
			FormAttachment leftWidth = rightAttachment.minus (leftAttachment);
			cacheLeft = leftAttachment.plus (leftWidth.minus (cacheWidth).divide (2));
			break;
		}
//		case SWT.RIGHT:
//		case SWT.DEFAULT:
		default: {
			FormAttachment rightAttachment = formData.getRightAttachment (spacing);
			cacheLeft = rightAttachment.plus (left.offset + spacing); 
		}
	}
	isVisited = false; 
	return cacheLeft;
}	

String getName () {
	String string = getClass ().getName ();
	int index = string.lastIndexOf ('.');
	if (index == -1) return string;
	return string.substring (index + 1, string.length ());
}

FormAttachment getRightAttachment (int spacing) {
	if (cacheRight != null) return cacheRight;
	if (isVisited) return cacheRight = new FormAttachment (0, cacheWidth);
	if (right == null) {
		if (left == null) return cacheRight = new FormAttachment (0, cacheWidth);
		return cacheRight = getLeftAttachment (spacing).plus (cacheWidth);
	}
	Control control = right.control;
	if (control != null && control.isDisposed ()) right.control = control = null;
	if (control == null) return cacheRight = right;
	isVisited = true;
	FormData formData = (FormData) control.getLayoutData ();
	FormAttachment rightAttachment = formData.getRightAttachment (spacing);
	switch (right.alignment) {
		case SWT.RIGHT: 
			cacheRight = rightAttachment.plus (right.offset);
			break;
		case SWT.CENTER: {
			FormAttachment leftAttachment = formData.getLeftAttachment (spacing);
			FormAttachment rightWidth = rightAttachment.minus (leftAttachment);
			cacheRight = rightAttachment.minus (rightWidth.minus (cacheWidth).divide (2));
			break;
		}
//		case SWT.LEFT:
//		case SWT.DEFAULT:
		default: {
			FormAttachment leftAttachment = formData.getLeftAttachment (spacing);
			cacheRight = leftAttachment.plus (right.offset - spacing);
			break;
		}
	}
	isVisited = false;
	return cacheRight;
}

FormAttachment getTopAttachment (int spacing) {
	if (cacheTop != null) return cacheTop;
	if (isVisited) return cacheTop = new FormAttachment (0, 0);
	if (top == null) {
		if (bottom == null) return cacheTop = new FormAttachment (0, 0);
		return cacheTop = getBottomAttachment (spacing).minus (cacheHeight);
	}
	Control control = top.control;
	if (control != null && control.isDisposed ()) top.control = control = null;
	if (control == null) return cacheTop = top;
	isVisited = true;
	FormData formData = (FormData) control.getLayoutData ();
	FormAttachment topAttachment = formData.getTopAttachment (spacing);
	switch (top.alignment) {
		case SWT.TOP:
			cacheTop = topAttachment.plus (top.offset);
			break;
		case SWT.CENTER: {
			FormAttachment bottomAttachment = formData.getBottomAttachment (spacing);
			FormAttachment topHeight = bottomAttachment.minus (topAttachment);
			cacheTop = topAttachment.plus (topHeight.minus (cacheHeight).divide (2));
			break;
		}
//		case SWT.BOTTOM:
//		case SWT.DEFAULT:
		default: {
			FormAttachment bottomAttachment = formData.getBottomAttachment (spacing);
			cacheTop = bottomAttachment.plus (top.offset + spacing);
			break;
		}
	}
	isVisited = false;
	return cacheTop;
}

public String toString () {
 	String string = getName()+" {";
	if (height != SWT.DEFAULT) string += "height="+height+" ";
 	if (width != SWT.DEFAULT) string += "width="+width+" ";
 	if (left != null) string += "left="+left+" ";
 	if (right != null) string += "right="+right+" ";
 	if (top != null) string += "top="+top+" ";
 	if (bottom != null) string += "bottom="+bottom+" ";
 	string = string.trim();
 	string += "}";
	return string;
}
}
