package org.eclipse.swt.layout;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 * Instances of this class are used to define the sides of 
 * controls which are being layed out using a <code>FormLayout</code>. 
 * <p>
 * A <code>FormAttachment</code> defines the side of the control by using 
 * an equation, y = ax + b. There are two ways to define the attachment.
 * The first way is to set the equation directly. The "a" term represents 
 * a fraction of the parent composite widget. It can be defined using a 
 * numerator and denominator, or just a percentage value. If a percentage is
 * used, the denominator is set to 100. The "b" term represents an
 * offset. For example,
 * <pre>
 * 		FormAttachment attach = new FormAttachment (20, 5);
 * </pre>
 * This means that the side to which the <code>FormAttachment</code>
 * object belongs will lie at 20% of its parent composite, plus 5 pixels.
 * </p>
 * <p>
 * The second way is to set the control to which the side is attached.
 * For example,
 * <pre>
 * 		FormAttachment attach = new FormAttachment (button, -10);
 * </pre>
 * This means that the side to which the <code>FormAttachment</code>
 * object belongs will lie in the same position as the adjacent side of 
 * the control <code>button</code>, minus 10 pixels.
 * </p>
 * <p>
 * The attachments are used by the <code>FormData</code> that belongs to
 * each side of the control. It computes the equations from the given
 * information.
 * </p>
 * 
 * @see FormLayout
 * @see FormData
 */
public class FormAttachment {
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
	 * offset specifies the offset of the equation which defines the
	 * attachment. 
	 * 
	 * This is equivalent to the "b" term in the equation y = ax + b.
	 */
	public int offset;
	/**
	 * control specifies the control to which the given control is
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
