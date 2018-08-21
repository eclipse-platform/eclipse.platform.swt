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


import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/*
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specificlanguage governing rights and limitations under the License.
 *
 * The Initial Developer of the Original Code is Gregor Mueckl.
 * All Rights Reserved.
 *
 * Created on Nov 16, 2005
 */

public class Bug144120_GLCanvasMouseEvents {

	private class MyMouseMoveListener implements MouseMoveListener {

		@Override
		public void mouseMove(MouseEvent event) {
			long time=System.currentTimeMillis();
			System.out.println(new Long(time).toString() + " mouse move: " + event.x + " " + event.y);
			try {
				synchronized(this) {
					wait(100);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private class MyMouseListener implements MouseListener {

		@Override
		public void mouseDoubleClick(MouseEvent event) {
			long time=System.currentTimeMillis();
			System.out.println(new Long(time).toString() + " mouse doubleclick: " + event.x + " " + event.y);
		}

		@Override
		public void mouseDown(MouseEvent event) {
			long time=System.currentTimeMillis();
			System.out.println(Long.valueOf(time).toString() + " mouse move: " + event.x + " " + event.y);
			try {
				synchronized(this) {
					wait(100);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(new Long(time).toString() + " mouse down: " + event.x + " " + event.y);
		}

		@Override
		public void mouseUp(MouseEvent event) {
			long time=System.currentTimeMillis();
			System.out.println(new Long(time).toString() + " mouse move: " + event.x + " " + event.y);
			try {
				synchronized(this) {
					wait(100);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(new Long(time).toString() + " mouse up: " + event.x + " " + event.y);
		}

	}

	public Bug144120_GLCanvasMouseEvents() {
		Display display=new Display();
		Shell shell=new Shell();
		GLCanvas glcanvas=new GLCanvas(shell,0, new GLData());

		glcanvas.addMouseMoveListener(new MyMouseMoveListener());
		glcanvas.addMouseListener(new MyMouseListener());

		glcanvas.setSize(300,300);

		shell.pack();

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public static void main(String args[]) {
	}
}
