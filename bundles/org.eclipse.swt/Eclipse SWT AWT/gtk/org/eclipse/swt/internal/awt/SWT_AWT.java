/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.awt;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/* SWT Imports */
import org.eclipse.swt.*;
import org.eclipse.swt.internal.Library;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;

/* AWT Imports */
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Canvas;
import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class SWT_AWT {

	static {
		System.loadLibrary("jawt");
		Library.loadLibrary("swt-awt");
	}

static native final int getAWTHandle (Canvas canvas);

public static Frame new_Frame (final Composite parent) {
	int handle = parent.embeddedHandle;
	/*
	 * Some JREs have implemented the embedded frame constructor to take an integer
	 * and other JREs take a long.  To handle this binary incompatability, use
	 * reflection to create the embedded frame.
	 */
	Class clazz = null;
	try {
		clazz = Class.forName("sun.awt.X11.XEmbeddedFrame");
	} catch (Throwable e) {
		SWT.error (SWT.ERROR_NOT_IMPLEMENTED, e);		
	}
	Constructor constructor = null;
	try {
		constructor = clazz.getConstructor (new Class [] {int.class});
	} catch (Throwable e1) {
		try {
			constructor = clazz.getConstructor (new Class [] {long.class});
		} catch (Throwable e2) {
			SWT.error (SWT.ERROR_NOT_IMPLEMENTED, e2);
		}
	}
	Object value = null;
	try {
		value = constructor.newInstance (new Object [] {new Integer (handle)});
	} catch (Throwable e) {
		SWT.error (SWT.ERROR_NOT_IMPLEMENTED, e);
	}
	try {
		/* Call registerListeners() to make XEmbed focus traversal work */
		Method method = clazz.getMethod("registerListeners", null);
		if (method != null) method.invoke(value, null);
	} catch (Throwable e) {}
	final Frame frame = (Frame) value;
	parent.getShell ().addListener (SWT.Move, new Listener () {
		public void handleEvent (Event e) {
			EventQueue.invokeLater(new Runnable () {
				public void run () {
					frame.dispatchEvent (new ComponentEvent (frame, ComponentEvent.COMPONENT_MOVED));
				}
			});
		}
	});
	parent.addListener (SWT.Dispose, new Listener () {
		public void handleEvent (Event event) {
			parent.setVisible(false);
			frame.dispose ();
		}
	});
	return frame;
}

public static Shell new_Shell (Display display, final Canvas parent) {
	int handle = 0;
	try {
		handle = getAWTHandle (parent);
	} catch (Throwable e) {
		SWT.error (SWT.ERROR_NOT_IMPLEMENTED, e);
	}
	if (handle == 0) SWT.error (SWT.ERROR_NOT_IMPLEMENTED);

	final Shell shell = Shell.gtk_new (display, handle);
	final Display newDisplay = shell.getDisplay ();
	parent.addComponentListener(new ComponentAdapter () {
		public void componentResized (ComponentEvent e) {
			newDisplay.syncExec (new Runnable () {
				public void run () {
					Dimension dim = parent.getSize ();
					shell.setSize (dim.width, dim.height);
				}
			});
		}
	});
	shell.setVisible (true);
	return shell;
}
}
