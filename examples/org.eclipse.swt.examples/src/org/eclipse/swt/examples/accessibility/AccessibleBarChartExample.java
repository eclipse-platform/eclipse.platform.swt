/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.accessibility;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * This example creates two bar charts.
 * The BarChart class is a simple example of the use of the SWT Accessibility API.
 * Each piece of data in a BarChart is represented as an accessible child, which
 * can be specified by its childID.
 */
public class AccessibleBarChartExample {
	static Display display;
	static Shell shell;
	
	public static void main(String[] args) {
		display = new Display();
		shell = new Shell(display);
		shell.setLayout(new GridLayout());
		
		BarChart pets = new BarChart(shell, SWT.BORDER);
		pets.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		pets.setTitle("Kids in my class: Pet Survey");
		pets.addData("Dogs", 4);
		pets.addData("Cats", 5);
		pets.addData("Hamsters", 6);
		pets.addData("Budgies", 1);
		pets.addData("Fish", 4);
		pets.addData("None", 4);
		
		BarChart foods = new BarChart(shell, SWT.BORDER);
		foods.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		foods.setTitle("Kids in my class: Favorite Food Survey");
		foods.setColor(SWT.COLOR_BLUE);
		foods.addData("Pizza", 10);
		foods.addData("Hot Dogs", 5);
		foods.addData("Chicken Fingers", 2);
		foods.addData("French Fries", 3);
		foods.addData("Ice Cream", 4);

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
	}
}