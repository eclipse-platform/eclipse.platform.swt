package org.eclipse.swt.layout;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 * Instances of this class are used to define where to attach 
 * the sides of controls in a <code>FormLayout</code>. 
 * <p>
 * <code>FormAttachments</code> are set into the top, bottom, left,
 * and right fields of the <code>FormData</code> for a control.
 * For example:
 * <pre>
 * 		FormData formData = new FormData();
 * 		formData.top = new FormAttachment(0,5);
 * 		formData.bottom = new FormAttachment(100,-5);
 * 		formData.left = new FormAttachment(0,5);
 * 		formData.right = new FormAttachment(100,-5);
 * 		button.setLayoutData(formData);
 * </pre>
 * </p>
 * <p>
 * A <code>FormAttachment</code> defines where to attach the side of
 * a control by using the equation, y = ax + b. The "a" term represents 
 * a fraction of the parent composite's width (from the left) or height
 * (from the top). It can be defined using a numerator and denominator,
 * or just a percentage value. If a percentage is used, the denominator 
 * is set to 100. The "b" term in the equation represents an offset, in
 * pixels, from the attachment position. For example:
 * <pre>
 * 		FormAttachment attach = new FormAttachment (20, -5);
 * </pre>
 * specifies that the side to which the <code>FormAttachment</code>
 * object belongs will lie at 20% of the parent composite, minus 5 pixels.
 * </p>
 * <p>
 * Control sides can also be attached to the adjacent side of another control.
 * For example:
 * <pre>
 * 		FormAttachment attach = new FormAttachment (button, 10);
 * </pre>
 * specifies that the side to which the <code>FormAttachment</code>
 * object belongs will lie in the same position as the adjacent side of 
 * the <code>button</code> control, plus 10 pixels.
 * </p>
 * 
 * @see FormLayout
 * @see FormData
 */
public final class FormAttachment {
	/**
	 * numerator specifies the numerator of the "a" term in the
	 * equation, y = ax + b, which defines the attachment.
	 */
	public int numerator;
	/**
	 * denominator specifies the denominator of the "a" term in the
	 * equation, y = ax + b, which defines the attachment.
	 * 
	 * The default value is 100.
	 */
	public int denominator = 100;
	/**
	 * offset specifies the offset, in pixels, of the control side
	 * from the attachment position.
	 * If the offset is positive, then the control side is offset
	 * to the right of or below the attachment position. If it is
	 * negative, then the control side is offset to the left of or
	 * above the attachment position.
	 * 
	 * This is equivalent to the "b" term in the equation y = ax + b.
	 * The default value is 0.
	 */
	public int offset;
	/**
	 * control specifies the control to which the control side is
	 * attached.
	 */
	public Control control;
	
FormAttachment () {
}

public FormAttachment (int numerator, int denominator, int offset) {
	if (denominator == 0) SWT.error (SWT.ERROR_CANNOT_BE_ZERO);
	this.numerator = numerator;
	this.denominator = denominator;
	this.offset = offset;
}

public FormAttachment (int numerator, int offset) {
	this (numerator, 100, offset);
}

public FormAttachment (Control control, int offset) {
	this.control = control;
	this.offset = offset;
}

public FormAttachment (Control control) {
	this (control, 0);
}

FormAttachment minus (FormAttachment attachment) {
	FormAttachment solution = new FormAttachment();
	solution.numerator = numerator * attachment.denominator - denominator * attachment.numerator;
	solution.denominator = denominator * attachment.denominator;
	solution.offset = offset - attachment.offset;
	return solution;
}

FormAttachment plusB (int value) {
	return new FormAttachment (numerator, denominator, offset + value);
}

FormAttachment minusB (int value) {
	return new FormAttachment (numerator, denominator, offset - value);
}

int solveX (int value) {
	if (denominator == 0) SWT.error (SWT.ERROR_CANNOT_BE_ZERO);
	return ((numerator * value) / denominator) + offset;
}

int solveY (int value) {
	if (numerator == 0) SWT.error (SWT.ERROR_CANNOT_BE_ZERO);
	return (value - offset) * denominator / numerator;
}
	
public String toString () {
 	String string = control != null ? control.toString () : numerator + "/" + denominator;
	return "y = (" + string + (offset >= 0 ? ")x + " + offset: ")x - " + (-offset));
}
}
