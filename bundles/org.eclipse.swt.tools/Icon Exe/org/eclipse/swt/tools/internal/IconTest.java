/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.internal;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;


public class IconTest {	
	static String EXECUTABLE = "d:/eclipse.exe";
	static ImageData[] src, dst, verify;
	public static void main(String[] args) {
		try {
			src = IconExe.loadIcons(EXECUTABLE);
			dst = new ImageData[] {
				new ImageData(IconTest.class.getResourceAsStream("test_16_4.bmp")),
				new ImageData(IconTest.class.getResourceAsStream("test_16_8.bmp")),
				new ImageData(IconTest.class.getResourceAsStream("test_32_4.bmp")),
				new ImageData(IconTest.class.getResourceAsStream("test_32_8.bmp")),
				new ImageData(IconTest.class.getResourceAsStream("test_48_4.bmp")),
				new ImageData(IconTest.class.getResourceAsStream("test_48_8.bmp")),
			};
			int cnt = IconExe.unloadIcons("d:/eclipse.exe", dst);
			if (cnt != dst.length) {
				System.out.println("Error - not all icons were written: "+cnt);
			}
			verify = IconExe.loadIcons("d:/eclipse.exe");
		} catch (Exception e) {
			e.printStackTrace();
		}
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.addListener(SWT.Paint, new Listener() {
			public void handleEvent(Event e) {
				e.gc.drawString("Source Icons in original exe", 10, 0);
				for (int i = 0; i < src.length; i++) {
					Image img = new Image(display, src[i]);
					e.gc.drawImage(img, 10, i * 50 + 20);
					img.dispose();
				}
				e.gc.drawString("New Icons", 200, 0);
				for (int i = 0; i < dst.length; i++) {
					Image img = new Image(display, dst[i]);
					e.gc.drawImage(img, 200, i * 50 + 20);
					img.dispose();
				}
				e.gc.drawString("Final Icons in modified exe", 400, 0);
				for (int i = 0; i < verify.length; i++) {
					Image img = new Image(display, verify[i]);
					e.gc.drawImage(img, 400, i * 50 + 20);
					img.dispose();
				}
			}
		});
		shell.setText("PR");
		shell.open();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}		
	}
}