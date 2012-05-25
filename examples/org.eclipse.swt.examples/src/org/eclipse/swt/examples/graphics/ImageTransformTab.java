/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.swt.examples.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * This tab demonstrates transformations, such as scaling, rotation, and
 * invert.  It allows the user to specify values for scaling and rotation.
 */
public class ImageTransformTab extends GraphicsTab {

	private Spinner rotateSpinner, translateSpinnerX, translateSpinnerY, scaleSpinnerX, scaleSpinnerY;
	private Button invertButton;

/**
 * Constructor
 * @param example A GraphicsExample
 */
public ImageTransformTab(GraphicsExample example) {
	super(example);
}

public String getCategory() {
	return GraphicsExample.getResourceString("Transform"); //$NON-NLS-1$
}

public String getText() {
	return GraphicsExample.getResourceString("Image"); //$NON-NLS-1$
}

public String getDescription() {
	return GraphicsExample.getResourceString("TransformImgDescription"); //$NON-NLS-1$
}

/**
 * This method creates the controls specific to the tab. The call to the
 * createControlPanel method in the super class create the controls that are
 * defined in the super class.
 * 
 * @param parent The parent composite 
 */
public void createControlPanel(Composite parent) {
	
	Composite comp;
	GridLayout gridLayout = new GridLayout(2, false);
	
	// create spinner for the rotation angle
	comp = new Composite(parent, SWT.NONE);
	comp.setLayout(gridLayout);
 
	new Label(comp, SWT.CENTER).setText(GraphicsExample.getResourceString("Rotate")); //$NON-NLS-1$
	rotateSpinner = new Spinner(comp, SWT.BORDER | SWT.WRAP);
	GC gc = new GC(rotateSpinner);
	int width = gc.getFontMetrics().getAverageCharWidth() * 5;
	gc.dispose();
	rotateSpinner.setLayoutData(new GridData(width, SWT.DEFAULT));
	rotateSpinner.setSelection(0);
	rotateSpinner.setMinimum(-720);
	rotateSpinner.setMaximum(720);
	rotateSpinner.setIncrement(30);
	rotateSpinner.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {	
				example.redraw();
		}
	});
	
	// create a spinner for translating along the x axis
	comp = new Composite(parent, SWT.NONE);
	comp.setLayout(gridLayout);
	
	new Label(comp, SWT.CENTER).setText(GraphicsExample.getResourceString("xtranslate")); //$NON-NLS-1$
	translateSpinnerX = new Spinner(comp, SWT.BORDER | SWT.WRAP);
	translateSpinnerX.setLayoutData(new GridData(width, SWT.DEFAULT));
	translateSpinnerX.setMinimum(-100);
	translateSpinnerX.setMaximum(500);
	translateSpinnerX.setSelection(0);
	translateSpinnerX.setIncrement(10);
	translateSpinnerX.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {	
				example.redraw();
		}
	});
	
	// create a spinner for translating along the y axis
	comp = new Composite(parent, SWT.NONE);
	comp.setLayout(gridLayout);

	new Label(comp, SWT.CENTER).setText(GraphicsExample.getResourceString("ytranslate")); //$NON-NLS-1$
	translateSpinnerY = new Spinner(comp, SWT.BORDER | SWT.WRAP);
	translateSpinnerY.setLayoutData(new GridData(width, SWT.DEFAULT));
	translateSpinnerY.setMinimum(-100);
	translateSpinnerY.setMaximum(500);
	translateSpinnerY.setSelection(0);
	translateSpinnerY.setIncrement(10);
	translateSpinnerY.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {	
				example.redraw();
		}
	});
	
	// create a spinner for scaling along the x axis
	comp = new Composite(parent, SWT.NONE);
	comp.setLayout(gridLayout);
	
	new Label(comp, SWT.CENTER).setText(GraphicsExample.getResourceString("xscale")); //$NON-NLS-1$
	scaleSpinnerX = new Spinner(comp, SWT.BORDER | SWT.WRAP);
	scaleSpinnerX.setLayoutData(new GridData(width, SWT.DEFAULT));
	scaleSpinnerX.setDigits(2);
	scaleSpinnerX.setMinimum(1);
	scaleSpinnerX.setMaximum(400);
	scaleSpinnerX.setSelection(100);
	scaleSpinnerX.setIncrement(10);
	scaleSpinnerX.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {	
				example.redraw();
		}
	});
	
	// create a spinner for scaling along the y axis
	comp = new Composite(parent, SWT.NONE);
	comp.setLayout(gridLayout);

	new Label(comp, SWT.CENTER).setText(GraphicsExample.getResourceString("yscale")); //$NON-NLS-1$
	scaleSpinnerY = new Spinner(comp, SWT.BORDER | SWT.WRAP);
	scaleSpinnerY.setLayoutData(new GridData(width, SWT.DEFAULT));
	scaleSpinnerY.setDigits(2);
	scaleSpinnerY.setMinimum(1);
	scaleSpinnerY.setMaximum(400);
	scaleSpinnerY.setSelection(100);
	scaleSpinnerY.setIncrement(10);
	scaleSpinnerY.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {	
				example.redraw();
		}
	});
	
	// create a button for inverting the transform matrix
	comp = new Composite(parent, SWT.NONE);
	comp.setLayout(new GridLayout());
	invertButton = new Button(comp, SWT.TOGGLE);
	invertButton.setText(GraphicsExample.getResourceString("Invert")); //$NON-NLS-1$
	invertButton.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {	
				example.redraw();
		}
	});
}

/* (non-Javadoc)
 * @see org.eclipse.swt.examples.graphics.GraphicsTab#paint(org.eclipse.swt.graphics.GC, int, int)
 */
public void paint(GC gc, int width, int height) {
	if (!example.checkAdvancedGraphics()) return;
	Device device = gc.getDevice();

	Image image = GraphicsExample.loadImage(device, GraphicsExample.class, "ace_club.jpg"); 

	Transform transform = new Transform(device);
	
	// scale image
	transform.scale(scaleSpinnerX.getSelection()/100f, scaleSpinnerY.getSelection()/100f);
	
	// translate image
	transform.translate(translateSpinnerX.getSelection(), translateSpinnerY.getSelection());
	
	// rotate on center of image
	Rectangle rect = image.getBounds();
	transform.translate(rect.width/2, rect.height/2);
	transform.rotate(rotateSpinner.getSelection());
	transform.translate(-rect.width/2, -rect.height/2);
	
	if(invertButton.getSelection()){
		transform.invert();
	}
	
	gc.setTransform(transform);
	gc.drawImage(image, 0, 0);

	transform.dispose();
	image.dispose();
}

}
