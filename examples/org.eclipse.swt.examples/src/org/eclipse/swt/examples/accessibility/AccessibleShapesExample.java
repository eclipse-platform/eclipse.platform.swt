/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.accessibility;

/**
 * This example creates some Shapes in a Shell and uses a FillLayout to position them.
 * The Shape class is a simple example of the use of the SWT Accessibility API.
 * A Shape is accessible, but it does not have any accessible children.
 * 
 * @see TrafficLight - for an example of a class that does have accessible children
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class AccessibleShapesExample {
	static Display display;
	static Shell shell;
	
	public static void main(String[] args) {
		display = new Display();
		shell = new Shell(display);
		shell.setLayout(new FillLayout());
		
		Shape redSquare = new Shape(shell, SWT.NONE);
		redSquare.setColor("Red");
		redSquare.setShape("Square");
		
		Shape blueCircle = new Shape(shell, SWT.NONE);
		blueCircle.setColor("Blue");
		blueCircle.setShape("Circle");

		Shape greenSquare = new Shape(shell, SWT.NONE);
		greenSquare.setColor("Green");
		greenSquare.setShape("Square");

		shell.pack();
		shell.open();
		redSquare.setFocus();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
	}
}
