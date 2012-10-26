/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
/* 
 * example snippet: Hello World
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.widgets.*;

public class Snippet1 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell(display);
	
////	Cursor cursor = display.getSystemCursor(SWT.CURSOR_APPSTARTING);
////	ImageData s = new ImageData(32, 32, 1, new PaletteData(new RGB[]{new RGB(0, 0, 0), new RGB(255, 255, 255)}));
////	ImageData m = new ImageData(32, 32, 1, new PaletteData(new RGB[]{new RGB(0, 0, 0), new RGB(255, 255, 255)}));
////	for (int i=0; i<s.data.length; i++) {
////		s.data[i] = (byte)0xFF;
////	}
////	for (int i=0; i<s.data.length; i++) {
//////		m.data[i] = (byte)0xFF;
////	}
////	
////	Cursor cursor = new Cursor(display, s, m, 10, 10);
//	
////	ImageData mask = new ImageData("/home/test/git/eclipse.platform.ui/bundles/org.eclipse.ui/icons/full/pointer/bottom_mask.bmp");
////	ImageData source = new ImageData("/home/test/git/eclipse.platform.ui/bundles/org.eclipse.ui/icons/full/pointer/bottom_source.bmp");
////	ImageData mask = new ImageData("/home/test/git/eclipse.platform.ui/bundles/org.eclipse.ui/icons/full/pointer/offscreen_mask.bmp");
////	ImageData source = new ImageData("/home/test/git/eclipse.platform.ui/bundles/org.eclipse.ui/icons/full/pointer/offscreen_source.bmp");
////	ImageData mask = new ImageData("/home/test/git/eclipse.platform.ui/bundles/org.eclipse.ui/icons/full/pointer/invalid_mask.bmp");
////	ImageData source = new ImageData("/home/test/git/eclipse.platform.ui/bundles/org.eclipse.ui/icons/full/pointer/invalid_source.bmp");
//	ImageData mask = new ImageData("/home/test/git/eclipse.platform.ui/bundles/org.eclipse.ui/icons/full/pointer/left_mask.bmp");
//	ImageData source = new ImageData("/home/test/git/eclipse.platform.ui/bundles/org.eclipse.ui/icons/full/pointer/left_source.bmp");
//	Cursor cursor = new Cursor(display, mask, source, 10, 10);
//	
////	Cursor cursor = new Cursor(display, new ImageData("/home/test/icons/oil-logo-v1.0.png"), 0, 0);
//	
//	shell.setCursor(cursor);
	
//	Link link = new Link(shell, SWT.BORDER);
//	link.setText("a like <a>Silenio</a> now");
//	link.setSize(300, 30);
	
	shell.setLayout(new GridLayout(1, false));
	
//	Button b = new Button(shell, SWT.PUSH);
//	b.setText("Button");
//	Text text = new Text(shell, SWT.SINGLE | SWT.BORDER);
//	text.setMessage("Hello wolrd");
	
	shell.setBackgroundImage(new Image(display, "/home/test/icons/oil-logo-v1.0.png"));
	shell.setBackgroundMode(SWT.INHERIT_FORCE);
	
	Table table = new Table(shell, SWT.MULTI);
	for (int i=0; i<4; i++) {
		
	}
	table.addListener(SWT.Paint, new Listener() {
		public void handleEvent(Event event) {
			event.gc.drawLine(0, 0, 100, 100);
		}
	});
	
	
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}
