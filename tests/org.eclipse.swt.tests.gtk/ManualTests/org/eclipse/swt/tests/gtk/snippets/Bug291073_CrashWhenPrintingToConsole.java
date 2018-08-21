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


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.BitSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Bug291073_CrashWhenPrintingToConsole {
	public static void main(String[] args) throws IOException{
		Display display = new Display();
		Shell shell = new Shell (display);
		
		Font font1 = new Font(display, "Tahoma", 14, SWT.BOLD);
		Color blue = display.getSystemColor(SWT.COLOR_BLUE);
		Color yellow = display.getSystemColor(SWT.COLOR_YELLOW);
		
		final TextLayout layout = new TextLayout(display);
		TextStyle style1 = new TextStyle(font1, yellow, blue);
		
        BitSet bs = new BitSet();
        bs.set(2894298, true);
        bs.set(324832893, true);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(bs);

		layout.setText(out.toString());
		System.out.println("length:"+out.toString());
		layout.setStyle(style1, 2, out.toString().length() - 10000000);
		shell.addListener(SWT.Paint, new Listener() {
			@Override
			public void handleEvent (Event event) {
				Display display = event.display;
				GC gc = event.gc;
				
				Rectangle rect0 = layout.getBounds();
				rect0.x += 10;
				rect0.y += 10;
				gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
				gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
				gc.fillRectangle(rect0);
				layout.draw(gc, rect0.x, rect0.y);
				}
		});
		shell.open();	
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		font1.dispose();
		layout.dispose();
		display.dispose();
	}

}