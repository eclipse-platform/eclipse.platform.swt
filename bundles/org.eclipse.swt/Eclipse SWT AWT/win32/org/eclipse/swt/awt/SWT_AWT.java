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
package org.eclipse.swt.awt;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/* SWT Imports */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.internal.Library;

/* AWT Imports */
import java.awt.EventQueue;
import java.awt.Canvas;
import java.awt.Frame;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.FocusEvent;

public class SWT_AWT {

static boolean loaded;
static final boolean JDK1_3;

static {
	JDK1_3 = "1.3".equals(System.getProperty("java.specification.version"));
}

static native final int getAWTHandle (Canvas canvas);

static synchronized void loadLibrary () {
	if (loaded) return;
	loaded = true;
	Toolkit.getDefaultToolkit();
	System.loadLibrary("jawt");
	Library.loadLibrary("swt-awt");
}

public static Frame new_Frame (final Composite parent) {
	if (parent == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	int handle = parent.handle;
	/*
	 * Some JREs have implemented the embedded frame constructor to take an integer
	 * and other JREs take a long.  To handle this binary incompatability, use
	 * reflection to create the embedded frame.
	 */
	Class clazz = null;
	try {
		clazz = Class.forName("sun.awt.windows.WEmbeddedFrame");
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
	final Frame frame = (Frame) value;
	
	/*
	* This is necessary to make lightweigth components
	* directly added to the frame receive mouse events
	* properly.
	*/
	frame.addNotify();
	
	/*
	* Generate the appropriate events to activate and deactivate
	* the embedded frame. This is needed in order to make keyboard
	* focus work properly for lightweights.
	*/
	parent.addListener (SWT.Activate, new Listener () {
		public void handleEvent (Event e) {
			EventQueue.invokeLater(new Runnable () {
				public void run () {
					if (JDK1_3) {
						frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_ACTIVATED));
						frame.dispatchEvent (new FocusEvent (frame, FocusEvent.FOCUS_GAINED));
					} else {
						frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_ACTIVATED));
						frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_GAINED_FOCUS));
					}
				}
			});
		}
	});
	parent.addListener (SWT.Deactivate, new Listener () {
		public void handleEvent (Event e) {
			EventQueue.invokeLater(new Runnable () {
				public void run () {
					if (JDK1_3) {
						frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_DEACTIVATED));
						frame.dispatchEvent (new FocusEvent (frame, FocusEvent.FOCUS_LOST));
					} else {
						frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_LOST_FOCUS));
						frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_DEACTIVATED));
					}
				}
			});
		}
	});

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
			EventQueue.invokeLater(new Runnable () {
				public void run () {
					frame.dispose ();
				}
			});
		}
	});
	parent.getDisplay().asyncExec(new Runnable() {
		public void run () {
			if (parent.isDisposed()) return;
			final Rectangle rect = parent.getClientArea ();
			EventQueue.invokeLater(new Runnable () {
				public void run () {
					frame.setSize (rect.width, rect.height);
					frame.validate ();
				}
			});
		}
	});
	/*
	* TEMPORARY CODE
	* 
	* For some reason, the graphics configuration of the embedded
	* frame is not initialized properly. This causes an exception
	* when the depth of the screen is changed.
	*/
	EventQueue.invokeLater(new Runnable () {
		public void run () {
			try {
				Class clazz = Class.forName("sun.awt.windows.WComponentPeer");
				Field field = clazz.getDeclaredField("winGraphicsConfig");
				field.setAccessible(true);
				field.set(frame.getPeer(), frame.getGraphicsConfiguration());
			} catch (Throwable e) {}
		}
	});
	return frame;
}

public static Shell new_Shell (final Display display, final Canvas parent) {
	if (display == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (parent == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	int handle = 0;
	try {
		loadLibrary ();
		handle = getAWTHandle (parent);
	} catch (Throwable e) {
		SWT.error (SWT.ERROR_NOT_IMPLEMENTED, e);
	}
	if (handle == 0) SWT.error (SWT.ERROR_NOT_IMPLEMENTED);

	final Shell shell = Shell.win32_new (display, handle);
	parent.addComponentListener(new ComponentAdapter () {
		public void componentResized (ComponentEvent e) {
			display.syncExec (new Runnable () {
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
