/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;


import org.eclipse.swt.widgets.Display;

public class Bug271024_DeviceLoadFont {

	public static void main(String[] args) {
		Display display = new Display();
		if(display.loadFont("garbage.ttf")) {
			System.out.println("Should not have got here!");
		} else {
			System.out.println("OK, this graphics.font does not exist.");
		}
		display.dispose();
	}

}