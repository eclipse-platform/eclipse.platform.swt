/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.opengl.examples;


import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class OpenGLExample {
	private TabFolder tabFolder;
	private OpenGLTab[] tabs;
	private int sleep;

	/**
	 * Renders the scene of the current tab.
	 */
	void display() {
		int index = tabFolder.getSelectionIndex();
		tabs[index].render();
		tabs[index].swap();
	}
	
	/**
	 * Disposes of all contained tabs.
	 */
	void dispose() {
		tabFolder = null;
		for (int i = 0; i < tabs.length; i++) {
			tabs[i].dispose();
		}
	}

	/**
	 * Runs the OpenGL example
	 */
	void run() {
		final Display display = new Display();
		final Shell shell =
			new Shell(display, SWT.SHELL_TRIM | SWT.NO_BACKGROUND);
		shell.setLayout(new FillLayout());

		tabFolder = new TabFolder(shell, SWT.NONE);
		tabs =
			new OpenGLTab[] {
				new AntialiasingTab(),
				new AreaTab(),
				new BezierTab(),
				new BitmapTextTab(),
				new FogTab(),
				new GradientTab(),
				new LightTab(),
				new NurbTab(),
				new ObjectsTab(),
				new OutlineTextTab(),	/* outline tab is win32-only */
				new ReflectionTab(),
				new StencilTab(),
				new TextureTab(),
				new TransparencyTab()};
				
		for (int i = 0; i < tabs.length; i++) {
			TabItem item = new TabItem(tabFolder, SWT.NONE);
			item.setText(tabs[i].getTabText());
			item.setControl(tabs[i].createTabFolderPage(tabFolder));
		}
		
		tabFolder.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				OpenGLTab tab = tabs[tabFolder.getSelectionIndex()];
				tab.setCurrent();
				sleep = tab.getSleepLength();
			}
		});
		if (tabs.length > 0) {
			tabs[0].setCurrent();
			sleep = tabs[0].getSleepLength();
		}
		
		shell.setText("OpenGL Example");
		Runnable timer = new Runnable() {
			public void run() {
				if (shell.isDisposed()) return;
				display();
				display.timerExec(sleep, this);
			}
		};
		timer.run();
		shell.addListener(SWT.Dispose, new Listener() {
			public void handleEvent(Event e) {
				dispose();
			}
		});
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	/**
	 * Invokes as a standalone program.
	 */
	public static void main(String[] args) {
		new OpenGLExample().run();
	}
}
