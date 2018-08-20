/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.opengl.examples;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.opengl.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class ColorSelectionGroup implements Listener {
	private Color color;
	private Label label;
	private Image image;
	private Button button;
	private List<IColorSelectionListener> listeners = new ArrayList<IColorSelectionListener>();

	/**
	 * Constructor.
	 * 
	 * @param parent the parent composite
	 * @param style style bits to be applied to the color group
	 */
	ColorSelectionGroup(Composite parent, int style) {
		super();
		initColor(parent.getDisplay());

		Composite colorGroup = new Composite(parent, style);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		colorGroup.setLayout(layout);
//		GridData data = new GridData(GridData.FILL_HORIZONTAL);
//		data.grabExcessHorizontalSpace = true;
//		colorGroup.setLayoutData(data);

		button = new Button(colorGroup, SWT.NONE);
		button.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		image = new Image(button.getDisplay(), 12, 12);
		drawButtonImage();
		button.setImage(image);
		button.addListener(SWT.Selection, this);
		button.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent event) {
				if (image != null) image.dispose();
				if (color != null) color.dispose();
			}
		});
		
		label = new Label(colorGroup, SWT.NONE);
		label.setText("Color");
//		data = new GridData(GridData.FILL_HORIZONTAL);
//		data.grabExcessHorizontalSpace = true;
//		label.setLayoutData(data);
	}

	/**
	 * Adds the argument to this group's collection of
	 * color selection listeners.
	 * 
	 * @param listener
	 */
	void addColorSelectionListener(IColorSelectionListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Draws the colored square on the selection button.
	 */
	void drawButtonImage() {
		GC gc = new GC(image);
		gc.setBackground(color);
		Rectangle bounds = image.getBounds();
		gc.fillRectangle(0, 0, bounds.width, bounds.height);
		gc.drawRectangle(0, 0, bounds.width - 1, bounds.height - 1);
		gc.dispose();
	}

	/**
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(Event)
	 */
	public void handleEvent(Event e) {
		Shell shell = button.getShell();
		ColorDialog colorDialog = new ColorDialog(shell);
		colorDialog.setRGB(
			new RGB(color.getRed(), color.getGreen(), color.getBlue()));
		RGB rgb = colorDialog.open();
		if (rgb == null) return;
		setRGB(rgb);
		notifyListeners(rgb);
	}

	/**
	 * Initializes the color by querying for the current color.
	 * 
	 * @param display
	 */
	void initColor(Display display) {
		double[] currentColor = new double[4];
		GL.glGetDoublev(GL.GL_CURRENT_COLOR, currentColor);
		RGB rgb =
			new RGB(
				(int) currentColor[0] * 255,
				(int) currentColor[1] * 255,
				(int) currentColor[2] * 255);
		color = new Color(display, rgb);
	}
	
	/**
	 * Notifies all registered color selection listeners.
	 * 
	 * @param value
	 */
	void notifyListeners(RGB rgb) {
		IColorSelectionListener[] listenersArr =
			listeners.toArray(new IColorSelectionListener[listeners.size()]);
		for (int i = 0; i < listenersArr.length; i++) {
			listenersArr [i].handleColorSelection(rgb);
		}
	}

	/**
	 * Removes the argument from this group's collection of color selection
	 * listeners.  If the argument is not a registered listener then does
	 * nothing.
	 * 
	 * @param listener
	 */
	void removeColorSelectionListener(IColorSelectionListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Sets the text for the group's label.
	 * 
	 * @param text the new label text
	 */
	void setText(String text) {
		label.setText(text);
	}

	/**
	 * Sets the current color.
	 * 
	 * @param rgb the rgb of the new color
	 */
	void setRGB(RGB rgb) {
		Color oldColor = color;
		color = new Color(button.getDisplay(), rgb);
		drawButtonImage();
		button.setImage(image);
		if (oldColor != null) oldColor.dispose();
	}
}
