package org.eclipse.swt.layout;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.*;

/**
 * Instances of this class are used to set the attachments 
 * of a control in a <code>FormLayout</code>. 
 * <p>
 * To set a <code>FormData</code> object into a widget, you use the 
 * <code>setLayoutData ()</code> method. To define attachments for the 
 * <code>FormData</code> objects, you set the fields directly, like this:
 * <pre>
 * 		FormData formData = new FormData();
 * 		formData.left = new FormAttachment(0,5);
 * 		button1.setLayoutData(formData);
 * </pre>
 * 
 * <code>FormData</code> takes the <code>FormAttachments</code> defined for 
 * each side of the control and computes an equation for them. These 
 * equations are used to determine the size and position of the control
 * within the parent composite. <code>FormData</code> objects also allow you 
 * to set the width and height of widgets. 
 * </p>
 * <p>
 * The equation for the side of the widget can be explicitly defined in the 
 * <code>FormAttachment</code>. If the equation has not been explicitly 
 * defined but given a control to which the side has been attached, it is 
 * computed based on the equations of other controls in the layout. If there 
 * is no attachment defined for a specific side, an equation is 
 * defined based on the height or width of the control, and the other sides
 * of the control.
 * </p>
 * 
 * @see FormLayout
 * @see FormAttachment
 */
public final class FormData {
	/**
	 * height specifies the desired height in pixels given
	 * by the user
	 */
	public int height;
	/**
	 * width specifies the desired width in pixels given
	 * by the user
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
	boolean isVisited;
	
public FormData () {
	this (SWT.DEFAULT, SWT.DEFAULT);
}
	
public FormData (int width, int height) {
	this.width = width;
	this.height = height;
}

FormAttachment getLeftAttachment () {
	if (isVisited) return new FormAttachment (0, 0);
	if (left == null) {
		if (right == null) return new FormAttachment (0, 0);
		return getRightAttachment ().minusB (cacheWidth);
	}
	if (left.control == null) return left;
	isVisited = true;
	FormData leftData = (FormData) left.control.getLayoutData ();
	FormAttachment rightAttachment = leftData.getRightAttachment ();
	isVisited = false; 
	return rightAttachment.plusB (left.offset); 
}	

FormAttachment getRightAttachment () {
	if (isVisited) return new FormAttachment (0, cacheWidth);
	if (right == null) {
		if (left == null) return new FormAttachment (0, cacheWidth);
		return getLeftAttachment ().plusB (cacheWidth);
	}
	if (right.control == null) return right;
	isVisited = true;
	FormData rightData = (FormData) right.control.getLayoutData ();
	FormAttachment leftAttachment = rightData.getLeftAttachment ();
	isVisited = false;
	return leftAttachment.plusB (right.offset);
}

FormAttachment getTopAttachment () {
	if (isVisited) return new FormAttachment (0, 0);
	if (top == null) {
		if (bottom == null) return new FormAttachment (0, 0);
		return getBottomAttachment ().minusB (cacheHeight);
	}
	if (top.control == null) return top;
	isVisited = true;
	FormData topData = (FormData) top.control.getLayoutData ();
	FormAttachment bottomAttachment = topData.getBottomAttachment ();
	isVisited = false;
	return bottomAttachment.plusB (top.offset);
}

FormAttachment getBottomAttachment () {
	if (isVisited) return new FormAttachment (0, cacheHeight);
	if (bottom == null) {
		if (top == null) return new FormAttachment (0, cacheHeight);
		return getTopAttachment ().plusB (cacheHeight);
	}
	if (bottom.control == null) return bottom;
	isVisited = true;
	FormData bottomData = (FormData) bottom.control.getLayoutData ();
	FormAttachment topAttachment = bottomData.getTopAttachment ();
	isVisited = false;
	return topAttachment.plusB (bottom.offset);	
}

}
