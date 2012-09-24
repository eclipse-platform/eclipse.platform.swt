/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.graphics.Rectangle;
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
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.FocusEvent;

/**
 * This class provides a bridge between SWT and AWT, so that it
 * is possible to embed AWT components in SWT and vice versa.
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#awt">Swing/AWT snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * 
 * @since 3.0
 */
public class SWT_AWT {

	/**
	 * The name of the embedded Frame class. The default class name
	 * for the platform will be used if <code>null</code>. 
	 */
	public static String embeddedFrameClass;

	/**
	 * Key for looking up the embedded frame for a Composite using
	 * getData(). 
	 */
	static String EMBEDDED_FRAME_KEY = "org.eclipse.swt.awt.SWT_AWT.embeddedFrame";

static boolean loaded, swingInitialized;

static native final long /*int*/ getAWTHandle (Canvas canvas);

static synchronized void loadLibrary () {
	if (loaded) return;
	loaded = true;
	Toolkit.getDefaultToolkit();
	/*
	* Note that the jawt library is loaded explicitly
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
 * Returns a <code>java.awt.Frame</code> which is the embedded frame
 * associated with the specified composite.
 * 
 * @param parent the parent <code>Composite</code> of the <code>java.awt.Frame</code>
 * @return a <code>java.awt.Frame</code> the embedded frame or <code>null</code>.
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * 
 * @since 3.2
 */
public static Frame getFrame (Composite parent) {
	if (parent == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if ((parent.getStyle () & SWT.EMBEDDED) == 0) return null;
	return (Frame)parent.getData(EMBEDDED_FRAME_KEY);
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
	final long /*int*/ handle = parent.handle;
	final Frame[] result = new Frame[1];
	final Throwable[] exception = new Throwable[1];
	Runnable runnable = new Runnable () {
		public void run () {
			try {
				/*
				 * Some JREs have implemented the embedded frame constructor to take an integer
				 * and other JREs take a long.  To handle this binary incompatibility, use
				 * reflection to create the embedded frame.
				 */
				Class clazz = null;
				try {
					String className = embeddedFrameClass != null ? embeddedFrameClass : "sun.awt.windows.WEmbeddedFrame";
					clazz = Class.forName(className);
				} catch (Throwable e) {
					exception[0] = e;
					return;
				}
				initializeSwing ();
				Object value = null;
				Constructor constructor = null;
				try {
					constructor = clazz.getConstructor (new Class [] {int.class});
					value = constructor.newInstance (new Object [] {new Integer ((int)/*64*/handle)});
				} catch (Throwable e1) {
					try {
						constructor = clazz.getConstructor (new Class [] {long.class});
						value = constructor.newInstance (new Object [] {new Long (handle)});
					} catch (Throwable e2) {
						exception[0] = e2;
						return;
					}
				}
				final Frame frame = (Frame) value;
				/*
				 * This is necessary to make lightweight components
				 * directly added to the frame receive mouse events
				 * properly.
				 */
				frame.addNotify();
				
				/*
				* TEMPORARY CODE
				* 
				* For some reason, the graphics configuration of the embedded
				* frame is not initialized properly. This causes an exception
				* when the depth of the screen is changed.
				*/
				try {
					clazz = Class.forName("sun.awt.windows.WComponentPeer");
					Field field = clazz.getDeclaredField("winGraphicsConfig");
					field.setAccessible(true);
					field.set(frame.getPeer(), frame.getGraphicsConfiguration());
				} catch (Throwable e) {}
				
				result[0] = frame;
			} finally {
				synchronized(result) {
					result.notify();
				}
			}
		}
	};
	if (EventQueue.isDispatchThread() || parent.getDisplay().getSyncThread() != null) {
		runnable.run();
	} else {
		EventQueue.invokeLater(runnable);
		OS.ReplyMessage(0);
		boolean interrupted = false;
		MSG msg = new MSG ();
		int flags = OS.PM_NOREMOVE | OS.PM_NOYIELD | OS.PM_QS_SENDMESSAGE;
		while (result[0] == null && exception[0] == null) {
			OS.PeekMessage (msg, 0, 0, 0, flags);
			try {
				synchronized (result) {
					result.wait(50);
				}
			} catch (InterruptedException e) {
				interrupted = true;
			}
		}
		if (interrupted) {
			Compatibility.interrupt();
		}
	}
	if (exception[0] != null) {
		SWT.error (SWT.ERROR_NOT_IMPLEMENTED, exception[0]);
	}
	final Frame frame = result[0];

	parent.setData(EMBEDDED_FRAME_KEY, frame);	
	
	/* Forward the iconify and deiconify events */
	final Listener shellListener = new Listener () {
		public void handleEvent (Event e) {
			switch (e.type) {
				case SWT.Deiconify:
					EventQueue.invokeLater(new Runnable () {
						public void run () {
							frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_DEICONIFIED));
						}
					});
					break;
				case SWT.Iconify:
					EventQueue.invokeLater(new Runnable () {
						public void run () {
							frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_ICONIFIED));
						}
					});
					break;
			}
		}
	};
	Shell shell = parent.getShell ();
	shell.addListener (SWT.Deiconify, shellListener);
	shell.addListener (SWT.Iconify, shellListener);
	
	/*
	* Generate the appropriate events to activate and deactivate
	* the embedded frame. This is needed in order to make keyboard
	* focus work properly for lightweights.
	*/
	Listener listener = new Listener () {
		public void handleEvent (Event e) {
			switch (e.type) {
				case SWT.Dispose:
					Shell shell = parent.getShell ();
					shell.removeListener (SWT.Deiconify, shellListener);
					shell.removeListener (SWT.Iconify, shellListener);
					parent.setVisible(false);
					EventQueue.invokeLater(new Runnable () {
						public void run () {
							try {
								frame.dispose ();
							} catch (Throwable e) {}
						}
					});
					break;
				case SWT.FocusIn:
				case SWT.Activate:
					EventQueue.invokeLater(new Runnable () {
						public void run () {
							if (Library.JAVA_VERSION < Library.JAVA_VERSION(1, 4, 0)) {
								frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_ACTIVATED));
								frame.dispatchEvent (new FocusEvent (frame, FocusEvent.FOCUS_GAINED));
							} else if (Library.JAVA_VERSION < Library.JAVA_VERSION(1, 5, 0)) {
								frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_ACTIVATED));
								frame.dispatchEvent (new WindowEvent (frame, 207 /*WindowEvent.WINDOW_GAINED_FOCUS*/));
							} else {
								if (frame.isActive()) return;
								try {
									Class clazz = frame.getClass();
									Method method = clazz.getMethod("synthesizeWindowActivation", new Class[]{boolean.class});
									if (method != null) method.invoke(frame, new Object[]{new Boolean(true)});
								} catch (Throwable e) {}
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
							} else if (Library.JAVA_VERSION < Library.JAVA_VERSION(1, 5, 0)) {
								frame.dispatchEvent (new WindowEvent (frame, 208 /*WindowEvent.WINDOW_LOST_FOCUS*/));
								frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_DEACTIVATED));
							} else {
								if (!frame.isActive()) return;
								try {
									Class clazz = frame.getClass();
									Method method = clazz.getMethod("synthesizeWindowActivation", new Class[]{boolean.class});
									if (method != null) method.invoke(frame, new Object[]{new Boolean(false)});
								} catch (Throwable e) {}
							}
						}
					});
					break;
			}
		}
	};
	if (Library.JAVA_VERSION < Library.JAVA_VERSION(1, 5, 0)) {
		parent.addListener (SWT.Activate, listener);
	} else {
		parent.addListener (SWT.FocusIn, listener);
	}
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
	long /*int*/ handle = 0;
	try {
		loadLibrary ();
		handle = getAWTHandle (parent);
	} catch (Throwable e) {
		SWT.error (SWT.ERROR_NOT_IMPLEMENTED, e);
	}
	if (handle == 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT, null, " [peer not created]");

	final Shell shell = Shell.win32_new (display, handle);
	final ComponentListener listener = new ComponentAdapter () {
		public void componentResized (ComponentEvent e) {
			display.syncExec (new Runnable () {
				public void run () {
					if (shell.isDisposed()) return;
					Dimension dim = parent.getSize ();
					shell.setSize (dim.width, dim.height);
				}
			});
		}
	};
	parent.addComponentListener(listener);
	shell.addListener(SWT.Dispose, new Listener() {
		public void handleEvent(Event event) {
			parent.removeComponentListener(listener);
		}
	});
	shell.setVisible (true);
	return shell;
}
}
