/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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