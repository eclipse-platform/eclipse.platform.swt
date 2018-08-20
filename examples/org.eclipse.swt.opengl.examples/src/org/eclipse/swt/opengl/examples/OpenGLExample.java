/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.opengl.examples;


import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import java.util.*;

public class OpenGLExample {
	private static ResourceBundle resourceBundle =
		ResourceBundle.getBundle("examples_opengl");
	private TabFolder tabFolder;
	private OpenGLTab[] tabs;
	private int sleep;
	
	public OpenGLExample() {}

	/**
	 * Creates an instance of an OpenGLExample embedded inside
	 * the supplied parent Composite.
	 * 
	 * @param parent the container of the example
	 */
	public OpenGLExample(Composite parent) {
		tabFolder = new TabFolder(parent, SWT.NONE);
		tabs =
			new OpenGLTab[] {
				new AntialiasingTab(),
				new AreaTab(),
				new FogTab(),
				new GradientTab(),
				new LightTab(),
				new NurbTab(),
				new ObjectsTab(),
				new ReflectionTab(),
				new StencilTab(),
				new TextureTab(),
				new TransparencyTab()
			};
				
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
		
		Runnable timer = new Runnable() {
			public void run() {
				if (tabFolder.isDisposed()) return;
				display();
				tabFolder.getDisplay().timerExec(sleep, this);
			}
		};
		timer.run();
		tabFolder.addListener(SWT.Dispose, new Listener() {
			public void handleEvent(Event e) {
				dispose();
			}
		});
	}

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
	 * Gets a string from the resource bundle.
	 * We don't want to crash because of a missing String.
	 * Returns the key if not found.
	 */
	static String getResourceString(String key) {
		try {
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!";
		}			
	}

	/**
	 * Invokes as a standalone program.
	 */
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		OpenGLExample instance = new OpenGLExample(shell);
		shell.setText(getResourceString("window.title"));
		shell.open();
		while (! shell.isDisposed()) {
			if (! display.readAndDispatch()) display.sleep();
		}
		instance.dispose();
	}

	public Shell open (Display display) {
		Shell shell = new Shell (display);
		shell.setLayout(new FillLayout());
		new OpenGLExample(shell);
		shell.setText(getResourceString("window.title"));
		shell.open();
		return shell;
	}
}
