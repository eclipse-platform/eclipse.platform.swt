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
package org.eclipse.swt.awt;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

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

/**
 * This class provides a bridge between SWT and AWT, so that it
 * is possible to embedded AWT components in SWT and vice versa.
 * 
 * @since 3.0
 */
public class SWT_AWT {

	/**
	 * The name of the embedded Frame class. The default class name
	 * for the platform will be used if <code>null</code>. 
	 */
	public static String embeddedFrameClass;

	static boolean loaded, swingInitialized;

static native final int getAWTHandle (Canvas canvas);

static synchronized void loadLibrary () {
	if (loaded) return;
	loaded = true;
	Toolkit.getDefaultToolkit();
	/*
	* Note that the jawt library is loaded explicitily
	* because it cannot be found by the library loader.
	* All exceptions are caught because the library may
	* have been loaded already.
	*/
	try {
		System.loadLibrary("jawt");
	} catch (Throwable e) {}
	Library.loadLibrary("swt-awt");
}

static synchronized void initializeSwing() {
	if (swingInitialized) return;
	swingInitialized = true;
	try {
		/* Initialize the default focus traversal policy */
		Class[] emptyClass = new Class[0];
		Object[] emptyObject = new Object[0];
		Class clazz = Class.forName("javax.swing.UIManager");
		Method method = clazz.getMethod("getDefaults", emptyClass);
		if (method != null) method.invoke(clazz, emptyObject);
	} catch (Throwable e) {}
}

/**
 * Creates a new <code>java.awt.Frame</code>. This frame is the root for
 * the AWT components that will be embedded within the composite. In order
 * for the embedding to succeed, the composite must have been created
 * with the SWT.EMBEDDED style.
 * <p>
 * IMPORTANT: As of JDK1.5, the embedded frame does not receive mouse events.
 * When a lightweight component is added as a child of the embedded frame,
 * the cursor does not change. In order to work around both these problems, it is
 * strongly recommended that a heavyweight component such as <code>java.awt.Panel</code>
 * be added to the frame as the root of all components.
 * </p>
 * 
 * @param parent the parent <code>Composite</code> of the new <code>java.awt.Frame</code>
 * @return a <code>java.awt.Frame</code> to be the parent of the embedded AWT components
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parent Composite does not have the SWT.EMBEDDED style</li> 
 * </ul>
 * 
 * @since 3.0
 */
public static Frame new_Frame (final Composite parent) {
	if (parent == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if ((parent.getStyle () & SWT.EMBEDDED) == 0) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int handle = parent.handle;
	/*
	 * Some JREs have implemented the embedded frame constructor to take an integer
	 * and other JREs take a long.  To handle this binary incompatability, use
	 * reflection to create the embedded frame.
	 */
	Class clazz = null;
	try {
		String className = embeddedFrameClass != null ? embeddedFrameClass : "sun.awt.windows.WEmbeddedFrame";
		clazz = Class.forName(className);
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
	initializeSwing ();
	Object value = null;
	try {
		value = constructor.newInstance (new Object [] {new Integer (handle)});
	} catch (Throwable e) {
		SWT.error (SWT.ERROR_NOT_IMPLEMENTED, e);
	}
	final Frame frame = (Frame) value;
	
	/*
	* This is necessary to make lightweight components
	* directly added to the frame receive mouse events
	* properly.
	*/
	frame.addNotify();
	
	/*
	* Generate the appropriate events to activate and deactivate
	* the embedded frame. This is needed in order to make keyboard
	* focus work properly for lightweights.
	*/
	Listener listener = new Listener () {
		public void handleEvent (Event e) {
			switch (e.type) {
				case SWT.Dispose:
					parent.setVisible(false);
					EventQueue.invokeLater(new Runnable () {
						public void run () {
							frame.dispose ();
						}
					});
					break;
				case SWT.Activate:
					EventQueue.invokeLater(new Runnable () {
						public void run () {
							if (Library.JAVA_VERSION < Library.JAVA_VERSION(1, 4, 0)) {
								frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_ACTIVATED));
								frame.dispatchEvent (new FocusEvent (frame, FocusEvent.FOCUS_GAINED));
							} else {
								frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_ACTIVATED));
								frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_GAINED_FOCUS));
							}
						}
					});
					break;
				case SWT.Deactivate:
					EventQueue.invokeLater(new Runnable () {
						public void run () {
							if (Library.JAVA_VERSION < Library.JAVA_VERSION(1, 4, 0)) {
								frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_DEACTIVATED));
								frame.dispatchEvent (new FocusEvent (frame, FocusEvent.FOCUS_LOST));
							} else {
								frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_LOST_FOCUS));
								frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_DEACTIVATED));
							}
						}
					});
					break;
			}
		}
	};
	parent.addListener (SWT.Activate, listener);
	parent.addListener (SWT.Deactivate, listener);
	parent.addListener (SWT.Dispose, listener);
	
	parent.getDisplay().asyncExec(new Runnable() {
		public void run () {
			if (parent.isDisposed()) return;
			final Rectangle clientArea = parent.getClientArea();
			EventQueue.invokeLater(new Runnable () {
				public void run () {
					frame.setSize (clientArea.width, clientArea.height);
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

/**
 * Creates a new <code>Shell</code>. This Shell is the root for
 * the SWT widgets that will be embedded within the AWT canvas. 
 * 
 * @param display the display for the new Shell
 * @param parent the parent <code>java.awt.Canvas</code> of the new Shell
 * @return a <code>Shell</code> to be the parent of the embedded SWT widgets
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the display is null</li>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parent's peer is not created</li>
 * </ul>
 * 
 * @since 3.0
 */
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
	if (handle == 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT, null, " [peer not created]");

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
