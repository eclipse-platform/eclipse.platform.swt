package org.eclipse.swt.custom;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

/**
 * The SashForm lays out its children in a Row or Column arrangement (as specified
 * by the orientation) and places a Sash between the children.
 * One child may be maximized to occupy the entire size of the SashForm.
 * The relative sizes of the children may be specfied using weights.
 *
 * <p>
 * <dl>
 * <dt><b>Styles:</b><dd>HORIZONTAL, VERTICAL
 * </dl>
 */
public class SashForm extends Composite {

	public int SASH_WIDTH = 3;

	private static final int DRAG_MINIMUM = 20;
	
	private int orientation = SWT.HORIZONTAL;
	private Sash[] sashes = new Sash[0];
	private Control[] controls = new Control[0];
	private Control maxControl = null;
	private Listener sashListener;

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
 * </p>
 *
 * @param parent a widget which will be the parent of the new instance (cannot be null)
 * @param style the style of widget to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 * </ul>
 *
 * @see SWT
 * @see #getStyle
 */
public SashForm(Composite parent, int style) {
	super(parent, checkStyle(style));
	if ((style & SWT.VERTICAL) != 0){
		orientation = SWT.VERTICAL;
	}
	
	this.addListener(SWT.Resize, new Listener() {
		public void handleEvent(Event e) {
			layout(true);
		}
	});
	
	sashListener = new Listener() {
		public void handleEvent(Event e) {
			onDragSash(e);
		}
	};
}
private static int checkStyle (int style) {
	int mask = SWT.BORDER;
	return style & mask;
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	Control[] controls = getControls(true);
	if (controls.length == 0) return new Point(wHint, hHint);
	
	int width = 0;
	int height = 0;
	boolean vertical = (orientation == SWT.VERTICAL);
	if (vertical) {
		height += (controls.length - 1) * SASH_WIDTH;
	} else {
		width += (controls.length - 1) * SASH_WIDTH;
	}
	for (int i = 0; i < controls.length; i++) {
		if (vertical) {
			Point size = controls[i].computeSize(wHint, SWT.DEFAULT);
			height += size.y;	
			width = Math.max(width, size.x);
		} else {
			Point size = controls[i].computeSize(SWT.DEFAULT, hHint);
			width += size.x;
			height = Math.max(height, size.y);
		}
	}
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	
	return new Point(width, height);
}
/**
 * Returns SWT.HORIZONTAL if the controls in the SashForm are laid out side by side
 * or SWT.VERTICAL   if the controls in the SashForm are laid out top to bottom.
 * 
 * @return SWT.HORIZONTAL or SWT.VERTICAL
 */
public int getOrientation() {
	//checkWidget();
	return orientation;
}
/**
 * Answer the control that currently is maximized in the SashForm.  
 * This value may be null.
 * 
 * @return the control that currently is maximized or null
 */
public Control getMaximizedControl(){
	//checkWidget();
	return this.maxControl;
}
/**
 * Answer the relative weight of each child in the SashForm.  The weight represents the
 * percent of the total width (if SashForm has Horizontal orientation) or 
 * total height (if SashForm has Vertical orientation) each control occupies.
 * The weights are returned in order of the creation of the widgets (weight[0]
 * corresponds to the weight of the first child created).
 * 
 * @return the relative weight of each child
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */

public int[] getWeights() {
	checkWidget();
	Control[] cArray = getControls(false);
	float[] ratios = new float[cArray.length];
	for (int i = 0; i < cArray.length; i++) {
		Float ratio = (Float)cArray[i].getData("layout ratio");
		if (ratio != null) {
			ratios[i] = ratio.floatValue();
		} else {
			ratios[i] = (float)0.2;
		}
	}
	
	int[] weights = new int[cArray.length];
	for (int i = 0; i < weights.length; i++) {
		weights[i] = (int)(ratios[i] * 1000);
	}
	return weights;
}
private Control[] getControls(boolean onlyVisible) {
	Control[] children = getChildren();
	Control[] controls = new Control[0];
	for (int i = 0; i < children.length; i++) {
		if (children[i] instanceof Sash) continue;
		if (onlyVisible && !children[i].getVisible()) continue;

		Control[] newControls = new Control[controls.length + 1];
		System.arraycopy(controls, 0, newControls, 0, controls.length);
		newControls[controls.length] = children[i];
		controls = newControls;
	}
	return controls;
}
public void layout(boolean changed) {
	checkWidget();
	Rectangle area = getClientArea();
	if (area.width == 0 || area.height == 0) return;
	
	Control[] newControls = getControls(true);
	if (controls.length == 0 && newControls.length == 0) return;
	controls = newControls;
	
	if (maxControl != null && !maxControl.isDisposed()) {
		for (int i= 0; i < controls.length; i++){
			if (controls[i] != maxControl) {
				controls[i].setBounds(-200, -200, 0, 0);
			} else {
				controls[i].setBounds(area);
			}
		}
		return;
	}
	
	// keep just the right number of sashes
	if (sashes.length < controls.length - 1) {
		Sash[] newSashes = new Sash[controls.length - 1];
		System.arraycopy(sashes, 0, newSashes, 0, sashes.length);
		int sashOrientation = (orientation == SWT.HORIZONTAL) ? SWT.VERTICAL : SWT.HORIZONTAL;
		for (int i = sashes.length; i < newSashes.length; i++) {
			newSashes[i] = new Sash(this, sashOrientation);
			newSashes[i].addListener(SWT.Selection, sashListener);
		}
		sashes = newSashes;
	}
	if (sashes.length > controls.length - 1) {
		if (controls.length == 0) {
			for (int i = 0; i < sashes.length; i++) {
				sashes[i].dispose();
			}
			sashes = new Sash[0];
		} else {
			Sash[] newSashes = new Sash[controls.length - 1];
			System.arraycopy(sashes, 0, newSashes, 0, newSashes.length);
			for (int i = controls.length - 1; i < sashes.length; i++) {
				sashes[i].dispose();
			}
			sashes = newSashes;
		}
	}
	
	if (controls.length == 0) return;
	
	// get the ratios
	float[] ratios = new float[controls.length];
	float total = 0;
	for (int i = 0; i < controls.length; i++) {
		Float ratio = (Float)controls[i].getData("layout ratio");
		if (ratio != null) {
			ratios[i] = ratio.floatValue();
		} else {
			ratios[i] = (float)0.2;
		}
		total += ratios[i];
	}
	
	if (orientation == SWT.HORIZONTAL) {
		total += (float)sashes.length * ((float)SASH_WIDTH / (float)area.width);
	} else {
		total += (float)sashes.length * ((float)SASH_WIDTH / (float)area.height);
	}
	
	if (orientation == SWT.HORIZONTAL) {
		int width = (int)((ratios[0] / total) * (float)area.width);
		int x = area.x;
		controls[0].setBounds(x, area.y, width, area.height);
		x += width;
		for (int i = 1; i < controls.length - 1; i++) {
			sashes[i - 1].setBounds(x, area.y, SASH_WIDTH, area.height);
			x += SASH_WIDTH;
			width = (int)((ratios[i] / total) * (float)area.width);
			controls[i].setBounds(x, area.y, width, area.height);
			x += width;
		}
		if (controls.length > 1) {
			sashes[sashes.length - 1].setBounds(x, area.y, SASH_WIDTH, area.height);
			x += SASH_WIDTH;
			width = area.width - x;
			controls[controls.length - 1].setBounds(x, area.y, width, area.height);
		}
	} else {
		int height = (int)((ratios[0] / total) * (float)area.height);
		int y = area.y;
		controls[0].setBounds(area.x, y, area.width, height);
		y += height;
		for (int i = 1; i < controls.length - 1; i++) {
			sashes[i - 1].setBounds(area.x, y, area.width, SASH_WIDTH);
			y += SASH_WIDTH;
			height = (int)((ratios[i] / total) * (float)area.height);
			controls[i].setBounds(area.x, y, area.width, height);
			y += height;
		}
		if (controls.length > 1) {
			sashes[sashes.length - 1].setBounds(area.x, y, area.width, SASH_WIDTH);
			y += SASH_WIDTH;
			height = area.height - y;
			controls[controls.length - 1].setBounds(area.x, y, area.width, height);
		}

	}
}
private void onDragSash(Event event) {
	if (event.detail == SWT.DRAG) {
		// constrain feedback
		Rectangle area = getClientArea();
		if (orientation == SWT.HORIZONTAL) {
			event.x = Math.min(Math.max(DRAG_MINIMUM, event.x), area.width - DRAG_MINIMUM);
		} else {
			event.y = Math.min(Math.max(DRAG_MINIMUM, event.y), area.height - DRAG_MINIMUM);
		}
		return;
	}

	Sash sash = (Sash)event.widget;
	int sashIndex = -1;
	for (int i= 0; i < sashes.length; i++) {
		if (sashes[i] == sash) {
			sashIndex = i;
			break;
		}
	}
	if (sashIndex == -1) return;

	Control c1 = controls[sashIndex];
	Control c2 = controls[sashIndex + 1];
	Rectangle b1 = c1.getBounds();
	Rectangle b2 = c2.getBounds();
	
	Rectangle sashBounds = sash.getBounds();
	Rectangle area = getClientArea();
	if (orientation == SWT.HORIZONTAL) {
		int shift = event.x - sashBounds.x;
		b1.width += shift;
		b2.x += shift;
		b2.width -= shift;
		if (b1.width < DRAG_MINIMUM || b2.width < DRAG_MINIMUM) {
			return;
		}
		c1.setData("layout ratio", new Float((float)b1.width / (float)area.width));
		c2.setData("layout ratio", new Float((float)b2.width / (float)area.width));
	} else {
		int shift = event.y - sashBounds.y;
		b1.height += shift;
		b2.y += shift;
		b2.height -= shift;
		if (b1.height < DRAG_MINIMUM || b2.height < DRAG_MINIMUM) {
			return;
		}
		c1.setData("layout ratio", new Float((float)b1.height / (float)area.height));
		c2.setData("layout ratio", new Float((float)b2.height / (float)area.height));
	}
	
	c1.setBounds(b1);
	sash.setBounds(event.x, event.y, event.width, event.height);
	c2.setBounds(b2);
}
/**
 * If orientation is SWT.HORIZONTAL, lay the controls in the SashForm 
 * out side by side.  If orientation is SWT.VERTICAL, lay the 
 * controls in the SashForm out top to bottom.
 * 
 * @param orientation SWT.HORIZONTAL or SWT.VERTICAL
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the value of orientation is not SWT.HORIZONTAL or SWT.VERTICAL
 * </ul>
 */
public void setOrientation(int orientation) {
	checkWidget();
	if (this.orientation == orientation) return;
	if (orientation != SWT.HORIZONTAL && orientation != SWT.VERTICAL) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	this.orientation = orientation;
	
	int sashOrientation = (orientation == SWT.HORIZONTAL) ? SWT.VERTICAL : SWT.HORIZONTAL;
	for (int i = 0; i < sashes.length; i++) {
		sashes[i].dispose();
		sashes[i] = new Sash(this, sashOrientation);
		sashes[i].addListener(SWT.Selection, sashListener);
	}
	layout();
}
public void setLayout (Layout layout) {
	checkWidget();
}
/**
 * Specify the control that should take up the entire client area of the SashForm.  
 * If one control has been maximized, and this method is called with a different control, 
 * the previous control will be minimized and the new control will be maximized..
 * if the value of control is null, the SashForm will minimize all controls and return to
 * the default layout where all controls are laid out separated by sashes.
 * 
 * @param control the control to be maximized or null
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMaximizedControl(Control control){
	checkWidget();
	if (control == null) {
		if (maxControl != null) {
			this.maxControl = null;
			layout();
			for (int i= 0; i < sashes.length; i++){
				sashes[i].setVisible(true);
			}
		}
		return;
	}
	
	for (int i= 0; i < sashes.length; i++){
		sashes[i].setVisible(false);
	}
	maxControl = control;
	layout();
}

/**
 * Specify the relative weight of each child in the SashForm.  This will determine
 * what percent of the total width (if SashForm has Horizontal orientation) or 
 * total height (if SashForm has Vertical orientation) each control will occupy.
 * The weights must be positive values and there must be an entry for each
 * non-sash child of the SashForm.
 * 
 * @param weights the relative weight of each child
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the weights value is null or of incorrect length (must match the number of children)</li>
 * </ul>
 */
public void setWeights(int[] weights) {
	checkWidget();
	Control[] cArray = getControls(false);
	if (weights == null || weights.length != cArray.length) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	
	int total = 0;
	for (int i = 0; i < weights.length; i++) {
		if (weights[i] < 0) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		total += weights[i];
	}
	if (total == 0) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	for (int i = 0; i < cArray.length; i++) {
		cArray[i].setData("layout ratio", new Float((float)weights[i] / (float)total));
	}
	
	layout();
}
}
