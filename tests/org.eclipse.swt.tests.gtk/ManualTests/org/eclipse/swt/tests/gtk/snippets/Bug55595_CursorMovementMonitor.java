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


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class Bug55595_CursorMovementMonitor {

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.addListener(SWT.MouseMove, new Listener() {
			@Override
			public void handleEvent(Event event) {
				System.out.println("MouseMove x "+event.x+" y "+event.y);
		}});
		shell.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				System.out.println("MouseUp x "+event.x+" y "+event.y);
		}});		
		shell.addListener(SWT.MouseDown, new Listener() {
			@Override
			public void handleEvent(Event event) {
				System.out.println("MouseDown x "+event.x+" y "+event.y);
		}});		
		new Thread() { 
			@Override
			public void run() {
				while (!shell.isDisposed()) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {}
					display.asyncExec(new Runnable() {
						@Override
						public void run() {
							Point pt = shell.getLocation();
							Event event = new Event();
							event.type = SWT.MouseMove;
							event.x = pt.x + 100;
							event.y = pt.y + 100;
							display.post(event);
						}
					});
				}
			}
		}.start();
		
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
}

